package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Down;
import zzjz.bean.DownRequest;
import zzjz.bean.Staff;

import java.util.List;

/**
 * @author 房桂堂
 * @version 2017/9/6 8:36
 * @ClassName: DownService
 * @Description: 下载管理service
 */
@Service
public interface DownService {

    /**
     * 分页回去下载信息.
     *
     * @param request request
     * @return 下载列表
     */
    Page<Down> getDownPage(DownRequest request);

    /**
     * 新增下载信息.
     *
     * @param down Down
     * @return Down
     */
    Down addDownInfo(Down down);

    /**
     * 修改下载信息.
     *
     * @param down Down
     * @return boolean
     */
    boolean updateDownInfo(Down down);

    /**
     * 提交下载信息.
     *
     * @param down    Down
     * @param user    user
     * @param staffId staffId
     * @return boolean
     */
    boolean submitDownTask(Down down, String user, String staffId);

    /**
     * 删除下载信息.
     *
     * @param actId actId
     * @return boolean
     */
    boolean deleteRecordByActId(String actId);

    /**
     * 撤销下载信息.
     *
     * @param actId actId
     * @return boolean
     */
    boolean revokeRecordByActId(String actId);

    /**
     * 根据id获取下载信息.
     *
     * @param id id
     * @return Down
     */
    Down getDownInfoById(int id);

    /**
     * 获取下一处理人列表.
     *
     * @param down     Down
     * @param departId departId
     * @param staffId  staffId
     * @param msg      msg
     * @return List
     */
    List<Staff> getNextUser(Down down, long departId, String staffId, String msg);

    /**
     * 审批下载.
     *
     * @param down   Down
     * @param user   user
     * @param msg    msg
     * @param reason reason
     * @return boolean
     */
    boolean approveDownTask(Down down, String user, String msg, String reason);

    /**
     * 直接通过下载流程信息.
     *
     * @param down Down
     * @return boolean
     */
    boolean terminationDownTask(Down down);

    /**
     * 下载信息忽略提醒.
     *
     * @param id 主键
     * @return 结果
     */
    boolean ignore(Integer id);
}
