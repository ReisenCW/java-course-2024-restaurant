// 张俊峰 2350284
package com.reisen.cw.Food;

public class Food {
    private String name;
    private String feature;
    private String picture;
    private int weight;
    private int calory;
    private double price;

    Food(String name, String feature, String picture, int weight, int calory, double price) {
        this.name = name;
        this.feature = feature;
        this.picture = picture; // ensure picture is kept from parser
        this.weight = weight;
        this.calory = calory;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public String getFeature() {
        return this.feature;
    }

    public String getPicture() {
        return this.picture;
    }

    public int getWeight() {
        return this.weight;
    }

    public int getCalory() {
        return this.calory;
    }

    public double getPrice() {
        return this.price;
    }

    @Override
    public String toString() {
        String template = "name: %s\nfeature: %s\npicture: %s\nweight: %d\n" +
                          "calory: %d\n" +
                          "price: %.2f\n";
        return String.format(template, name, feature, picture, weight, calory, price);
    }
}
