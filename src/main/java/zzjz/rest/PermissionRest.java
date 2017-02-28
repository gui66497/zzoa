package zzjz.rest;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.*;
import zzjz.service.*;
import zzjz.util.ConfigUtil;
import zzjz.util.PageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: PermissionRest
 * @Description: 权限服务接口
 * @author 曹雪东
 * @date 2015年5月9日 上午10:08:59
 */
@Component
@Path("permissionInfo")
public class PermissionRest {

	@Autowired
	private PermissionService permissionService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private SystemInfoService systemInfoService;
	@Autowired
	private UserService userService;
	@Context
	HttpServletRequest servletRequest;
	private Logger LOGGER = Logger.getLogger(getClass());
	private String message = "";
	private String URI = "";
	@POST
	@Path("/list")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<PermissionInfo> queryList(@Context HttpHeaders headers,
												  PermissionRequest request) {
		BaseResponse<PermissionInfo> response = new BaseResponse<PermissionInfo>();
		MultivaluedMap<String, String> headerParams = headers
				.getRequestHeaders();
		String userId = headerParams.getFirst("userId");
		LOGGER.debug("当前用户ID：" + userId);
		// 用户ID非空校验
		if (StringUtils.isEmpty(userId) || "0".equals(userId)) {
			response.setResultCode(ResultCode.RESULT_ERROR);
			return response;
		}
		// 获取分页信息
		// 分页的处理 暂时比考虑分页
		PagingEntity pagingEntity = request.getPaging();
		if (null == pagingEntity) {
			message = "分页的paging不能为空";
			response.setMessage(message);
			LOGGER.debug(message);
			response.setResultCode(ResultCode.RESULT_ERROR);
			return response;
		}
		String deleteFlag = "0";
		long systemId = request.getSystemId();
		// 校验权限
		/*String checkPermissionName = "查看系统权限";
		boolean has = permissionService.checkPermission(userId, systemId + "",
				checkPermissionName);
		if (!has) {
			message = "没有" + checkPermissionName + "的权限";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			LOGGER.debug(message);
			return response;
		}*/
		int totalCount = 0;
		if (-1 == systemId) {
			// 获取总记录数(角色)
			totalCount = permissionService.getTotalCount(deleteFlag);
		} else {
			totalCount = permissionService.getTotalCountBySystemId(systemId,
					deleteFlag);
		}

		LOGGER.debug("未删除的系统信息总条数：" + totalCount);
		// 根据分页信息和总记录数处理分页信息
		List<Object> otherData = PageUtil.dealPaging(pagingEntity, totalCount);
		int pageNo = pagingEntity.getPageNo();
		int pageSize = pagingEntity.getPageSize();
		int index = (pageNo - 1) * pageSize;

		List<PermissionInfo> list = new ArrayList<PermissionInfo>();
	if (-1 == request.getSystemId()) {
			list = permissionService.queryList(index, pageSize);
		} else {

			list = permissionService.queryList(index, pageSize, systemId);
		}
		response.setData(list);
		response.setOtherData(otherData);
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		LOGGER.debug("query permission success " + list);
		LOGGER.debug("请求数据：" + JSONObject.fromObject(response).toString());
		LOGGER.debug("返回数据：" + JSONObject.fromObject(response).toString());
		return response;

	}

	@GET
	@Path("/list/{roleId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<Map<String,String>> queryListByRoleId(
			@PathParam("roleId") long roleId, @Context HttpHeaders headers) {

		BaseResponse<Map<String,String>> response = new BaseResponse<Map<String,String>>();
		MultivaluedMap<String, String> headerParams = headers
				.getRequestHeaders();
		String userId = headerParams.getFirst("userId");
		LOGGER.debug("当前用户ID：" + userId);
		// 用户ID非空校验
		if (StringUtils.isEmpty(userId) || "0".equals(userId)) {
			response.setResultCode(ResultCode.RESULT_ERROR);
			return response;
		}
		// 获取分页信息
		// 分页的处理 暂时比考虑分页
//		PagingEntity pagingEntity = request.getPaging();
//		if (null == pagingEntity) {
//			message = "分页的paging不能为空";
//			response.setMessage(message);
//			LOGGER.debug(message);
//			response.setResultCode(ResultCode.RESULT_ERROR);
//			return response;
//		}
		String deleteFlag = "0";
		long systemId = roleService.getRoleInfoById(roleId + "", 0)
				.getSystemId();
		// 校验权限
		String checkPermissionName = "查看系统权限";
		boolean has = permissionService.checkPermission(userId, systemId + "",
				checkPermissionName);
		if (!has) {
			message = "没有" + checkPermissionName + "的权限";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			LOGGER.debug(message);
			return response;
		}
//		// 获取总记录数(角色)
//		int totalCount = permissionService
//				.getTotalCountByRoleId(roleId, deleteFlag);
//
//		LOGGER.debug("未删除的系统信息总条数：" + totalCount);
		// 根据分页信息和总记录数处理分页信息
//		List<Object> otherData = PageUtil.dealPaging(pagingEntity, totalCount);
//		int pageNo = pagingEntity.getPageNo();
//		int pageSize = pagingEntity.getPageSize();
//		int index = (pageNo - 1) * pageSize;

		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		List<Map<String,String>> list1 = new ArrayList<Map<String,String>>();
		list = permissionService.getPermissionInfoByRoleId(roleId,"0","1");
		list1 = permissionService.getPermissionInfoByRoleId1(roleId,"0","0");
		list.addAll(list1);
		response.setData(list);
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		LOGGER.debug("query permission success " + list);
		LOGGER.debug("请求数据：" + JSONObject.fromObject(response).toString());
		LOGGER.debug("返回数据：" + JSONObject.fromObject(response).toString());
		return response;

	}

	@POST
	@Path("insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<PermissionInfo> insert(PermissionInfo request,
											   @Context HttpHeaders headers) {
		// 获取当前操作用户ID，如果为空，则提示当前用户未登录

		BaseResponse<PermissionInfo> response = new BaseResponse<PermissionInfo>();
		// 获取当前操作用户ID
		MultivaluedMap<String, String> headerParams = headers
				.getRequestHeaders();
		String userId = headerParams.getFirst("userId");
		LOGGER.debug("当前用户ID：" + userId);
		// 用户ID非空校验
		if (StringUtils.isEmpty(userId) || "0".equals(userId)) {
			response.setResultCode(ResultCode.RESULT_ERROR);
			return response;
		}
		// 需要插入的数据非空判断
		String permissionName = request.getName();
		String permissionSign = request.getSign();
		if (StringUtils.isEmpty(permissionName)) {
			response.setResultCode(ResultCode.RESULT_ERROR);
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
			LOGGER.debug(message);
			return response;
		}
		// 需要插入的数据唯一性校验
		PermissionInfo info = permissionService.checkUnique(permissionName,
				permissionSign, systemId + "");
		if (null != info) {
			String message = "权限名称和权限标示已存在";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			LOGGER.debug(message);
			return response;
		}
//		// 校验权限
//		String checkPermissionName = "添加系统权限";
//		boolean has = permissionService.checkPermission(userId, systemId + "",
//				checkPermissionName);
//		if (!has) {
//			message = "没有" + checkPermissionName + "的权限";
//			response.setResultCode(ResultCode.RESULT_ERROR);
//			response.setMessage(message);
//			LOGGER.debug(message);
//			return response;
//		}
		// 数据准备
		request.setCreatorId(userId);
		// 插入数据
		Long key = -1l;
		/*
		 * if(map.isEmpty()){ key = permissionService.insertPermission(request);
		 * }else{ request.setDelete_falg(0);
		 * request.setId(Long.valueOf(map.get("authority_id").toString()));
		 * permissionService.updatePenmission(request); key=request.getId();
		 * 
		 * }
		 */
		key = permissionService.insertPermission(request);
		// 返回插入后的数据
		List<PermissionInfo> list = new ArrayList<PermissionInfo>();

		info = permissionService.queryById(key, 0);
		if (null == info) {
			message = "数据保存失败";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			LOGGER.debug(message);
			return response;
		}

		response.setResultCode(ResultCode.RESULT_SUCCESS);
		list.add(info);
		response.setData(list);
		return response;
	}

	@DELETE
	@Path("/{id}/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<PermissionInfo> delete(@PathParam("id") long id,
											   @Context HttpHeaders headers) {
		// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		BaseResponse<PermissionInfo> response = new BaseResponse<PermissionInfo>();
		// 获取当前操作用户ID
		MultivaluedMap<String, String> headerParams = headers
				.getRequestHeaders();
		String userId = headerParams.getFirst("userId");
		// 用户ID非空校验
		if (StringUtils.isEmpty(userId) || "0".equals(userId)) {
			response.setResultCode(ResultCode.RESULT_ERROR);
			return response;
		}
		List<PermissionInfo> list = new ArrayList<PermissionInfo>();
		// 校验权限
		PermissionInfo info = permissionService.queryById(id, 0);
		if (null != info) {
//			String permissionName = "删除系统权限";
//			boolean has = permissionService.checkPermission(userId,
//					info.getSystemId() + "", permissionName);
//			if (!has) {
//				message = "没有" + permissionName + "的权限";
//				response.setResultCode(ResultCode.RESULT_ERROR);
//				response.setMessage(message);
//				LOGGER.debug(message);
//				return response;
//			}
			// 删除数据
			info = permissionService.queryById(id, 0);
			if (null == info) {
				message = "数据删除失败";
				response.setResultCode(ResultCode.RESULT_ERROR);
				response.setMessage(message);
				LOGGER.debug(message);
				return response;
			}
			Map<String, String> columnValues = permissionService
					.getColumnValues(id);
			String tableName = "tb_authority";
			/*boolean bak = bakService.bak(tableName, columnValues);// 数据备份
			if (!bak) {
				message = "数据删除失败";
				response.setResultCode(ResultCode.RESULT_ERROR);
				response.setMessage(message);
				LOGGER.debug(message);
				return response;
			}*/
			// int temp = permissionService.deletePermission(id, userId);
			// // 返回处理结果
			// if (temp == 0) {
			// response.setResultCode(ResultCode.RESULT_ERROR);
			// return response;
			// }
			permissionService.delete(id);

			list.add(info);
		}

		response.setData(list);
		response.setResultCode(ResultCode.RESULT_SUCCESS);

		return response;
	}

	/**
	 * 获取权限名称
	 * @param headers
	 * @param request
	 * @return
	 */
	@POST
	@Path("/getAuthorityName")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<String> queryList(@Context HttpHeaders headers,
										  RoleRequest request) {// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		URI = "permissionInfo/getAuthorityName";
		LOGGER.debug("开始调用接口：" + URI);
		BaseResponse<String> response = new BaseResponse<String>();
		// 获取当前操作用户ID
		MultivaluedMap<String, String> headerParams = headers
				.getRequestHeaders();
		//String userId = (String) servletRequest.getSession().getAttribute("userId");
		String userId = headerParams.getFirst("userId");
		LOGGER.debug("当前用户ID：" + userId);
		// 用户ID非空校验
		if (org.springframework.util.StringUtils.isEmpty(userId) || "0".equals(userId)) {
			message = "当前用户未登录";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			LOGGER.debug(message);
			return response;
		}
		String deleteFlag = "0";
		long systemId = request.getSystemId();
		HttpSession session = servletRequest.getSession();
		//String currentUserId = (String)session.getAttribute("userId");
		// 获取用户具有查看权限的系统
		ConfigUtil.load("system.properties");
//		String adminUserId="";
		String adminUserId = ConfigUtil.getProperty("tyqxgl.administrator");
		//判断是否为超级管理员，如果是返回isAdmin到message中，否则返回权限名称到data中
		if(adminUserId !=null && userId !=null && adminUserId.equals(userId)) {
			response.setMessage("isAdmin");
		}else{
			List<String> list = permissionService.getAuthorityName(userId,systemId,deleteFlag);
			response.setData(list);
		}
		// 返回列表
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		LOGGER.debug("结束调用接口：" + URI);
		return response;
	}

	@PUT
	@Path("update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<PermissionInfo> update(PermissionInfo request,
											   @Context HttpHeaders headers) {
		// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		BaseResponse<PermissionInfo> response = new BaseResponse<PermissionInfo>();
		// 获取当前操作用户ID
		MultivaluedMap<String, String> headerParams = headers
				.getRequestHeaders();
		String userId = headerParams.getFirst("userId");
		// 用户ID非空校验
		if (StringUtils.isEmpty(userId) || "0".equals(userId)) {
			response.setResultCode(ResultCode.RESULT_ERROR);
			return response;
		}
		// 需要插入的数据非空判断
		String systemName = request.getName();
		String systemSign = request.getSign();
		if (StringUtils.isEmpty(systemName) && StringUtils.isEmpty(systemSign)) {
			response.setResultCode(ResultCode.RESULT_ERROR);
			return response;
		}
		// 判断系统是否存在
		long systemId = request.getSystemId();
		long permissionId = request.getId();
		SystemInfo systemInfo = systemInfoService.getSystemInfoById(systemId
				+ "", 0);
		if (null == systemInfo) {
			message = "系统不存在";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			LOGGER.debug(message);
			return response;
		}
		// 校验权限
//		String checkPermissionName = "更新系统权限";
//		boolean has = permissionService.checkPermission(userId, systemId + "",
//				checkPermissionName);
//		if (!has) {
//			message = "没有" + checkPermissionName + "的权限";
//			response.setResultCode(ResultCode.RESULT_ERROR);
//			response.setMessage(message);
//			LOGGER.debug(message);
//			return response;
//		}
		// 需要插入的数据唯一性校验
		PermissionInfo info = permissionService.checkUnique(systemName,
				systemSign, systemId + "");
		if (null != info && info.getId() != permissionId) {
			message = "权限名称和权限标示已存在";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			LOGGER.debug(message);
			return response;
		}

		// 设置当前的操作用户ID
		request.setCreatorId(userId);

		Long key = -1l;
		key = permissionService.updatePenmission(request);

		if (key == -1) {
			response.setResultCode(ResultCode.RESULT_ERROR);
			return response;
		}

		// 返回更新后的数据
		// 返回插入后的数据
		List<PermissionInfo> list = new ArrayList<PermissionInfo>();

		info = permissionService.queryById(key, 0);
		if (null == info) {
			message = "数据更新失败";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			LOGGER.debug(message);
			return response;
		}

		response.setResultCode(ResultCode.RESULT_SUCCESS);
		list.add(info);
		response.setData(list);
		return response;
	}

	@POST
	@Path("/permissionList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<PermissionInfo> queryObjectList(
			PermissionRequest request) {

		BaseResponse<PermissionInfo> response = new BaseResponse<PermissionInfo>();

		if (request == null) {
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("传入的参数错误");
			return response;
		}
		if (StringUtils.isEmpty(request.getUserId())
				|| StringUtils.isEmpty(request.getTargetId())) {

			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("用户ID或者目标ID不能为空");
			return response;
		}

		List<PermissionInfo> list = null;
		// 当target为-1时，获取该用户在当前系统中的所有权限
		if (request.getTargetId().equalsIgnoreCase("-1")) {
			list = permissionService.getPermissionInfoList(request.getUserId(),
					request.getSystemName(), request.getSystemSign());

		} else {

			list = permissionService.getPermissionInfoListByTarget(
					request.getUserId(), request.getTargetId(),
					request.getSystemName(), request.getSystemSign());
		}

		if (list == null) {
			response.setData(new ArrayList<PermissionInfo>());
		} else {

			response.setData(list);
		}

		response.setResultCode(ResultCode.RESULT_SUCCESS);
		LOGGER.debug("query permissionList success " + list);
		return response;

	}

	@POST
	@Path("/targetList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<Object> queryTargetList(PermissionRequest request) {

		BaseResponse<Object> response = new BaseResponse<Object>();

		if (request == null) {
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("传入的参数错误");
			return response;
		}
		if (StringUtils.isEmpty(request.getUserId())
				|| StringUtils.isEmpty(request.getPermissionName())) {

			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("用户ID或者权限名称不能为空");
			return response;
		}

		List<Object> list = permissionService.getCategoryList(
				request.getUserId(), request.getPermissionName(),
				request.getSystemName(), request.getSystemSign());

		if (list == null) {
			response.setData(new ArrayList<Object>());
		} else {
			response.setData(list);
		}

		response.setResultCode(ResultCode.RESULT_SUCCESS);
		LOGGER.debug("query target success " + list);
		return response;

	}

	@POST
	@Path("/targetSignList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<String> queryTargetSignList(PermissionRequest request) {

		BaseResponse<String> response = new BaseResponse<String>();

		if (request == null) {
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("传入的参数错误");
			return response;
		}
		if (StringUtils.isEmpty(request.getUserId())
				|| StringUtils.isEmpty(request.getPermissionName())) {

			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("用户ID或者权限名称不能为空");
			return response;
		}

		List<String> list = permissionService.getSystemSignList(
				request.getUserId(), request.getPermissionName(),
				request.getSystemName(), request.getSystemSign());

		if (list == null) {
			response.setData(new ArrayList<String>());
		} else {
			response.setData(list);
		}

		response.setResultCode(ResultCode.RESULT_SUCCESS);
		LOGGER.debug("query target success " + list);
		return response;

	}

	/**
	 * 根据userid和系统id获取权限 systemId==-1时获取用户的所有权限
	 * @param request
	 * @param headers
	 * @return
	 */
	@POST
	@Path("getUserPermission")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<Permission> addUser(PermissionOutRequest request, @Context HttpHeaders headers) {
		BaseResponse<Permission> response = new BaseResponse<>();
		long systemId = request.getSystemId();
		String userId = request.getUserId();
		if (StringUtils.isBlank(userId)) {
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("userId为空");
			return response;
		}
		if(request.getSystemId() < -1) {
			systemId = -1;
		}
		List<Permission> permissionList = permissionService.getPermissionsByUserIdAndSysId(systemId, userId);
		message = "权限获取成功!";
		response.setMessage(message);
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		response.setData(permissionList);
		return response;
	}

	/**
	 * 提供给外部添加新的分类（数据源）的时候，对该分类赋予权限
	 * @param request 请求
	 * @return 插入结果
	 */
	@POST
	@Path("addPermission")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<String> add(OutdataRequest request) {
		BaseResponse<String> response = new BaseResponse<>();
		long systemId = request.getSystemId();//系统ID
		// 判断传入的systemId是否为空或者为0
		if(systemId <= 0){
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("系统Id不正确!");
			return response;
		}
		// 根据systemId获取系统信息
		SystemInfo systemInfo = systemInfoService.getSystemInfoById(systemId);
		// 判断系统是否存在
		if(systemInfo == null){
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("系统Id不存在!");
			return response;
		}

		long authorityId = request.getAuthorityId();	//权限ID
		// 判断传入的authorityId是否为空或者为0
		if(authorityId <= 0){
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("权限Id不正确!");
			return response;
		}
		// 判断权限是否存在
		if(!permissionService.hasPermissionInSystem(authorityId, systemId)){
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("权限Id不存在!");
			return response;
		}

		long roleId = request.getRoleId();			 	//角色ID
		// 判断传入的roleId是否为空或者为0
		if(roleId <= 0){
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("角色Id不正确!");
			return response;
		}
		// 判断角色是否存在
		if(!roleService.hasRoleInSystem(roleId, systemId)){
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("角色Id不存在!");
			return response;
		}

		long targetId = request.getTargetId();			//外部数据ID
		String dataSource = request.getDataSource();	//外部数据数据源
		String targetName = request.getTargetName();	//外部数据名称
		long userId = request.getUserId();				//用户ID
		if(userId == 0){
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("用户Id不正确!");
			return response;
		}
		User user = userService.getUserById2(String.valueOf(userId));
		if(user == null){
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("用户Id不存在!");
			return response;
		}
		// 创建PermissionInfo对象
		PermissionInfo permissionInfo = new PermissionInfo();
		permissionInfo.setTargetId(String.valueOf(targetId));
		permissionInfo.setTargetName(targetName);
		permissionInfo.setTargetTableName(dataSource);
		permissionInfo.setId(authorityId);
		permissionInfo.setCreatorId(String.valueOf(userId));
		// 查询是否存在
		boolean success = roleService.hasRoleAuthority(roleId, authorityId, targetId);
		if(success){
			response.setResultCode(ResultCode.RESULT_EXIST);
			response.setMessage("权限已经分配!");
			return response;
		}
		// 插入角色权限关系
		String generatedId = roleService.insertRoleAuthority(roleId, permissionInfo);
		// 查询角色权限关系是否正确
		if(roleService.hasRoleAuthority(Long.valueOf(generatedId))){
			response.setResultCode(ResultCode.RESULT_SUCCESS);
			response.setMessage("操作成功!");
			return response;
		}else {
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage("服务器异常,操作失败!");
			return response;
		}
	}

	/**
	 * 提供给外部添加新的分类（数据源）的时候，对该分类赋予权限
	 * @param request 请求
	 * @return 插入结果
	 */
	@POST
	@Path("outDataPermission")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<String> addPermission(OutdataRequest request) {
		BaseResponse<String> response = new BaseResponse<>();
		String systemSign = request.getSystemSign();
		if (StringUtils.isBlank(systemSign)) {
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("systemSign为空");
			return response;
		}
		// 根据systemSign获取系统信息
		SystemInfo systemInfo = systemInfoService.getSystemInfoBySign(systemSign);
		// 判断系统是否存在
		if(systemInfo == null){
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("该系统不存在");
			return response;
		}
		long systemId = systemInfo.getId();//系统ID

		String authoritySign = request.getAuthoritySign();
		long authorityId = permissionService.getPermissionId(authoritySign, systemId);//权限ID
		// 判断权限是否存在
		if(authorityId == 0){
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("权限不存在!");
			return response;
		}

		long targetId = request.getTargetId();			//外部数据ID
		String targetName = request.getTargetName();	//外部数据名称
		RoleInfo roleInfo = new RoleInfo();
		roleInfo.setName(targetName);					//目标Name作为角色Name
		roleInfo.setSign(String.valueOf(targetId));		//目标ID作为角色Sign
		roleInfo.setSystemId(systemId);
		String roleIdStr = roleService.insert(roleInfo);//创建角色
		long roleId = Long.parseLong(roleIdStr);
		String dataSource = request.getDataSource();	//外部数据数据源
		List<Long> userIdList = request.getUserIdList();
		List<Object> otherDataList = new ArrayList<>();
		for (Long userId:userIdList){
			// 创建PermissionInfo对象
			PermissionInfo permissionInfo = new PermissionInfo();
			permissionInfo.setTargetId(String.valueOf(targetId));
			permissionInfo.setTargetName(targetName);
			permissionInfo.setTargetTableName(dataSource);
			permissionInfo.setId(authorityId);
			permissionInfo.setCreatorId(String.valueOf(userId));
			// 查询是否存在
			boolean success = roleService.hasRoleAuthority(roleId, authorityId, targetId);
			if(success){
				continue;
			}
			// 插入角色权限关系
			String generatedId = roleService.insertRoleAuthority(roleId, permissionInfo);
			// 查询角色权限关系是否正确
			if(!roleService.hasRoleAuthority(Long.valueOf(generatedId))) {
				otherDataList.add(userId+"赋予权限不正确");
			}
		}

		response.setResultCode(ResultCode.RESULT_SUCCESS);
		response.setMessage("操作成功!");
		response.setOtherData(otherDataList);
		return response;
	}

}
