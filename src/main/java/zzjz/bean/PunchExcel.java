package zzjz.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @Description 打卡记录excel类
 * @Author 房桂堂
 * @Date 2019/10/30 15:27
 */
@ExcelTarget("punchExcel")
@XmlRootElement
public class PunchExcel implements java.io.Serializable {

    @Excel(name = "姓名")
    private String name;

    @Excel(name = "打卡时间", format = "yyyy/MM/dd", databaseFormat = "yyyy-MM-dd")
    private Date time;

    @Excel(name = "编号")
    private String num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
