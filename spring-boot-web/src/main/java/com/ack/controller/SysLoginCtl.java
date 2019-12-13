/**
 * 
 */
package com.ack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.ack.config.annotation.CheckSign;
import com.ack.entity.SysRole;
import com.ack.entity.SysUser;
import com.ack.service.SysLoginService;
import com.ack.utils.DateUtil;
import com.ack.utils.RespUtil;

/**
 * 系统登录
 * 
 * @author ack @date Oct 19, 2019
 *
 */
@RestController
public class SysLoginCtl {

	@Autowired
	SysLoginService sysLoginService;

	@RequestMapping("hello")
	public String hello() {
		return "hello" + System.currentTimeMillis();
	}

	/**
	 * 用户登录
	 * 
	 * @author ack @date Oct 19, 2019
	 * @param version
	 * @param req
	 * @return
	 */
	@RequestMapping("user/login")
	@CheckSign(isCheckLogin = false)
	public Object login(@RequestHeader("version") String version, @RequestBody String req) {
		return RespUtil.dataResp(sysLoginService.userLogin(JSONObject.parseObject(req, SysUser.class)));
	}

	/**
	 * 用户注销
	 * 
	 * @author ack @date Oct 19, 2019
	 * @param version
	 * @param req
	 * @return
	 */
	@RequestMapping("user/logout")
	@CheckSign()
	public Object logout(@RequestHeader("version") String version, @RequestHeader("token") String token) {
		return RespUtil.baseResp(sysLoginService.userLogout(token));
	}

}
