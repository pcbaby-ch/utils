/**
 * 
 */
package com.ack.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.ack.entity.SysUser;

/**
 * 
 * @author chenzhao @date Oct 18, 2019
 */
@Repository
@Mapper
public interface SysUserMapper {

	@Select(value = "<script>select `id`,`user_name`,`name`,`title`,`phone_number`,`email`,`status`,`ref_role_ids`"
	+ " from sys_user" + "<where>" 
	+" and is_del=0"
	+ "</where></script>")
	public List<SysUser> selectList();

	@Select(value = "<script>select `id`,`user_name`,`password`,`name`,`title`,`phone_number`,`email`,`status`,`ref_role_ids`"
	+ " from sys_user" + "<where>" 
	+ " and `id` = #{id} " + "</where></script>")
	public SysUser selectOne(@Param(value = "id") Long id);
	
	@Select(value = "<script>select `id`,`user_name`,`password`,`name`,`title`,`phone_number`,`email`,`status`,`ref_role_ids`"
	+ " from sys_user" + "<where>" 
	+ "<if test=\"userName != null and userName != '' \">"	+ " and `user_name` = #{userName}" + "</if>" 
	+ "<if test=\"phoneNumber != null and phoneNumber != '' \">"	+ " and `phone_number` = #{phoneNumber}" + "</if>" 
	 + "</where></script>")
	public SysUser selectOne4Login(@Param(value = "userName") String userName,@Param(value = "phoneNumber") String phoneNumber);

	@Insert(value = "INSERT INTO sys_user" 
	+ " (`user_name`,`password`,`name`,`title`,`phone_number`,`email`,`ref_role_ids`,`create_user`,`create_time`,`last_update_user`,`last_update_time`) " 
	+ " VALUES (#{userName},#{password},#{name},#{title},#{phoneNumber},#{email},#{refRoleIds},#{createUser},NOW(),#{createUser},NOW())")
	public int insert(SysUser sysUser);

	@Update(value = "<script>UPDATE sys_user SET  `last_update_time`=NOW()"
	+ "<if test=\"userName != null and userName != '' \">"	+ ",`user_name` = #{userName}" + "</if>" 
	+ "<if test=\"password != null and password != '' \">"	+ ",`password` = #{password}" + "</if>" 
	+ "<if test=\"name != null and name != '' \">"	+ ",`name` = #{name}" + "</if>" 
	+ "<if test=\"title != null and title != '' \">"	+ ",`title` = #{title}" + "</if>" 
	+ "<if test=\"phoneNumber != null and phoneNumber != '' \">"	+ ",`phone_number` = #{phoneNumber}" + "</if>" 
	+ "<if test=\"email != null and email != '' \">"	+ ",`email` = #{email}" + "</if>" 
	+ "<if test=\"status != null and status != '' \">"	+ ",`status` = #{status}" + "</if>" 
	+ "<if test=\"refRoleIds != null and refRoleIds != '' \">"	+ ",`ref_role_ids` = #{refRoleIds}" + "</if>" 
	+ "<if test=\"lastUpdateUser != null and lastUpdateUser != '' \">"	+ ",`last_update_user` = #{createUser}" + "</if>" 
	+ "<if test=\"isDel != null and isDel != '' \">"	+ ",`is_del` = #{isDel}" + "</if>" 
	+"where id=#{id}"
	+ "</script>")
	public int update(SysUser sysUser);

}
