package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Column;
import zzjz.bean.Staff;
import zzjz.bean.StaffRequest;

import java.util.List;

/**
 * @ClassName: StaffService
 * @Description: 员工服务接口
 * @author 房桂堂
 * @date 2017/3/2 15:44
 */
@Service
public interface StaffService {

	/**
	 * 分页获取员工列表信息
	 * @param request request
	 * @return 员工列表信息
     */
	Page<Staff> getStaffListPage(StaffRequest request);

	/**
	 * 新增员工信息
	 * @param staff staff
	 * @return 结果
     */
	boolean addStaff(Staff staff);

	List<Column> getAllColumn();

	boolean updateCol(Column column);

}
