package com.ano.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.ano.pojo.Role;
import com.ano.pojo.User;
import com.ano.service.role.RoleService;
import com.ano.service.role.RoleServiceImpl;
import com.ano.service.user.UserService;
import com.ano.service.user.UserServiceImpl;
import com.ano.util.Constants;
import com.ano.util.PageSupport;
import com.mysql.jdbc.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现Servlet复用
 * @author wangjiao
 */
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method != null && method.equals("savepwd")) {
            this.updatePwd(req, resp);
        } else if (method != null && method.equals("pwdmodify")) {
            this.pwdModify(req, resp);
        } else if (method != null && method.equals("query")) {
            this.query(req, resp);
        } else if (method != null && method.equals("add")) {
            this.add(req, resp);
        } else if (method != null && method.equals("ucexist")) {
            this.userCodeExist(req,resp);
        } else if (method != null && method.equals("getrolelist")) {
            this.getRoleList(req,resp);
        } else if (method != null && method.equals("deluser")) {
            this.delete(req, resp);
        }else if (method != null && method.equals("view")) {
            this.getUserById(req, resp, "/jsp/userview.jsp");
        }else if (method != null && method.equals("modify")) {
            this.getUserById(req,resp,"usermodify.jsp");
        }else if (method != null && method.equals("modifyexe")) {
            this.modify(req,resp);
        }

    }

    /**
     * 添加用户时判断输入的userCode是否已经存在
     * @param req
     * @param resp
     */
    private void userCodeExist(HttpServletRequest req, HttpServletResponse resp) {
        String userCode = req.getParameter("userCode");
        Map<String, String> map = new HashMap<>();
        if(StringUtils.isNullOrEmpty(userCode)) {
            map.put("userCode","empty");
        }else {
            UserService userService = new UserServiceImpl();
            User user = userService.getUSerByCode(userCode);
            if(user != null) {
                map.put("userCode","exist");
            }else {
                map.put("userCode", "notExist");
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
     * 获取用户角色列表
     * @param req
     * @param resp
     */
    private void getRoleList(HttpServletRequest req, HttpServletResponse resp) {
        RoleService roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        resp.setContentType("application/json");
        try {
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(roleList));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据用户Id查询用户
     * @param req
     * @param resp
     * @param url
     */
    private void getUserById(HttpServletRequest req, HttpServletResponse resp, String url) {
        String userId = req.getParameter("uid");
        if(!StringUtils.isNullOrEmpty(userId)) {
            UserService userService = new UserServiceImpl();
            User user = userService.getUserById(Integer.parseInt(userId));
            req.setAttribute("user", user);
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
     * 修改用户
     * @param req
     * @param resp
     */
    private void modify(HttpServletRequest req, HttpServletResponse resp) {
        String userId = req.getParameter("uid");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        user.setId(Integer.valueOf(userId));
        user.setUserName(userName);
        user.setGender(Integer.valueOf(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());

        UserService userService = new UserServiceImpl();

        if(userService.modifyUser(user)) {
            try {
                resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                req.getRequestDispatcher("usermodify.jsp").forward(req,resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除用户
     * @param req
     * @param resp
     */
    private void delete(HttpServletRequest req, HttpServletResponse resp) {
        String userId = req.getParameter("uid");
        Integer id = Integer.parseInt(userId);
        Map<String, String> resultMap = new HashMap<>();
        if(id <= 0) {
            resultMap.put("delResult","notexist");
        } else if (id.equals(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId())) {
            resultMap.put("delResult","cannotdel");
        } else {
            UserService userService = new UserServiceImpl();
            if(userService.deleteUser(id)) {
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
     * 添加用户
     * @param req
     * @param resp
     */
    private void add(HttpServletRequest req, HttpServletResponse resp) {
        //从前端获取数据
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        Integer gender = Integer.valueOf(req.getParameter("gender"));
        String birth = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        Integer userRole = Integer.valueOf(req.getParameter("userRole"));
        Date birthday = null;
        try {
            birthday = new SimpleDateFormat("yyyy-MM-dd").parse(birth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //将前端参数封装到User对象中传给后端
        User user = new User(userCode,userName,userPassword,gender,birthday,phone,address,userRole);
        user.setCreateBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setCreationDate(new Date());

        //调用Service层方法执行添加用户操作
        UserServiceImpl userService = new UserServiceImpl();
        if(userService.addUser(user)) {
            try {
                resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                req.getRequestDispatcher("useradd.jsp").forward(req,resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 验证旧密码
     * @param req
     * @param resp
     */
    private void pwdModify(HttpServletRequest req, HttpServletResponse resp) {
       //Session中有用户的密码
        Object attribute = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");
        //结果集
        HashMap<String, String> resultMap = new HashMap<>();

        if(attribute == null) {
            //Session过期或失效
            resultMap.put("result","sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)) {
            //旧密码输入为空
            resultMap.put("result", "error");
        }else {
            //Session中用户的密码
            String userPassword = ((User) attribute).getUserPassword();
            if (!oldpassword.equals(userPassword)) {
                //旧密码输入错误
                resultMap.put("result", "false");
            }else {
                resultMap.put("result", "true");
            }
        }
        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            //JSONArray Alibaba的JSON工具类，转换格式
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改密码
     * @param req
     * @param resp
     */
    private void updatePwd(HttpServletRequest req, HttpServletResponse resp){
        //从Session里面拿当前用户
        Object attribute = req.getSession().getAttribute(Constants.USER_SESSION);
        String newPassword = req.getParameter("newpassword");

        System.out.println("UserServlet" + newPassword);

        boolean flag = false;
        if(attribute != null && !StringUtils.isNullOrEmpty(newPassword)){
            //调用Service层修改密码
            UserService userService = new UserServiceImpl();
            flag = userService.updatePwd(((User)attribute).getId(), newPassword);
            if(flag) {
                req.setAttribute("message","密码修改成功，请退出，使用新密码登录");
                //密码修改成功，移除当前Session,重新登录
                req.getSession().removeAttribute(Constants.USER_SESSION);
            }else {
                req.setAttribute("message","密码修改失败");
            }
        }else {
            req.setAttribute("message","新密码设置有问题");
        }
        try {
            req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户管理
     * @param req
     * @param resp
     */
    private void query(HttpServletRequest req, HttpServletResponse resp) {
        //从前端获取数据
        String queryUserName = req.getParameter("queryname");
        String tempUserRole = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");

        //获取用户列表
        UserServiceImpl userService = new UserServiceImpl();
        List<User> userList = null;
        //第一次走这个请求，一定是首页，并且页面大小固定
        int queryUserRole = 0;
        int pageSize = 5;
        int currentPageNo = 1;

        if(queryUserName == null) {
            queryUserName = "";
        }
        if( tempUserRole!=null && !tempUserRole.equals("")) {
            queryUserRole = Integer.parseInt(tempUserRole);
        }
        if(pageIndex != null) {
            currentPageNo = Integer.parseInt(pageIndex);
        }

        //获取用户的总数（分页：上一页、下一页的情况）
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        //总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);
        int totalPageCount = pageSupport.getTotalPageCount();
        //控制首页和尾页:如果是首页，不能再往前了，如果是尾页，不能再往后了
        if(currentPageNo < 1) {
            currentPageNo = 1;
        }else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }

        //获取用户列表
        userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList",userList);

        //获取角色列表
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList",roleList);

        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPageCount);

        req.setAttribute("queryUserName",queryUserName);
        req.setAttribute("queryUserRole",queryUserRole);

        //返回前端
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req,resp);
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
