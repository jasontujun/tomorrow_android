package com.tomorrow.android.data.model;

/**
 * Created by jasontujun on 14-7-12.
 */
public class User {

    private String userId;
    private String userName;
    private double credit;
    private long createdTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public void copy(User user) {
        if (user == null)
            return;

        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.credit = user.getCredit();
        this.createdTime = user.getCreatedTime();
    }
}
