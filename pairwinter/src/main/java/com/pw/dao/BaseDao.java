package com.pw.dao;

import com.pw.common.pagination.DataPage;
import com.pw.common.pagination.Order;
import com.pw.model.user.User;
import org.apache.commons.collections.CollectionUtils;

import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 4/15/14
 * Time: 4:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseDao<T> {

    protected PersistenceManagerFactory persistenceManagerFactory;


    public PersistenceManagerFactory getPersistenceManagerFactory() {
        return persistenceManagerFactory;
    }

    public void setPersistenceManagerFactory(PersistenceManagerFactory persistenceManagerFactory) {
        this.persistenceManagerFactory = persistenceManagerFactory;
    }

    protected DataPage<T> pagedQuery(Query query,Long totalCount,Integer pageNo,Integer pageSize,List<Order> orders){
        if(!CollectionUtils.isEmpty(orders)){
            for (Order order : orders) {
                query.setOrdering(order.toString());
            }
        }
        DataPage<T> dataPage = new DataPage<T>(pageNo,pageSize);
        if(null != pageNo && null != pageSize){
            query.setRange(dataPage.getStart(),dataPage.getEnd());
        }
        return null;
    }
}
