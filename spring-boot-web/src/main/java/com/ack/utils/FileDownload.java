package com.ack.utils;


import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

/**
 * 下载文件
 * @author chenzhao
 * @time May 30, 2017-11:36:14 AM
 */
public class FileDownload {

	/**
	 * 
	 * @author chenzhao
	 * @time May 30, 2017-11:33:40 AM
	 * @param response httpResponse对象
	 * @param filePath 文件路径
	 * @param fileName 文件名
	 * @throws Exception
	 */
	public static void fileDownload(final HttpServletResponse response, String filePath, String fileName) throws Exception {
		
		byte[] data = FileUtil.toByteArray2(filePath);
		fileName = URLEncoder.encode(fileName, "UTF-8");
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream;charset=UTF-8");
		OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
		outputStream.write(data);
		outputStream.flush();
		outputStream.close();
		response.flushBuffer();

	}

}
