package zzjz.bean;

/** 
* @ClassName: PagingEntity 
* @Description: 分页实体类
* @author 梅宏振
* @date 2015年2月27日 下午1:46:13
*/
public class PagingEntity {
	
	private int pageSize = 10;		//每页记录条数(默认每页十条)
	
	private int pageNo = 1;			//页码(从1开始)(默认显示第一页数据)
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
}
