package com.one100solutions.viandsbackend.objects;

/**
 * Created by sujith on 14/3/15.
 */
public class DishObject {

    private String id;
    private String name;
    private String category;
    private int cost;
    private int sno;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    private boolean available;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "[ \nid: " + getId() +
                "\nname: " + getName()
                + "\ncategory: " + getCategory()
                + "\ncost: " + getCost()
                + "\nsno: " + getSno()
                + "\navailable: " + isAvailable()
                + "\n" + "]";
    }

}
