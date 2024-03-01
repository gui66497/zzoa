package zzjz;

import com.github.pagehelper.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import zzjz.bean.PagingEntity;
import zzjz.bean.Payment;
import zzjz.bean.PaymentRequest;
import zzjz.bean.Process;
import zzjz.service.PaymentService;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017/11/113.
 */
public class PaymentServiceTest extends JunitBaseTest {

    private static final Integer PAGE_SIZE = 50;
    @Autowired
    PaymentService paymentService;

    /**
     * 分页获取付款申请信息.
     */
    @Test
    @Rollback
    public void testGetPaymentPage() {
        PaymentRequest request = new PaymentRequest();
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageNo(1);
        pagingEntity.setPageSize(PAGE_SIZE);
        request.setPaging(pagingEntity);
        Page<Payment> list = paymentService.getPaymentPage(request);
        assert list != null;
    }

    /**
     * 新增付款信息.
     */
    @Test
    public void testAddPayment() {
        Payment payment = new Payment();
        payment.setId(1);
        payment.setAscription("666");
        payment.setAccount("6666");
        payment.setReason("xxx");
        payment.setName("666");
        payment.setPriceLower("12312");
        String rs = paymentService.addPayment(payment);
        assert !"".equals(rs);
    }

    /**
     * 修改付款信息.
     */
    @Test
    public void testUpdatePayment() {
        Payment payment = paymentService.getPaymentInfoById(1);
        boolean rs = paymentService.updatePayment(payment);
        assert rs;
    }

    /**
     * 提交付款信息.
     */
    @Test
    public void testSubmitPayment() {
        Payment payment = paymentService.getPaymentInfoById(1);
        boolean rs = paymentService.submitPayment(payment,"55");
        assert rs;
    }

    /**
     * 审批付款申请信息.
     */
    @Test
    public void testApprovePayment() {
        Payment payment = paymentService.getPaymentInfoById(1);
        boolean rs = paymentService.approvePayment(payment,"56","agree","同意");
        assert rs;
    }

    /**
     * 获取下一处理人.
     */
    @Test
    @Rollback
    public void testGetNextUser() {
        Payment payment = paymentService.getPaymentInfoById(1);
        List<Process> list = paymentService.getNextUser(payment,"agree");
        assert list != null;
    }

    /**
     * 通过主键查询付款信息.
     */
    @Test
    @Rollback
    public void testGetPaymentInfoById() {
        Payment payment = paymentService.getPaymentInfoById(1);
        assert payment != null;
    }

    /**
     * 删除付款申请信息.
     */
    @Test
    public void testDeleteRecordByActId() {
        Payment payment = paymentService.getPaymentInfoById(1);
        boolean rs = paymentService.deleteRecordByActId(payment.getActId());
        assert rs;
    }

}
