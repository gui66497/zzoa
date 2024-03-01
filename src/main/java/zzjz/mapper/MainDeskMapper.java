package zzjz.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import zzjz.bean.MainDesk;
import zzjz.bean.MainDeskRequest;

import java.util.List;

/**
 * @author 房桂堂
 * @version 2017/10/9 9:08
 * @ClassName: MainDeskMapper
 * @Description:
 */
@Repository
public interface MainDeskMapper {

    /**
     * 获取待办事项.
     *
     * @param request request
     * @return 事项
     */
    List<MainDesk> getMainDeskPageList(MainDeskRequest request);

    /**
     * 获取已办事项.
     *
     * @param request request
     * @return 事项
     */
    List<MainDesk> getMainDeskDoneList(MainDeskRequest request);

    /**
     * 获取所有事项完成信息.（20191219 添加了endtime字段 但由于前端改动较大没有继续进行下去 todo）
     *
     * @param request   request
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param userName  用户名
     * @param applyTyp  类型
     * @param departName  类型
     * @return 事项
     */
    List<MainDesk> getCommitAttentionList(MainDeskRequest request, @Param("beginDate") String beginDate,
                                          @Param("endDate") String endDate, @Param("userName") String userName, @Param("applyTyp") String applyTyp,
                                          @Param("departName") String departName);


    /**
     * 获取所有进行中事项信息.
     * @param request   request
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param userName  用户名
     * @param applyTyp  类型
     * @param departName  类型
     * @return 事项
     */
    List<MainDesk> getUnCommitAttentionList(MainDeskRequest request, @Param("beginDate") String beginDate,
                                          @Param("endDate") String endDate, @Param("userName") String userName, @Param("applyTyp") String applyTyp,
                                          @Param("departName") String departName);
    /**
     * 获取所有进行中的任务.
     *
     * @return 任务
     */
    List<MainDesk> getRunningDesk();

    /**
     * 获取处理进度列表.
     *
     * @param request request
     * @return 任务列表
     */
    List<MainDesk> getProgressList(MainDeskRequest request);
}