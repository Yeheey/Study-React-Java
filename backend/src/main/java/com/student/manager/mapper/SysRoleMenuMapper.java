package com.student.manager.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface SysRoleMenuMapper {

    @Insert("INSERT INTO sys_role_menu (role_id, menu_id) VALUES (#{roleId}, #{menuId})")
    void insert(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    @Delete("DELETE FROM sys_role_menu WHERE role_id = #{roleId}")
    void deleteByRoleId(@Param("roleId") Long roleId);
}
