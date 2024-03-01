package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Dic;
import zzjz.bean.DicRequest;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年10月17日9:49
 * @ClassName: DicService
 * @Description: 字典项服务接口
 */
@Service
public interface DicService {
    /**
     * 根据字典项编号获取字典项信息.
     *
     * @param dicId 主键
     * @return 列表
     */
    List<Dic> geDicByDicId(String dicId);

    /**
     * 分页获取字典项信息列表.
     *
     * @param request DicRequest
     * @return Page
     */
    Page<Dic> getDicPageList(DicRequest request);

    /**
     * 新增字典项信息.
     *
     * @param dic Dic
     * @return boolean
     */
    boolean addDicInfo(Dic dic);

    /**
     * 修改字典项信息.
     *
     * @param dic Dic
     * @return boolean
     */
    boolean updateDicInfo(Dic dic);

    /**
     * 删除字典项信息.
     *
     * @param dicId  dicId
     * @param dicKey dicKey
     * @return boolean
     */
    boolean deleteDicInfo(String dicId, String dicKey);

    /**
     * 查询字典项所有ID.
     *
     * @return List
     */
    List<Dic> getDicTitleList();
}
