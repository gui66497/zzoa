package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 彭鹏
 * @version 2017年05月09日11:24
 * @ClassName: AttenSummaryDetails
 * @Description: 考勤汇总详情Bean
 */
@XmlRootElement
public class AttenSummaryDetails {
    private String type;

    private String beginDate;

    private Double days;

    /**
     * getType.
     *
     * @return TYPE
     */
    public String getType() {
        return type;
    }

    /**
     * setType.
     *
     * @param type TYPE
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getBeginDate.
     *
     * @return BEGIN_DATE
     */
    public String getBeginDate() {
        return beginDate;
    }

    /**
     * setBeginDate.
     *
     * @param beginDate BEGIN_DATE
     */
    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    /**
     * getDays.
     *
     * @return DAYS
     */
    public Double getDays() {
        return days;
    }

    /**
     * setDays.
     *
     * @param days DAYS
     */
    public void setDays(Double days) {
        this.days = days;
    }
}
