package zzjz.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import zzjz.bean.Dic;
import zzjz.bean.DicRequest;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017-10-16
 */
@Repository
public interface DicMapper {
    /**
     * @param dic Dic
     * @return int
     */
    int deleteByPrimaryKey(Dic dic);

    /**
     * @param record Dic
     * @return int
     */
    int insert(Dic record);

    /**
     * @param record Dic
     * @return int
     */
    int insertSelective(Dic record);

    /**
     * @param dic Dic
     * @return Dic
     */
    Dic selectByPrimaryKey(Dic dic);

    /**
     * @param record Dic
     * @return int
     */
    int updateByPrimaryKeySelective(Dic record);

    /**
     * @param record Dic
     * @return int
     */
    int updateByPrimaryKey(Dic record);

    /**
     * @param dicId dicId
     * @return List
     */
    List<Dic> getDicByDicId(@Param("dicId") String dicId);

    /**
     * @param request DicRequest
     * @return List
     */
    List<Dic> getDicList(DicRequest request);

    /**
     * @return List
     */
    List<Dic> getDicTitleList();
}