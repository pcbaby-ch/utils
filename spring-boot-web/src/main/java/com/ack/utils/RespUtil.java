/**
 *
 */
package com.ack.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ack.enums.RespCode;
import com.github.pagehelper.Page;

/**
 * json响应数据组装
 * @author chenzhao @date Oct 16, 2019
 */
public class RespUtil {

	/**
	 * datagrid表格列表-web
	 * 
	 * @param page
	 */
	public static Map<String, Object> listResp(Page<?> page) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", RespCode.SUCCESS.getCode());
		map.put("msg", RespCode.SUCCESS.getMsg());
		map.put("respData", page.getResult());
		map.put("total", page.getTotal());
		return map;
	}

	/**
	 * 基础业务操作-web
	 * 适配响应：新增、修改、删除、业务操作。。。
	 * @param result
	 *  true:操作成功 false：操作失败
	 * @return
	 */
	public static Map<String, Object> baseResp(boolean result) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(result){
			map.put("code", RespCode.SUCCESS.getCode());
			map.put("msg", RespCode.SUCCESS.getMsg());
		}else{
			map.put("code", RespCode.FAILURE.getCode());
			map.put("msg", RespCode.FAILURE.getMsg());
		}
		return map;
	}
	/**
	 * 平台resultAPI接口交互响应
	 * @param result
	 * @return
	 */
	public static Map<String, Object> apiResp(boolean result) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(result){
			map.put("code", RespCode.SUCCESS.getCode());
			map.put("msg", RespCode.SUCCESS.getMsg());
		}else{
			map.put("code", RespCode.FAILURE.getCode());
			map.put("msg", RespCode.FAILURE.getMsg());
		}
		return map;
	}
	
	/**
	 * 业务invoke成功带data响应业务数据
	 * 适配响应：详情查看操作。。。
	 * @param data 不限定格式规约
	 * @return
	 */
	public static Map<String, Object> dataResp(Object data) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", RespCode.SUCCESS.getCode());
		map.put("msg", RespCode.SUCCESS.getMsg());
		map.put("respData", data);
		return map;
	}
	/**
	 *  
	 * 适配响应：。。。
	 * @param data 不限定格式规约
	 * @return
	 */
	public static Map<String, Object> dataCodeResp(RespCode respEnum,Object data) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", respEnum.getCode());
		map.put("msg", respEnum.getMsg());
		map.put("respData", data);
		return map;
	}

	/**
	 *
	 * 适配响应：。。。
	 * @param respEnum 不限定格式规约
	 * @return
	 */
	public static Map<String, Object> codeResp(RespCode respEnum) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", respEnum.getCode());
		map.put("msg", respEnum.getMsg());
		return map;
	}

	public static Map<String, Object> listResp(List<?> list) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", RespCode.SUCCESS.getCode());
		map.put("msg", RespCode.SUCCESS.getMsg());
		map.put("respData", list);
		map.put("total", list.size());
		return map;
	}

	public static Map<String, Object> listResp(org.springframework.data.domain.Page<?> page) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", RespCode.SUCCESS.getCode());
		map.put("msg", RespCode.SUCCESS.getMsg());
		map.put("respData", page.getContent());
		map.put("total", page.getTotalElements());
		return map;
	}

}
