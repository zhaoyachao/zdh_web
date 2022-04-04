package com.zyc.zdh.hadoop;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.util.FileCopyUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * hdfs工具类
 */
public class HadoopUtil {

    /**
     * 写入hdfs
     * @param dsiInfo
     * @param path 写入文件全路径(目录+文件名)
     * @param file
     * @throws Exception
     */
    public static void writeHdfs(Dsi_Info dsiInfo, String path,byte[] file) throws Exception {
        Configuration conf = new Configuration();
        try {
            //fs_defaultFS 必须指定ip和端口,如果是虚拟地址,需要提前解析成ip:port
            String fs_defaultFS = dsiInfo.getUrl();
            String hadoop_user_name = dsiInfo.getUser();
            FileSystem fs = FileSystem.getLocal(conf);
            if (!StringUtils.isEmpty(fs_defaultFS)) {
                fs = FileSystem.get(new URI(fs_defaultFS), conf, hadoop_user_name);
            }
            if(path.contains(".xlsx") || path.contains(".xls")){
                File fileDir = new File( "tmp" );
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                String file_name = path.substring(path.lastIndexOf('/')+1);
                File f = new File("tmp/"+file_name);
                FileCopyUtils.copy(file, f );
                fs.copyFromLocalFile(new Path(f.getAbsolutePath()), new Path(path));
            }else{
                Path hdfsWritePath = new Path(path);
                FSDataOutputStream fsDataOutputStream = fs.create(hdfsWritePath,true);

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fsDataOutputStream, StandardCharsets.UTF_8));
                bufferedWriter.write(IOUtils.toString(file, "UTF-8"));
                bufferedWriter.newLine();
                bufferedWriter.close();
            }
            fs.close();
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 读取hdfs
     * @param dsiInfo
     * @param path
     * @return
     * @throws Exception
     */
    public static byte[] readHdfs(Dsi_Info dsiInfo, String path) throws Exception {
        Configuration conf = new Configuration();
        try {
            String fs_defaultFS = dsiInfo.getUrl();
            String hadoop_user_name = dsiInfo.getUser();
            FileSystem fs = FileSystem.getLocal(conf);
            if (!StringUtils.isEmpty(fs_defaultFS)) {
                fs = FileSystem.get(new URI(fs_defaultFS), conf, hadoop_user_name);
            }

            Path hdfsReadPath = new Path(path);
            FSDataInputStream inputStream = fs.open(hdfsReadPath);
            byte[] out= IOUtils.toByteArray(inputStream);
            inputStream.close();
            fs.close();
            return out;
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 删除hdfs
     * @param dsiInfo
     * @param path
     * @param recursive
     * @throws Exception
     */
    public static void deleteHdfs(Dsi_Info dsiInfo, String path, boolean recursive) throws Exception {
        Configuration conf = new Configuration();
        try {
            String fs_defaultFS = dsiInfo.getUrl();
            String hadoop_user_name = dsiInfo.getUser();
            FileSystem fs = FileSystem.getLocal(conf);
            if (!StringUtils.isEmpty(fs_defaultFS)) {
                fs = FileSystem.get(new URI(fs_defaultFS), conf, hadoop_user_name);
            }
            Path hdfsReadPath = new Path(path);
            fs.delete(hdfsReadPath, recursive);
            fs.close();
        }catch (Exception e){
            throw e;
        }
    }

}
