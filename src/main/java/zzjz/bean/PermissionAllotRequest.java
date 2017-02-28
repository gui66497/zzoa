package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PermissionAllotRequest {

	private long systemId;
	private PagingEntity paging;

	public long getSystemId() {
		return systemId;
	}

	public void setSystemId(long systemId) {
		this.systemId = systemId;
	}

	public PagingEntity getPaging() {
		return paging;
	}

	public void setPaging(PagingEntity paging) {
		this.paging = paging;
	}

}
