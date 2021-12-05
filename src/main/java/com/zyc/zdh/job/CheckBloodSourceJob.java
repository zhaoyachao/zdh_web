package com.zyc.zdh.job;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.fastjson.JSON;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.SpringContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.util.*;

public class CheckBloodSourceJob {
    private static Logger logger = LoggerFactory.getLogger(CheckBloodSourceJob.class);


    public static BloodSourceInfo resove(EtlTaskInfo eti, String version){
        EtlTaskMapper etlTaskMapper=(EtlTaskMapper) SpringContext.getBean("etlTaskMapper");
        DataSourcesMapper dataSourcesMapper=(DataSourcesMapper) SpringContext.getBean("dataSourcesMapper");
        BloodSourceInfo bsi=new BloodSourceInfo();
        bsi.setContext(eti.getEtl_context());
        bsi.setOwner(eti.getOwner());
        bsi.setCreate_time(new Date());
        bsi.setInput_type(eti.getData_source_type_input());
        DataSourcesInfo dsi_input=dataSourcesMapper.selectByPrimaryKey(eti.getData_sources_choose_input());
        String md5=DigestUtils.md5DigestAsHex((dsi_input.getData_source_type()+dsi_input.getUrl()).getBytes());
        bsi.setInput_md5(md5);
        bsi.setInput(dsi_input.getData_source_type().equalsIgnoreCase("jdbc")?eti.getData_sources_table_name_input():eti.getData_sources_file_name_input());

        bsi.setOutput(eti.getData_source_type_output());
        DataSourcesInfo dsi_output=dataSourcesMapper.selectByPrimaryKey(eti.getData_sources_choose_output());
        String md5_output=DigestUtils.md5DigestAsHex((dsi_output.getData_source_type()+dsi_output.getUrl()).getBytes());
        bsi.setOutput_md5(md5_output);
        bsi.setOutput(dsi_output.getData_source_type().equalsIgnoreCase("jdbc")?eti.getData_sources_table_name_output():eti.getData_sources_file_name_output());

        bsi.setVersion(version);

        return bsi;

    }

    /**
     * 检查单源ETL的血源
     */
    public static void check_etl_blood_source(String version){

        EtlTaskMapper etlTaskMapper=(EtlTaskMapper) SpringContext.getBean("etlTaskMapper");

        List<EtlTaskInfo> etlTaskInfoList=etlTaskMapper.selectAll();

        List<BloodSourceInfo> bsis=new ArrayList<>();
        for (EtlTaskInfo eti:etlTaskInfoList){
            bsis.add(resove(eti,version));
        }
    }


    /**
     * 多源ETL
     */
    public static void check_more_etl_blood_source(String version){

        EtlMoreTaskMapper etlMoreTaskMapper=(EtlMoreTaskMapper) SpringContext.getBean("etlMoreTaskMapper");
        EtlTaskMapper etlTaskMapper=(EtlTaskMapper) SpringContext.getBean("etlTaskMapper");
        DataSourcesMapper dataSourcesMapper=(DataSourcesMapper) SpringContext.getBean("dataSourcesMapper");

        List<EtlMoreTaskInfo> etlMoreTaskInfoList=etlMoreTaskMapper.selectAll();

        List<BloodSourceInfo> bsis=new ArrayList<>();
        for (EtlMoreTaskInfo emti:etlMoreTaskInfoList){
            List<EtlTaskInfo> etlTaskInfoList=etlTaskMapper.selectByIds(emti.getEtl_ids().split(","));

            for (EtlTaskInfo eti:etlTaskInfoList){
                BloodSourceInfo bsi=new BloodSourceInfo();
                bsi.setContext(eti.getEtl_context());
                bsi.setOwner(eti.getOwner());
                bsi.setCreate_time(new Date());
                bsi.setInput_type(eti.getData_source_type_input());
                DataSourcesInfo dsi_input=dataSourcesMapper.selectByPrimaryKey(eti.getData_sources_choose_input());
                String md5=DigestUtils.md5DigestAsHex((dsi_input.getData_source_type()+dsi_input.getUrl()).getBytes());
                bsi.setInput_md5(md5);
                bsi.setInput(dsi_input.getData_source_type().equalsIgnoreCase("jdbc")?eti.getData_sources_table_name_input():eti.getData_sources_file_name_input());

                bsi.setOutput(emti.getData_source_type_output());
                DataSourcesInfo dsi_output=dataSourcesMapper.selectByPrimaryKey(emti.getData_sources_choose_output());
                String md5_output=DigestUtils.md5DigestAsHex((dsi_output.getData_source_type()+dsi_output.getUrl()).getBytes());
                bsi.setOutput_md5(md5_output);
                bsi.setOutput(dsi_output.getData_source_type().equalsIgnoreCase("jdbc")?emti.getData_sources_table_name_output():emti.getData_sources_file_name_output());

                bsi.setVersion(version);

                bsis.add(bsi);
            }
        }
    }

    public static void check_sql_blood_source(String version){
        BloodSourceMapper bloodSourceMappeer=(BloodSourceMapper) SpringContext.getBean("bloodSourceMapper");
        EtlTaskJdbcMapper etlTaskJdbcMapper=(EtlTaskJdbcMapper) SpringContext.getBean("etlTaskJdbcMapper");
        DataSourcesMapper dataSourcesMapper=(DataSourcesMapper) SpringContext.getBean("dataSourcesMapper");
        List<EtlTaskJdbcInfo> etlTaskJdbcInfos=etlTaskJdbcMapper.selectAll();
        List<BloodSourceInfo> bsis=new ArrayList<>();
        for (EtlTaskJdbcInfo etlTaskJdbcInfo: etlTaskJdbcInfos){
            ArrayList input_tables = new ArrayList<String>();
            ArrayList output_tables = new ArrayList<String>();
            DataSourcesInfo ds =dataSourcesMapper.selectByPrimaryKey(etlTaskJdbcInfo.getData_sources_choose_input());
            String dbType=JdbcUtils.getDbType(ds.getUrl(),ds.getDriver());
            String[] sqls = etlTaskJdbcInfo.getEtl_sql().split("\r\n|\n");
            for (String sql : sqls){
                List<SQLStatement> sqlStatementList =SQLUtils.parseStatements(sql, dbType);
                SQLStatement stmt = sqlStatementList.get(0);
                SchemaStatVisitor ssv =SQLUtils.createSchemaStatVisitor(dbType);
                stmt.accept(ssv);
                for(Map.Entry<TableStat.Name, TableStat> entry:ssv.getTables().entrySet()){

                    String table_name = entry.getKey().getName();
                    if(entry.getValue().getInsertCount()>0 || entry.getValue().getUpdateCount()>0 || entry.getValue().getDeleteCount()>0||entry.getValue().getMergeCount()>0){
                        output_tables.add(table_name);
                    }
                    if(entry.getValue().getSelectCount()>0 || entry.getValue().getCreateCount()>0){
                        input_tables.add(table_name);
                    }

                }

                BloodSourceInfo bsi=new BloodSourceInfo();
                bsi.setContext(etlTaskJdbcInfo.getEtl_context());
                bsi.setOwner(etlTaskJdbcInfo.getOwner());
                bsi.setCreate_time(new Date());
                bsi.setInput_type(etlTaskJdbcInfo.getData_source_type_input());
                DataSourcesInfo dsi_input=dataSourcesMapper.selectByPrimaryKey(etlTaskJdbcInfo.getData_sources_choose_input());
                String md5=DigestUtils.md5DigestAsHex((dsi_input.getData_source_type()+dsi_input.getUrl()).getBytes());
                bsi.setInput_md5(md5);
                bsi.setInput(StringUtils.join(input_tables,","));

                bsi.setOutput(etlTaskJdbcInfo.getData_source_type_input());
                bsi.setOutput_md5(md5);
                bsi.setOutput_type(etlTaskJdbcInfo.getData_source_type_input());
                bsi.setOutput(StringUtils.join(output_tables,","));
                bsi.setVersion(version);
                bloodSourceMappeer.insert(bsi);
                bsis.add(bsi);
            }

        }

    }
}
