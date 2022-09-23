package org.java.service;

import java.util.Map;

/**
 * @author zyhstart
 */
public interface ReportService {

    /**
     * 获得运营统计数据
     * Map数据格式：
     * reportDate（当前时间）--String
     * todayNewMember（今日新增会员数） -> number
     * totalMember（总会员数） -> number
     * thisWeekNewMember（本周新增会员数） -> number
     * thisMonthNewMember（本月新增会员数） -> number
     * todayOrderNumber（今日预约数） -> number
     * todayVisitsNumber（今日出游数） -> number
     * thisWeekOrderNumber（本周预约数） -> number
     * thisWeekVisitsNumber（本周出游数） -> number
     * thisMonthOrderNumber（本月预约数） -> number
     * thisMonthVisitsNumber（本月出游数） -> number
     * hotSetmeal（热门套餐（取前4）） -> List<Setmeal>
     */
    Map<String, Object> getBusinessReportData();
}
