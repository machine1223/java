package com.pw.test.service.user;

import com.google.appengine.api.datastore.*;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.pw.dao.user.UserDao;
import com.pw.model.user.User;
import com.pw.service.api.user.UserService;
import com.pw.service.impl.user.UserServiceImpl;
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
 * Date: 4/15/14
 * Time: 5:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserServiceTest {
    protected static ApplicationContext applicationContext;
    protected static UserService userService;
    static{
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        userService = (UserServiceImpl)applicationContext.getBean("userService");
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

    @Test
    public void testAddUser() throws IOException {
        User user = new User("damon","liu",29,"male",null);
        user.setFirstName("damon");
        assertNotNull(userService.addUser(user));
    }

    public void listUser() throws IOException{
        this.testAddUser();
        userService.listUser(null,null,null,null);
    }
}
