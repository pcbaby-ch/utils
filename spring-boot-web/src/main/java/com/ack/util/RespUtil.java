/**
 *
 */
package com.ack.util;

import java.util.HashMap;
import java.util.Map;

import com.ack.enums.RespCode;
import com.github.pagehelper.Page;

/**
 * 响应数据
 * 
 * @author chen.zhao (chenzhao) @DATE: 2018年3月1日
 */
public class RespUtil {

	/**
	 * datagrid表格列表-web
	 * 
	 * @param page
	 */
	public static Map<String, Object> listResp(Page<?> page) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", page.getTotal());
		map.put("rows", page.getResult());
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
			map.put("code", RespCode.API_SUCCESS.getCode());
			map.put("msg", RespCode.API_SUCCESS.getMsg());
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
		map.put("data", data);
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
		map.put("data", data);
		return map;
	}

}
