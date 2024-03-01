package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.AptitudeBorrow;
import zzjz.bean.AptitudeBorrowRequest;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年12月14日15:04
 * @ClassName: AptitudeBorrowService
 * @Description: 资质证书借阅信息服务接口
 */
@Service
public interface AptitudeBorrowService {

    /**
     * 分页获取资质证书借阅信息.
     *
     * @param request 资质证书借阅请求信息
     * @return 列表
     */
    Page<AptitudeBorrow> getAptitudeBorrowPageList(AptitudeBorrowRequest request);

    /**
     * 获取资质证书借阅列表.
     *
     * @param aptitudeBorrow 资质证书借阅信息
     * @return 列表
     */
    List<AptitudeBorrow> getAptitudeBorrowList(AptitudeBorrow aptitudeBorrow);

    /**
     * 查询资质证书借阅信息.
     *
     * @param id 主键
     * @return 资质证书借阅信息
     */
    AptitudeBorrow query(Integer id);

    /**
     * 添加资质证书借阅信息.
     *
     * @param aptitudeBorrow 资质证书借阅信息
     * @return 结果
     */
    boolean add(AptitudeBorrow aptitudeBorrow);

    /**
     * 更新资质证书借阅信息.
     *
     * @param aptitudeBorrow 资质证书借阅信息
     * @return 结果
     */
    boolean update(AptitudeBorrow aptitudeBorrow);

    /**
     * 删除资质证书借阅信息.
     *
     * @param id 主键
     * @return 结果
     */
    boolean delete(Integer id);
}
