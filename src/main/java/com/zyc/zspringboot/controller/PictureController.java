package com.zyc.zspringboot.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.swetake.util.Qrcode;

@Controller
@RequestMapping("picture")
public class PictureController {

	@RequestMapping("list")
	public String list() {
		return "picture/picture-list";
	}

	@RequestMapping("build")
	public String build() {

		return "picture/picture-build";
	}

	@RequestMapping("img")
	public void img(ServletResponse response, String content) {

		Qrcode x = new Qrcode();
		x.setQrcodeErrorCorrect('M');// 纠错等级
		x.setQrcodeEncodeMode('B');// N代表数字,A表示a-Z,B表示其他字符(中文等)
		x.setQrcodeVersion(7);// 版本号
		if (content == null || content.equals("")) {
			content = "你好,需要你填写二维码信息才能生成";
		}
		System.out.println(content);
		String qrData = content;
		int width = 67 + 12 * (7 - 1);
		int height = 67 + 12 * (7 - 1);
		BufferedImage bufferedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);// 创建缓冲区的图片
		Graphics2D gs = bufferedImage.createGraphics();
		gs.setBackground(Color.white);
		gs.setColor(Color.black);
		gs.clearRect(0, 0, width, height);
		int pixoff = 2;// 偏移量
		// 填充画板 内容
		byte[] d;
		try {
			d = qrData.getBytes("utf-8");
			if (d.length > 0 && d.length < 120) {
				boolean[][] s = x.calQrcode(d); // 填充内容转化成字节数进行填充
				for (int i = 0; i < s.length; i++) {
					for (int j = 0; j < s.length; j++) {
						if (s[j][i]) {
							gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
						}
					}
				}
			}
//			File f = new File("");
//
//			if (!f.exists()) {
//				f.createNewFile();
//			}
			//添加logo
			BufferedImage logo = ImageIO.read(new URL("http://avatar.csdn.net/2/F/6/3_zhaoyachao123.jpg"));
			gs.drawImage(logo.getScaledInstance(35, 35, Image.SCALE_SMOOTH), 53, 53, null);
			gs.dispose();
			bufferedImage.flush();
			//ImageIO.write(bufferedImage, "JPEG", f);
			ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());
		} catch (Exception e) {

		}
	}
	
	@RequestMapping(value="download")
	public void download(HttpServletResponse response, String content) {

		Qrcode x = new Qrcode();
		x.setQrcodeErrorCorrect('M');// 纠错等级
		x.setQrcodeEncodeMode('B');// N代表数字,A表示a-Z,B表示其他字符(中文等)
		x.setQrcodeVersion(7);// 版本号
		if (content == null || content.equals("")) {
			content = "你好,需要你填写二维码信息才能生成";
		}
		System.out.println(content);
		String qrData = content;
		int width = 67 + 12 * (7 - 1);
		int height = 67 + 12 * (7 - 1);
		BufferedImage bufferedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);// 创建缓冲区的图片
		Graphics2D gs = bufferedImage.createGraphics();
		gs.setBackground(Color.white);
		gs.setColor(Color.black);
		gs.clearRect(0, 0, width, height);
		int pixoff = 2;// 偏移量
		// 填充画板 内容
		byte[] d;
		try {
			d = qrData.getBytes("utf-8");
			if (d.length > 0 && d.length < 120) {
				boolean[][] s = x.calQrcode(d); // 填充内容转化成字节数进行填充
				for (int i = 0; i < s.length; i++) {
					for (int j = 0; j < s.length; j++) {
						if (s[j][i]) {
							gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
						}
					}
				}
			}

			//添加logo
			BufferedImage logo = ImageIO.read(new URL("http://avatar.csdn.net/2/F/6/3_zhaoyachao123.jpg"));
			gs.drawImage(logo.getScaledInstance(35, 35, Image.SCALE_SMOOTH), 53, 53, null);
			gs.dispose();
			bufferedImage.flush();
		
			response.setHeader("Content-Type","application/octet-stream");
            String fileName="二维码"+UUID.randomUUID().toString().replaceAll("-", "")+".jpg";
            if(regex(fileName)){
            	 response.setHeader("Content-Disposition","attachment;filename="+ new String(fileName.getBytes("gb2312"), "ISO8859-1" ));	
            }else{
            	 response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileName, "utf-8"));	
            }
           
   
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "JPEG", out);
			response.getOutputStream().write(out.toByteArray());
		
			
		} catch (Exception e) {
System.out.println(e.getMessage());
		}
	}
	
	 public boolean regex(String fileName){
	    	// 要验证的字符串
	        String str = "baike.xsoftlab.net";
	        // 正则表达式规则
	        String regEx = "[\\u4e00-\\u9fa5]+";
	        // 编译正则表达式
	        Pattern pattern = Pattern.compile(regEx);
	        // 忽略大小写的写法
	        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
	        Matcher matcher = pattern.matcher(fileName);
	        // 查找字符串中是否有匹配正则表达式的字符/字符串
	        boolean rs = matcher.find();
	        System.out.println(rs);
	        return rs;
	    }
}
