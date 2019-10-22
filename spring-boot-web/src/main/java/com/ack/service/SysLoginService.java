/**
 * 
 */
package com.ack.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ack.common.RedisPool;
import com.ack.common.ServiceException;
import com.ack.constants.RedisCons;
import com.ack.entity.SysRole;
import com.ack.entity.SysUser;
import com.ack.enums.RespCode;
import com.ack.mapper.SysRoleMapper;
import com.ack.mapper.SysUserMapper;
import com.ack.utils.PropertiesUtils;

/**
 * 用户CRUD
 * 
 * @author chenzhao @date Oct 18, 2019
 */
@Service
public class SysLoginService {

	@Autowired
	SysUserMapper sysUserMapper;

	@Autowired
	SysRoleMapper sysRoleMapper;

	public Object userLogin(SysUser sysUser) {
		if (StringUtils.isEmpty(sysUser.getUserName()) || StringUtils.isEmpty(sysUser.getPassword())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "UserName,Password");
		}
		// 验证旧密码正确性:
		SysUser oldSysUser = sysUserMapper.selectOne4Login(sysUser.getUserName(), sysUser.getPhoneNumber());
		if (oldSysUser != null && sysUser.getPassword().equals(oldSysUser.getPassword())) {
			if ("0".equals(oldSysUser.getStatus())) {
				throw new ServiceException(RespCode.user_islimited);
			}
			if (1 == oldSysUser.getIsDel()) {
				throw new ServiceException(RespCode.user_isDeleted);
			}

			// 配置登录session-token
			String loginToken = UUID.randomUUID().toString().replaceAll("-", "");
			// 获取用户角色&权限数据
			if (StringUtils.isEmpty(oldSysUser.getRefRoleIds())) {
				throw new ServiceException(RespCode.user_role_unExists);
			}
			SysRole sysRole = sysRoleMapper.selectOne(Long.parseLong(oldSysUser.getRefRoleIds()));// 用户只支持单角色，多角色支持，变更此处
			if (StringUtils.isEmpty(sysRole.getPermissions())) {
				throw new ServiceException(RespCode.user_role_unExists);
			}
			oldSysUser.setPermissions(sysRole.getPermissions());
			RedisPool.set(RedisCons.loginToken + loginToken,
					Integer.parseInt(PropertiesUtils.getProperty("loginSessionTimeout", "86400")), oldSysUser);
			oldSysUser.setToken(loginToken);
			oldSysUser.setPassword(null);
			return oldSysUser;
		} else {
			throw new ServiceException(RespCode.user_pwd_error);
		}

	}

	public boolean userLogout(String token) {
		RedisPool.del(RedisCons.loginToken + token);
		return true;
	}
}
