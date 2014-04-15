package com.pw.dao.jdo;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 4/14/14
 * Time: 11:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class PMF {
    private static final PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private PMF() {}

    public static PersistenceManagerFactory get(){
        return pmfInstance;
    }
}
