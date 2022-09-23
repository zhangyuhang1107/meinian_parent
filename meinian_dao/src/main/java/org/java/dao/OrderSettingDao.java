package org.java.dao;

import org.java.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zyhstart
 */
public interface OrderSettingDao {

    /**
     * 预约日期查询
     * @param orderDate
     * @return
     */
    int findOrderSettingByOrderDate(Date orderDate);

    /**
     * 根据日期更新预约人数
     * @param orderSetting
     */
    void edit(OrderSetting orderSetting);

    /**
     * 新增预约
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 根据日期查询预约设置数据
     * @param paramMap
     * @return
     */
    List<OrderSetting> getOrderSettingByMonth(Map<String, String> paramMap);

    /**
     * 根据预约日期查询是否存在预约设置
     * @param date
     * @return
     */
    OrderSetting getOrderSettingByOrderDate(Date date);

    /**
     * 更新已预约人数
     * @param orderSetting
     */
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
