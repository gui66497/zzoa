package zzjz.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.Bean;
import zzjz.bean.Column;
import zzjz.bean.DepartRole;
import zzjz.bean.DepartRoleRequest;
import zzjz.bean.ResultCode;
import zzjz.bean.Staff;
import zzjz.bean.StaffExcel;
import zzjz.bean.StaffRequest;
import zzjz.service.ActivitiIdentifyService;
import zzjz.service.AnnualService;
import zzjz.service.DepartService;
import zzjz.service.StaffService;
import zzjz.service.TurnoverService;
import zzjz.service.ZkemService;
import zzjz.task.TaskTest;
import zzjz.util.DateUtil;
import zzjz.util.LdapUtil;
import zzjz.util.MD5Util;
import zzjz.util.PageUtil;
import zzjz.util.PinyinUtil;
import zzjz.util.RoleUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author 房桂堂
 * @version 2017/3/2 15:34
 * @ClassName: StaffRest
 * @Description: 员工rest
 */
@Component
@Path("staff")
public class StaffRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(StaffRest.class);

    private String url = "";

    private String message = "";

    private static final Integer NUM2048 = 2048;

    private static final Integer NUM3 = 3;

    private String exportPath = "D:/downloadFiles/";

    @Context
    HttpServletRequest servletRequest;

    @Autowired
    StaffService staffService;

    @Autowired
    IdentityService identityService;

    @Autowired
    ActivitiIdentifyService activitiIdentifyService;

    @Autowired
    DepartService departService;

    @Autowired
    TurnoverService turnoverService;

    @Autowired
    AnnualService annualService;

    @Autowired
    TaskTest taskTest;

    @Autowired
    ZkemService zkemService;

    @Autowired
    LdapUtil ldapUtil;

    String[] pattern = new String[]{"yyyy-MM", "yyyyMM", "yyyy/MM", "yyyyMMdd", "yyyy-MM-dd", "yyyy/MM/dd",
        "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss"};

    /**
     * 君智邮箱后缀
     */
    String mailSuffix = "@zzjunzhi.com";

    /**
     * 分页获取员工信息.
     *
     * @param request 请求信息
     * @return 结果
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Staff> getStaffPage(StaffRequest request) {
        LOGGER.info("开始调用/staff/page");
        BaseResponse<Staff> response = new BaseResponse<>();
        if (request != null && ("null").equals(request.getDepartId())) {
            request.setDepartId(null);
        }
        Page<Staff> staffList = staffService.getStaffListPage(request);
        List<Object> otherData = null;
        try {
            otherData = PageUtil.dealPaging(request.getPaging(), (int) staffList.getTotal());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        response.setData(staffList);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/staff/page");
        return response;
    }

    /**
     * 获取所有员工信息（只包含简单信息）.
     *
     * @param type type
     * @return 结果
     */
    @Path("{type}/allSimple")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Staff> getAllSimpleStaff(@PathParam("type") String type) {
        LOGGER.info("开始调用/staff/allSimple");
        BaseResponse<Staff> response = new BaseResponse<>();
        List<Staff> staffList;
        if ("onJob".equals(type)) {
            staffList = staffService.getAllSimpleStaffOnJob();
        } else {
            staffList = staffService.getAllSimpleStaff();
        }
        response.setData(staffList);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/staff/allSimple");
        return response;
    }

    /**
     * 密码验证.
     *
     * @param headers 头信息
     * @param pass    密码
     * @return 结果
     */
    @PUT
    @Path("{pass}/checkUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> checkUser(@Context HttpHeaders headers, @PathParam("pass") String pass) {
        LOGGER.info("开始调用/staff/" + pass + "/checkUser");
        BaseResponse<String> response = new BaseResponse<>();
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String uId = headerParams.getFirst("userId");
        Staff staff = staffService.getStaffByUserId(uId);
        pass = MD5Util.md5(pass);
        if (staff.getPassword().equals(pass)) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/staff/checkUser");
        return response;
    }

    /**
     * 密码修改.
     *
     * @param staff   用户信息
     * @param headers 头信息
     * @return 结果
     * @throws ParseException ParseException
     */
    @POST
    @Path("editPass")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> editPass(Staff staff, @Context HttpHeaders headers) throws ParseException {
        LOGGER.info("开始调用/staff/editPass");
        BaseResponse<String> response = new BaseResponse<>();
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String uId = headerParams.getFirst("userId");
        staff.setStaffId(Long.parseLong(uId));
        staff.setPassword(MD5Util.md5(staff.getPassword()));
        boolean res = staffService.editStaff(staff);
        if (res) {
            message = "员工信息添加成功";
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            message = "员工信息添加失败";
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/staff/editPass");
        return response;
    }

    /**
     * 查询不显示的列.
     *
     * @return 结果
     */
    @Path("/unShow")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Column> getUnShowColumn() {
        LOGGER.info("开始调用/staff/unShow");
        BaseResponse<Column> response = new BaseResponse<>();
        List<Column> allColmn = staffService.getAllColumn();
        List<Column> unShowColumn = Lists.newArrayList();
        for (Column column : allColmn) {
            if (column.getIsShow() == 0) {
                unShowColumn.add(column);
            }
        }
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setData(unShowColumn);
        LOGGER.info("调用完成/staff/unShow");
        return response;
    }

    /**
     * 获取所有列.
     *
     * @return 列信息
     */
    @Path("/allCol")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Column> getAllColumn() {
        LOGGER.info("开始调用/staff/allCol");
        BaseResponse<Column> response = new BaseResponse<>();
        List<Column> allColumn = staffService.getAllColumn();
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setData(allColumn);
        LOGGER.info("调用完成/staff/allCol");
        return response;
    }

    /**
     * 修改列.
     *
     * @param column 列
     * @return 结果
     */
    @Path("/updateCol")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateCol(Column column) {
        LOGGER.info("开始调用/staff/updateCol");
        BaseResponse<String> response = new BaseResponse<>();
        boolean res = staffService.updateCol(column);
        if (res) {
            response.setMessage("修改列成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("修改列失败");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/staff/updateCol");
        return response;
    }

    /**
     * 新增员工信息.
     *
     * @param staff   员工信息
     * @param headers headers
     * @return 结果
     * @throws ParseException ParseException
     * @author 房桂堂
     */
    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addStaff(Staff staff, @Context HttpHeaders headers) throws ParseException {
        LOGGER.info("开始调用/staff/add");
        BaseResponse<String> response = new BaseResponse<>();
        String name = staff.getName();
        String email = staff.getEmail();
        String staffId = String.valueOf(staff.getStaffId());
        if (StringUtils.isBlank(name) || staff.getStaffId() < 0) {
            message = "姓名不能为空且员工ID大于0!";
            response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
            response.setMessage(message);
            LOGGER.debug(message);
            return response;
        }
        if (StringUtils.isBlank(email)) {
            message = "邮箱不能为空";
            response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
            response.setMessage(message);
            LOGGER.debug(message);
            return response;
        }
        String userName = PinyinUtil.getInstance().getStringPinyin(name);
        // 20191108 房桂堂 不再校验用户名 因为确实存在很多同名情况
        // 需要插入的数据唯一性校验
        // Staff staffTemp = staffService.getStaffByUserName(userName, true);
        /*int count = 0;
        StringBuilder tmpUserName = new StringBuilder(userName);
        while (null != staffTemp) {
            count++;
            tmpUserName.append(count);
            staffTemp = staffService.getStaffByUserName(userName);
        }
        userName = userName + tmpUserName.toString();*/
        staff.setUserName(userName);
        // 如果没有设置邮箱 则根据用户拼音自动生成
        if (StringUtils.isBlank(staff.getEmail())) {
            staff.setEmail(PinyinUtil.getInstance().getZZEmailByName(name));
        }
        // 在职的人 邮箱不能重复
        Staff emailStaff = staffService.getStaffByMail(staff.getEmail());
        if (null != emailStaff) {
            message = "在职员工中存在相同的邮箱!";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            LOGGER.debug(message);
            return response;
        }
        Staff staffIdTemp = staffService.getStaffByUserId(staffId);
        if (null != staffIdTemp) {
            message = "考勤号已存在!";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            LOGGER.debug(message);
            return response;
        }
        //根据邮箱判断ldap中是否已经存在此人
        String ldapId = email.substring(0, email.lastIndexOf("@"));
        if (!ldapUtil.isInLdap(ldapId)) {
            message = "根据所输入邮箱，在Ldap中未查找到ID为[" + ldapId + "]的员工，请联系管理员处理";
            response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
            response.setMessage(message);
            LOGGER.debug(message);
            return response;
        }
        //根据身份证好补全信息
        staff = staffService.completeStaffByIdNum(staff);
        //计算司龄 工龄
        staff = taskTest.countSeniority(staff);
        //密码加密
        staff.setPassword(MD5Util.md5(staff.getPassword()));
        boolean res = staffService.addStaff(staff);
        if (res) {
            //初始化年假信息
            annualService.queryAndInitAnnual(staff.getStaffId());
            activitiIdentifyService.addUser(staff);
            //添加异动信息 2017-12-15 fangguitang OA-210 只记录入职信息
            try {
                turnoverService.addOne(staff);
            } catch (Exception e) {
                e.printStackTrace();
            }
            message = "员工信息添加成功";
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            message = "员工信息添加失败";
            response.setResultCode(ResultCode.RESULT_ERROR);
            return response;
        }
        response.setMessage(message);
        LOGGER.info("调用完成/staff/add");
        return response;
    }

    /**
     * 修改员工信息.
     *
     * @param staff   员工信息
     * @param headers headers
     * @return 结果
     * @throws ParseException ParseException
     */
    @POST
    @Path("edit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> editStaff(Staff staff, @Context HttpHeaders headers) throws ParseException {
        LOGGER.info("开始调用/staff/edit");
        BaseResponse<String> response = new BaseResponse<>();
        long staffId = staff.getStaffId();
        String userName = PinyinUtil.getInstance().getStringPinyin(staff.getName());
        // 20191108 房桂堂 不再校验用户名 因为确实存在很多同名情况
        // 需要插入的数据唯一性校验
        /*Staff staffTemp = staffService.getStaffByUserName(userName, true);
        int count = 0;
        StringBuffer tmpUserName = new StringBuffer("");
        while (null != staffTemp && staffTemp.getStaffId() != staffId) {
            count++;
            tmpUserName.append(count);
            staffTemp = staffService.getStaffByUserName(userName, true);
        }
        userName = userName + tmpUserName.toString();*/
        staff.setUserName(userName);
        if (staffId < 1 || StringUtils.isBlank(userName)) {
            message = "参数错误";
            response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
            response.setMessage(message);
            return response;
        }
        //Staff oldStaff = staffService.getStaffByUserId(String.valueOf(staffId));
        boolean res = staffService.editStaff(staff);
        if (res) {
            //初始化年假信息
            if (staffService.getStaffByUserId(String.valueOf(staffId)) != null) {
                annualService.queryAndInitAnnual(staff.getStaffId());
            }
            //将异动信息记录 2017-12-15 fangguitang OA-210 不需要主动记录异动事项了
            //turnoverService.record(oldStaff, staff);
            message = "员工信息修改成功";
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            message = "员工信息修改失败";
            response.setResultCode(ResultCode.RESULT_ERROR);
            return response;
        }
        response.setMessage(message);
        LOGGER.info("调用完成/staff/edit");
        return response;
    }

    /**
     * 删除员工信息.
     *
     * @param staffId 员工信息id
     * @return 结果
     * @author 房桂堂
     */
    @DELETE
    @Path("{staffId}/del")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteUser(@PathParam("staffId") long staffId) {
        LOGGER.info("开始调用/staff/" + staffId + "/del");
        BaseResponse<String> response = new BaseResponse<>();
        if (staffId < 0) {
            message = "删除参数不合法";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            LOGGER.debug(message);
            return response;
        }
        Staff staff = staffService.getStaffByUserIdAll(String.valueOf(staffId));
        List<Group> groups = identityService.createGroupQuery().groupMember(staff.getUserName()).list();
        if (groups.size() > 0) {
            response.setMessage("用户[" + staff.getUserName() + "]具有分组信息，无法删除，请核实！");
            response.setResultCode(ResultCode.RESULT_ERROR);
            return response;
        }
        boolean res = staffService.delByStaffId(staffId);
        if (res) {
            response.setMessage("删除员工信息成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("删除员工信息失败");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/staff/del");
        return response;
    }

    /**
     * 批量删除员工信息.
     *
     * @param staffIds 员工信息id
     * @return 结果
     * @author 房桂堂
     */
    @DELETE
    @Path("{staffIds}/batchDel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> batchDeleteUser(@PathParam("staffIds") String staffIds) {
        LOGGER.info("开始调用/staff/" + staffIds + "/batchDel");
        BaseResponse<String> response = new BaseResponse<>();
        String[] arr = staffIds.split(",");
        for (int i = 0; i < arr.length; i++) {
            Staff staff = staffService.getStaffByUserId(String.valueOf(arr[i]));
            if (staff != null) {
                List<Group> groups = identityService.createGroupQuery().groupMember(staff.getUserName()).list();
                if (groups.size() > 0) {
                    response.setMessage("用户[" + staff.getUserName() + "]具有分组信息，无法删除，请核实！");
                    response.setResultCode(ResultCode.RESULT_ERROR);
                    return response;
                }
            }
        }
        boolean res = staffService.delByStaffIds(arr);
        if (res) {
            response.setMessage("删除员工信息成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("删除员工信息失败");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/staff/batchDel");
        return response;
    }

    /**
     * 登陆.
     *
     * @param staff 员工信息
     * @return 登陆结果
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> login(Staff staff) {
        LOGGER.info("开始调用/staff/login");
        LOGGER.debug("用户尝试登陆，用户名: {},密码: {}", staff.getUserName(), staff.getPassword());
        String userName = staff.getUserName();
        String password = staff.getPassword();
        password = MD5Util.md5(password);
        BaseResponse<String> response = new BaseResponse<>();
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
            response.setMessage("用户名或密码为空！");
            return response;
        }
        boolean loginResult = staffService.login(userName, password);
        LOGGER.debug("用户登陆结果:" + (loginResult ? "成功！" : "失败！"));

        if (loginResult) {
            Staff loginStaff = staffService.getStaffByUserName(userName, true);
            //设置session
            servletRequest.getSession().setAttribute("userName", loginStaff.getUserName());
            servletRequest.getSession().setAttribute("staffId", loginStaff.getStaffId());
            servletRequest.getSession().setAttribute("name", loginStaff.getName());
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("登陆成功！");
            List<Object> otherData = new ArrayList<>();
            otherData.add(loginStaff.getStaffId());
            otherData.add(loginStaff.getName());
            response.setOtherData(otherData);
            return response;
        }
        response.setMessage("登陆失败！");
        LOGGER.info("调用完成/staff/login");
        return response;
    }

    /**
     * 判断是否登录.
     *
     * @return 是否登录
     */
    @GET
    @Path("/hasLogin")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> haiLogin() {
        LOGGER.info("开始调用/staff/hasLogin");
        LOGGER.debug("判断用户是否登陆");
        BaseResponse<String> response = new BaseResponse<>();
        String userName = String.valueOf(servletRequest.getSession().getAttribute("userName"));
        //单点登录成功后的标志 有其一既可
        String loginUserName = String.valueOf(servletRequest.getSession().getAttribute("loginUserName"));
        if (StringUtils.isNotBlank(userName) || StringUtils.isNotBlank(loginUserName)) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            LOGGER.debug("用户已登陆: " + userName);
        } else {
            response.setResultCode(ResultCode.RESULT_NOT_AUTHORIZED);
            LOGGER.debug("用户未登陆！");
        }
        LOGGER.info("调用完成/staff/hasLogin");
        return response;
    }

    /**
     * 判断单点登录是否已登录.
     * @param request request
     * @return 是否登录
     */
    @GET
    @Path("/isSsoLogin")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Staff> isSsoLogin(@Context HttpServletRequest request) {
        LOGGER.info("开始调用/staff/isSsoLogin");
        LOGGER.debug("判断单点登录是否已登陆");
        BaseResponse<Staff> response = new BaseResponse<>();
        String loginUserName = null;
        Assertion assertion = AssertionHolder.getAssertion();
        if (assertion != null) {
            AttributePrincipal attributePrincipal = assertion.getPrincipal();
            if (attributePrincipal != null) {
                loginUserName = attributePrincipal.getName();
            }
        }
        //HttpSession session = servletRequest.getSession();
        //String loginUserName = (String) session.getAttribute("loginUserName");
        // 2019.07.30 该用ldap统一账户 帐号默认为邮箱的前缀
        // 原来username是拼音全拼 考虑到直接修改username可能会有潜在影响
        // 这里根据有户名获取员工信息的接口改为根据邮箱前缀获取
        if (StringUtils.isNotBlank(loginUserName)) {
            String mail = loginUserName + mailSuffix;
            Staff loginStaff = staffService.getStaffByMail(mail);
            if (loginStaff != null) {
                //设置session
                servletRequest.getSession().setAttribute("userName", loginStaff.getUserName());
                servletRequest.getSession().setAttribute("staffId", loginStaff.getStaffId());
                servletRequest.getSession().setAttribute("name", loginStaff.getName());
                response.setObj(loginStaff);
                response.setMessage("已经登录，并成功设置session");
                response.setResultCode(ResultCode.RESULT_SUCCESS);
                return response;
            } else {
                response.setMessage("认证通过，但OA系统中未找到此人信息");
                response.setResultCode(ResultCode.RESULT_NOT_EXIST);
                return response;
            }
        }
        response.setMessage("登录失败");
        response.setResultCode(ResultCode.RESULT_ERROR);
        LOGGER.info("调用完成/staff/isSsoLogin");
        return response;
    }

    /**
     * 获取生日提醒信息.
     *
     * @return 列信息
     * @throws ParseException ParseException
     */
    @Path("/birthList")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Bean> getBirthList() throws ParseException {
        LOGGER.info("开始调用/staff/birthList");
        BaseResponse<Bean> response = new BaseResponse<>();
        List<Bean> beanList = new ArrayList<>();
        List<Staff> birthList = staffService.getbirthRemindList(); //当月生日
        for (Staff staff : birthList) {
            Bean bean = new Bean();
            bean.setCol(staff.getName() + " 将在本月生日；");
            beanList.add(bean);
        }
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setData(beanList);
        LOGGER.info("调用完成/staff/birthList");
        return response;
    }

    /**
     * 获取转正提醒信息.
     *
     * @return 列信息
     * @throws ParseException ParseException
     */
    @Path("/formalList")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Bean> getFormalList() throws ParseException {
        LOGGER.info("开始调用/staff/formalList");
        BaseResponse<Bean> response = new BaseResponse<>();
        List<Bean> beanList = new ArrayList<>();
        List<Staff> formalList = staffService.getformalRemindList(); //转正
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        for (Staff staff : formalList) {
            Bean bean = new Bean();
            bean.setCol(staff.getName() + " 将于 " + sdf.format(sdf1.parse(staff.getFormalDate())) + " 转正；");
            beanList.add(bean);
        }
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setData(beanList);
        LOGGER.info("调用完成/staff/formalList");
        return response;
    }

    /**
     * 获取资格证书到期提醒信息.
     *
     * @return 列信息
     * @throws ParseException ParseException
     */
    @Path("/certificateList")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Bean> getCertificateList() throws ParseException {
        LOGGER.info("开始调用/staff/certificateList");
        BaseResponse<Bean> response = new BaseResponse<>();
        List<Bean> beanList = new ArrayList<>();
        List<Staff> certificateList = staffService.getcertificateRemindList(); //资格证书到期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        for (Staff staff : certificateList) {
            Bean bean = new Bean();
            bean.setCol(staff.getName() + " 的《" + staff.getProfessionalName() + "》证书将于 " + sdf.format(staff.getCertificateValidity()) + " 到期；");
            beanList.add(bean);
        }
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setData(beanList);
        LOGGER.info("调用完成/staff/certificateList");
        return response;
    }

    /**
     * 获取合同到期提醒信息.
     *
     * @return 列信息
     * @throws ParseException ParseException
     */
    @Path("/contList")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Bean> getContList() throws ParseException {
        LOGGER.info("开始调用/staff/contList");
        BaseResponse<Bean> response = new BaseResponse<>();
        List<Bean> beanList = new ArrayList<>();
        List<Staff> contList = staffService.getcontRemindList(); //合同到期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        for (Staff staff : contList) {
            Bean bean = new Bean();
            bean.setCol(staff.getName() + " 将于 " + sdf.format(sdf1.parse(staff.getContractEnd())) + " 合同到期；");
            beanList.add(bean);
        }
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setData(beanList);
        LOGGER.info("调用完成/staff/contList");
        return response;
    }

    /**
     * 导出员工信息(支持列定制).
     *
     * @param request      请求信息
     * @param response     返回信息
     * @param staffRequest StaffRequest
     * @return 结果
     * @throws IOException IOException
     */
    @POST
    @Path("export")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] downloadExcel(@Context HttpServletRequest request, @Context HttpServletResponse response,
                                StaffRequest staffRequest) throws IOException {
        LOGGER.info("开始调用/staff/export");
        String exportStr = staffRequest.getExportStr();
        assert StringUtils.isNotBlank(exportStr);
        String[] exportArr = exportStr.split(",");
        List<ExcelExportEntity> entities = staffService.getExcelExportEntityByArr(exportArr);
        List<Map<String, Object>> staffMap = staffService.getStaffMap(staffRequest);
        /*List<StaffExcel> staffList = staffService.getStaffList(staffRequest);*/
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(
                "员工信息", "员工信息", "员工信息"), entities, staffMap);
        File file = new File(exportPath);
        if (!file.exists()) {
            boolean rs = file.mkdirs();
            LOGGER.info("创建文件:" + file + (rs == true ? "成功" : "失败"));
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        response.addHeader("filename", URLEncoder.encode("员工信息导出.xls", "utf-8"));
        LOGGER.info("调用完成/staff/export");
        return bos.toByteArray();
    }

    /**
     * 根据staffId获取用户信息(包括部门和对应角色).
     *
     * @param staffId staffId
     * @return 员工信息
     */
    @GET
    @Path("getStaffWithRole/{staffId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Staff> getStaffWithRoleByUserId(@PathParam("staffId") String staffId) {
        LOGGER.info("开始调用/staff/getStaffWithRole/" + staffId);
        BaseResponse<Staff> response = new BaseResponse<>();
        if (StringUtils.isBlank(staffId)) {
            response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
            response.setMessage("参数错误");
            return response;
        }
        Staff staff = staffService.getStaffWithRoleByUserId(staffId);
        List<Staff> staffs = new ArrayList<>();
        staffs.add(staff);
        response.setData(staffs);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setMessage("获取成功!");
        LOGGER.info("调用完成/staff/getStaffWithRole");
        return response;
    }

    /**
     * 根据staffId获取用户信息.
     *
     * @param staffId staffId
     * @return 员工信息
     */
    @GET
    @Path("/{staffId}/getStaffByStaffId")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Staff getStaffByStaffId(@PathParam("staffId") String staffId) {
        LOGGER.info("开始调用/staff/" + staffId + "/getStaffByStaffId");
        Staff staff = staffService.getStaffByUserId(staffId);
        LOGGER.info("调用完成/staff/getStaffByStaffId");
        return staff;
    }

    /**
     * 设置员工的部门和角色信息.
     *
     * @param departRoleRequest departRoleRequest
     * @return 结果
     */
    @POST
    @Path("setDepartRole")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> setDepartRole(DepartRoleRequest departRoleRequest) {
        LOGGER.info("开始调用/staff/setDepartRole");
        BaseResponse<String> response = new BaseResponse<>();
        long staffId = departRoleRequest.getStaffId();
        if (staffId < 0) {
            response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
            response.setMessage("参数错误");
            return response;
        }
        List<DepartRole> departRoles = departRoleRequest.getDepartRoles();
        boolean res = staffService.setDepartRole(staffId, departRoles);
        if (res) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("设置成功");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("设置失败");
        }
        LOGGER.info("调用完成/staff/setDepartRole");
        return response;
    }

    /**
     * 导入员工信息用
     */
    private List<StaffExcel> staffExcelList;

    /**
     * 导入员工信息前处理.
     *
     * @param fileInputStream          输入文件流
     * @param contentDispositionHeader 文件属性信息
     * @return 结果
     * @throws IOException IOException
     */
    @POST
    @Path("preUpload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<StaffExcel> preUpload(@FormDataParam("file") InputStream fileInputStream,
                                              @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) throws IOException {
        LOGGER.info("开始调用/staff/upload");
        BaseResponse<StaffExcel> response = new BaseResponse<>();
        ImportParams importParams = new ImportParams();
        List<StaffExcel> res = null;
        try {
            res = ExcelImportUtil.importExcel(fileInputStream, StaffExcel.class, importParams);
            /*System.out.println(res);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert res != null;
        for (StaffExcel staff : res) {
            if (StringUtils.isBlank(staff.getPosition())) {
                LOGGER.error("岗位字段为空,请检查导入数据!");
                response.setResultCode(ResultCode.RESULT_ERROR);
                response.setMessage("岗位字段为空！");
                return response;
            }
            //下面这行代码原先是不用的 之前导入时时间加了format会自动格式化为yyyy/MM/dd格式 升级了3.0版本之后就变成了
            //Thu Jan 01 00:00:00 CST 2015这种 所以这里加了层转换
            staff = staffService.timeFormate(staff);
            //时间戳
            staff.setStaffId(Long.parseLong(DateUtil.getIdByDate()));
            //根据身份证信息补全
            staff = staffService.completeStaffByIdNum(staff);
            //计算转正日期
            String entryDateStr = staff.getEntryDate();
            if (StringUtils.isNotBlank(entryDateStr)) {
                //导入日期格式变了 所以做了调整
                DateTimeFormatter format = DateTimeFormat.forPattern("yyyy/MM/dd");
                DateTime entryDateTime = DateTime.parse(entryDateStr, format);
                String formalDateStr = entryDateTime.plusMonths(2).toString(format);
                staff.setFormalDate(formalDateStr);
            }
            String name = staff.getName();
            //将名字的全拼作为用户名
            String userName = PinyinUtil.getInstance().getStringPinyin(name);
            staff.setEmail(PinyinUtil.getInstance().getZZEmailByName(name));
            staff.setUserName(userName);
            //默认密码12345
            staff.setPassword(MD5Util.md5("12345"));
            //设置staffId
            //String staffIdStr = StaffIdUtil.resMap.get(staff.getName());
            String staffIdStr = zkemService.getUserIdByName(staff.getName());
            if (StringUtils.isNotBlank(staffIdStr)) {
                staff.setStaffId(Long.parseLong(staffIdStr));
            }
            //设置部门
            String departName = staff.getDepartName();
            if (StringUtils.isNotBlank(departName)) {
                String departId = departService.getDepartIdByName(departName);
                staff.setDepartId(departId);
            }
            //设置库中是否已存在
            staff.setIsExist(0);
            Staff staffTemp = staffService.getStaffByName(name);
            if (staffTemp != null) {
                staff.setIsExist(1);
            }
            //设置司龄 工龄
            staff = taskTest.countSeniority(staff);
        }
        staffExcelList = res;

        //boolean result = staffService.addStaffList(res);
        //初始化年假信息
        //taskTest.initAnnualInfo();
        //初始化异动信息
        //turnoverService.initData(res);
        response.setData(res);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setMessage("上传成功！");
        LOGGER.info("调用完成/staff/upload");
        return response;
    }

    /**
     * 导入员工信息.
     *
     * @param staffIdStr staffIdStr
     * @return 结果
     * @throws IOException IOException
     */
    @POST
    @Path("{staffIdStr}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> upload(@PathParam("staffIdStr") String staffIdStr) {
        LOGGER.info("开始调用/staff/upload");
        BaseResponse<String> response = new BaseResponse<>();
        if (CollectionUtils.isEmpty(staffExcelList)) {
            LOGGER.error("staffExcelList为空");
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("staffExcelList为空！");
            return response;
        }
        if (StringUtils.isBlank(staffIdStr)) {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("没有选择员工！");
            return response;
        }
        List<String> staffIdList = Arrays.asList(staffIdStr.split(","));
        List<StaffExcel> resStaffs = new ArrayList<>();
        for (StaffExcel staffExcel : staffExcelList) {
            String staffId = String.valueOf(staffExcel.getStaffId());
            if (staffIdList.contains(staffId)) {
                resStaffs.add(staffExcel);
            }
        }
        if (!CollectionUtils.isEmpty(resStaffs)) {
            boolean result = staffService.uploadStaffs(resStaffs);
            //boolean result = staffService.addStaffList(resStaffs);
            //初始化年假信息
            taskTest.initAnnualInfo();
            //初始化异动信息 2017-12-19 fangguitang OA-210 只记录入职信息
            try {
                turnoverService.initData(resStaffs);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (result) {
                response.setResultCode(ResultCode.RESULT_SUCCESS);
                response.setMessage("上传成功！");
                return response;
            }
        }
        response.setResultCode(ResultCode.RESULT_ERROR);
        response.setMessage("上传失败！");
        return response;
    }

    /**
     * 获取所有双软人员属性.
     *
     * @return 列表
     */
    @Path("/allPostList")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Bean> getAllPostList() {
        LOGGER.info("开始调用/staff/allPostList");
        List<Bean> list = staffService.getAllPostList();
        LOGGER.info("调用完成/staff/allPostList");
        return list;
    }

    /**
     * 根据部门id获取员工信息（包括其角色信息）.
     *
     * @param departId 部门id
     * @return 员工信息
     */
    @Path("/getByDepart/{departId}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Staff> getStaffByDepart(@PathParam("departId") long departId) {
        LOGGER.info("开始调用/staff/getByDepart/" + departId);
        BaseResponse<Staff> response = new BaseResponse<>();
        if (departId < 1) {
            response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
            response.setMessage("参数错误！");
            return response;
        }
        List<Staff> staffList = staffService.getStaffByDepart(departId);
        response.setData(staffList);
        response.setMessage("获取成功！");
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/staff/getByDepart");
        return response;
    }

    /**
     * 上传.
     *
     * @param fileInputStream          输入文件流
     * @param contentDispositionHeader 文件属性信息
     * @param staffId                  用户ID
     * @param fileTyp                  文件类型
     * @return 结果
     * @throws IOException IOException
     */
    @POST
    @Path("{staffId}/{fileTyp}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> upload(@FormDataParam("file") InputStream fileInputStream,
                                       @FormDataParam("file") FormDataContentDisposition contentDispositionHeader,
                                       @PathParam("staffId") String staffId, @PathParam("fileTyp") String fileTyp) throws IOException {
        LOGGER.info("开始调用/staff/" + staffId + "/" + fileTyp + "/upload");
        BaseResponse<String> response = new BaseResponse<>();
        Properties props = new Properties();
        try {
            props.load(TalentRest.class.getClassLoader().getResourceAsStream("props/system.properties"));
        } catch (IOException e) {
            LOGGER.info("加载配置文件出错。。。");
            e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Staff staff = staffService.getStaffByUserId(staffId);
        String fileName = staff.getName() + "_" + sdf.format(new Date()) + fileTyp;
        staff.setResumeName(fileName);
        staffService.editStaff(staff);
        FileOutputStream out = new FileOutputStream(new File(props.getProperty("resume.path") + fileName));
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(fileInputStream);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[NUM2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
        out.close();
        LOGGER.info("调用完成/staff/upload");
        return response;
    }

    /**
     * 下载.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param staffId  用户ID
     * @return byte
     * @throws IOException IOException
     */
    @POST
    @Path("{staffId}/down")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] downloadExcel(@Context HttpServletRequest request, @Context HttpServletResponse response,
                                @PathParam("staffId") String staffId) throws IOException {
        LOGGER.info("开始调用/staff/" + staffId + "/down");
        Properties props = new Properties();
        try {
            props.load(TalentRest.class.getClassLoader().getResourceAsStream("props/system.properties"));
        } catch (IOException e) {
            LOGGER.info("加载配置文件出错。。。");
            e.printStackTrace();
        }

        Staff staff = staffService.getStaffByUserId(staffId);
        String resumeName = staff.getResumeName();
        String fileTyp = resumeName.split("\\.")[1];

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        InputStream fis = new BufferedInputStream(new FileInputStream(props.getProperty("resume.path") + resumeName));
        byte[] buffer = new byte[NUM3];
        int len = 0;
        while ((len = fis.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        fis.close();
        response.addHeader("filename", fileTyp); //文件命名
        LOGGER.info("调用完成/staff/down");
        return bos.toByteArray();
    }

    /**
     * *** username不具备唯一性 该方法推荐使用
     * 通过userName获取员工信息.
     *
     * @param userName userName
     * @return 员工信息
     */
    @Deprecated
    @Path("{userName}/getByUserName")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Staff getByUserName(@PathParam("userName") String userName) {
        LOGGER.info("开始调用{userName}/getByUserName");
        return staffService.getStaffByUserName(userName, true);
    }

    /**
     * 通过邮箱前缀获取员工信息.
     *
     * @param prefix 邮箱前缀
     * @return 员工信息
     */
    @Path("{prefix}/getByMailPrefix")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Staff getByMailPrefix(@PathParam("prefix") String prefix) {
        LOGGER.info("开始调用{userName}/getByMailPrefix");
        String mail = prefix + mailSuffix;
        return staffService.getStaffByMail(mail);
    }

    /**
     * 设置统一权限管理系统中的用户名和权限.
     * todo 暂时没有直接调用 而是打印出用户信息然后手动调用
     *
     * @return String
     */
    @Path("/setUserAndRole")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> setUserAndRole() {
        LOGGER.info("开始调用/staff/setUserAndRole");
        BaseResponse<String> response = new BaseResponse<>();
        Map<String, String> resMap = new HashMap<>();
        StaffRequest staffRequest = new StaffRequest();
        staffRequest.setOnJob(1);
        List<Staff> staffList = staffService.getWithRole(staffRequest);
        for (Staff staff : staffList) {
            List<DepartRole> departRoles = staff.getDepartRoles();
            String topRole = RoleUtil.getTopRole(departRoles);
            resMap.put(staff.getUserName(), topRole);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            //将结果打印出来
            LOGGER.info(mapper.writeValueAsString(resMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setMessage("获取成功！");
        LOGGER.info("调用完成/staff/setUserAndRole");
        return response;
    }
}