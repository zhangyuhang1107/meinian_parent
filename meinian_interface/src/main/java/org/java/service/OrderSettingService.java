package org.java.service;

import org.java.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @author zyhstart
 */
public interface OrderSettingService {

    /**
     * 批量添加
     * @param listData
     */
    void addBatch(List<OrderSetting> listData);

    /**
     * 根据日期查询预约设置数据(获取指定日期所在月份的预约设置数据)
     * @param date
     * @return
     */
    List<Map<String, Object>> getOrderSettingByMonth(String date);

    /**
     * 根据日期修改可预约人数
     * @param orderSetting
     */
    void editNumberByDate(OrderSetting orderSetting);

}
