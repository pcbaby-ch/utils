/**
 * 
 */
package com.ack.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.ack.entity.SysRole;

/**
 * 
 * @author chenzhao @date Oct 18, 2019
 */
@Repository
@Mapper
public interface SysRoleMapper {

	@Select(value = "<script>select `id`,`role_name`,`permissions`"
			+ " from sys_role" + "<where>" 
			+ "</where></script>")
			public List<SysRole> selectList();

			@Select(value = "<script>select `id`,`role_name`,`permissions`"
			+ " from sys_role" + "<where>" 
			+ " and `id` = #{id} " + "</where></script>")
			public SysRole selectOne(@Param(value = "id") Long id);

			@Insert(value = "INSERT INTO sys_role" 
			+ " (`role_name`,`permissions`,`create_user`,`create_time`,`last_update_user`,`last_update_time`) " 
			+ " VALUES (#{roleName},#{permissions},#{createUser},NOW(),#{createUser},NOW())")
			public int insert(SysRole sysRole);

			@Update(value = "<script>UPDATE sys_role SET  `last_update_time`=NOW()"
			+ "<if test=\"roleName != null and roleName != '' \">"	+ ",`role_name` = #{roleName}" + "</if>" 
			+ "<if test=\"permissions != null and permissions != '' \">"	+ ",`permissions` = #{permissions}" + "</if>" 
			+ "<if test=\"lastUpdateUser != null and lastUpdateUser != '' \">"	+ ",`last_update_user` = #{createUser}" + "</if>" 
			+"where id=#{id}"
			+ "</script>")
			public int update(SysRole sysRole);
			
			@Delete(value = "DELETE from sys_role where `id`=#{id}")
			public int delete(@Param(value = "id") Long id);

}
