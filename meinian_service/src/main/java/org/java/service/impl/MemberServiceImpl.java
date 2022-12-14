package org.java.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.java.dao.MemberDao;
import org.java.pojo.Member;
import org.java.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyhstart
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {

        return memberDao.getMemberByTelephone(telephone);
    }

    @Override
    public void add(Member member) {

        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {

        List<Integer> list = new ArrayList<>();
        if (months != null && months.size() > 0) {
            for (String month : months) {
                int count = memberDao.findMemberCountByMonth(month);
                list.add(count);
            }
        }

        return list;
    }
}
