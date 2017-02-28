package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RoleRequest {

	private long systemId;
	private PagingEntity paging;
	
	private String roleId;
	private String permissionId;
	private String DataSource;	//数据源

	public String getDataSource() {
		return DataSource;
	}

	public void setDataSource(String dataSource) {
		DataSource = dataSource;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	public PagingEntity getPaging() {
		return paging;
	}

	public void setPaging(PagingEntity paging) {
		this.paging = paging;
	}

	public long getSystemId() {
		return systemId;
	}

	public void setSystemId(long systemId) {
		this.systemId = systemId;
	}

}
