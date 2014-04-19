package com.pw.service.impl.user;

import com.pw.common.pagination.DataPage;
import com.pw.common.pagination.Order;
import com.pw.dao.user.UserDao;
import com.pw.model.user.User;
import com.pw.service.api.user.UserService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 4/15/14
 * Time: 4:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    @Override
    public Long addUser(User user) {
        userDao.addUser(user);
        return user.getId();
    }

    @Override
    public List<Long> addUserList(List<User> userList) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateUser(User user) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteUser(long id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteUserByBatch(long[] ids) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DataPage<User> listUser(User user,Order order, Integer pageNo, Integer pageSize) {
        return userDao.listUser(user,order,pageNo,pageSize);
    }

    @Override
    public User getUser(long id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
