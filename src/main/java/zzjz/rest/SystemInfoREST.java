package zzjz.rest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zzjz.bean.*;
import zzjz.service.PermissionService;
import zzjz.service.SystemInfoService;
import zzjz.util.ConfigUtil;
import zzjz.util.ConstantUtil;
import zzjz.util.DBUtil;
import zzjz.util.PageUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 梅宏振
 * @ClassName: SystemInfoREST
 * @Description: 系统管理模块Rest接口类
 * @date 2016年7月15日 上午8:35:38
 */
@Component
@Path("systemInfo")
public class SystemInfoREST {

    private Logger log = Logger.getLogger(SystemInfoREST.class);
    private String message = "";
    private String URI = "";

    @Autowired
    private SystemInfoService systemInfoService;

    @Autowired
    private PermissionService permissionService;

    @Context
    HttpServletRequest servletRequest;

    @POST
    @Path("list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<SystemInfo> list(BaseRequest request,
                                         @Context HttpHeaders headers) {
        // 获取当前操作用户ID，如果为空，则提示当前用户未登录
        URI = "systemInfo/list";
        log.debug("开始调用接口：" + URI);
        BaseResponse<SystemInfo> response = new BaseResponse<SystemInfo>();
        // 获取当前操作用户ID
        MultivaluedMap<String, String> headerParams = headers
                .getRequestHeaders();
        String userId = headerParams.getFirst("userId");
        log.debug("当前用户ID：" + userId);
        // 用户ID非空校验
        if (StringUtils.isEmpty(userId) || "0".equals(userId)) {
            message = "当前用户未登录";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            log.debug(message);
            return response;
        }
        // 获取分页信息
        // 分页的处理 暂时比考虑分页
        PagingEntity pagingEntity = request.getPaging();
        if (null == pagingEntity) {
            message = "分页的paging不能为空";
            response.setMessage(message);
            log.debug(message);
            response.setResultCode(ResultCode.RESULT_ERROR);
            return response;
        }
        String deleteFlag = "0";
        String permissionName = "查看系统信息";
        // 获取用户具有查看权限的系统
        ConfigUtil.load("system.properties");
        String admin = ConfigUtil.getProperty("tyqxgl.administrator");
        // 获取总记录数
        int totalCount = 0;
        if (userId.equals(admin)) {
            totalCount = systemInfoService.getTotalCount(deleteFlag);
        } else {
            totalCount = systemInfoService.getTotalCount(userId,
                    permissionName, deleteFlag);
        }

        log.debug("未删除的系统信息总条数：" + totalCount);
        // 根据分页信息和总记录数处理分页信息
        List<Object> otherData = PageUtil.dealPaging(pagingEntity, totalCount);
        int pageNo = pagingEntity.getPageNo();
        int pageSize = pagingEntity.getPageSize();
        int index = (pageNo - 1) * pageSize;
        log.debug("显示的词条页数：" + pageNo + " 每页的大小：" + pageSize + " 起始位置：" + index);
        // 获取系统信息列表
        List<SystemInfo> list = new ArrayList<SystemInfo>();
        if (userId.equals(admin)) {
            list = systemInfoService.list(deleteFlag, index, pageSize);
        } else {
            list = systemInfoService.list(userId, permissionName, deleteFlag,
                    index, pageSize);
        }

        // 返回列表
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setData(list);
        response.setOtherData(otherData);
        log.debug("结束调用接口：" + URI);
//		log.debug("请求数据：" + JSONObject.fromObject(response).toString());
//		log.debug("返回数据：" + JSONObject.fromObject(response).toString());
        return response;
    }

    @POST
    @Path("insert")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<SystemInfo> insert(SystemInfo request,
                                           @Context HttpHeaders headers) {
        // 获取当前操作用户ID，如果为空，则提示当前用户未登录
        URI = "systemInfo/list";
        log.debug("开始调用接口：" + URI);
        BaseResponse<SystemInfo> response = new BaseResponse<SystemInfo>();
        // 获取当前操作用户ID
        MultivaluedMap<String, String> headerParams = headers
                .getRequestHeaders();
        String userId = headerParams.getFirst("userId");
        log.debug("当前用户ID：" + userId);
        // 用户ID非空校验
        if (StringUtils.isEmpty(userId) || "0".equals(userId)) {
            message = "当前用户未登录";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            log.debug(message);
            return response;
        }
        // 需要插入的数据非空判断
        String systemName = request.getName();
        String systemSign = request.getSign();
        if (StringUtils.isEmpty(systemName)) {
            message = "系统名称不能为空";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            log.debug(message);
            return response;
        }

        // 需要插入的数据唯一性校验
        SystemInfo info = systemInfoService.checkUnique(systemName, systemSign);
        if (null != info) {
            message = "系统名称和系统标识已存在!";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            log.debug(message);
            return response;
        }
        // 数据源正确性校验 //comment by Steven
        /*String dataSource = request.getDataSource();
		boolean flag = validateDataSource(dataSource);
		if (!flag) {
			message = "数据源配置错误" + dataSource;
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}*/
        // 权限校验
        String permissionName = "添加系统";
        info = systemInfoService.checkUnique(ConstantUtil.getSystemName(),
                ConstantUtil.getSystemSign());
        // 首先判断用户是否是超管，如果不是超管，再判断默认系统是否存在和用户是否有权限 //comment by Steven
		/*if (!userId.equals(ConstantUtil.getAdmin())
				&& (null == info || permissionService.checkPermission(userId,
						info.getId() + "", permissionName))) {
			message = "没有" + permissionName + "的权限";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}*/
        // 数据准备
        request.setCreator(userId);
        // 插入数据
        String key = systemInfoService.insert(request);
        // 返回插入后的数据
        info = systemInfoService.getSystemInfoById(key, 0);
        if (null == info) {
            message = "数据保存失败";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            log.debug(message);
            return response;
        }

        if ("统一权限管理系统".equals(info.getName())) {
            String path = SystemInfoREST.class.getClassLoader().getResource("defaultTYQXGL.sql").getPath();
            if (path.indexOf("%20") >= 0) {
                path = path.replaceAll("%20", " ");
            }
            //添加统一权限默认权限
            try {
                systemInfoService.addDefaultAuthority(path, userId, key);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<SystemInfo> list = new ArrayList<SystemInfo>();
        list.add(info);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setData(list);
        log.debug("结束调用接口：" + URI);
        return response;
    }

    /**
     * @param dataSources
     * @return
     * @throws
     * @Title: validateDataSource
     * @Description: 校验数据源的正确性，可以没有数据源，数据源的格式：tableName;columnName1,columnName2[,
     * columnName3]
     * ;jdbcUrl,user,password!tableName;columnName1,columnName2
     * [,columnName3] ;jdbcUrl,user,password
     */
    private boolean validateDataSource(String dataSources) {
        // 对系统的数据源处理
        if (";;".equals(dataSources)) {
            return true;
        }
        String[] dataSourceArr = dataSources.split(DBUtil.DATA_SOURCE_SEPARATE);
        for (String dataSource : dataSourceArr) {
            String[] source = dataSource.split(DBUtil.DATA_SEPARATE);
            if (source.length < 3) {
                return false;
            }
            String tableName = source[0];
            String columns = source[1];
            if (columns.split(DBUtil.COLUMN_SEPARATE).length < 2) {
                return false;
            }
            // 根据数据源获取数据
            String conStr = source[2];// 连接串
            String sql = "select " + columns + " from " + tableName;
            Connection conn = DBUtil.getConnection(conStr);
            ResultSet rs = DBUtil.executeQuery(conn, sql);
            if (null == rs) {
                return false;
            }
            DBUtil.close(rs);
            DBUtil.close(conn);
        }
        return true;
    }

    @DELETE
    @Path("{systemId}/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<SystemInfo> delete(
            @PathParam("systemId") long systemId, @Context HttpHeaders headers) {
        // 获取当前操作用户ID，如果为空，则提示当前用户未登录
        URI = "systemInfo/list";
        log.debug("开始调用接口：" + URI);
        BaseResponse<SystemInfo> response = new BaseResponse<SystemInfo>();
        // 获取当前操作用户ID
        MultivaluedMap<String, String> headerParams = headers
                .getRequestHeaders();
        String userId = headerParams.getFirst("userId");
        log.debug("当前用户ID：" + userId);
        // 用户ID非空校验
        if (StringUtils.isEmpty(userId) || "0".equals(userId)) {
            message = "当前用户未登录";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            log.debug(message);
            return response;
        }
        // 删除系统权限校验
		/*String permissionName = "删除系统"; //comment by Steven
		boolean has = permissionService.checkPermission(userId, systemId + "",
				permissionName);
		if (!has) {
			message = "没有" + permissionName + "的权限";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}*/
        // 删除数据
        SystemInfo info = systemInfoService.getSystemInfoById(systemId + "", 0);// 判断数据是否存在
        if (null == info) {
            message = "数据删除失败";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            log.debug(message);
            return response;
        }
        Map<String, String> columnValues = systemInfoService
                .getColumnValues(systemId);
        String tableName = "tb_system";
        /*boolean bak = bakService.bak(tableName, columnValues);// 数据备份
        if (!bak) {
            message = "数据删除失败";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            log.debug(message);
            return response;
        }*/
        // String deleteFlag = "1";
        // systemInfoService.delete(systemId, deleteFlag);
        systemInfoService.delete(systemId);
        List<SystemInfo> list = new ArrayList<SystemInfo>();
        list.add(info);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setData(list);
        log.debug("结束调用接口：" + URI);
        return response;
    }

    @PUT
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<SystemInfo> update(SystemInfo request,
                                           @Context HttpHeaders headers) {
        // 获取当前操作用户ID，如果为空，则提示当前用户未登录
        URI = "systemInfo/list";
        log.debug("开始调用接口：" + URI);
        BaseResponse<SystemInfo> response = new BaseResponse<SystemInfo>();
        // 获取当前操作用户ID
        MultivaluedMap<String, String> headerParams = headers
                .getRequestHeaders();
        String userId = headerParams.getFirst("userId");
        log.debug("当前用户ID：" + userId);
        // 用户ID非空校验
        if (StringUtils.isEmpty(userId) || "0".equals(userId)) {
            message = "当前用户未登录";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            log.debug(message);
            return response;
        }
        // 需要插入的数据非空判断
        String systemName = request.getName();
        String systemSign = request.getSign();
        if (StringUtils.isEmpty(systemName)) {
            message = "系统名称不能为空!";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            log.debug(message);
            return response;
        }
        // 判断系统是否存在
        long systemId = request.getId();
        SystemInfo info = systemInfoService.getSystemInfoById(systemId + "", 0);
        if (null == info) {
            message = "系统不存在!";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            log.debug(message);
            return response;
        }
        // 需要插入的数据唯一性校验
        info = systemInfoService.checkUnique(systemName, systemSign);
        if (null != info && info.getId() != systemId) {
            message = "系统名称和系统标识已存在!";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            log.debug(message);
            return response;
        }
        // 数据源正确性校验
		/*String dataSource = request.getDataSource();
		boolean flag = validateDataSource(dataSource);
		if (!flag) {
			message = "数据源配置错误" + dataSource;
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}*/
        // 设置当前的操作用户ID
        request.setCreator(userId);
        // 校验更新权限
		/*String permissionName = "更新系统";//comment by Steven
		boolean has = permissionService.checkPermission(userId, systemId + "",
				permissionName);
		if (!has) {
			message = "没有" + permissionName + "的权限";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}*/
        // 更新数据
        boolean isUpdateDeleteFlag = false;
        String key = systemInfoService.update(request, isUpdateDeleteFlag);
        // 返回更新后的数据
        info = systemInfoService.getSystemInfoById(key, 0);
        if (null == info) {
            message = "数据保存失败!";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            log.debug(message);
            return response;
        }
        List<SystemInfo> list = new ArrayList<SystemInfo>();
        list.add(info);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setData(list);
        log.debug("结束调用接口：" + URI);
        return response;
    }

    /**
     * 通过id获取系统信息
     *
     * @param systemId
     * @param headers
     * @return
     * @author 房桂堂
     */
    @GET
    @Path("getById/{systemId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<SystemInfo> getById(@PathParam("systemId") long systemId, @Context HttpHeaders headers) {
        BaseResponse<SystemInfo> response = new BaseResponse<>();
        if (systemId < 0) {
            message = "参数错误";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            log.debug(message);
            return response;
        }
        SystemInfo systemInfo = systemInfoService.getSystemInfoById(systemId);
        List<SystemInfo> res = new ArrayList<>();
        res.add(systemInfo);
        if (systemInfo == null) {
            message = "没有此系统信息";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            log.debug(message);
            return response;
        } else {
            message = "查询成功";
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage(message);
            response.setData(res);
        }
        return response;
    }

    @POST
    @Path("/systemList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<SystemInfo> getSystemListByUserId(RoleAuthorityRequest request, @Context HttpHeaders headers) {
        log.debug("开始调用：systemInfo/systemList");
        BaseResponse<SystemInfo> response = new BaseResponse<SystemInfo>();
        // 获取当前操作用户ID
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst("userId");
        log.debug("当前用户ID：" + userId);
        // 用户ID非空校验
        if (StringUtils.isEmpty(userId) || "0".equals(userId)) {
            message = "当前用户未登录";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            log.debug(message);
            return response;
        }
        //获取session中保存的登录用户
        HttpSession session = servletRequest.getSession();
        String currentUserId = (String) session.getAttribute("userId");
        List<SystemInfo> systemInfoList;
        // 获取用户具有查看权限的系统
        ConfigUtil.load("system.properties");
        String adminUserId = ConfigUtil.getProperty("tyqxgl.administrator");
        if (adminUserId.equals(currentUserId)) {
            systemInfoList = systemInfoService.list("0", 0, 10000);
        } else {
            String authorityName = request.getAuthorityName();
            systemInfoList = systemInfoService.getSystemListByUserId(authorityName, currentUserId);
        }
        response.setData(systemInfoList);
        return response;
    }

    @POST
    @Path("/permissionSystem")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<SystemInfo> permissionSystem(RoleAuthorityRequest request, @Context HttpHeaders headers) {
        log.debug("开始调用：systemInfo/systemList");
        BaseResponse<SystemInfo> response = new BaseResponse<SystemInfo>();
        // 获取当前操作用户ID
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst("userId");
        log.debug("当前用户ID：" + userId);
        // 用户ID非空校验
        if (StringUtils.isEmpty(userId) || "0".equals(userId)) {
            message = "当前用户未登录";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            log.debug(message);
            return response;
        }
        //获取session中保存的登录用户
        HttpSession session = servletRequest.getSession();
        //String currentUserId = (String) session.getAttribute("userId");
        ConfigUtil.load("system.properties");
        String adminUserId = ConfigUtil.getProperty("tyqxgl.administrator");
//分页
        PagingEntity pagingEntity = request.getPaging();
        if (null == pagingEntity) {
            message = "分页的paging不能为空";
            response.setMessage(message);
            log.debug(message);
            response.setResultCode(ResultCode.RESULT_ERROR);
            return response;
        }
        // 获取总记录数
        int totalCount = 0;
        if (adminUserId != null && userId != null && adminUserId.equals(userId)) {
            totalCount = systemInfoService.getTotalCountAdmList();
        } else {
            totalCount = systemInfoService.getTotalCountList(userId, request.getAuthorityName());
        }
//		 systemInfoService.getTotalCountList(userId);
        log.debug("未删除的系统信息总条数：" + totalCount);
        // 根据分页信息和总记录数处理分页信息
        List<Object> otherData = PageUtil.dealPaging(pagingEntity, totalCount);
        int pageNo = pagingEntity.getPageNo();
        int pageSize = pagingEntity.getPageSize();
        int index = (pageNo - 1) * pageSize;
        log.debug("显示的词条页数：" + pageNo + " 每页的大小：" + pageSize + " 起始位置：" + index);
        // 获取系统信息列表

        List<SystemInfo> systemInfoList = new ArrayList<SystemInfo>();
        // 获取用户具有查看权限的系统

        if (adminUserId != null && userId != null && adminUserId.equals(userId)) {
            systemInfoList = systemInfoService.list("0", index, pageSize);
            if (systemInfoList.size() > 0) {
                for (int i = 0; i < systemInfoList.size(); i++) {
                    systemInfoList.get(i).setAddFalg(true);
                    systemInfoList.get(i).setDelFlag(true);
                    systemInfoList.get(i).setUpdateFlag(true);
                    systemInfoList.get(i).setShowFlag(true);
                }
            }

        } else {
            String authorityName = request.getAuthorityName();
            systemInfoList = systemInfoService.getSystemListByUserId(index, pageSize, authorityName, userId);
            if (systemInfoList.size() > 0) {
                for (int i = 0; i < systemInfoList.size(); i++) {

                    List<String> list = permissionService.getAuthorityName(userId, systemInfoList.get(i).getId(), "0");
                    if (list.size() > 0) {
                        for (int j = 0; j < list.size(); j++) {
                            if ("查看系统".equals(list.get(j))) {
                                systemInfoList.get(i).setShowFlag(true);
                            }
                            if ("更新系统".equals(list.get(j))) {
                                systemInfoList.get(i).setUpdateFlag(true);
                            }
                            if ("添加系统".equals(list.get(j))) {
                                systemInfoList.get(i).setAddFalg(true);
                            }
                            if ("删除系统".equals(list.get(j))) {
                                systemInfoList.get(i).setDelFlag(true);
                            }

                        }
                    }
                }
            }
        }

        // 返回列表
        response.setData(systemInfoList);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setOtherData(otherData);
        return response;
    }

    /**
     * 获取所有系统信息
     *
     * @param request
     * @param headers
     * @return
     */
    @POST
    @Path("/getAllSystem")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<SystemInfo> getSystemInfo(BaseRequest request, @Context HttpHeaders headers) {
        log.debug("开始调用：systemInfo/getAllSystem");
        BaseResponse<SystemInfo> response = new BaseResponse<>();

        PagingEntity pagingEntity = request.getPaging();
        if (null == pagingEntity) {
            message = "分页的paging不能为空";
            response.setMessage(message);
            log.debug(message);
            response.setResultCode(ResultCode.RESULT_ERROR);
            return response;
        }
        // 获取总记录数
        int totalCount = systemInfoService.getTotalCountAdmList();
        log.debug("未删除的系统信息总条数：" + totalCount);
        // 根据分页信息和总记录数处理分页信息
        List<Object> otherData = PageUtil.dealPaging(pagingEntity, totalCount);
        int pageNo = pagingEntity.getPageNo();
        int pageSize = pagingEntity.getPageSize();
        int index = (pageNo - 1) * pageSize;
        log.debug("显示的词条页数：" + pageNo + " 每页的大小：" + pageSize + " 起始位置：" + index);

        // 获取系统信息列表
        List<SystemInfo> systemInfoList = systemInfoService.list("0", index, pageSize);

        // 返回列表
        response.setData(systemInfoList);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setOtherData(otherData);
        return response;
    }


}
