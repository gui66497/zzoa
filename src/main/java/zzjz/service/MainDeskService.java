package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.MainDesk;
import zzjz.bean.MainDeskRequest;

import java.util.List;

/**
 * @author 房桂堂
 * @version 2017/5/15 9:15
 * @ClassName: MainDeskService
 * @Description: 待办事项service
 */
@Service
public interface MainDeskService {

    /**
     * 分页获取待办信息.
     *
     * @param request request
     * @return 事项
     */
    Page<MainDesk> getMainDeskPageList(MainDeskRequest request);

    /**
     * 分页获取已办信息.
     *
     * @param request request
     * @return 事项
     */
    Page<MainDesk> getMainDeskDoneList(MainDeskRequest request);

    /**
     * 分页获取已提交信息.
     *
     * @param request   request
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param userName  用户名
     * @param applyTyp  类别
     * @param departName 部门名称
     * @param finish 是否是结束流程
     * @return 事项
     */
    Page<MainDesk> getCommitAttentionList(MainDeskRequest request, String beginDate, String endDate,
                                          String userName, String applyTyp, String departName, boolean finish);

    /**
     * 获取所有进行中的任务.
     *
     * @return 任务
     */
    List<MainDesk> getRunningDesk();

    /**
     * 获取处理进度列表.
     *
     * @param request 请求信息
     * @return 列表
     */
    Page<MainDesk> getProgressList(MainDeskRequest request);
}
