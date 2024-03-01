package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author 汪浩
 * @version 2017/5/15.
 */
@XmlRootElement
public class GroupDepartRequest {
    private long id;

    private long departId;

    private List<String> groupList;

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
     * getGroupList.
     *
     * @return GROUP_LIST
     */
    public List<String> getGroupList() {
        return groupList;
    }

    /**
     * setGroupList.
     *
     * @param groupList GROUP_LIST
     */
    public void setGroupList(List<String> groupList) {
        this.groupList = groupList;
    }
}
