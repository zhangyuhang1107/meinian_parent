package org.java.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.java.constant.RedisConstant;
import org.java.dao.SetmealDao;
import org.java.entity.PageResult;
import org.java.pojo.Setmeal;
import org.java.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyhstart
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealDao setmealDao;
    @Autowired
    JedisPool jedisPool;

    @Override
    public void add(Integer[] travelgroupIds, Setmeal setmeal) {

        // 1. 保存套餐
        setmealDao.add(setmeal); // id回填

        // 2. 保存关联数据
        Integer setmealId = setmeal.getId();

        // 绑定套餐和跟团游的多对多关系
        setSetmealAndTravelGrop(travelgroupIds, setmealId);

        // ******************* 补充代码，解决垃圾图片问题
        String pic = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pic);
        //********************
    }

    private void setSetmealAndTravelGrop(Integer[] travelgroupIds, Integer setmealId) {

        if (travelgroupIds != null && travelgroupIds.length > 0) {
            for (Integer travelgroupId : travelgroupIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("travelgroupId", travelgroupId);
                map.put("setmealId", setmealId);
                setmealDao.addSetmealAndTravelGrop(map);
            }
        }
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page = setmealDao.findPage(queryString);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void deleteById(Integer id) {

        Long count = setmealDao.findCountBySetmealId(id);

        if (count > 0) {
            throw new RuntimeException("删除套餐游失败，存在关联数据。请解除关系，再删除！");
        }

        setmealDao.deleteById(id);
    }

    @Override
    public List<Setmeal> findAll() {

        return setmealDao.findAll();
    }

    @Override
    public Setmeal findById(Integer id) {

        return setmealDao.findById(id);
    }

    @Override
    public Setmeal getSetmealById(Integer id) {

        return setmealDao.getSetmealById(id);
    }

    @Override
    public List<Map<String, Object>> getSetmealReport() {

        return setmealDao.getSetmealReport();
    }
}
