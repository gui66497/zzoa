package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.PagingEntity;
import zzjz.bean.Payment;
import zzjz.bean.PaymentRequest;
import zzjz.bean.Process;
import zzjz.bean.ActHis;
import zzjz.mapper.PaymentMapper;
import zzjz.service.ActivitiService;
import zzjz.service.PaymentService;
import zzjz.service.SpecialActivitiService;
import zzjz.service.StaffService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * PaymentServiceImpl.
 *
 * @author 彭鹏
 * @version 2017/11/10 17:19
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentMapper paymentMapper;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    SpecialActivitiService specialActivitiService;

    @Autowired
    StaffService staffService;

    @Override
    public Page<Payment> getPaymentPage(PaymentRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        return (Page<Payment>) paymentMapper.getPaymentList(request);
    }

    @Override
    public String addPayment(Payment payment) {
        String userId = payment.getCreateUser();
        String actId = activitiService.flowStart("payment", userId);
        if (actId == null || "".equals(actId)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sdf.format(new Date());
        payment.setCreateTime(createTime);
        payment.setStatus("00");
        payment.setActId(actId);
        payment.setUserName(staffService.getStaffByUserId(userId).getName());
        if (paymentMapper.selectByPrimaryKey(payment.getId()) != null) {
            return paymentMapper.updateByPrimaryKey(payment) > 0 ? actId : "";
        } else {
            return paymentMapper.insert(payment) > 0 ? actId : "";
        }
    }

    @Override
    public boolean updatePayment(Payment payment) {
        return paymentMapper.updateByPrimaryKey(payment) > 0;
    }

    @Override
    public boolean submitPayment(Payment payment, String user) {
        payment = paymentMapper.selectByPrimaryKey(payment.getId());
        boolean rs = specialActivitiService.submitTask(payment.getActId(), user, "payment");
        if (rs) {
            payment.setStatus("01");
            rs = paymentMapper.updateByPrimaryKey(payment) > 0;
        }
        if (activitiService.isEndProcess(payment.getActId())) {
            payment.setStatus("99");
            rs = paymentMapper.updateByPrimaryKey(payment) > 0;
        }
        return rs;
    }

    @Override
    public boolean approvePayment(Payment payment, String user, String msg, String reason) {
        payment = paymentMapper.selectByPrimaryKey(payment.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("msg", msg);
        map.put("price", payment.getPriceLower().replaceAll(",", "").replaceAll("￥", "").split("\\.")[0]);
        boolean rs = specialActivitiService.approveTask(payment.getActId(), "payment", reason, map);
        if (rs && "agree".equals(msg)) {
            if (activitiService.isEndProcess(payment.getActId())) {
                payment.setStatus("99");
            } else {
                payment.setStatus("01");
            }
        } else if (rs && "disagree".equals(msg)) {
            if (activitiService.isApply(payment.getActId())) {
                payment.setStatus("03");
            } else {
                payment.setStatus("02");
            }
        }
        return paymentMapper.updateByPrimaryKey(payment) > 0;
    }

    @Override
    public List<Process> getNextUser(Payment payment, String msg) {
        payment = paymentMapper.selectByPrimaryKey(payment.getId());
        if ("agree".equals(msg)) {
            return specialActivitiService.getNextPaymentList(payment.getActId(), "payment", msg, "price",
                    Integer.parseInt(payment.getPriceLower().replaceAll(",", "").replaceAll("￥", "").split("\\.")[0]));
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean deleteRecordByActId(String actId) {
        Boolean rs;
        Payment payment = paymentMapper.queryByActId(actId);
        rs = paymentMapper.deleteByActId(actId);
        if ("00".equals(payment.getStatus())) {
            return rs && activitiService.deleteActinstByActId(actId);
        } else {
            return rs && activitiService.deleteRecordByActId(actId);
        }
    }

    @Override
    public Payment getPaymentInfoById(int id) {
        return paymentMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Map<String, Object>> getPaymentMapList(Payment payment) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            map.put("createTime", sdf1.format(sdf.parse(payment.getCreateTime()))); //申请日期
        } catch (ParseException e) {
            e.printStackTrace();
        }
        map.put("ascription", payment.getAscription()); //归属
        map.put("name", payment.getName()); //单位名称
        map.put("account", payment.getAccount()); //账号
        map.put("bank", payment.getBank()); //开户行
        map.put("reason", payment.getReason()); //付款事由
        map.put("priceUpper", payment.getPriceUpper()); //付款金额大写
        map.put("priceLower", payment.getPriceLower()); //付款金额小写

        List<ActHis> actHisList = activitiService.getActHisList(payment.getActId());
        String apply = "";
        String manager = "";
        String finance = "";
        String general1 = "";
        String general2 = "";
        String pay = "";
        String check = "";
        for (ActHis actHis : actHisList) {
            if (actHis.getAssignee() == null) {
                continue;
            }
            if ("apply".equals(actHis.getActId())) {
                apply = actHis.getAssignee(); //经办人
            }
            if ("manager".equals(actHis.getActId())) {
                manager = actHis.getAssignee(); //部门经理
            }
            if ("finance".equals(actHis.getActId())) {
                finance = actHis.getAssignee(); //财务审核
            }
            if ("general1".equals(actHis.getActId())) {
                general1 = actHis.getAssignee(); //总经理
            }
            if ("general2".equals(actHis.getActId())) {
                general2 = actHis.getAssignee(); //总经理
            }
            if ("pay".equals(actHis.getActId())) {
                pay = actHis.getAssignee(); //支付方式及制单
            }
            if ("check".equals(actHis.getActId())) {
                check = actHis.getAssignee(); //复核
            }
        }
        map.put("apply", apply);
        map.put("manager", manager);
        map.put("finance", finance);
        map.put("general", "".equals(general2) ? general1 : general2);
        map.put("pay1", pay);
        map.put("pay2", pay);
        map.put("check", check);

        list.add(map);
        return list;
    }

    @Override
    public boolean ignore(Integer id) {
        Payment payment = paymentMapper.selectByPrimaryKey(id);
        payment.setStatus("93");
        return paymentMapper.updateByPrimaryKey(payment) > 0;
    }
}
