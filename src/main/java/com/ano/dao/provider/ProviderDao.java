package com.ano.dao.provider;

import com.ano.pojo.Bill;
import com.ano.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author wangjiao
 * @Date 2021/9/9 22:54
 * @Version 1.0
 */

public interface ProviderDao {

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
    List<Provider> getProviderList(Connection connection,
                                          String queryProCode,
                                          String queryProName,
                                          int currentPageNo,
                                          int pageSize) throws SQLException;

    /**
     * 获取供应商数量
     * @param connection
     * @param queryProCode
     * @param queryProName
     * @return
     * @throws SQLException
     */
    int getProviderCount(Connection connection, String queryProCode, String queryProName) throws SQLException;

    /**
     * 添加供应商
     * @param connection
     * @param provider
     * @return
     * @throws SQLException
     */
    int addProvider(Connection connection, Provider provider) throws SQLException;

    /**
     * 根据Id删除供应商
     * @param connection
     * @param proId
     * @return
     * @throws SQLException
     */
    int delProvider(Connection connection, int proId) throws SQLException;

    /**
     * 根据Id查询该供应商是否有未支付订单
     * @param connection
     * @param proId
     * @return
     * @throws SQLException
     */
    List<Bill> noPayBill(Connection connection, int proId) throws SQLException;

    /**
     * 根据Id查询供应商
     * @param connection
     * @param proId
     * @return
     * @throws SQLException
     */
    Provider getProviderById(Connection connection, int proId) throws SQLException;

    /**
     * 根据proCode查询供应商（用来判断proCode是否已经存在）
     * @param connection
     * @param proCode
     * @return
     * @throws SQLException
     */
    Provider getProviderByProCode(Connection connection, String proCode) throws SQLException;

    /**
     * 修改供应商信息
     * @param connection
     * @param provider
     * @return
     * @throws SQLException
     */
    int modifyProvider(Connection connection, Provider provider) throws SQLException;




}
