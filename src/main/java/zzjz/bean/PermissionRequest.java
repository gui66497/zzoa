package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;


/** 
* @ClassName: PermissionRequest 
* @Description: 权限request
* @author 曹雪东
* @date 2015年5月9日 上午11:42:23
*/
@XmlRootElement
public class PermissionRequest extends BaseRequest {
	
	private String userId; //用户ID
	private String permissionName;//权限名称
	private String systemName;//系统名称
	private String systemSign;//系统标示
	private String targetId;//目标ID
	private long systemId;	//系统ID

	public long getSystemId() {
		return systemId;
	}

	public void setSystemId(long systemId) {
		this.systemId = systemId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemSign() {
		return systemSign;
	}

	public void setSystemSign(String systemSign) {
		this.systemSign = systemSign;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	
	
	

}
 