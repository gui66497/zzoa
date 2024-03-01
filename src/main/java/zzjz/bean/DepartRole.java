package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 房桂堂
 *         create 2017-05-15 11:11
 * @version 1.0
 **/
@XmlRootElement
public class DepartRole {

    private int id;

    private long staffId;

    private long departId;

    private Depart depart;

    private String groupId;

    private String groupName;

    /**
     * 是否默认
     */
    private boolean checked;

    /**
     * getId.
     *
     * @return ID
     */
    public int getId() {
        return id;
    }

    /**
     * setId.
     *
     * @param id ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getStaffId.
     *
     * @return STAFF_ID
     */
    public long getStaffId() {
        return staffId;
    }

    /**
     * setStaffId.
     *
     * @param staffId STAFF_ID
     */
    public void setStaffId(long staffId) {
        this.staffId = staffId;
    }

    /**
     * getDepartId.
     *
     * @return DEPART_ID
     */
    public long getDepartId() {
        return departId;
    }

    /**
     * setDepartId.
     *
     * @param departId DEPART_ID
     */
    public void setDepartId(long departId) {
        this.departId = departId;
    }

    /**
     * getGroupId.
     *
     * @return GROUP_ID
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * setGroupId.
     *
     * @param groupId GROUP_ID
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * getDepart.
     *
     * @return DEPART
     */
    public Depart getDepart() {
        return depart;
    }

    /**
     * setDepart.
     *
     * @param depart DEPART
     */
    public void setDepart(Depart depart) {
        this.depart = depart;
    }

    /**
     * isChecked.
     *
     * @return CHECKED
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * setChecked.
     *
     * @param checked CHECKED
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * getGroupName.
     *
     * @return GROUP_NAME
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * setGroupName.
     *
     * @param groupName GROUP_NAME
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}

