package com.one100solutions.viandsbackend.objects;

/**
 * Created by sujith on 19/3/15.
 */
public class CartObject extends DishObject {

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private int quantity;

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    private boolean complete;

    @Override
    public String toString() {
        return "[ \nid: " + getId() +
                "\nname: " + getName()
                + "\ncategory: " + getCategory()
                + "\ncost: " + getCost()
                + "\nsno: " + getSno()
                + "\navailable: " + isAvailable()
                + "\ncomplete: " + isComplete()
                + "\nquantity: " + getQuantity()
                + "\n" + "]";
    }

}
