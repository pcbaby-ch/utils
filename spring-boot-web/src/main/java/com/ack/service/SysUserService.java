/**
 * 
 */
package com.ack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.ack.common.RedisPool;
import com.ack.common.ServiceException;
import com.ack.constants.RedisCons;
import com.ack.entity.SysUser;
import com.ack.enums.RespCode;
import com.ack.mapper.SysUserMapper;
import com.ack.utils.CheckUtil;

/**
 * 用户CRUD
 * 
 * @author chenzhao @date Oct 18, 2019
 */
@Service
public class SysUserService {

	@Autowired
	SysUserMapper sysUserMapper;

	public List<SysUser> selectList() {
		return sysUserMapper.selectList();
	}

	public SysUser selectOne(long id) {
		if (id == 0) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "id");
		}
		return sysUserMapper.selectOne(id);
	}

	public boolean insert(SysUser sysUser, String token) {
		if (StringUtils.isEmpty(sysUser.getUserName())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "name");
		}
		if (StringUtils.isEmpty(sysUser.getPassword())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "Password");
		}
		if (StringUtils.isEmpty(sysUser.getName())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "name");
		}
		if (StringUtils.isEmpty(sysUser.getTitle())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "Title");
		}
		if (StringUtils.isEmpty(sysUser.getPhoneNumber())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "PhoneNumber");
		}
		if (StringUtils.isEmpty(sysUser.getEmail())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "Email");
		}
		if (!StringUtils.isEmpty(sysUser.getPassword()) && !CheckUtil.isPwd(sysUser.getPassword())) {
			throw new ServiceException(RespCode.user_pwd_formatError);
		}
		if (!StringUtils.isEmpty(sysUser.getPhoneNumber()) && !CheckUtil.isPhone(sysUser.getPhoneNumber())) {
			throw new ServiceException(RespCode.PARAM_ILLEGAL);
		}
		if (!StringUtils.isEmpty(sysUser.getEmail()) && !CheckUtil.isEmail(sysUser.getEmail())) {
			throw new ServiceException(RespCode.PARAM_ILLEGAL);
		}

		// #设置变更人
		setLoginUser(sysUser, token);
		try {
			sysUserMapper.insert(sysUser);
		} catch (DuplicateKeyException e) {
			throw new ServiceException(RespCode.user_exists);
		}
		return true;
	}

	private void setLoginUser(SysUser sysUser, String token) {
		JSONObject loginUser = JSONObject.parseObject(RedisPool.get(RedisCons.loginToken + token));
		sysUser.setCreateUser(
				StringUtils.isEmpty(loginUser.getString("userName")) ? "system" : loginUser.getString("userName"));
	}

	public boolean update(SysUser sysUser, String token) {
		if (sysUser.getId() == 0) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "id");
		}
		if (!StringUtils.isEmpty(sysUser.getPassword()) && !CheckUtil.isPwd(sysUser.getPassword())) {
			throw new ServiceException(RespCode.user_pwd_formatError, ",必须包含大小写字母和数字的8~20位组合");
		}
		if (!StringUtils.isEmpty(sysUser.getPhoneNumber()) && !CheckUtil.isPhone(sysUser.getPhoneNumber())) {
			throw new ServiceException(RespCode.PARAM_ILLEGAL);
		}
		if (!StringUtils.isEmpty(sysUser.getEmail()) && !CheckUtil.isEmail(sysUser.getEmail())) {
			throw new ServiceException(RespCode.PARAM_ILLEGAL);
		}
		// #设置变更人
		setLoginUser(sysUser, token);
		return sysUserMapper.update(sysUser) > 0;
	}

	public boolean update4role(SysUser sysUser, String token) {
		if (sysUser.getId() == 0) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "id");
		}
		if (StringUtils.isEmpty(sysUser.getRefRoleIds())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "RefRoleIds");
		}
		if (!CheckUtil.isInteger(sysUser.getRefRoleIds())) {// 可以放开，以支持多角色
			throw new ServiceException(RespCode.user_only_singleRole);
		}
		// #设置变更人
		setLoginUser(sysUser, token);
		return sysUserMapper.update(sysUser) > 0;
	}

	public boolean resetPwd(JSONObject req, String token) {
		long id = req.getLongValue("id");
		String newPwd = req.getString("newPwd");
		String originPwd = req.getString("originPwd");
		if (id == 0) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "id");
		}
		if (StringUtils.isEmpty(newPwd) || StringUtils.isEmpty(originPwd)) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "newPwd,originPwd");
		}
		if (CheckUtil.isPwd(newPwd)) {
			throw new ServiceException(RespCode.user_pwd_formatError, ",必须包含大小写字母和数字的8~20位组合");
		}
		SysUser sysUser = new SysUser();
		sysUser.setId(req.getLongValue("id"));
		sysUser.setPassword(newPwd);
		// #设置变更人
		setLoginUser(sysUser, token);
		// 验证旧密码正确性:
		SysUser oldSysUser = sysUserMapper.selectOne(req.getLongValue("id"));
		if (oldSysUser != null && originPwd.equals(oldSysUser.getPassword())) {
			if (newPwd.equals(oldSysUser.getPassword()))
				throw new ServiceException(RespCode.user_pwd_exists);
			return sysUserMapper.update(sysUser) > 0;
		} else {
			throw new ServiceException(RespCode.user_pwd_error);
		}

	}

}
