package org.java.service;

import org.java.pojo.Member;

import java.util.List;

/**
 * @author zyhstart
 */
public interface MemberService {

    /**
     * 根据手机号查询会员信息
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    void add(Member member);

    /**
     * 根据月份统计会员数量
     * @param months
     * @return
     */
    List<Integer> findMemberCountByMonth(List<String> months);
}
