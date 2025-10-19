package com.reisen.cw.PdfGenerator;

import com.reisen.cw.Discount.Discount;
import com.reisen.cw.Food.Food;
import java.util.List;

public abstract class PdfGenerator {
    public String fileName;
    public String name;

    // 统一内容构造
    public String buildContent(Discount selectedDiscount, Discount.PaybillResult result, List<Food> shoppingList) {
        StringBuilder content = new StringBuilder();
        content.append("Shopping List:\n");
        int i = 1;
        for (Food food : shoppingList) {
            content.append(i).append(". ").append(food.toString()).append("\n");
            i++;
        }
        content.append(String.format(
            "\nAfter using discount %s,\nthe total cost is: %.2f,\nthe total calory is: %d\n",
            selectedDiscount.description, result.getTotalCost(), result.getTotalCalory()
        ));
        return content.toString();
    }

    // 统一接口
    public abstract void generatePdf(Discount selectedDiscount, Discount.PaybillResult result, List<Food> shoppingList);
}
