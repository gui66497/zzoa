package zzjz.rest;

import com.github.pagehelper.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.*;
import zzjz.service.StaffService;
import zzjz.util.PageUtil;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: StaffRest
 * @Description: 员工rest
 * @author 房桂堂
 * @date 2017/3/2 15:34
 */
@Component
@Path("staff")
public class StaffRest {

	private static final Logger LOGGER = LoggerFactory.getLogger(StaffRest.class);

	private String URI = "";
	private String message = "";

	@Autowired
	StaffService staffService;

	String[] pattern = new String[]{"yyyy-MM","yyyyMM","yyyy/MM",
			"yyyyMMdd","yyyy-MM-dd","yyyy/MM/dd",
			"yyyyMMddHHmmss",
			"yyyy-MM-dd HH:mm:ss",
			"yyyy/MM/dd HH:mm:ss"};

	/**
	 * 分页获取员工信息
	 * @param requst
	 * @return
     */
	@Path("/page")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<Staff> getStaffPage(StaffRequest requst) {
		BaseResponse<Staff> response = new BaseResponse<>();
		Page<Staff> staffList = staffService.getStaffListPage(requst);
		List<Object> otherData = PageUtil.dealPaging(requst.getPaging(), (int) staffList.getTotal());
		response.setData(staffList);
		response.setOtherData(otherData);
		response.setResultCode(ResultCode.RESULT_SUCCESS);
		return response;
	}

	/**
	 * 新增员工信息
	 * @param staff 员工信息
	 * @param headers headers
	 * @author 房桂堂
	 * @return
	 */
	@POST
	@Path("add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse<String> addUser(Staff staff, @Context HttpHeaders headers) throws ParseException {
		// 获取当前操作用户ID，如果为空，则提示当前用户未登录
		URI = "staff/add";
		LOGGER.debug("开始调用接口：" + URI);
		BaseResponse<String> response = new BaseResponse<String>();
		String name = staff.getName();
		Date nDate = DateUtils.parseDate(staff.getEntryDate(), pattern);
		if (StringUtils.isBlank(name)) {
			message = "姓名不能为空";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			LOGGER.debug(message);
			return response;
		}
		// 需要插入的数据唯一性校验
		/*User userTemp = userService.getUserByName2(userName);
		if (null != userTemp ) {
			message = "用户名已存在";
			response.setResultCode(ResultCode.RESULT_ERROR);
			response.setMessage(message);
			LOGGER.debug(message);
			return response;
		}*/
		staff.setStaffId(new Date().getTime());
		boolean res = staffService.addStaff(staff);
		if (res) {
			message = "员工信息添加成功";
			response.setResultCode(ResultCode.RESULT_SUCCESS);
		} else {
			message = "员工信息添加失败";
			response.setResultCode(ResultCode.RESULT_ERROR);
			return response;
		}
		response.setMessage(message);
		return  response;
	}

}
