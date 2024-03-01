package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author guzhenggen
 * @version 2016/7/21 10:39
 * @ClassName: RoleAuthorityRequest
 * @Description: 这里用一句话描述这个类的作用
 */
@XmlRootElement
public class RoleAuthorityRequest extends BaseRequest {
    private String authorityName;

    /**
     * getAuthorityName.
     *
     * @return AUTHORITY_NAME
     */
    public String getAuthorityName() {
        return authorityName;
    }

    /**
     * setAuthorityName.
     *
     * @param authorityName AUTHORITY_NAME
     */
    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }
}
