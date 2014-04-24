package com.pw.model.user;

import javax.jdo.annotations.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 4/14/14
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class User {
    public static enum Column{
        firstName,lastName
    };
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
    @Persistent
    private String firstName;
    @Persistent
    private String lastName;
    @Persistent
    private int age;
    @Persistent
    private String gender;
    @NotPersistent
    private List<Address> addressList;

    public User(String firstName, String lastName, int age, String gender, List<Address> addressList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.addressList = addressList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}
