package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.PagingEntity;
import zzjz.bean.Talent;
import zzjz.bean.TalentRequest;
import zzjz.mapper.TalentMapper;
import zzjz.rest.TalentRest;
import zzjz.service.TalentService;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author 彭鹏
 * @version 2017/7/6
 */
@Service
public class TalentServiceImpl implements TalentService {
    private final static Logger LOGGER = LoggerFactory.getLogger(TalentServiceImpl.class);

    @Autowired
    TalentMapper talentMapper;

    @Override
    public Page<Talent> queryTalentPageList(TalentRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        if (request.getKeyword() != null && !"".equals(request.getKeyword())) {
            String[] arr = request.getKeyword().split(" ");
            int len = arr.length;
            StringBuffer keyword = new StringBuffer("");
            for (int i = 0; i < len; i++) {
                if (i != 0) {
                    keyword.append(" or keyword like '%" + arr[i] + "%'");
                } else {
                    keyword.append("( keyword like '%" + arr[i] + "%'");
                }
                keyword.append(" or word_desc like '%" + arr[i] + "%'");
            }
            keyword.append(")");
            request.setKeyword(keyword.toString());
        } else {
            request.setKeyword(null);
        }
        if (request.getType() != null && !"".equals(request.getType())) {
            String[] arr = request.getType().split(",");
            int len = arr.length;
            StringBuffer type = new StringBuffer("");
            for (int i = 0; i < len; i++) {
                if (i != 0) {
                    type.append("or type like '%" + arr[i] + "%'");
                } else {
                    type.append("( type like '%" + arr[i] + "%'");
                }
            }
            type.append(")");
            request.setType(type.toString());
        } else {
            request.setType(null);
        }
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Talent> pageList = (Page<Talent>) talentMapper.getTalentList(request);
        return pageList;
    }

    @Override
    public Talent queryTalentById(Integer id) {
        return talentMapper.selectByPrimaryKey(id);
    }

    @Override
    public Talent queryTalentByPhoneAndEmail(String phone, String email) {
        if (phone != null && "".equals(phone.trim())) {
            phone = null;
        }
        if (email != null && "".equals(email.trim())) {
            email = null;
        }
        if (phone == null && email == null) {
            return null;
        } else {
            return talentMapper.selectByPhoneAndEmail(phone, email);
        }
    }

    @Override
    public String addFile(String fileName, String source, String type, String keyword, String note) throws IOException {
        type = type.replaceAll("&", "/");
        if ("undefined".equals(keyword)) {
            keyword = null;
        }
        if ("undefined".equals(note)) {
            note = null;
        }

        if ("智联招聘".equals(source)) {
            return addZLZP(fileName, source, type, keyword, note);
        } else if ("前程无忧".equals(source)) {
            return addQCWY(fileName, source, type, keyword, note);
        } else if ("猎聘网".equals(source)) {
            return addLPW(fileName, source, type, keyword, note);
        } else {
            return "";
        }
    }

    private String addZLZP(String fileName, String source, String type, String keyword, String note) throws IOException {
        if (!"html".equals(fileName.split("\\.")[fileName.split("\\.").length - 1])) {
            return "";
        }
        String encoding = "utf-8";
        Talent talent = new Talent();
        StringBuffer sb = new StringBuffer("");
        File file = new File(fileName);
        InputStreamReader read = new InputStreamReader(
                new FileInputStream(file), encoding); //考虑到编码格式
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;
        while ((lineTxt = bufferedReader.readLine()) != null) {
            sb.append(lineTxt.replaceAll("\t", "").replaceAll("\n", "").replaceAll(" ", ""));
            if (lineTxt.indexOf("<div class=\"main-title-fl fc6699cc\">") != -1) {
                //20180615 用户名现在不在一行了 这里标记tag
                String userName = lineTxt.replace("<div class=\"main-title-fl fc6699cc\">", "").replace("</div>", "").replaceAll("\t", "");
                if (StringUtils.isBlank(userName)) {
                    lineTxt = bufferedReader.readLine();
                    talent.setUserName(lineTxt.replace("</div>", "").replaceAll("\t", ""));
                } else {
                    talent.setUserName(userName);
                }
            }
            if (lineTxt.indexOf("手机：") != -1) {
                talent.setPhone(lineTxt.split("手机：")[1].split("<br />")[0].replaceAll("\t", ""));
            }
            if (lineTxt.indexOf("E-mail：") != -1) {
                talent.setEmail(lineTxt.split("\"")[1].replace("mailto:", "").replaceAll("\t", ""));
            }
            if (lineTxt.indexOf("<div class=\"summary-top\">") != -1) {
                lineTxt = bufferedReader.readLine();
                lineTxt = bufferedReader.readLine();
                if (lineTxt != null) {
                    sb.append(lineTxt.replaceAll("\t", "").replaceAll("\n", "").replaceAll(" ", ""));
                    if (lineTxt.indexOf("男") != -1) {
                        talent.setSex("1");
                    } else {
                        talent.setSex("2");
                    }
                }
            }
            if (lineTxt != null && lineTxt.indexOf("期望工作地区：") != -1) {
                lineTxt = bufferedReader.readLine();
                if (lineTxt != null) {
                    sb.append(lineTxt.replaceAll("\t", "").replaceAll("\n", "").replaceAll(" ", ""));
                    talent.setArea(lineTxt.replace("<td>", "").replace("</td>", "").replaceAll("\t", ""));
                }
            }
        }
        bufferedReader.close();
        read.close();

        if (talent.getUserName() == null || "".equals(talent.getUserName())) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
        talent.setCreateTime(sdf.format(new Date()));
        talent.setWordDesc(sb.toString());
        talent.setResumeName(talent.getUserName() + "_" + sdf1.format(new Date()) + ".html");
        talent.setSource(source);
        talent.setType(type);
        talent.setKeyword(keyword);
        talent.setNote(note);
        Talent tmpTalent = queryTalentByPhoneAndEmail(talent.getPhone(), talent.getEmail());
        if (tmpTalent != null) {
            return "";
        }
        boolean rs = talentMapper.insert(talent) > 0;
        if (rs) {
            return talent.getResumeName();
        } else {
            return "";
        }
    }

    private String addQCWY(String fileName, String source, String type, String keyword, String note) {
        if (!"mht".equals(fileName.split("\\.")[fileName.split("\\.").length - 1])) {
            return "";
        }
        InputStream textStream = null;
        BufferedInputStream buff = null;
        BufferedReader br = null;
        Reader r = null;
        try {
            File file = new File(fileName);
            InputStream fis = new FileInputStream(file);
            Session mailSession = Session.getDefaultInstance(System.getProperties(), null);
            MimeMessage msg = new MimeMessage(mailSession, fis);
            Object content = msg.getContent();
            MimeMultipart mp = (MimeMultipart) content;
            MimeBodyPart bp = (MimeBodyPart) mp.getBodyPart(0);
            textStream = bp.getInputStream();
            buff = new BufferedInputStream(textStream);
            r = new InputStreamReader(buff, "gbk");
            br = new BufferedReader(r);
            String lineTxt = null;
            StringBuffer sb = new StringBuffer("");
            Talent talent = new Talent();
            while ((lineTxt = br.readLine()) != null) {
                sb.append(lineTxt.replaceAll("\t", "").replaceAll("\n", "").replaceAll(" ", ""));
                if (lineTxt.indexOf("<td class=\"name\" colspan=\"3\">") != -1) {
                    talent.setUserName(lineTxt.split("<td class=\"name\" colspan=\"3\">&nbsp;")[1].split("<span")[0]);
                }
                if (lineTxt.indexOf("width=\"20\" height=\"20\"></td><td valign=\"middle\" style=\"color:#000;white-space:nowrap;\">") != -1) {
                    talent.setPhone(lineTxt.split("width=\"20\" height=\"20\"></td><td valign=\"middle\" " +
                            "style=\"color:#000;white-space:nowrap;\">")[2].split("</td>")[0]);
                }
                if (lineTxt.indexOf("mailto:") != -1) {
                    talent.setEmail(lineTxt.split("mailto:")[1].split("\"")[0]);
                }
                if (lineTxt.indexOf("<img class=\"vam\" width=\"20\" height=\"20\" valign=\"bottom\" src=\"cid:") != -1) {
                    if ("男".equals(lineTxt.split("<img class=\"vam\" width=\"20\" height=\"20\" " +
                            "valign=\"bottom\" src=\"cid:")[1].split("\">")[1].split("<span")[0])) {
                        talent.setSex("1");
                    } else {
                        talent.setSex("2");
                    }
                }
                if (lineTxt.indexOf("地点：</td><td valign=\"top\" class=\"txt2\"><span class=\"tag\">") != -1) {
                    talent.setArea(lineTxt.split("地点：</td><td valign=\"top\" class=\"txt2\">" +
                            "<span class=\"tag\">")[1].split("</span>")[0].replaceAll("&emsp;", " "));
                }
            }
            br.close();
            r.close();
            textStream.close();
            fis.close();

            if (talent.getUserName() == null || "".equals(talent.getUserName())) {
                return "";
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
            talent.setCreateTime(sdf.format(new Date()));
            talent.setWordDesc(sb.toString());
            talent.setResumeName(talent.getUserName() + "_" + sdf1.format(new Date()) + ".mht");
            talent.setSource(source);
            talent.setType(type);
            talent.setKeyword(keyword);
            talent.setNote(note);
            Talent tmpTalent = queryTalentByPhoneAndEmail(talent.getPhone(), talent.getEmail());
            if (tmpTalent != null) {
                return "";
            }
            boolean rs = talentMapper.insert(talent) > 0;
            if (rs) {
                return talent.getResumeName();
            } else {
                return "";
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String addLPW(String fileName, String source, String type, String keyword, String note) throws IOException {
        if (!"html".equals(fileName.split("\\.")[fileName.split("\\.").length - 1])) {
            return "";
        }
        File file = new File(fileName);
        String encoding = "utf-8";
        Talent talent = new Talent();
        InputStreamReader read = new InputStreamReader(
                new FileInputStream(file), encoding); //考虑到编码格式
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;
        StringBuffer sb = new StringBuffer("");
        while ((lineTxt = bufferedReader.readLine()) != null) {
            sb.append(lineTxt.replaceAll("\t", "").replaceAll("\n", "").replaceAll(" ", ""));
            if (lineTxt.indexOf("姓名：") != -1) {
                lineTxt = bufferedReader.readLine();
                if (lineTxt != null) {
                    sb.append(lineTxt.replaceAll("\t", "").replaceAll("\n", "").replaceAll(" ", ""));
                    talent.setUserName(lineTxt.replace("<td data-selector=\"field-name\"><span class=\"init-data\">",
                            "").replace("</span></td>", "").replaceAll("\t", "").replaceAll(" ", ""));
                }
            }
            if (lineTxt != null && lineTxt.indexOf("手机号码：") != -1) {
                lineTxt = bufferedReader.readLine();
                if (lineTxt != null) {
                    sb.append(lineTxt.replaceAll("\t", "").replaceAll("\n", "").replaceAll(" ", ""));
                }
                lineTxt = bufferedReader.readLine();
                if (lineTxt != null) {
                    sb.append(lineTxt.replaceAll("\t", "").replaceAll("\n", "").replaceAll(" ", ""));
                    talent.setPhone(lineTxt.replace("<span class=\"init-data\">", "")
                            .replace("</span>", "").replaceAll("\t", "").replaceAll(" ", ""));
                }
            }
            if (lineTxt != null && lineTxt.indexOf("电子邮件：") != -1) {
                lineTxt = bufferedReader.readLine();
                if (lineTxt != null) {
                    sb.append(lineTxt.replaceAll("\t", "").replaceAll("\n", "").replaceAll(" ", ""));
                }
                lineTxt = bufferedReader.readLine();
                if (lineTxt != null) {
                    sb.append(lineTxt.replaceAll("\t", "").replaceAll("\n", "").replaceAll(" ", ""));
                    talent.setEmail(lineTxt.replace("<span class=\"init-data\">", "")
                            .replace("</span>", "").replaceAll("\t", "").replaceAll(" ", ""));
                }
            }
            if (lineTxt != null && lineTxt.indexOf("性别：") != -1) {
                lineTxt = bufferedReader.readLine();
                if (lineTxt != null) {
                    sb.append(lineTxt.replaceAll("\t", "").replaceAll("\n", "").replaceAll(" ", ""));
                    if (lineTxt.indexOf("男") != -1) {
                        talent.setSex("1");
                    } else {
                        talent.setSex("2");
                    }
                }
            }
            if (lineTxt != null && lineTxt.indexOf("期望地点：") != -1) {
                lineTxt = bufferedReader.readLine();
                if (lineTxt != null) {
                    sb.append(lineTxt.replaceAll("\t", "").replaceAll("\n", "").replaceAll(" ", ""));
                    talent.setArea(lineTxt.replace("<td>", "").replace("</td>", "").replaceAll("\t", "").replaceAll(" ", "").replaceAll(";", " "));
                }
            }
        }
        read.close();

        if (talent.getUserName() == null || "".equals(talent.getUserName())) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
        talent.setCreateTime(sdf.format(new Date()));
        talent.setWordDesc(sb.toString());
        talent.setResumeName(talent.getUserName() + "_" + sdf1.format(new Date()) + ".html");
        talent.setSource(source);
        talent.setType(type);
        talent.setKeyword(keyword);
        talent.setNote(note);
        Talent tmpTalent = queryTalentByPhoneAndEmail(talent.getPhone(), talent.getEmail());
        if (tmpTalent != null) {
            return "";
        }
        boolean rs = talentMapper.insert(talent) > 0;
        if (rs) {
            return talent.getResumeName();
        } else {
            return "";
        }
    }

    @Override
    public Talent addTalentInfo(Talent talent) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
        talent.setCreateTime(sdf.format(new Date()));

        String postfix = talent.getResumeName().substring(StringUtils.lastIndexOf(talent.getResumeName(), "."));
        talent.setResumeName(talent.getUserName() + "_" + sdf1.format(new Date()) + postfix);
        Talent tmpTalent = queryTalentByPhoneAndEmail(talent.getPhone(), talent.getEmail());
        if (tmpTalent != null) {
            return new Talent();
        }
        if (talentMapper.insert(talent) > 0) {
            return talent;
        } else {
            return new Talent();
        }
    }

    @Override
    public boolean updateTalentInfo(Talent talent) {
        return talentMapper.updateByPrimaryKey(talent) > 0;
    }

    @Override
    public boolean deleteTalentById(Integer id) {
        Talent talent = talentMapper.selectByPrimaryKey(id);
        Properties props = new Properties();
        try {
            props.load(TalentRest.class.getClassLoader().getResourceAsStream("props/system.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(props.getProperty("resume.path") + talent.getResumeName());
        if (file.exists()) {
            boolean rs = file.delete();
            LOGGER.info("删除文件：" + file + (rs == true ? "成功" : "失败"));
        }
        return talentMapper.deleteByPrimaryKey(id) > 0;
    }
}
