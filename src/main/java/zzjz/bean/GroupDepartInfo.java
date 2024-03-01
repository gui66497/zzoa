package zzjz.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 汪浩
 * @version 2017/5/16.
 */
public class GroupDepartInfo {
    private long departId;

    private List<GroupDepart> list = new ArrayList<GroupDepart>();

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
     * getList.
     *
     * @return LIST
     */
    public List<GroupDepart> getList() {
        return list;
    }

    /**
     * setList.
     *
     * @param list LIST
     */
    public void setList(List<GroupDepart> list) {
        this.list = list;
    }
}
