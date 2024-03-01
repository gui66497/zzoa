package zzjz.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 彭鹏
 * @version 2017年04月12日15:14
 * @ClassName: AttenSummary
 * @Description: 考勤汇总
 */
@XmlRootElement
public class SummaryYear {

    @Excel(name = "ID")
    private String userId;

    @Excel(name = "员工姓名")
    private String userName;

    @Excel(name = "部门")
    private String depart;

    @Excel(name = "调休（天）")
    private Double off;

    @Excel(name = "年假（天）")
    private Double annual;

    @Excel(name = "事假（天）")
    private Double compassionate;

    @Excel(name = "婚假（天）")
    private Double marriage;

    @Excel(name = "病假（天）")
    private Double sick;

    @Excel(name = "丧假（天）")
    private Double funeral;

    @Excel(name = "产假（天）")
    private Double maternity;

    @Excel(name = "产前假（天）")
    private Double antenatal;

    @Excel(name = "工程调休（天）")
    private Double project;

    @Excel(name = "总计（天）")
    private Double total;

    /**
     * getUserId.
     *
     * @return USER_ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * setUserId.
     *
     * @param userId USER_ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * getUserName.
     *
     * @return USER_NAME
     */
    public String getUserName() {
        return userName;
    }

    /**
     * setUserName.
     *
     * @param userName USER_NAME
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * getDepart.
     *
     * @return DEPART
     */
    public String getDepart() {
        return depart;
    }

    /**
     * setDepart.
     *
     * @param depart DEPART
     */
    public void setDepart(String depart) {
        this.depart = depart;
    }

    /**
     * getOff.
     *
     * @return OFF
     */
    public Double getOff() {
        return off;
    }

    /**
     * setOff.
     *
     * @param off OFF
     */
    public void setOff(Double off) {
        this.off = off;
    }

    /**
     * getAnnual.
     *
     * @return ANNUAL
     */
    public Double getAnnual() {
        return annual;
    }

    /**
     * setAnnual.
     *
     * @param annual ANNUAL
     */
    public void setAnnual(Double annual) {
        this.annual = annual;
    }

    /**
     * getCompassionate.
     *
     * @return COMPASSIONATE
     */
    public Double getCompassionate() {
        return compassionate;
    }

    /**
     * setCompassionate.
     *
     * @param compassionate COMPASSIONATE
     */
    public void setCompassionate(Double compassionate) {
        this.compassionate = compassionate;
    }

    /**
     * getMarriage.
     *
     * @return MARRIAGE
     */
    public Double getMarriage() {
        return marriage;
    }

    /**
     * setMarriage.
     *
     * @param marriage MARRIAGE
     */
    public void setMarriage(Double marriage) {
        this.marriage = marriage;
    }

    /**
     * getSick.
     *
     * @return SICK
     */
    public Double getSick() {
        return sick;
    }

    /**
     * setSick.
     *
     * @param sick SICK
     */
    public void setSick(Double sick) {
        this.sick = sick;
    }

    /**
     * getFuneral.
     *
     * @return FUNERAL
     */
    public Double getFuneral() {
        return funeral;
    }

    /**
     * setFuneral.
     *
     * @param funeral FUNERAL
     */
    public void setFuneral(Double funeral) {
        this.funeral = funeral;
    }

    /**
     * getMaternity.
     *
     * @return MATERNITY
     */
    public Double getMaternity() {
        return maternity;
    }

    /**
     * setMaternity.
     *
     * @param maternity MATERNITY
     */
    public void setMaternity(Double maternity) {
        this.maternity = maternity;
    }

    /**
     * getAntenatal.
     *
     * @return ANTENATAL
     */
    public Double getAntenatal() {
        return antenatal;
    }

    /**
     * setAntenatal.
     *
     * @param antenatal ANTENATAL
     */
    public void setAntenatal(Double antenatal) {
        this.antenatal = antenatal;
    }

    /**
     * getProject.
     *
     * @return PROJECT
     */
    public Double getProject() {
        return project;
    }

    /**
     * setProject.
     *
     * @param project PROJECT
     */
    public void setProject(Double project) {
        this.project = project;
    }

    /**
     * getTotal.
     *
     * @return TOTAL
     */
    public Double getTotal() {
        return total;
    }

    /**
     * setTotal.
     *
     * @param total TOTAL
     */
    public void setTotal(Double total) {
        this.total = total;
    }
}