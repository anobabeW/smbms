package com.ano.dao.user;

import com.ano.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author wangjiao
 */
public interface UserDao {
    /**
     * 得到要登录的用户
     * @param connection
     * @param userCode
     * @return
     */
    User getLoginUser(Connection connection, String userCode) throws SQLException;

    /**
     * 修改当前用户密码
     * @param connection
     * @param id
     * @param password
     * @return
     */
    int updatePwd(Connection connection,int id, String password) throws SQLException;

    /**
     * 根据用户名或者用户角色ID查询用户总数量
     * @param connection
     * @param userName
     * @param userRole
     * @return
     * @throws SQLException
     */
    int getUserCount(Connection connection, String userName, int userRole) throws SQLException;

    /**
     * 根据条件查询用户列表
     * @param connection
     * @param userName
     * @param userRole
     * @param currentPageNo
     * @param pageSize
     * @return
     * @throws SQLException
     */
    List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException;

    /**
     * 添加用户
     * @param connection
     * @param user
     * @return
     */
    int addUser(Connection connection, User user) throws SQLException;

    /**
     * 根据userId删除用户
     * @param connection
     * @param userId
     * @return
     */
    int deleteUser(Connection connection, int userId) throws SQLException;

    /**
     * 根据userId获取用户信息
     * @param connection
     * @param userId
     * @return
     */
    User getUserById(Connection connection, Integer userId) throws SQLException;

    /**
     * 根据userCode查询用户，用于添加用户时判断该账号是否可用
     * @param connection
     * @param userCode
     * @return
     * @throws SQLException
     */
    User getUserByCode(Connection connection, String userCode) throws SQLException;

    /**
     * 修改用户信息
     * @param connection
     * @param user
     * @return
     * @throws SQLException
     */
    int modifyUser(Connection connection, User user) throws SQLException;

}
