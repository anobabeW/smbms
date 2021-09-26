package com.ano.dao.provider;

import com.ano.dao.BaseDao;
import com.ano.pojo.Bill;
import com.ano.pojo.Provider;
import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangjiao
 * @Date 2021/9/9 22:56
 * @Version 1.0
 */


public class ProviderDaoImpl implements ProviderDao{
    /**
     * 根据供应商编码或者名称查询供应商
     * @param connection
     * @param queryProCode
     * @param queryProName
     * @param currentPageNo
     * @param pageSize
     * @return
     * @throws SQLException
     */
    @Override
    public List<Provider> getProviderList(Connection connection, String queryProCode, String queryProName, int currentPageNo, int pageSize) throws SQLException {
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        List<Provider> providerList = new ArrayList<>();
        if(connection != null) {
            StringBuffer sql = new StringBuffer();
            List<Object> params = new ArrayList<>();
            sql.append("SELECT * FROM smbms_provider WHERE 1=1");
            if(!StringUtils.isNullOrEmpty(queryProCode)) {
                sql.append(" AND proCode like ?");
                params.add("%" + queryProCode + "%");
            }
            if(!StringUtils.isNullOrEmpty(queryProName)) {
                sql.append(" AND proName like ?");
                params.add("%" + queryProName + "%");
            }
            //分页
            if(pageSize != 0) {
                sql.append(" ORDER BY creationDate DESC LIMIT ? , ?");
                //注意这里计算当前页
                currentPageNo = (currentPageNo - 1) * pageSize;
                params.add(currentPageNo);
                params.add(pageSize);
            }

            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql.toString(), params.toArray());
            while (resultSet.next()) {
                Provider provider = new Provider();
                provider.setId(resultSet.getInt("id"));
                provider.setProCode(resultSet.getString("proCode"));
                provider.setProName(resultSet.getString("proName"));
                provider.setProDesc(resultSet.getString("proDesc"));
                provider.setProContact(resultSet.getString("proContact"));
                provider.setProPhone(resultSet.getString("proPhone"));
                provider.setProAddress(resultSet.getString("proAddress"));
                provider.setProFax(resultSet.getString("proFax"));
                provider.setCreatedBy(resultSet.getInt("createdBy"));
                provider.setCreationDate(resultSet.getDate("creationDate"));
                provider.setModifyBy(resultSet.getInt("modifyBy"));
                provider.setModifyDate(resultSet.getDate("modifyDate"));

                providerList.add(provider);
            }
            BaseDao.closeResource(null,preparedStatement,resultSet);
        }
        return providerList;
    }

    /**
     * 获取供应商数量
     * @param connection
     * @param queryProCode
     * @param queryProName
     * @return
     * @throws SQLException
     */
    @Override
    public int getProviderCount(Connection connection, String queryProCode, String queryProName) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        int count = 0;
        if(connection != null) {
            StringBuffer sql = new StringBuffer();
            List<Object> params = new ArrayList<>();
            sql.append("SELECT COUNT(1) as count FROM smbms_provider WHERE 1=1");

            if(!StringUtils.isNullOrEmpty(queryProCode)) {
                sql.append(" AND proCode like ?");
                params.add("%" + queryProCode +  "%");
            }
            if(!StringUtils.isNullOrEmpty(queryProName)) {
                sql.append(" AND proName like ?");
                params.add("%" + queryProName +  "%");
            }

            rs = BaseDao.execute(connection,preparedStatement,rs,sql.toString(),params.toArray());
            while (rs.next()) {
                count = rs.getInt("count");
            }
            BaseDao.closeResource(null,preparedStatement,rs);
        }
        return count;
    }

    /**
     * 添加供应商
     * @param connection
     * @param provider
     * @return
     * @throws SQLException
     */
    @Override
    public int addProvider(Connection connection, Provider provider) throws SQLException {
        int updateRows = 0;
        PreparedStatement preparedStatement = null;

        if(connection != null) {
            String sql = "INSERT INTO smbms_provider (proCode, proName, proDesc, proContact, proPhone, proAddress, " +
                    "proFax, createdBy, creationDate) VALUES " +
                    "(?,?,?,?,?,?,?,?,?)";
            Object[] params = {provider.getProCode(), provider.getProName(), provider.getProDesc(),
                    provider.getProContact(),provider.getProPhone(),provider.getProAddress(),provider.getProFax(),
                    provider.getCreatedBy(),provider.getCreationDate()};
            updateRows = BaseDao.executeUpdate(connection, preparedStatement, sql, params);
            BaseDao.closeResource(null,preparedStatement,null);
        }
        return updateRows;
    }

    /**
     * 根据id删除供应商
     * @param connection
     * @param proId
     * @return
     * @throws SQLException
     */
    @Override
    public int delProvider(Connection connection, int proId) throws SQLException {
        int updateRows = 0;
        PreparedStatement preparedStatement = null;
        if(connection != null) {
            String sql = "DELETE FROM smbms_provider WHERE id = ?";
            Object[] params = {proId};

            updateRows = BaseDao.executeUpdate(connection,preparedStatement,sql,params);
            BaseDao.closeResource(null,preparedStatement,null);
        }
        return updateRows;
    }

    /**
     * 根据Id查询该供应商是否存在未支付的订单
     * @param connection
     * @param proId
     * @return List
     * @throws SQLException
     */
    @Override
    public List<Bill> noPayBill(Connection connection, int proId) throws SQLException {
        List<Bill> billList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        if(connection != null) {
            String sql = "SELECT b.* FROM smbms_bill b, smbms_provider p WHERE b.providerId = p.id " +
                    "AND b.isPayment = 1 AND p.id = ?";
            Object[] params = {proId};

            resultSet = BaseDao.execute(connection,preparedStatement,resultSet,sql,params);
            while (resultSet.next()) {
                Bill bill = new Bill();
                bill.setId(resultSet.getInt("id"));
                bill.setBillCode(resultSet.getString("billCode"));
                bill.setProductName(resultSet.getString("productName"));
                bill.setProductUnit(resultSet.getString("productUnit"));
                bill.setProductCount(resultSet.getBigDecimal("productCount"));
                bill.setTotalPrice(resultSet.getBigDecimal("totalPrice"));
                bill.setIsPayment(resultSet.getInt("isPayment"));
                bill.setCreatedBy(resultSet.getInt("createdBy"));
                bill.setCreationDate(resultSet.getDate("creationDate"));
                bill.setModifyBy(resultSet.getInt("modifyBy"));
                bill.setModifyDate(resultSet.getDate("modifyDate"));
                bill.setProviderId(resultSet.getInt("providerId"));

                billList.add(bill);
            }
            BaseDao.closeResource(null,preparedStatement,resultSet);
        }
        return billList;
    }

    /**
     * 根据Id获取供应商
     * @param connection
     * @param proId
     * @return
     * @throws SQLException
     */
    @Override
    public Provider getProviderById(Connection connection, int proId) throws SQLException {
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Provider provider = null;
        if(connection != null) {
            String sql = "SELECT * FROM smbms_provider WHERE id = ?";
            Object[] params = {proId};
            resultSet = BaseDao.execute(connection,preparedStatement,resultSet,sql,params);

            while (resultSet.next()) {
                provider = new Provider();
                provider.setId(resultSet.getInt("id"));
                provider.setProCode(resultSet.getString("proCode"));
                provider.setProName(resultSet.getString("proName"));
                provider.setProContact(resultSet.getString("proContact"));
                provider.setProPhone(resultSet.getString("proPhone"));
                provider.setProAddress(resultSet.getString("proAddress"));
                provider.setProFax(resultSet.getString("proFax"));
                provider.setProDesc(resultSet.getString("proDesc"));
                provider.setCreatedBy(resultSet.getInt("createdBy"));
                provider.setCreationDate(resultSet.getDate("creationDate"));
                provider.setModifyBy(resultSet.getInt("modifyBy"));
                provider.setModifyDate(resultSet.getDate("modifyDate"));
            }
            BaseDao.closeResource(null,preparedStatement,resultSet);

        }
        return provider;
    }

    /**
     * 根据proCode查询供应商
     * @param connection
     * @param proCode
     * @return
     * @throws SQLException
     */
    @Override
    public Provider getProviderByProCode(Connection connection, String proCode) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Provider provider = null;
        if(connection != null) {
            String sql = "SELECT * FROM smbms_provider WHERE proCode = ?";
            Object[] params = {proCode};
            resultSet = BaseDao.execute(connection,preparedStatement,resultSet,sql,params);
            while (resultSet.next()) {
                provider = new Provider();
                provider.setId(resultSet.getInt("id"));
                provider.setProCode(resultSet.getString("proCode"));
                provider.setProName(resultSet.getString("proName"));
                provider.setProContact(resultSet.getString("proContact"));
                provider.setProPhone(resultSet.getString("proPhone"));
                provider.setProAddress(resultSet.getString("proAddress"));
                provider.setProFax(resultSet.getString("proFax"));
                provider.setProDesc(resultSet.getString("proDesc"));
                provider.setCreatedBy(resultSet.getInt("createdBy"));
                provider.setCreationDate(resultSet.getDate("creationDate"));
                provider.setModifyBy(resultSet.getInt("modifyBy"));
                provider.setModifyDate(resultSet.getDate("modifyDate"));
            }
            BaseDao.closeResource(null,preparedStatement,resultSet);

        }
        return provider;
    }

    /**
     * 修改供应商信息
     * @param connection
     * @param provider
     * @return
     * @throws SQLException
     */
    @Override
    public int modifyProvider(Connection connection, Provider provider) throws SQLException {
        int updateRows = 0;
        PreparedStatement preparedStatement = null;
        if(connection != null) {
            String sql = "UPDATE smbms_provider SET proCode = ?, proName = ?, proContact = ?, proPhone = ?," +
                    "proAddress = ?, proFax = ?, proDesc = ?, modifyBy = ?, modifyDate = ? WHERE id = ?";
            Object[] params = {provider.getProCode(),provider.getProName(),provider.getProContact(),
                    provider.getProPhone(),provider.getProAddress(),provider.getProFax(),provider.getProDesc(),
                    provider.getModifyBy(),provider.getModifyDate(),provider.getId()};
            updateRows = BaseDao.executeUpdate(connection,preparedStatement,sql,params);
            BaseDao.closeResource(null,preparedStatement,null);
        }
        return updateRows;
    }

}
