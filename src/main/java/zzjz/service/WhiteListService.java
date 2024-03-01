package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.WhiteList;
import zzjz.bean.WhiteListRequest;

/**
 * @author 彭鹏
 * @version 2017年12月08日15:09
 * @ClassName: WhiteListService
 * @Description: 考勤白名单服务接口
 */
@Service
public interface WhiteListService {

    /**
     * 分页获取白名单列表信息.
     *
     * @param request request
     * @return 白名单列表信息
     */
    Page<WhiteList> getWhiteListPage(WhiteListRequest request);

    /**
     * 添加白名单信息.
     *
     * @param whiteList 白名单信息
     * @return 结果
     */
    boolean add(WhiteList whiteList);

    /**
     * 更新白名单信息.
     *
     * @param whiteList 白名单信息
     * @return 结果
     */
    boolean update(WhiteList whiteList);

    /**
     * 删除白名单信息.
     *
     * @param id 主键
     * @return 结果
     */
    boolean delete(Integer id);

    /**
     * 根据主键查询白名单信息.
     *
     * @param id 主键
     * @return 白名单信息
     */
    WhiteList getWhiteListInfoById(Integer id);

    /**
     * 根据员工号和类型查询白名单信息.
     *
     * @param userId    员工号
     * @param type      类型
     * @param checkDate 日期
     * @return 白名单信息
     */
    WhiteList getWhiteListInfoByUserIdAndType(String userId, String type, String checkDate);

}
