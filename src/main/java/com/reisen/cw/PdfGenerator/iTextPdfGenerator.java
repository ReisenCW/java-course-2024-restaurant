package com.reisen.cw.PdfGenerator;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.reisen.cw.Discount.Discount;
import com.reisen.cw.Food.Food;
import java.util.List;

public class iTextPdfGenerator extends PdfGenerator {
    public iTextPdfGenerator(String name, String fileName) {
        this.name = name;
        this.fileName = fileName;
    }
    @Override
    public void generatePdf(Discount selectedDiscount, Discount.PaybillResult result, List<Food> shoppingList) {
        System.out.println("Generating PDF using iText with content...");
        String content = buildContent(selectedDiscount, result, shoppingList);
        try(PdfDocument pdf = new PdfDocument(new PdfWriter(fileName))){
            Document document = new Document(pdf);
            document.add(new Paragraph(content));
            document.close();
        }catch(Exception e){
            System.err.println("Error generating PDF: " + e.getMessage());
        }
    }
}
