package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Depart;
import zzjz.bean.DepartRequest;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年03月31日9:49
 * @ClassName: DepartService
 * @Description: 部门服务接口
 */
@Service
public interface DepartService {
    /**
     * 分页获取部门列表信息.
     *
     * @param request request
     * @return 部门列表信息
     */
    Page<Depart> getDepartListPage(DepartRequest request);


    /**
     * 获取部门列表信息（树）.
     *
     * @return 部门列表信息
     */
    List<Depart> getDepartList();

    /**
     * 获取所有部门列表信息.
     *
     * @return 部门列表信息
     */
    List<Depart> getAllDepartList();


    /**
     * 新增部门信息.
     *
     * @param depart depart
     * @return 结果
     */
    boolean addDepart(Depart depart);

    /**
     * 更新机构信息.
     *
     * @param depart depart
     * @return boolean
     */
    boolean updateDepart(Depart depart);

    /**
     * 根据departId删除部门信息.
     *
     * @param departId 部门ID
     * @return 结果
     */
    boolean delByDepartId(Long departId);

    /**
     * 根据部门名称获取部门id.
     *
     * @param departName 部门名称
     * @return 部门id
     */
    String getDepartIdByName(String departName);

    /**
     * 查询机构信息.
     *
     * @param departId departId
     * @return Depart
     */
    Depart selectByPrimaryKey(long departId);
}
