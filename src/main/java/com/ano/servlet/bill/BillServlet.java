package com.ano.servlet.bill;

import com.alibaba.fastjson.JSONArray;
import com.ano.pojo.Bill;
import com.ano.pojo.Provider;
import com.ano.pojo.User;
import com.ano.service.bill.BillService;
import com.ano.service.bill.BillServiceImpl;
import com.ano.service.provider.ProviderService;
import com.ano.service.provider.ProviderServiceImpl;
import com.ano.util.Constants;
import com.ano.util.PageSupport;
import com.mysql.jdbc.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author wangjiao
 * @Date 2021/9/12 14:56
 * @Version 1.0
 */


public class BillServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method != null && method.equals("query")) {
            this.getBills(req,resp);
        }else if(method != null && method.equals("add")) {
            this.addBill(req,resp);
        }else if (method != null && method.equals("getproviderlist")) {
            this.getProviderList(req,resp);
        }else if (method != null && method.equals("existBillCode")) {
            this.billCodeExist(req,resp);
        }else if (method != null && method.equals("view")) {
            this.getBillById(req,resp,"billview.jsp");
        }else if (method != null && method.equals("modify")) {
            this.getBillById(req,resp,"billmodify.jsp");
        }else if (method != null && method.equals("modifysave")) {
            this.modifyBill(req,resp);
        }else if (method != null && method.equals("delbill")) {
            this.deleteBill(req,resp);
        }
    }

    /**
     * 删除订单
     * @param req
     * @param resp
     */
    private void deleteBill(HttpServletRequest req, HttpServletResponse resp) {
        String billId = req.getParameter("billid");
        Map<String, String> map = new HashMap<>();
        BillService billService = new BillServiceImpl();
        Bill bill = billService.getBillById(Integer.parseInt(billId));
        if( bill == null) {
            map.put("delResult","notexist");
        }else if(bill.getIsPayment() == 1) {
            map.put("delResult","isNotPayment");
        }else {
            if(billService.deleteBill(Integer.parseInt(billId))) {
                map.put("delResult","true");
            }else{
                map.put("delResult","false");
            }
        }
        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(map));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 修改订单信息
     * @param req
     * @param resp
     */
    private void modifyBill(HttpServletRequest req, HttpServletResponse resp) {
        //从前端获取数据
        String billId = req.getParameter("id");
        String billCode = req.getParameter("billCode");
        String productName = req.getParameter("productName");
        String productDesc = req.getParameter("productDesc");
        String productUnit = req.getParameter("productUnit");
        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String providerId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");

        Bill bill = new Bill();
        bill.setId(Integer.parseInt(billId));
        bill.setBillCode(billCode);
        bill.setProductName(productName);
        bill.setProductDesc(productDesc);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setTotalPrice(new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setProviderId(Integer.parseInt(providerId));
        bill.setIsPayment(Integer.parseInt(isPayment));

        bill.setModifyBy(((User)(req.getSession().getAttribute(Constants.USER_SESSION))).getId());
        bill.setModifyDate(new Date());

        BillService billService = new BillServiceImpl();
        if(billService.modifyBill(bill)) {
            try {
                resp.sendRedirect(req.getContextPath() + "/jsp/bill.do?method=query");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                req.getRequestDispatcher("billmodify.jsp").forward(req,resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    /**
     * 查询单个订单信息
     * @param req
     * @param resp
     */
    private void getBillById(HttpServletRequest req, HttpServletResponse resp, String url) {
        String billId = req.getParameter("billid");
        if(!StringUtils.isNullOrEmpty(billId)) {
            BillServiceImpl billService = new BillServiceImpl();
            Bill bill = billService.getBillById(Integer.parseInt(billId));
            req.setAttribute("bill",bill);
            try {
                req.getRequestDispatcher(url).forward(req,resp);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 添加订单时，判断输入的订单编码是否已经存在
     * @param req
     * @param resp
     */
    private void billCodeExist(HttpServletRequest req, HttpServletResponse resp) {
        //从前端获取数据
        String billCode = req.getParameter("billCode");
        //判断订单编码是否存在，将结果集封装在万能的Map中
        Map<String, String> resultMap = new HashMap<>();
        if(StringUtils.isNullOrEmpty(billCode)) {
            resultMap.put("billCode","empty");
        }else {
            BillService billService = new BillServiceImpl();
            Bill bill = billService.existBillCode(billCode);
            if(bill != null) {
                resultMap.put("billCode","exist");
            }else {
                resultMap.put("billCode","notExist");
            }
        }
        //将数据转换成JSON格式返回前端
        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取供应商列表
     * @param req
     * @param resp
     */
    private void getProviderList(HttpServletRequest req, HttpServletResponse resp) {
        ProviderService providerService = new ProviderServiceImpl();
        List<Provider> providerList = providerService.getProviderList(null, null, 1, 0);
        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(providerList));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加订单
     * @param req
     * @param resp
     */
    private void addBill(HttpServletRequest req, HttpServletResponse resp) {
        //从前端获取数据
        String billCode = req.getParameter("billCode");
        String productName = req.getParameter("productName");
        String productDesc = req.getParameter("productDesc");
        String productUnit = req.getParameter("productUnit");
        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String providerId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");

        Bill bill = new Bill();
        bill.setBillCode(billCode);
        bill.setProductName(productName);
        bill.setProductDesc(productDesc);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setTotalPrice(new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setProviderId(Integer.parseInt(providerId));
        bill.setIsPayment(Integer.parseInt(isPayment));

        bill.setCreatedBy(((User)(req.getSession().getAttribute(Constants.USER_SESSION))).getId());
        bill.setCreationDate(new Date());

        BillService billService = new BillServiceImpl();
        if(billService.addBill(bill)){
            try {
                resp.sendRedirect(req.getContextPath() + "/jsp/bill.do?method=query");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                req.getRequestDispatcher("billadd.jsp").forward(req,resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 订单管理页面（根据条件查询订单，包括分页）
     * @param req
     * @param resp
     */
    private void getBills(HttpServletRequest req, HttpServletResponse resp) {
        //获取前端数据
        String queryProductName = req.getParameter("queryProductName");
        String queryProviderId = req.getParameter("queryProviderId");
        String queryIsPayment = req.getParameter("queryIsPayment");
        String pageIndex = req.getParameter("pageIndex");
        //设置查询条件的默认初始值
        int providerId = 0;
        int isPayment = 0;
        int currentPageNo = 1;
        int pageSize = 5;
        //判断前端参数并设置查询条件的值
        if(queryProductName == null) {
            queryProductName = "";
        }
        if(!StringUtils.isNullOrEmpty(queryProviderId)) {
            providerId = Integer.parseInt(queryProviderId);
        }
        if(!StringUtils.isNullOrEmpty(queryIsPayment)) {
            isPayment = Integer.parseInt(queryIsPayment);
        }
        if(!StringUtils.isNullOrEmpty(pageIndex)) {
            currentPageNo = Integer.parseInt(pageIndex);
        }

        //总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setPageSize(pageSize);
        pageSupport.setCurrentPageNo(currentPageNo);
        BillService billService = new BillServiceImpl();
        //根据查询条件获取订单记录数
        int billCount = billService.getBillCount(queryProductName, providerId, isPayment);
        pageSupport.setTotalCount(billCount);
        int totalPageCount = pageSupport.getTotalPageCount();
        //控制首页和尾页
        if(currentPageNo < 1) {
            currentPageNo = 1;
        } else if(currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        //根据查询条件获取订单列表
        List<Bill> billList = billService.getBillList(queryProductName, providerId, isPayment, currentPageNo, pageSize);
        req.setAttribute("billList",billList);
        //获取供应商列表
        ProviderService providerService = new ProviderServiceImpl();
        List<Provider> providerList = providerService.getProviderList(null, null, currentPageNo, 0);

        //返回前端
        req.setAttribute("providerList",providerList);
        req.setAttribute("totalCount",billCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("queryProductName", queryProductName);
        req.setAttribute("queryProviderId", queryProviderId);
        req.setAttribute("queryIsPayment", queryIsPayment);

        try {
            req.getRequestDispatcher("billlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
