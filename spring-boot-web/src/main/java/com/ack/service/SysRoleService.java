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
import com.ack.entity.SysRole;
import com.ack.entity.SysUser;
import com.ack.enums.RespCode;
import com.ack.mapper.SysRoleMapper;

/**
 * 角色CRUD
 * 
 * @author chenzhao @date Oct 18, 2019
 */
@Service
public class SysRoleService {

	@Autowired
	SysRoleMapper sysRoleMapper;

	public List<SysRole> selectList() {
		return sysRoleMapper.selectList();
	}

	public SysRole selectOne(long id) {
		if (id == 0) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "id");
		}
		return sysRoleMapper.selectOne(id);
	}

	public boolean insert(SysRole sysRole, String token) {
		if (StringUtils.isEmpty(sysRole.getRoleName())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "RoleName");
		}
		// #设置变更人
		setLoginUser(sysRole, token);
		int count;
		try {
			count = sysRoleMapper.insert(sysRole);
		} catch (DuplicateKeyException e) {
			throw new ServiceException(RespCode.role_exists);
		}
		return count > 0;
	}

	public boolean update(SysRole sysRole, String token) {
		if (sysRole.getId() == 0) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "id");
		}
		// #设置变更人
		setLoginUser(sysRole, token);
		int count = sysRoleMapper.update(sysRole);
		return count > 0;
	}

	public boolean delete(long id) {
		if (id == 0) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "id");
		}
		return sysRoleMapper.delete(id) > 0;
	}

	private void setLoginUser(SysRole sysRole, String token) {
		JSONObject loginUser = JSONObject.parseObject(RedisPool.get(RedisCons.loginToken + token));
		sysRole.setCreateUser(
				StringUtils.isEmpty(loginUser.getString("userName")) ? "system" : loginUser.getString("userName"));
	}

}
