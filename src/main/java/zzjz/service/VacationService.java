package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Staff;
import zzjz.bean.Vacation;
import zzjz.bean.VacationDetail;
import zzjz.bean.VacationRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年04月13日16:32
 * @ClassName: VacationService
 * @Description: 假期管理服务接口
 */
@Service
public interface VacationService {
    /**
     * 分页获取假期列表信息.
     *
     * @param request request
     * @return 假期列表信息
     */
    Page<Vacation> getVacationListPage(VacationRequest request);

    /**
     * 获取假期列表.
     * @param vacation 假期
     * @return 结果
     */
    List<Vacation> getVacationList(VacationRequest vacation);

    /**
     * 添加并保存请假信息.
     *
     * @param vacation 假期信息
     * @return 流程号
     */
    String addVacationInfo(Vacation vacation);

    /**
     * 修改请假信息.
     *
     * @param vacation 假期信息
     * @return 结果
     */
    boolean updateVacationInfo(Vacation vacation);

    /**
     * 销假.
     *
     * @param vacation 假期信息
     * @return 结果
     * @throws ParseException ParseException
     */
    boolean forVacationInfo(Vacation vacation) throws ParseException;

    /**
     * 提交请假信息.
     *
     * @param vacation 请假信息
     * @param user     处理人
     * @param staffId  当前用户
     * @return 结果
     */
    boolean submitVacationInfo(Vacation vacation, String user, String staffId);

    /**
     * 审批流程.
     *
     * @param vacation 假期信息
     * @param user     处理人
     * @param msg      操作
     * @param reason   原因
     * @return boolean
     */
    boolean approveVacationTask(Vacation vacation, String user, String msg, String reason);

    /**
     * 根据主键获取请假信息.
     *
     * @param id 主键
     * @return Vacation
     */
    Vacation getVacationInfoById(Integer id);

    /**
     * 获取下一处理人列表.
     *
     * @param vacation Vacation
     * @param departId departId
     * @param msg      msg
     * @return List
     */
    List<Staff> getNextUser(Vacation vacation, long departId, String msg);

    /**
     * 计算并生成假期明细表.
     *
     * @param vacation 假期信息
     * @return boolean
     * @throws ParseException ParseException
     */
    boolean initVacationDetailInfo(Vacation vacation) throws ParseException;

    /**
     * 计算请假天数.
     *
     * @param vacation 假期信息
     * @return 结果
     */
    Double betweenDays(Vacation vacation);

    /**
     * 根据假期明细请求信息查询假期明细信息.
     *
     * @param vacation VacationDetail
     * @return List
     */
    List<VacationDetail> selectVacationInfo(VacationDetail vacation);

    /**
     * 初始化计算假期信息.
     *
     * @param vacation Vacation
     * @param staffId  staffId
     * @return Vacation
     * @throws ParseException ParseException
     */
    Vacation initVacationInfo(Vacation vacation, String staffId) throws ParseException;

    /**
     * 删除假期信息.
     *
     * @param actId actId
     * @return boolean
     * @throws ParseException ParseException
     */
    boolean deleteRecordByActId(String actId) throws ParseException;

    /**
     * 撤销假期信息.
     *
     * @param actId actId
     * @return boolean
     */
    boolean revokeRecordByActId(String actId);

    /**
     * 审批并结束假期信息.
     *
     * @param vacation Vacation
     * @return boolean
     */
    boolean terminationVacationTask(Vacation vacation);

    /**
     * 获取剩余调休天数.
     *
     * @param staffId staffId
     * @return Double
     */
    Double getLeftOffDays(String staffId);

    /**
     * 获取剩余年假天数.
     *
     * @param staffId staffId
     * @return Double
     */
    Double getLeftAnnualDays(String staffId);

    /**
     * 请假信息导出内容.
     * @param vacation 请假信息
     * @return 结果
     */
    Map<String, Object> getVacationMapList(Vacation vacation);

    /**
     * 忽略请假提醒.
     *
     * @param id 主键
     * @return 结果
     */
    boolean ignore(Integer id);

    String dateToMonth(String date);

    String dateToMonthOld(String date);
}
