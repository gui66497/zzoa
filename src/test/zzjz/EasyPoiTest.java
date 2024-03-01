package zzjz;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zzjz.bean.Out;
import zzjz.bean.Over;
import zzjz.bean.OverOff;
import zzjz.bean.Punch;
import zzjz.bean.Trip;
import zzjz.bean.Vacation;
import zzjz.service.AnnualService;
import zzjz.service.OutService;
import zzjz.service.OverOffService;
import zzjz.service.OverService;
import zzjz.service.PunchService;
import zzjz.service.TripService;
import zzjz.service.VacationService;
import zzjz.util.Office2PDF;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author 房桂堂
 * @version 2017/12/12 14:27
 */
public class EasyPoiTest extends JunitBaseTest {

    @Autowired
    OverService overService;

    @Autowired
    PunchService punchService;

    @Autowired
    VacationService vacationService;

    @Autowired
    AnnualService annualService;

    @Autowired
    OverOffService overOffService;

    @Autowired
    OutService outService;

    @Autowired
    TripService tripService;

    private static final int OVER_ID = 568;

    private static final int PUNCH_ID = 591;

    private static final int VACATION_ID = 592;

    private static final int OUT_ID = 578;

    private static final int TRIP_ID = 581;

    /**
     * 加班信息导出excel测试.
     */
    @Test
    public void testOverExport() {
        Over over = overService.getOverInfoById(OVER_ID);
        Map<String, Object> map = overService.getOverMapList(over);
        try {
            TemplateExportParams params = new TemplateExportParams(
                    "E:\\zzjz_work\\zzoa_241\\src\\main\\resources\\docs\\加班_template.xls");
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);
            File file = new File("D:/downloadFiles/");
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream("D:/downloadFiles/加班测试.xls");
            workbook.write(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 忘打卡信息导出excel测试.
     */
    @Test
    public void testPunchExcelTest() {
        Punch punch = punchService.getPunchInfoById(PUNCH_ID);
        Map<String, Object> map = punchService.getPunchMapList(punch);
        try {
            TemplateExportParams params = new TemplateExportParams(
                    "E:\\zzjz_work\\zzoa_241\\src\\main\\resources\\docs\\忘打卡_template.xls");
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);
            File file = new File("D:/downloadFiles/");
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream("D:/downloadFiles/忘打卡测试.xls");
            workbook.write(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请假信息导出excel测试.
     */
    @Test
    public void testVacationExcelTest() {
        Vacation vacation = vacationService.getVacationInfoById(VACATION_ID);
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy");
        Map<String, Object> map = vacationService.getVacationMapList(vacation);
        Double annual = annualService.annualDays(vacation.getUserId(), sdf4.format(new Date()));
        OverOff overOff = overOffService.queryOverOffInfo(vacation.getUserId(), sdf3.format(new Date()));

        try {
            TemplateExportParams params = new TemplateExportParams(
                    "E:\\zzjz_work\\zzoa_241\\src\\main\\resources\\docs\\请假_template.xls");
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);
            File file = new File("D:/downloadFiles/");
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream("D:/downloadFiles/请假测试.xls");
            workbook.write(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 外出信息导出excel测试.
     */
    @Test
    public void testOutExcelTest() {
        /*Out out = outService.getOutInfoById(576);*/
        Out out = outService.getOutInfoById(OUT_ID);
        Map<String, Object> map = outService.getOutMapList(out);
        try {
            TemplateExportParams params = new TemplateExportParams(
                    "E:\\zzjz_work\\zzoa_241\\src\\main\\resources\\docs\\外出_template.xls");
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);
            File file = new File("D:/downloadFiles/");
            if (!file.exists()) {
                file.mkdirs();
            }
            String excelFileName = "D:/downloadFiles/外出测试.xls";
            FileOutputStream fos = new FileOutputStream(excelFileName);
            workbook.write(fos);
            String pdfName = "D:/downloadFiles/外出测试.pdf";
            //excel转换为pdf
            Office2PDF.office2PDF(excelFileName, pdfName);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 出差信息导出excel测试.
     */
    @Test
    public void testTripExcelTest() {
        /*Out out = outService.getOutInfoById(576);*/
        Trip trip = tripService.getTripInfoById(TRIP_ID);
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy");
        Map<String, Object> map = tripService.getTripMapList(trip);
        try {
            TemplateExportParams params = new TemplateExportParams(
                    "E:\\zzjz_work\\zzoa_241\\src\\main\\resources\\docs\\出差_template.xls");
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);
            File file = new File("D:/downloadFiles/");
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream("D:/downloadFiles/出差测试.xls");
            workbook.write(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
