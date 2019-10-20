package com.ack.common;

import com.alibaba.fastjson.JSONObject;
import com.ack.enums.RespCode;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InvokeService {

	private static Logger log = LoggerFactory.getLogger(InvokeService.class);

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
		if(jsonObj==null||jsonObj.toString().length()<=0){
			throw new ServiceException(RespCode.RESTFUL_REQ_RESPERROR, jsonObj);
		}
		if (!(RespCode.SUCCESS.getCode().equals(jsonObj.getString("retCode"))
				|| RespCode.SUCCESS.getCode().equals(jsonObj.getString("code")))) {
			String code=StringUtils.isBlank(jsonObj.getString("retCode"))?jsonObj.getString("code"):jsonObj.getString("retCode");
			throw new ServiceException(code,RespCode.RESTFUL_REQ_SERVICEERROR, jsonObj);
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
