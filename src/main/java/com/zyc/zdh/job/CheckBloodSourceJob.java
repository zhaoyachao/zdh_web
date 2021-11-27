package com.zyc.zdh.job;

import com.zyc.zdh.dao.DataSourcesMapper;
import com.zyc.zdh.dao.EtlMoreTaskMapper;
import com.zyc.zdh.dao.EtlTaskMapper;
import com.zyc.zdh.entity.BloodSourceInfo;
import com.zyc.zdh.entity.DataSourcesInfo;
import com.zyc.zdh.entity.EtlMoreTaskInfo;
import com.zyc.zdh.entity.EtlTaskInfo;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;

public class CheckBloodSourceJob {
    private static Logger logger = LoggerFactory.getLogger(CheckBloodSourceJob.class);


    public static BloodSourceInfo resove(EtlTaskInfo eti, String version){
        EtlTaskMapper etlTaskMapper=(EtlTaskMapper) SpringContext.getBean("etlTaskMapper");
        DataSourcesMapper dataSourcesMapper=(DataSourcesMapper) SpringContext.getBean("dataSourcesMapper");
        BloodSourceInfo bsi=new BloodSourceInfo();
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

        //LineageInfo li=new LineageInfo();

    }
}
