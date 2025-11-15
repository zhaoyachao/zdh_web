package com.zyc.zdh.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MultipartFileUtil {

    /**
     * 获取文件名称
     * 去除路径
     * @param multipartFile
     * @return
     */
    public static String getFileName(MultipartFile multipartFile){
        if(multipartFile == null){
            return null;
        }

        String fileName = multipartFile.getOriginalFilename();
        if(StringUtils.isEmpty(fileName)){
            return null;
        }
        Path path = Paths.get(fileName);
        fileName = path.getFileName().toString();
        return fileName;
    }
}
