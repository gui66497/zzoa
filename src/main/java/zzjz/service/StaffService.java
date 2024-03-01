package zzjz.service;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzjz.bean.Bean;
import zzjz.bean.Column;
import zzjz.bean.DepartRole;
import zzjz.bean.Staff;
import zzjz.bean.StaffExcel;
import zzjz.bean.StaffRequest;
import zzjz.bean.Turnover;
import java.util.List;
import java.util.Map;

/**
 * @author 房桂堂
 * @version 2017/3/2 15:44
 * @ClassName: StaffService
 * @Description: 员工服务接口
 */
@Service
public interface StaffService {

    /**
     * 分页获取员工列表信息.
     *
     * @param request request
     * @return 员工列表信息
     */
    Page<Staff> getStaffListPage(StaffRequest request);

    /**
     * 获取所有在职员工信息（只包含简单信息）.
     *
     * @return List
     */
    List<Staff> getAllSimpleStaffOnJob();

    /**
     * 获取所有员工信息(包括离职的)（只包含简单信息）.
     *
     * @return List
     */
    List<Staff> getAllSimpleStaff();

    /**
     * 获取员工列表信息.
     *
     * @param request request
     * @return 员工列表信息
     */
    List<StaffExcel> getStaffList(StaffRequest request);

    /**
     * 获取员工列表信息(Map形式).
     *
     * @param request request
     * @return 员工列表信息
     */
    List<Map<String, Object>> getStaffMap(StaffRequest request);

    /**
     * 获取总经办列表信息.
     *
     * @return 总经办人员信息列表
     */
    List<Staff> getTopStaffList();

    /**
     * 新增员工信息.
     *
     * @param staff staff
     * @return 结果
     */
    boolean addStaff(Staff staff);

    /**
     * 获取所有展示列.
     *
     * @return List
     */
    List<Column> getAllColumn();

    /**
     * 更新展示列.
     *
     * @param column Column
     * @return boolean
     */
    boolean updateCol(Column column);

    /**
     * 根据staffId删除员工信息.
     *
     * @param staffId staffId
     * @return 结果
     */
    boolean delByStaffId(long staffId);

    /**
     * 批量新增用户信息.
     *
     * @param res 用户信息
     * @return 结果
     */
    boolean addStaffList(List<StaffExcel> res);

    /**
     * 批量新增用户信息(删除原来已有的员工数据).
     *
     * @param res 用户信息
     * @return 结果
     */
    boolean uploadStaffs(List<StaffExcel> res);

    /**
     * 获取到期提醒列表.
     *
     * @return 员工信息
     */
    List<Staff> getformalRemindList();

    /**
     * 获取到期提醒条数.
     *
     * @return 条数
     */
    Integer getformalRemindCount();

    /**
     * 获取生日提醒列表.
     *
     * @return 员工信息
     */
    List<Staff> getbirthRemindList();

    /**
     * 获取生日提醒条数.
     *
     * @return 条数
     */
    Integer getbirthRemindCount();

    /**
     * 获取合同到期提醒列表.
     *
     * @return 员工信息
     */
    List<Staff> getcontRemindList();

    /**
     * 获取合同到期提醒条数.
     *
     * @return 条数
     */
    Integer getcontRemindCount();

    /**
     * 获取资格证书到期提醒列表.
     *
     * @return 员工信息
     */
    List<Staff> getcertificateRemindList();

    /**
     * 获取资格证书到期提醒条数.
     *
     * @return 条数
     */
    Integer getcertificateRemindCount();

    /**
     * 登陆.
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    boolean login(String userName, String password);

    /**
     * 根据用户名获取员工信息.
     *
     * @param userName 用户名
     * @param onlyOnJob 只查在职的
     * @return 员工信息
     */
    Staff getStaffByUserName(String userName, boolean onlyOnJob);

    /**
     * 根据公司邮箱获取员工信息（历史原因 公司邮箱用的是email字段而不是cEmail）.
     * 注：该接口只返回在职员工
     * @param mail 邮箱
     * @return 员工信息
     */
    Staff getStaffByMail(String mail);

    /**
     * 根据中文名获取员工信息.
     *
     * @param name 中文名
     * @return 员工信息
     */
    Staff getStaffByName(String name);

    /**
     * 根据用户号获取员工信息.
     *
     * @param userId 用户id
     * @return 用户id
     */
    Staff getStaffByUserId(String userId);

    /**
     * 根据用户号获取员工信息(包括离职,兼职,挂职的).
     *
     * @param userId 用户id
     * @return 用户id
     */
    Staff getStaffByUserIdAll(String userId);

    /**
     * 根据用户号获取员工信息(包括默认部门信息).
     *
     * @param userId 用户id
     * @return 员工信息
     */
    Staff getStaffWithDefDepartByUserId(String userId);

    /**
     * 根据用户号获取员工信息（包括部门角色信息）.
     *
     * @param userId 用户id
     * @return 用户id
     */
    Staff getStaffWithRoleByUserId(String userId);

    /**
     * 同步staff信息到activiti.
     *
     * @return 结果
     */
    boolean synStaffToAct();

    /**
     * 修改员工信息.
     *
     * @param staff 员工信息
     * @return 结果
     */
    boolean editStaff(Staff staff);

    /**
     * 根据身份证信息自动填充相应信息.
     *
     * @param staff 员工信息
     * @return 员工信息
     */
    Staff completeStaffByIdNum(Staff staff);

    /**
     * 根据身份证信息自动填充相应信息.
     *
     * @param staff 员工信息
     * @return 员工信息
     */
    StaffExcel completeStaffByIdNum(StaffExcel staff);

    /**
     * 设置用户角色关系.
     *
     * @param departRoles departRoles
     * @param staffId     staffId
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    boolean setDepartRole(long staffId, List<DepartRole> departRoles);

    /**
     * 根据角色id和部门id获取匹配的员工.
     *
     * @param roleId   角色id
     * @param departId 部门id
     * @return 员工信息
     */
    List<Staff> getStaffByDepartAndRole(String roleId, long departId);

    /**
     * 根据员工id和部门id获取员工角色.
     *
     * @param staffId  员工id
     * @param departId 部门id
     * @return 员工角色
     */
    String getStaffByDepartAndUser(String staffId, long departId);

    /**
     * 获取所有双软人员属性.
     *
     * @return List
     */
    List<Bean> getAllPostList();

    /**
     * 通过staffId获取email.
     *
     * @param staffId 员工id
     * @return email
     */
    String getEmailByStaffId(long staffId);

    /**
     * 根据部门id获取所有员工信息(包括角色信息).
     *
     * @param departId 部门ID
     * @return 列表
     */
    List<Staff> getStaffByDepart(long departId);

    /**
     * 批量删除用户信息.
     *
     * @param staffIds staffIds
     * @return boolean
     */
    boolean delByStaffIds(String[] staffIds);

    /**
     * 获取所有员工信息（包括部门角色）.
     *
     * @param staffRequest 请求信息
     * @return 列表
     */
    List<Staff> getWithRole(StaffRequest staffRequest);

    /**
     * 获取保密办主任.
     *
     * @return 列表
     */
    List<Staff> getDirectorList();

    /**
     * 根据异动信息同步修改员工信息.
     * @param turnover 异动信息
     * @return 结果
     * @throws Exception Exception
     */
    boolean changeStaffByTurnover(Turnover turnover) throws Exception;

    /**
     * 根据数组构造导出实体.
     * @param exportArr 导出列数组
     * @return 结果
     */
    List<ExcelExportEntity> getExcelExportEntityByArr(String[] exportArr);

    /**
     * 时间格式化.
     * @param staff staff
     * @return 结果
     */
    StaffExcel timeFormate(StaffExcel staff);

    /**
     * 通过名字获取签名文件的路径.
     * @param name 名字 (中文)
     * @return 路径
     */
    String getSignPath(String name);
}
