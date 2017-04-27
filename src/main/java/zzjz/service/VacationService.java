package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Vacation;
import zzjz.bean.VacationRequest;

import java.text.ParseException;

/**
 * @author 彭鹏
 * @ClassName: VacationService
 * @Description: 假期管理
 * @date 2017年04月13日16:32
 */
@Service
public interface VacationService {
    /**
     * 分页获取假期列表信息
     * @param request request
     * @return 假期列表信息
     */
    Page<Vacation> getVacationListPage(VacationRequest request);

    /**
     * 添加并保存请假信息
     * @param vacation
     * @return 结果
     */
    boolean addVacationInfo(Vacation vacation);

    /**
     * 修改请假信息
     * @param vacation
     * @return 结果
     */
    boolean updateVacationInfo(Vacation vacation);

    /**
     * 提交请假信息
     * @param vacation
     * @param msg
     * @return 结果
     */
    boolean submitVacationInfo(Vacation vacation,String msg);

    /**
     * 获取主键
     * @return 主键
     */
    Integer getActSeq();

    /**
     * 根据主键获取请假信息
     * @param id 主键
     */
    Vacation getVacationInfoById(Integer id);

    /**
     * 计算并生成假期明细表
     * @param vacation
     */
    boolean initVacationDetailInfo(Vacation vacation) throws ParseException;
}
