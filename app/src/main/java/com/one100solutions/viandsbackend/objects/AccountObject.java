package com.one100solutions.viandsbackend.objects;

/**
 * Created by sujith on 18/4/15.
 */
public class AccountObject {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

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

    private String id;
    private String name;
    private long phone;
    private int credits;
    private int totalCredited;
    private int totalOrdered;
}
