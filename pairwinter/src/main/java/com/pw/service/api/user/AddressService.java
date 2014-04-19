package com.pw.service.api.user;

import com.pw.model.user.Address;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 4/15/14
 * Time: 3:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AddressService {
    /**
     * add new address
     * @param address
     * @return
     */
    public Long addAddress(Address address);

    /**
     * add new multiple address by batch
     * @param addressList
     * @return
     */
    public List<Long> addAddressList(List<Address> addressList);

    public void updateAddress(Address address);

    public void deleteAddress(long id);

    public void deleteAddressByBatch(long[] ids);

    public List<Address> listAddress(Address address,Integer pageNo, Integer pageSize);

    public Address getAddress(long id);
}
