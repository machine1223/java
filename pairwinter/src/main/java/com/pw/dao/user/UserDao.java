package com.pw.dao.user;

import com.pw.common.pagination.DataPage;
import com.pw.common.pagination.Order;
import com.pw.dao.BaseDao;
import com.pw.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 1/20/14
 * Time: 2:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserDao extends BaseDao{

    public Long addUser(User user){
        PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
        pm.makePersistent(user);
        return user.getId();
    }

    public void addUserByBatch(List<User> userList){
        List<Long> ids = new ArrayList<Long>();
        PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
        pm.makePersistentAll(userList);
    }

    public DataPage<User> listUser(User user,Order order,Integer pageNo,Integer pageSize){
        PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
        Query q = pm.newQuery(User.class);
        Map<String,String> parameterMaps = new HashMap<String,String>();
        if(user.getFirstName() !=null){
            q.setFilter("firstName == "+user.getFirstName());
            q.declareParameters("String "+user.getFirstName());
        }
        if(user != null){
            if(user.getFirstName() !=null){
                q.setFilter("firstName == firstNameParam");
                parameterMaps.put("firstNameParam",user.getFirstName());
            }
            if(user.getLastName() != null){
                q.setFilter("lastName == lastNameParam");
                parameterMaps.put("lastNameParam",user.getLastName());
            }
        }

        try{
            q.setResult("count(*)");
            List<User> userList = (List<User>)q.executeWithMap(parameterMaps);
//            dataPage.setData(userList);
        }finally {
            q.closeAll();
        }
        return null;
    }

    public void modifyUser(User user){
        List<Long> ids = new ArrayList<Long>();
        PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
        pm.makePersistent(user);
    }

    public void deleteUser(User user){
        PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
        pm.deletePersistent(user);
    }

    public void deleteUserById(long id){
        PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
        User user = pm.getObjectById(User.class,id);
        pm.deletePersistent(user);
    }

}
