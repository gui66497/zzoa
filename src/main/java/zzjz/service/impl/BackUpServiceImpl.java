package zzjz.service.impl;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import zzjz.bean.Bean;
import zzjz.service.BackUpService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author 彭鹏
 * @version 2017年04月06日14:31
 * @ClassName: BackUpServiceImpl
 * @Description: 备份实现类
 */
@Service
public class BackUpServiceImpl implements BackUpService {
    private final static Logger LOGGER = LoggerFactory.getLogger(BackUpServiceImpl.class);

    @Override
    public boolean backup(String backFile, File file) {
        try {
            zipFile(backFile, file);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean mysqlBackUp(String mysqlPath, String mysqlBackPath, String jdbcUsername, String jdbcPassword) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(mysqlPath + "\\mysqldump --default-character-set=utf8 -u" +
                    jdbcUsername + " -p" + jdbcPassword + " zzoa --hex-blob");
            //得到输入流，写成.dump文件
            InputStream inputStream = process.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream, "utf8");
            BufferedReader br = new BufferedReader(reader);
            String s;
            StringBuffer sb = new StringBuffer();
            while ((s = br.readLine()) != null) {
                sb.append(s + "\r\n");
            }
            if (!new File(mysqlBackPath).exists()) {
                boolean rs = new File(mysqlBackPath).mkdir();
                LOGGER.info("创建文件：" + mysqlBackPath + (rs == true ? "成功" : "失败"));
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            File file = new File(mysqlBackPath + "\\zzoa_" + sdf.format(new Date()) + ".dump");
            boolean rs = file.getParentFile().mkdirs();
            LOGGER.info("创建文件夹：" + mysqlBackPath + (rs == true ? "成功" : "失败"));
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(sb.toString().getBytes("utf8"));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            }
            br.close();
            reader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean mysqlReduce(String fileName, String mysqlPath, String mysqlBackPath, String jdbcUsername, String jdbcPassword) {
        try {
            Runtime run = Runtime.getRuntime();
            Process process = run.exec(mysqlPath + "\\mysql -u" + jdbcUsername + " -p" + jdbcPassword + " zzoa ");
            OutputStream outputStream = process.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(mysqlBackPath + "\\" + fileName), "gbk"));
            String str = null;
            StringBuffer sb = new StringBuffer();
            while ((str = br.readLine()) != null) {
                sb.append(str + "\r\n");
            }
            str = sb.toString();
            OutputStreamWriter writer = new OutputStreamWriter(outputStream,
                    "utf-8");
            writer.write(str);
            writer.flush();
            outputStream.close();
            br.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Bean> getFileList(String mysqlBackPath) {
        File f = new File(mysqlBackPath);
        if (!f.exists()) {
            return null;
        } else {
            List<Bean> list = new ArrayList<>();
            File[] fa = f.listFiles();
            if (fa == null) {
                return null;
            }
            for (int i = 0; i < fa.length; i++) {
                File fs = fa[i];
                if (!fs.isDirectory() && fs.getName().contains("zzoa_") && fs.getName().contains(".dump")) {
                    Bean bean = new Bean();
                    bean.setCol(fs.getName());
                    list.add(bean);
                }
            }
            return list;
        }
    }

    private void zipFile(String zipFileName, File inputFile) throws Exception {
        /*System.out.println("压缩中...");*/
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                zipFileName));
        BufferedOutputStream bo = new BufferedOutputStream(out);
        zip(out, inputFile, inputFile.getName(), bo);
        out.setEncoding("utf-8");
        bo.close();
        out.close(); // 输出流关闭
        /*System.out.println("压缩完成");*/
    }

    private void zip(ZipOutputStream out, File f, String base,
                     BufferedOutputStream bo) throws Exception { // 方法重载
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            if (fl != null) {
                if (fl.length == 0) {
                    out.putNextEntry(new ZipEntry(base + "/")); // 创建zip压缩进入点base
                }
                for (int i = 0; i < fl.length; i++) {
                    zip(out, fl[i], base + "/" + fl[i].getName(), bo); // 递归遍历子文件夹
                }
            }
        } else {
            out.putNextEntry(new ZipEntry(base)); // 创建zip压缩进入点base
            /*System.out.println(base);*/
            FileInputStream in = new FileInputStream(f);
            BufferedInputStream bi = new BufferedInputStream(in);
            int b;
            while ((b = bi.read()) != -1) {
                bo.write(b); // 将字节流写入当前zip目录
            }
            bi.close();
            in.close(); // 输入流关闭
        }
    }
}
