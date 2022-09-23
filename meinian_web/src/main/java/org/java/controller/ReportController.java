package org.java.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.java.constant.MessageConstant;
import org.java.entity.Result;
import org.java.service.MemberService;
import org.java.service.ReportService;
import org.java.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zyhstart
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    MemberService memberService;
    @Reference
    SetmealService setmealService;
    @Reference
    ReportService reportService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {

        try {
            List<String> months = new ArrayList<>();
            List<Integer> memberCount = null;

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -12);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            for (int i = 0; i < 12; i++) {
                // 第一个参数是月份
                // 第二个参数是月份 + 1个月
                calendar.add(Calendar.MONTH, 1);
                Date time = calendar.getTime();
                String month = sdf.format(time);
                months.add(month);
            }
            memberCount = memberService.findMemberCountByMonth(months);

            Map<String, Object> map = new HashMap<>();
            map.put("months", months);
            map.put("memberCount", memberCount);

            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {

        try {
            List<String> setmealNames = new ArrayList<>();
            // 组织套餐名称 + 套餐名称对应的数据
            List<Map<String, Object>> setmealCount = setmealService.getSetmealReport();

            for (Map<String, Object> map : setmealCount) {
                String setmealName = (String) map.get("name");
                setmealNames.add(setmealName);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("setmealNames", setmealNames);
            map.put("setmealCount", setmealCount);

            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {

        try {
            Map<String, Object> map = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        Workbook workbook = null;
        ServletOutputStream out = null;

        try {
            // 拿数据
            Map<String, Object> result = reportService.getBusinessReportData();
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            // 获取模板文件
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";

            // 工作薄
            workbook = new XSSFWorkbook(new File(filePath));

            // 写数据
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日出游数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周出游数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月出游数

            int rowNum = 12;
            for (Map map : hotSetmeal) {//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            // 输出文件，以流形式文件下载，另存为操作
            out = response.getOutputStream();

            // 下载的数据类型（excel类型）
            response.setContentType("application/vnd.ms-excel");
            // 设置下载形式(通过附件的形式下载)
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");

            workbook.write(out); //写给浏览器，文件下载
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭
            try {
                if (out != null)
                    out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (workbook != null)
                    workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
