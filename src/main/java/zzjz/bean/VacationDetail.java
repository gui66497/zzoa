package zzjz.bean;

public class VacationDetail {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_vacation_detail.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_vacation_detail.user_id
     *
     * @mbggenerated
     */
    private String userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_vacation_detail.leave_date
     *
     * @mbggenerated
     */
    private String leaveDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_vacation_detail.leave_part
     *
     * @mbggenerated
     */
    private String leavePart;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_vacation_detail.leave_typ
     *
     * @mbggenerated
     */
    private String leaveTyp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_vacation_detail.leave_day
     *
     * @mbggenerated
     */
    private Double leaveDay;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_vacation_detail.id
     *
     * @return the value of tb_vacation_detail.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_vacation_detail.id
     *
     * @param id the value for tb_vacation_detail.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_vacation_detail.user_id
     *
     * @return the value of tb_vacation_detail.user_id
     *
     * @mbggenerated
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_vacation_detail.user_id
     *
     * @param userId the value for tb_vacation_detail.user_id
     *
     * @mbggenerated
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_vacation_detail.leave_date
     *
     * @return the value of tb_vacation_detail.leave_date
     *
     * @mbggenerated
     */
    public String getLeaveDate() {
        return leaveDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_vacation_detail.leave_date
     *
     * @param leaveDate the value for tb_vacation_detail.leave_date
     *
     * @mbggenerated
     */
    public void setLeaveDate(String leaveDate) {
        this.leaveDate = leaveDate == null ? null : leaveDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_vacation_detail.leave_part
     *
     * @return the value of tb_vacation_detail.leave_part
     *
     * @mbggenerated
     */
    public String getLeavePart() {
        return leavePart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_vacation_detail.leave_part
     *
     * @param leavePart the value for tb_vacation_detail.leave_part
     *
     * @mbggenerated
     */
    public void setLeavePart(String leavePart) {
        this.leavePart = leavePart == null ? null : leavePart.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_vacation_detail.leave_typ
     *
     * @return the value of tb_vacation_detail.leave_typ
     *
     * @mbggenerated
     */
    public String getLeaveTyp() {
        return leaveTyp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_vacation_detail.leave_typ
     *
     * @param leaveTyp the value for tb_vacation_detail.leave_typ
     *
     * @mbggenerated
     */
    public void setLeaveTyp(String leaveTyp) {
        this.leaveTyp = leaveTyp == null ? null : leaveTyp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_vacation_detail.leave_day
     *
     * @return the value of tb_vacation_detail.leave_day
     *
     * @mbggenerated
     */
    public Double getLeaveDay() {
        return leaveDay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_vacation_detail.leave_day
     *
     * @param leaveDay the value for tb_vacation_detail.leave_day
     *
     * @mbggenerated
     */
    public void setLeaveDay(Double leaveDay) {
        this.leaveDay = leaveDay;
    }
}