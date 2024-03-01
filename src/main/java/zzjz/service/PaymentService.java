package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Payment;
import zzjz.bean.PaymentRequest;
import zzjz.bean.Process;

import java.util.List;
import java.util.Map;

/**
 * ProviderService.
 *
 * @author 彭鹏
 * @version 2017/11/10 17:12
 */
@Service
public interface PaymentService {

    /**
     * 分页获取付款列表信息.
     *
     * @param request request
     * @return 付款列表信息
     */
    Page<Payment> getPaymentPage(PaymentRequest request);

    /**
     * 新增付款信息.
     *
     * @param payment 付款信息
     * @return actId
     */
    String addPayment(Payment payment);

    /**
     * 修改付款信息.
     *
     * @param payment 付款信息
     * @return 结果
     */
    boolean updatePayment(Payment payment);

    /**
     * 提交付款信息.
     *
     * @param payment 付款信息
     * @param user    用户
     * @return 结果
     */
    boolean submitPayment(Payment payment, String user);

    /**
     * 审批付款信息.
     *
     * @param payment 付款信息
     * @param user    用户
     * @param msg     操作
     * @param reason  原因
     * @return 结果
     */
    boolean approvePayment(Payment payment, String user, String msg, String reason);

    /**
     * 获取下一处理人.
     *
     * @param payment 付款信息
     * @param msg     操作
     * @return 列表
     */
    List<Process> getNextUser(Payment payment, String msg);

    /**
     * 删除付款信息.
     *
     * @param actId actId
     * @return 结果
     */
    boolean deleteRecordByActId(String actId);

    /**
     * 通过主键查询付款信息.
     *
     * @param id 主键
     * @return 付款信息
     */
    Payment getPaymentInfoById(int id);

    /**
     * 付款申请单信息导出内容.
     *
     * @param payment Payment
     * @return List
     */
    List<Map<String, Object>> getPaymentMapList(Payment payment);

    /**
     * 忽略付款提醒.
     *
     * @param id 主键
     * @return 结果
     */
    boolean ignore(Integer id);
}
