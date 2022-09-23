package org.java.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.java.constant.MessageConstant;
import org.java.dao.MemberDao;
import org.java.dao.OrderDao;
import org.java.dao.OrderSettingDao;
import org.java.entity.Result;
import org.java.pojo.Member;
import org.java.pojo.Order;
import org.java.pojo.OrderSetting;
import org.java.service.OrderService;
import org.java.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zyhstart
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;
    @Autowired
    MemberDao memberDao;
    @Autowired
    OrderSettingDao orderSettingDao;

    /**
     * 1. 判断当前的日期是否可以预约(根据orderDate查询t_ordersetting, 能查询出来可以预约;查询不出来,不能预约)
     * 2. 判断当前日期是否预约已满(判断reservations（已经预约人数）是否等于number（最多预约人数）)
     * 3. 判断是否是会员(根据手机号码查询t_member)
     * - 如果是会员(能够查询出来), 防止重复预约(根据member_id,orderDate,setmeal_id查询t_order)
     * - 如果不是会员(不能够查询出来) ,自动注册为会员(直接向t_member插入一条记录)
     * 4. 进行预约
     * - 向t_order表插入一条记录
     * - t_ordersetting表里面预约的人数reservations+1
     *
     * @param map
     * @return
     */
    @Override
    public Result saveOrder(Map<String, Object> map) {

        Integer setmealId = Integer.parseInt((String) map.get("setmealId"));

        String orderDate = (String) map.get("orderDate");
        Date date = null;
        try {
            date = DateUtils.parseString2Date(orderDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 判断当前日期是否可以预约
        OrderSetting orderSetting = orderSettingDao.getOrderSettingByOrderDate(date);

        // 预约不存在，不能预约
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        } else {
            // 判断当前日期是否预约已满
            // 最多预约人数
            int number = orderSetting.getNumber();
            // 已经预约人数
            int reservations = orderSetting.getReservations();
            if (reservations >= number) {
                return new Result(false, MessageConstant.ORDER_FULL);
            }
        }

        // 判断是否是会员，根据手机号查询 会员
        String telephone = (String) map.get("telephone");
        Member member = memberDao.getMemberByTelephone(telephone);

        if (member == null) {
            // 不存在，快速注册
            member = new Member();
            member.setName((String) map.get("name"));
            member.setSex((String) map.get("sex"));
            member.setIdCard((String) map.get("idCard"));
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            // 注意主键回填，因为下面查询是否重复预约需要id
            memberDao.add(member);
        } else {

            // 根据输入的会员名和已经存在的会员名进行对比，是否一致
            if (!((String) map.get("name")).equals(member.getName())) {
                // 去数据库进行更新
                memberDao.update((String) map.get("name"), member.getId());
            }

            // 检查是否重复预约
            Order orderParam = new Order();
            orderParam.setOrderDate(date);
            orderParam.setSetmealId(setmealId);
            orderParam.setMemberId(member.getId());
            List<Order> orderList = orderDao.findOrderByCondition(orderParam);

            if (orderList != null && orderList.size() > 0) {
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }

        // 进行预约，t_order表插入一条记录，t_ordersetting表里面预约的人数reservation + 1
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(date);
        order.setOrderType("微信预约");
        order.setOrderStatus("未出游");
        order.setSetmealId(setmealId);
        // 注意主键回填
        orderDao.add(order);

        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        return new Result(true, MessageConstant.ORDER_SUCCESS, order);
    }

    @Override
    public Map<String, Object> findById(Integer id) {

        Map<String, Object> map = orderDao.findById(id);
        if (map != null) {
            // 处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            try {
                map.put("orderDate", DateUtils.parseDate2String(orderDate));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
