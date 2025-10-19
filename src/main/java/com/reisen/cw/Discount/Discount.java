package com.reisen.cw.Discount;

import java.util.ArrayList;

import com.reisen.cw.Food.Food;

public abstract class Discount {
    public class PaybillResult {
        int totalCalory = 0;
        double totalCost = 0;

        public int getTotalCalory() {
            return totalCalory;
        }
        public double getTotalCost() {
            return totalCost;
        }
    }
    public String description;
    public abstract PaybillResult caculateResult(ArrayList<Food> food);
}
