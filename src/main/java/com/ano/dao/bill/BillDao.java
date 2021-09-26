package com.ano.dao.bill;

import com.ano.pojo.Bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author wangjiao
 * @Date 2021/9/12 14:54
 * @Version 1.0
 */


public interface BillDao {

    /**
     * 订单管理页面（根据条件查询）
     * @param connection
     * @param productName
     * @param providerId
     * @param isPayment
     * @param currentPageNo
     * @param pageSize
     * @return
     * @throws SQLException
     */
    List<Bill> getBillList(Connection connection,
                                  String productName,
                                  int providerId,
                                  int isPayment,
                                  int currentPageNo,
                                  int pageSize) throws SQLException;

    /**
     * 获取订单数量
     * @param connection
     * @param productName
     * @param providerId
     * @param isPayment
     * @return
     * @throws SQLException
     */
    int getBillCount(Connection connection,
                            String productName,
                            int providerId,
                            int isPayment) throws SQLException;

    /**
     * 添加订单
     * @param connection
     * @param bill
     * @return
     * @throws SQLException
     */
    int addBill(Connection connection, Bill bill) throws SQLException;

    /**
     * 根据billCode查询订单
     * @param connection
     * @param billCode
     * @return
     * @throws SQLException
     */
    Bill getBillByCode(Connection connection, String billCode) throws SQLException;

    /**
     * 根据Id查询订单
     * @param connection
     * @param id
     * @return
     * @throws SQLException
     */
    Bill getBillById(Connection connection, Integer id) throws SQLException;

    /**
     * 修改订单信息
     * @param connection
     * @param bill
     * @return
     * @throws SQLException
     */
    int modifyBill(Connection connection, Bill bill) throws SQLException;

    /**
     * 删除订单信息
     * @param connection
     * @param id
     * @return
     * @throws SQLException
     */
    int deleteBill(Connection connection, Integer id) throws SQLException;

}
