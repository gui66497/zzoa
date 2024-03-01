package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 顾征根
 * @version 2015年4月1日 下午3:36:04
 * @ClassName: UserResponse
 * @Description: userResponse实体类
 */
@XmlRootElement
public class UserRequest extends BaseRequest {

    private String authorId; // 作者ID

    private String title; //标题

    private int statisticsType; //统计类型

    private String userName;    //用户名

    private String password;    //登陆密码

    /**
     * getUserName.
     *
     * @return USER_NAME
     */
    public String getUserName() {
        return userName;
    }

    /**
     * setUserName.
     *
     * @param userName USER_NAME
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * getPassword.
     *
     * @return PASSWORD
     */
    public String getPassword() {
        return password;
    }

    /**
     * setPassword.
     *
     * @param password PASSWORD
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * getTitle.
     *
     * @return TITLE
     */
    public String getTitle() {
        return title;
    }

    /**
     * setTitle.
     *
     * @param title TITLE
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * getStatisticsType.
     *
     * @return STATISTICS_TYPE
     */
    public int getStatisticsType() {
        return statisticsType;
    }

    /**
     * setStatisticsType.
     *
     * @param statisticsType STATISTICS_TYPE
     */
    public void setStatisticsType(int statisticsType) {
        this.statisticsType = statisticsType;
    }

    /**
     * getAuthorId.
     *
     * @return AUTHOR_ID
     */
    public String getAuthorId() {
        return authorId;
    }

    /**
     * setAuthorId.
     *
     * @param authorId AUTHOR_ID
     */
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
}
