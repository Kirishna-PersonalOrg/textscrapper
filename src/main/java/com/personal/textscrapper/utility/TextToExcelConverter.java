package com.personal.textscrapper.utility;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.*;
import java.util.*;

// Class to convert Text into Excel
public class TextToExcelConverter {

    private static final String BASE_OUTPUT_DIRECTORY = "C:\\text_extractor\\output\\"; // Base directory
    private static final String EXCEL_FILE_PATH = BASE_OUTPUT_DIRECTORY + "output.xlsx"; // Output Excel file

    public static void main(String[] args) {
        writeAllToExcel(BASE_OUTPUT_DIRECTORY, EXCEL_FILE_PATH);
    }

    public static void writeAllToExcel(String baseDirectory, String excelFilePath) {
        Workbook workbook;
        Sheet sheet;
        File excelFile = new File(excelFilePath);

        try {
            // If Excel file exists, load it; otherwise, create a new one
            if (excelFile.exists()) {
                FileInputStream fis = new FileInputStream(excelFile);
                workbook = new XSSFWorkbook(fis);
                fis.close();
                sheet = workbook.getSheet("Extracted Data");

                // If the sheet does not exist, create it
                if (sheet == null) {
                    sheet = workbook.createSheet("Extracted Data");
                    createHeader(sheet);
                }
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Extracted Data");
                createHeader(sheet);
            }

            // Load existing Folder+File names to check for duplicates
            Set<String> existingEntries = new HashSet<>();
            int lastRowNum = sheet.getLastRowNum();

            for (int i = 1; i <= lastRowNum; i++) { // Start from 1 (skip header)
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell folderCell = row.getCell(0);
                    Cell fileCell = row.getCell(1);
                    if (folderCell != null && fileCell != null) {
                        existingEntries.add(folderCell.getStringCellValue() + "|" + fileCell.getStringCellValue());
                    }
                }
            }

            File baseFolder = new File(baseDirectory);
            File[] folders = baseFolder.listFiles(File::isDirectory);

            if (folders == null || folders.length == 0) {
                System.out.println("No folders found in: " + baseDirectory);
                return;
            }

            // Sort folders numerically (1, 2, 3, ...)
            Arrays.sort(folders, Comparator.comparingInt(f -> Integer.parseInt(f.getName())));

            int rowNum = lastRowNum + 1; // Start after last written row
            boolean newDataAdded = false; // Track if new records were added

            for (File folder : folders) {
                File[] textFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

                if (textFiles == null || textFiles.length == 0) {
                    continue;
                }

                // Sort files numerically inside each folder
                Arrays.sort(textFiles, Comparator.comparingInt(f -> Integer.parseInt(f.getName().replace(".txt", ""))));

                for (File file : textFiles) {
                    String folderName = folder.getName();
                    String fileName = file.getName();
                    String uniqueKey = folderName + "|" + fileName;

                    // Skip if this entry already exists
                    if (existingEntries.contains(uniqueKey)) {
                        continue;
                    }

                    List<String> lines = Files.readAllLines(file.toPath());
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(folderName); // Column 1: Folder ID
                    row.createCell(1).setCellValue(fileName); // Column 2: File Name

                    for (int i = 0; i < lines.size(); i++) {
                        row.createCell(i).setCellValue(lines.get(i)); // Data starts from column 3
                    }

                    // Add to the existing entries set
                    existingEntries.add(uniqueKey);
                    newDataAdded = true;
                }
            }

            // Write back to the Excel file only if new data was added
            if (newDataAdded) {
                try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
                    workbook.write(fileOut);
                }
                System.out.println("Excel file updated successfully: " + excelFilePath);
            } else {
                System.out.println("No new data found to add.");
            }

        } catch (IOException e) {
            System.err.println("Error writing to Excel: " + e.getMessage());
        }
    }

    private static void createHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Folder No");
        headerRow.createCell(1).setCellValue("File Name");

        for (int i = 0; i < 28; i++) {
            headerRow.createCell(i + 2).setCellValue("Column " + (i + 1));
        }
    }
}
