
//This class represents a word from the scrabble word list. -- Connor / Harry

public class Word implements Comparable<Word> {
    private String word; // The string representation of the word

    //Constructor to initialize the word object with a string
    public Word(String word) {
        this.word = word;
    }

    //Getter method to retrieve the word
    public String getWord() {
        return word; // Returns the string representation of the word
    }

    // Method to compare this Word object with another Word object
    @Override
    public int compareTo(Word other) {
        return this.word.compareTo(other.word); // compares the words with the compareTo method
    }

    //Method to provide a string representation of the word object
    @Override
    public String toString() {
        return word; // Returns the word for easy display
    }
}