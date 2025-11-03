import java.io.*;
import java.util.*;
import java.util.regex.*;

public class WordCounter {

    public static int Texty(StringBuffer x, String pointy) throws InvalidStopwordException, TooSmallText {
        if (x == null) {
            x = new StringBuffer("");
        }

        Pattern x1 = Pattern.compile("[a-zA-Z0-9']+");
        Matcher wordy = x1.matcher(x);

        int count = 0;
        int loc = 0;
        boolean match = false;

        while (wordy.find()) {
            String cur = wordy.group();
            count++;
            if (!match) {
                loc++;
                if (pointy != null && cur.equalsIgnoreCase(pointy)) {
                    match = true;
                }
            }
        }

        if (count < 5) {
            throw new TooSmallText("failure: only " + count);
        }

        if (pointy != null && !match) {
            throw new InvalidStopwordException("failure: stopword missing");
        }

        return (pointy != null) ? loc : count;
    }

    public static StringBuffer file1(String filePath) throws EmptyFileException {
        return file1(filePath, new Scanner(System.in));
    }

    public static StringBuffer file1(String filePath, Scanner consoleScanner) throws EmptyFileException {
        File inputFile = new File(filePath);
        Scanner fileScanner = null;

        while (true) {
            try {
                fileScanner = new Scanner(inputFile);
                break;
            } catch (FileNotFoundException e) {
                System.err.println("failure: file not found");
                filePath = consoleScanner.nextLine();
                inputFile = new File(filePath);
            }
        }

        StringBuilder fileContentBuilder = new StringBuilder();
        boolean hasContent = false;

        while (fileScanner.hasNextLine()) {
            hasContent = true;
            fileContentBuilder.append(fileScanner.nextLine());
            if (fileScanner.hasNextLine()) fileContentBuilder.append('\n');
        }
        fileScanner.close();

        if (!hasContent) {
            throw new EmptyFileException("failure: empty file");
        }

        return new StringBuffer(fileContentBuilder.toString());
    }

    public static void main(String[] args) {
        Scanner consoleInput = new Scanner(System.in);
        Integer arg1 = null;

        if (args.length >= 1) {
            try {
                int op = Integer.parseInt(args[0]);
                if (op == 1 || op == 2) arg1 = op;
            } catch (NumberFormatException ignored) {}
        }

        int choosy;
        if (arg1 != null) {
            choosy = arg1;
        } else {
            while (true) {
                System.err.println("1=file,2=text");
                try {
                    choosy = Integer.parseInt(consoleInput.nextLine());
                    if (choosy == 1 || choosy == 2) break;
                } catch (Exception ignored) {}
            }
        }

        String pointy = (args.length > 1) ? args[1] : null;
        StringBuffer texty = new StringBuffer("");
        String firsty = (args.length > 0 && choosy == 1) ? args[0] : null;

        try {
            if (choosy == 1) {
                String filePath = (firsty != null) ? firsty : null;
                if (filePath == null) {
                    System.err.print("file:");
                    filePath = consoleInput.nextLine();
                }
                texty = file1(filePath, consoleInput);
            } else {
                String userText = (args.length > 2) ? args[2] : null;
                if (userText == null) {
                    System.out.println("text:");
                    userText = consoleInput.nextLine();
                }
                texty = new StringBuffer(userText);
            }

            int wordCount = Texty(texty, pointy);
            System.out.println("counted: " + wordCount);

        } catch (EmptyFileException e) {
            System.out.println("failure: empty file");
            try {
                Texty(new StringBuffer(""), pointy);
            } catch (TooSmallText smallTextEx) {
                System.out.println("failure: too small");
            } catch (InvalidStopwordException ignored) {}
        } catch (InvalidStopwordException e) {
            System.out.println("failure: stopword");
            System.err.print("retry:");
            pointy = consoleInput.nextLine();
            try {
                int retryCount = Texty(texty, pointy);
                System.out.println("counted: " + retryCount);
            } catch (TooSmallText tooSmallEx) {
                System.out.println("failure: too small");
            } catch (InvalidStopwordException invalidEx) {
                System.out.println("failure: stopword");
            } catch (Exception otherEx) {
                System.out.println("failure: error");
            }
        } catch (TooSmallText e) {
            System.out.println("failure: too small");
        }
    }
}
