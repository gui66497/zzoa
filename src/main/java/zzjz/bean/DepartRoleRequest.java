package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author 房桂堂
 *         create 2017-05-15 14:55
 * @version 1.0
 **/
@XmlRootElement
public class DepartRoleRequest extends BaseRequest {

    List<DepartRole> departRoles;

    long staffId;

    /**
     * getDepartRoles.
     *
     * @return DEPART_ROLES
     */
    public List<DepartRole> getDepartRoles() {
        return departRoles;
    }

    /**
     * setDepartRoles.
     *
     * @param departRoles DEPART_ROLES
     */
    public void setDepartRoles(List<DepartRole> departRoles) {
        this.departRoles = departRoles;
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
}
