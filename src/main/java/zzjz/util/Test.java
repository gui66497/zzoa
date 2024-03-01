package zzjz.util;

//import cern.colt.Arrays;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.net.SyslogAppender;
import org.apache.logging.log4j.core.layout.SyslogLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类标题. <br>
 * 类详细说明.
 * Copyright: Copyright (c) 2018/5/9
 * Company: 上海直真君智科技有限公司
 *
 * @author mingqing.wang@zzjunzhi.com
 * @version 1.0.0
 */
public class Test {
    public static void main(String[] args) {
        Map<Integer, List<String>> logs = new HashMap<>(20);
        List<String> glq = new ArrayList<>();
        glq.add("2015-11-19 13:26:3 SAW004 audit1 2015-11-19 09:26:02 192.168.200.135 sysadm WEB界面登录成功 『用户名：sysadm』");
        glq.add("2015-10-11 16:49:4 SAW004 status.log <disk> 55% used </disk>");
        glq.add("2015-10-11 16:49:4 SAW004 status.log <cpu> 9% used </cpu>");
        glq.add("2015-10-11 16:49:4 SAW004 status.log <mem> 23% used </mem>");
        List<String> jns_firewall = new ArrayList<>();
        /*jns_firewall.add("2015-10-11 16:23:5 SAW005     [NEW] udp      17 30 src=192.168.201.149 dst=192.168.201.177 sport=51265 dport=514 [UNREPLIED] src=192.168.201.177 dst=192.168.201.149 sport=514 dport=51265");
        jns_firewall.add("2015-10-11 16:23:5 SAW005 [DESTROY] udp      17 src=192.168.201.149 dst=192.168.201.177 sport=44137 dport=514 packets=3 bytes=894 src=192.168.201.177 dst=192.168.201.149 sport=514 dport=44137 packets=0 bytes=0");
        jns_firewall.add("2015-10-11 16:23:5 SAW005 filterdrop Oct 11 16:23:05 SWfirewall kernel: [10134622.144137] filterdrop IN=eth0 OUT= MAC=ff:ff:ff:ff:ff:ff:6c:92:bf:17:b5:38:08:00 SRC=0.0.0.0 DST=255.255.255.255 LEN=576 TOS=0x00 PREC=0x00 TTL=64 ID=0 PROTO=UDP SPT=68 DPT=67 LEN=556");
        jns_firewall.add("2015-11-24 10:7:18 SAW005 audit1 2014/04/24 10:07:11 192.168.200.156 admin 日志配置 设置 成功 serverIP: 192.168.201.110 serverPort: 514 timeout: 180 logMail: 192.168.201.110");
        jns_firewall.add("2015-10-11 16:23:4 SAW005 status.log <cpu> 7% used </cpu>");
        jns_firewall.add("2015-10-11 16:23:4 SAW005 status.log <disk> 67% used </disk>");
        jns_firewall.add("2015-10-11 16:23:4 SAW005 status.log <mem> 11% used </mem>");*/
        jns_firewall.add("May 23 08:52:07 firewall kernel: filteralert a tcp packet(192.168.0.82: 139->192.168.121.1: 1841) was denied!");
        //jns_firewall.add("May 23 08:45:09 firewall kernel: 10 - tcp(2181) 11.39.159.186 11.39.143.188 48 0");

        List<String> zkww_ids = new ArrayList<>();
        zkww_ids.add("srcip=192.168.1.3 dstip=192.168.1.1 sport=333 dport=222 proto=TCP eventname=ahah seclevel=1 action=asd hitcount=11 sigid=1 groupid=12 user=admin policyid=123");
        List<String> bxy_nwzdjg = new ArrayList<>();
        //bxy_nwzdjg.add("'AUD*|17*|计算机关机*|2015-10-11 15:40:02*|192.168.222.223*|7427EA1E2E50*|WJA-PC*|*|WJA*|*|*|*|*|480*|终端设置策略*|关机");
        /*bxy_nwzdjg.add("STA*|142*|10*|11.24*|107*|89*|0*|0*|0*|0*|1*|1*|0*|10*|0*|3*|1");
        bxy_nwzdjg.add("'AUD*|17*|计算机关机*|2015-10-11 15:40:02*|192.168.200.206*|7427EA1E2E50*|WJA-PC*|*|WJA*|*|*|*|*|480*|终端设置策略*|关机");
        bxy_nwzdjg.add("AUD*|17*|计算机开机*|2015-10-11 15:41:57*|192.168.200.206*|7427EA1E2E50*|WJA-PC*|*|WJA*|*|*|*|*|480*|终端设置策略*|开机");
        bxy_nwzdjg.add("LOG*|admin*|2015-10-11 15:37:33*|192.168.200.206*|登录系统");
        bxy_nwzdjg.add("LOG*|admin*|2015-10-11 15:40:53*|192.168.200.206*|策略状态由:启动->停止");
        bxy_nwzdjg.add("WAR*|admin*|2015-10-11 15:40:53*|192.168.200.206*|策略状态由:启动->停止");
        bxy_nwzdjg.add("LOG*|admin*|2015-10-11 15:42:01*|192.168.200.206*|修改策略内容:由<br><?xml version=\"1.0\" encoding=\"gb2312\"?><vrvscript PolicyName=\"IP与MAC绑定策略\" Class=\"IPMAC-BIND-CONTROL\" AuditTypeBigNum=\"\" AuditTypeNum=\"\" Priority=\"1\" StartPolicy=\"1\" PolicyVersion=\"1.0\" PolicyRiskLevel=\"0\" ControlRegionMode=\"129\" NetValidMode=\"0\" outline=\"0\" CloningMachineValidMode=\"0\" UserValidMode=\"0\" ExceptUser=\"\" GatewayValidMode=\"0\" ExceptGateway=\"\" ImportPolicyNotStop=\"0\" Remark=\"\" FourceUseFatherDeal=\"0\"><item IntervalTime=\"1\" SpringMode=\"5\" IPGetDHCPMode=\"1\" IPCombineMAC=\"1\" CombineGateWay=\"0\" IPMACDealMode=\"1\" AutoResumeMaskAndGateway=\"1\" IPMACContinueAttack=\"0\" PersistAttack=\"0\" AttackWSTVPN=\"0\" IPMACInfo1=\"IP与MAC绑定\" IPMACInfo2=\"断网\" IPMACInfo4=\"\" LimitVLAN=\"0\" IPMACCheckTime=\"60\" AvoidARPAttack=\"0\" AntiIPConflict=\"1\" IPMACBindValidIP=\"\" DisableSecondaryETH=\"0\" NotDealOnChangedInSubNetwork=\"0\" /></vrvscript><br>修改为:<br><?xml version=\"1.0\" encoding=\"gb2312\"?><vrvscript PolicyName=\"IP与MAC绑定策略\" Class=\"IPMAC-BIND-CONTRO");*/
        bxy_nwzdjg.add("STA*|1788*|822*|58.67*|500*|1401*|0*|0*|0*|3*|248*|40*|0*|448*|18*|219*|0");
        bxy_nwzdjg.add("PEXEC*|2954836084*|19*|移动存储审计*|401*|2018-05-29 01:39:49");
        bxy_nwzdjg.add("PEXEC*|699185792*|9*|IP与MAC绑定策略*|808*|2018-05-29 01:41:49");
        bxy_nwzdjg.add("WAR*|0*|IP绑定变化*|2015-10-11 15:51:58*|192.168.200.206*|7427EA1E2E50*|WJA-PC*|*|WJA*|*|*|*|*|480*|*|IP地址由 192.168.200.206,192.168.1.206, 改变为 192.168.200.206");
        bxy_nwzdjg.add("WAR*|0*|探头被卸载*|2015-05-23 10:51:58*|11.39.159.15*|6C0B84DF242D*|GINNY-PC*|*|八处210室*|安甲*|八处*|处直*|844106*|1048*|*|扫描报警,此计算机曾经安装过探头,此时已经被卸载了");
        bxy_nwzdjg.add("AUD*|6*|打印文件输出*|2018-06-13 02:40:02*|192.168.200.206*|7427EA1E2E50*|61486-684E0F725*|指挥楼*|龙再勇:Administrator*|参谋部*|部队管理科*|840147*|*|534*|打印功能*|审计打印文件:Microsoft Word - 保障部");

        List<String> zkww_firewall = new ArrayList<>();
        zkww_firewall.add("<14>May 17 23:02:30 NPFW NPNetMonitor: TYPE=NPFW_5.0_03 MODE=1 LIP=192.168.0.56 RBS=0 SBS=48 RPS=0 SPS=1 LST=1305644550 LRT=0 RPS=0 SPS=1 LST=1305644550 LRT=0");
        zkww_firewall.add("<6> Aug  3 16:19:24 NPFW TYPE=NPFW_5.0_01 TARGET=0 MOD=2 IN=eth0 OUT= SRC=192.168.1.137 DST=192.168.1.255 LEN=78 TOS=0x00 PREC=0x00 TTL=64 ID=32034 PROTO=UDP SPT=137 DPT=137 LEN=58");

        logs.put(5140, bxy_nwzdjg);
        //logs.put(5144, jns_firewall);
        //logs.put(10012, glq);
        //logs.put(10014, zkww_firewall);
        //logs.put(10015, zkww_ids);

        for (Map.Entry<Integer, List<String>> entry : logs.entrySet()) {
            Logger logger = Logger.getLogger("a");
            Appender appender = new SyslogAppender();
            ((SyslogAppender) appender).setSyslogHost("192.168.1.129:" + entry.getKey());
            logger.addAppender(appender);
            for (String s : entry.getValue()) {
                logger.info(s);
            }
        }
    }
}
