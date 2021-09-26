package com.ano.service.bill;

import com.ano.pojo.Bill;

import java.util.List;

/**
 * @Author wangjiao
 * @Date 2021/9/12 15:29
 * @Version 1.0
 */


public interface BillService {

    /**
     * 订单管理查询
     * @param productName
     * @param providerId
     * @param isPayment
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    List<Bill> getBillList(String productName, int providerId, int isPayment, int currentPageNo, int pageSize);

    /**
     * 根据条件获取订单数量
     * @param productName
     * @param providerId
     * @param isPayment
     * @return
     */
    int getBillCount(String productName, int providerId, int isPayment);

    /**
     * 添加订单
     * @param bill
     * @return
     */
    boolean addBill(Bill bill);

    /**
     * 该订单编码查询订单
     * @param billCode
     * @return
     */
    Bill existBillCode(String billCode);

    /**
     * 根据Id查询订单
     * @param id
     * @return
     */
    Bill getBillById(Integer id);

    /**
     * 修改订单
     * @param bill
     * @return
     */
    boolean modifyBill(Bill bill);

    /**
     * 删除订单
     * @param id
     * @return
     */
    boolean deleteBill(Integer id);
}
