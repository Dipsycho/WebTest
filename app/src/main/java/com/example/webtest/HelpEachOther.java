package com.example.webtest;

import com.avos.avoscloud.AVUser;

import java.util.Date;

/**
 * Created by Darkness on 2017/5/22.
 */

public class HelpEachOther {

    private String remark;

    private String content;

    private AVUser releaseUser;

    private AVUser acceptUser;

    private int history;        //=0 发布后还未接受 =1 已被接受 =2 已完成

    private int talkUser;          //=0 默认 =1 发布者联系接受者 =2 接受者联系发布者

    private double latitude;

    private double longitude;

    private String location;

    private String objectIDnumber;

    private Date createdAt;

    private Date updatedAt;

    public HelpEachOther(String remark, String content
            , AVUser releaseUser, AVUser acceptUser
            , int history, int talkUser
            , double latitude, double longitude, String location
            , String objectIDnumber, Date createdAt, Date updatedAt) {
        this.remark = remark;
        this.content = content;
        this.releaseUser = releaseUser;
        this.acceptUser = acceptUser;
        this.history = history;
        this.talkUser = talkUser;
        this.latitude = latitude;
        this.longitude = longitude;
        this.objectIDnumber = objectIDnumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.location = location;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AVUser getReleaseUser() {
        return releaseUser;
    }

    public void setReleaseUser(AVUser releaseUser) {
        this.releaseUser = releaseUser;
    }

    public AVUser getAcceptUser() {
        return acceptUser;
    }

    public void setAcceptUser(AVUser acceptUser) {
        this.acceptUser = acceptUser;
    }

    public int getHistory() {
        return history;
    }

    public void setHistory(int history) {
        this.history = history;
    }

    public int getTalkUser() {
        return talkUser;
    }

    public void setTalkUser(int talkUser) {
        this.talkUser = talkUser;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getObjectIDnumber() {
        return objectIDnumber;
    }

    public void setObjectIDnumber(String objectIDnumber) {
        this.objectIDnumber = objectIDnumber;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

