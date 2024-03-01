package zzjz.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections.MapUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import zzjz.bean.PunchedCard;
import zzjz.mapper.ZkemUserMapper;
import zzjz.service.ZkemService;
import zzjz.util.DateUtil;
import zzjz.util.HttpUtil;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ZkemServiceImpl.
 *
 * @author Administrator
 * @version 2017/7/5 17:06
 */
@Service
public class ZkemServiceImpl implements ZkemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkemServiceImpl.class);

    private static final Integer INT4370 = 4370;

    /*@Value("${zkemIp1}")
    String zkemIp1;

    @Value("${zkemIp2}")
    String zkemIp2;

    @Value("${zkemPort}")
    int zkemPort;*/

    @Value("${zkemRest}")
    String zkemRest;

    @Autowired
    ZkemUserMapper zkemUserMapper;

    //todo 修改getLogsToday getLogAfter getOneDayLog这几个接口  改从数据库拿而不是直接调用打卡机

    @Override
    public List<PunchedCard> getLogsToday() throws ConnectException {
        Date todayZeroDate = DateUtil.getTodayZero();
        return getLogAfter(todayZeroDate);
    }

    /*@Override
    public List<PunchedCard> getLogAfter(Date date) {
        ZkemSDK sdk = new ZkemSDK();
        List<PunchedCard> punchedCards = new ArrayList<>();
        //恢复两个打卡机
        String[] zkemIpArr = new String[]{zkemIp1, zkemIp2};

        for (int i = 0; i < zkemIpArr.length; i++) {
            boolean connFlag = sdk.connect(zkemIpArr[i], zkemPort);
            if (connFlag) {
                List<PunchedCard> punchedCardsTemp;
                sdk.readLastestLogData(date);
                List<Map<String, Object>> res = sdk.getGeneralLogData();
                //断开连接
                sdk.disConnect();
                punchedCardsTemp = convert(res);
                punchedCards.addAll(punchedCardsTemp);
            } else {
                LOGGER.error("打卡机" + zkemIpArr[i] + "连接失败！");
                //throw new ConnectException("打卡机" + zkemIpArr[i] + "连接失败!");
            }
        }
        return punchedCards;
    }*/

    @Override
    public List<PunchedCard> getLogAfter(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(date);
        String restUrl = zkemRest + "zkem/queryAfter/" + dateStr;
        String res = HttpUtil.doGet(restUrl, null);
        Gson gson = new Gson();
        List<PunchedCard> punchedCardList = gson.fromJson(res, new TypeToken<List<PunchedCard>>() { }.getType());
        return punchedCardList;
    }

    @Override
    public List<PunchedCard> getOneDayLog(Date date) {
        List<PunchedCard> punchedCardList = getLogAfter(date);
        List<PunchedCard> oneDayPunchedCards = new ArrayList<>();
        //剔除当天之后的打卡记录
        for (PunchedCard punchedCard : punchedCardList) {
            String d = punchedCard.getYear() + "-" +
                    (punchedCard.getMonth().length() == 1 ? "0" + punchedCard.getMonth() : punchedCard.getMonth()) + "-" +
                    (punchedCard.getDay().length() == 1 ? "0" + punchedCard.getDay() : punchedCard.getDay());
            DateTime dateTime = new DateTime(date);
            if (d.equals(dateTime.toString("yyyy-MM-dd"))) {
                oneDayPunchedCards.add(punchedCard);
            }
        }
        return oneDayPunchedCards;
    }

    /*@Override
    public List<ZkUserInfo> getAllUserInfo() throws ConnectException {
        List<ZkUserInfo> zkUserList = new ArrayList<>();
        ZkemSDK sdk = new ZkemSDK();
        boolean connFlag1 = sdk.connect(zkemIp1, zkemPort);

        //连接打卡机1
        if (connFlag1) {
            List<Map<String, Object>> userList = sdk.getUserInfo();
            //断开连接1
            sdk.disConnect();
            List<ZkUserInfo> zkUserList1 = convertUserInfo(userList);
            zkUserList.addAll(zkUserList1);
        } else {
            LOGGER.error("打卡机1连接失败！");
            throw new ConnectException("打卡机1连接失败!");
        }

        //连接打卡机2
        boolean connFlag2 = sdk.connect(zkemIp2, zkemPort);
        if (connFlag2) {
            List<Map<String, Object>> userList = sdk.getUserInfo();
            //断开连接2
            sdk.disConnect();
            List<ZkUserInfo> zkUserList1 = convertUserInfo(userList);
            zkUserList.addAll(zkUserList1);
        } else {
            LOGGER.error("打卡机2连接失败！");
            throw new ConnectException("打卡机2连接失败!");
        }

        List<ZkUserInfo> uniqueZkUsers = new ArrayList<>();
        //过滤 取唯一
        for (ZkUserInfo zkUserInfo : zkUserList) {
            if (!zkContains(uniqueZkUsers, zkUserInfo)) {
                uniqueZkUsers.add(zkUserInfo);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            LOGGER.info(mapper.writeValueAsString(uniqueZkUsers));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uniqueZkUsers;
    }*/

    /*@Override
    public ZkUserInfo getZkUserInfoByUid(String userId) throws ConnectException {

        ZkemSDK sdk = new ZkemSDK();
        boolean connFlag1 = sdk.connect(zkemIp1, zkemPort);

        //连接打卡机1
        if (connFlag1) {
            List<Map<String, Object>> userList = sdk.getUserInfo();
            //断开连接1
            sdk.disConnect();
            List<ZkUserInfo> zkUserList1 = convertUserInfo(userList);

            for (ZkUserInfo zkUserInfo : zkUserList1) {
                if (userId.equals(zkUserInfo.getUid())) {
                    return zkUserInfo;
                }
            }

        } else {
            LOGGER.error("打卡机1连接失败！");
            throw new ConnectException("打卡机1连接失败!");
        }

        //连接打卡机2
        boolean connFlag2 = sdk.connect(zkemIp2, zkemPort);

        if (connFlag2) {
            List<Map<String, Object>> userList = sdk.getUserInfo();
            //断开连接2
            sdk.disConnect();
            List<ZkUserInfo> zkUserList1 = convertUserInfo(userList);

            for (ZkUserInfo zkUserInfo : zkUserList1) {
                if (userId.equals(zkUserInfo.getUid())) {
                    return zkUserInfo;
                }
            }

        } else {
            LOGGER.error("打卡机2连接失败！");
            throw new ConnectException("打卡机2连接失败!");
        }

        return null;
    }*/

    /*@Override
    public List<ZkUserInfo> getAllUserInfoLocal() {
        return zkemUserMapper.getAll();
    }*/

    @Override
    public String getUserIdByName(String name) {
        return zkemUserMapper.getUserIdByName(name);
    }

    /*@Override
    public String onConnect() {
        ZkemSDK sdk = new ZkemSDK();
        boolean connFlag2 = sdk.connect(zkemIp1, zkemPort);
        if (connFlag2) {
            sdk.regEvent();
            //如果是程序调用的这里能监控到OnConnected 事件
            sdk.connect("192.168.1.201", INT4370);

        }
        sdk.disConnect();
        return "";
    }*/

    /*private boolean zkContains(List<ZkUserInfo> zkUserInfos, ZkUserInfo zkUserInfo) {
        for (ZkUserInfo zkUser : zkUserInfos) {
            if (zkUser.getUid().equals(zkUserInfo.getUid())) {
                return true;
            }
        }
        return false;
    }*/

    /**
     * 转换打卡记录
     */
    private List<PunchedCard> convert(List<Map<String, Object>> res) {
        List<PunchedCard> punchedCards = new ArrayList<>();
        if (!CollectionUtils.isEmpty(res)) {
            for (Map<String, Object> map : res) {
                PunchedCard punchedCard = new PunchedCard();
                if (MapUtils.isNotEmpty(map)) {
                    punchedCard.setYear(String.valueOf(map.get("Year")));
                    punchedCard.setMonth(String.valueOf(map.get("Month")));
                    punchedCard.setDay(String.valueOf(map.get("Day")));
                    punchedCard.setHour(String.valueOf(map.get("Hour")));
                    punchedCard.setMinute(String.valueOf(map.get("Minute")));
                    punchedCard.setSecond(String.valueOf(map.get("Second")));
                    punchedCard.setUid(String.valueOf(map.get("EnrollNumber")));
                    punchedCard.setMode(String.valueOf(map.get("VerifyMode")));
                }
                punchedCards.add(punchedCard);
            }
        }
        return punchedCards;
    }

    /**
     * 转换用户信息
     */
    /*private List<ZkUserInfo> convertUserInfo(List<Map<String, Object>> res) {
        List<ZkUserInfo> zkUserInfoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(res)) {
            for (Map<String, Object> map : res) {
                ZkUserInfo zkUserInfo = new ZkUserInfo();
                if (MapUtils.isNotEmpty(map)) {
                    zkUserInfo.setName(String.valueOf(map.get("Name")));
                    zkUserInfo.setPassword(String.valueOf(map.get("Password")));
                    zkUserInfo.setPrivilege(String.valueOf(map.get("Privilege")));
                    zkUserInfo.setEnabled(String.valueOf(map.get("Enabled")));
                    zkUserInfo.setUid(String.valueOf(map.get("EnrollNumber")));
                }
                zkUserInfoList.add(zkUserInfo);
            }
        }
        return zkUserInfoList;
    }*/

}
