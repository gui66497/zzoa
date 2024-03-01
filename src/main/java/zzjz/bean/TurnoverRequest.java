package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * TurnoverRequest.
 *
 * @author fgt
 * @version 2017/6/14 8:57
 */
@XmlRootElement
public class TurnoverRequest extends BaseRequest {

    private Integer id;

    private String staffId;

    private String staffName;

    private String type;

    private String action;

    private String oldValue;

    private String newValue;

    private String createTime;

    /**
     * 生效日期
     */
    private String effectTime;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 原部门
     */
    private String oldDepart;

    /**
     * 现部门
     */
    private String newDepart;

    /**
     * 原岗位
     */
    private String oldPosition;

    /**
     * 现岗位
     */
    private String newPosition;

    /**
     * 原职务
     */
    private String oldDuty;

    /**
     * 现职务
     */
    private String newDuty;

    /**
     * 导出用
     */
    private String exportStr;

    /**
     * get_id.
     *
     * @return ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * setId.
     *
     * @param id ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * getStaffId.
     *
     * @return STAFF_ID
     */
    public String getStaffId() {
        return staffId;
    }

    /**
     * setStaffId.
     *
     * @param staffId STAFF_ID
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    /**
     * getStaffName.
     * @return staffName
     */
    public String getStaffName() {
        return staffName;
    }

    /**
     * setStaffName.
     * @param staffName staffName
     */
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

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
     * getAction.
     *
     * @return ACTION
     */
    public String getAction() {
        return action;
    }

    /**
     * setAction.
     *
     * @param action ACTION
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * getOldValue.
     *
     * @return OLD_VALUE
     */
    public String getOldValue() {
        return oldValue;
    }

    /**
     * setOldValue.
     *
     * @param oldValue OLD_VALUE
     */
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    /**
     * getNewValue.
     *
     * @return NEW_VALUE
     */
    public String getNewValue() {
        return newValue;
    }

    /**
     * setNewValue.
     *
     * @param newValue NEW_VALUE
     */
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    /**
     * getCreateTime.
     *
     * @return CREATE_TIME
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * setCreateTime.
     *
     * @param createTime CREATE_TIME
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * getEffectTime.
     *
     * @return EFFECT_TIME
     */
    public String getEffectTime() {
        return effectTime;
    }

    /**
     * setEffectTime.
     *
     * @param effectTime EFFECT_TIME
     */
    public void setEffectTime(String effectTime) {
        this.effectTime = effectTime;
    }

    /**
     * getOldDepart.
     * @return oldDepart
     */
    public String getOldDepart() {
        return oldDepart;
    }

    /**
     * setOldDepart.
     * @param oldDepart oldDepart
     */
    public void setOldDepart(String oldDepart) {
        this.oldDepart = oldDepart;
    }

    /**
     *getNewDepart.
     * @return newDepart
     */
    public String getNewDepart() {
        return newDepart;
    }

    /**
     *setNewDepart.
     * @param newDepart newDepart
     */
    public void setNewDepart(String newDepart) {
        this.newDepart = newDepart;
    }

    /**
     *getOldPosition.
     * @return oldPosition
     */
    public String getOldPosition() {
        return oldPosition;
    }

    /**
     *setOldPosition.
     * @param oldPosition oldPosition
     */
    public void setOldPosition(String oldPosition) {
        this.oldPosition = oldPosition;
    }

    /**
     * getNewPosition.
     * @return newPosition
     */
    public String getNewPosition() {
        return newPosition;
    }

    /**
     *setNewPosition.
     * @param newPosition newPosition
     */
    public void setNewPosition(String newPosition) {
        this.newPosition = newPosition;
    }

    /**
     * getOldDuty.
     * @return oldDuty
     */
    public String getOldDuty() {
        return oldDuty;
    }

    /**
     *setOldDuty.
     * @param oldDuty oldDuty
     */
    public void setOldDuty(String oldDuty) {
        this.oldDuty = oldDuty;
    }

    /**
     * getNewDuty.
     * @return newDuty
     */
    public String getNewDuty() {
        return newDuty;
    }

    /**
     * setNewDuty.
     * @param newDuty newDuty
     */
    public void setNewDuty(String newDuty) {
        this.newDuty = newDuty;
    }

    /**
     * getStartTime.
     * @return startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * setStartTime.
     * @param startTime startTime
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * getEndTime.
     * @return endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * setEndTime.
     * @param endTime endTime
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * getExportStr.
     * @return exportStr
     */
    public String getExportStr() {
        return exportStr;
    }

    /**
     * setExportStr.
     * @param exportStr exportStr
     */
    public void setExportStr(String exportStr) {
        this.exportStr = exportStr;
    }
}
