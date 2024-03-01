package zzjz.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017-10-16
 */
@Repository
public interface CommonMapper {

    /**
     * 获取序列号.
     *
     * @return int
     */
    int actSeq();

    /**
     * @param userId    userId
     * @param beginDate beginDate
     * @param endDate   endDate
     * @return List
     */
    List<Map<String, String>> selectOptionDays(@Param("userId") String userId,
                                               @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 获取指定用户指定时间段的事项.
     * @param userId    userId
     * @param beginDate beginDate
     * @param endDate   endDate
     * @param id        id
     * @return List
     */
    List<Map<String, String>> selectForOptionDays(@Param("userId") String userId,
                                                  @Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("id") int id);
}