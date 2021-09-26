package com.ano.service.user;

import com.ano.pojo.User;
import java.util.List;

/**
 * @author wangjiao
 */
public interface UserService {
    /**
     * 用户登录
     * @param userCode
     * @param password
     * @return
     */
    User login(String userCode, String password);

    /**
     * 根据当前用户ID修改密码
     * @param id
     * @param password
     * @return
     */
    boolean updatePwd(int id, String password);

    /**
     * 根据用户名和用户角色查询记录数
     * @param userName
     * @param userRole
     * @return
     */
    int getUserCount(String userName, int userRole);

    /**
     * 根据用户名、用户角色查用户列表
     * @param userName
     * @param userRole
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize);


    /**
     * 添加用户
     * @param user
     * @return
     */
    boolean addUser(User user);

    /**
     * 删除用户
     * @param userId
     * @return
     */
    boolean deleteUser(int userId);


    /**
     * 根据用户Id查询用户信息
     * @param userId
     * @return
     */
    User getUserById(Integer userId);

    /**
     * 修改用户
     * @param user
     * @return
     */
    boolean modifyUser(User user);

    /**
     * 根据userCode查询用户
     * @param userCode
     * @return
     */
    User getUSerByCode(String userCode);


}
