package com.reisen.cw.Food;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;


public class HtmlInfoRetriever implements InfoRetriever{
    // Load from classpath root (src/main/resources/food.html -> /food.html)
    final private String FILE_PATH = "/food.html";

    @Override
    public ArrayList<Food> retrieveFoodInfo() {
        ArrayList<Food> foodList = new ArrayList<Food>();
        Document doc = getHtmlDocument();
        if(doc != null) {
            // 提取标签
            String[] tags = {"pizza", "french-fries", "fried-chicken"};
            for(int i = 0;i < tags.length; i++){
                Elements foodElements = doc.select("div.pizza-menu div." + tags[i]);
                for(Element e : foodElements) {
                    // 获取公共元素
                    ArrayList<Object> commonInfo = getCommonInfo(e);
                    String name = (String) commonInfo.get(0);
                    String feature = (String) commonInfo.get(1);
                    String picture = (String) commonInfo.get(2);
                    double price = (double) commonInfo.get(3);
                    int[] wc = (int[]) commonInfo.get(4);

                    // 特殊元素
                    Object special = getSpecialInfo(e, i);

                    // 创建food并加入列表
                    foodList.add(createFood(name,feature,picture,price,wc, special, i));
                }
            }
        }
        return foodList;
    }

    private Food createFood(String name, String feature, String picture, double price, int[] wc, Object special, int i) {
        switch(i) {
            case 0:
                return new Pizza(name, feature, picture, wc[0], wc[1], price, (int)special);
            case 1:
                return new Fries(name, feature, picture, wc[0], wc[1], price, (String)special);
            case 2:
                return new FriedChicken(name, feature, picture, wc[0], wc[1], price, (String)special);
            default:
                return null;
        }
    }

    /*
     * type: 0 - pizza radius
     *       1 - thickness of fries
     *       2 - spiciness of fried chicken
     */
    private Object getSpecialInfo(Element e, int type) {
        Elements p = e.select("p");
        String text = p.get(2).text();
        switch (type) {
            case 0: // pizza radius
                String rText = text.substring(text.indexOf(":") + 2, text.lastIndexOf(" inches"));
                return Integer.parseInt(rText);
            case 1: // fries thickness
                String fText = text.substring(text.indexOf(":") + 2);
                return fText;
            case 2: // fried chicken spiciness
                String cText = text.substring(text.indexOf(":") + 2);
                return cText;
            default:
                return null;
        }
    }

    private ArrayList<Object> getCommonInfo(Element e) {
        String name = e.select("h2").text();
        String feature = e.select("p.features").text();
        String picture = e.select("img").attr("src");
        double price = Double.parseDouble(e.select("p.price").text().substring(1));
        Elements p = e.select("p");
        int[] wc = new int[2]; // weight, calory
        String[] affix = {"g", " kcal"};
        for(int i = 0;i < 2;i ++){
            String text = p.get(i).text();
            text = text.substring(text.indexOf(":") + 2, text.lastIndexOf(affix[i]));
            wc[i] = Integer.parseInt(text);
        }
        ArrayList<Object> info = new ArrayList<>();
        info.add(name);
        info.add(feature);
        info.add(picture);
        info.add(price);
        info.add(wc);
        return info;
    }

    private Document getHtmlDocument() {
        try (InputStream inputStream = getClass().getResourceAsStream(FILE_PATH)) {
            if (inputStream == null) {
                System.err.printf("文件 %s 打开失败（未在classpath中找到）\n", FILE_PATH);
                return null;
            }
            return Jsoup.parse(inputStream, "UTF-8", "");
        } catch (IOException e) {
            System.err.printf("Jsoup获取html文件 %s 失败：%s\n", FILE_PATH, e.getMessage());
            return null;
        }
    }

}
