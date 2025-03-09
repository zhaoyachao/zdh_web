package com.zyc.zdh.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResoveExcel {

    public static final String OFFICE_EXCEL_XLS = "xls";
    public static final String OFFICE_EXCEL_XLSX = "xlsx";

    /**
     *
     * @param multipartFile 上传文件,支持xls, xlsx格式
     * @param colNames 中英文字段映射,key:中文 value:英文
     * @param isEnglish 上传的文件表头是否英文, true:英文,false:中文
     * @throws Exception
     */
    public static List<Map<String, Object>> importExcel(MultipartFile multipartFile, Map<String, String> colNames, boolean isEnglish) throws Exception {
        //检查文件是否支持
        checkFile(multipartFile);

        return importExcel(multipartFile.getInputStream(), colNames, isEnglish);
    }

    /**
     *  使用文件流,解析excel
     * @param file 文件输入流
     * @param colNames  中英文字段映射,key:中文 value:英文
     * @param isEnglish 文件表头是否英文, true:英文,false:中文
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> importExcel(InputStream file, Map<String, String> colNames, boolean isEnglish) throws Exception {

        //生成excel
        Workbook wb = readExcel(file);

        //读取文件表头
        Row title= readTitle(wb.getSheetAt(0));
        //检查表头是否有为空的异常数据
        List<String> titles = checkTitle(title);
        //检查表头是否和模型匹配
        List<String> en_titles = checkTitleByModel(isEnglish, titles, colNames);

        //读取数据,并删除表头
        List<Map<String, Object>> data = readExcelSheet(wb.getSheetAt(0), en_titles, 1);

        if(data == null || data.size()<1){
            throw new Exception("数据文件为空,请检查文件是否为空");
        }

        return data;
    }

    public static void checkFile(MultipartFile multipartFile) throws Exception {
        String fileName = multipartFile.getOriginalFilename();
        if(fileName==null||fileName.trim().equalsIgnoreCase("")){
           throw new Exception("上传文件为空,无法完成解析");
        }

        //校验是否xls, xlsx文件
        String suffiex = getSuffiex(fileName);
        if (StringUtils.isBlank(suffiex)) {
            throw new IllegalArgumentException("文件后缀不能为空");
        }
        if (!suffiex.equalsIgnoreCase(OFFICE_EXCEL_XLS) && !suffiex.equalsIgnoreCase(OFFICE_EXCEL_XLSX)) {
            throw new IllegalArgumentException("不支持的文件类型");
        }
    }

    public static List<String> checkTitle(Row title) throws Exception {
        List<String> titles = new ArrayList<>();
        int totalCellNum = title.getLastCellNum();
        for(int i=0;i<totalCellNum;i++){
            Cell cell = title.getCell(i);
            if(cell != null){
                cell.setCellType(CellType.STRING);
                titles.add(cell.getStringCellValue());
            }else{
               throw new Exception("表头中存在空列,请检查数据格式是否正常");
            }
        }
        return titles;
    }


    public static List<String> checkTitleByModel(boolean isEnglish, List<String> title,Map<String, String> colNames) throws Exception {
        List<String> title_tmp_diff = new ArrayList<>(title);
        //表头中文
        List<String> model = new ArrayList<>(colNames.keySet());
        if(isEnglish){
            //表头英文
            model = new ArrayList<String>(colNames.values());
        }

        //对比文件表头和模型表头是否匹配
        title_tmp_diff.removeAll(model);
        model.removeAll(title);
        if(title_tmp_diff.size()>0 || model.size()>0){
            throw new Exception("文件表头和模型不匹配,请检查文件表头是否正确");
        }

        //返回 英文表名
        if(isEnglish){
            return title;
        }else{
            List<String> ret = new ArrayList<>();
            for (int i=0;i<title.size();i++){
                ret.add(colNames.get(title.get(i)));
            }
            return ret;
        }
    }

    public static Workbook readExcel(InputStream inputStream) throws IOException {
        InputStream is = null;
        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                inputStream.close();
            }
            if (wb != null) {
                wb.close();
            }
        }
        return wb;
    }


    public static Row readTitle(Sheet sheet) throws Exception {
        Row returnRow = null;
        int totalRow = sheet.getLastRowNum();// 得到excel的总记录条数
        returnRow = sheet.getRow(0);
        if (returnRow == null) {
            throw new Exception("表头必须在文件首行");
        }
        return returnRow;
    }

    /**
     *
     * @param sheet
     * @param title
     * @param start_row
     * @return
     */
    private static List<Map<String, Object>> readExcelSheet(Sheet sheet, List<String> title,int start_row) {
        //colType 是表头映射,如果为空则以excel顺序
        //colNames 为中英文映射,如果表头是中文,则映射成英文字段
        List<Map<String, Object>> rows = new ArrayList<>();
        if(sheet != null){
            int rowNos = sheet.getLastRowNum();// 得到excel的总记录条数
            for (int i = start_row; i <= rowNos; i++) {// 遍历行
                Row row = sheet.getRow(i);
                Map<String, Object> jsonObject=JsonUtil.createEmptyMap();
                if(row != null){
                    int columNos = row.getLastCellNum();// 表头总共的列数
                    for (int j = 0; j < columNos; j++) {
                        Cell cell = row.getCell(j);
                        String col = title.get(j);
                        if(cell != null){
                            cell.setCellType(CellType.STRING);
                            jsonObject.put(col, cell.getStringCellValue());
                        }else{
                            jsonObject.put(col, "");
                        }
                    }

                    rows.add(jsonObject);
                }
            }
        }

        return rows;
    }

    /**
     * 获取后缀
     * @param filepath filepath 文件全路径
     */
    private static String getSuffiex(String filepath) {
        if (StringUtils.isBlank(filepath)) {
            return "";
        }
        int index = filepath.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return filepath.substring(index + 1, filepath.length());
    }
}
