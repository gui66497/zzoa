package zzjz.service.impl;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.mapper.CommonMapper;
import zzjz.service.CommonService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年04月13日16:34
 * @ClassName: CommonServiceImpl
 * @Description: 通用方法实现类
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    CommonMapper commonMapper;

    /**
     * 获取主键.
     *
     * @return 主键
     */
    @Override
    public Integer getActSeq() {
        return commonMapper.actSeq();
    }

    @Override
    public boolean checkOption(String userId, String beginDate, String endDate, String beginPart, String endPart) throws ParseException {
        if (StringUtils.isBlank(beginDate)) {
            return false;
        }
        if (StringUtils.isBlank(endPart)) {
            endPart = beginPart;
        }
        if (StringUtils.isBlank(endDate)) {
            endDate = beginDate;
            endPart = beginPart;
        }
        if (StringUtils.isBlank(beginPart)) {
            beginPart = "all";
        }
        // 出去和回来不是同一天
        if (!beginDate.equals(endDate)) {
            if ("0".equals(beginPart)) {
                beginPart = "all";
            } else if ("1".equals(beginPart)) {
                beginPart = "aft";
            }
            if ("1".equals(endPart)) {
                endPart = "all";
            } else if ("0".equals(endPart)) {
                endPart = "mor";
            }
        } else {
            // 出去和回来是同一天
            if ("0".equals(beginPart) && "0".equals(endPart)) {
                beginPart = "mor";
                endPart = "mor";
            } else if ("1".equals(beginPart) && "1".equals(endPart)) {
                beginPart = "aft";
                endPart = "aft";
            } else if ("0".equals(beginPart) && "1".equals(endPart)) {
                beginPart = "all";
                endPart = "all";
            }
        }
        List<Map<String, String>> list = commonMapper.selectOptionDays(userId, beginDate, endDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, String> map = list.get(i);
                String optionDate = map.get("optionDate");
                String optionPart = map.get("optionPart");
                /*if (sdf.parse(optionDate).after(sdf.parse(beginDate)) || sdf.parse(optionDate).before(sdf.parse(endDate))) {
                    return false;
                } else if (optionDate.equals(beginDate) && !(("mor".equals(optionPart) && "aft".equals(beginPart))
                        || ("aft".equals(optionPart) && "mor".equals(beginPart)))) {
                    return false;
                } else if (optionDate.equals(endDate) && !(("mor".equals(optionPart) && "aft".equals(endPart))
                        || ("aft".equals(optionPart) && "mor".equals(endPart)))) {
                    return false;
                }*/

                // 房桂堂 20191108 如果在开始日期和结束日期之内
                // 开始日期和结束日期当天单独判断 如果是all肯定冲突 如果是类型相同 肯定冲突
                if (isBetween(optionDate, beginDate, endDate)) {
                    if (optionDate.equals(beginDate)) {
                        if ("all".equals(optionPart) || optionPart.equals(beginPart)) {
                            return false;
                        }
                    }
                    if (optionDate.equals(endDate)) {
                        if ("all".equals(optionPart) || optionPart.equals(endPart)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean forCheckOption(String userId, String startTimeStr, String endTimeStr, int id) throws ParseException {
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isBlank(startTimeStr)) {
            return false;
        }
        if (StringUtils.isBlank(endTimeStr)) {
            endTimeStr = startTimeStr;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        DateTime startTime = DateTime.parse(startTimeStr, format);
        DateTime endTime = DateTime.parse(endTimeStr, format);
        String beginDate = startTime.toString("yyyy-MM-dd");
        String endDate = endTime.toString("yyyy-MM-dd");
        // 这里根据时间算出是mor还是aft 房桂堂 20191108
        String beginPart = startTime.getHourOfDay() < 12 ? "mor" : "aft";
        String endPart = endTime.getHourOfDay() < 12 ? "mor" : "aft";
        /*String beginPart = "mor";
        String endPart = "aft";*/

        List<Map<String, String>> list = commonMapper.selectForOptionDays(userId, beginDate, endDate, id);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, String> map = list.get(i);
                String optionDate = map.get("optionDate");
                String optionPart = map.get("optionPart");
                /*if (sdf1.parse(optionDate).after(sdf1.parse(beginDate)) || sdf1.parse(optionDate).before(sdf1.parse(endDate))) {
                    return false;
                } else if (optionDate.equals(beginDate) && !(("mor".eq`uals(optionPart) && "aft".equals(beginPart))
                        || ("aft".equals(optionPart) && "mor".equals(beginPart)))) {
                    return false;
                } else if (optionDate.equals(endDate) && !(("mor".equals(optionPart) && "aft".equals(endPart))
                        || ("aft".equals(optionPart) && "mor".equals(endPart)))) {
                    return false;
                }*/
                // 房桂堂 20191108 如果在开始日期和结束日期之内
                // 开始日期和结束日期当天单独判断 如果是all肯定冲突 如果是类型相同 肯定冲突
                if (isBetween(optionDate, beginDate, endDate)) {
                    if (optionDate.equals(beginDate)) {
                        if ("all".equals(optionPart) || optionPart.equals(beginPart)) {
                            return false;
                        }
                    }
                    if (optionDate.equals(endDate)) {
                        if ("all".equals(optionPart) || optionPart.equals(endPart)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }


    /**
     * 判断指定日期是否在范围之内 包括边界值也算在内.
     * @param dateStr 待测日期
     * @param beginDateStr 开始日期
     * @param endDateStr 结束日期
     * @return 结果
     */
    boolean isBetween(String dateStr, String beginDateStr, String endDateStr) {
        DateTime date = new DateTime(dateStr);
        DateTime beginDate = new DateTime(beginDateStr);
        DateTime endDate = new DateTime(endDateStr);

        if (date.isEqual(beginDate) || date.isEqual(endDate)) {
            return true;
        }
        if (date.isAfter(beginDate) && date.isBefore(endDate)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkOptionWithId(Integer id, String userId, String beginDate, String endDate, String beginPart, String endPart)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, String>> list = commonMapper.selectForOptionDays(userId, beginDate, endDate, id);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, String> map = list.get(i);
                String optionDate = map.get("optionDate");
                String optionPart = map.get("optionPart");
                if (sdf.parse(optionDate).after(sdf.parse(beginDate)) || sdf.parse(optionDate).before(sdf.parse(endDate))) {
                    return false;
                } else if (optionDate.equals(beginDate) && !(("mor".equals(optionPart) && "aft".equals(beginPart))
                        || ("aft".equals(optionPart) && "mor".equals(beginPart)))) {
                    return false;
                } else if (optionDate.equals(endDate) && !(("mor".equals(optionPart) && "aft".equals(endPart))
                        || ("aft".equals(optionPart) && "mor".equals(endPart)))) {
                    return false;
                }
            }
        }
        return true;
    }

}
