package com.personal.textscrapper.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.personal.textscrapper.dto.OCRDataDTO;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@RestController
@RequestMapping("/textextractorold")
public class OCRExtractorold1 {

    @GetMapping("/gettext/{id}")
    public String displayTextfromImage(@PathVariable int id) {
        System.out.println("Inside displayTextFromImage" + id);
        String imageFilePath = "C:\\text_extractor\\input\\" + id + ".png";
        System.out.println("Path = " + imageFilePath);
        File imageFile = new File(imageFilePath); // Replace with your image path
        ITesseract tesseract = new Tesseract();

        String extractedText = "";

        tesseract.setDatapath("C:\\testdata");
        tesseract.setLanguage("eng"); // Set English as the language

        try {
            extractedText = tesseract.doOCR(imageFile);

            OCRDataDTO dto = processExtractedText(extractedText);
            System.out.println("DTO Data: " + dto);
            System.out.println("Extracted Text: \n" + extractedText);

            // Save DTO data to Excel
            saveToExcel(dto, "C:\\text_extractor\\output\\" + id + ".xlsx");

        } catch (TesseractException e) {
            System.err.println("Error during OCR: " + e.getMessage());
        }

        return extractedText;
    }

    private static OCRDataDTO processExtractedText(String text) {
        OCRDataDTO dto = new OCRDataDTO();
        String[] words = text.split("\\s+"); // Split words by space

        // Ensure at least 10 words exist
        for (int i = 0; i < Math.min(words.length, 10); i++) {
            switch (i) {
                case 0:
                    dto.setField1(words[i]);
                    break;
                case 1:
                    dto.setField2(words[i]);
                    break;
                case 2:
                    dto.setField3(words[i]);
                    break;
                case 3:
                    dto.setField4(words[i]);
                    break;
                case 4:
                    dto.setField5(words[i]);
                    break;
                case 5:
                    dto.setField6(words[i]);
                    break;
                case 6:
                    dto.setField7(words[i]);
                    break;
                case 7:
                    dto.setField8(words[i]);
                    break;
                case 8:
                    dto.setField9(words[i]);
                    break;
                case 9:
                    dto.setField10(words[i]);
                    break;
            }
        }

        return dto;
    }

    private static void saveToExcel(OCRDataDTO dto, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("OCR Data");

        // Create a row at index 0
        Row row = sheet.createRow(0);

        // Write DTO fields into one row
        row.createCell(0).setCellValue(dto.getField1());
        row.createCell(1).setCellValue(dto.getField2());
        row.createCell(2).setCellValue(dto.getField3());
        row.createCell(3).setCellValue(dto.getField4());
        row.createCell(4).setCellValue(dto.getField5());
        row.createCell(5).setCellValue(dto.getField6());
        row.createCell(6).setCellValue(dto.getField7());
        row.createCell(7).setCellValue(dto.getField8());
        row.createCell(8).setCellValue(dto.getField9());
        row.createCell(9).setCellValue(dto.getField10());

        // Save Excel file
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
            workbook.close();
            System.out.println("Excel file saved successfully: " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving Excel file: " + e.getMessage());
        }
    }
}
