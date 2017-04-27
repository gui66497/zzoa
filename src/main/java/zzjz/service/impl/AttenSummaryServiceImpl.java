package zzjz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.AttenSummary;
import zzjz.mapper.AttenSummaryMapper;
import zzjz.service.AttenSummaryService;

import java.util.List;

/**
 * @author 彭鹏
 * @ClassName: zoa
 * @Description:
 * @date 2017年04月19日11:15
 */
@Service
public class AttenSummaryServiceImpl implements AttenSummaryService{

    @Autowired
    AttenSummaryMapper attenSummaryMapper;

    @Override
    public List<AttenSummary> initSummaryList(String month) {
        List<AttenSummary> list = attenSummaryMapper.getSummaryList(month);

        for (AttenSummary attenSummary:list){
            Double workday = attenSummary.getWorkday()==null?0.0:attenSummary.getWorkday();//工作日
            Double compassionate = attenSummary.getCompassionate()==null?0.0:attenSummary.getCompassionate();//事假
            Double sick = attenSummary.getSick()==null?0.0:attenSummary.getSick();//病假
            Double absent = attenSummary.getAbsent()==null?0.0:attenSummary.getAbsent();//旷工
            attenSummary.setActual(workday-compassionate-sick-absent);//事假病假旷工
        }

        return list;
    }
}
