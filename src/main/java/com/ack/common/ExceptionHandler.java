package com.ack.common;

import com.alibaba.fastjson.JSON;
import com.ack.enums.RespCode;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

@Component
public class ExceptionHandler implements HandlerExceptionResolver {
	private static Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

	@Value("${businessError:false}")
	private String businessError;
	@Value("${systemError:false}")
	private String systemError;
	@Value("${logNotificationMan:pcbaby-ch@qq.com}")
	private String logNotificationMan;
	@Autowired
	private EmailService mailService;

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		HashMap<String, Object> result = new HashMap<>(16);
		if (ex instanceof ServiceException) {
			result.put("code", ((ServiceException) ex).getCode());
			result.put("msg", (StringUtils.isEmpty(((ServiceException) ex).getMsg()) ? ex.getMessage()
					: ((ServiceException) ex).getMsg()));
			LOGGER.info("业务异常：" + (StringUtils.isEmpty(((ServiceException) ex).getMsg()) ? ex.getMessage()
					: ((ServiceException) ex).getMsg()));
			LOGGER.debug("业务异常：" + (StringUtils.isEmpty(((ServiceException) ex).getMsg()) ? ex.getMessage()
					: ((ServiceException) ex).getMsg()), ex);
			errorNotifications(ex);
		} else {
			result.put("code", RespCode.FAILURE.getCode());
			result.put("msg", RespCode.FAILURE.getMsg());
			LOGGER.error(RespCode.FAILURE.getMsg() + ":" + ex.getMessage(), ex);
			errorNotifications(ex);
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

	public void errorNotifications(Exception e) {
		InetAddress address = null;
		try {
			address = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		if (e instanceof ServiceException) {
			if ("true".equals(businessError)) {
				mailService.sendSimpleMail(logNotificationMan.split(","),
						address == null ? "" : address.getHostAddress() + "业务异常告警",
						(StringUtils.isEmpty(((ServiceException) e).getMsg()) ? ((ServiceException) e).getMessage()
								: ((ServiceException) e).getMsg()));
			}
		} else {
			if ("true".equals(systemError)) {
				mailService.sendSimpleMail(logNotificationMan.split(","),
						address == null ? "" : address.getHostAddress() + "系统异常告警", e.getMessage());
			}
		}
	}

}
