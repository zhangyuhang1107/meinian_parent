package org.java.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.java.dao.TravelGroupDao;
import org.java.entity.PageResult;
import org.java.pojo.TravelGroup;
import org.java.service.TravelGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyhstart
 */
@Service(interfaceClass = TravelGroupService.class)
@Transactional
public class TravelGroupServiceImpl implements TravelGroupService {

    @Autowired
    TravelGroupDao travelGroupDao;

    @Override
    public void add(Integer[] travelItemIds, TravelGroup travelGroup) {

        // 数据库分配id
        travelGroupDao.add(travelGroup);
        // 这里 travelGroupId 不能拿到，必须进行主键回填，获取自增长主键
        Integer travelGroupId = travelGroup.getId();

        setTravelGroupAndTravelItem(travelGroupId, travelItemIds);
    }

    private void setTravelGroupAndTravelItem(Integer travelGroupId, Integer[] travelItemIds) {

        if (travelItemIds != null && travelItemIds.length > 0) {

            for (Integer travelItemId : travelItemIds) {
                // 准备dao层需要参数，利用map集合作为参数传递数据
                Map<String, Integer> map = new HashMap<>();
                map.put("travelGroupId", travelGroupId);
                map.put("travelItemId", travelItemId);

                travelGroupDao.addTravelGroupAndTravelItem(map);
            }
        }
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        // 使用分页插件PageHelper，设置当前页，每页最多显示的记录数
        PageHelper.startPage(currentPage, pageSize);
        Page<TravelGroup> page = travelGroupDao.findPage(queryString);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public TravelGroup getById(Integer id) {

        return travelGroupDao.getById(id);
    }

    @Override
    public List<Integer> getTravelItemIdsByTravelGroupId(Integer travelGroupId) {

        return travelGroupDao.getTravelItemIdsByTravelGroupId(travelGroupId);
    }

    @Override
    public void edit(Integer[] travelItemIds, TravelGroup travelGroup) {

        travelGroupDao.edit(travelGroup);
        Integer travelGroupId = travelGroup.getId();

        // 先删除中间表
        travelGroupDao.delete(travelGroupId);

        // 重新再添加关联数据
        setTravelGroupAndTravelItem(travelGroupId, travelItemIds);
    }

    @Override
    public void deleteById(Integer id) {

        long count = travelGroupDao.findCountByTravelgroupId(id);

        if (count > 0) {
            throw new RuntimeException("删除跟团游失败，存在关联数据。请解除关系，再删除！");
        }
        travelGroupDao.deleteById(id);
    }

    @Override
    public List<TravelGroup> findAll() {
        return travelGroupDao.findAll();
    }
}
