package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * GroupRequest.
 *
 * @author 房桂堂
 * @version 2017/5/4 16:45
 */
@XmlRootElement
public class GroupRequest {

    private String groupId;

    private String groupName;

    private String groupType;

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

    /**
     * getGroupType.
     *
     * @return GROUP_TYPE
     */
    public String getGroupType() {
        return groupType;
    }

    /**
     * setGroupType.
     *
     * @param groupType GROUP_TYPE
     */
    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }
}
