package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * StaffGroupRequest.
 *
 * @author fgt
 * @version 2017/5/5 14:16
 */
@XmlRootElement
public class StaffGroupRequest {

    /**
     * 员工id
     */
    private String staffId;

    /**
     * 组id数组
     */
    private List<String> groupIdList;

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
     * getGroupIdList.
     *
     * @return GROUP_ID_LIST
     */
    public List<String> getGroupIdList() {
        return groupIdList;
    }

    /**
     * setGroupIdList.
     *
     * @param groupIdList GROUP_ID_LIST
     */
    public void setGroupIdList(List<String> groupIdList) {
        this.groupIdList = groupIdList;
    }
}
