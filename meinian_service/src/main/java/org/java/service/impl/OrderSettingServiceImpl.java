package org.java.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.java.dao.OrderSettingDao;
import org.java.pojo.OrderSetting;
import org.java.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyhstart
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    OrderSettingDao orderSettingDao;

    @Override
    public void addBatch(List<OrderSetting> listData) {

        for (OrderSetting orderSetting : listData) {
            // 如果日期对应设置存在，就修改，否则就添加
            int count = orderSettingDao.findOrderSettingByOrderDate(orderSetting.getOrderDate());

            // 预约设置存在了，修改人数
            if (count > 0) {
                orderSettingDao.edit(orderSetting);
            } else {
                orderSettingDao.add(orderSetting);
            }
        }
    }

    @Override
    public List<Map<String, Object>> getOrderSettingByMonth(String date) {

        // 1. 组织查询map参数
        String startDate = date + "-1";
        String endDate = date + "-31";

        // 封装参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);

        // 2. 查询当前月份的预约设置
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(paramMap);

        // 3. 将List<OrderSetting>，组织成List<Map>
        List<Map<String, Object>> listData = new ArrayList<>();

        for (OrderSetting orderSetting : list) {
            Map<String, Object> map = new HashMap<>();
            // 获取日期（几号）
            map.put("date", orderSetting.getOrderDate().getDate());
            // 可预约人数
            map.put("number", orderSetting.getNumber());
            // 已预约人数
            map.put("reservations", orderSetting.getReservations());

            listData.add(map);
        }
        return listData;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {

        int count = orderSettingDao.findOrderSettingByOrderDate(orderSetting.getOrderDate());

        if (count > 0) {
            // 当前日期已经进行预约设置，需要进行修改操作
            orderSettingDao.edit(orderSetting);
        } else {
            // 当前日期没有进行预约设置，进行添加操作
            orderSettingDao.add(orderSetting);
        }
    }
}
