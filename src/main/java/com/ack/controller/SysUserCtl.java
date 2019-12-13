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
import com.ack.entity.SysUser;
import com.ack.service.SysUserService;
import com.ack.utils.RespUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 系统用户CRUD
 * 
 * @author chenzhao @date Oct 18, 2019
 */
@RestController
public class SysUserCtl {

	@Autowired
	SysUserService sysUserService;

	/**
	 * 用户列表查询
	 * 
	 * @author ack @date Oct 19, 2019
	 * @param version
	 * @param req
	 * @return
	 */
	@RequestMapping("user/list")
	@CheckSign
	public Object selectList(@RequestHeader("version") String version, @RequestBody String reqStr) {
		JSONObject req = JSONObject.parseObject(reqStr);
		Page<?> page = PageHelper.startPage(req.getIntValue("pageNum"), req.getIntValue("pageSize"));
		sysUserService.selectList();
		return RespUtil.listResp(page);
	}

	/**
	 * 用户详情
	 * 
	 * @author ack @date Oct 19, 2019
	 * @param version
	 * @param req
	 * @return
	 */
	@RequestMapping("user/detail")
	@CheckSign
	public Object selectOne(@RequestHeader("version") String version, @RequestBody String reqStr) {
		JSONObject req = JSONObject.parseObject(reqStr);
		return RespUtil.dataResp(sysUserService.selectOne(req.getLongValue("id")));
	}

	/**
	 * 用户新增
	 * 
	 * @author ack @date Oct 19, 2019
	 * @param version
	 * @param req
	 * @return
	 */
	@RequestMapping("user/add")
	@CheckSign
	public Object insert(@RequestHeader("version") String version, @RequestHeader("token") String token,
			@RequestBody String reqStr) {
		JSONObject req = JSONObject.parseObject(reqStr);
		return RespUtil.baseResp(sysUserService.insert(JSONObject.toJavaObject(req, SysUser.class), token));
	}

	/**
	 * 用户信息编辑
	 * 
	 * @author ack @date Oct 19, 2019
	 * @param version
	 * @param req
	 * @return
	 */
	@RequestMapping("user/edit")
	@CheckSign
	public Object update(@RequestHeader("version") String version, @RequestHeader("token") String token,
			@RequestBody String reqStr) {
		JSONObject req = JSONObject.parseObject(reqStr);
		return RespUtil.baseResp(sysUserService.update(JSONObject.toJavaObject(req, SysUser.class), token));
	}

	/**
	 * 用户删除-逻辑删除
	 * 
	 * @author ack @date Oct 19, 2019
	 * @param version
	 * @param req
	 * @return
	 */
	@RequestMapping("user/delete")
	@CheckSign
	public Object delete(@RequestHeader("version") String version, @RequestHeader("token") String token,
			@RequestBody String reqStr) {
		JSONObject req = JSONObject.parseObject(reqStr);
		SysUser sysUser = new SysUser();
		sysUser.setId(req.getLongValue("id"));
		sysUser.setIsDel(1);
		return RespUtil.baseResp(sysUserService.update(sysUser, token));
	}

	/**
	 * 给用户分配角色
	 * 
	 * @author ack @date Oct 19, 2019
	 * @param version
	 * @param req
	 * @return
	 */
	@RequestMapping("user/update4role")
	@CheckSign
	public Object update4role(@RequestHeader("version") String version, @RequestHeader("token") String token,
			@RequestBody String reqStr) {
		JSONObject req = JSONObject.parseObject(reqStr);
		return RespUtil.baseResp(sysUserService.update4role(JSONObject.toJavaObject(req, SysUser.class), token));
	}

	/**
	 * 重置密码
	 * 
	 * @author ack @date Oct 19, 2019
	 * @param version
	 * @param req
	 * @return
	 */
	@RequestMapping("user/resetPwd")
	@CheckSign
	public Object resetPwd(@RequestHeader("version") String version, @RequestHeader("token") String token,
			@RequestBody String reqStr) {
		JSONObject req = JSONObject.parseObject(reqStr);
		return RespUtil.baseResp(sysUserService.resetPwd(req, token));
	}

}
