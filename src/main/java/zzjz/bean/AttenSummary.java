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
public class AttenSummary {
    private String userId;

    @Excel(name = "员工姓名")
    private String userName;

    @Excel(name = "部门")
    private String depart;

    @Excel(name = "应出勤")
    private Double workday;

    @Excel(name = "实出勤")
    private Double actual;

    @Excel(name = "迟到")
    private Double late;

    @Excel(name = "早退")
    private Double early;

    @Excel(name = "旷工")
    private Double absent;

    @Excel(name = "本月加班")
    private Double overtime;

    @Excel(name = "迟到扣款")
    private Double deduction;

    @Excel(name = "迟到15分钟以内次数")
    private Double deduction1;

    @Excel(name = "餐补")
    private Double meal;

    @Excel(name = "调休")
    private Double off;

    @Excel(name = "年假")
    private Double annual;

    @Excel(name = "事假")
    private Double compassionate;

    @Excel(name = "婚假")
    private Double marriage;

    @Excel(name = "病假")
    private Double sick;

    @Excel(name = "丧假")
    private Double funeral;

    @Excel(name = "产假")
    private Double maternity;

    @Excel(name = "产前假")
    private Double antenatal;

    @Excel(name = "工程调休")
    private Double project;

    @Excel(name = "出差")
    private Double trip;

    private String note;

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
     * getWorkday.
     *
     * @return WORK_DAY
     */
    public Double getWorkday() {
        return workday;
    }

    /**
     * setWorkday.
     *
     * @param workday WORK_DAY
     */
    public void setWorkday(Double workday) {
        this.workday = workday;
    }

    /**
     * getActual.
     *
     * @return ACTUAL
     */
    public Double getActual() {
        return actual;
    }

    /**
     * setActual.
     *
     * @param actual ACTUAL
     */
    public void setActual(Double actual) {
        this.actual = actual;
    }

    /**
     * getLate.
     *
     * @return LATE
     */
    public Double getLate() {
        return late;
    }

    /**
     * setLate.
     *
     * @param late LATE
     */
    public void setLate(Double late) {
        this.late = late;
    }

    /**
     * getEarly.
     *
     * @return EARLY
     */
    public Double getEarly() {
        return early;
    }

    /**
     * setEarly.
     *
     * @param early EARLY
     */
    public void setEarly(Double early) {
        this.early = early;
    }

    /**
     * getAbsent.
     *
     * @return ABSENT
     */
    public Double getAbsent() {
        return absent;
    }

    /**
     * setAbsent.
     *
     * @param absent ABSENT
     */
    public void setAbsent(Double absent) {
        this.absent = absent;
    }

    /**
     * getOvertime.
     *
     * @return OVER_TIME
     */
    public Double getOvertime() {
        return overtime;
    }

    /**
     * setOvertime.
     *
     * @param overtime OVER_TIME
     */
    public void setOvertime(Double overtime) {
        this.overtime = overtime;
    }

    /**
     * getDeduction.
     *
     * @return DEDUCTION
     */
    public Double getDeduction() {
        return deduction;
    }

    /**
     * setDeduction.
     *
     * @param deduction DEDUCTION
     */
    public void setDeduction(Double deduction) {
        this.deduction = deduction;
    }

    /**
     * getDeduction1.
     *
     * @return DEDUCTION1
     */
    public Double getDeduction1() {
        return deduction1;
    }

    /**
     * setDeduction1.
     *
     * @param deduction1 DEDUCTION1
     */
    public void setDeduction1(Double deduction1) {
        this.deduction1 = deduction1;
    }

    /**
     * getMeal.
     *
     * @return MEAL
     */
    public Double getMeal() {
        return meal;
    }

    /**
     * setMeal.
     *
     * @param meal MEAL
     */
    public void setMeal(Double meal) {
        this.meal = meal;
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
     * getTrip.
     *
     * @return TRIP
     */
    public Double getTrip() {
        return trip;
    }

    /**
     * setTrip.
     *
     * @param trip TRIP
     */
    public void setTrip(Double trip) {
        this.trip = trip;
    }

    /**
     * getNote.
     *
     * @return NOTE
     */
    public String getNote() {
        return note;
    }

    /**
     * setNote.
     *
     * @param note NOTE
     */
    public void setNote(String note) {
        this.note = note;
    }
}