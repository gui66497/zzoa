package zzjz.mapper;

import zzjz.bean.ActHis;

import java.util.List;

public interface ActHisMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_actinst
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_actinst
     *
     * @mbggenerated
     */
    int insert(ActHis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_actinst
     *
     * @mbggenerated
     */
    int insertSelective(ActHis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_actinst
     *
     * @mbggenerated
     */
    ActHis selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_actinst
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(ActHis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_actinst
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(ActHis record);

    List<ActHis> selectActListByActId(String ActId);
}