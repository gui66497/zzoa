package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.StaffAttribute;
import zzjz.bean.StaffAttributeRequest;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年12月14日15:04
 * @ClassName: StaffAttributeService
 * @Description: 人员属性信息服务接口
 */
@Service
public interface StaffAttributeService {

    /**
     * 分页获取人员属性信息.
     *
     * @param request 人员属性请求信息
     * @return 列表
     */
    Page<StaffAttribute> getStaffAttributePageList(StaffAttributeRequest request);

    /**
     * 获取人员属性列表.
     *
     * @param staffAttribute 人员属性信息
     * @return 列表
     */
    List<StaffAttribute> getStaffAttributeList(StaffAttribute staffAttribute);

    /**
     * 查询人员属性信息.
     *
     * @param id 主键
     * @return 人员属性信息
     */
    StaffAttribute query(Integer id);

    /**
     * 添加人员属性信息.
     *
     * @param staffAttribute 人员属性信息
     * @return 结果
     */
    boolean add(StaffAttribute staffAttribute);

    /**
     * 更新人员属性信息.
     *
     * @param staffAttribute 人员属性信息
     * @return 结果
     */
    boolean update(StaffAttribute staffAttribute);

    /**
     * 删除人员属性信息.
     *
     * @param id 主键
     * @return 结果
     */
    boolean delete(Integer id);
}
