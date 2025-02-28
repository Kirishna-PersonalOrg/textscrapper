package com.personal.textscrapper.util;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;


    public class KeyBoardSimulator {

    public static void main(String[] args) {
        System.out.println("Inside performKeyBoardSimulation");

        try {
            Robot robot = new Robot();
            String filePath = "C:\\text_extractor\\output\\formatted_file.txt";
            List<String> lines = readLinesFromFile(filePath);

            if (lines.isEmpty()) {
                System.out.println("No lines found in the file.");
                return;
            }

            System.out.println("Typing will start in 7 seconds...");
            Thread.sleep(7000); // Initial delay

            for (int i = 0; i < lines.size(); i++) {
                typeLine(robot, lines.get(i));

                if (i < lines.size() - 1) { // If not the last line
                    Thread.sleep(400); // Delay after each line
                    pressTab(robot); // Move to next field
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> readLinesFromFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.trim());
            }
        }
        return lines;
    }

    private static void typeLine(Robot robot, String line) throws InterruptedException {
        for (char ch : line.toCharArray()) {
            typeCharacter(robot, ch);
            Thread.sleep(300); // Delay between characters
        }
    }

    private static void typeCharacter(Robot robot, char ch) {
        boolean shiftNeeded = Character.isUpperCase(ch) || "!@#$%^&*()_+{}|:\"<>?".indexOf(ch) >= 0;

        if (shiftNeeded) robot.keyPress(KeyEvent.VK_SHIFT);

        int keyCode = getKeyCode(ch);
        if (keyCode != -1) {
            typeKey(robot, keyCode);
        }

        if (shiftNeeded) robot.keyRelease(KeyEvent.VK_SHIFT);
    }

    private static void typeKey(Robot robot, int keyCode) {
        robot.keyPress(keyCode);
        robot.keyRelease(keyCode);
    }

    private static void pressTab(Robot robot) {
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
    }

    private static int getKeyCode(char ch) {
        switch (ch) {
            // Lowercase letters
            case 'a': case 'A': return KeyEvent.VK_A;
            case 'b': case 'B': return KeyEvent.VK_B;
            case 'c': case 'C': return KeyEvent.VK_C;
            case 'd': case 'D': return KeyEvent.VK_D;
            case 'e': case 'E': return KeyEvent.VK_E;
            case 'f': case 'F': return KeyEvent.VK_F;
            case 'g': case 'G': return KeyEvent.VK_G;
            case 'h': case 'H': return KeyEvent.VK_H;
            case 'i': case 'I': return KeyEvent.VK_I;
            case 'j': case 'J': return KeyEvent.VK_J;
            case 'k': case 'K': return KeyEvent.VK_K;
            case 'l': case 'L': return KeyEvent.VK_L;
            case 'm': case 'M': return KeyEvent.VK_M;
            case 'n': case 'N': return KeyEvent.VK_N;
            case 'o': case 'O': return KeyEvent.VK_O;
            case 'p': case 'P': return KeyEvent.VK_P;
            case 'q': case 'Q': return KeyEvent.VK_Q;
            case 'r': case 'R': return KeyEvent.VK_R;
            case 's': case 'S': return KeyEvent.VK_S;
            case 't': case 'T': return KeyEvent.VK_T;
            case 'u': case 'U': return KeyEvent.VK_U;
            case 'v': case 'V': return KeyEvent.VK_V;
            case 'w': case 'W': return KeyEvent.VK_W;
            case 'x': case 'X': return KeyEvent.VK_X;
            case 'y': case 'Y': return KeyEvent.VK_Y;
            case 'z': case 'Z': return KeyEvent.VK_Z;

            // Numbers
            case '0': return KeyEvent.VK_0;
            case '1': return KeyEvent.VK_1;
            case '2': return KeyEvent.VK_2;
            case '3': return KeyEvent.VK_3;
            case '4': return KeyEvent.VK_4;
            case '5': return KeyEvent.VK_5;
            case '6': return KeyEvent.VK_6;
            case '7': return KeyEvent.VK_7;
            case '8': return KeyEvent.VK_8;
            case '9': return KeyEvent.VK_9;

            // Special characters
            case ' ': return KeyEvent.VK_SPACE;
            case '!': return KeyEvent.VK_1;
            case '@': return KeyEvent.VK_2;
            case '#': return KeyEvent.VK_3;
            case '$': return KeyEvent.VK_4;
            case '%': return KeyEvent.VK_5;
            case '^': return KeyEvent.VK_6;
            case '&': return KeyEvent.VK_7;
            case '*': return KeyEvent.VK_8;
            case '(': return KeyEvent.VK_9;
            case ')': return KeyEvent.VK_0;
            case '-': return KeyEvent.VK_MINUS;
            case '_': return KeyEvent.VK_MINUS;
            case '=': return KeyEvent.VK_EQUALS;
            case '+': return KeyEvent.VK_EQUALS;
            case '[': return KeyEvent.VK_OPEN_BRACKET;
            case ']': return KeyEvent.VK_CLOSE_BRACKET;
            case '{': return KeyEvent.VK_OPEN_BRACKET;
            case '}': return KeyEvent.VK_CLOSE_BRACKET;
            case '\\': return KeyEvent.VK_BACK_SLASH;
            case '|': return KeyEvent.VK_BACK_SLASH;
            case ';': return KeyEvent.VK_SEMICOLON;
            case ':': return KeyEvent.VK_SEMICOLON;
            case '\'': return KeyEvent.VK_QUOTE;
            case '"': return KeyEvent.VK_QUOTE;
            case ',': return KeyEvent.VK_COMMA;
            case '<': return KeyEvent.VK_COMMA;
            case '.': return KeyEvent.VK_PERIOD;
            case '>': return KeyEvent.VK_PERIOD;
            case '/': return KeyEvent.VK_SLASH;
            case '?': return KeyEvent.VK_SLASH;

            default: return -1; // Unsupported characters
        }
    }
}

