package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @ClassName: RoleAuthorityRequest
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author guzhenggen
 * @Date: 2016/7/21 10:39
 */
@XmlRootElement
public class RoleAuthorityRequest extends BaseRequest {
    private String authorityName;

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }
}
