package org.java.dao;

import org.apache.ibatis.annotations.Param;
import org.java.pojo.Member;

import java.util.List;

/**
 * @author zyhstart
 */
public interface MemberDao {

    void add(Member member);

    void update(@Param("name") String name, @Param("id") Integer id);

    Member getMemberByTelephone(String telephone);

    int findMemberCountByMonth(String month);

    /**
     * 今日新增会员数
     * @param date
     * @return
     */
    int getTodayNewMember(String date);

    /**
     * 总会员数
     * @return
     */
    int getTotalMember();

    /**
     * 本周 / 本月新增会员数
     * @param date
     * @return
     */
    int getThisWeekAndMonthNewMember(String date);
}
