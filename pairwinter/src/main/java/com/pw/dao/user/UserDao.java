package com.pw.dao.user;

import com.pw.dao.jdo.PMF;
import com.pw.model.user.User;

import javax.jdo.PersistenceManager;

/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 1/20/14
 * Time: 2:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserDao {
    public Long addUser(User user){
        PersistenceManager pm = PMF.get().getPersistenceManager();
        pm.makePersistent(user);
        return user.getId();
    }
}
