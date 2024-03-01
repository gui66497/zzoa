package zzjz.service.impl;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.AttenSingleSummary;
import zzjz.bean.AttenSummary;
import zzjz.bean.NumbersSummary;
import zzjz.bean.SummaryYear;
import zzjz.bean.NumbersSummaryRequest;
import zzjz.bean.AttenSummaryDetails;
import zzjz.mapper.AttenSummaryMapper;
import zzjz.service.AttenSummaryService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * @author 彭鹏
 * @version 2017年04月19日11:15
 * @ClassName: AttenSummaryServiceImpl
 * @Description: 考勤汇总实现类
 */
@Service
public class AttenSummaryServiceImpl implements AttenSummaryService {

    private static final Integer INT10 = 10;

    private static final Integer INT3 = 3;

    private static final Integer INT4 = 4;

    private static final Integer INT5 = 5;

    private static final Integer INT6 = 6;

    private static final Integer INT7 = 7;

    private static final Integer INT8 = 8;

    @Autowired
    AttenSummaryMapper attenSummaryMapper;

    @Override
    public List<AttenSummary> initSummaryList(String date, String departName) {
        // 考勤周期改为自然月
        int year = Integer.parseInt(date.split("-")[0]);
        int month = Integer.parseInt(date.split("-")[1]);
        String beginDate = date + "-01";
        DateTime dateTime = new DateTime(year, month, 1, 0, 0, 0);
        String endDate = dateTime.dayOfMonth().withMaximumValue().toString("yyyy-MM-dd");
        List<AttenSummary> list = attenSummaryMapper.getSummaryList(beginDate, endDate, departName);

        for (AttenSummary attenSummary : list) {
            Double workday = checkNull(attenSummary.getWorkday()); //工作日
            Double compassionate = checkNull(attenSummary.getCompassionate()); //事假
            Double sick = checkNull(attenSummary.getSick()); //病假
            Double absent = checkNull(attenSummary.getAbsent()); //旷工
            attenSummary.setActual(workday - compassionate - sick - absent); //事假病假旷工
            if (workday < 0) {
                attenSummary.setWorkday(0.0);
                attenSummary.setActual(0.0);
            }
            if (attenSummary.getDeduction1() != null && attenSummary.getDeduction1() > 0) {
                attenSummary.setDeduction(attenSummary.getDeduction() + INT10);
            }
        }

        return list;
    }

    @Override
    public List<AttenSingleSummary> getSummerySingleList(Map map) {
        String leaveTyp = map.get("leaveTyp") == null ? "" : map.get("leaveTyp").toString();
        if ("20".equals(leaveTyp)) {
            return attenSummaryMapper.getSummaryTripList(map);
        } else if ("21".equals(leaveTyp)) {
            return attenSummaryMapper.getSummaryOutList(map);
        } else if ("01".equals(leaveTyp)) {
            return attenSummaryMapper.getSummaryAnnualList(map);
        } else if ("00".equals(leaveTyp)) {
            List<AttenSingleSummary> list = attenSummaryMapper.getSummaryOffList(map);
            for (AttenSingleSummary attenSingleSummary : list) {
                attenSingleSummary.setOver03(checkNull(attenSingleSummary.getOver03()));
                attenSingleSummary.setOver04(checkNull(attenSingleSummary.getOver04()));
                attenSingleSummary.setOver05(checkNull(attenSingleSummary.getOver05()));
                attenSingleSummary.setOver06(checkNull(attenSingleSummary.getOver06()));
                attenSingleSummary.setOver07(checkNull(attenSingleSummary.getOver07()));
                attenSingleSummary.setOver08(checkNull(attenSingleSummary.getOver08()));
                attenSingleSummary.setOver09(checkNull(attenSingleSummary.getOver09()));
                attenSingleSummary.setOver10(checkNull(attenSingleSummary.getOver10()));
                attenSingleSummary.setOver11(checkNull(attenSingleSummary.getOver11()));
                attenSingleSummary.setOver12(checkNull(attenSingleSummary.getOver12()));
                attenSingleSummary.setOver01(checkNull(attenSingleSummary.getOver01()));
                attenSingleSummary.setOver02(checkNull(attenSingleSummary.getOver02()));
                attenSingleSummary.setMonth03(checkNull(attenSingleSummary.getMonth03()));
                attenSingleSummary.setMonth04(checkNull(attenSingleSummary.getMonth04()));
                attenSingleSummary.setMonth05(checkNull(attenSingleSummary.getMonth05()));
                attenSingleSummary.setMonth06(checkNull(attenSingleSummary.getMonth06()));
                attenSingleSummary.setMonth07(checkNull(attenSingleSummary.getMonth07()));
                attenSingleSummary.setMonth08(checkNull(attenSingleSummary.getMonth08()));
                attenSingleSummary.setMonth09(checkNull(attenSingleSummary.getMonth09()));
                attenSingleSummary.setMonth10(checkNull(attenSingleSummary.getMonth10()));
                attenSingleSummary.setMonth11(checkNull(attenSingleSummary.getMonth11()));
                attenSingleSummary.setMonth12(checkNull(attenSingleSummary.getMonth12()));
                attenSingleSummary.setMonth01(checkNull(attenSingleSummary.getMonth01()));
                attenSingleSummary.setMonth02(checkNull(attenSingleSummary.getMonth02()));
                attenSingleSummary.setLeft03(checkNull(attenSingleSummary.getLeft03()));
                attenSingleSummary.setLeft04(checkNull(attenSingleSummary.getLeft04()));
                attenSingleSummary.setLeft05(checkNull(attenSingleSummary.getLeft05()));
                attenSingleSummary.setLeft06(checkNull(attenSingleSummary.getLeft06()));
                attenSingleSummary.setLeft07(checkNull(attenSingleSummary.getLeft07()));
                attenSingleSummary.setLeft08(checkNull(attenSingleSummary.getLeft08()));
                attenSingleSummary.setLeft09(checkNull(attenSingleSummary.getLeft09()));
                attenSingleSummary.setLeft10(checkNull(attenSingleSummary.getLeft10()));
                attenSingleSummary.setLeft11(checkNull(attenSingleSummary.getLeft11()));
                attenSingleSummary.setLeft12(checkNull(attenSingleSummary.getLeft12()));
                attenSingleSummary.setLeft01(checkNull(attenSingleSummary.getLeft01()));
                attenSingleSummary.setLeft02(checkNull(attenSingleSummary.getLeft02()));
            }
            return list;
        } else {
            return attenSummaryMapper.getSummaryVacationList(map);
        }
    }

    @Override
    public List<SummaryYear> getSummaryYearList(String departName, String beginDate, String endDate, String total) {
        List<SummaryYear> list = null;
        if (beginDate == null || "".equals(beginDate) || "undefined".equals(beginDate)) {
            beginDate = null;
        }
        if (endDate == null || "".equals(endDate) || "undefined".equals(endDate)) {
            endDate = null;
        }
        if (total == null || "".equals(total) || "undefined".equals(total)) {
            total = "11111111";
        }
        list = attenSummaryMapper.getSummaryYearList(departName, beginDate, endDate);

        for (SummaryYear summaryYear : list) {
            Double all = 0.0;
            if ("1".equals(total.substring(0, 1))) {
                all += summaryYear.getOff(); //调休
            }
            if ("1".equals(total.substring(1, 2))) {
                all += summaryYear.getAnnual(); //年假
            }
            if ("1".equals(total.substring(2, INT3))) {
                all += summaryYear.getCompassionate(); //事假
            }
            if ("1".equals(total.substring(INT3, INT4))) {
                all += summaryYear.getMaternity() + summaryYear.getAntenatal(); //产假+产前假
            }
            if ("1".equals(total.substring(INT4, INT5))) {
                all += summaryYear.getMarriage(); //婚假
            }
            if ("1".equals(total.substring(INT5, INT6))) {
                all += summaryYear.getFuneral(); //丧假
            }
            if ("1".equals(total.substring(INT6, INT7))) {
                all += summaryYear.getSick(); //病假
            }
            if ("1".equals(total.substring(INT7, INT8))) {
                all += summaryYear.getProject(); //工程调休
            }

            summaryYear.setTotal(all);
        }
        return list;
    }

    @Override
    public List<NumbersSummary> getNumbersSummary(NumbersSummaryRequest request) throws ParseException {
        List<NumbersSummary> list = new ArrayList<NumbersSummary>();
        String beginMonth = request.getBeginMonth();
        String endMonth = request.getEndMonth();
        String rule = request.getRule();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(beginMonth + "-01"));
        c.add(Calendar.MONTH, 1);
        String thisMonth = beginMonth;
        String nextMonth = sdf.format(c.getTime()).substring(0, INT7);
        while (sdf.parse(endMonth + "-01").after(sdf.parse(thisMonth + "-01")) || sdf.parse(endMonth + "-01").equals(sdf.parse(thisMonth + "-01"))) {
            NumbersSummaryRequest numbersSummaryRequest = new NumbersSummaryRequest();
            numbersSummaryRequest.setThisMonth(thisMonth);
            numbersSummaryRequest.setNextMonth(nextMonth);
            numbersSummaryRequest.setEducation(request.getEducation());
            numbersSummaryRequest.setPost(request.getPost());
            NumbersSummary numbersSummary = attenSummaryMapper.getNumbersSummary(numbersSummaryRequest);
            if (numbersSummary.getMonthBegin() == null) {
                numbersSummary.setMonthBegin(0.0);
            }
            if (numbersSummary.getMonthEnd() == null) {
                numbersSummary.setMonthEnd(0.0);
            }
            numbersSummary.setMonthAvg(Double.valueOf(Math.round((numbersSummary.getMonthBegin() + numbersSummary.getMonthEnd()) / 2)));
            list.add(numbersSummary);
            c.add(Calendar.MONTH, 1);
            thisMonth = nextMonth;
            nextMonth = sdf.format(c.getTime()).substring(0, INT7);
        }
        if ("0".equals(rule)) {
            Double sum = 0.0;
            for (int i = 0; i < list.size(); i++) {
                sum += list.get(i).getMonthAvg();
            }
            NumbersSummary numbersSummary = new NumbersSummary();
            numbersSummary.setMonth("平均");
            numbersSummary.setAvg(Double.valueOf(Math.round(sum / list.size())));
            list.add(numbersSummary);
        } else if ("1".equals(rule)) {
            Double sum = list.get(0).getMonthBegin() + list.get(list.size() - 1).getMonthEnd();
            NumbersSummary numbersSummary = new NumbersSummary();
            numbersSummary.setMonth("平均");
            numbersSummary.setAvg(Double.valueOf(Math.round(sum / 2)));
            list.add(numbersSummary);
        }
        return list;
    }

    @Override
    public List<AttenSummaryDetails> getSummaryDetails(String userId, String beginDate, String endDate) {
        Map<String, String> map = new HashMap<>();
        map.put("00", "调休");
        map.put("01", "年假");
        map.put("02", "事假");
        map.put("03", "产假");
        map.put("04", "婚假");
        map.put("05", "丧假");
        map.put("06", "病假");
        map.put("07", "工程调休");
        map.put("08", "产前假");
        map.put("10", "加班");
        map.put("20", "出差");
        map.put("21", "外出");
        map.put("31", "迟到");
        map.put("32", "早退");
        map.put("33", "旷工");
        beginDate = beginDate.substring(0, INT7) + "-16";
        endDate = endDate.substring(0, INT7) + "-15";
        List<AttenSummaryDetails> list = attenSummaryMapper.getSummaryDetails(userId, beginDate, endDate);
        for (AttenSummaryDetails attenSummaryDetails : list) {
            attenSummaryDetails.setType(map.get(attenSummaryDetails.getType()));
        }
        return list;
    }

    @Override
    public List<AttenSummaryDetails> getSummaryOverDetails(String userId, String year, String month) {
        // 1月和2月应该是第二年的
        if ("01".equals(month) || "02".equals(month)) {
            year = (Integer.parseInt(year) + 1) + "";
        }
        // 考勤周期改为自然月
        String beginDate = year + "-" + month + "-01";
        DateTime begin = new DateTime(Integer.parseInt(year), Integer.parseInt(month), 1, 0, 0, 0);
        String endDate = begin.plusMonths(1).minusDays(1).toString("yyyy-MM-dd");
        return attenSummaryMapper.getSummaryOverDetails(userId, beginDate, endDate);
    }

    @Override
    public List<AttenSummaryDetails> getSummaryOffDetails(String userId, String year, String month) {
        // 1月和2月应该是第二年的
        if ("01".equals(month) || "02".equals(month)) {
            year = (Integer.parseInt(year) + 1) + "";
        }
        // 考勤周期改为自然月
        String beginDate = year + "-" + month + "-01";
        DateTime begin = new DateTime(Integer.parseInt(year), Integer.parseInt(month), 1, 0, 0, 0);
        String endDate = begin.plusMonths(1).minusDays(1).toString("yyyy-MM-dd");
        return attenSummaryMapper.getSummaryOffDetails(userId, beginDate, endDate);
    }

    private Double checkNull(Double d) {
        if (d == null) {
            return 0.0;
        } else {
            return d;
        }
    }
}
