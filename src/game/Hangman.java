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
     * Stores words for this instance.
     */
    private Dictionary words;
    
    /**
     * Stores the current word that is being guessed by the player.
     */
    private String currentWord;
    
    /**
     * Stores the characters that the player has already guessed for relaying to
     * a user interface.
     */
    private String alreadyGuessed;
    
    /**
     * Stores the characters that the player has already correctly guessed, 
     * (i.e. they exist in {@link #currentWord}). This should be relayed to the
     * user interface.
     */
    private String correctGuesses;
    
    /**
     * Stores the amount of character guesses the player has already made.
     */
    private int guessesRemaining;
    
    /**
     * Stores the current image repository for use in a graphical interface.
     */
    private Actor actor;
    
    /**
     * Initializes a new game with medium difficulty 
     * ({@link WordProperties#MEDIUM_WORD}).
     */
    public Hangman() {
        this(WordProperties.MEDIUM_WORD);
    }
    
    /**
     * Initializes a new game with the difficulty setting given.
     *
     * @param difficulty The difficulty for this game.
     */
    public Hangman(WordProperties difficulty) {
        words = new Dictionary();
        actor = Actor.HUMAN;
        guessesRemaining = actor.getImageArray().length;
        resetGame(difficulty);
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
        correctGuesses = StringUtilities.createRepeating(currentWord.length(), '_');
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
    private char sanitizeGuess(char guess) {
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
     * Appends the given character to the end of the {@code String} containing
     * the other characters that have been guessed.
     *
     * @param guess The character guess to append.
     */
    public void appendCorrectGuesses(char guess) {
        char g = sanitizeGuess(guess);
        correctGuesses += guess;
    }
    
    /**
     * Returns the actor for this game instance.
     *
     * @return The actor for this instance.
     */
    public Actor getActor() {
        return actor;
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
        if (contains(currentWord, guess)) {
            if (!contains(alreadyGuessed, g)) {
                alreadyGuessed += g;
                insertCorrectGuess(g);
                return true;
            }
            // This case should never happen if insertCorrectGuess(char) is 
            // working properly.
        }
        else {
            alreadyGuessed += g;
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
     * Tests the state of the game, returning {@code true} if the the user's 
     * correct guesses are the same as the actual word and there are greater 
     * than zero guesses remaining, {@code false} otherwise.
     * 
     * <p> While this method does take into account the amount of guesses the 
     * user has already made, ensure that the user is blocked from making 
     * guesses within the individual context after the amount of guesses 
     * remaining reaches zero.
     * 
     * @return {@code true} if the user's correct guesses are the same as the 
     *         actual word and there are greater than zero guesses remaining, 
     *         {@code false} otherwise.
     */
    public boolean hasWon() {
        return correctGuesses.equals(currentWord) && guessesRemaining > 0;
    }
    
}
