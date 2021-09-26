package com.ano.service.role;

import com.ano.pojo.Role;

import java.util.List;

/**
 * @author wangjiao
 */
public interface RoleService {
    /**
     * 查询角色列表
     * @return
     */
    List<Role> getRoleList();
}
