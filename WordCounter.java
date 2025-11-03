import java.io.*;
import java.util.*;
import java.util.regex.*;

public class WordCounter {

    // This is the method your tester expects
    public static int processText(StringBuffer textBuffer, String targetWord) 
            throws InvalidStopwordException, TooSmallText {
        if (textBuffer == null) {
            textBuffer = new StringBuffer("");
        }

        Pattern wordPattern = Pattern.compile("[a-zA-Z0-9']+");
        Matcher matcher = wordPattern.matcher(textBuffer);

        int totalWords = 0;
        int wordsUntilTarget = 0;
        boolean targetFound = false;

        while (matcher.find()) {
            String currentWord = matcher.group();
            totalWords++;
            if (!targetFound) {
                wordsUntilTarget++;
                if (targetWord != null && currentWord.equalsIgnoreCase(targetWord)) {
                    targetFound = true;
                }
            }
        }

        if (totalWords < 5) {
            throw new TooSmallText("Only found " + totalWords + " words.");
        }

        if (targetWord != null && !targetFound) {
            throw new InvalidStopwordException("Couldn't find stopword: " + targetWord);
        }

        return (targetWord != null) ? wordsUntilTarget : totalWords;
    }

    // This is the method your tester expects
    public static StringBuffer processFile(String filePath) throws EmptyFileException {
        return processFile(filePath, new Scanner(System.in));
    }

    public static StringBuffer processFile(String filePath, Scanner consoleScanner) throws EmptyFileException {
        File inputFile = new File(filePath);
        Scanner fileScanner = null;

        while (true) {
            try {
                fileScanner = new Scanner(inputFile);
                break;
            } catch (FileNotFoundException e) {
                System.err.println("File not found. Enter another filename:");
                filePath = consoleScanner.nextLine(); 
                inputFile = new File(filePath);
            }
        }

        String fileContent = "";
        boolean hasContent = false;

        while (fileScanner.hasNextLine()) {
            hasContent = true;
            fileContent += fileScanner.nextLine();
            if (fileScanner.hasNextLine()) {
                fileContent += "\n";
            }
        }
        fileScanner.close();

        if (!hasContent) {
            throw new EmptyFileException(filePath + " was empty");
        }

        return new StringBuffer(fileContent);
    }

   
}
