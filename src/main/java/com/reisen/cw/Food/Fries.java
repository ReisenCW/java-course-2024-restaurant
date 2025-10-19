// 张俊峰 2350284
package com.reisen.cw.Food;

public class Fries extends Food{
    private String thickness;

    Fries(String name, String feature, String picture, int weight, int calory, double price, String thickness) {
        super(name, feature, picture, weight, calory, price);
        this.thickness = thickness;
    }

    public String getThickness() {
        return this.thickness;
    }

    @Override
    public String toString() {
        return super.toString() + "thickness: " + this.thickness + "\n";
    }
}
