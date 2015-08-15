package com.one100solutions.viandsbackend.objects;

/**
 * Created by sujith on 18/4/15.
 */
public class AccountHeaderObject {
    public int getTotalCredited() {
        return totalCredited;
    }

    public void setTotalCredited(int totalCredited) {
        this.totalCredited = totalCredited;
    }

    public int getTotalOrdered() {
        return totalOrdered;
    }

    public void setTotalOrdered(int totalOrdered) {
        this.totalOrdered = totalOrdered;
    }

    public int getCredited() {
        return credited;
    }

    public void setCredited(int credited) {
        this.credited = credited;
    }

    private int totalCredited;
    private int totalOrdered;
    private int credited;

    public int getOrdered() {
        return ordered;
    }

    public void setOrdered(int ordered) {
        this.ordered = ordered;
    }

    private int ordered;

}
