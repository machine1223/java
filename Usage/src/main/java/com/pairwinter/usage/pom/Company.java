package com.pairwinter.usage.pom;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 14-3-3
 * Time: 下午11:20
 * To change this template use File | Settings | File Templates.
 */

public class Company {
    private String boothNum;//姓名
    private String nameChina;//性别
    private String nameEnglish;
    private List<String> types;
    private String address;
    private String postcode;
    private String phoneNum;
    private String faxNum;
    private String email;
    private String website;
    private String summaryChina;
    private String summaryEnglish;

    public String getBoothNum() {
        return boothNum;
    }

    public void setBoothNum(String boothNum) {
        this.boothNum = boothNum;
    }

    public String getNameChina() {
        return nameChina;
    }

    public void setNameChina(String nameChina) {
        this.nameChina = nameChina;
    }

    public String getNameEnglish() {
        return nameEnglish;
    }

    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getFaxNum() {
        return faxNum;
    }

    public void setFaxNum(String faxNum) {
        this.faxNum = faxNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSummaryChina() {
        return summaryChina;
    }

    public void setSummaryChina(String summaryChina) {
        this.summaryChina = summaryChina;
    }

    public String getSummaryEnglish() {
        return summaryEnglish;
    }

    public void setSummaryEnglish(String summaryEnglish) {
        this.summaryEnglish = summaryEnglish;
    }
}
