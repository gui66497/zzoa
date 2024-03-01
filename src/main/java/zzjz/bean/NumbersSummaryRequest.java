package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 彭鹏
 * @version 2017年04月12日15:14
 */
@XmlRootElement
public class NumbersSummaryRequest extends BaseRequest {
    private String beginMonth;

    private String endMonth;

    private String thisMonth;

    private String nextMonth;

    private String rule;

    private String education;

    private String post;

    /**
     * getBeginMonth.
     *
     * @return BEGIN_MONTH
     */
    public String getBeginMonth() {
        return beginMonth;
    }

    /**
     * setBeginMonth.
     *
     * @param beginMonth BEGIN_MONTH
     */
    public void setBeginMonth(String beginMonth) {
        this.beginMonth = beginMonth;
    }

    /**
     * getEndMonth.
     *
     * @return END_MONTH
     */
    public String getEndMonth() {
        return endMonth;
    }

    /**
     * setEndMonth.
     *
     * @param endMonth END_MONTH
     */
    public void setEndMonth(String endMonth) {
        this.endMonth = endMonth;
    }

    /**
     * getThisMonth.
     *
     * @return THIS_MONTH
     */
    public String getThisMonth() {
        return thisMonth;
    }

    /**
     * setThisMonth.
     *
     * @param thisMonth THIS_MONTH
     */
    public void setThisMonth(String thisMonth) {
        this.thisMonth = thisMonth;
    }

    /**
     * getNextMonth.
     *
     * @return NEXT_MONTH
     */
    public String getNextMonth() {
        return nextMonth;
    }

    /**
     * setNextMonth.
     *
     * @param nextMonth NEXT_MONTH
     */
    public void setNextMonth(String nextMonth) {
        this.nextMonth = nextMonth;
    }

    /**
     * getRule.
     *
     * @return RULE
     */
    public String getRule() {
        return rule;
    }

    /**
     * setRule.
     *
     * @param rule RULE
     */
    public void setRule(String rule) {
        this.rule = rule;
    }

    /**
     * getEducation.
     *
     * @return EDUCATION
     */
    public String getEducation() {
        return education;
    }

    /**
     * setEducation.
     *
     * @param education EDUCATION
     */
    public void setEducation(String education) {
        this.education = education;
    }

    /**
     * getPost.
     *
     * @return POST
     */
    public String getPost() {
        return post;
    }

    /**
     * setPost.
     *
     * @param post POST
     */
    public void setPost(String post) {
        this.post = post;
    }
}