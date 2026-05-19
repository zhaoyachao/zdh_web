package com.zyc.zdh.util;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class ImageUtils {

    /**
     * 将图片URL转换为Base64编码字符串
     * @param imageUrl 图片URL地址
     * @return Base64编码字符串，失败时返回null
     */
    public static String urlToBase64(String imageUrl) {
        try {
            // 创建URL对象
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // 检查响应码
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // 获取输入流
                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                // 读取图片数据
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                // 转换为Base64
                byte[] imageBytes = outputStream.toByteArray();
                return Base64.getEncoder().encodeToString(imageBytes);
            }
        } catch (Exception e) {
            LogUtil.error(ImageUtils.class, "图片URL转Base64失败", e);
        }
        return null;
    }

    public static String fileToBase64(MultipartFile file, boolean withMimePrefix){
        try{
            String base64Str  = fileToBase64(file);
            if (withMimePrefix) {
                String fileName = MultipartFileUtil.getFileName(file);
                String mimeType = getFileMimeType(fileName);
                base64Str = "data:" + mimeType + ";base64," + base64Str;
            }
            return base64Str;
        }catch (Exception e){
            LogUtil.error(ImageUtils.class, "文件转Base64失败", e);
        }
        return null;
    }

    public static String fileToBase64(MultipartFile file){
        try{
            byte[] fileBytes = file.getBytes();
            return Base64.getEncoder().encodeToString(fileBytes);
        }catch (Exception e){
            LogUtil.error(ImageUtils.class, "文件转Base64失败", e);
        }
        return null;
    }

    public static String bufferedImageToBase64(BufferedImage image) throws IOException {
        return bufferedImageToBase64(image, "PNG");
    }

   /**
    * 将 BufferedImage 转换为 Base64 编码字符串（不带 Data URI 前缀）
    * @param image 图片对象
    * @param format 图片格式（如 "PNG"、"JPG"、"JPEG"）
    * @return Base64 编码字符串
    * @throws IOException 转换异常
    */
    public static String bufferedImageToBase64(BufferedImage image, String format) throws IOException {
        if (image == null) {
            throw new IllegalArgumentException("BufferedImage 不能为空");
        }
        if (format == null || format.isEmpty()) {
            format = "PNG"; // 默认PNG格式（二维码推荐）
        }

        // 1. 将 BufferedImage 写入字节数组输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, format, outputStream);

        // 2. 字节数组转 Base64 编码
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private static String getFileMimeType(String fileName) {
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        // 常见 MIME 类型映射（可根据需求扩展）
        switch (suffix) {
            // 图片
            case "jpg":
                return "image/jpg";
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "bmp":
                return "image/bmp";
            case "webp":
                return "image/webp";
            // 视频
            case "mp4":
                return "video/mp4";
            case "avi":
                return "video/x-msvideo";
            case "mov":
                return "video/quicktime";
            case "mkv":
                return "video/x-matroska";
            case "flv":
                return "video/x-flv";
            // 语音/音频
            case "mp3":
                return "audio/mpeg";
            case "wav":
                return "audio/wav";
            case "ogg":
                return "audio/ogg";
            case "amr":
                return "audio/amr";
            // 文本
            case "txt":
                return "text/plain";
            case "json":
                return "application/json";
            // 其他默认类型
            default:
                return "application/octet-stream";
        }
    }
}