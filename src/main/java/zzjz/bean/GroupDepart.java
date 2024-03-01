package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 汪浩
 * @version 2017/5/15.
 */
@XmlRootElement
public class GroupDepart {
    private long id;

    private long departId;

    private String groupId;

    private String groupName;

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
     * getId.
     *
     * @return ID
     */
    public long getId() {
        return id;
    }

    /**
     * setId.
     *
     * @param id ID
     */
    public void setId(long id) {
        this.id = id;
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
}
