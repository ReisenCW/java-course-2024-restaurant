package com.reisen.cw.Discount;

import java.util.ArrayList;

import com.reisen.cw.Food.Food;

public class Minus50Discount extends Discount{
    public Minus50Discount() {
        description = "total cost -50 if total cost >=100";
    }

    @Override
    public PaybillResult caculateResult(ArrayList<Food> food) {
        PaybillResult result = new PaybillResult();
        double totalCost = 0;
        for(Food f : food) {
            totalCost += f.getPrice();
            result.totalCalory += f.getCalory();
        }
        if(totalCost >= 100) {
            totalCost -= 50;
        }
        result.totalCost = totalCost;
        return result;
    }
}
