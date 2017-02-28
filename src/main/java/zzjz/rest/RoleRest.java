package zzjz.rest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zzjz.bean.*;
import zzjz.service.*;
import zzjz.util.ConfigUtil;
import zzjz.util.DBUtil;
import zzjz.util.PageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Path("roleInfo")
public class RoleRest {

	@Autowired
	private RoleService roleService;
	@Autowired
	private SystemInfoService systemInfoService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private PermissionAllotService permissionAllotService;

	@Context
	HttpServletRequest servletRequest;

	private Logger log = Logger.getLogger(getClass());

	private String message = "";

	private String URI = "";

	@POST
	@Path("/queryTargetList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<Map<String,String>> queryTargetList	(@Context HttpHeaders headers,
																RoleRequest request) {// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		URI = "RoleInfo/queryTargetList";
		log.debug("开始调用接口：" + URI);
		BaseResponse<Map<String,String>> response = new BaseResponse<Map<String,String>>();
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
		String roleId = request.getRoleId();
		String permissionId = request.getPermissionId();
		if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(permissionId)) {
			message = "角色ID或者权限ID不能为空";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}
		String deleteFlag = "0";
		long systemId = roleService.getRoleInfoById(roleId, 0).getSystemId();
		/*// 校验权限
		String permissionName = "查看系统角色";
		boolean has = permissionService.checkPermission(userId, systemId + "",
				permissionName);
		if (!has) {
			message = "没有" + permissionName + "的权限";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}*/
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		list = roleService.queryTargetList(roleId,permissionId);
		// 返回列表
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		response.setData(list);
		log.debug("结束调用接口：" + URI);
		//log.debug("请求数据：" + JSONObject.fromObject(request).toString());
		//log.debug("返回数据：" + JSONObject.fromObject(response).toString());
		return response;
	}
	
	@POST
	@Path("/list")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<RoleInfo> queryList(@Context HttpHeaders headers,
											RoleRequest request) {// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		URI = "RoleInfo/list";
		log.debug("开始调用接口：" + URI);
		BaseResponse<RoleInfo> response = new BaseResponse<RoleInfo>();
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
		long systemId = request.getSystemId();
		/*// 校验权限
		String permissionName = "查看系统角色";
		boolean has = permissionService.checkPermission(userId, systemId + "",
				permissionName);
		if (!has) {
			message = "没有" + permissionName + "的权限";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}*/

		HttpSession session = servletRequest.getSession();//获取session
		//String currentUserId = (String)session.getAttribute("userId");//获取登录的用户Id
		// 获取用户具有查看权限的系统
		ConfigUtil.load("system.properties");
		String adminUserId = ConfigUtil.getProperty("tyqxgl.administrator");

		int totalCount = 0;
		if (-1 == systemId) {
			//判断当前用户是否为+超级管理员，为超级管理员返回全部角色列表总记录数,否则返回具有权限的角色列表总记录数
			if(adminUserId.equals(userId)) {
				totalCount = roleService.getTotalCount(deleteFlag);
			}else{
				totalCount = roleService.getTotalCount("查看角色", userId);
			}
		} else {
			totalCount = roleService.getTotalCountBySystemId(systemId,deleteFlag);
		}

		log.debug("未删除的系统信息总条数：" + totalCount);
		// 根据分页信息和总记录数处理分页信息
		List<Object> otherData = PageUtil.dealPaging(pagingEntity, totalCount);
		int pageNo = pagingEntity.getPageNo();
		int pageSize = pagingEntity.getPageSize();
		int index = (pageNo - 1) * pageSize;
		log.debug("显示的词条页数：" + pageNo + " 每页的大小：" + pageSize + " 起始位置：" + index);
		// 获取系统信息列表（角色列表）
		List<RoleInfo> list = null;


		if (-1 == systemId) {
			//判断当前用户是否为超级管理员，为超级管理员返回全部角色列表,否则返回具有权限的角色列表
			if(adminUserId.equals(userId)) {
				list = roleService.list(deleteFlag, index, pageSize);
			}else{
				list = roleService.getRoleList("查看角色", userId, index, pageSize);
			}
		} else {
			list = roleService.listBySystemId(systemId, deleteFlag, index,
					pageSize);
		}

		// 根据角色ID获取角色对用的用户信息
//		for (RoleInfo info : list) {
//			long roleId = info.getId();
//			List<PermissionInfo> permissionInfoList = roleService
//					.getPermissionInfoByRoleId(roleId);
//			info.setPermissionInfoList(permissionInfoList);
//		}
		// 返回列表
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		response.setData(list);
		response.setOtherData(otherData);
		log.debug("结束调用接口：" + URI);
		//log.debug("请求数据：" + JSONObject.fromObject(request).toString());
		//log.debug("返回数据：" + JSONObject.fromObject(response).toString());
		return response;
	}

	@POST
	@Path("/authorityList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<PermissionInfo> getAuthorityList(@Context HttpHeaders headers, RoleRequest request) {
		// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		URI = "RoleInfo/delete";
		log.debug("开始调用接口：" + URI);
		BaseResponse<PermissionInfo> response = new BaseResponse<PermissionInfo>();
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
		String roleIdStr = request.getRoleId();//获取角色ID
		// 用户ID非空校验
		if (StringUtils.isEmpty(roleIdStr)) {
			message = "当前用户未登录";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}
		long roleId = Long.parseLong(roleIdStr);
		// 获取分页信息
		PagingEntity pagingEntity = request.getPaging();
		if (null == pagingEntity) {
			message = "分页的paging不能为空";
			response.setMessage(message);
			log.debug(message);
			response.setResultCode(ResultCode.RESULT_ERROR);
			return response;
		}
		// 获取权限列表信息
		List<PermissionInfo> permissionInfoList = permissionService.getPermissionInfoListByRoleId(roleId,pagingEntity);
		if(permissionInfoList != null && permissionInfoList.size() > 0){
			message = "权限获取成功!";
			response.setMessage(message);
			log.debug(message);
			response.setResultCode(ResultCode.RESULT_SUCCESS);
		}else{
			message = "权限获取失败!";
			response.setMessage(message);
			log.debug(message);
			response.setResultCode(ResultCode.RESULT_ERROR);
		}
		response.setData(permissionInfoList);
		int totalCount = permissionService.getPermissionInfoListCountByRoleId(roleId);
		log.debug("权限总条数：" + totalCount);
		// 根据分页信息和总记录数处理分页信息
		List<Object> otherData = PageUtil.dealPaging(pagingEntity, totalCount);
		response.setOtherData(otherData);
		return response;
	}

	@POST
	@Path("insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<RoleInfo> insert(RoleInfo request,
										 @Context HttpHeaders headers) {
		// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		URI = "systemInfo/insert";
		log.debug("开始调用接口：" + URI);
		BaseResponse<RoleInfo> response = new BaseResponse<RoleInfo>();
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
		String roleName = request.getName();
		String roleSign = request.getSign();
		if (StringUtils.isEmpty(roleName)) {
			message = "角色名称不能为空";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}
		// 判断系统是否存在
		long systemId = request.getSystemId();
		SystemInfo systemInfo = systemInfoService.getSystemInfoById(systemId
				+ "", 0);
		if (null == systemInfo || systemInfo.getDeleteFlag() == 1) {
			message = "系统不存在";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}
		// 需要插入的数据唯一性校验
		RoleInfo info = roleService.checkUnique(roleName, roleSign, systemId
				+ "");
		if (null != info) {
			message = "角色名称和角色标示已存在";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}
		// 数据准备
		request.setCreator(userId);
		// 插入数据
		/*// 校验权限
		String permissionName = "添加系统角色";
		boolean has = permissionService.checkPermission(userId, systemId + "",
				permissionName);
		if (!has) {
			message = "没有" + permissionName + "的权限";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}*/
		String roleId = roleService.insert(request);
		// 返回插入后的数据
		info = roleService.getRoleInfoById(roleId, 0);
		if (null == info) {
			message = "数据保存失败";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}
		// 插入角色权限关系
		List<PermissionInfo> permissionInfoList = request
				.getPermissionInfoList();
		if (permissionInfoList != null) {
			for (PermissionInfo permissionInfo : permissionInfoList) {
				// 判断权限是否存在
				long permissionId = permissionInfo.getId();
				PermissionInfo checkPermissionInfo = permissionService
						.queryById(permissionId, 0);
				if (null == checkPermissionInfo) {
					log.debug("权限" + permissionId + "不存在");
					continue;
				}
				permissionInfo.setCreatorId(userId);
				roleService.insertRoleAuthority(Long.valueOf(roleId),
						permissionInfo);
			}
		}
		// 返回插入后的数据
//		permissionInfoList = roleService.getPermissionInfoByRoleId(Long
//				.valueOf(roleId));
		permissionInfoList = new ArrayList<PermissionInfo>();
		info.setPermissionInfoList(permissionInfoList);
		List<RoleInfo> list = new ArrayList<RoleInfo>();
		list.add(info);
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		response.setData(list);
		log.debug("结束调用接口：" + URI);
		return response;
	}

	@DELETE
	@Path("/{roleId}/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<RoleInfo> delete(@PathParam("roleId") long roleId,
										 @Context HttpHeaders headers) {
		// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		URI = "RoleInfo/delete";
		log.debug("开始调用接口：" + URI);
		BaseResponse<RoleInfo> response = new BaseResponse<RoleInfo>();
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
		// 删除数据
		List<RoleInfo> list = new ArrayList<RoleInfo>();
		// 校验更新权限
		RoleInfo info = roleService.getRoleInfoById(roleId + "", 0);
		if (null != info) {
			String permissionName = "删除系统角色";
			boolean has = permissionService.checkPermission(userId,
					info.getSystemId() + "", permissionName);
			if (!has) {
				message = "没有" + permissionName + "的权限";
				response.setResultCode(ResultCode.RESULT_ERROR);
				response.setMessage(message);
				log.debug(message);
				return response;
			}
			// 删除数据
			info = roleService.getRoleInfoById(roleId + "", 0);// 判断数据是否存在
			if (null == info) {
				message = "数据删除失败";
				response.setResultCode(ResultCode.RESULT_ERROR);
				response.setMessage(message);
				log.debug(message);
				return response;
			}
			Map<String, String> columnValues = roleService
					.getColumnValues(roleId);
			String tableName = "tb_role";
			/*boolean bak = bakService.bak(tableName, columnValues);// 数据备份
			if (!bak) {
				message = "数据删除失败";
				response.setResultCode(ResultCode.RESULT_ERROR);
				response.setMessage(message);
				log.debug(message);
				return response;
			}*/
			// String deleteFlag = "1";
			// roleService.delete(roleId, deleteFlag);
			roleService.delete(roleId);
			// 删除角色与权限的关联关系（直接从数据表中删除）
			roleService.deleteRoleAuthority(roleId);
			// 删除角色与用户的关联关系（直接从数据表中删除）
			permissionAllotService.delete(roleId);
			// 返回处理结果
			list.add(info);
		}
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		response.setData(list);
		log.debug("结束调用接口：" + URI);
		return response;
	}

	@PUT
	@Path("update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<RoleInfo> update(RoleInfo request,
										 @Context HttpHeaders headers) {
		// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		URI = "systemInfo/update";
		log.debug("开始调用接口：" + URI);
		BaseResponse<RoleInfo> response = new BaseResponse<RoleInfo>();
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
		String roleName = request.getName();
		String roleSign = request.getSign();
		if (StringUtils.isEmpty(roleName)) {
			message = "角色名称不能为空";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}
		// 判断系统是否存在
		long systemId = request.getSystemId();
		long roleId = request.getId();

		SystemInfo systemInfo = systemInfoService.getSystemInfoById(systemId
				+ "", 0);
		if (null == systemInfo || systemInfo.getDeleteFlag() == 1) {
			message = "系统不存在";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}

		// 需要插入的数据唯一性校验
		RoleInfo info = roleService.checkUnique(roleName, roleSign, systemId
				+ "");
		if (null != info && info.getId() != roleId) {
			message = "角色名称和角色标示已存在";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}
		info = roleService.getRoleInfoById(roleId + "", 0);
		if (null == info) {
			message = "角色不存在";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}
		// 数据准备
		request.setCreator(userId);
		// 插入数据

		/*// 校验权限
		String permissionName = "更新系统角色";
		boolean has = permissionService.checkPermission(userId, systemId + "",
				permissionName);
		if (!has) {
			message = "没有" + permissionName + "的权限";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}*/

		String key = roleService.update(request);
		// 返回插入后的数据
		info = roleService.getRoleInfoById(roleId + "", 0);
		if (null == info) {
			message = "数据保存失败";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}
		// 更新角色权限关系
		List<PermissionInfo> permissionInfoList = request
				.getPermissionInfoList();
		roleService.deleteRoleAuthority(roleId);
		if (permissionInfoList != null) {
			for (PermissionInfo permissionInfo : permissionInfoList) {
				// 判断权限是否存在
				long permissionId = permissionInfo.getId();
				PermissionInfo checkPermissionInfo = permissionService
						.queryById(permissionId, 0);
				if (null == checkPermissionInfo
						|| checkPermissionInfo.getDelete_falg() != 0) {
					log.debug("权限" + permissionId + "不存在");
					continue;
				}
				permissionInfo.setCreatorId(userId);
				roleService.insertRoleAuthority(roleId, permissionInfo);
			}
		}

		// 返回插入后的数据
		//permissionInfoList = roleService.getPermissionInfoByRoleId(roleId);
		permissionInfoList = new ArrayList<PermissionInfo>();
		info.setPermissionInfoList(permissionInfoList);
		List<RoleInfo> list = new ArrayList<RoleInfo>();
		list.add(info);
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		response.setData(list);
		log.debug("结束调用接口：" + URI);
		return response;
	}

	@POST
	@Path("/outDataList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<OutData> queryOutData(RoleRequest request,
											  @Context HttpHeaders headers) {
		// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		URI = "systemInfo/outDataList";
		log.debug("开始调用接口：" + URI);
		BaseResponse<OutData> response = new BaseResponse<OutData>();
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
		// 判断系统是否存在
		/*long systemId = request.getSystemId();
		SystemInfo info = systemInfoService.getSystemInfoById(systemId + "", 0);
		if (null == info || (info != null && info.getDeleteFlag() == 1)) {
			message = "系统不存在";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}
		// 校验权限
		String permissionName = "查看系统外部数据";
		boolean has = permissionService.checkPermission(userId, systemId + "",
				permissionName);
		if (!has) {
			message = "没有" + permissionName + "的权限";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}*/
		String dataSources = request.getDataSource();
		// 对系统的数据源处理
		if (StringUtils.isEmpty(dataSources)) {
			message = "系统没有指定数据源";
			response.setResultCode(ResultCode.RESULT_SUCCESS);
			log.debug(message);
			return response;
		}
		String[] dataSourceArr = dataSources.split(DBUtil.DATA_SOURCE_SEPARATE);
		OutData outData = null;
		List<OutData> list = new ArrayList<OutData>();
		for (String dataSource : dataSourceArr) {
			String[] source = dataSource.split(DBUtil.DATA_SEPARATE);
			if (source.length < 3) {
				continue;
			}
			String tableName = source[0];
			String columns = source[1];
			if (columns.split(DBUtil.COLUMN_SEPARATE).length < 2) {
				continue;
			}
			// 根据数据源获取数据
			String conStr = source[2];// 连接串
			String sql = "select " + columns + " from " + tableName;
			/*if(columns.indexOf("CATEGORY_ID") != -1 || columns.indexOf("category_id") != -1){
				sql = sql+" and IS_PUBLIC = 1";
			}*/
			Connection conn = DBUtil.getConnection(conStr);
			ResultSet rs = DBUtil.executeQuery(conn, sql);
			if (null == rs) {
				continue;
			}
			try {
				while (rs.next()) {
					outData = new OutData();
					outData.setDataSource(conStr);
					outData.setId(rs.getString(1));
					outData.setName(rs.getString(2));
					try {
						outData.setParentId(rs.getString(3));
					} catch (Exception e) {
						outData.setParentId("0");
					}
					list.add(outData);
				}
			} catch (SQLException e) {
				log.debug(e.getMessage());
			}finally{
				DBUtil.close(rs);
				DBUtil.close(conn);
			}

		}
		// 返回数据
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		response.setData(list);
		System.out.println("分类信息数据长度：" + list.size());
		log.debug("结束调用接口：" + URI);
		return response;
	}

	@POST
	@Path("/targetIdList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<Long> getTargetIdList(RoleRequest request,
											  @Context HttpHeaders headers) {
		// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		URI = "systemInfo/targetIdList";
		log.debug("开始调用接口：" + URI);
		BaseResponse<Long> response = new BaseResponse<Long>();
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

		long systemId = request.getSystemId();
		String dataSource = request.getDataSource();
		String permissionId = request.getPermissionId();
		List<Long> targetIdList = roleService.getTargetIdList(userId,systemId,dataSource,permissionId);
		// 返回数据
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		response.setData(targetIdList);
		log.debug("结束调用接口：" + URI);
		return response;
	}

	/**
	 * 获取指定用户已分配的所有角色
	 * @param request
	 * @param headers
	 * @author 房桂堂
	 * @return
	 */
	@POST
	@Path("/getUserRole")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<RoleInfo> getUserRole(RoleOutRequest request, @Context HttpHeaders headers) {
		BaseResponse<RoleInfo> response = new BaseResponse<>();
		String userId = request.getUserId();
		long systemId = request.getSystemId();
		if (org.apache.commons.lang.StringUtils.isBlank(userId)) {
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("userId为空");
			return response;
		}
		if(request.getSystemId() < -1) {
			systemId = -1;
		}
		// 获取分页信息
		PagingEntity pagingEntity = request.getPaging();
		if (null == pagingEntity) {
			message = "分页的paging不能为空";
			response.setMessage(message);
			log.debug(message);
			response.setResultCode(ResultCode.RESULT_ERROR);
			return response;
		}
		int totalCount = roleService.getTotalCountByUserIdAndSysId(systemId, userId);
		// 根据分页信息和总记录数处理分页信息
		List<Object> otherData = PageUtil.dealPaging(pagingEntity, totalCount);
		int pageNo = pagingEntity.getPageNo();
		int pageSize = pagingEntity.getPageSize();
		int index = (pageNo - 1) * pageSize;
		log.debug("显示的词条页数：" + pageNo + " 每页的大小：" + pageSize + " 起始位置：" + index);

		List<RoleInfo> roleInfoList = roleService.getRoleListByUserIdAndSysId(systemId, userId, index, pageSize);
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		response.setOtherData(otherData);
		response.setMessage("获取角色信息成功");
		response.setData(roleInfoList);
		return response;
	}

	/**
	 * 获取指定用户已分配的所有角色
	 * @param request
	 * @param headers
	 * @author 房桂堂
	 * @return
	 */
	@POST
	@Path("/getUserRoleBySystemSign")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<RoleInfo> getUserRoleBySystemSign(RoleOutRequest request, @Context HttpHeaders headers) {
		BaseResponse<RoleInfo> response = new BaseResponse<>();
		String userId = request.getUserId();
		String systemSign = request.getSystemSign();
		//long systemId = request.getSystemId();
		if (org.apache.commons.lang.StringUtils.isBlank(userId)) {
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("userId为空");
			return response;
		}
		if (org.apache.commons.lang.StringUtils.isBlank(systemSign)) {
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("systemSign为空");
			return response;
		}
		SystemInfo systemInfo = systemInfoService.getSystemInfoBySign(systemSign);
		if(systemInfo == null){
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("该系统不存在");
			return response;
		}
		long systemId = systemInfo.getId();

		// 获取分页信息
		PagingEntity pagingEntity = request.getPaging();
		if (null == pagingEntity) {
			message = "分页的paging不能为空";
			response.setMessage(message);
			log.debug(message);
			response.setResultCode(ResultCode.RESULT_ERROR);
			return response;
		}
		int totalCount = roleService.getTotalCountByUserIdAndSysId(systemId, userId);
		// 根据分页信息和总记录数处理分页信息
		List<Object> otherData = PageUtil.dealPaging(pagingEntity, totalCount);
		int pageNo = pagingEntity.getPageNo();
		int pageSize = pagingEntity.getPageSize();
		int index = (pageNo - 1) * pageSize;
		log.debug("显示的词条页数：" + pageNo + " 每页的大小：" + pageSize + " 起始位置：" + index);

		List<RoleInfo> roleInfoList = roleService.getRoleListByUserIdAndSysId(systemId, userId, index, pageSize);
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		response.setOtherData(otherData);
		response.setMessage("获取角色信息成功");
		response.setData(roleInfoList);
		return response;
	}

	/**
	 * 获取指定系统下的所有角色
	 * @param headers
	 * @param request
	 * @author 房桂堂
	 * @return
	 */
	@POST
	@Path("/getSystemRole")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<RoleInfo> getSystemRole(@Context HttpHeaders headers, RoleRequest request) {// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		URI = "RoleInfo/getSystemRole";
		log.debug("开始调用接口：" + URI);
		BaseResponse<RoleInfo> response = new BaseResponse<RoleInfo>();

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
		long systemId = request.getSystemId();
		if (systemId < -1) {
			systemId = -1;
		}
		int totalCount = 0;
		if (-1 == systemId) {
			totalCount = roleService.getTotalCount(deleteFlag);
		} else {
			totalCount = roleService.getTotalCountBySystemId(systemId,deleteFlag);
		}

		log.debug("未删除的角色信息总条数：" + totalCount);
		// 根据分页信息和总记录数处理分页信息
		List<Object> otherData = PageUtil.dealPaging(pagingEntity, totalCount);
		int pageNo = pagingEntity.getPageNo();
		int pageSize = pagingEntity.getPageSize();
		int index = (pageNo - 1) * pageSize;
		log.debug("显示的词条页数：" + pageNo + " 每页的大小：" + pageSize + " 起始位置：" + index);
		// 获取系统信息列表（角色列表）
		List<RoleInfo> list = null;

		if (-1 == systemId) {
			list = roleService.list(deleteFlag, index, pageSize);
		} else {
			list = roleService.listBySystemId(systemId, deleteFlag, index, pageSize);
		}

		// 返回列表
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		response.setData(list);
		response.setOtherData(otherData);
		log.debug("结束调用接口：" + URI);
		return response;
	}

	/**
	 * 获取指定系统下的所有角色
	 * @param headers
	 * @param request
	 * @author 房桂堂
	 * @return
	 */
	@POST
	@Path("/getSystemRoleBySystemSign")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<RoleInfo> getSystemRoleBySystemSign(@Context HttpHeaders headers, RoleOutRequest request) {// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		URI = "RoleInfo/getSystemRole";
		log.debug("开始调用接口：" + URI);
		BaseResponse<RoleInfo> response = new BaseResponse<RoleInfo>();

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

		String systemSign = request.getSystemSign();
		if (org.apache.commons.lang.StringUtils.isBlank(systemSign)) {
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("systemSign为空");
			return response;
		}
		SystemInfo systemInfo = systemInfoService.getSystemInfoBySign(systemSign);
		if(systemInfo == null){
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("该系统不存在");
			return response;
		}
		long systemId = systemInfo.getId();

		int totalCount = 0;
		totalCount = roleService.getTotalCountBySystemId(systemId,deleteFlag);

		log.debug("未删除的角色信息总条数：" + totalCount);
		// 根据分页信息和总记录数处理分页信息
		List<Object> otherData = PageUtil.dealPaging(pagingEntity, totalCount);
		int pageNo = pagingEntity.getPageNo();
		int pageSize = pagingEntity.getPageSize();
		int index = (pageNo - 1) * pageSize;
		log.debug("显示的词条页数：" + pageNo + " 每页的大小：" + pageSize + " 起始位置：" + index);
		// 获取系统信息列表（角色列表）
		List<RoleInfo> list = null;

		list = roleService.listBySystemId(systemId, deleteFlag, index, pageSize);

		// 返回列表
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		response.setData(list);
		response.setOtherData(otherData);
		log.debug("结束调用接口：" + URI);
		return response;
	}
}
