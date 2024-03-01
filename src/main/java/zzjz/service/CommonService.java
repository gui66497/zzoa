package zzjz.service;

import org.springframework.stereotype.Service;

import java.text.ParseException;

/**
 * @author 彭鹏
 * @version 2017年04月13日16:32
 * @ClassName: CommonService
 * @Description: 通用管理服务接口
 */
@Service
public interface CommonService {
    /**
     * 获取主键.
     *
     * @return 主键
     */
    Integer getActSeq();

    /**
     * 事项冲突校验.
     *
     * @param userId    用户
     * @param beginDate 开始日期
     * @param endDate   开始时段
     * @param beginPart 结束日期
     * @param endPart   结束时段
     * @return 结果
     * @throws ParseException ParseException
     */
    boolean checkOption(String userId, String beginDate, String endDate, String beginPart, String endPart) throws ParseException;

    /**
     * 事项冲突校验.
     *
     * @param userId    用户
     * @param startTime 开始时间
     * @param endTime   开始时间
     * @param id        id
     * @return 结果
     * @throws ParseException ParseException
     */
    boolean forCheckOption(String userId, String startTime, String endTime, int id) throws ParseException;

    /**
     * 事项冲突校验.
     *
     * @param userId    用户
     * @param beginDate 开始日期
     * @param endDate   开始时段
     * @param beginPart 结束日期
     * @param endPart   结束时段
     * @param id        id
     * @return 结果
     * @throws ParseException ParseException
     */
    boolean checkOptionWithId(Integer id, String userId, String beginDate, String endDate, String beginPart, String endPart) throws ParseException;
}
