package zzjz.mapper;

import zzjz.bean.Depart;
import zzjz.bean.DepartRequest;

import java.util.List;

public interface DepartMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_depart
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_depart
     *
     * @mbggenerated
     */
    int insert(Depart record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_depart
     *
     * @mbggenerated
     */
    int insertSelective(Depart record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_depart
     *
     * @mbggenerated
     */
    Depart selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_depart
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Depart record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_depart
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Depart record);



    /**
     * 分页获取部门列表信息
     * @return 部门列表
     */
    List<Depart> getDepartList(DepartRequest staff);
}