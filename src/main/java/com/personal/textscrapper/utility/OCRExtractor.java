package com.personal.textscrapper.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

@RestController
@RequestMapping("/textextractor")
public class OCRExtractor {

    @SuppressWarnings("deprecation")
    @GetMapping("/gettext/{fileNumber}")
    public String displayTextfromImage(@PathVariable String fileNumber) {
        
        System.out.println("Inside displayTextFromImage" + fileNumber);
        
        File imageFile = new File("C:\\text_extractor\\input\\" + fileNumber + ".png");
        

        
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:\\text_extractor\\testdata");
        tesseract.setLanguage("eng");
      
        // Use best OCR Engine Mode
        //tesseract.setOcrEngineMode(1);  // LSTM + Legacy mode for best accuracy
        tesseract.setOcrEngineMode(3); // Pure LSTM mode

        // Adaptive recognition for better symbol distinction
        tesseract.setTessVariable("enable_new_adaptive_recognizer", "1");

        // Increase DPI for sharper image processing
        tesseract.setTessVariable("user_defined_dpi", "300");

        // Whitelist characters (to avoid confusion between '$' and '8')
        tesseract.setTessVariable("tessedit_char_whitelist", 
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=|\\<>?/{}[]:;\"',. ");

        // Preserve spaces
        tesseract.setTessVariable("preserve_interword_spaces", "1");

        // Ensure line breaks are retained
        //tesseract.setTessVariable("tessedit_write_images", "true");


        String passages = "";
        //String formattedText = "";
        
        try {
           
            passages = tesseract.doOCR(imageFile);
            System.out.println("Extracted Text: \n" + passages);

            //formattedText = formatTextWithNewLines(extractedText);
            List<String> passageList = RegexTextFormatter.splitPassages(passages);

            // Create folder with the input folder name
            String filePath = createFolder(fileNumber);
            saveToTextFile(passageList, filePath, fileNumber );

        } catch (Exception e) {
            System.err.println("Error during OCR: " + e.getMessage());
        }
        return passages.toString();
    }


    public String createFolder(String folderName) {
        String BASE_DIRECTORY = "C:\\text_extractor\\output\\"; // Base directory

        try {
            // Define the full folder path inside C:\output
            Path folderPath = Paths.get(BASE_DIRECTORY, folderName);

            // Create the directory if it does not exist
            Files.createDirectories(folderPath);

            return folderPath.toAbsolutePath().toString();
        } catch (IOException e) {
            return "Error creating folder: " + e.getMessage();
        }
    }


    private static void saveToTextFile(List<String> passageList, String folderPath, String fileNumber) {
        int fileCounter = 0;
        for (String passage : passageList) {
            fileCounter++;
                
            // Construct the filename
            String fileName = fileCounter + ".txt";
            String filePath = folderPath + "\\" + fileName;
            System.out.println("Creating file: " + filePath);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

                List<String> formattedList = RegexTextFormatter.formatter(passage);
                for (String formattedWord : formattedList) {
                    writer.write(formattedWord + "\n");
                }
                
                //writer.write(passage);
                //writer.newLine(); // Add newline after writing passage
                System.out.println("File saved: " + filePath);
            } catch (IOException e) {
                System.err.println("Error saving file " + filePath + ": " + e.getMessage());
            }
        }
    }


    /**
     * Method to save the extracted text to a text file
     */
   /* 
     private static void saveToTextFile(List<String> passageList, String folderPath, String fileNumber) {
        System.out.println("Folder Path: " + folderPath);
        String filePath = folderPath + "\\";
        System.out.println("filePath: " + filePath);

        // Convert folderPath to Path object
        // Path baseFolderPath = Paths.get(folderPath);

        int fileCounter = 0;
        for (String passage : passageList) {
            fileCounter++;

            // Construct the file path safely
            //Path filePath = baseFolderPath.resolve(fileNumber + "-" + fileCounter + ".txt");


            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + fileNumber + "-"+ fileCounter +".txt"))) {
                List<String> formattedList = RegexTextFormatter.formatter(passage);

                for (String formattedWord : formattedList) {
                    writer.write(formattedWord + "\n");
                }

                writer.write(formattedList.toString());
                System.out.println("Formatted text file saved successfully: " + filePath);

            } catch (IOException e) {
                System.err.println("Error saving text file: " + e.getMessage());
            }
        }

        // try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

        //     writer.write(extractedText);

        //     System.out.println("Formatted text file saved successfully: " + filePath);

        // } catch (IOException e) {
        //     System.err.println("Error saving text file: " + e.getMessage());
        // }

    }
        */

/*
    private static String formatTextWithNewLines(String text) {
        // Regex to match a date in the format DD/MM/YYYY
        String dateRegex = "\\b\\d{2}/\\d{2}/\\d{4}\\b";
        Pattern pattern = Pattern.compile(dateRegex);
        Matcher matcher = pattern.matcher(text);

        int count = 0;
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            count++;
            // After the second date, add three newlines
            if (count == 3  ) {
                matcher.appendReplacement(result, matcher.group() + "\n\n\n");
            }
        }
        matcher.appendTail(result);

        return result.toString();
    }
         */
        
}

    
