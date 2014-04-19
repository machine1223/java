package com.pw.service.api.user;

import com.pw.common.pagination.DataPage;
import com.pw.common.pagination.Order;
import com.pw.model.user.User;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 1/20/14
 * Time: 2:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserService {
    /**
     * add new user
     * @param user
     * @return
     */
    public Long addUser(User user);

    /**
     * add new multiple user by batch
     * @param userList
     * @return
     */
    public List<Long> addUserList(List<User> userList);

    public void updateUser(User user);

    public void deleteUser(long id);

    public void deleteUserByBatch(long[] ids);

    public DataPage<User> listUser(User user,Order order,Integer pageNo, Integer pageSize);

    public User getUser(long id);
}
