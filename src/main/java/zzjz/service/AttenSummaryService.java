package zzjz.service;

import org.springframework.stereotype.Service;
import zzjz.bean.AttenSingleSummary;
import zzjz.bean.AttenSummary;
import zzjz.bean.NumbersSummary;
import zzjz.bean.SummaryYear;
import zzjz.bean.AttenSummaryDetails;
import zzjz.bean.NumbersSummaryRequest;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年04月12日10:29
 * @ClassName: AttenSummaryService
 * @Description: 考勤汇总服务接口
 */
@Service
public interface AttenSummaryService {

    /**
     * 查询考勤汇总信息列表.
     *
     * @param date      月份
     * @param departName departName
     * @return 结果集
     */
    List<AttenSummary> initSummaryList(String date, String departName);

    /**
     * 获取单项考勤统计.
     *
     * @param map 参数集
     * @return 结果集
     */
    List<AttenSingleSummary> getSummerySingleList(Map map);

    /**
     * 根据日期区间查询汇总信息.
     *
     * @param departName departName
     * @param beginDate  beginDate
     * @param endDate    endDate
     * @param total      total
     * @return List
     */
    List<SummaryYear> getSummaryYearList(String departName, String beginDate, String endDate, String total);

    /**
     * 查询人数统计信息.
     *
     * @param request NumbersSummaryRequest
     * @return List
     * @throws ParseException ParseException
     */
    List<NumbersSummary> getNumbersSummary(NumbersSummaryRequest request) throws ParseException;

    /**
     * 查询汇总详情.
     *
     * @param userId    userId
     * @param beginDate beginDate
     * @param endDate   endDate
     * @return List
     */
    List<AttenSummaryDetails> getSummaryDetails(String userId, String beginDate, String endDate);

    /**
     * 查询加班汇总详情.
     *
     * @param userId userId
     * @param year   year
     * @param month  month
     * @return List
     */
    List<AttenSummaryDetails> getSummaryOverDetails(String userId, String year, String month);

    /**
     * 查询调休汇总详情.
     *
     * @param userId userId
     * @param year   year
     * @param month  month
     * @return List
     */
    List<AttenSummaryDetails> getSummaryOffDetails(String userId, String year, String month);
}
