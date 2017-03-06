package zzjz.rest;

import com.github.pagehelper.Page;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zzjz.bean.*;
import zzjz.service.PermissionService;
import zzjz.service.UserService;
import zzjz.util.ConfigUtil;
import zzjz.util.PageUtil;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName: UserRest
 * @Description: 用戶Rest
 * @author 房桂堂
 * @date 2017年2月15日 上午10:20:42
 */
@Component
@Path("user")
public class UserRest {

	@Autowired
	private UserService userService;

	@Context
	HttpServletRequest servletRequest;

	@Autowired
	private PermissionService permissionService;

	private Logger LOGGER = Logger.getLogger(UserRest.class);// 日志

	private String message = "";

	private String URI = "";

	@Value("${tyqxgl.administrator}")
	private String adminStr;

	@GET
	@Path("{userId}/get")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserByUserId(@PathParam("userId") String userId) {
		return userService.getUserById2(userId);
	}

	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<User> getUserList() {
		List<User> users = userService.getUserList2();
		BaseResponse<User> response = new BaseResponse<>();
		response.setData(users);
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		return response;
	}

	/**
	 * 分页获取用户
	 * @param userRequest
	 * @return
     */
	@POST
	@Path("/page")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<User> getUserListPage(UserRequest userRequest) {
		PagingEntity pagingEntity = userRequest.getPaging();
		String userName = userRequest.getUserName();
		Page<User> users = new Page<>();
		if (StringUtils.isNotBlank(userName)) {
			users = userService.getUserListPageWithName(userName, pagingEntity);
		} else {
			users = userService.getUserListPage(pagingEntity);
		}

		List<Object> otherData = PageUtil.dealPaging(pagingEntity, (int) users.getTotal());
		BaseResponse<User> response = new BaseResponse<>();
		response.setData(users);
		response.setOtherData(otherData);
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		return response;
	}

	/**
	 * 根据userId删除用户
	 * @param userId
	 * @param headers
	 * @author 房桂堂
	 * @return
	 */
	@DELETE
	@Path("{userId}/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<String> delete(@PathParam("userId") long userId, @Context HttpHeaders headers) {
		BaseResponse<String> response = new BaseResponse<String>();
		if (userId < 0) {
			message = "参数错误";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			return response;
		}
		//UserEntity userEntity = userService.getUserById(String.valueOf(userId));
		User userEntity = userService.getUserById2(String.valueOf(userId));
		if (userEntity == null) {
			message = "没有查询到此用户，请核实！";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			return response;
		}
		boolean res = userService.deleteUser(userId);
		if (res) {
			message = "删除成功";
			response.setResultCode(ResultCode.RESULT_SUCCESS);
			response.setMessage(message);
		}
		return response;
	}

	/**
	 * 新增员工信息
	 * @param request
	 * @param headers
	 * @author 房桂堂
	 * @return
	 */
	@POST
	@Path("add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<SystemInfo> addStaff(User request, @Context HttpHeaders headers) {
		// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		URI = "user/add";
		LOGGER.debug("开始调用接口：" + URI);
		BaseResponse<SystemInfo> response = new BaseResponse<SystemInfo>();
		// 获取当前操作用户ID
		MultivaluedMap<String, String> headerParams = headers
				.getRequestHeaders();
		String userId = headerParams.getFirst("userId");
		LOGGER.debug("当前用户ID：" + userId);

		String userName = request.getUserName();
		String password = request.getPassword();
		if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
			message = "用户名密码不能为空";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			LOGGER.debug(message);
			return response;
		};
		// 需要插入的数据唯一性校验
		User userTemp = userService.getUserByName2(userName);
		if (null != userTemp ) {
			message = "用户名已存在";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			LOGGER.debug(message);
			return response;
		}

		User user = new User();
		user.setUserName(request.getUserName());
		user.setPassword(request.getPassword());
		user.setUserId(String.valueOf(new Date().getTime()));
		boolean res = userService.addUser2(user);
		if (res) {
			response.setResultCode(ResultCode.RESULT_SUCCESS);
		} else {
			message = "用户添加失败";
			response.setResultCode(ResultCode.RESULT_ERROR);
			return response;
		}
		return  response;
	}

	/**
	 * 修改用户信息
	 * @param request
	 * @param headers
	 * @author 房桂堂
	 * @return
	 */
	@PUT
	@Path("update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<SystemInfo> update(User request, @Context HttpHeaders headers) {
		// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		URI = "user/update";
		LOGGER.debug("开始调用接口：" + URI);
		BaseResponse<SystemInfo> response = new BaseResponse<SystemInfo>();
		// 获取当前操作用户ID
		MultivaluedMap<String, String> headerParams = headers
				.getRequestHeaders();
		String uId = headerParams.getFirst("userId");
		LOGGER.debug("当前用户ID：" + uId);

		String userName = request.getUserName();
		String userId = request.getUserId();
		if (StringUtils.isBlank(userName)) {
			message = "用户名不能为空";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			LOGGER.debug(message);
			return response;
		};
		User userTemp = userService.getUserById2(userId);
		if (null == userTemp) {
			message = "该用户不存在";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			LOGGER.debug(message);
			return response;
		}
		// 需要插入的数据唯一性校验
		userTemp = userService.getUserByName2(userName);
		if (null != userTemp && userTemp.getUserId() != userId) {
			message = "用户名已存在";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			LOGGER.debug(message);
			return response;
		}

		boolean res = userService.update(request);
		if (res) {
			response.setResultCode(ResultCode.RESULT_SUCCESS);
		} else {
			message = "用户修改失败";
			response.setResultCode(ResultCode.RESULT_ERROR);
			return response;
		}

		return  response;
	}


	public static void main(String[] args) throws IOException {
		TemplateExportParams params = new TemplateExportParams("docs/专项支出用款申请书.xls");
		Map<String, Object> map = new HashMap<>();
		map.put("date", "2017.2.20");
		map.put("person", "小龙");
		map.put("phone", "12345");
		map.put("company", "直真");
		Workbook book = ExcelExportUtil.exportExcel(params, map);
		File file = new File("D:/excel/");
		if (!file.exists()) {
			file.mkdirs();
		}
		FileOutputStream fos = new FileOutputStream("D:/excel/ttt.xls");
		book.write(fos);
		fos.close();
	}





	@GET
	@Path("/hasLogin")
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<String> haiLogin() {
		LOGGER.debug("判断用户是否登陆");
		BaseResponse<String> response = new BaseResponse<String>();
		String userId = (String) servletRequest.getSession().getAttribute("userId");
		if(StringUtils.isNotBlank(userId) ) {
			response.setResultCode(ResultCode.RESULT_SUCCESS);
			LOGGER.debug("用户已登陆:" + userId);
		} else {
			response.setResultCode(ResultCode.RESULT_NOT_AUTHORIZED);
			LOGGER.debug("用户未登陆！");
		}
		return response;
	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<String> login(UserRequest userRequest) {
		LOGGER.debug("用户尝试登陆，用户名:" + userRequest.getUserName() + ",密码:" + userRequest.getPassword());
		String userName = userRequest.getUserName();
		String password = userRequest.getPassword();
		BaseResponse<String> response = new BaseResponse<String>();
		if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
			response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
			response.setMessage("用户名或密码为空！");
			return response;
		}
		boolean loginResult = userService.login(userName, password);
		LOGGER.debug("用户登陆结果:" + (loginResult == true ? "成功！" : "失败！"));

		if(loginResult) {
			User user = userService.getUserByName(userName);
			//设置session
			servletRequest.getSession().setAttribute("userName", user.getUserName());
			servletRequest.getSession().setAttribute("userId", user.getUserId());
			//获取菜单权限列表
			List<String> AuthorityList = permissionService.getAuthorityName(user.getUserId(),0L,"0");
			response.setData(AuthorityList);
			response.setResultCode(loginResult == true ? ResultCode.RESULT_SUCCESS : ResultCode.RESULT_ERROR);
			response.setMessage(loginResult == true ? "登陆成功！" : "登陆失败！");
			List<Object> otherData = new ArrayList<>();
			otherData.add(Long.valueOf(user.getUserId()));
			response.setOtherData(otherData);
			return response;
		}

		response.setMessage("登陆失败！");
		return response;
	}

	/**
	 * 根据userid获取其可操作菜单
	 * @return
	 */
	@GET
	@Path("/getMenus")
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<String> getMenus() {
		BaseResponse<String> response = new BaseResponse<String>();
		String userId = (String) servletRequest.getSession().getAttribute("userId");
		if (StringUtils.isBlank(userId)) {
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage("userId为空！");
			return response;
		}
		// 获取用户具有查看权限的系统
		ConfigUtil.load("system.properties");
		String adminUserId = ConfigUtil.getProperty("tyqxgl.administrator");
		//判断是否为超级管理员，如果是返回isAdmin到message中，否则返回权限名称到data中
		if(adminUserId.equals(userId)) {
			response.setMessage("isAdmin");
		}else{
			//systemId为0时查询的就是没有关联的所有权限
			List<String> authorityList = permissionService.getAuthorityName(userId,0L,"0");
			response.setData(authorityList);
		}
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		return response;
	}


	/**
	 * 给用户分配角色
	 * @param userEntity
	 * @author 房桂堂
	 * @return
	 */
	@POST
	@Path("/allocateRole")
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<String> allocateRole(User userEntity, @Context HttpHeaders headers) {
		MultivaluedMap<String, String> headerParams = headers
				.getRequestHeaders();
		String cUserId = headerParams.getFirst("userId");
		BaseResponse<String> response = new BaseResponse<String>();
		if (StringUtils.isEmpty(userEntity.getUserId())) {
			message = "参数错误";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			LOGGER.debug(message);
			return response;
		}
		boolean res = userService.allocateRole(userEntity.getSystemId(), userEntity.getUserId(), userEntity.getRoleId(), cUserId);
		if (res) {
			response.setResultCode(ResultCode.RESULT_SUCCESS);
		} else {
			message = "分配角色失败";
			response.setResultCode(ResultCode.RESULT_ERROR);
			return response;
		}
		return response;
	}

}
