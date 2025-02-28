package com.personal.textscrapper.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.*;

public class RegexTextFormatter {

    public static List<String> splitPassages(String passages) {

        // String passages = "Mrs. Celen Beeer Celen.beeer@googlemail.com Binsseld Beeer
        // 02/05/1945 MaleServices Manl Street Bideford, Devon EX392JW England Virgin
        // Tango- 8230230 Vbrl1KN%:#!SE968 HNGxpH790299103WT56 CDMA+GSM Nokia 9106
        // 740326 - 15 - 568008-1 !++$|/-*$-032338-1 Classic SMS+ Master Card Silver 341
        // 09/05/2004 09/05/2007\r\n";
        // String passages = "Mrs. Celen Beeer Celen.beeer@googlemail.com Binsseld Beeer 02/05/1945 MaleServices Manl Street Bideford, Devon EX392JW England Virgin Tango- 8230230 Vbrl1KN%:#!SE968 HNGxpH790299103WT56 CDMA+GSM Nokia 9106 740326 - 15 - 568008-1 !++$|/-*$-032338-1 Classic SMS+ Master Card Silver 341 09/05/2004 09/05/2007\r\n"
        //         + //
        //         "\r\n" + //
        //         "\r\n" + //
        //         "Mrs. sadadan Beeer Celen.beeer@googlemail.com Binsseld Beeer 02/05/1945 MaleServices \r\n" + //
        //         "Manl Street Bideford, Devon EX392JW England Virgin Tango- 8230230 Vbrl1KN%:#!SE968 HNGxpH790299103WT56 CDMA+GSM Nokia 9106 740326 - 15 - 568008-1 !++$|/-*$-032338-1 Classic SMS+ Master Card Silver 341 09/05/2008 09/05/2009";

        // String filePath = "C:\\text_extractor\\output\\";

        // Break the passages into passage[] each containing text until the 2 dates as
        // their last arrays
        List<String> passageList = splitPassageOnConsecutiveDates(passages);

        return passageList;

    }

    public static List<String> formatter(String text) {
        String formattedString = "Shri NatarajanKamakshi Namaha";

        List<String> wordList = new ArrayList<String>();
        String[] splitWords = text.split("\\s+");
        String[] splitText = null;
        String remainingText = null;
        String[] textSplitAtNetworkType = null;

        if (splitWords != null && splitWords.length >= 10) {
            wordList.add("FileNo");
            wordList.add("FormNo");
            wordList.add(splitWords[0]); // Title
            wordList.add(splitWords[1]); // First Name
            wordList.add(splitWords[2]); // Last Name
            wordList.add("" + splitWords[0].charAt(0) + splitWords[1].charAt(0) + splitWords[2].charAt(0));
            wordList.add(splitWords[3]); // email
            wordList.add(splitWords[4] + " " + splitWords[5]); // Father's full name
            wordList.add(splitWords[6]); // DOB

            // Check for Gender
            splitText = splitMaleFemale(splitWords[7]);
            if (splitText != null) {
                if (splitText.length == 2) {
                    wordList.add(splitText[0]); // Gender
                    wordList.add(splitText[1]); // Job
                    remainingText = text.substring(text.indexOf(splitWords[8])); // Remaining Text after Job
                } else {
                    wordList.add(splitText[0]); // Gender
                    wordList.add(splitWords[8]); // Job
                    remainingText = text.substring(text.indexOf(splitWords[9])); // Remaining Text after Job
                }
            }

            // Split Text at Network Type and store the Before / Current / After texts
            textSplitAtNetworkType = splitNetworkType(remainingText);

            String textBeforeNetworkType = null;
            String remainingTextAfterNetworkType = null;
            String[] splitTextBeforeNetworkType = null;
            String textBeforeFileNo = null;
            if (textSplitAtNetworkType != null) {
                if (textSplitAtNetworkType.length == 3) {
                    textBeforeNetworkType = textSplitAtNetworkType[0]; // Text before Network Type
                    // Call the method to split textBeforeNetworkType to store textBeforeFileNo,
                    // FileNo and SimNo
                    {
                        splitTextBeforeNetworkType = textBeforeNetworkType.split("\\s+"); // Split by spaces

                        // Get remaining words
                        textBeforeFileNo = String.join(" ",
                                java.util.Arrays.copyOfRange(splitTextBeforeNetworkType, 0,
                                        splitTextBeforeNetworkType.length - 2));
                        wordList.add(textBeforeFileNo);
                        wordList.add(splitTextBeforeNetworkType[splitTextBeforeNetworkType.length - 2]); // textBeforeNetworkType
                        wordList.add(splitTextBeforeNetworkType[splitTextBeforeNetworkType.length - 1]); // Network Type
                    }

                    // wordList.add(textBeforeNetworkType); // textBeforeNetworkType
                    wordList.add(textSplitAtNetworkType[1]); // Network Type
                    remainingTextAfterNetworkType = textSplitAtNetworkType[2]; // Remaining Text after Network Type

                    //
                    // splitText[2] contains text after CDMA/GSM
                    // Extract the last 2 words in the BeforeText that contains Ref No and File No.
                    // Remaining all can be part of remainingBeforeText

                    // wordList.add(splitText[splitText.length - 2]); // File No
                    // wordList.add(splitText[splitText.length - 1]); // Ref No
                } else {
                    // remainingText = splitText[0]; // Remaining Text after Job, incase Network
                    // Type is not present }
                    System.out.println("..... Error - Network Type not present .....");
                }
            }

            // Extract the last 3 words that contain the Price, Issue Date and Expiry Date.
            // Remaining all can be part of remainingBeforeText
            String textBeforePrice = null;
            splitText = (remainingTextAfterNetworkType != null) ? remainingTextAfterNetworkType.split("\\s+") : null; // Split
                                                                                                                      // spaces

            // Get remaining words
            if (splitText != null) {
                textBeforePrice = String.join(" ", java.util.Arrays.copyOfRange(splitText, 0, splitText.length - 3));

                // Store the remaining text
                wordList.add(textBeforePrice); // TextBeforePrice

                wordList.add(splitText[splitText.length - 3]); // Price
                wordList.add(splitText[splitText.length - 2]); // Issue Date
                wordList.add(splitText[splitText.length - 1]); // Expiry Date
            } else {
                System.out.println("..... Error - Data not present after Network Type .....");
            }

        }

        return wordList;

    }

    public static List<String> splitPassageOnConsecutiveDates(String passage) {
        List<String> passageList = new ArrayList<>();
        StringBuilder currentPassage = new StringBuilder();

        List<String> words = new ArrayList<>(Arrays.asList(passage.split("\\s+"))); // Split by spaces
        Pattern datePattern = Pattern.compile("\\b\\d{2}/\\d{2}/\\d{4}\\b"); // Strict DD/MM/YYYY pattern

        boolean lastWasDate = false;

        for (String word : words) {

            currentPassage.append(word).append(" ");

            Matcher matcher = datePattern.matcher(word);
            if (matcher.matches()) {
                if (lastWasDate) {
                    // Two consecutive dates found → Start a new passage

                    passageList.add(currentPassage.toString().trim());
                    // Reset the flags
                    lastWasDate = false;
                    currentPassage = new StringBuilder();
                }
                lastWasDate = true;
            } else {
                lastWasDate = false;
            }
        }

        // Add the final passage if not empty
        if (!currentPassage.isEmpty()) {
            passageList.add(currentPassage.toString().trim());
        }

        return passageList;
    }

    // Split Gender and Services
    public static String[] splitMaleFemale(String word) {
        // Case-insensitive regex pattern to find "Male" or "Female"
        Pattern pattern = Pattern.compile("(?i)(Male|Female)");
        Matcher matcher = pattern.matcher(word);

        if (matcher.find()) {
            String prefix = matcher.group(1); // Extracts "Male" or "Female"
            String suffix = word.substring(matcher.end()); // Extracts the remaining part after "Male" or "Female"
            return new String[] { prefix, suffix };
        } else {
            return new String[] { word, "" }; // No split needed
        }
    }

    // Split Gender and Services
    public static String[] splitNetworkType(String remainingSentence) {
        // Case-insensitive regex pattern to find "Male" or "Female"
        Pattern pattern = Pattern.compile("\\b(CDMA\\+CDMA|CDMA\\+GSM|CDMA|GSM)\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(remainingSentence);

        if (matcher.find()) {
            String beforePattern = remainingSentence.substring(0, matcher.start()).trim(); // Text before match
            String matchedPattern = matcher.group(); // The matched network type (CDMA, GSM, etc.)
            String afterPattern = remainingSentence.substring(matcher.end()).trim(); // Text after match

            return new String[] { beforePattern, matchedPattern, afterPattern };
        } else {
            return new String[] { remainingSentence, "", "" }; // No split needed
        }
    }

    private static String extractAndFormat(String text) {
        StringBuilder output = new StringBuilder();

        // Define regex patterns
        Pattern titlePattern = Pattern.compile("^(Ms\\.|Mr\\.|Dr\\.)");
        Pattern firstNamePattern = Pattern.compile("(?<=^Ms\\.|Mr\\.|Dr\\.)\\s+(\\w+)");
        Pattern lastNamePattern = Pattern.compile("(?<=\\s)(\\w+)(?=\\s+\\S+@\\S+)");
        Pattern emailPattern = Pattern.compile("\\S+@\\S+");
        Pattern fatherNamePattern = Pattern.compile("(\\w+)\\s+(\\w+)\\s+(?=\\d{2}/\\d{2}/\\d{4})");
        Pattern dobPattern = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");
        Pattern genderPattern = Pattern.compile("(Male|Female)\\S*");
        Pattern streetPattern = Pattern.compile("(?<=\\S*Male\\S*|Female\\S*)\\s+(.+?)(?=\\s+\\w+,\\s+\\w+)");
        Pattern cityPattern = Pattern.compile("(\\w+,\\s\\w+)");
        Pattern postalPattern = Pattern.compile("(\\w+\\d{2,5}\\s?\\w{3})");
        Pattern countryPattern = Pattern.compile("(?<=\\s\\w+\\s\\d{2,5}\\s?\\w{3}\\s)([A-Za-z]+)");
        Pattern serviceProviderPattern = Pattern
                .compile("\\b(Vodafone|Virgin|Orange|T-Mobile|Hutchison Whampoa|O2)\\b");
        Pattern fileNoPattern = Pattern.compile("(\\w+\\s-\\s\\d+)");
        Pattern refNoPattern = Pattern.compile("([A-Za-z0-9%#@:!]+)");
        Pattern simNoPattern = Pattern.compile("([A-Za-z0-9]+)");
        Pattern networkTypePattern = Pattern.compile("\\b(CDMA|GSM|CDMA\\+CDMA|CDMA\\+GSM)\\b");
        Pattern deviceModelPattern = Pattern.compile("(.+?)(?=\\s+(Visa|Master))");
        Pattern cardNumberPattern = Pattern.compile("\\d+");

        // Extract data
        Matcher matcher;

        matcher = titlePattern.matcher(text);
        if (matcher.find())
            output.append(matcher.group()).append("\n");

        matcher = firstNamePattern.matcher(text);
        if (matcher.find())
            output.append(matcher.group(1)).append("\n");

        matcher = lastNamePattern.matcher(text);
        if (matcher.find())
            output.append(matcher.group(1)).append("\n");

        output.append("XXX").append("\n"); // Replace 4th word with "XXX"

        matcher = emailPattern.matcher(text);
        if (matcher.find())
            output.append(matcher.group()).append("\n");

        matcher = fatherNamePattern.matcher(text);
        if (matcher.find())
            output.append(matcher.group(1)).append(" ").append(matcher.group(2)).append("\n");

        matcher = dobPattern.matcher(text);
        if (matcher.find())
            output.append(matcher.group()).append("\n");

        matcher = genderPattern.matcher(text);
        if (matcher.find())
            output.append(matcher.group()).append("\n");

        matcher = streetPattern.matcher(text);
        if (matcher.find())
            output.append(matcher.group().trim()).append("\n");

        matcher = cityPattern.matcher(text);
        if (matcher.find())
            output.append(matcher.group()).append("\n");

        matcher = postalPattern.matcher(text);
        if (matcher.find())
            output.append(matcher.group()).append("\n");

        matcher = countryPattern.matcher(text);
        if (matcher.find())
            output.append(matcher.group()).append("\n");

        matcher = serviceProviderPattern.matcher(text);
        if (matcher.find())
            output.append(matcher.group()).append("\n");

        matcher = fileNoPattern.matcher(text);
        if (matcher.find())
            output.append(matcher.group()).append("\n");

        matcher = refNoPattern.matcher(text);
        if (matcher.find())
            output.append(matcher.group()).append("\n");

        matcher = simNoPattern.matcher(text);
        if (matcher.find())
            output.append(matcher.group()).append("\n");

        matcher = networkTypePattern.matcher(text);
        if (matcher.find())
            output.append(matcher.group()).append("\n");

        matcher = deviceModelPattern.matcher(text);
        if (matcher.find())
            output.append(matcher.group().trim()).append("\n");

        matcher = cardNumberPattern.matcher(text);
        if (matcher.find())
            output.append(matcher.group()).append("\n");

        return output.toString();
    }

}
