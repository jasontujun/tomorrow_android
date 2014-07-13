package com.tomorrow.android.data.model;

import java.util.List;

/**
 * Created by jasontujun on 14-7-5.
 */
public class Prediction {

    private String predictionId;
    private String what;
    private String how;
    private String where;
    private int whenYear;
    private int whenMonth;
    private int whenDay;
    private String reason;
    private String authorId;
    private String authorName;
    private double credit;
    private long createdTime;
    private double publishLatitude;
    private double publishLongitude;
    private double averageCount;
    private int replyCount;
    private int resultCount;
    private int scanCount;

    private List<Reply> replyList;
    private List<Result> resultList;

    public String getPredictionId() {
        return predictionId;
    }

    public void setPredictionId(String predictionId) {
        this.predictionId = predictionId;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getHow() {
        return how;
    }

    public void setHow(String how) {
        this.how = how;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public int getWhenYear() {
        return whenYear;
    }

    public void setWhenYear(int whenYear) {
        this.whenYear = whenYear;
    }

    public int getWhenMonth() {
        return whenMonth;
    }

    public void setWhenMonth(int whenMonth) {
        this.whenMonth = whenMonth;
    }

    public int getWhenDay() {
        return whenDay;
    }

    public void setWhenDay(int whenDay) {
        this.whenDay = whenDay;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
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

    public double getPublishLatitude() {
        return publishLatitude;
    }

    public void setPublishLatitude(double publishLatitude) {
        this.publishLatitude = publishLatitude;
    }

    public double getPublishLongitude() {
        return publishLongitude;
    }

    public void setPublishLongitude(double publishLongitude) {
        this.publishLongitude = publishLongitude;
    }

    public double getAverageCount() {
        return averageCount;
    }

    public void setAverageCount(double averageCount) {
        this.averageCount = averageCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public int getScanCount() {
        return scanCount;
    }

    public void setScanCount(int scanCount) {
        this.scanCount = scanCount;
    }

    public List<Reply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Reply> replyList) {
        this.replyList = replyList;
    }

    public List<Result> getResultList() {
        return resultList;
    }

    public void setResultList(List<Result> resultList) {
        this.resultList = resultList;
    }

    public void copy(Prediction p) {
        if (p == null)
            return;

        this.predictionId = p.getPredictionId();
        this.what = p.getWhat();
        this.how = p.getHow();
        this.where = p.getWhere();
        this.whenYear = p.getWhenYear();
        this.whenMonth = p.getWhenMonth();
        this.whenDay = p.getWhenDay();
        this.reason = p.getReason();
        this.authorId = p.getAuthorId();
        this.authorName = p.getAuthorName();
        this.credit = p.getCredit();
        this.createdTime = p.getCreatedTime();
        this.publishLatitude = p.getPublishLatitude();
        this.publishLongitude = p.getPublishLongitude();
        this.averageCount = p.getAverageCount();
        this.replyCount = p.getReplyCount();
        this.resultCount = p.getResultCount();
        this.scanCount = p.getScanCount();
        this.replyList = p.getReplyList();
        this.resultList = p.getResultList();
    }
}
