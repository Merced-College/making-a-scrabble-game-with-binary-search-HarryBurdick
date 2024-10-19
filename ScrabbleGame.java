// Group members: Harry Burdick, Connor Reilly, Kylie Scheibli
// Date: 10-18-2024
// CPSC-39-12112


// Improvements
// - canFormWord method is used to validate user input: Checks to see if user input uses letters provided from the random letter generated string
// - swapLetter method is used to allow user to swap letters given: Allows user to swap a letter provided from the random letter generated string



import java.io.*;
import java.util.*;

public class ScrabbleGame {
    private ArrayList<Word> wordsList;
    private int swapCount; // Track the number of swaps used by user - Enhancement -- Harry

    // method to take in file as ArrayList -- Harry/ChatGPT
    public ScrabbleGame(String fileName) {
        wordsList = new ArrayList<>(); //Creates ArrayList
        loadWords(fileName); // loads words using loadWords method
        //System.out.println(wordsList); -- debug printout for wordList array -- Harry
    }

    // Loads words from file into the ArrayList -- Harry/ChatGPT assist
    private void loadWords(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                wordsList.add(new Word(line.trim()));
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // Generate 4 random letters -- Kylie
    public String generateRandomLetters() {
        Random rand = new Random(); // Initializes a new random object, which is used to generate random #'s
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Contains all uppercase letters of alphabet, randomly picks 
        StringBuilder sb = new StringBuilder(4); // Stringbuilder used to build string of 4 characters
        
        // 
        for (int i = 0; i < 4; i++) {// A loop that runs 4 times (for each letter)
            sb.append(alphabet.charAt(rand.nextInt(alphabet.length())));//Generate random integer between 0 and length of alphabet (26), making sure we select a valid index from alphabet string / Appends a random letter from the alphabet
        }
        return sb.toString();//Converts the StringBuilder to a String and returns it The for loop will build a string with the letters generated
    }

    // Method to check if user created word is valid -- Connor
    public boolean isValidWord(String input) {
        return binarySearch(new Word(input.toUpperCase()));
    }

    // Binary search method implementation -- Connor
    private boolean binarySearch(Word target) {
        int low = 0; // Sets low to min array size
        int high = wordsList.size() - 1; // Sets high to max array size

        while (low <= high) { // While loop to check low position vs high
            int mid = low + (high - low) / 2; // defines middle position
            Word midWord = wordsList.get(mid); // defines middle position of array

            int comparison = target.compareTo(midWord); // defines comparison check to compare user input word against midWord

            if (comparison == 0) { // Checks to see if compareTo outputs a zero to verify word has been found
                return true; // Word found
            } else if (comparison < 0) { // Checks to see if compareTo outputs -1 to increment high
                high = mid - 1; // Target is smaller, search in the left half
            } else { // else increment low
                low = mid + 1; // Target is larger, search in the right half
            }
        }
        return false; // Word not found
    }

    // Check if user word can be formed from the given letters - Improvement - assistance from ChatGPT - Harry Burdick
    public boolean canFormWord(String input, String availableLetters) {
        // Convert the input and available letters to char arrays
        char[] inputArray = input.toCharArray();
        char[] letterArray = availableLetters.toCharArray();

        // Create frequency maps to count occurrences of letters
        Map<Character, Integer> letterCount = new HashMap<>();
        for (char c : letterArray) {
            letterCount.put(c, letterCount.getOrDefault(c, 0) + 1);
        }

        // Check if input word can be made with the available letters
        for (char c : inputArray) {
            if (!letterCount.containsKey(c) || letterCount.get(c) == 0) { // check to see if character is not present or if character is used
                return false; // If letter is not in available set or used in word already
            }
            letterCount.put(c, letterCount.get(c) - 1); // Uses up one occurrence of this letter to stop from duplicate uses of a letter if they don't exist
        }

        return true;
    }

    // Allow the user to swap a letter - Improvement -- Harry
    public String swapLetter(String currentLetters, Scanner scanner) {
        Random rand = new Random(); // Generates random number
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Defines alphabet

        System.out.print("Which letter would you like to swap? Enter one of your current letters: ");
        char letterToSwap = scanner.nextLine().toUpperCase().charAt(0); // Only chooses the first letter provided ex. user input = 'gtt' letter chosen to swap is 'g'

        if (currentLetters.indexOf(letterToSwap) == -1) { //checks to see if
            System.out.println("Invalid choice. That letter is not in your current set.");
            return currentLetters; // Invalid choice, since no swap is made
        }

        // Generates a new letter
        char newLetter = alphabet.charAt(rand.nextInt(alphabet.length()));
        System.out.println("Swapping letter '" + letterToSwap + "' with '" + newLetter + "'.");

        // Replace the old letter with the new one in the string
        currentLetters = currentLetters.replaceFirst(String.valueOf(letterToSwap), String.valueOf(newLetter));
        swapCount++; // Increment the swap count

        return currentLetters;
    }

    public static void main(String[] args) {
        ScrabbleGame game = new ScrabbleGame("Collins Scrabble Words (2019).txt");
        Scanner scanner = new Scanner(System.in);
        

        boolean validWordFound = false;

        // Game loop until valid word is found
        while (!validWordFound) {
            // Step 1: Generate random letters and reset swap count
            String randomLetters = game.generateRandomLetters(); // -- Kylie
            game.swapCount = 0;

            while (true) { // Inner loop allows swaps and word checking in the same round
                System.out.println("Your letters are: " + randomLetters);
                System.out.println("You can enter a word or type 'swap' to exchange one letter (up to 3 times).");

                // Step 2: Get input from the user -- Harry
                String userInput = scanner.nextLine().toUpperCase();

                if (userInput.equals("SWAP")) { // Checks to see if user input is equal to 'SWAP' string -- Harry
                    if (game.swapCount >= 3) { // Checks swap count
                        System.out.println("You have used all 3 swaps for this round."); // Output this is swap count is >= 3
                    } else {
                        randomLetters = game.swapLetter(randomLetters, scanner); // Swap user specified letter with new random letter
                    }
                    continue; // Continue the loop after a swap
                }

                // Step 3: Validate if the input word can be formed using the given letters -- Harry/ChatGPT
                if (!game.canFormWord(userInput, randomLetters)) {
                    System.out.println("Invalid input: Your word uses letters that were not provided. Try again.");
                    continue; // Prompt user again for input
                }

                // Step 4: Check if the word is valid in the word list -- Connor
                if (game.isValidWord(userInput)) {
                    System.out.println("Valid word: " + userInput);
                    validWordFound = true; // End the loop when a valid word is found
                    break; // Exit the inner loop
                } else {
                    System.out.println("Invalid word. Try again.");
                }
            }
        }

        scanner.close();
    }
}
