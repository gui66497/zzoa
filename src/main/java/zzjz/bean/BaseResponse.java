package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/** 
* @ClassName: ResponseEntity 
* @Description: REST接口通用响应类 
* @author 梅宏振
* @date 2015年2月27日 下午6:16:18
*/
@XmlRootElement
public class BaseResponse<T> {
	
	private ResultCode resultCode;	//结果状态码
	
	private List<T> data;			//数据集
	
	private List<Object> otherData;	//其它数据
	
	private String message;			//提示消息

	public ResultCode getResultCode() {
		return resultCode;
	}

	public void setResultCode(ResultCode resultCode) {
		this.resultCode = resultCode;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public List<Object> getOtherData() {
		return otherData;
	}

	public void setOtherData(List<Object> otherData) {
		this.otherData = otherData;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
