package com.reisen.cw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import com.reisen.cw.Discount.*;
import com.reisen.cw.Food.*;
import com.reisen.cw.PdfGenerator.PdfBoxGenerator;
import com.reisen.cw.PdfGenerator.PdfGenerator;
import com.reisen.cw.PdfGenerator.iTextPdfGenerator;

public class Displayer {
    private ArrayList<Pizza> pizzaList;
    private ArrayList<Fries> fryList;
    private ArrayList<FriedChicken> chickenList;
    private ArrayList<? extends Food> currentList;
    private char currentSortWord = 'c';
    private char currentSortWay = 'u';

    private ArrayList<Food> shoppingList;
    private static Displayer displayer;

    private ArrayList<Discount> discounts;
    private ArrayList<PdfGenerator> pdfGenerators;

    private final String pdfRootPath = "reports/";

    // 全局使用一个单一的scanner
    private final Scanner scanner = new Scanner(System.in);

    static {
        displayer = new Displayer();
    }

    private Displayer(){
        HtmlInfoRetriever infoRetriever = new HtmlInfoRetriever();
        ArrayList<Food>foodList = infoRetriever.retrieveFoodInfo();
        pizzaList = new ArrayList<>();
        fryList = new ArrayList<>();
        chickenList = new ArrayList<>();
        discounts = new ArrayList<>();
        pdfGenerators = new ArrayList<>();
        for(Food f : foodList) {
            switch(getFoodType(f)){
                case 0:
                    pizzaList.add((Pizza)f);
                    break;
                case 1:
                    fryList.add((Fries)f);
                    break;
                case 2:
                    chickenList.add((FriedChicken)f);
                    break;
                default:
                    break;
            }
        }
        shoppingList = new ArrayList<>();
        discounts.add(new HalfPriceDiscount());
        discounts.add(new Minus50Discount());
        pdfGenerators.add(new iTextPdfGenerator("iTextGenerator", pdfRootPath + "iTextReport.pdf"));
        pdfGenerators.add(new PdfBoxGenerator("PdfBoxGenerator", pdfRootPath + "PdfBoxReport.pdf"));
    }

    public static Displayer getInstance() {
        if(displayer == null) {
            displayer = new Displayer();
        }
        return displayer;
    }

    public void startMenu() {
        boolean running = true;
        int choice = 0;
        while(running) {
            System.out.println("欢迎来到餐厅！请选择您的操作：");
            System.out.println("1. 查看所有披萨");
            System.out.println("2. 查看所有薯条");
            System.out.println("3. 查看所有炸鸡");
            System.out.println("4. 查看购物车");
            System.out.println("5. 退出系统");
            
            if(scanner.hasNext()) {
                if(scanner.hasNextInt()){
                    choice = scanner.nextInt();
                }
                else{
                    String text = scanner.next();
                    System.out.printf("输入内容 %s 非数字, 无效, 请重新输入!\n", text);
                    continue;
                }
            }
            switch(choice) {
                case 1:
                    currentList = pizzaList;
                    displayFoodList(0);
                    break;
                case 2:
                    currentList = fryList;
                    displayFoodList(1);
                    break;
                case 3:
                    currentList = chickenList;
                    displayFoodList(2);
                    break;
                case 4:
                    displayShoppingCart();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("无效的选择，请重试。");
            }
        }
        scanner.close();
    }

    private void printShoppingCart() {
        int i = 1;
        for(Food food : shoppingList) {
            System.out.println(i + ". " + food.toString());
            i++;
        }
    }

    private void displayShoppingCart() {
        boolean running = true;
        while(running) {
            System.out.println("购物车内容：");
            printShoppingCart();
            System.out.println("请输入您的操作：");
            System.out.println("1. 输入q返回主菜单界面");
            System.out.println("2. 输入序号去除特定商品");
            System.out.println("3. 输入p选择折扣方案并结算");

            if(scanner.hasNextInt()) {
                int index = scanner.nextInt();
                if(index >= 1 && index <= shoppingList.size()) {
                    Food removedFood = shoppingList.remove(index - 1);
                    System.out.printf("已将%s从购物车中移除！\n", removedFood.getName());
                } else {
                    System.out.println("无效的序号，请重试。");
                }
            }
            else {
                String input = scanner.next().toLowerCase();
                if(input.length() != 1) {
                    System.out.println("无效的输入，请重试。");
                    continue;
                }
                else {
                    char choice = input.charAt(0);
                    switch(choice) {
                        case 'q':
                            running = false;
                            break;
                        case 'p':
                            handlePayment();
                            running = false;
                            break;
                        default:
                            System.out.println("无效输入,请重试.");
                            break;
                    }
                }
            }
        }
    }

    private void handlePayment() {
        boolean running = true;
        while(running) {
            System.out.println("请选择折扣方案：");
            for(Discount d : discounts) {
                System.out.println((discounts.indexOf(d) + 1) + ". " + d.description);
            }

            if(scanner.hasNextInt()) {
                int index = scanner.nextInt();
                if(index >= 1 && index <= discounts.size()) {
                    Discount selectedDiscount = discounts.get(index - 1);
                    Discount.PaybillResult result = selectedDiscount.caculateResult(shoppingList);
                    printShoppingCart();
                    System.out.printf("使用折扣方案%s后，\n您的总费用为：%.2f，总热量为：%d\n", selectedDiscount.description, result.getTotalCost(), result.getTotalCalory());
                    generatePdfReport(selectedDiscount, result);
                    shoppingList.clear();
                    running = false;
                } else {
                    System.out.println("无效的序号，请重试。");
                }
            }
        }
    }

    private void generatePdfReport(Discount selectedDiscount, Discount.PaybillResult result) {
        boolean running = true;
        while(running){
            System.out.println("请选择PDF生成器：");
            for(PdfGenerator p : pdfGenerators) {
                System.out.println((pdfGenerators.indexOf(p) + 1) + ". " + p.name);
            }

            if(scanner.hasNextInt()) {
                int index = scanner.nextInt();
                if(index >= 1 && index <= pdfGenerators.size()) {
                    PdfGenerator selectedGenerator = pdfGenerators.get(index - 1);
                    // 直接传递购物车、折扣、结算结果
                    selectedGenerator.generatePdf(selectedDiscount, result, shoppingList);
                    running = false;
                } else {
                    System.out.println("无效的序号，请重试。");
                }
            }
        }
    }


    private void printCurrentFoods() {
        int i = 1;
        for(Food food : currentList) {
            System.out.println(i + ". " + food.toString());
            i++;
        }
    }

    private void displayFoodList(int type) {
        boolean running = true;
        Character choice = 'a';
        while(running){
            printCurrentFoods();
            System.out.println("请输入您的操作：");
            System.out.println("1. 输入商品前的序号以将其加入购物车");
            System.out.println("2. 输入c按热量排序, 输入p按价格排序");
            System.out.println("3. 输入u升序排序, 输入d降序排序");
            System.out.println("4. 输入q返回主菜单界面");

            if(scanner.hasNextInt()){
                int index = scanner.nextInt();
                if(index >= 1 && index <= currentList.size()) {
                    Food selectedFood = currentList.get(index - 1);
                    shoppingList.add(selectedFood);
                    System.out.printf("已将%s加入购物车！\n", selectedFood.getName());
                } else {
                    System.out.println("无效的序号，请重试。");
                }
            }
            else {
                String input = scanner.next().toLowerCase();
                if(input.length() != 1) {
                    System.out.println("无效的输入，请重试。");
                    continue;
                }
                else {
                    choice = input.charAt(0);
                    switch(choice) {
                        case 'q':
                            running = false;
                            break;
                        case 'c':
                        case 'p':
                            changeSortWord(choice);
                            break;
                        case 'u':
                        case 'd':
                            changeSortWay(choice);
                            break;
                        default:
                            System.out.println("无效输入,请重试.");
                            break;
                    }
                }
            }
        }
    }

    private void changeSortWord(char c) {
        if(c != 'c' && c != 'p') {
            return;
        }
        currentSortWord = c;
        reSortArrays();
    }

    private void changeSortWay(char c) {
        if(c != 'u' && c != 'd') {
            return;
        }
        currentSortWay = c;
        reSortArrays();
    }

    private void reSortArrays() {
        sortArray(pizzaList);
        sortArray(fryList);
        sortArray(chickenList);
    }

    private void sortArray(ArrayList<? extends Food> list) {
        Collections.sort(list, (food1, food2) -> {
            int comparison = 0;
            if(currentSortWord == 'c') {
                comparison = Integer.compare(food1.getCalory(), food2.getCalory());
            } else if (currentSortWord == 'p') {
                comparison = Double.compare(food1.getPrice(), food2.getPrice());
            }

            if(currentSortWay == 'u') {
                return comparison;
            } else {
                return -comparison;
            }
        });
    }

    /*
     * type: 0 - pizza
     *       1 - french fries
     *       2 - fried chicken
     */
    private int getFoodType(Food food) {
        if(food instanceof com.reisen.cw.Food.Pizza)
            return 0;
        else if (food instanceof com.reisen.cw.Food.Fries)
            return 1;
        else if (food instanceof com.reisen.cw.Food.FriedChicken)
            return 2;
        return -1;
    }
}
