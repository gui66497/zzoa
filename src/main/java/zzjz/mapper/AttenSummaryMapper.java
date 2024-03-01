package zzjz.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import zzjz.bean.AttenSingleSummary;
import zzjz.bean.AttenSummary;
import zzjz.bean.NumbersSummary;
import zzjz.bean.SummaryYear;
import zzjz.bean.AttenSummaryDetails;
import zzjz.bean.NumbersSummaryRequest;

import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年04月12日15:15
 * @ClassName: AttenSummaryMapper
 * @Description:
 */
@Repository
public interface AttenSummaryMapper {

    /**
     * @param beginDate beginDate
     * @param endDate   endDate
     * @param depart    depart
     * @return List
     */
    List<AttenSummary> getSummaryList(@Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("depart") String depart);

    /**
     * 获取单项假期考勤统计.
     *
     * @param map map
     * @return 列表
     */
    List<AttenSingleSummary> getSummaryVacationList(Map map);

    /**
     * 获取年假考勤统计.
     *
     * @param map map
     * @return 列表
     */
    List<AttenSingleSummary> getSummaryAnnualList(Map map);

    /**
     * 获取调休考勤统计.
     *
     * @param map map
     * @return 列表
     */
    List<AttenSingleSummary> getSummaryOffList(Map map);

    /**
     * 获取单项差旅考勤统计.
     *
     * @param map map
     * @return 列表
     */
    List<AttenSingleSummary> getSummaryTripList(Map map);

    /**
     * 获取单项外出考勤统计.
     *
     * @param map map
     * @return 列表
     */
    List<AttenSingleSummary> getSummaryOutList(Map map);

    /**
     * 获取请假汇总信息.
     *
     * @param departName 机构名称
     * @param beginDate  开始日期
     * @param endDate    结束日期
     * @return 列表
     */
    List<SummaryYear> getSummaryYearList(@Param("departName") String departName,
                                         @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 获取月初月末人数.
     *
     * @param request 条件集
     * @return 数据信息
     */
    NumbersSummary getNumbersSummary(NumbersSummaryRequest request);

    /**
     * 获取考勤汇总详情.
     *
     * @param userId    用户ID
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 列表
     */
    List<AttenSummaryDetails> getSummaryDetails(@Param("userId") String userId,
                                                @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 获取考勤汇总加班详情.
     *
     * @param userId    用户ID
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 列表
     */
    List<AttenSummaryDetails> getSummaryOverDetails(@Param("userId") String userId,
                                                    @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 获取考勤汇总调休详情.
     *
     * @param userId    用户ID
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 列表
     */
    List<AttenSummaryDetails> getSummaryOffDetails(@Param("userId") String userId,
                                                   @Param("beginDate") String beginDate, @Param("endDate") String endDate);

}
