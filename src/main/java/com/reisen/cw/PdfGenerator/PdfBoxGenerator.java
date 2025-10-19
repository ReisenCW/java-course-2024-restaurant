package com.reisen.cw.PdfGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import com.reisen.cw.Discount.Discount;
import com.reisen.cw.Food.Food;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.font.encoding.StandardEncoding;

public class PdfBoxGenerator extends PdfGenerator {
    private String fontPath = "simkai.ttf";
    public PdfBoxGenerator(String name, String fileName) {
        this.name = name;
        this.fileName = fileName;
    }

    @Override
    public void generatePdf(Discount selectedDiscount, Discount.PaybillResult result, List<Food> shoppingList) {
        System.out.println("Generating PDF using PDFBox with content...");
        String content = buildContent(selectedDiscount, result, shoppingList);
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 700);
                InputStream fontStream = getClass().getClassLoader().getResourceAsStream(fontPath);
                if (fontStream != null) {
                    PDTrueTypeFont font = PDTrueTypeFont.load(document, fontStream, StandardEncoding.INSTANCE);
                    contentStream.setFont(font, 12);
                }
                // 按行写入，避免字体不支持换行符
                for (String line : content.split("\\n")) {
                    contentStream.showText(line);
                    contentStream.newLineAtOffset(0, -16); // 向下移动一行
                }
                contentStream.endText();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            document.save(fileName);
        }
        catch (Exception e) {
            System.err.println("Error generating PDF: " + e.getMessage());
        }
    }
}
