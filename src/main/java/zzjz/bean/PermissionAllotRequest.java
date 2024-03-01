package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 彭鹏
 * @version 2017年05月09日11:24
 */
@XmlRootElement
public class PermissionAllotRequest {

    private long systemId;

    private PagingEntity paging;

    /**
     * getSystemId.
     *
     * @return SYSTEM_ID
     */
    public long getSystemId() {
        return systemId;
    }

    /**
     * setSystemId.
     *
     * @param systemId SYSTEM_ID
     */
    public void setSystemId(long systemId) {
        this.systemId = systemId;
    }

    /**
     * getPaging.
     *
     * @return PAGING
     */
    public PagingEntity getPaging() {
        return paging;
    }

    /**
     * setPaging.
     *
     * @param paging PAGING
     */
    public void setPaging(PagingEntity paging) {
        this.paging = paging;
    }

}
