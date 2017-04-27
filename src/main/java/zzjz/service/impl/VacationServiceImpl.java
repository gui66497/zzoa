package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.PagingEntity;
import zzjz.bean.Vacation;
import zzjz.bean.VacationDetail;
import zzjz.bean.VacationRequest;
import zzjz.mapper.VacationDetailMapper;
import zzjz.mapper.VacationMapper;
import zzjz.service.ActivitiService;
import zzjz.service.CalendarService;
import zzjz.service.VacationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 彭鹏
 * @ClassName: VacationServiceImpl
 * @Description:
 * @date 2017年04月13日16:34
 */
@Service
public class VacationServiceImpl implements VacationService{

    @Autowired
    VacationMapper vacationMapper;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    VacationDetailMapper vacationDetailMapper;

    @Autowired
    CalendarService calendarService;

    /**
     * 分页获取假期列表信息
     * @param request request
     * @return 结果
     */
    @Override
    public Page<Vacation> getVacationListPage(VacationRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Vacation> tripPage = (Page<Vacation>) vacationMapper.getVacationList(request);
        long total = tripPage.getTotal();
        return tripPage;
    }

    /**
     * 添加请假信息
     * @param vacation 假期信息
     * @return 结果
     */
    @Override
    public boolean addVacationInfo(Vacation vacation) {
        boolean result;
        String userId = "zhangsan";
        String userName = "张三";
        String actId = activitiService.flowStart("HelloWorldKey",userId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String creditTime = sdf.format(new Date());
        vacation.setUserId(userId);
        vacation.setUserName(userName);
        vacation.setActId(actId);
        vacation.setCreateTime(creditTime);
        //计算请假天数
        //int days = betweenDays();
        result = vacationMapper.insert(vacation)>0;
        //生成假期明细信息
        if(result){
            try {
                result = initVacationDetailInfo(vacation);
            } catch (ParseException e) {
                result = false;
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 修改请假信息
     * @param vacation 假期信息
     * @return 结果
     */
    @Override
    public boolean updateVacationInfo(Vacation vacation) {
        boolean result;
        Vacation tmpVacation = vacationMapper.selectByPrimaryKey(vacation.getId());
        vacation.setCreateTime(tmpVacation.getCreateTime());
        vacation.setUserName(tmpVacation.getUserName());
        vacation.setUserId(tmpVacation.getUserId());
        vacation.setActId(tmpVacation.getActId());
        result = vacationMapper.updateByPrimaryKey(vacation)>0;
        //生成假期明细信息
        if(result){
            try {
                result = initVacationDetailInfo(vacation);
            } catch (ParseException e) {
                result = false;
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 提交请假信息
     * @param vacation 假期信息
     * @return 结果
     */
    @Override
    public boolean submitVacationInfo(Vacation vacation,String msg) {
        String userId= "zhangsan";
        String actId = activitiService.findTaskById(vacation.getActId()).getId();
        return activitiService.submitTask(actId,userId,msg);
    }

    /**
     * 获取主键
     * @return 主键
     */
    @Override
    public Integer getActSeq() {
        return vacationMapper.actSeq();
    }

    /**
     * 根据主键获取请假信息
     * @param id 主键
     * @return
     */
    @Override
    public Vacation getVacationInfoById(Integer id) {
        return vacationMapper.selectByPrimaryKey(id);
    }

    /**
     * 计算并生成假期明细表
     * @param vacation
     */
    @Override
    public boolean initVacationDetailInfo(Vacation vacation) throws ParseException {
        Integer id = vacation.getId();
        //无论有没有数据，删除数据
        vacationDetailMapper.deleteById(id);

        if(vacation.getLeaveDate()==null||vacation.getBackDate()==null){//信息不全
            return true;
        }

        String userId = vacation.getUserId();
        String leaveTyp = vacation.getLeaveTyp();
        //不顺延：丧假，婚假，产假，病假，计算非工作日
        //顺延：调休，年假，事假，只计算工作日
        if(vacation.getLeaveDate().equals(vacation.getBackDate())) {//请假一天
            String leavePart = vacation.getLeavePart();
            VacationDetail vacationDetail = new VacationDetail();
            vacationDetail.setId(id);
            vacationDetail.setUserId(userId);
            vacationDetail.setLeaveTyp(leaveTyp);
            vacationDetail.setLeaveDate(vacation.getLeaveDate());
            vacationDetail.setLeavePart(leavePart);
            vacationDetail.setLeaveDay("0".equals(leavePart)?1.0:0.5);
            vacationDetailMapper.insert(vacationDetail);
        } else {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            calendar.setTime(sdf.parse(vacation.getLeaveDate()));
            //第一天
            VacationDetail vacationDetail = new VacationDetail();
            String leavePart = vacation.getLeavePart()==null?"0":vacation.getLeavePart();
            vacationDetail.setId(id);
            vacationDetail.setUserId(userId);
            vacationDetail.setLeaveTyp(leaveTyp);
            vacationDetail.setLeaveDate(vacation.getLeaveDate());
            vacationDetail.setLeavePart(leavePart);
            vacationDetail.setLeaveDay("0".equals(leavePart)?1.0:0.5);
            vacationDetailMapper.insert(vacationDetail);

            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) +1);
            //中间天
            while(sdf.parse(sdf.format(calendar.getTime())).before(sdf.parse(vacation.getBackDate()))){
                boolean isWorkday = calendarService.isWorkDay(sdf.format(calendar.getTime()));
                if(isWorkday){
                    vacationDetail.setId(id);
                    vacationDetail.setUserId(userId);
                    vacationDetail.setLeaveTyp(leaveTyp);
                    vacationDetail.setLeaveDate(sdf.format(calendar.getTime()));
                    vacationDetail.setLeavePart("0");
                    vacationDetail.setLeaveDay(1.0);
                    vacationDetailMapper.insert(vacationDetail);
                }else{
                    if("03".equals(leaveTyp)||"04".equals(leaveTyp)||"05".equals(leaveTyp)||"06".equals(leaveTyp)){
                        vacationDetail.setId(id);
                        vacationDetail.setUserId(userId);
                        vacationDetail.setLeaveTyp(leaveTyp);
                        vacationDetail.setLeaveDate(sdf.format(calendar.getTime()));
                        vacationDetail.setLeavePart("0");
                        vacationDetail.setLeaveDay(1.0);
                        vacationDetailMapper.insert(vacationDetail);
                    }
                }
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) +1);
            }
            //最后一天
            String backPart = vacation.getBackPart()==null?"0":vacation.getBackPart();
            vacationDetail.setId(id);
            vacationDetail.setUserId(userId);
            vacationDetail.setLeaveTyp(leaveTyp);
            vacationDetail.setLeaveDate(vacation.getBackDate());
            vacationDetail.setLeavePart(backPart);
            vacationDetail.setLeaveDay("0".equals(backPart)?1.0:0.5);
            vacationDetailMapper.insert(vacationDetail);
        }
        return true;
    }
}
