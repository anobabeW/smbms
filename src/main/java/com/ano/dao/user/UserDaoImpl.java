package com.ano.dao.user;

import com.ano.dao.BaseDao;
import com.ano.pojo.User;
import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjiao
 */
public class UserDaoImpl implements UserDao{
    /**
     * 得到要登录的用户
     * @param connection
     * @param userCode
     * @return
     * @throws SQLException
     */
    @Override
    public User getLoginUser(Connection connection, String userCode) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        if(connection != null) {
            String sql = "SELECT * FROM smbms_user WHERE userCode = ?";
            Object[] params = {userCode};
            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql, params);
            if(resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setUserPassword(resultSet.getString("userPassword"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setCreateBy(resultSet.getInt("createdBy"));
                user.setCreationDate(resultSet.getDate("creationDate"));
                user.setModifyBy(resultSet.getInt("modifyBy"));
                user.setModifyDate(resultSet.getDate("modifyDate"));
            }
            BaseDao.closeResource(null,preparedStatement,resultSet);
        }
        return user;
    }

    /**
     * 修改当前用户密码
     * @param connection
     * @param id
     * @param password
     * @return
     * @throws SQLException
     */
    @Override
    public int updatePwd(Connection connection, int id, String password) throws SQLException {
        PreparedStatement preparedStatement = null;
        int execute = 0;
        if(connection != null) {
            String sql = "UPDATE smbms_user SET userPassword=? WHERE id = ?";
            Object[] params = {password, id};
            execute = BaseDao.executeUpdate(connection,preparedStatement,sql,params);
            BaseDao.closeResource(null,preparedStatement,null);
        }
        return execute;
    }

    /**
     * 根据用户名或者角色查询用户总数
     * @param connection
     * @param userName
     * @param userRole 用户角色id
     * @return
     */
    @Override
    public int getUserCount(Connection connection, String userName, int userRole) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int count = 0;
        if (connection != null) {
            /*
            sql示例：
            select count(1) as count
            from smbms_user u, smbms_role r
            where u.userRole = r.id
            and u.userName like '%孙%'
            */
            StringBuffer sql = new StringBuffer();
            sql.append("select count(1) as count from smbms_user u, smbms_role r where u.userRole = r.id");
            List<Object> params= new ArrayList<>();
            if(!StringUtils.isNullOrEmpty(userName)) {
                //注意and前面要加空格
                sql.append(" and u.userName like ?");
                //模糊查询需要加%
                params.add("%" + userName + "%");
            }
            if(userRole > 0) {
                sql.append(" and u.userRole = ?");
                params.add(userRole);
            }
            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql.toString(), params.toArray());
            if(resultSet.next()) {
                count = resultSet.getInt("count");
            }
            BaseDao.closeResource(null,preparedStatement,resultSet);
        }
        return count;
    }

    /**
     * 查询用户列表
     * @param connection
     * @param userName
     * @param userRole
     * @param currentPageNo
     * @param pageSize
     * @return
     * @throws SQLException
     */
    @Override
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> userList = new ArrayList<>();
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*, r.roleName as userRoleName from smbms_user u, smbms_role r where u.userRole = r.id");
            List<Object> params = new ArrayList<>();
            if (!StringUtils.isNullOrEmpty(userName)) {
                sql.append(" and u.userName like ?");
                params.add("%" + userName + "%");
            }
            if (userRole > 0) {
                sql.append(" and u.userRole = ?");
                params.add(userRole);
            }
            //使用limit分页： limit startIndex, pageSize
            sql.append(" order by u.creationDate DESC limit ?,?");
            currentPageNo = (currentPageNo - 1) * pageSize;
            params.add(currentPageNo);
            params.add(pageSize);

            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql.toString(), params.toArray());

            while (resultSet.next()) {
                User user =  new User();
                user.setId(resultSet.getInt("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setUserRoleName(resultSet.getString("userRoleName"));

                userList.add(user);
            }
            BaseDao.closeResource(null, preparedStatement, resultSet);

        }
        return userList;
    }

    /**
     * 根据userCode查询用户
     * @param connection
     * @param userCode
     * @return
     * @throws SQLException
     */
    @Override
    public User getUserByCode(Connection connection, String userCode) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        if(connection != null) {
            String sql = "SELECT * FROM smbms_user WHERE userCode =  ?";
            Object[] params = {userCode};
            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql, params);
            if(resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setUserPassword(resultSet.getString("userPassword"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setCreateBy(resultSet.getInt("createdBy"));
                user.setCreationDate(resultSet.getDate("creationDate"));
                user.setModifyBy(resultSet.getInt("modifyBy"));
                user.setModifyDate(resultSet.getDate("modifyDate"));
            }
            BaseDao.closeResource(null,preparedStatement,resultSet);
        }
        return user;
    }

    /**
     * 添加用户
     * @param connection
     * @param user
     * @return
     * @throws SQLException
     */
    @Override
    public int addUser(Connection connection, User user) throws SQLException {
        PreparedStatement preparedStatement = null;
        int updateRows = 0;
        if(connection != null) {
            String sql = "INSERT INTO smbms_user (userCode, userName, userPassword, gender, birthday, phone, address, userRole, createdBy, creationDate)" +
                    "VALUES (?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {user.getUserCode(), user.getUserName(), user.getUserPassword(), user.getGender(),
                    user.getBirthday(), user.getPhone(), user.getAddress(), user.getUserRole(), user.getCreateBy(),
                    user.getCreationDate()};

            updateRows = BaseDao.executeUpdate(connection, preparedStatement, sql, params);
            BaseDao.closeResource(null,preparedStatement,null);
        }
        return updateRows;
    }

    /**
     * 根据用户ID删除用户记录
     * @param connection
     * @param userId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteUser(Connection connection, int userId) throws SQLException {
        PreparedStatement preparedStatement = null;
        int updateRows = 0;
        if(connection != null) {
            String sql = "DELETE FROM smbms_user WHERE id = ?";
            Object[] params = {userId};
            updateRows = BaseDao.executeUpdate(connection, preparedStatement, sql, params);
            BaseDao.closeResource(null,preparedStatement,null);
        }
        return updateRows;
    }

    /**
     * 根据用户Id查询用户
     * @param connection
     * @param userId
     * @return
     * @throws SQLException
     */
    @Override
    public User getUserById(Connection connection, Integer userId) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        if(connection != null) {
            String sql = "SELECT u.*,r.roleName as userRoleName FROM smbms_user u, smbms_role r WHERE u.userRole = r.id AND u.id = ?";
            Object[] params = {userId};
            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql, params);
            if(resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setUserPassword(resultSet.getString("userPassword"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setCreateBy(resultSet.getInt("createdBy"));
                user.setCreationDate(resultSet.getDate("creationDate"));
                user.setModifyBy(resultSet.getInt("modifyBy"));
                user.setModifyDate(resultSet.getDate("modifyDate"));
                user.setUserRoleName(resultSet.getString("userRoleName"));
            }
            BaseDao.closeResource(null,preparedStatement,resultSet);
        }
        return user;
    }

    /**
     * 修改用户
     * @param connection
     * @param user
     * @return
     * @throws SQLException
     */
    @Override
    public int modifyUser(Connection connection, User user) throws SQLException {
        /**
         * UPDATE smbms_user
         * SET phone = '13488888888'
         * WHERE id = 1
         */
        PreparedStatement preparedStatement = null;
        int updateRows = 0;
        if(connection != null) {
            String sql = "UPDATE smbms_user SET userName=?,gender=?,birthday=?,phone=?,address=?,userRole=?," +
                    "modifyBy=?,modifyDate=? WHERE id = ?";

            Object[] params = {user.getUserName(),user.getGender(), user.getBirthday(), user.getPhone(),
                    user.getAddress(), user.getUserRole(),user.getModifyBy(),user.getModifyDate(), user.getId()};

            updateRows = BaseDao.executeUpdate(connection, preparedStatement, sql, params);
            BaseDao.closeResource(null, preparedStatement, null);
        }
        return updateRows;
    }

}
