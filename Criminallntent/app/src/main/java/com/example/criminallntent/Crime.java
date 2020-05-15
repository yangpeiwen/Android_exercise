package com.example.criminallntent;

import java.util.Date;
import java.util.UUID;
//Crime类表示：各种坏事情的Id，名称，发生的事件，是否解决
public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    public Crime(){
        //14-15代码使用数据库，所以不能用原来的构造方法了
       // mId = UUID.randomUUID();
       // mDate = new Date();
        this(UUID.randomUUID());
    }

    public Crime(UUID id){
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public void setSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public boolean isSolved(){
        return mSolved;
    }

}
