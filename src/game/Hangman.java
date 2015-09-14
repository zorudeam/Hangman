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
        alreadyGuessed += guess;
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
    
    /**
     * Method that delegates a guess operation to the {@code isValidGuess(char)} 
     * method in this class. Returns {@code true} if the guess is valid, 
     * {@code false} if not.
     * 
     * @param guess The character to attempt to guess for.
     * @return      Returns {@code true} if the guess is valid, {@code false} if
     *              not.
     */
    public boolean guessLetter(char guess) {
        guess = Character.toLowerCase(guess);
        boolean validFlag = isValidGuess(guess);
        alreadyGuessed = StringUtilities.sortString(alreadyGuessed);
        return validFlag;
    }
    
    /**
     * Method that attempts to add a guess character to the appropriate words.
     * If the character has already been guessed, do nothing. If the character
     * has not already been guessed but is not in the current word, increment
     * the amount of guesses made and add the letter to the list of guessed 
     * letters. Otherwise update the correct guess display appropriately.
     * 
     * @param guess The character to attempt to guess for.
     * @return      Returns {@code true} if the guess is valid, {@code false} if
     *              not.
     */
    private boolean isValidGuess(char guess) {
        guess = Character.toLowerCase(guess);
        if (alreadyGuessed.contains(String.valueOf(guess))) {
            return false;
        }
        if (!currentWord.contains(String.valueOf(guess))) {
            alreadyGuessed += guess;
            guessesMade++;
            return false;
        }
        else {
            alreadyGuessed += guess;
            correctGuess(guess);
        }
        return true;
    }
    
    /**
     * Method that updates the correct guess display. For each time the given
     * argument appears in the current word, add it to the correct guesses.
     * 
     * @param guess The character to add to the correct display.
     */
    private void correctGuess(char guess) {
        guess = Character.toLowerCase(guess);
        for (int i = 0; i < currentWord.length(); i++) {
            if (currentWord.charAt(i) == guess) {
                correctGuesses = correctGuesses.substring(0, i) 
                               + guess 
                               + correctGuesses.substring(i + 1);
            }
        }
    }
    
    /**
     * Method that checks for the win state of Hangman, or, in other words, when 
     * the user's guesses are equal to the actual word. 
     * 
     * While this method does take into account the amount of guesses the user 
     * has already made, implementations of this class should ensure that the 
     * amount of guesses that a user may make are kept concurrent with the 
     * maximum amount of guesses that are possible for this instance.
     * 
     * @return  Returns {@code true} if the user's correct guesses are the same 
     *          as the actual word, otherwise returns {@code false}.
     */
    public boolean hasWon() {
        return correctGuesses.equalsIgnoreCase(currentWord) 
                && guessesMade <= maxGuesses;
    }
    
}
