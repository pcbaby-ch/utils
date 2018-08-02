package com.ack.common;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.ack.enums.RespCode;
import com.alibaba.fastjson.JSON;

/**
 * 
 * @author chen.zhao (chenzhao) @DATE: Jan 9, 2018
 */
@Component
public class ExceptionHandler implements HandlerExceptionResolver {
	private static final Logger LOGGER = LogManager.getLogger(ExceptionHandler.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		if (ex instanceof ServiceException) {
			result.put("code", ((ServiceException) ex).getCode());
			result.put("message", (StringUtils.isEmpty(((ServiceException) ex).getMsg())
					? ((ServiceException) ex).getMessage() : ((ServiceException) ex).getMsg()));
			LOGGER.debug("业务异常：" + (StringUtils.isEmpty(((ServiceException) ex).getMsg())
					? ((ServiceException) ex).getMessage() : ((ServiceException) ex).getMsg()),ex);
			LOGGER.info("业务异常：" + (StringUtils.isEmpty(((ServiceException) ex).getMsg())
					? ((ServiceException) ex).getMessage() : ((ServiceException) ex).getMsg()));
		} else {
			result.put("code", RespCode.END.getCode());
			result.put("message", RespCode.END.getMsg() + ex.getMessage());
			LOGGER.error(RespCode.END.getMsg() + ex.getMessage());
		}
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		try {
			response.getWriter().write(JSON.toJSONString(result));
		} catch (IOException e) {
			LOGGER.error("#与客户端通讯异常：" + e.getMessage(), e);
		}

		return new ModelAndView();
	}

}
