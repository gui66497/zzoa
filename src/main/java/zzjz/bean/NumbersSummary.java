package zzjz.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @author 彭鹏
 * @version 2017年05月09日11:24
 */
public class NumbersSummary {

    @Excel(name = "年/月")
    private String month;

    @Excel(name = "月初人数")
    private Double monthBegin;

    @Excel(name = "月末人数")
    private Double monthEnd;

    @Excel(name = "月均人数")
    private Double monthAvg;

    @Excel(name = "年均人数")
    private Double avg;

    /**
     * getMonth.
     *
     * @return MONTH
     */
    public String getMonth() {
        return month;
    }

    /**
     * setMonth.
     *
     * @param month MONTH
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * getMonthBegin.
     *
     * @return MONTH_BEGIN
     */
    public Double getMonthBegin() {
        return monthBegin;
    }

    /**
     * setMonthBegin.
     *
     * @param monthBegin MONTH_BEGIN
     */
    public void setMonthBegin(Double monthBegin) {
        this.monthBegin = monthBegin;
    }

    /**
     * getMonthEnd.
     *
     * @return MONTH_END
     */
    public Double getMonthEnd() {
        return monthEnd;
    }

    /**
     * setMonthEnd.
     *
     * @param monthEnd MONTH_END
     */
    public void setMonthEnd(Double monthEnd) {
        this.monthEnd = monthEnd;
    }

    /**
     * getMonthAvg.
     *
     * @return MONTH_AVG
     */
    public Double getMonthAvg() {
        return monthAvg;
    }

    /**
     * setMonthAvg.
     *
     * @param monthAvg MONTH_AVG
     */
    public void setMonthAvg(Double monthAvg) {
        this.monthAvg = monthAvg;
    }

    /**
     * getAvg.
     *
     * @return AVG
     */
    public Double getAvg() {
        return avg;
    }

    /**
     * setAvg.
     *
     * @param avg AVG
     */
    public void setAvg(Double avg) {
        this.avg = avg;
    }
}