package com.ano.servlet.provider;

import com.alibaba.fastjson.JSONArray;
import com.ano.pojo.Provider;
import com.ano.pojo.User;
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
import java.util.*;

/**
 * @Author wangjiao
 * @Date 2021/9/10 8:56
 * @Version 1.0
 */

public class ProviderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method != null && method.equals("query")) {
            this.getProviders(req,resp);
        }else if(method != null && method.equals("add")) {
            this.addProvider(req,resp);
        }else if (method != null && method.equals("pcexist")) {
            this.proCodeExist(req,resp);
        }else if (method != null && method.equals("delprovider")) {
            this.delProvider(req,resp);
        }else if(method != null && method.equals("view")) {
            this.getProviderById(req, resp, "providerview.jsp");
        }else if (method != null && method.equals("modify")) {
            this.getProviderById(req, resp, "providermodify.jsp");
        }else if(method != null && method.equals("modifyexe")) {
            this.modifyProvider(req, resp);
        }
    }

    /**
     * 修改供应商信息
     * @param req
     * @param resp
     */
    private void modifyProvider(HttpServletRequest req, HttpServletResponse resp) {
        //从前端获取数据
        String providerid = req.getParameter("providerid");
        String proCode = req.getParameter("proCode");
        String proName = req.getParameter("proName");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");

        Provider provider = new Provider();
        provider.setId(Integer.parseInt(providerid));
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProAddress(proAddress);
        provider.setProFax(proFax);
        provider.setProDesc(proDesc);

        provider.setModifyBy(((User)(req.getSession().getAttribute(Constants.USER_SESSION))).getId());
        provider.setModifyDate(new Date());

        ProviderService providerService = new ProviderServiceImpl();
        if(providerService.modifyProvider(provider)){
            try {
                resp.sendRedirect(req.getContextPath() + "/jsp/provider.do?method=query");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                req.getRequestDispatcher("providermodify.jsp").forward(req,resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 添加供应商时判断proCode是否已经存在
     * @param req
     * @param resp
     */
    private void proCodeExist(HttpServletRequest req, HttpServletResponse resp) {
        String proCode = req.getParameter("proCode");
        Map<String,String> result = new HashMap<>();
        if(StringUtils.isNullOrEmpty(proCode)) {
            result.put("proCode","empty");
        }else {
            ProviderService providerService = new ProviderServiceImpl();
            if(providerService.getProviderByProCode(proCode) != null) {
                result.put("proCode","exist");
            }else {
                result.put("proCode","notexist");
            }
        }

        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(result));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据Id获取供应商信息
     * @param req
     * @param resp
     */
    private void getProviderById(HttpServletRequest req, HttpServletResponse resp, String url) {
        String proid = req.getParameter("proid");

        if(!StringUtils.isNullOrEmpty(proid)) {
            ProviderService providerService = new ProviderServiceImpl();
            Provider provider = providerService.getProviderById(Integer.parseInt(proid));
            req.setAttribute("provider",provider);
            try {
                req.getRequestDispatcher(url).forward(req,resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除供应商
     * @param req
     * @param resp
     */
    private void delProvider(HttpServletRequest req, HttpServletResponse resp) {
        String proId = req.getParameter("proid");
        int delProId = Integer.parseInt(proId);
        ProviderService providerService = new ProviderServiceImpl();
        Provider providerById = providerService.getProviderById(delProId);
        int noPayBillCount = providerService.existNoPayBill(delProId);
        Map<String, String> resultMap = new HashMap<>();
        //判断该Id的供应商是否存在
        if(providerById == null){
            resultMap.put("delResult","notexist");
            //如果该供应商存在，判断是否存在未支付订单
        }else if(noPayBillCount > 0) {
            resultMap.put("delResult", String.valueOf(noPayBillCount));
            //判断删除操作是否成功
        }else {
            if(providerService.delProvider(delProId)) {
                resultMap.put("delResult","true");
            }else {
                resultMap.put("delResult","false");
            }
        }

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
     * 添加供应商
     * @param req
     * @param resp
     */
    private void addProvider(HttpServletRequest req, HttpServletResponse resp) {
        //1. 获取前端数据
        String proCode = req.getParameter("proCode");
        String proName = req.getParameter("proName");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");

        System.out.println(proCode);
        //2. 将数据封装在Provider对象中传给后端
        Provider provider = new Provider();
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProAddress(proAddress);
        provider.setProFax(proFax);
        provider.setProDesc(proDesc);
        provider.setCreatedBy(((User)(req.getSession().getAttribute(Constants.USER_SESSION))).getId());
        provider.setCreationDate(new Date());
        ProviderService providerService = new ProviderServiceImpl();
        if(providerService.addProvider(provider)){
            try {
                resp.sendRedirect(req.getContextPath()+"/jsp/provider.do?method=query");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                req.getRequestDispatcher("provideradd.jsp").forward(req,resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 供应商管理页面(查询供应商列表，包含分页)
     * @param req
     * @param resp
     */
    private void getProviders(HttpServletRequest req, HttpServletResponse resp) {
        String queryProCode = req.getParameter("queryProCode");
        String queryProName = req.getParameter("queryProName");
        String pageIndex = req.getParameter("pageIndex");

        ProviderService providerService = new ProviderServiceImpl();
        List<Provider> providerList = null;

        int currentPageNo = 1;
        int pageSize = 5;

        if(pageIndex != null) {
            currentPageNo = Integer.parseInt(pageIndex);
        }

        int providerCount = providerService.getProviderCount(queryProCode, queryProName);
        PageSupport pageSupport = new PageSupport();
        pageSupport.setPageSize(pageSize);
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setTotalCount(providerCount);
        int totalPageCount = pageSupport.getTotalPageCount();

        if(currentPageNo < 1) {
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }

        providerList = providerService.getProviderList(queryProCode,queryProName,currentPageNo,pageSize);
        req.setAttribute("providerList",providerList);

        req.setAttribute("totalCount",providerCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPageCount);

        req.setAttribute("queryProCode",queryProCode);
        req.setAttribute("queryProName",queryProName);

        try {
            req.getRequestDispatcher("providerlist.jsp").forward(req,resp);
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
