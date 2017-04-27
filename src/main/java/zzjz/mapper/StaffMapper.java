package zzjz.mapper;

import org.apache.ibatis.annotations.Param;
import zzjz.bean.Staff;
import zzjz.bean.StaffExcel;
import zzjz.bean.StaffRequest;

import java.util.List;

public interface StaffMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_staff
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_staff
     *
     * @mbggenerated
     */
    int insert(Staff record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_staff
     *
     * @mbggenerated
     */
    int insertSelective(Staff record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_staff
     *
     * @mbggenerated
     */
    Staff selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_staff
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Staff record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_staff
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Staff record);

    /**
     * 分页获取员工列表信息
     * @return 员工列表
     */
    List<Staff> getStaffList(StaffRequest staff);

    int delByStaffId(@Param("staffId") long staffId);

    int addStaffList(@Param("res") List<StaffExcel> res);

    /**
     * 转正提醒，15天内转正
     * @return 员工列表
     */
    List<Staff> formalRemindList ();

    /**
     * 生日提醒，本月内生日
     * @return 员工列表
     */
    List<Staff> birthRemindList ();
}