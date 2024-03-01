package zzjz.service;

import org.springframework.stereotype.Service;
import zzjz.bean.Annual;

/**
 * @author 彭鹏
 * @version 2017年04月06日14:17
 * @ClassName: AnnualService
 * @Description: 年假服务接口
 */
@Service
public interface AnnualService {
    /**
     * 计算年假天数.
     *
     * @param userId 用户ID
     * @param year   年份
     * @return 天数
     */
    Double annualDays(String userId, String year);

    /**
     * 生成年假表信息.
     *
     * @param userId 用户号
     * @param year   年份
     * @return 结果
     */
    boolean initAnnual(String userId, String year);

    /**
     * 根据年份生成年假信息.
     *
     * @param year 年份
     * @return 结果
     */
    boolean initBatchAnnual(String year);

    /**
     * 更新年假信息.
     *
     * @param annual annual
     * @return boolean
     */
    boolean updateAnnualInfo(Annual annual);

    /**
     * 查询年假信息.
     *
     * @param staffId staffId
     * @return boolean
     */
    boolean queryAndInitAnnual(long staffId);
}
