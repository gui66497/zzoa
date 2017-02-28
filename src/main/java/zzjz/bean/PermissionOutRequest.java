package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @ClassName: PermissionOutRequest
 * @Description: 权限请求实体
 * @author 房桂堂
 * @date 2016/7/26 13:41
 */
@XmlRootElement
public class PermissionOutRequest extends BaseRequest {

    private String userId;  //用户ID
    private long systemId;	//系统ID

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getSystemId() {
        return systemId;
    }

    public void setSystemId(long systemId) {
        this.systemId = systemId;
    }
}
