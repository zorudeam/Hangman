package game;

import language.Dictionary;
import language.WordProperties;
import utilities.functions.StringUtilities;

/**
 * The {@code Hangman} class contains the logic for a game of "Hangman." Game 
 * operations, such as updating the game board, are executed through methods 
 * contained within this class.
 * 
 * <p> This class allows for the selection of word difficulty during 
 * construction.
 * 
 * @author Oliver Abdulrahim
 */
public class Hangman {
    
    /**
     * The delimiter for the {@code String} that stores correct guesses.
     * 
     * @see #correctGuesses
     */
    private static final char CORRECT_GUESS_DELIMITER = '_';
    
    /**
     * Stores words for this instance.
     */
    private Dictionary words;
    
    /**
     * Stores the current word that is being guessed.
     */
    private String currentWord;
    
    /**
     * Stores the characters have already been guessed.
     */
    private String alreadyGuessed;
    
    /**
     * Stores the characters have already been guessed correctly, (i.e. they 
     * exist in {@link #currentWord}).
     */
    private String correctGuesses;
    
    /**
     * Stores the amount of character guesses that are left in this game.
     */
    private int guessesRemaining;
    
    /**
     * Stores the amount of hints remaining in this game. This value is defined
     * by half of the length of the current word.
     */
    private int hintsRemaining;
    
    /**
     * Stores the current image repository for use in a graphical interface.
     */
    private Actor actor;
    
    /**
     * Initializes a new game with medium difficulty 
     * ({@link WordProperties#MEDIUM_WORD}).
     */
    public Hangman() {
        this(WordProperties.MEDIUM_WORD, Actor.HUMAN, new Dictionary());
    }
    
    /**
     * Initializes a new game with the difficulty setting and actor given.
     * 
     * @param difficulty The difficulty for this game.
     * @param actor The actor for this game.
     * @param words The dictionary for this game.
     */
    public Hangman(WordProperties difficulty, Actor actor, Dictionary words) {
        this.words = words;
        this.actor = actor;
        this.guessesRemaining = actor.getImageArray().length - 1;
        resetGame(difficulty);
        this.hintsRemaining = currentWord.length() / 2;
    }
    
    /**
     * Initializes a new game with the given difficulty.
     * 
     * <p> If the given difficulty is invalid, defaults to medium difficulty.
     *
     * @param difficulty The difficulty setting to use for this game. If this
     *        is either {@code null} or an invalid property, defaults to 
     *        {@link WordProperties#MEDIUM_WORD}.
     */
    public final void resetGame(WordProperties difficulty) {
        switch(difficulty) {
            case EASY_WORD: {
                currentWord = words.getEasyWord().characters();
                break;
            }
            case MEDIUM_WORD: {
                currentWord = words.getMediumWord().characters();
                break;
            }
            case HARD_WORD: {
                currentWord = words.getHardWord().characters();
                break;
            }
            default: {
                currentWord = words.getMediumWord().characters();
            }
        }
        correctGuesses = StringUtilities
                .createRepeating(currentWord.length(), CORRECT_GUESS_DELIMITER);
        alreadyGuessed = "";
        
    }
    
    /**
     * Returns a "sanitized" version a given character guess. This 
     * implementation changes the given character to lowercase and assumes that
     * is it alphabetic.
     * 
     * @param guess The character to sanitize.
     * @return A sanitized version of the given character.
     */
    private static char sanitizeGuess(char guess) {
        return Character.toLowerCase(guess);
    }
    
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
    public String getAlreadyGuessed() {
        return alreadyGuessed;
    }
    
    /**
     * Appends the given character to the end of the {@code String} containing
     * the other characters that have been guessed.
     *
     * @param guess The character guess to append.
     */
    public void appendAlreadyGuessed(char guess) {
        char g = sanitizeGuess(guess);
        alreadyGuessed += g;
    }
    
    /**
     * Returns a {@code String} containing all the characters that have been
     * guessed correctly in this game instance.
     * 
     * <p> This returns, in other words, the union between the current word and
     * the characters that have already been guessed.
     *
     * @return The characters that have already been guessed correctly.
     */
    public String getCorrectGuesses() {
        return correctGuesses;
    }
    
    /**
     * Returns the actor for this game instance.
     *
     * @return The actor for this instance.
     */
    public Actor getActor() {
        return actor;
    }
    
    /**
     * Returns the dictionary for this game instance.
     * 
     * @return The dictionary for this instance.
     */
    public Dictionary getWords() {
        return words;
    }
    
    /**
     * Returns the amount of incorrect guesses remaining for this game.
     * 
     * @return The amount of incorrect guesses remaining for this game.
     */
    public int getGuessesRemaining() {
        return guessesRemaining;
    }
    
    /**
     * Returns the amount of hints remaining for this game. This value is 
     * defined by half of the length of the current word.
     * 
     * @return The amount of hints remaining for this game.
     */
    public int getHintsRemaining() {
        return hintsRemaining;
    }
    
    /**
     * Returns {@code true} if this game allows guesses at this point in time,
     * {@code false} otherwise.
     * 
     * @return {@code true} if this game allows guesses at this point in time,
     *         {@code false} otherwise.
     */
    public boolean canGuess() {
        return guessesRemaining > 0;
    }
    
    /**
     * Returns the maximum amount of guesses allowed to this game instance.
     * 
     * @return The maximum amount of guesses allowed to this game instance.
     */
    public final int maxGuesses() {
        return actor.getImageArray().length;
    }
    
    /**
     * Returns the last character guess for this game.
     * 
     * @return The last character guess for this game.
     */
    public char lastGuess() {
        return alreadyGuessed.charAt(alreadyGuessed.length() - 1);
    }
    
    /**
     * Tests the state of the game, returning {@code true} if the correct 
     * guesses are the same as the actual word and there are greater than zero 
     * guesses remaining, {@code false} otherwise.
     * 
     * <p> While this method does take into account the amount of guesses that 
     * have already been made, it is up to the implementor to ensure that guess 
     * input is blocked when the remaining guesses reaches zero.
     * 
     * @return {@code true} if the user's correct guesses are the same as the 
     *         actual word and there are greater than zero guesses remaining, 
     *         {@code false} otherwise.
     */
    public boolean hasWon() {
        return correctGuesses.equals(currentWord) && guessesRemaining > 0;
    }
    
    /**
     * Sets the actor for this game instance.
     * 
     * @param actor The actor for this game.
     */
    public void setActor(Actor actor) {
        this.actor = actor;
    }
    
// Gameplay methods   
    
    /**
     * The entry point for game guesses. Returns {@code true} if the guess was 
     * made, {@code false} otherwise.
     * 
     * <p> In order to correctly process the given character, this method takes 
     * a formulaic approach to either reject or accept the guess. Guesses are 
     * processed in the following way: 
     *   <ol> 
     *     <li> Sanitizes the guess to ensure that it is uniform in case with 
     *          this instance's current word. 
     *     <li> Tests if the guess has already been made. Returns {@code false}
     *          if it has.
     *     <li> Tests if the guess is in the current word. Adds the word to the
     *          set of already guessed characters.
     *       <ul>
     *          <li> If the given character is in the current word, updates the 
     *               set of already guessed characters and returns {@code true}.
     *          <li> If the given character is not in the current word, returns
     *               {@code false}.
     *       </ul>
     *   </ol>
     * 
     * <p> In other words, this method will return {@code true} if and only if 
     * the guess satisfies all of the following conditions, {@code false} 
     * otherwise:
     *   <ul>
     *     <li> The guess is in the current word.
     *     <li> The guess is not in the set of already guessed characters.
     *   </ul>
     * 
     * @param guess The character to attempt to guess for.
     * @return {@code true} if the guess was completed, {@code false} otherwise.
     */
    public boolean makeGuess(char guess) {
        char g = sanitizeGuess(guess);
        if (!contains(alreadyGuessed, g)) {
            appendAlreadyGuessed(g);
            if (contains(currentWord, g)) {
                insertCorrectGuess(g);
                return true;
            }
            else {
                guessesRemaining--;
            }
        }
        return false;
    }
    
    /**
     * Returns {@code true} if the given character has already been guessed, 
     * {@code false} otherwise.
     * 
     * @param guess The character to test.
     * @return {@code true} if the given character has already been guessed, 
     *         {@code false} otherwise
     */
    public boolean hasAlreadyGuessed(char guess) {
        sanitizeGuess(guess);
        return contains(alreadyGuessed, guess);
    }
    
    /**
     * Returns {@code true} if a guess has been made this game, {@code false} 
     * otherwise.
     * 
     * @return {@code true} if a guess has been made this game{@code false} 
     *         otherwise.
     */
    public boolean hasAlreadyGuessed() {
        return !alreadyGuessed.isEmpty();
    }
    
    /**
     * Gives a hint at the expense of a move (if one is available), returning 
     * {@code true} if a hint was given, {@code false} otherwise. This method 
     * will guess from the first character in the current word that has not 
     * already been guessed.
     * 
     * <p> The formal requirements for this method are delineated as follows:
     *   <ol>
     *     <li> The game has not already been won.
     *     <li> There is at least one or more hint remaining.
     *   </ol>
     * 
     * <p> In other words, this method will return {@code true} if and only if 
     * the current state of the game satisfies all of the following conditions,
     * {@code false} otherwise:
     *   <ul>
     *     <li> All letters of the current word have not been guessed.
     *     <li> The amount of hints available to the game have not already been 
     *          used.
     *   </ul>
     * 
     * @return {@code true} if a hint was given, {@code false} otherwise.
     */
    public boolean giveHint() {
        if (canGuess() && !hasWon()) {
            if (hintsRemaining > 0) {
                char guess = getTheHint();
                insertCorrectGuess(guess);
                appendAlreadyGuessed(guess);
                guessesRemaining--;
                hintsRemaining--;
                return true;
            }
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
            if (correctGuesses.charAt(i) == CORRECT_GUESS_DELIMITER) {
                return currentWord.charAt(i);
            }
        }
        throw new IllegalStateException("Called getTheHint() after game ended.");
    }
    
    /**
     * Tests if a given {@code char} occurs at least once in a given 
     * {@code String}.
     * 
     * @param str The {@code String} to test.
     * @param key The character to look for in the given {@code String}.
     * @return {@code true} if the given character is contained in the given 
     *         {@code String}.
     */
    private static boolean contains(String str, char key) {
        for (char c : str.toCharArray()) {
            if (key == c) {
                return true;
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
    
}
