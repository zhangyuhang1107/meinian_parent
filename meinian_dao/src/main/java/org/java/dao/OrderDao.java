package org.java.dao;

import org.java.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * @author zyhstart
 */
public interface OrderDao {

    List<Order> findOrderByCondition(Order orderParam);

    void add(Order order);

    Map<String, Object> findById(Integer id);

    /**
     * 今日预约数
     * @param date
     * @return
     */
    int getTodayOrderNumber(String date);

    /**
     * 今日已出游数
     * @param date
     * @return
     */
    int getTodayVisitsNumber(String date);

    /**
     * 本周 / 本月，预约数
     * @param map
     * @return
     */
    int getThisWeekAndMonthOrderNumber(Map<String, Object> map);

    /**
     * 本周 / 本月，已出游数
     * @param map
     * @return
     */
    int getThisWeekAndMonthVisitsNumber(Map<String, Object> map);

    /**
     * 热门套餐
     * @return
     */
    List<Map<String,Object>> findHotSetmeal();
}
