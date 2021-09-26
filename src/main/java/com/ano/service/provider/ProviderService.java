package com.ano.service.provider;

import com.ano.pojo.Provider;

import java.util.List;

/**
 * @Author wangjiao
 * @Date 2021/9/10 8:48
 * @Version 1.0
 */


public interface ProviderService {

    /**
     * 获取供应商列表
     * @param proCode
     * @param proName
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    List<Provider> getProviderList(String proCode, String proName, int currentPageNo, int pageSize);

    /**
     * 获取供应商总数量
     * @param proCode
     * @param proName
     * @return
     */
    int getProviderCount(String proCode, String proName);

    /**
     * 添加供应商
     * @param provider
     * @return
     */
    boolean addProvider(Provider provider);

    /**
     * 删除供应商
     * @param proId
     * @return
     */
    boolean delProvider(int proId);

    /**
     * 查询该供应商未支付订单数量
     * @param proId
     * @return
     */
    int existNoPayBill(int proId);

    /**
     * 根据Id获取供应商
     * @param proId
     * @return
     */
    Provider getProviderById(int proId);

    /**
     * 根据proCode查询供应商
     * @param proCode
     * @return
     */
    Provider getProviderByProCode(String proCode);

    /**
     * 修改供应商信息
     * @param provider
     * @return
     */
    boolean modifyProvider(Provider provider);
}
