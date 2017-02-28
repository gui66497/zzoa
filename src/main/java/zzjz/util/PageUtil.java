package zzjz.util;

import org.apache.log4j.Logger;
import zzjz.bean.PagingEntity;

import java.util.ArrayList;
import java.util.List;

/** 
* @ClassName: PageUtil 
* @Description: 在分页查询中的工具类 
* @author 李飞
* @date 2015年3月20日 上午11:04:01
*/
public class PageUtil {

	private static Logger log = Logger.getLogger(PageUtil.class);
	
	/** 
	* @Title: dealPaging
	* @Description: 根据分页信息和总记录数处理分页信息，并返回总条数和总页数，处理后的分页信息分装置原分页对象中
	* @param pagingEntity
	* @param totalCount
	* @return
	* @throws 
	*/
	public static List<Object> dealPaging(PagingEntity pagingEntity, int totalCount) {
		int pageNo = pagingEntity.getPageNo();
		int pageSize = pagingEntity.getPageSize();
		log.debug("传入的页数：" + pageNo + " 每页的大小：" + pageSize);
		//总页数
		int pageCount = totalCount/pageSize + (totalCount%pageSize == 0 ? 0 : 1);
		if (pageSize <= 0) {
			pageSize = 10;
		}
		log.debug("总页数：" + pageCount);
		if(pageNo > pageCount){
			pageNo = pageCount;
		}
		if (pageNo <= 0) {
			pageNo = 1;
		}
		//封装处理后的分页信息
		pagingEntity.setPageNo(pageNo);
		pagingEntity.setPageSize(pageSize);
		//封装总记录条数和总页数
		List<Object> otherData = new ArrayList<Object>();
		otherData.add(totalCount);
		otherData.add(pageCount);
		return otherData;
	}
}
