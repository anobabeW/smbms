package com.ano.service.user;

import com.ano.dao.BaseDao;
import com.ano.dao.user.UserDao;
import com.ano.dao.user.UserDaoImpl;
import com.ano.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author wangjiao
 */
public class UserServiceImpl implements UserService {

    //业务层都会调用dao层，所以要引入Dao层
    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();

    }

    /**
     * 得到登录用户
     * @param userCode
     * @param password
     * @return
     */
    @Override
    public User login(String userCode, String password) {
        Connection connection = null;
        User user = null;
        connection = BaseDao.getConnection();
        try {
            //通过业务层调用对应的具体的数据库操作
            user = userDao.getLoginUser(connection, userCode);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return user;
    }

    /**
     * 修改密码是否成功
     * @param id
     * @param password
     * @return
     */
    @Override
    public boolean updatePwd(int id, String password) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            //修改密码
            if(userDao.updatePwd(connection,id,password) > 0) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //关闭资源
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    /**
     * 查询记录数
     * @param userName
     * @param userRole
     * @return
     */
    @Override
    public int getUserCount(String userName, int userRole) {
        Connection connection = null;
        int userCount = 0;
        try {
            connection = BaseDao.getConnection();
            userCount = userDao.getUserCount(connection, userName, userRole);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return userCount;
    }

    /**
     * 根据条件获取用户列表
     * @param userName
     * @param userRole
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
        List<User> userList = new ArrayList<>();
        Connection connection = null;

        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, userName, userRole, currentPageNo, pageSize);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null,null);
        }
        return userList;
    }

    /**
     * 根据userCode查询用户
     * @param userCode
     * @return
     */
    @Override
    public User getUSerByCode(String userCode) {
        Connection connection = null;
        User user = null;

        connection = BaseDao.getConnection();
        try {
            user = userDao.getUserByCode(connection, userCode);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return user;
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @Override
    public boolean addUser(User user) {
        int count = 0;
        boolean flag = false;
        Connection connection = null;

        try {
            connection = BaseDao.getConnection();
            //关闭自动提交，开启事务
            connection.setAutoCommit(false);
            count = userDao.addUser(connection, user);
            //执行完添加操作，一定要提交事务！！
            connection.commit();
            if(count > 0) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    /**
     * 根据id删除用户记录
     * @param userId
     * @return
     */
    @Override
    public boolean deleteUser(int userId) {
        Connection connection = null;
        boolean flag = false;
        int count = 0;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            count = userDao.deleteUser(connection, userId);
            connection.commit();
            if(count > 0) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;

    }

    /**
     * 根据Id获取用户
     * @param userId
     * @return
     */
    @Override
    public User getUserById(Integer userId) {
        Connection connection = null;
        User user = null;

        connection = BaseDao.getConnection();
        try {
            user = userDao.getUserById(connection, userId);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return user;

    }

    /**
     * 修改用户
     * @param user
     * @return
     */
    @Override
    public boolean modifyUser(User user) {
        Connection connection = null;
        int count = 0;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            count = userDao.modifyUser(connection, user);
            connection.commit();
            if(count > 0) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }


    /*@Test
    public void test() {
        UserServiceImpl userService = new UserServiceImpl();

        *//*List<User> userList = userService.getUserList("孙", 3, 1, 5);
        for (User user : userList) {
            System.out.println(user.getUserName());
        }
        int userCount = userService.getUserCount("孙", 3);
        System.out.println(userCount);
        User admim = userService.login("admin", "1111");
        System.out.println(admim.getUserPassword());*//*
    }*/
}
