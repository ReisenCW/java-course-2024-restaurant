// 张俊峰 2350284
package com.reisen.cw.Food;

public class FriedChicken extends Food {
    private String spiciness;

    FriedChicken(String name, String feature, String picture, int weight, int calory, double price, String spiciness) {
        super(name, feature, picture, weight, calory, price);
        this.spiciness = spiciness;
    }

    public String getSpiciness() {
        return this.spiciness;
    }

    @Override
    public String toString() {
        return super.toString() + "spiciness: " + this.spiciness + "\n";
    }
}
