package com.zyc.zdh.job;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.fastjson.JSON;
import com.hubspot.jinjava.Jinjava;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.SpringContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.DigestUtils;

import java.sql.Timestamp;
import java.util.*;

public class CheckBloodSourceJob {
    private static Logger logger = LoggerFactory.getLogger(CheckBloodSourceJob.class);


    public static void Check() {
        try {
            BloodSourceMapper bloodSourceMapper = (BloodSourceMapper) SpringContext.getBean("bloodSourceMapper");
            String version = DateUtil.getCurrentTime();
            List<BloodSourceInfo> bsis = check_etl_blood_source(version);
            List<BloodSourceInfo> bsis2 = check_more_etl_blood_source(version);
            List<BloodSourceInfo> bsis3 = check_sql_blood_source(version);

            bsis.addAll(bsis2);
            bsis.addAll(bsis3);


            for (BloodSourceInfo bsi : bsis) {
                if (!StringUtils.isEmpty(bsi.getInput()))
                    bloodSourceMapper.insert(bsi);
            }
        } catch (Exception e) {

             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常:"+e.getMessage());
        }
    }


    public static BloodSourceInfo resove(EtlTaskInfo eti, String version) {
        BloodSourceInfo bsi = new BloodSourceInfo();
        try {
            EtlTaskMapper etlTaskMapper = (EtlTaskMapper) SpringContext.getBean("etlTaskMapper");
            DataSourcesMapper dataSourcesMapper = (DataSourcesMapper) SpringContext.getBean("dataSourcesMapper");
            bsi.setContext(eti.getEtl_context());
            bsi.setOwner(eti.getOwner());
            bsi.setCreate_time(new Date());
            bsi.setInput_type(eti.getData_source_type_input());
            DataSourcesInfo dsi_input = dataSourcesMapper.selectByPrimaryKey(eti.getData_sources_choose_input());
            String md5 = DigestUtils.md5DigestAsHex((dsi_input.getData_source_type() + dsi_input.getUrl()).getBytes());
            bsi.setInput_md5(md5);
            bsi.setInput(dsi_input.getData_source_type().equalsIgnoreCase("jdbc") ? eti.getData_sources_table_name_input() : eti.getData_sources_file_name_input());

            bsi.setOutput_type(eti.getData_source_type_output());
            DataSourcesInfo dsi_output = dataSourcesMapper.selectByPrimaryKey(eti.getData_sources_choose_output());
            String md5_output = DigestUtils.md5DigestAsHex((dsi_output.getData_source_type() + dsi_output.getUrl()).getBytes());
            bsi.setOutput_md5(md5_output);
            bsi.setOutput(dsi_output.getData_source_type().equalsIgnoreCase("jdbc") ? eti.getData_sources_table_name_output() : eti.getData_sources_file_name_output());

            bsi.setVersion(version);

        } catch (Exception e) {
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常:"+e.getMessage());
        }


        return bsi;

    }

    /**
     * 检查单源ETL的血源
     */
    public static List<BloodSourceInfo> check_etl_blood_source(String version) {

        EtlTaskMapper etlTaskMapper = (EtlTaskMapper) SpringContext.getBean("etlTaskMapper");

        List<EtlTaskInfo> etlTaskInfoList = etlTaskMapper.selectAll();

        List<BloodSourceInfo> bsis = new ArrayList<>();
        for (EtlTaskInfo eti : etlTaskInfoList) {
            bsis.add(resove(eti, version));
        }

        return bsis;
    }


    /**
     * 多源ETL
     */
    public static List<BloodSourceInfo> check_more_etl_blood_source(String version) {

        EtlMoreTaskMapper etlMoreTaskMapper = (EtlMoreTaskMapper) SpringContext.getBean("etlMoreTaskMapper");
        EtlTaskMapper etlTaskMapper = (EtlTaskMapper) SpringContext.getBean("etlTaskMapper");
        DataSourcesMapper dataSourcesMapper = (DataSourcesMapper) SpringContext.getBean("dataSourcesMapper");

        List<EtlMoreTaskInfo> etlMoreTaskInfoList = etlMoreTaskMapper.selectAll();

        List<BloodSourceInfo> bsis = new ArrayList<>();
        for (EtlMoreTaskInfo emti : etlMoreTaskInfoList) {
            List<EtlTaskInfo> etlTaskInfoList = etlTaskMapper.selectByIds(emti.getEtl_ids().split(","));

            for (EtlTaskInfo eti : etlTaskInfoList) {
                BloodSourceInfo bsi = new BloodSourceInfo();
                bsi.setContext(eti.getEtl_context());
                bsi.setOwner(eti.getOwner());
                bsi.setCreate_time(new Date());
                bsi.setInput_type(eti.getData_source_type_input());
                DataSourcesInfo dsi_input = dataSourcesMapper.selectByPrimaryKey(eti.getData_sources_choose_input());
                String md5 = DigestUtils.md5DigestAsHex((dsi_input.getData_source_type() + dsi_input.getUrl()).getBytes());
                bsi.setInput_md5(md5);
                bsi.setInput(dsi_input.getData_source_type().equalsIgnoreCase("jdbc") ? eti.getData_sources_table_name_input() : eti.getData_sources_file_name_input());

                bsi.setOutput_type(emti.getData_source_type_output());
                DataSourcesInfo dsi_output = dataSourcesMapper.selectByPrimaryKey(emti.getData_sources_choose_output());
                String md5_output = DigestUtils.md5DigestAsHex((dsi_output.getData_source_type() + dsi_output.getUrl()).getBytes());
                bsi.setOutput_md5(md5_output);
                bsi.setOutput(dsi_output.getData_source_type().equalsIgnoreCase("jdbc") ? emti.getData_sources_table_name_output() : emti.getData_sources_file_name_output());

                bsi.setVersion(version);

                bsis.add(bsi);
            }
        }
        return bsis;
    }

    public static List<BloodSourceInfo> check_sql_blood_source(String version) {
        BloodSourceMapper bloodSourceMappeer = (BloodSourceMapper) SpringContext.getBean("bloodSourceMapper");
        EtlTaskJdbcMapper etlTaskJdbcMapper = (EtlTaskJdbcMapper) SpringContext.getBean("etlTaskJdbcMapper");
        DataSourcesMapper dataSourcesMapper = (DataSourcesMapper) SpringContext.getBean("dataSourcesMapper");
        List<EtlTaskJdbcInfo> etlTaskJdbcInfos = etlTaskJdbcMapper.selectAll();
        List<BloodSourceInfo> bsis = new ArrayList<>();
        for (EtlTaskJdbcInfo etlTaskJdbcInfo : etlTaskJdbcInfos) {
            ArrayList input_tables = new ArrayList<String>();
            ArrayList output_tables = new ArrayList<String>();
            DataSourcesInfo ds = dataSourcesMapper.selectByPrimaryKey(etlTaskJdbcInfo.getData_sources_choose_input());
            String dbType = JdbcUtils.getDbType(ds.getUrl(), ds.getDriver());
            Map<String, Object> jinJavaParam = getJinJavaParam(new Timestamp(new Date().getTime()));
            Jinjava jj = new Jinjava();
            String etl_sql = jj.render(etlTaskJdbcInfo.getEtl_sql(), jinJavaParam);
            String[] sqls = etl_sql.split("\r\n|\n");
            for (String sql : sqls) {
                System.out.println(sql);
                System.out.println("======");
                try {
                    List<SQLStatement> sqlStatementList = SQLUtils.parseStatements(sql, dbType);
                    SQLStatement stmt = sqlStatementList.get(0);
                    SchemaStatVisitor ssv = SQLUtils.createSchemaStatVisitor(dbType);
                    stmt.accept(ssv);
                    for (Map.Entry<TableStat.Name, TableStat> entry : ssv.getTables().entrySet()) {

                        String table_name = entry.getKey().getName();
                        if (entry.getValue().getInsertCount() > 0 || entry.getValue().getUpdateCount() > 0 || entry.getValue().getDeleteCount() > 0 || entry.getValue().getMergeCount() > 0) {
                            output_tables.add(table_name);
                        }
                        if (entry.getValue().getSelectCount() > 0 || entry.getValue().getCreateCount() > 0) {
                            input_tables.add(table_name);
                        }

                    }

                    BloodSourceInfo bsi = new BloodSourceInfo();
                    bsi.setContext(etlTaskJdbcInfo.getEtl_context());
                    bsi.setOwner(etlTaskJdbcInfo.getOwner());
                    bsi.setCreate_time(new Date());
                    bsi.setInput_type(etlTaskJdbcInfo.getData_source_type_input());
                    DataSourcesInfo dsi_input = dataSourcesMapper.selectByPrimaryKey(etlTaskJdbcInfo.getData_sources_choose_input());
                    String md5 = DigestUtils.md5DigestAsHex((dsi_input.getData_source_type() + dsi_input.getUrl()).getBytes());
                    bsi.setInput_md5(md5);
                    bsi.setInput(StringUtils.join(input_tables, ","));

                    bsi.setOutput_md5(md5);
                    bsi.setOutput_type(etlTaskJdbcInfo.getData_source_type_input());
                    bsi.setOutput(StringUtils.join(output_tables, ","));
                    bsi.setVersion(version);
                    bloodSourceMappeer.insert(bsi);
                    bsis.add(bsi);
                } catch (Exception e) {
                     logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常:"+e.getMessage());
                    continue;
                }

            }

        }

        return bsis;

    }

    public static List<BloodSourceInfo> check_spark_sql_blood_source(String version) {
        BloodSourceMapper bloodSourceMappeer = (BloodSourceMapper) SpringContext.getBean("bloodSourceMapper");
        SqlTaskMapper sqlTaskMapper = (SqlTaskMapper) SpringContext.getBean("sqlTaskMapper");
        DataSourcesMapper dataSourcesMapper = (DataSourcesMapper) SpringContext.getBean("dataSourcesMapper");
        List<SqlTaskInfo> sqlTaskInfos = sqlTaskMapper.selectAll();
        List<BloodSourceInfo> bsis = new ArrayList<>();
        for (SqlTaskInfo sqlTaskInfo : sqlTaskInfos) {
            ArrayList input_tables = new ArrayList<String>();
            ArrayList output_tables = new ArrayList<String>();
            DataSourcesInfo ds = dataSourcesMapper.selectByPrimaryKey(sqlTaskInfo.getData_sources_choose_input());
            String dbType = JdbcUtils.getDbType(ds.getUrl(), ds.getDriver());
            String[] sqls = sqlTaskInfo.getEtl_sql().split("\r\n|\n");
            for (String sql : sqls) {
                List<SQLStatement> sqlStatementList = SQLUtils.parseStatements(sql, dbType);
                SQLStatement stmt = sqlStatementList.get(0);
                SchemaStatVisitor ssv = SQLUtils.createSchemaStatVisitor(dbType);
                stmt.accept(ssv);
                for (Map.Entry<TableStat.Name, TableStat> entry : ssv.getTables().entrySet()) {

                    String table_name = entry.getKey().getName();
                    if (entry.getValue().getInsertCount() > 0 || entry.getValue().getUpdateCount() > 0 || entry.getValue().getDeleteCount() > 0 || entry.getValue().getMergeCount() > 0) {
                        output_tables.add(table_name);
                    }
                    if (entry.getValue().getSelectCount() > 0 || entry.getValue().getCreateCount() > 0) {
                        input_tables.add(table_name);
                    }

                }

                BloodSourceInfo bsi = new BloodSourceInfo();
                bsi.setContext(sqlTaskInfo.getSql_context());
                bsi.setOwner(sqlTaskInfo.getOwner());
                bsi.setCreate_time(new Date());
                bsi.setInput_type(sqlTaskInfo.getData_source_type_input());
                DataSourcesInfo dsi_input = dataSourcesMapper.selectByPrimaryKey(sqlTaskInfo.getData_sources_choose_input());
                String md5 = DigestUtils.md5DigestAsHex((dsi_input.getData_source_type() + dsi_input.getUrl()).getBytes());
                bsi.setInput_md5(md5);
                bsi.setInput(StringUtils.join(input_tables, ","));

                DataSourcesInfo dsi_output = dataSourcesMapper.selectByPrimaryKey(sqlTaskInfo.getData_sources_choose_output());
                String md5_output = DigestUtils.md5DigestAsHex((dsi_output.getData_source_type() + dsi_output.getUrl()).getBytes());

                bsi.setOutput_md5(md5_output);
                bsi.setOutput_type(sqlTaskInfo.getData_sources_choose_output());
                bsi.setOutput(StringUtils.join(output_tables, ","));
                bsi.setVersion(version);
                bloodSourceMappeer.insert(bsi);
                bsis.add(bsi);
            }

        }

        return bsis;

    }

    public static Map<String, Object> getJinJavaParam(Timestamp timestamp){
        Timestamp cur_time=timestamp;
        String date_nodash = DateUtil.formatNodash(cur_time);
        String date_time = DateUtil.formatTime(cur_time);
        String date_dt = DateUtil.format(cur_time);
        Map<String, Object> jinJavaParam = new HashMap<>();
        jinJavaParam.put("zdh_date_nodash", date_nodash);
        jinJavaParam.put("zdh_date_time", date_time);
        jinJavaParam.put("zdh_date", date_dt);
        jinJavaParam.put("zdh_year",DateUtil.year(cur_time));
        jinJavaParam.put("zdh_month",DateUtil.month(cur_time));
        jinJavaParam.put("zdh_day",DateUtil.day(cur_time));
        jinJavaParam.put("zdh_hour",DateUtil.hour(cur_time));
        jinJavaParam.put("zdh_minute",DateUtil.minute(cur_time));
        jinJavaParam.put("zdh_second",DateUtil.second(cur_time));
        jinJavaParam.put("zdh_time",cur_time.getTime());
        return jinJavaParam;
    }
}
