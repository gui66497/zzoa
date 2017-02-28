package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/** 
* @ClassName: UserResponse 
* @Description: userResponse实体类
* @author 顾征根
* @date 2015年4月1日 下午3:36:04
*/
@XmlRootElement
public class UserRequest extends BaseRequest{
	
	private String authorId; // 作者ID 
	
	private String title;//标题
	
	private int statisticsType;//统计类型

	private String userName;	//用户名

	private String password;	//登陆密码

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getStatisticsType() {
		return statisticsType;
	}
	public void setStatisticsType(int statisticsType) {
		this.statisticsType = statisticsType;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
}
