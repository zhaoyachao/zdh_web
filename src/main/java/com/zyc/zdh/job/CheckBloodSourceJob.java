package com.zyc.zdh.job;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
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
import org.springframework.util.DigestUtils;

import java.sql.Timestamp;
import java.util.*;

public class CheckBloodSourceJob {
    private static Logger logger = LoggerFactory.getLogger(CheckBloodSourceJob.class);


    public static void Check(String product_code) {
        try {
            BloodSourceMapper bloodSourceMapper = (BloodSourceMapper) SpringContext.getBean("bloodSourceMapper");
            String version = DateUtil.getCurrentTime();
            List<BloodSourceInfo> bsis = check_etl_blood_source(version, product_code);
            List<BloodSourceInfo> bsis2 = check_more_etl_blood_source(version, product_code);
            List<BloodSourceInfo> bsis3 = check_sql_blood_source(version, product_code);
            List<BloodSourceInfo> bsis4 = check_spark_sql_blood_source(version, product_code);

            bsis.addAll(bsis2);
            bsis.addAll(bsis3);
            bsis.addAll(bsis4);

            for (BloodSourceInfo bsi : bsis) {
                if (!StringUtils.isEmpty(bsi.getInput())&& !StringUtils.isEmpty(bsi.getOutput_type())){
                    System.out.println("Blood: "+JSON.toJSONString(bsi));
                    bloodSourceMapper.insertSelective(bsi);
                }

            }
        } catch (Exception e) {
            logger.error("{}",e);
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
        }
    }


    public static BloodSourceInfo resove(EtlTaskInfo eti, String version) {
        BloodSourceInfo bsi = new BloodSourceInfo();
        try {
            EtlTaskMapper etlTaskMapper = (EtlTaskMapper) SpringContext.getBean("etlTaskMapper");
            DataSourcesMapper dataSourcesMapper = (DataSourcesMapper) SpringContext.getBean("dataSourcesMapper");
            bsi.setProduct_code(eti.getProduct_code());
            bsi.setContext(eti.getEtl_context());
            bsi.setOwner(eti.getOwner());
            bsi.setCreate_time(new Date());
            bsi.setInput_type(eti.getData_source_type_input());
            DataSourcesInfo dsi_input = dataSourcesMapper.selectByPrimaryKey(eti.getData_sources_choose_input());
            String md5 = DigestUtils.md5DigestAsHex((dsi_input.getData_source_type() + dsi_input.getUrl()).getBytes());
            dsi_input.setPassword("");
            bsi.setInput_json(JSON.toJSONString(dsi_input));
            bsi.setInput_md5(md5);
            bsi.setInput(dsi_input.getData_source_type().equalsIgnoreCase("jdbc") ? eti.getData_sources_table_name_input() : eti.getData_sources_file_name_input());

            bsi.setOutput_type(eti.getData_source_type_output());
            DataSourcesInfo dsi_output = dataSourcesMapper.selectByPrimaryKey(eti.getData_sources_choose_output());
            if(dsi_output==null){
                return bsi;
            }
            String md5_output = DigestUtils.md5DigestAsHex((dsi_output.getData_source_type() + dsi_output.getUrl()).getBytes());
            bsi.setOutput_md5(md5_output);
            dsi_output.setPassword("");
            bsi.setOutput_json(JSON.toJSONString(dsi_output));
            bsi.setOutput(dsi_output.getData_source_type().equalsIgnoreCase("jdbc") ? eti.getData_sources_table_name_output() : eti.getData_sources_file_name_output());

            bsi.setVersion(version);

        } catch (Exception e) {
            logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
        }


        return bsi;

    }

    /**
     * 检查单源ETL的血源
     */
    public static List<BloodSourceInfo> check_etl_blood_source(String version, String product_code) {

        EtlTaskMapper etlTaskMapper = (EtlTaskMapper) SpringContext.getBean("etlTaskMapper");

        List<EtlTaskInfo> etlTaskInfoList = etlTaskMapper.selectAll();

        List<BloodSourceInfo> bsis = new ArrayList<>();
        for (EtlTaskInfo eti : etlTaskInfoList) {
            if(!eti.getProduct_code().equalsIgnoreCase(product_code)){
                continue;
            }
            bsis.add(resove(eti, version));
        }

        return bsis;
    }


    /**
     * 多源ETL
     */
    public static List<BloodSourceInfo> check_more_etl_blood_source(String version, String product_code) {

        EtlMoreTaskMapper etlMoreTaskMapper = (EtlMoreTaskMapper) SpringContext.getBean("etlMoreTaskMapper");
        EtlTaskMapper etlTaskMapper = (EtlTaskMapper) SpringContext.getBean("etlTaskMapper");
        DataSourcesMapper dataSourcesMapper = (DataSourcesMapper) SpringContext.getBean("dataSourcesMapper");

        List<EtlMoreTaskInfo> etlMoreTaskInfoList = etlMoreTaskMapper.selectAll();

        List<BloodSourceInfo> bsis = new ArrayList<>();
        for (EtlMoreTaskInfo emti : etlMoreTaskInfoList) {
            if(!emti.getProduct_code().equalsIgnoreCase(product_code)){
                continue;
            }
            List<EtlTaskInfo> etlTaskInfoList = etlTaskMapper.selectByIds(emti.getEtl_ids().split(","));

            for (EtlTaskInfo eti : etlTaskInfoList) {
                BloodSourceInfo bsi = new BloodSourceInfo();
                bsi.setProduct_code(eti.getProduct_code());
                bsi.setContext(eti.getEtl_context());
                bsi.setOwner(eti.getOwner());
                bsi.setCreate_time(new Date());
                bsi.setInput_type(eti.getData_source_type_input());
                DataSourcesInfo dsi_input = dataSourcesMapper.selectByPrimaryKey(eti.getData_sources_choose_input());
                String md5 = DigestUtils.md5DigestAsHex((dsi_input.getData_source_type() + dsi_input.getUrl()).getBytes());
                bsi.setInput_md5(md5);
                dsi_input.setPassword("");
                bsi.setInput_json(JSON.toJSONString(dsi_input));
                bsi.setInput(dsi_input.getData_source_type().equalsIgnoreCase("jdbc") ? eti.getData_sources_table_name_input() : eti.getData_sources_file_name_input());

                bsi.setOutput_type(emti.getData_source_type_output());
                DataSourcesInfo dsi_output = dataSourcesMapper.selectByPrimaryKey(emti.getData_sources_choose_output());
                String md5_output = DigestUtils.md5DigestAsHex((dsi_output.getData_source_type() + dsi_output.getUrl()).getBytes());
                bsi.setOutput_md5(md5_output);
                dsi_output.setPassword("");
                bsi.setOutput_json(JSON.toJSONString(dsi_output));
                bsi.setOutput(dsi_output.getData_source_type().equalsIgnoreCase("jdbc") ? emti.getData_sources_table_name_output() : emti.getData_sources_file_name_output());

                bsi.setVersion(version);

                bsis.add(bsi);
            }
        }
        return bsis;
    }

    public static List<BloodSourceInfo> check_sql_blood_source(String version, String product_code) {
        BloodSourceMapper bloodSourceMappeer = (BloodSourceMapper) SpringContext.getBean("bloodSourceMapper");
        EtlTaskJdbcMapper etlTaskJdbcMapper = (EtlTaskJdbcMapper) SpringContext.getBean("etlTaskJdbcMapper");
        DataSourcesMapper dataSourcesMapper = (DataSourcesMapper) SpringContext.getBean("dataSourcesMapper");
        List<EtlTaskJdbcInfo> etlTaskJdbcInfos = etlTaskJdbcMapper.selectAll();
        List<BloodSourceInfo> bsis = new ArrayList<>();
        for (EtlTaskJdbcInfo etlTaskJdbcInfo : etlTaskJdbcInfos) {
            if(!etlTaskJdbcInfo.getProduct_code().equalsIgnoreCase(product_code)){
                continue;
            }
            ArrayList input_tables = new ArrayList<String>();
            ArrayList output_tables = new ArrayList<String>();
            DataSourcesInfo ds = dataSourcesMapper.selectByPrimaryKey(etlTaskJdbcInfo.getData_sources_choose_input());
            String dbType = JdbcUtils.getDbType(ds.getUrl(), ds.getDriver());
            Map<String, Object> jinJavaParam = getJinJavaParam(new Timestamp(System.currentTimeMillis()));
            Jinjava jj = new Jinjava();
            String etl_sql = jj.render(etlTaskJdbcInfo.getEtl_sql(), jinJavaParam);
            String[] sqls = etl_sql.split(";\r\n|;\n");
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
                    bsi.setProduct_code(etlTaskJdbcInfo.getProduct_code());
                    bsi.setContext(etlTaskJdbcInfo.getEtl_context());
                    bsi.setOwner(etlTaskJdbcInfo.getOwner());
                    bsi.setCreate_time(new Date());
                    bsi.setInput_type(etlTaskJdbcInfo.getData_source_type_input());
                    DataSourcesInfo dsi_input = dataSourcesMapper.selectByPrimaryKey(etlTaskJdbcInfo.getData_sources_choose_input());
                    String md5 = DigestUtils.md5DigestAsHex((dsi_input.getData_source_type() + dsi_input.getUrl()).getBytes());
                    bsi.setInput_md5(md5);
                    dsi_input.setPassword("");
                    bsi.setInput_json(JSON.toJSONString(dsi_input));
                    bsi.setInput(StringUtils.join(input_tables, ","));

                    bsi.setOutput_md5(md5);
                    dsi_input.setPassword("");
                    bsi.setOutput_json(JSON.toJSONString(dsi_input));
                    bsi.setOutput_type(etlTaskJdbcInfo.getData_source_type_input());
                    bsi.setOutput(StringUtils.join(output_tables, ","));
                    bsi.setVersion(version);
                    bloodSourceMappeer.insertSelective(bsi);
                    bsis.add(bsi);
                } catch (Exception e) {
                     logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
                    continue;
                }
            }
        }
        return bsis;
    }

    public static List<BloodSourceInfo> check_spark_sql_blood_source(String version, String product_code) {
        BloodSourceMapper bloodSourceMappeer = (BloodSourceMapper) SpringContext.getBean("bloodSourceMapper");
        SqlTaskMapper sqlTaskMapper = (SqlTaskMapper) SpringContext.getBean("sqlTaskMapper");
        DataSourcesMapper dataSourcesMapper = (DataSourcesMapper) SpringContext.getBean("dataSourcesMapper");
        List<SqlTaskInfo> sqlTaskInfos = sqlTaskMapper.selectAll();
        List<BloodSourceInfo> bsis = new ArrayList<>();
        for (SqlTaskInfo sqlTaskInfo : sqlTaskInfos) {
            if(!sqlTaskInfo.getProduct_code().equalsIgnoreCase(product_code)){
                continue;
            }
            ArrayList input_tables = new ArrayList<String>();
            ArrayList output_tables = new ArrayList<String>();
            DataSourcesInfo ds = new DataSourcesInfo();//dataSourcesMapper.selectByPrimaryKey(sqlTaskInfo.getData_sources_choose_input());
            ds.setData_source_type("HIVE");
            ds.setData_source_context("默认HIVE数据源");
            ds.setUrl("");

            String dbType = "hive";//JdbcUtils.getDbType(ds.getUrl(), ds.getDriver());
            String[] sqls = sqlTaskInfo.getEtl_sql().split(";\r\n|;\n");
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
                bsi.setProduct_code(sqlTaskInfo.getProduct_code());
                bsi.setContext(sqlTaskInfo.getSql_context());
                bsi.setOwner(sqlTaskInfo.getOwner());
                bsi.setCreate_time(new Date());
                bsi.setInput_type(ds.getData_source_type());
                //DataSourcesInfo dsi_input = dataSourcesMapper.selectByPrimaryKey(sqlTaskInfo.getData_sources_choose_input());
                String md5 = DigestUtils.md5DigestAsHex((ds.getData_source_type() + ds.getUrl()).getBytes());
                bsi.setInput_json(JSON.toJSONString(ds));
                bsi.setInput_md5(md5);
                bsi.setInput(StringUtils.join(input_tables, ","));

                DataSourcesInfo dsi_output = dataSourcesMapper.selectByPrimaryKey(sqlTaskInfo.getData_sources_choose_output());
                if(dsi_output == null) {
                    continue;
                }
                String md5_output = DigestUtils.md5DigestAsHex((dsi_output.getData_source_type() + dsi_output.getUrl()).getBytes());

                bsi.setOutput_md5(md5_output);
                bsi.setOutput_type(sqlTaskInfo.getData_source_type_output());
                String out = dsi_output.getData_source_type().equalsIgnoreCase("jdbc") ? sqlTaskInfo.getData_sources_table_name_output() : sqlTaskInfo.getData_sources_file_name_output();
                dsi_output.setPassword("");
                bsi.setOutput_json(JSON.toJSONString(dsi_output));
                if(!StringUtils.isEmpty(out)) {
                    output_tables.add(out);
                }
                bsi.setOutput(StringUtils.join(output_tables, ","));
                bsi.setVersion(version);
                bloodSourceMappeer.insertSelective(bsi);
                System.out.println("Spark: "+JSON.toJSONString(bsi));
                bsis.add(bsi);
            }

        }

        return bsis;

    }


    /**
     *
     * @param context
     * @param input
     * @param inputTable
     * @param output
     * @param outputTable
     * @param owner
     * @param version
     * @return
     */
    public static BloodSourceInfo report(String product_code, String context, String input, String inputTable, String output,String outputTable, String owner, String version) {
        BloodSourceInfo bsi = new BloodSourceInfo();
        try {
            BloodSourceMapper bloodSourceMapper = (BloodSourceMapper) SpringContext.getBean("bloodSourceMapper");
            DataSourcesMapper dataSourcesMapper = (DataSourcesMapper) SpringContext.getBean("dataSourcesMapper");
            bsi.setProduct_code(product_code);
            bsi.setContext(context);
            bsi.setOwner(owner);
            bsi.setCreate_time(new Date());

            DataSourcesInfo dsi_input = dataSourcesMapper.selectByPrimaryKey(input);
            bsi.setInput_type(dsi_input.getData_source_type());

            String md5 = DigestUtils.md5DigestAsHex((dsi_input.getData_source_type() + dsi_input.getUrl()).getBytes());
            dsi_input.setPassword("");
            bsi.setInput_json(JSON.toJSONString(dsi_input));
            bsi.setInput_md5(md5);
            bsi.setInput(inputTable);

            DataSourcesInfo dsi_output = dataSourcesMapper.selectByPrimaryKey(output);
            if(dsi_output==null){
                return bsi;
            }

            bsi.setOutput_type(dsi_output.getData_source_type());
            String md5_output = DigestUtils.md5DigestAsHex((dsi_output.getData_source_type() + dsi_output.getUrl()).getBytes());
            bsi.setOutput_md5(md5_output);
            dsi_output.setPassword("");
            bsi.setOutput_json(JSON.toJSONString(dsi_output));
            bsi.setOutput(outputTable);

            bsi.setVersion(version);

            bloodSourceMapper.insertSelective(bsi);

        } catch (Exception e) {
            logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            throw e;
        }


        return bsi;

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
