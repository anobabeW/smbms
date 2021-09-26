package com.ano.service.bill;


import com.ano.dao.BaseDao;
import com.ano.dao.bill.BillDao;
import com.ano.dao.bill.BillDaoImpl;
import com.ano.pojo.Bill;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @Author wangjiao
 * @Date 2021/9/12 15:29
 * @Version 1.0
 */


public class BillServiceImpl implements BillService {

    private static BillDao billDao;
    public BillServiceImpl (){
        billDao = new BillDaoImpl();
    }

    /**
     * 订单管理查询
     * @param productName
     * @param providerId
     * @param isPayment
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<Bill> getBillList(String productName, int providerId, int isPayment, int currentPageNo, int pageSize) {
        List<Bill> billList = null;
        Connection connection = null;

        try {
            connection = BaseDao.getConnection();
            billList = billDao.getBillList(connection,productName,providerId,isPayment,currentPageNo,pageSize);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return billList;
    }

    /**
     * 根据条件查询订单数量
     * @param productName
     * @param providerId
     * @param isPayment
     * @return
     */
    @Override
    public int getBillCount(String productName, int providerId, int isPayment) {
        int count = 0;
        Connection connection = null;

        connection = BaseDao.getConnection();
        try {
            count = billDao.getBillCount(connection,productName,providerId,isPayment);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return count;
    }

    /**
     * 添加订单操作
     * @param bill
     * @return
     */
    @Override
    public boolean addBill(Bill bill) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            if(billDao.addBill(connection, bill) > 0 ) {
                flag = true;
            }
            connection.commit();

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
     * 根据billCode查询订单，用户添加订单时判断所输入的billCode是否可用
     * @param billCode
     * @return
     */
    @Override
    public Bill existBillCode(String billCode) {
        Connection connection = null;
        Bill bill = null;

        try {
            connection = BaseDao.getConnection();
            bill = billDao.getBillByCode(connection,billCode);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return bill;
    }

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    @Override
    public Bill getBillById(Integer id) {
        Connection connection = null;
        Bill bill = null;

        try {
            connection = BaseDao.getConnection();
            bill = billDao.getBillById(connection,id);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return bill;
    }

    /**
     * 添加订单操作
     * @param bill
     * @return
     */
    @Override
    public boolean modifyBill(Bill bill) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            if(billDao.modifyBill(connection, bill) > 0 ) {
                flag = true;
            }
            connection.commit();

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
     * 删除订单
     * @param id
     * @return
     */
    @Override
    public boolean deleteBill(Integer id) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            if(billDao.deleteBill(connection, id) > 0 ) {
                flag = true;
            }
            connection.commit();

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

   /* @Test
    public void test() {
        BillServiceImpl billService = new BillServiceImpl();
        Bill bill = billService.getBillById(21);
        bill.setIsPayment(1);
        bill.setModifyBy(16);
        bill.setModifyDate(new Date());
        boolean b = billService.modifyBill(bill);
        System.out.println(b);
    }*/

}
