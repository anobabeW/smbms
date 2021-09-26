package com.ano.service.role;

import com.ano.dao.BaseDao;
import com.ano.dao.role.RoleDao;
import com.ano.dao.role.RoleDaoImpl;
import com.ano.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjiao
 */
public class RoleServiceImpl implements RoleService{

    private RoleDao roleDao;
    public RoleServiceImpl() {
        roleDao = new RoleDaoImpl();
    }

    /**
     * 获取角色列表
     * @return
     */
    @Override
    public List<Role> getRoleList() {
        List<Role> roleList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            roleList = roleDao.getRoleList(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return roleList;
    }

    /*@Test
    public void test() {
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        for (Role role : roleList) {
            System.out.println(role.getRoleName());
        }

    }*/
}
