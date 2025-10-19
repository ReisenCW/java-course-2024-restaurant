// 张俊峰 2350284
package com.reisen.cw.Food;

public class Pizza extends Food {
    private int radius;

    Pizza(String name, String feature, String picture, int weight, int calory, double price, int radius) {
        super(name, feature, picture, weight, calory, price);
        this.radius = radius;
    }

    public int getRadius() {
        return this.radius;
    }

    @Override
    public String toString() {
        return super.toString() + "radius: " + Integer.toString(radius) + "\n";
    }
}
