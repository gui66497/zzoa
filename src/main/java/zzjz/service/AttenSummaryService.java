package zzjz.service;

import org.springframework.stereotype.Service;
import zzjz.bean.AttenSummary;
import zzjz.bean.Calendar;

import java.util.List;

/**
 * @author 彭鹏
 * @ClassName: zoa
 * @Description:
 * @date 2017年04月12日10:29
 */
@Service
public interface AttenSummaryService {

    /**
     * 查询考勤汇总信息列表
     * @param month 月份
     * @return 结果
     */
    List<AttenSummary> initSummaryList(String month);


}
