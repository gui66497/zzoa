package zzjz.mapper;

import zzjz.bean.TbMail;

/**
 * @ClassName: MailMapper
 * @Description: MailMapper
 * @author 房桂堂
 * @version  2018/3/1 9:01
 */
public interface MailMapper {

    /**
     * 删除.
     * @param id id
     * @return 结果
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入.
     * @param record 记录
     * @return 结果
     */
    int insert(TbMail record);

    /**
     * 插入.
     * @param record 记录
     * @return 结果
     */
    int insertSelective(TbMail record);

    /**
     * 查询.
     * @param id id
     * @return 结果
     */
    TbMail selectByPrimaryKey(Integer id);

    /**
     * 修改.
     * @param record 记录
     * @return 结果
     */
    int updateByPrimaryKeySelective(TbMail record);

    /**
     * 修改2.
     * @param record 记录
     * @return 结果
     */
    int updateByPrimaryKeyWithBLOBs(TbMail record);

    /**
     * 根据id修改.
     * @param record 记录
     * @return 结果
     */
    int updateByPrimaryKey(TbMail record);

    /**
     * 查找最新的一次邮件.
     * @param tbMail 记录
     * @return 结果
     */
    TbMail queryLatest(TbMail tbMail);
}