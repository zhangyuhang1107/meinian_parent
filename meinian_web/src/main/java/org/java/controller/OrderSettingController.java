package org.java.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.java.constant.MessageConstant;
import org.java.entity.Result;
import org.java.pojo.OrderSetting;
import org.java.service.OrderSettingService;
import org.java.util.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zyhstart
 */
@RestController
@RequestMapping("/orderSetting")
public class OrderSettingController {

    @Reference
    OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile) {

        try {
            // 使用poi工具类解析excel文件，读取里面的内容
            List<String[]> list = POIUtils.readExcel(excelFile);

            // 把List<String[]> 数据转换成 List<OrderSetting> 数据
            List<OrderSetting> listData = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            for (String[] strArray : list) {
                String dateStr = strArray[0];
                String numberStr = strArray[1];
                OrderSetting orderSetting = new OrderSetting(sdf.parse(dateStr), Integer.parseInt(numberStr));
                listData.add(orderSetting);
            }

            orderSettingService.addBatch(listData);
            return new Result(true, MessageConstant.UPLOAD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.UPLOAD_FAIL);
        }
    }

    //    this.leftobj = [
//    { date: 1, number: 120, reservations: 1 },
//    { date: 3, number: 120, reservations: 1 },
//    { date: 4, number: 120, reservations: 120 },
//    { date: 6, number: 120, reservations: 1 },
//    { date: 8, number: 120, reservations: 1 }
//    ];
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date) {

        try {
            List<Map<String, Object>> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {

        try {
            orderSettingService.editNumberByDate(orderSetting);
            // 预约设置成功
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            // 预约设置失败
            return new Result(true, MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
