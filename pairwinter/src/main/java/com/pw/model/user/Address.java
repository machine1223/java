package com.pw.model.user;

import javax.jdo.annotations.*;

/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 4/14/14
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Address {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
    @Persistent
    private String city;
    @Persistent
    private String county;
    @Persistent
    private String stateOrProvince;
    @Persistent
    private String street;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
