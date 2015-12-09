/** reads a file with pinyin with number after the word and replaces word with pinyin with tone mark.
 * uses v instead of ü. to use ü, replace the BufferedReader initialization below; however, then support for Chinese characters is lost.
 * support is missing for some special characters due to BufferedReader replacing them with replacement characters
 * supports initial-final-r-# pattern, called "erhua"
 * new file has same path and extension, but with a - after the file name
 *
 * I used this for Anki so I could simply input the numbered pinyin when making the flashcards.
 * I then exported the notes to a txt and ran this program on the txt. Lastly, I imported the new txt and chose "update notes"
 */

import java.io.*;
import java.util.Scanner;

public class PinyinNumbersToTones {
    final static String[][] lowerToneChars = {{"ā","á", "ǎ", "à"}, {"ē", "é", "ě", "è"}, {"ī", "í", "ǐ", "ì"}, {"ō", "ó", "ǒ", "ò"}, {"ū", "ú", "ǔ", "ù"}, {"ǖ", "ǘ", "ǚ", "ǜ"}};
    final static String[][] upperToneChars = {{"Ā","Á", "Ǎ", "À"}, {"Ē", "É", "Ě", "È"}, {"Ī", "Í", "Ǐ", "Ì"}, {"Ō", "Ó", "Ǒ", "Ò"}, {"Ū", "Ú", "Ǔ", "Ù"}, {"Ǖ", "Ǘ", "Ǚ", "Ǜ"}};
    final static String[] finals = {
            "uang",
            "iang",
            "iong",
            "uai",
            "uan",
            "iao",
            "üan", "ian", "iai", "van",
            "eng", "ang", "ing",
            "üe", "ue", "ui", "uo", "ua", "iu", "ie", "io","ia", "ou", "ao", "ei", "ai", "ve",
            "ün", "ong", "un", "in", "er", "en", "an", "vn",
            "ü", "v", "u", "ê", "e", "o", "a", "i"};

    public static void main(String[] args) throws Exception {
        final String initials = "bpmfdtnlgkhjqxrzcs aoeêyw";

        System.out.println("File to modify: ");
        Scanner userInput = new Scanner(System.in);
        File file = new File(userInput.nextLine());
        /** use this instead if you want to use the ü character; however, it then does not support chinese characters **/
        //final BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file), "iso-8859-1"));
        final BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        String fileNameNoExt = file.getPath().replaceFirst("[.][^.]+$", "");
        File outputFile = new File(fileNameNoExt + "-" + file.getPath().substring(fileNameNoExt.length(), file.getPath().length()));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));

        String nextString;
        StringBuilder next;
        int firstCharIndex, lastCharIndex;

        while ((nextString = input.readLine()) != null) { //doesn't think file has lines
            next = new StringBuilder(nextString);
            for (int i = 0; i < finals.length; i++) { //for each string
                if (next.toString().toLowerCase().contains(finals[i])) { //if final is in string
                    firstCharIndex = next.toString().toLowerCase().indexOf(finals[i]); //get the first char of the final
                    lastCharIndex = firstCharIndex+finals[i].length(); //get the last char of the final (the #)
                    if (next.length() > lastCharIndex && next.charAt(lastCharIndex)-'0' >= 1 && next.charAt(lastCharIndex)-'0' <= 4) {//if next contains the final and final has number 1 to 4 after
                        next.replace(firstCharIndex, lastCharIndex+1, numberToTone(next.substring(firstCharIndex, lastCharIndex+1))); //replace the numbered final with the final with the accent mark
                        i--; //repeat the search for the final (in case 2 of same final in a line)
                    }
                    else if ((next.length() > lastCharIndex+1) && (next.charAt(lastCharIndex) == 'r' || next.charAt(lastCharIndex) == 'R') && (next.charAt(lastCharIndex+1)-'0' >= 1 && next.charAt(lastCharIndex+1)-'0' <= 4)) {//if next contains the final and there is an r between the final and a number 1 to 4
                        next.replace(firstCharIndex, lastCharIndex+2, numberToTone(next.substring(firstCharIndex, lastCharIndex+2))); //replace the final, r, and number with tone set
                        i--; //repeat final
                    }
                }
            }
            System.out.println(next.toString()); //print the finished line
            bufferedWriter.write(next.toString()+"\n"); //write the finished line to the file
        }
        bufferedWriter.close(); //write changes to file
    }

    private static String numberToTone(String pinyin) {
        String vowels = "";
        int index;
        int toneNumber = pinyin.charAt(pinyin.length()-1)-'0'; //get the tone number
        for (int i = 0; i < pinyin.length(); i++) { //get vowels in string
            if ("aeiouüv".contains(Character.toString(pinyin.toLowerCase().charAt(i)))) {
                vowels += pinyin.charAt(i);
            }
        }
        if (vowels.length() == 1 || !"iuüv".contains(Character.toString(vowels.toLowerCase().charAt(0)))) { //if only 1 vowel or first vowel is iuüv
            index = pinyin.indexOf(vowels.charAt(0)); //find index of vowel
            return new StringBuilder(pinyin).replace(index, index+1, replacementChar(vowels.charAt(0), toneNumber)).deleteCharAt(pinyin.length()-1).toString(); //replace vowel with toned vowel, delete #
        }
        else { //first vowel is iuüIUÜ
            index = pinyin.indexOf(vowels.charAt(1)); //get index of second vowel
            return new StringBuilder(pinyin).replace(index, index+1, replacementChar(vowels.charAt(1), toneNumber)).deleteCharAt(pinyin.length()-1).toString(); //replace vowel
        }
    }

    /** such thing as 2d if statements? vowels and uppercase table? **/
    private static String replacementChar(char vowel, int toneNumber) {
        toneNumber--; //1->0, 4->3 for array
        switch (vowel) {
            case 'a':
                return lowerToneChars[0][toneNumber];
            case 'A':
                return upperToneChars[0][toneNumber];
            case 'e':
                return lowerToneChars[1][toneNumber];
            case 'E':
                return upperToneChars[1][toneNumber];
            case 'i':
                return lowerToneChars[2][toneNumber];
            case 'I':
                return upperToneChars[2][toneNumber];
            case 'o':
                return lowerToneChars[3][toneNumber];
            case 'O':
                return upperToneChars[3][toneNumber];
            case 'u':
                return lowerToneChars[4][toneNumber];
            case 'U':
                return upperToneChars[4][toneNumber];
            case 'ü':
                return lowerToneChars[5][toneNumber];
            case 'Ü':
                return upperToneChars[5][toneNumber];
            case 'v':
                return lowerToneChars[5][toneNumber];
            case 'V':
                return upperToneChars[5][toneNumber];
        }
        return "";
    }
}
