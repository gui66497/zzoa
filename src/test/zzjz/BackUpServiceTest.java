package zzjz;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import zzjz.service.BackUpService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 彭鹏
 * @version 2017/9/28.
 */
public class BackUpServiceTest extends JunitBaseTest {
    @Autowired
    BackUpService backUpService;

    /**
     * 测试备份.
     */
    @Test
    @Rollback()
    public void testBackup() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String time = sdf.format(new Date());
        boolean rs = backUpService.backup("E:\\backup\\backup_" + time + ".zip", new File("F:\\project"));
        assert rs;
    }
}
