package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/** 
* @ClassName: BaseRequest 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author 梅宏振
* @date 2015年3月4日 下午1:32:29
*/
@XmlRootElement
public class BaseRequest {
	
	private Long recordId;			//记录ID
	
	private PagingEntity paging;	//分页实体

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public PagingEntity getPaging() {
		return paging;
	}

	public void setPaging(PagingEntity paging) {
		this.paging = paging;
	}

}
 