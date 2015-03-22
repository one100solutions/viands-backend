package com.one100solutions.viandsbackend.objects;

import java.util.ArrayList;

/**
 * Created by sujith on 14/3/15.
 */
public class RestaurantObject {

    private String name;
    private String location;
    private String id;
    private ArrayList<DishObject> menu;



    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    private boolean open;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<DishObject> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<DishObject> menu) {
        this.menu = menu;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
