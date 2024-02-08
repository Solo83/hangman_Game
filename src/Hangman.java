import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Hangman {
    private static String getRandomLineFromFile() {
        File f = new File("src/resources/dictionary.txt");
        String str = null;
        while (str == null) {

            try (RandomAccessFile rcf = new RandomAccessFile(f, "r");) {
                long rand = (long) (new Random().nextDouble() * f.length());
                rcf.seek(rand);
                rcf.readLine();
                str = new String(rcf.readLine().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            } catch (IOException e) {
                System.out.println("Something went wrong.");
            }
        }

        return str;
    }

    private static String getRandomWordFromLine() {
        String line = getRandomLineFromFile();
        String[] wordsFromLine = line.split("\\s+");
        return Arrays.asList(wordsFromLine).get(new Random().nextInt(wordsFromLine.length));
    }

    private static boolean russianCharsetChecker(char letter) {
         return Character.UnicodeBlock.of(letter).equals(Character.UnicodeBlock.CYRILLIC);
    }

    private static String hangmanImage(int errorCounter) {
        String s = "";
        switch (errorCounter) {
            case 0 ->
                s =
                        """
                                  +---+
                                  |   |
                                      |
                                      |
                                      |
                                      |
                                =========""";

            case 1 ->
                s =
                        """
                                  +---+
                                  |   |
                                  O   |
                                      |
                                      |
                                      |
                                =========""";

            case 2 ->
                s =
                        """
                                  +---+
                                  |   |
                                  O   |
                                  |   |
                                      |
                                      |
                                =========""";

            case 3 ->
                s =
                        """
                                  +---+
                                  |   |
                                  O   |
                                 /|   |
                                      |
                                      |
                                =========""";

            case 4 ->
                s =
                        """
                                  +---+
                                  |   |
                                  O   |
                                 /|\\  |
                                      |
                                      |
                                =========""";

            case 5 ->
                s =
                        """
                                  +---+
                                  |   |
                                  O   |
                                 /|\\  |
                                 /    |
                                      |
                                =========""";

            case 6 ->
                s =
                        """
                                  +---+
                                  |   |
                                  O   |
                                 /|\\  |
                                 / \\  |
                                      |
                                =========""";

        }

        return s;
    }

    private static void gameLogic() {

        String randomWord = getRandomWordFromLine().toLowerCase();
        StringBuilder hiddenWord = new StringBuilder("_".repeat(randomWord.length()).toLowerCase());
        List<Character> errorLetters = new ArrayList<>();
        List<Character> enteredLetters = new ArrayList<>();
        int errorCount = 0;
        Scanner scanner = new Scanner(System.in);

        System.out.print("Welcome to the hangman game: ");

        while (errorCount < 7) {

            System.out.printf("%n%n%s  %n%nHidden word is: %s ", hangmanImage(errorCount), hiddenWord.toString().toUpperCase());
            System.out.printf("%nErrors [%d]: %s ", errorCount, errorLetters);

            if (randomWord.contentEquals(hiddenWord)) {
                System.out.println("\n" + "You are WIN!");
                break;
            }

            if (errorCount == 6) {
                System.out.println("\n" + "You are LOOSE!");
                break;
            }

            System.out.printf("%nEnter letter: ");

            char letter = Character.toLowerCase(scanner.next().charAt(0));;

            while(!russianCharsetChecker(letter)) {
                System.out.println("Enter only Russian Letters!");
                letter = Character.toLowerCase(scanner.next().charAt(0));
            }


            if (enteredLetters.contains(letter)) {
                System.out.printf("%nThis Letter has been already entered, try again! ");
                continue;
            }

            if (randomWord.indexOf(letter) < 0) {
                if (!errorLetters.contains(letter)) {
                    errorLetters.add(letter);
                }
                errorCount++;
            }

            for (int i = 0; i < randomWord.length(); i++) {
                if (randomWord.charAt(i) == letter) {
                    hiddenWord.setCharAt(i, letter);
                }
            }

            if (!enteredLetters.contains(letter)) {
                enteredLetters.add(letter);
            }
        }

    }

    public static void playTheGame() {
        boolean game = true;
        System.out.println("Do you wanna play the Game? y/n ");

        while (game) {
            String choose = String.valueOf(new Scanner(System.in).next().charAt(0)).toLowerCase();
            switch (choose) {
                case "n" ->
                        game = false;
                case "y" -> {
                    gameLogic();
                    System.out.println();
                    System.out.println("Do you wanna play again? y/n ");
                }
            }
        }
    }

    public static void main(String[] args) {

        playTheGame();

    }
}