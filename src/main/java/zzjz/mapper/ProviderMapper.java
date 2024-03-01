package zzjz.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import zzjz.bean.Provider;
import zzjz.bean.ProviderRequest;

import java.util.List;

/**
 * @author 房桂堂
 * @version 2017/11/2 9:36
 * @Description: 供应商mapper
 * @ClassName: ProviderMapper
 */
@Repository
public interface ProviderMapper {

    /**
     * 插入.
     *
     * @param record record
     * @return 结果
     */
    int insert(Provider record);

    /**
     * 插入.
     *
     * @param record record
     * @return 结果
     */
    int insertSelective(Provider record);

    /**
     * 分页获取供应商列表信息.
     *
     * @param provider provider
     * @return 供应商列表
     */
    List<Provider> getProviderList(ProviderRequest provider);

    /**
     * 更新.
     *
     * @param record record
     * @return 结果
     */
    int updateByPrimaryKey(Provider record);

    /**
     * 查询.
     *
     * @param id 主键
     * @return 供应商信息
     */
    Provider selectByPrimaryKey(int id);

    /**
     * 根据流程号查询供应商信息.
     *
     * @param actId 流程号
     * @return 供应商信息
     */
    Provider queryByActId(@Param("actId") String actId);

    /**
     * 根据流程号删除供应商信息.
     *
     * @param id 主键
     * @return 结果
     */
    Boolean deleteProviderById(@Param("id") Integer id);

    /**
     * 批量新增供应商信息.
     *
     * @param list 供应商
     * @return 结果
     */
    boolean batchInsert(List<Provider> list);

    /**
     * 获取所有供应商信息.
     *
     * @return List
     */
    List<Provider> getAllProviderList();
}