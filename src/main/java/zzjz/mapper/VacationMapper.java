package zzjz.mapper;

import zzjz.bean.Vacation;
import zzjz.bean.VacationRequest;

import java.util.List;

/**
 * @author 彭鹏
 * @ClassName: VacationMapper
 * @Description:
 * @date 2017年04月13日16:20
 */
public interface VacationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_vacation
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_vacation
     *
     * @mbggenerated
     */
    int insert(Vacation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_vacation
     *
     * @mbggenerated
     */
    int insertSelective(Vacation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_vacation
     *
     * @mbggenerated
     */
    Vacation selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_vacation
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Vacation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_vacation
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Vacation record);

    /**
     * 分页获取出差列表信息
     * @param trip
     * @return 出差列表
     */
    List<Vacation> getVacationList(VacationRequest trip);

    /**
     * 清除事项信息
     */
    int deleteSelective(Vacation vacation);

    /**
     * 获取序列号
     */
    int actSeq();
}
