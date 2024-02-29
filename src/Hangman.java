import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class Hangman {
    final int MAXIMUM_ERRORS = 6;
    final File dictionary = new File("src/resources/dictionary.txt");
    Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        new Hangman().playTheGame();
    }

    private String getRandomLineFromFile(File f)
    {
        String result = null;
        Random rand = new Random();
        int n = 0;
        try {
            for(Scanner sc = new Scanner(f); sc.hasNext(); )
            {
                ++n;
                String line = sc.nextLine();
                if(rand.nextInt(n) == 0)
                    result = line;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Something went wrong.");
        }
        return result;
    }

    private String getRandomWordFromLine() {
        String line = null;
        while (line == null) {
        line = getRandomLineFromFile(dictionary);}
        String[] wordsFromLine = line.split("\\s+");
        int index = (new Random()).nextInt(wordsFromLine.length-1);
        return wordsFromLine[index];
    }

    private boolean isCorrectCyrillicInput(String input) {
        return input.length()==1 && Pattern.matches(".*\\p{InCyrillic}.*", input);
    }

    private boolean isCorrectLatinInput(String input) {
        return input.length()==1 && Pattern.matches("[yn]", input);
    }

    private String hangmanImage(int errorCounter) {
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

    private void gameLogic() {

        String randomWord = getRandomWordFromLine().toLowerCase();
        StringBuilder hiddenWord = new StringBuilder("_".repeat(randomWord.length()).toLowerCase());
        List<Character> errorLetters = new ArrayList<>();
        List<Character> enteredLetters = new ArrayList<>();
        int errorCount = 0;

        System.out.print("Welcome to the Hangman game: ");

        while (true) {

            System.out.printf("%n%n%s  %n%nHidden word is: %s ", hangmanImage(errorCount), hiddenWord.toString().toUpperCase());
            System.out.printf("%nErrors [%d]: %s ", errorCount, errorLetters);

            if (randomWord.contentEquals(hiddenWord)) {
                System.out.println("\nYou are WIN!");
                break;
            }

            if (errorCount == MAXIMUM_ERRORS) {
                System.out.println("\nYou are LOOSE! Guessed word is " + randomWord.toUpperCase());
                break;
            }

            System.out.print("\nEnter letter: ");
            String input = scanner.nextLine();

            while (!isCorrectCyrillicInput(input)){
                System.out.print("Enter only one cyrillic letter: ");
                input = scanner.nextLine();
            }

            char letter = input.toLowerCase().charAt(0);

            if (enteredLetters.contains(letter)) {
                System.out.printf("Letter \"%s\" has been already entered, enter another! ",letter);
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

    public void playTheGame() {
        boolean isGame = true;
        System.out.print("Do you want to play the Game? (y/n): ");
        Scanner scanner = new Scanner(System.in);

        while (isGame) {
            String input = scanner.nextLine().toLowerCase();
            while (!isCorrectLatinInput(input)){
                System.out.print("Enter only \"y\" or \"n\" : ");
                input = scanner.nextLine().toLowerCase();
            }

            switch (input) {
                case "n" ->
                        isGame = false;
                case "y" -> {
                    gameLogic();
                    System.out.print("Do you want to play again? (y/n): ");
                }
            }
        }
    }
}