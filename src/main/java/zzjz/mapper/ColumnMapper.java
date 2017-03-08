package zzjz.mapper;

import zzjz.bean.Column;

import java.util.List;

public interface ColumnMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_column
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_column
     *
     * @mbggenerated
     */
    int insert(Column record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_column
     *
     * @mbggenerated
     */
    int insertSelective(Column record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_column
     *
     * @mbggenerated
     */
    Column selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_column
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Column record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_column
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Column record);

    /**
     * 获取所有列信息
     * @return 列信息
     */
    List<Column> getAllColumn();

    /**
     * 修改列信息
     * @param record 列
     * @return 结果
     */
    int updateByName(Column record);
}