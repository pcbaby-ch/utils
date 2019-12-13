package com.ack.common;


import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.ack.enums.RespCode;
import com.alibaba.fastjson.JSONObject;

/**
 * 多线程异步restful服务调用
 * 
 * @author chen.zhao @DATE: Apr 17, 2018
 */
@Component
public class InvokeService {

	private static Logger log = LoggerFactory.getLogger(InvokeService.class);

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 异步调用
	 * 
	 * @param url
	 * @param req
	 * @return
	 */
	@Async("getThreadPoolTaskExecutor")
	public Future<String> asynInvoke(String url, Object req) {
		long startTime = System.currentTimeMillis();
		log.info(">>>START asynInvoke:[{}],,#reqData:[{}],,[{}]", url, JSONObject.toJSON(req), Thread.currentThread());
		Future<String> future = new AsyncResult<>(restTemplate.postForObject(url, req, String.class));
		log.info(">>>END asynInvoke:[{}],,#invoke interval time[{}]	", url, System.currentTimeMillis() - startTime);
		return future;
	}

	public String synInvoke(String url, Object req, int timeout) {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout(3000);
		factory.setReadTimeout(timeout);
		RestTemplate restTemplate2 = new RestTemplate(factory);
		long startTime = System.currentTimeMillis();
		log.info(">>>START synInvoke:[{}],,#reqData:[{}]", url, JSONObject.toJSON(req));
		String resp;
		try {
			resp = restTemplate2.postForObject(url, req, String.class);
		} catch (Exception e) {
			throw new ServiceException(RespCode.RESTFUL_REQ_TIMEOUT);
		}
		log.info(">>>END synInvoke:[{}],,#invoke interval time[{}]", url, System.currentTimeMillis() - startTime);
		return resp;
	}

	/**
	 * 同步调用
	 * 
	 * @param url
	 * @param req
	 * @return
	 */
	public String synInvoke(String url, Object req) {
		long startTime = System.currentTimeMillis();
		log.info(">>>START synInvoke:[{}],,#reqData:[{}]", url, JSONObject.toJSON(req));
		String resp;
		try {
			resp = restTemplate.postForObject(url, req, String.class);
		} catch (Exception e) {
			throw new ServiceException(RespCode.RESTFUL_REQ_TIMEOUT);
		}
		log.info(">>>END synInvoke:[{}],,#invoke interval time[{}],,#resp:[{}]", url,
				System.currentTimeMillis() - startTime, resp);
		return resp;
	}

	/**
	 * 解析响应数据，直接返回整个报文体jsonObj
	 * 
	 * @param fuResp
	 * @return
	 */
	public JSONObject parseResp(Future<String> fuResp) {
		String result = null;
		try {
			result = fuResp.get();
		} catch (Exception e) {
			throw new ServiceException(RespCode.RESTFUL_REQ_TIMEOUT);
		}
		// #解析服务响应数据>JSONObject
		return parse(result);
	}

	/**
	 * 重解析响应数据，响应码非0000报错，否则返回data部分
	 * 
	 * @param fuResp
	 * @return
	 */
	public JSONObject parseRespData(Future<String> fuResp) {
		String result = null;
		try {
			result = fuResp.get();
		} catch (Exception e) {
			throw new ServiceException(RespCode.RESTFUL_REQ_TIMEOUT);
		}
		// #解析服务响应数据>JSONObject
		return parseData(result);
	}

	/**
	 * 重解析响应数据，响应码非0000报错，否则返回data部分
	 * 
	 * @param resp
	 * @return
	 */
	public JSONObject parseRespData(String resp) {
		return parseData(resp);
	}

	/**
	 * 解析响应数据，直接返回整个报文体jsonObj
	 * 
	 * @param resp
	 * @return
	 */
	public JSONObject parseResp(String resp) {
		return parse(resp);
	}

	/**
	 * 解析响应数据，直接返回整个报文体jsonObj
	 * 
	 * @param result
	 * @return
	 */
	private static JSONObject parse(String result) {
		log.info(">>>parsingResp：[{}]", result);
		JSONObject jsonObj;
		try {
			jsonObj = JSONObject.parseObject(result);
			/*** 响应报文，响应code做业务是否正常通用处理判断 ****************************/
		} catch (Exception e) {
			log.error("#json parse error：" + result, e);
			result = result.replaceAll("\"", "'");
			jsonObj = JSONObject.parseObject("{\"wrapResp\":\"" + result + "\"}");
		}
		if (jsonObj == null || jsonObj.toString().length() <= 0) {
			throw new ServiceException(RespCode.RESTFUL_REQ_RESPERROR, jsonObj);
		}
		if (!(RespCode.SUCCESS.getCode().equals(jsonObj.getString("retCode"))
				|| RespCode.SUCCESS.getCode().equals(jsonObj.getString("code")))) {
			String code = StringUtils.isEmpty(jsonObj.getString("retCode")) ? jsonObj.getString("code")
					: jsonObj.getString("retCode");
			throw new ServiceException(code, RespCode.RESTFUL_REQ_SERVICEERROR, jsonObj);
		}
		return jsonObj;
	}

	/**
	 * 重解析响应数据，响应码非0000报错，否则返回data部分
	 * 
	 * @param result
	 * @return
	 */
	private static JSONObject parseData(String result) {
		JSONObject jsonObj = parse(result);
		if (jsonObj.getJSONObject("data") == null) {
			throw new ServiceException("#响应报文无data报文体");
		} else {
			return jsonObj.getJSONObject("data");
		}
	}


}
