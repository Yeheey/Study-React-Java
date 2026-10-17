package com.student.manager.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface SysUserRoleMapper {

    @Insert("INSERT INTO sys_user_role (user_id, role_id) VALUES (#{userId}, #{roleId})")
    void insert(@Param("userId") Long userId, @Param("roleId") Long roleId);

    @Delete("DELETE FROM sys_user_role WHERE user_id = #{userId}")
    void deleteByUserId(@Param("userId") Long userId);

    @Delete("DELETE FROM sys_user_role WHERE user_id = #{userId} AND role_id = #{roleId}")
    void delete(@Param("userId") Long userId, @Param("roleId") Long roleId);

    @Select("SELECT role_id FROM sys_user_role WHERE user_id = #{userId}")
    java.util.List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);
}
