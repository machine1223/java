package com.pw.service.impl.user;

import com.pw.model.user.Address;
import com.pw.service.api.user.AddressService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 4/15/14
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddressServiceImpl implements AddressService {
    @Override
    public Long addAddress(Address address) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Long> addAddressList(List<Address> addressList) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateAddress(Address address) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteAddress(long id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteAddressByBatch(long[] ids) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Address> listAddress(Address address, Integer pageNo, Integer pageSize) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Address getAddress(long id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
