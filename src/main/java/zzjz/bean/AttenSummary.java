package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 彭鹏
 * @ClassName: AttenSummary
 * @Description: 考勤汇总
 * @date 2017年04月12日15:14
 */
@XmlRootElement
public class AttenSummary {
    private String userId;
    private String userName;
    private String depart;
    private Double workday;
    private Double actual;
    private Double late;
    private Double early;
    private Double absent;
    private Double overtime;
    private Double deduction;
    private Double meal;
    private Double off;
    private Double annual;
    private Double compassionate;
    private Double marriage;
    private Double sick;
    private Double funeral;
    private Double maternity;
    private Double trip;
    private String note;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public Double getWorkday() {
        return workday;
    }

    public void setWorkday(Double workday) {
        this.workday = workday;
    }

    public Double getActual() {
        return actual;
    }

    public void setActual(Double actual) {
        this.actual = actual;
    }

    public Double getLate() {
        return late;
    }

    public void setLate(Double late) {
        this.late = late;
    }

    public Double getEarly() {
        return early;
    }

    public void setEarly(Double early) {
        this.early = early;
    }

    public Double getAbsent() {
        return absent;
    }

    public void setAbsent(Double absent) {
        this.absent = absent;
    }

    public Double getOvertime() {
        return overtime;
    }

    public void setOvertime(Double overtime) {
        this.overtime = overtime;
    }

    public Double getDeduction() {
        return deduction;
    }

    public void setDeduction(Double deduction) {
        this.deduction = deduction;
    }

    public Double getMeal() {
        return meal;
    }

    public void setMeal(Double meal) {
        this.meal = meal;
    }

    public Double getOff() {
        return off;
    }

    public void setOff(Double off) {
        this.off = off;
    }

    public Double getAnnual() {
        return annual;
    }

    public void setAnnual(Double annual) {
        this.annual = annual;
    }

    public Double getCompassionate() {
        return compassionate;
    }

    public void setCompassionate(Double compassionate) {
        this.compassionate = compassionate;
    }

    public Double getMarriage() {
        return marriage;
    }

    public void setMarriage(Double marriage) {
        this.marriage = marriage;
    }

    public Double getSick() {
        return sick;
    }

    public void setSick(Double sick) {
        this.sick = sick;
    }

    public Double getFuneral() {
        return funeral;
    }

    public void setFuneral(Double funeral) {
        this.funeral = funeral;
    }

    public Double getMaternity() {
        return maternity;
    }

    public void setMaternity(Double maternity) {
        this.maternity = maternity;
    }

    public Double getTrip() {
        return trip;
    }

    public void setTrip(Double trip) {
        this.trip = trip;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}