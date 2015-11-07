package game;

import functions.StringUtilities;
import javax.swing.ImageIcon;
import language.Dictionary;
import language.Difficulty;
import language.Word;

/**
 * The {@code Hangman} class contains the logic for a game of "Hangman." Game
 * operations, such as updating the game board, are executed through methods
 * contained within this class.
 *
 * <p> This class allows for the selection of word difficulty during
 * construction.
 *
 * @author Oliver Abdulrahim
 * @see language.Dictionary
 * @see language.Word
 */
public final class Hangman {

    /**
     * The place-holding delimiter for the {@code String} that stores correct
     * guesses. Characters that have not been guessed correctly are represented
     * by this character in the same index value within the
     * {@link #correctGuesses} object as in the {@link #currentWord} for this
     * instance.
     *
     * @see #correctGuesses
     */
    private static final char GUESS_DELIMITER = '_';

    /**
     * Stores the maximum amount of hints allowed to any game.
     *
     * @see #hintsLeft
     */
    private static final int MAX_HINTS = 3;

    /**
     * Stores words that are randomly selected for guessing for this instance.
     */
    private Dictionary words;

    /**
     * Stores the word that is being guessed.
     */
    private String currentWord;

    /**
     * Stores the characters have already been guessed. This {@code String}
     * stores all guesses, including those that are incorrect as well as those
     * that are correct.
     */
    private String previouslyGuessed;

    /**
     * Stores the characters have already been guessed correctly, (i.e. they
     * exist in {@link #currentWord}). In other words, this {@code String}
     * stores the union between the current word and the characters that have
     * already been guessed.
     */
    private String correctGuesses;

    /**
     * Stores the amount of character guesses that are left in this game.
     */
    private int guessesLeft;

    /**
     * Stores the amount of hints remaining in this game. This value is defined
     * by half of the length of the current word.
     */
    private int hintsLeft;

    /**
     * Stores the current image repository for use in a graphical interface.
     */
    private Actor actor;

    /**
     * Initializes a new game with medium difficulty
     * ({@link Difficulty#MEDIUM}).
     */
    public Hangman() {
        this(Difficulty.MEDIUM, Actor.HUMAN, new Dictionary());
    }

    /**
     * Initializes a new game with the given attributes.
     *
     * @param difficulty The difficulty for this game.
     * @param actor The actor for this game.
     * @param words The dictionary for this game.
     */
    public Hangman(Difficulty difficulty, Actor actor, Dictionary words) {
        this.words = words;
        this.actor = actor;
        resetGame(difficulty);
    }

    /**
     * Initializes a new game with the given difficulty. This method resets all
     * game-related attributes to their default state.
     *
     * @param d The difficulty setting to use for this game.
     */
    public void resetGame(Difficulty d) {
        currentWord = words.randomWordOf(d).characters();
        int length = currentWord.length();
        correctGuesses = StringUtilities.createRepeating(length,
                GUESS_DELIMITER);
        previouslyGuessed = "";

        guessesLeft = maxGuesses();
        int hints = length / 2;
        hintsLeft = (hints > MAX_HINTS) ? MAX_HINTS : hints;
    }
    
    /**
     * Returns a "sanitized" version a given character guess. This
     * implementation changes the given character to lowercase and assumes that
     * is it alphabetic.
     *
     * @param guess The character to sanitize.
     * @return A sanitized version of the given character.
     * @see Word#sanitizeCharacter(char)
     */
    private static char sanitizeGuess(char guess) {
        return Word.sanitizeCharacter(guess);
    }

// Getters and setters

    /**
     * Returns the current word for this game instance.
     *
     * @return The current word of this instance.
     */
    public String getCurrentWord() {
        return currentWord;
    }

/**
     * Returns a {@code String} containing all the characters that have already
     * been guessed in this game.
     *
     * @return The characters that have already been guessed.
     */
    public String getPreviouslyGuessed() {
        return previouslyGuessed;
    }

    /**
     * Returns a {@code String} containing all the characters that have been
     * guessed correctly in this game instance.
     *
     * @return The characters that have already been guessed correctly.
     */
    public String getCorrectGuesses() {
        return correctGuesses;
    }

    /**
     * Returns a copy of dictionary for this game instance.
     *
     * @return The dictionary for this instance.
     */
    public Dictionary getWords() {
        return new Dictionary(words);
    }

    /**
     * Returns the amount of incorrect guesses remaining for this game.
     *
     * @return The amount of incorrect guesses remaining for this game.
     */
    public int getGuessesLeft() {
        return guessesLeft;
    }

    /**
     * Returns the amount of hints remaining for this game.
     *
     * @return The amount of hints remaining for this game.
     */
    public int getHintsLeft() {
        return hintsLeft;
    }

    /**
     * Returns the images of this game instance.
     *
     * @return The images of this instance.
     */
    public ImageIcon[] images() {
        return actor.getImageArray();
    }
    
    /**
     * Sets the actor for this game instance.
     * 
     * @param actor The actor for this game.
     */
    public void setActor(Actor actor) {
        this.actor = actor;
    }
    
    /**
     * Appends the given character to the end of the {@code String} containing
     * the other characters that have been guessed.
     *
     * @param guess The character guess to append.
     */
    private void appendAlreadyGuessed(char guess) {
        char g = sanitizeGuess(guess);
        previouslyGuessed += g;
    }
    
// Game-state methods
    
    /**
     * Returns {@code true} if this game allows guesses at this point in time,
     * {@code false} otherwise.
     * 
     * @return {@code true} if this game allows guesses at this point in time,
     *         {@code false} otherwise.
     */
    public boolean canGuess() {
        return guessesLeft > 0;
    }
    
/**
     * Returns the maximum amount of guesses allowed to this game instance.
     * 
     * @return The maximum amount of guesses for this game instance.
     */
    public int maxGuesses() {
        return actor.getImageArray().length - 1;
    }
    
    /**
     * Returns the last character that was correctly guessed this game.
     * 
     * @return The last correct character guess.
     */
    public char lastGuess() {
        return previouslyGuessed.charAt(previouslyGuessed.length() - 1);
    }
    
    /**
     * Tests the state of the game, returning {@code true} if the game has been
     * won, {@code false} otherwise.
     *
     * <p> This method tests if the correct guesses are the same as the actual 
     * word and there are greater than zero guesses remaining.
     * 
     * @return {@code true} if the game has been won, {@code false} otherwise.
     */
    public boolean hasWon() {
        return correctGuesses.equals(currentWord) && guessesLeft > 0;
    }
    
    /**
     * Returns the amount of correct guesses required to win this game.
     * 
     * @return The amount of correct guesses required to win this game.
     */
    public int correctGuessesToWin() {
        int guesses = 0;
        String uniques = StringUtilities.reduceToUniques(currentWord);
        for (int i = 0; i < uniques.length(); i++) {
            if (!StringUtilities.contains(correctGuesses, uniques.charAt(i))) {
                guesses++;
            }
        }
        return guesses;
    }
    
    /**
     * Returns {@code true} if the given character has already been guessed, 
     * {@code false} otherwise.
     * 
     * @param guess The character to test.
     * @return {@code true} if the given character has already been guessed, 
     *         {@code false} otherwise.
     */
    public boolean hasGuessed(char guess) {
        sanitizeGuess(guess);
        return StringUtilities.contains(previouslyGuessed, guess);
    }
    
    /**
     * Returns {@code true} if a guess has been made this game, {@code false}
     * otherwise.
     * 
     * @return {@code true} if a guess has been made this game{@code false}
     *         otherwise.
     */
    public boolean hasGuessed() {
        return !previouslyGuessed.isEmpty();
    }
    
// Gameplay methods
    
    /**
     * The entry point for game guesses. Attempts to make the given character
     * guess, returning {@code true} if the guess was made, {@code false} 
     * otherwise.
     *
     * <p> In order to correctly process the given character, this method takes
     * a formulaic approach to either reject or accept the guess. Guesses are
     * processed in the following way:
     *   <ol> 
     *     <li> Sanitizes the guess to ensure that it is uniform in case and
     *          formatting with this instance's current word.
     *     <li> Tests if the guess has already been made, returning
     *          {@code false} if it has.
     *     <li> Otherwise, adds the word to the set of already guessed characters.
     *     <li> Tests if the guess is in the current word.
     *       <ul>
     *          <li> If the given character is in the current word, updates the
     *               set of already guessed characters and returns {@code true}.
     *          <li> Otherwise, decrements the amount of guesses remaining and
     *               returns {@code false}.
     *       </ul>
     *   </ol>
     *
     * <p> In other words, this method will return {@code true} if and only if
     * the guess satisfies all of the following conditions, {@code false} 
     * otherwise:
     *   <ul>
     *     <li> The guess is in the current word and by extension, the game has
     *          not already been won.
     *     <li> The guess is not in the set of already guessed characters.
     *   </ul>
     * 
     * @param guess The character to attempt to guess for.
     * @return {@code true} if the guess was correct, {@code false} otherwise.
     */
    public boolean makeGuess(char guess) {
        char g = sanitizeGuess(guess);
        if (!StringUtilities.contains(previouslyGuessed, g)) {
            appendAlreadyGuessed(g);
            if (StringUtilities.contains(currentWord, g)) {
                insertCorrectGuess(g);
                return true;
            }
            else {
                guessesLeft--;
            }
        }
        return false;
    }
    
/**
     * Places the given guess in the set of correct guesses at any and all index
     * values that it occurs in the current word, all while maintaining these
     * index values between the two {@code String}s.
     *
     * @param guess The character to add to the set of correct guesses based on 
     *        its index occurrence in the current word.
     */
    private void insertCorrectGuess(char guess) {
        for (int i = 0; i < currentWord.length(); i++) {
            if (currentWord.charAt(i) == guess) {
                correctGuesses = correctGuesses.substring(0, i)
                        + guess
                        + correctGuesses.substring(i + 1);
            }
        }
    }
    
    /**
     * Gives a hint at the expense of a move (if one is available), returning
     * {@code true} if a hint was given, {@code false} otherwise. This method
     * always guesses the first character in the current word that has not 
     * already been guessed.
     * 
     * <p> The formal requirements for this method are delineated as follows:
     *   <ol>
     *     <li> The game has not already been won.
     *     <li> There is at least one or more hints remaining.
     *     <li> There is at least two or more guesses remaining.
     *   </ol>
     *
     * <p> In other words, this method will perform a hint operation and return 
     * {@code true} if and only if the current state of the game satisfies all
     * of the following conditions, {@code false} otherwise:
     *   <ul>
     *     <li> All letters of the current word have not been guessed.
     *     <li> The amount of hints available to the game have not already been
     *          used.
     *   </ul>
     * 
     * @return {@code true} if a hint was given, {@code false} otherwise.
     */
    public boolean giveHint() {
        if ((!hasWon() && hintsLeft > 0 && guessesLeft > 1)) {
            char guess = getTheHint();
            appendAlreadyGuessed(guess);
            insertCorrectGuess(guess);
            guessesLeft--;
            hintsLeft--;
            return true;
        }
        return false;
    }
    
    /**
     * Returns a {@code char} representing the first character in the current 
     * word that has not already been guessed.
     * 
     * @return A character hint.
     * @throws IllegalStateException If this method is called after the game has
     *         already ended.
     */
    private char getTheHint() {
        for (int i = 0; i < correctGuesses.length(); i++) {
            if (correctGuesses.charAt(i) == GUESS_DELIMITER) {
                return currentWord.charAt(i);
            }
        }
        throw new IllegalStateException("Called getTheHint() after game ended.");
    }
    
}
