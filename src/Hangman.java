import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Hangman {

    public static String randomWordFromFile(String path) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = reader.readLine();
        List<String> wordsFromFile = new ArrayList<>();

        while (line != null) {
            String[] wordsFromLine = line.split("\\s+");
            wordsFromFile.addAll(Arrays.asList(wordsFromLine));
            line = reader.readLine();
        }

        Random r = new Random();

        return wordsFromFile.get(r.nextInt(wordsFromFile.size()));
    }

    public static String hangmanImage(int errorCounter) {
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

    public static void gameModule() throws IOException {

        String randomWord = randomWordFromFile("resources/dictionary.txt").toLowerCase();
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

            char letter = Character.toLowerCase(scanner.next().charAt(0));

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

    public static void main(String[] args) throws IOException {

        boolean game = true;
        System.out.println("Do you wanna play the Game? y/n ");

        while (game) {

            String choose = String.valueOf(new Scanner(System.in).next().charAt(0)).toLowerCase();

            switch (choose) {
                case "n" ->
                    game = false;
                case "y" -> {
                    gameModule();
                    System.out.println();
                    System.out.println("Do you wanna play again? y/n ");
                }
            }
        }
    }
}