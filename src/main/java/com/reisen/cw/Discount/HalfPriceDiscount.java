package com.reisen.cw.Discount;

import com.reisen.cw.Food.Food;

public class HalfPriceDiscount extends Discount{
    public HalfPriceDiscount() {
        description = "Only Margherita Pizza get 50% discount";
    }

    @Override
    public PaybillResult caculateResult(java.util.ArrayList<Food> food) {
        PaybillResult result = new PaybillResult();
        double totalCost = 0;
        for(Food f : food) {
            if(f.getName().equals("Margherita Pizza")) {
                totalCost += f.getPrice() / 2;
            } else {
                totalCost += f.getPrice();
            }
            result.totalCalory += f.getCalory();
        }
        result.totalCost = totalCost;
        return result;
    }
    
}
