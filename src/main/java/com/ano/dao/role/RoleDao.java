package com.ano.dao.role;

import com.ano.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author wangjiao
 */
public interface RoleDao {
    /**
     *
     * @param connection
     * @return
     */
    List<Role> getRoleList(Connection connection) throws SQLException;
}
