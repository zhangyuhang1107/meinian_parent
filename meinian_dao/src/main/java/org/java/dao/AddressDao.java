package org.java.dao;

import com.github.pagehelper.Page;
import org.java.pojo.Address;

import java.util.List;

/**
 * @author zyhstart
 */
public interface AddressDao {

    List<Address> findAllMaps();

    Page<Address> findPage(String queryString);

    void addAddress(Address address);

    void deleteById(Integer id);
}
