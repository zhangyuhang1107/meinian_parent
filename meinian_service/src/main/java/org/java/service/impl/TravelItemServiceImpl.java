package org.java.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.java.dao.TravelItemDao;
import org.java.entity.PageResult;
import org.java.pojo.TravelItem;
import org.java.service.TravelItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zyhstart
 */
@Service(interfaceClass = TravelItemService.class) // 发布服务，注册到zk中心
@Transactional // 声明式事务，所有方法都增加事务
public class TravelItemServiceImpl implements TravelItemService {

    @Autowired
    TravelItemDao travelItemDao;

    // ctl + i
    @Override
    public void add(TravelItem travelItem) {

        travelItemDao.add(travelItem);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        // 初始化分页操作
        PageHelper.startPage(currentPage, pageSize);

        // 使用sql语句进行查询（不必要使用mysql的limit）
        Page<TravelItem> page = travelItemDao.findPage(queryString); // 返回当前页数据
        return new PageResult(page.getTotal(), page.getResult()); // 1. 总记录数    2. 分页数据集合
    }

    @Override
    public void deleteById(Integer id) {

        // 查询自由行关联表中是否存在关联数据，如果存在，就抛异常，不进行删除
        long count = travelItemDao.findCountByTravelitemId(id);

        if (count > 0) { // 存在关联数据
            throw new RuntimeException("删除自由行失败，存在关联数据。请解除关系，再删除！");
        }
        travelItemDao.deleteById(id);
    }

    @Override
    public TravelItem getById(Integer id) {

        return travelItemDao.getById(id);
    }

    @Override
    public void edit(TravelItem travelItem) {

        travelItemDao.edit(travelItem);
    }

    @Override
    public List<TravelItem> findAll() {
        return travelItemDao.findAll();
    }
}
