package com.pw.test.dao.user;

import com.google.appengine.api.datastore.*;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.pw.dao.user.UserDao;
import com.pw.model.user.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 4/14/14
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserTest {
    protected static ApplicationContext applicationContext;
    protected static UserDao userDao;
    static{
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        userDao = (UserDao)applicationContext.getBean("userDao");
    }

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper((new LocalDatastoreServiceTestConfig()));

    @Before
    public void setUp(){
        helper.setUp();
    }
    @After
    public void tearDown(){
        helper.tearDown();
    }

    private void doTest(){
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        assertEquals(0,datastoreService.prepare(new Query("damon")).countEntities(FetchOptions.Builder.withLimit(10)));
        datastoreService.put(new Entity("damon"));
        datastoreService.put(new Entity("damon"));
        assertEquals(2, datastoreService.prepare(new Query("damon")).countEntities(FetchOptions.Builder.withLimit(10)));
    }

    @Test
    public void testAddUser() throws IOException {
        User user = new User("damon","liu",29,"male",null);
        user.setFirstName("damon");
        assertNotNull(userDao.addUser(user));
    }
}
