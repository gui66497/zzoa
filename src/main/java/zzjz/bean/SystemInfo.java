package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author 李飞
 * @version 2015年5月8日 上午8:44:47
 * @ClassName: SystemInfo
 * @Description: 这里用一句话描述这个类的作用
 */
@XmlRootElement
public class SystemInfo {

    /**
     * 主键ID
     *
     * @pdOid 56596045-2760-44fd-b7f1-ce7a17beffec
     */
    private long id;

    /**
     * 系统名称
     *
     * @pdOid 0fb4745a-4b03-4418-8411-24b1ccc296b3
     */
    private String name;

    /**
     * 系统标示
     *
     * @pdOid 96d02083-1193-41c1-987f-70da8e4c1586
     */
    private String sign;

    /**
     * 编辑者ID
     *
     * @pdOid 2d424817-7d4c-4496-8a46-6a7dcf087e21
     */
    private String creator;

    /**
     * 编辑时间
     *
     * @pdOid 299b7296-3db3-48e8-a6a2-e0024b249554
     */
    private String createTime = "0000-00-00 00:00:00";

    /**
     * 备注
     *
     * @pdOid b6ed5a20-bc6d-4422-9319-9eeb1358ec75
     */
    private String remark;

    /**
     * 系统数据源系统数据源：由表名，字段名，连接表用的数据库连接字串组成，格式
     * 表名;字段名,字段名,字段名,...;连接表用的数据库连接字串!表名;字段名,字段名,字段名,...;连接表用的数据库连接字串
     * !表名;字段名,字段名,字段名,...;连接表用的数据库连接字串!...!
     *
     * @pdOid 45e822d9-701b-4ca6-a7e1-3525de972ae4
     */
    private String dataSource;

    /**
     * 创建者姓名
     *
     * @pdOid 74a51e82-2cf9-4f92-aa5e-9a3609da6144
     */
    private String creatorName = "";

    /**
     * 删除标记，1：删除，0：未删除
     *
     * @pdOid f747f6ec-7a5f-484c-8a3d-dea2367bcc21
     */
    private int deleteFlag;

    /**
     * 按钮权限
     */
    private boolean addFalg;

    private boolean updateFlag;

    private boolean delFlag;

    private boolean showFlag;

    private List<DataSource> dataSources; //数据源列表

    /**
     * getDataSources.
     *
     * @return DATA_SOURCES
     */
    public List<DataSource> getDataSources() {
        return dataSources;
    }

    /**
     * setDataSources.
     *
     * @param dataSources DATA_SOURCES
     */
    public void setDataSources(List<DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    /**
     * getId.
     *
     * @return ID
     */
    public long getId() {
        return id;
    }

    /**
     * setId.
     *
     * @param id ID
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getName.
     *
     * @return NAME
     */
    public String getName() {
        return name;
    }

    /**
     * setName.
     *
     * @param name NAME
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getSign.
     *
     * @return SIGN
     */
    public String getSign() {
        return sign;
    }

    /**
     * setSign.
     *
     * @param sign SIGN
     */
    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * getCreator.
     *
     * @return CREATOR
     */
    public String getCreator() {
        return creator;
    }

    /**
     * setCreator.
     *
     * @param creator CREATOR
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * getCreateTime.
     *
     * @return CREATE_TIME
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * setCreateTime.
     *
     * @param createTime CREATE_TIME
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * getRemark.
     *
     * @return REMARK
     */
    public String getRemark() {
        return remark;
    }

    /**
     * setRemark.
     *
     * @param remark REMARK
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * getDataSource.
     *
     * @return DATA_SOURCE
     */
    public String getDataSource() {
        return dataSource;
    }

    /**
     * setDataSource.
     *
     * @param dataSource DATA_SOURCE
     */
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * getCreatorName.
     *
     * @return CREATOR_NAME
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * setCreatorName.
     *
     * @param creatorName CREATOR_NAME
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    /**
     * getDeleteFlag.
     *
     * @return DELETE_FLAG
     */
    public int getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * setDeleteFlag.
     *
     * @param deleteFlag DELETE_FLAG
     */
    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
    public String toString() {
        return "SystemInfo [systemId=" + id + ", systemName="
                + name + ", systemSign=" + sign + ", creator="
                + creator + ", createTime=" + createTime + ", remark=" + remark
                + ", dataSource=" + dataSource + ", creatorName=" + creatorName
                + ", deleteFlag=" + deleteFlag + "]";
    }

    /**
     * isAddFalg.
     *
     * @return ADD_FALG
     */
    public boolean isAddFalg() {
        return addFalg;
    }

    /**
     * setAddFalg.
     *
     * @param addFalg ADD_FALG
     */
    public void setAddFalg(boolean addFalg) {
        this.addFalg = addFalg;
    }

    /**
     * isDelFlag.
     *
     * @return DEL_FLAG
     */
    public boolean isDelFlag() {
        return delFlag;
    }

    /**
     * setDelFlag.
     *
     * @param delFlag DEL_FLAG
     */
    public void setDelFlag(boolean delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * isShowFlag.
     *
     * @return SHOW_FLAG
     */
    public boolean isShowFlag() {
        return showFlag;
    }

    /**
     * setShowFlag.
     *
     * @param showFlag SHOW_FLAG
     */
    public void setShowFlag(boolean showFlag) {
        this.showFlag = showFlag;
    }

    /**
     * isUpdateFlag.
     *
     * @return UPDATE_FLAG
     */
    public boolean isUpdateFlag() {
        return updateFlag;
    }

    /**
     * setUpdateFlag.
     *
     * @param updateFlag UPDATE_FLAG
     */
    public void setUpdateFlag(boolean updateFlag) {
        this.updateFlag = updateFlag;
    }
}
