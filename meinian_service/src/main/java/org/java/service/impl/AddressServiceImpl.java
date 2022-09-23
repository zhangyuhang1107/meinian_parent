package org.java.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.java.dao.AddressDao;
import org.java.entity.PageResult;
import org.java.entity.QueryPageBean;
import org.java.pojo.Address;
import org.java.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zyhstart
 */
@Service(interfaceClass = AddressService.class)
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressDao addressDao;

    @Override
    public List<Address> findAllMaps() {

        return addressDao.findAllMaps();
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Address> page = addressDao.findPage(queryPageBean.getQueryString());

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void addAddress(Address address) {

        addressDao.addAddress(address);
    }

    @Override
    public void deleteById(Integer id) {

        addressDao.deleteById(id);
    }
}
