package zzjz.mapper;

import zzjz.bean.AttenSummary;

import java.util.List;

/**
 * @author 彭鹏
 * @ClassName: AttenSummaryMapper
 * @Description:
 * @date 2017年04月12日15:15
 */
public interface AttenSummaryMapper {

    List<AttenSummary> getSummaryList(String month);
}
