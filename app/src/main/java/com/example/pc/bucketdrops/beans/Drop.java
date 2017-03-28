package com.example.pc.bucketdrops.beans;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;



public class Drop extends RealmObject{

    @PrimaryKey
    private long addTime;
    private String goal;
    private long when;
    private boolean isCompleted;

    public Drop(){}

    public Drop(long addTime, String goal, long when, boolean isCompleted) {
        this.addTime = addTime;
        this.goal = goal;
        this.when = when;
        this.isCompleted = isCompleted;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public long getWhen() {
        return when;
    }

    public void setWhen(long when) {
        this.when = when;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }


}
