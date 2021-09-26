package com.ano.service.provider;

import com.ano.dao.BaseDao;
import com.ano.dao.provider.ProviderDao;
import com.ano.dao.provider.ProviderDaoImpl;
import com.ano.pojo.Bill;
import com.ano.pojo.Provider;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangjiao
 * @Date 2021/9/10 8:49
 * @Version 1.0
 */

public class ProviderServiceImpl implements ProviderService{

    private static ProviderDao providerDao;
    public ProviderServiceImpl() {
        providerDao = new ProviderDaoImpl();
    }

    /**
     * 根据查询条件获取供应商列表
     * @param proCode
     * @param proName
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<Provider> getProviderList(String proCode, String proName, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<Provider> providerList = new ArrayList<>();

        try {
            connection = BaseDao.getConnection();
            providerList = providerDao.getProviderList(connection,proCode,proName,currentPageNo,pageSize);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }

        return providerList;
    }

    /**
     * 获取供应商总数量
     * @param proCode
     * @param proName
     * @return
     */
    @Override
    public int getProviderCount(String proCode, String proName) {
        Connection connection = null;
        int count = 0;

        try {
            connection = BaseDao.getConnection();
            count = providerDao.getProviderCount(connection,proCode,proName);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return count;
    }

    /**
     * 添加供应商
     * @param provider
     * @return
     */
    @Override
    public boolean addProvider(Provider provider) {
        boolean flag = false;
        Connection connection = null;
        int updateRows = 0;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            updateRows = providerDao.addProvider(connection,provider);
            connection.commit();
            if(updateRows > 0) {
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

    @Override
    public boolean delProvider(int proId) {
        boolean flag = false;
        int updateRows = 0;
        Connection connection = null;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            updateRows = providerDao.delProvider(connection, proId);
            connection.commit();
            if(updateRows > 0) {
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
            BaseDao.closeResource(connection, null, null);
        }

        return flag;
    }

    /**
     * 查询该供应商是否存在未支付订单
     * @param proId
     * @return
     */
    @Override
    public int existNoPayBill(int proId) {
        int count = 0;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            List<Bill> billList = providerDao.noPayBill(connection, proId);
            if(billList.size() > 0) {
                count = billList.size();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * 根据Id获取供应商信息
     * @param proId
     * @return
     */
    @Override
    public Provider getProviderById(int proId) {
        Connection connection = null;
        Provider provider = null;

        try {
            connection = BaseDao.getConnection();
            provider = providerDao.getProviderById(connection,proId);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return provider;
    }

    /**
     * 根据proCode查询供应商是否存在
     * @param proCode
     * @return
     */
    @Override
    public Provider getProviderByProCode(String proCode) {
        Connection connection = null;
        Provider provider = null;

        try {
            connection = BaseDao.getConnection();
            provider = providerDao.getProviderByProCode(connection,proCode);
        } catch (SQLException e) {
            e.printStackTrace();
            provider = null;
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return provider;

    }

    @Override
    public boolean modifyProvider(Provider provider) {
        boolean flag = false;
        int updateRows = 0;
        Connection connection = null;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            updateRows = providerDao.modifyProvider(connection,provider);
            connection.commit();
            if(updateRows > 0) {
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
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }

    /*@Test
    public void test() {
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        List<Provider> providerList = providerService.getProviderList(null, null, 0, 0);
        //List<Provider> providerList = providerService.getProviderList("", "北京", 1, 5);
        //int providerCount = providerService.getProviderCount("", "北京");
        //System.out.println(providerCount);
        for (Provider provider : providerList) {
            System.out.println(provider.getProCode());
        }

    }*/
}
