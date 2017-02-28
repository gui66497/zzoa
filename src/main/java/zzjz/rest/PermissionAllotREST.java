package zzjz.rest;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zzjz.bean.*;
import zzjz.service.PermissionAllotService;
import zzjz.service.PermissionService;
import zzjz.service.UserService;
import zzjz.util.PageUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("permissionAllot")
public class PermissionAllotREST {

	private Logger log = Logger.getLogger(PermissionAllotREST.class);
	private String message = "";
	private String URI = "";

	@Autowired
	private PermissionAllotService permissionAllotService;
	@Autowired
	private UserService userService;
	@Autowired
	private PermissionService permissionService;

	@POST
	@Path("list")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<PermissionAllotInfo> list(
			PermissionAllotRequest request, @Context HttpHeaders headers) {
		// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		URI = "PermissionAllotInfo/list";
		log.debug("开始调用接口：" + URI);
		BaseResponse<PermissionAllotInfo> response = new BaseResponse<PermissionAllotInfo>();
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
		// 校验权限
		String checkPermissionName = "查看用户角色";
		boolean has = permissionService.checkPermission(userId, systemId + "",
				checkPermissionName);
		if (!has) {
			message = "没有" + checkPermissionName + "的权限";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}
		int totalCount = 0;
		if (-1 == systemId) {
			// 获取总记录数(角色)
			totalCount = permissionAllotService.getTotalCount(deleteFlag);
		} else {
			totalCount = permissionAllotService.getTotalCountBySystemId(
					systemId, deleteFlag);
		}

		log.debug("未删除的系统信息总条数：" + totalCount);
		// 根据分页信息和总记录数处理分页信息
		List<Object> otherData = PageUtil.dealPaging(pagingEntity, totalCount);
		int pageNo = pagingEntity.getPageNo();
		int pageSize = pagingEntity.getPageSize();
		int index = (pageNo - 1) * pageSize;
		log.debug("显示的词条页数：" + pageNo + " 每页的大小：" + pageSize + " 起始位置：" + index);
		// 获取系统信息列表（角色列表）
		List<PermissionAllotInfo> list = null;
		if (-1 == systemId) {
			list = permissionAllotService.list(deleteFlag, index, pageSize);
		} else {
			list = permissionAllotService.listBySystemId(systemId, deleteFlag,
					index, pageSize);
		}

		// 根据角色ID获取角色对用的用户信息
		for (PermissionAllotInfo info : list) {
			long roleId = info.getId();
			List<User> userList = permissionAllotService
					.getUserByRoleId(roleId);
			info.setUserList(userList);
		}
		// 返回列表
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		response.setData(list);
		response.setOtherData(otherData);
		log.debug("结束调用接口：" + URI);
		log.debug("请求数据：" + JSONObject.fromObject(response).toString());
		log.debug("返回数据：" + JSONObject.fromObject(response).toString());
		return response;
	}

	@POST
	@Path("insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<PermissionAllotInfo> insert(
			PermissionAllotInfo request, @Context HttpHeaders headers) {
		// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		URI = "PermissionAllotInfo/insert";
		log.debug("开始调用接口：" + URI);
		BaseResponse<PermissionAllotInfo> response = new BaseResponse<PermissionAllotInfo>();
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
		List<User> userList = request.getUserList();

		if (null == userList || userList.size() == 0) {
			message = "角色用户不能为空";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}
		// 插入数据
		// TODO 判断角色是否存在，判断角色对应的系统是否存在，判断用户是否存在
		long roleId = request.getId();
		PermissionAllotInfo info = permissionAllotService
				.getPermissionAllotInfoById(roleId + "", 0);
		if (null == info) {
			message = "角色不存在";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}
		for (User userEntity : userList) {
			// 判断用户是否存在
			String relationUserId = userEntity.getUserId();
			if (userService.getUserInfoById(relationUserId).size() == 0) {
				log.debug("用户" + relationUserId + "不存在");
				continue;
			}
			permissionAllotService.insert(userEntity.getUserId(), roleId,
					userId);
		}

		// 返回插入后的数据
		userList = permissionAllotService.getUserByRoleId(roleId);
		info.setUserList(userList);
		List<PermissionAllotInfo> list = new ArrayList<PermissionAllotInfo>();
		list.add(info);
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		response.setData(list);
		log.debug("结束调用接口：" + URI);
		return response;
	}

	@POST
	@Path("/{userRoleId}/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<PermissionAllotInfo> delete(
			@PathParam("userRoleId") long userRoleId,
			@Context HttpHeaders headers) {
		// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		URI = "PermissionAllotInfo/delete";
		log.debug("开始调用接口：" + URI);
		BaseResponse<PermissionAllotInfo> response = new BaseResponse<PermissionAllotInfo>();
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
		String deleteFlag = "1";
		permissionAllotService.delete(userRoleId, deleteFlag);
		// 返回处理结果
		PermissionAllotInfo info = permissionAllotService
				.getPermissionAllotInfoById(userRoleId + "", 1);
		if (null == info) {
			message = "数据保存失败";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}
		List<PermissionAllotInfo> list = new ArrayList<PermissionAllotInfo>();
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
	public BaseResponse<PermissionAllotInfo> update(
			PermissionAllotInfo request, @Context HttpHeaders headers) {
		// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		URI = "PermissionAllotInfo/update";
		log.debug("开始调用接口：" + URI);
		BaseResponse<PermissionAllotInfo> response = new BaseResponse<PermissionAllotInfo>();
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
		List<User> userList = request.getUserList();
		// TODO 判断角色是否存在，判断角色对应的系统是否存在，判断用户是否存在
		long roleId = request.getId();
		PermissionAllotInfo info = permissionAllotService
				.getPermissionAllotInfoById(roleId + "", 0);
		if (null == info || info.getDeleteFlag() != 0) {
			message = "角色不存在";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}
		// 校验权限
		String checkPermissionName = "更新用户角色";
		boolean has = permissionService.checkPermission(userId,
				info.getSystemId() + "", checkPermissionName);
		if (!has) {
			message = "没有" + checkPermissionName + "的权限";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			log.debug(message);
			return response;
		}
		permissionAllotService.delete(roleId);
		if (userList != null) {
			for (User userEntity : userList) {
				// 判断用户是否存在
				String relationUserId = userEntity.getUserId();
				if (userService.getUserInfoById(relationUserId).size() == 0) {
					log.debug("用户" + relationUserId + "不存在");
					continue;
				}
				permissionAllotService.insert(userEntity.getUserId(), roleId,
						userId);
			}
		}

		// 返回插入后的数据
		userList = permissionAllotService.getUserByRoleId(roleId);
		info.setUserList(userList);
		List<PermissionAllotInfo> list = new ArrayList<PermissionAllotInfo>();
		list.add(info);
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		response.setData(list);
		log.debug("结束调用接口：" + URI);
		return response;
	}

}
