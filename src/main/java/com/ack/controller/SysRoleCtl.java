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
import com.ack.service.SysRoleService;
import com.ack.utils.RespUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 系统角色CRUD
 * 
 * @author chenzhao @date Oct 18, 2019
 */
@RestController
public class SysRoleCtl {

	@Autowired
	SysRoleService sysRoleService;

	/**
	 * 角色list列表
	 * 
	 * @author ack @date Oct 19, 2019
	 * @param version
	 * @param req
	 * @return
	 */
	@RequestMapping("role/list")
	@CheckSign
	public Object selectList(@RequestHeader("version") String version, @RequestBody String reqStr) {
		JSONObject req = JSONObject.parseObject(reqStr);
		Page<?> page = PageHelper.startPage(req.getIntValue("pageNum"), req.getIntValue("pageSize"));
		sysRoleService.selectList();
		return RespUtil.listResp(page);
	}

	/**
	 * 角色详情-XXX
	 * 
	 * @author ack @date Oct 19, 2019
	 * @param version
	 * @param req
	 * @return
	 */
	@RequestMapping("role/detail")
	@CheckSign
	public Object selectOne(@RequestHeader("version") String version, @RequestHeader("token") String token,
			@RequestBody String reqStr) {
		JSONObject req = JSONObject.parseObject(reqStr);
		return RespUtil.dataResp(sysRoleService.selectOne(req.getLongValue("id")));
	}

	/**
	 * 新增角色
	 * 
	 * @author ack @date Oct 19, 2019
	 * @param version
	 * @param req
	 * @return
	 */
	@RequestMapping("role/add")
	@CheckSign
	public Object insert(@RequestHeader("version") String version, @RequestHeader("token") String token,
			@RequestBody String reqStr) {
		JSONObject req = JSONObject.parseObject(reqStr);
		return RespUtil.baseResp(sysRoleService.insert(JSONObject.toJavaObject(req, SysRole.class), token));
	}

	/**
	 * 编辑角色
	 * 
	 * @author ack @date Oct 19, 2019
	 * @param version
	 * @param req
	 * @return
	 */
	@RequestMapping("role/edit")
	@CheckSign
	public Object update(@RequestHeader("version") String version, @RequestHeader("token") String token,
			@RequestBody String reqStr) {
		JSONObject req = JSONObject.parseObject(reqStr);
		return RespUtil.baseResp(sysRoleService.update(JSONObject.toJavaObject(req, SysRole.class), token));
	}

	/**
	 * 删除角色-物理删除
	 * 
	 * @author ack @date Oct 19, 2019
	 * @param version
	 * @param req
	 * @return
	 */
	@RequestMapping("role/delete")
	@CheckSign
	public Object delete(@RequestHeader("version") String version, @RequestBody String reqStr) {
		JSONObject req = JSONObject.parseObject(reqStr);
		return RespUtil.baseResp(sysRoleService.delete(req.getLongValue("id")));
	}

}
