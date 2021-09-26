package com.ano.dao.bill;

import com.ano.dao.BaseDao;
import com.ano.pojo.Bill;
import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangjiao
 * @Date 2021/9/12 14:55
 * @Version 1.0
 */


public class BillDaoImpl implements BillDao{

    /**
     * 订单管理页面（根据条件查询，分页）
     * @param connection
     * @param productName
     * @param providerId
     * @param isPayment
     * @param currentPageNo
     * @param pageSize
     * @return
     * @throws SQLException
     */
    @Override
    public List<Bill> getBillList(Connection connection,
                                  String productName,
                                  int providerId,
                                  int isPayment,
                                  int currentPageNo,
                                  int pageSize) throws SQLException {
        List<Bill> billList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        if(connection != null) {
            StringBuffer sql = new StringBuffer();
            List<Object> params = new ArrayList<>();
            sql.append("SELECT * FROM smbms_bill b, smbms_provider p WHERE b.providerId = p.id");
            if(!StringUtils.isNullOrEmpty(productName)) {
                sql.append(" AND b.productName like ?");
                params.add("%" + productName + "%");
            }
            if(providerId != 0) {
                sql.append(" AND b.providerId = ?");
                params.add(providerId);
            }
            if(isPayment != 0) {
                sql.append(" AND b.isPayment = ?");
                params.add(isPayment);
            }
            currentPageNo = (currentPageNo - 1) * pageSize;
            sql.append(" ORDER BY b.creationDate DESC LIMIT ?,?");
            params.add(currentPageNo);
            params.add(pageSize);

            resultSet = BaseDao.execute(connection,preparedStatement,resultSet,sql.toString(),params.toArray());
            while (resultSet.next()) {
                Bill bill = new Bill();
                bill.setId(resultSet.getInt("id"));
                bill.setBillCode(resultSet.getString("billCode"));
                bill.setProductName(resultSet.getString("productName"));
                bill.setProductDesc(resultSet.getString("productDesc"));
                bill.setProductUnit(resultSet.getString("productUnit"));
                bill.setProductCount(resultSet.getBigDecimal("productCount"));
                bill.setTotalPrice(resultSet.getBigDecimal("totalPrice"));
                bill.setIsPayment(resultSet.getInt("isPayment"));
                bill.setCreatedBy(resultSet.getInt("createdBy"));
                bill.setCreationDate(resultSet.getDate("creationDate"));
                bill.setModifyBy(resultSet.getInt("modifyBy"));
                bill.setModifyDate(resultSet.getDate("modifyDate"));
                bill.setProviderId(resultSet.getInt("providerId"));
                //供应商名称
                bill.setProviderName(resultSet.getString("proName"));

                billList.add(bill);
            }
            BaseDao.closeResource(null,preparedStatement,resultSet);
        }

        return billList;
    }

    /**
     * 根据条件获取订单数量
     * @param connection
     * @param productName
     * @param providerId
     * @param isPayment
     * @return
     * @throws SQLException
     */
    @Override
    public int getBillCount(Connection connection, String productName, int providerId, int isPayment) throws SQLException {
        int count = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        if(connection != null) {
            StringBuffer sql = new StringBuffer();
            List<Object> params = new ArrayList<>();
            //注意这里的连表查询
            sql.append("SELECT COUNT(1) as count FROM smbms_bill b, smbms_provider p WHERE b.providerId = p.id");
            if(!StringUtils.isNullOrEmpty(productName)) {
                //注意这里模糊查询
                sql.append(" AND b.productName like ?");
                params.add("%" + productName + "%");
            }
            if(providerId != 0) {
                sql.append(" AND b.providerId = ?");
                params.add(providerId);
            }
            if(isPayment != 0) {
                sql.append(" AND b.isPayment = ?");
                params.add(isPayment);
            }
            resultSet = BaseDao.execute(connection,preparedStatement,resultSet,sql.toString(),params.toArray());
            while (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            BaseDao.closeResource(null,preparedStatement,resultSet);
        }
        return count;
    }

    /**
     * 添加订单
     * @param connection
     * @param bill
     * @return
     * @throws SQLException
     */
    @Override
    public int addBill(Connection connection, Bill bill) throws SQLException {
        PreparedStatement preparedStatement = null;
        int updateRows = 0;
        if(connection != null) {
            String sql = "INSERT INTO smbms_bill (billCode, productName, productDesc, productUnit, productCount, " +
                    "totalPrice, isPayment, createdBy, creationDate, providerId) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {bill.getBillCode(),bill.getProductName(),bill.getProductDesc(),bill.getProductUnit(),
                    bill.getProductCount(),bill.getTotalPrice(),bill.getIsPayment(),bill.getCreatedBy(),
                    bill.getCreationDate(),bill.getProviderId()};
            updateRows = BaseDao.executeUpdate(connection, preparedStatement,sql,params);
            BaseDao.closeResource(null, preparedStatement, null);
        }
        return updateRows;
    }

    /**
     * 根据billCode查询订单
     * @param connection
     * @param billCode
     * @return
     * @throws SQLException
     */
    @Override
    public Bill getBillByCode(Connection connection, String billCode) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Bill bill = null;
        if(connection != null) {
            String sql = "SELECT * FROM smbms_bill WHERE billCode = ?";
            Object[] params = {billCode};
            resultSet = BaseDao.execute(connection,preparedStatement,resultSet,sql,params);
            while (resultSet.next()) {
                bill = new Bill();
                bill.setId(resultSet.getInt("id"));
                bill.setBillCode(resultSet.getString("billCode"));
                bill.setProductName(resultSet.getString("productName"));
                bill.setProductDesc(resultSet.getString("productDesc"));
                bill.setProductUnit(resultSet.getString("productUnit"));
                bill.setProductCount(resultSet.getBigDecimal("productCount"));
                bill.setTotalPrice(resultSet.getBigDecimal("totalPrice"));
                bill.setIsPayment(resultSet.getInt("isPayment"));
                bill.setCreatedBy(resultSet.getInt("createdBy"));
                bill.setCreationDate(resultSet.getDate("creationDate"));
                bill.setModifyBy(resultSet.getInt("modifyBy"));
                bill.setModifyDate(resultSet.getDate("modifyDate"));
                bill.setProviderId(resultSet.getInt("providerId"));
            }
            BaseDao.closeResource(null,preparedStatement,resultSet);
        }
        return bill;
    }

    /**
     * 根据Id查询订单
     * @param connection
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public Bill getBillById(Connection connection, Integer id) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Bill bill = null;
        if(connection != null) {
            String sql = "SELECT b.*,p.proName as providerName FROM smbms_bill b, smbms_provider p WHERE b.providerId = p.id AND b.id = ?";
            Object[] params = {id};
            resultSet = BaseDao.execute(connection,preparedStatement,resultSet,sql,params);
            while (resultSet.next()) {
                bill = new Bill();
                bill.setId(resultSet.getInt("id"));
                bill.setBillCode(resultSet.getString("billCode"));
                bill.setProductName(resultSet.getString("productName"));
                bill.setProductDesc(resultSet.getString("productDesc"));
                bill.setProductUnit(resultSet.getString("productUnit"));
                bill.setProductCount(resultSet.getBigDecimal("productCount"));
                bill.setTotalPrice(resultSet.getBigDecimal("totalPrice"));
                bill.setIsPayment(resultSet.getInt("isPayment"));
                bill.setCreatedBy(resultSet.getInt("createdBy"));
                bill.setCreationDate(resultSet.getDate("creationDate"));
                bill.setModifyBy(resultSet.getInt("modifyBy"));
                bill.setModifyDate(resultSet.getDate("modifyDate"));
                bill.setProviderId(resultSet.getInt("providerId"));
                bill.setProviderName(resultSet.getString("providerName"));
            }
            BaseDao.closeResource(null,preparedStatement,resultSet);
        }
        return bill;
    }

    /**
     * 修改订单信息
     * @param connection
     * @param bill
     * @return
     * @throws SQLException
     */
    @Override
    public int modifyBill(Connection connection, Bill bill) throws SQLException {
        PreparedStatement preparedStatement = null;
        int updateRows = 0;
        if(connection != null) {
            String sql = "UPDATE smbms_bill SET billCode = ?, productName = ?, productDesc = ?, productUnit = ?, " +
                    "productCount = ?, totalPrice = ?, isPayment = ?, providerId = ?, modifyBy = ?, modifyDate = ? " +
                    "WHERE id = ?";
            Object[] params = {bill.getBillCode(),bill.getProductName(),bill.getProductDesc(),bill.getProductUnit(),
                    bill.getProductCount(),bill.getTotalPrice(),bill.getIsPayment(),bill.getProviderId(),
                    bill.getModifyBy(), bill.getModifyDate(), bill.getId()};
            updateRows = BaseDao.executeUpdate(connection, preparedStatement,sql,params);
            BaseDao.closeResource(null, preparedStatement, null);
        }
        return updateRows;
    }

    /**
     * 删除订单
     * @param connection
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteBill(Connection connection, Integer id) throws SQLException {
        PreparedStatement preparedStatement = null;
        int updateRows = 0;
        if(connection != null) {
            String sql = "DELETE FROM smbms_bill WHERE id = ?";
            Object[] params = {id};
            updateRows = BaseDao.executeUpdate(connection, preparedStatement,sql,params);
            BaseDao.closeResource(null, preparedStatement, null);
        }
        return updateRows;
    }
}
