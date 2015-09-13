package game;

import language.Dictionary;
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
     * Defines the default word difficulty setting, which includes all words.
     */
    public final static int DEFAULT_DIFFICULTY = 0;
    
    /**
     * Defines the word difficulty setting of "easy."
     */
    public final static int EASY_DIFFICULTY = 1;
    
    /**
     * Defines the word difficulty setting of "medium."
     */
    public final static int MEDIUM_DIFFICULTY = 2;
    
    /**
     * Defines the word difficulty setting of "hard."
     */
    public final static int HARD_DIFFICULTY = 3;
    
    /**
     * Initializes a new game with the default difficulty setting.
     */
    public Hangman() {
        words = new Dictionary();
        actor = Actor.HUMAN;
        guessesRemaining = actor.getImageArray().length;
        initGame(DEFAULT_DIFFICULTY);
    }
    
    /**
     * Default constructor - initializes a new game with the difficulty setting
     * given.
     *
     * @param difficulty    The difficulty setting to use.
     */
    public Hangman(int difficulty) {
        words = new Dictionary();
        actor = Actor.HUMAN;
        //Subtract 1 to avoid ArrayIndexOutOfBoundsException when working with 
        //arrays
        maxGuesses = actor.getImageArray().length - 1;
        initGame(difficulty);
    }
    
    /**
     * Method that initializes a new game.
     *
     * @param difficulty    The difficulty setting to use for this game. If this
     *                      is too small or too large, default to medium
     *                      difficulty.
     */
    public final void initGame(int difficulty) {
        switch(difficulty) {
            case EASY_DIFFICULTY: {
                currentWord = words.getEasyWord();
                break;
            }
            case MEDIUM_DIFFICULTY: {
                currentWord = words.getMediumWord();
                break;
            }
            case HARD_DIFFICULTY: {
                currentWord = words.getHardWord();
                break;
            }
            case ANY_DIFFICULTY: {
                currentWord = words.getWord();
                break;
            }
            default: {
                currentWord = words.getMediumWord();
            }
        }
        correctGuesses = StringUtilities
                .createRepeatingString(currentWord.length(), '_');
        alreadyGuessed = "";
        guessesMade = 0;
    }
    
    /**
     * Accessor method for {@code maxGuesses} field.
     *
     * @return  Returns the {@code maxGuesses} field of this instance.
     */
    public int getMaxGuesses() {
        return maxGuesses;
    }
    
    /**
     * Mutator method for {@code maxGuesses} field.
     *
     * @param guesses   The value to assign to {@code maxGuesses} field of this
     *                  instance.
     */
    public void setMaxGuesses(int guesses) {
        maxGuesses = guesses;
    }
    
    /**
     * Accessor method for {@code words} field.
     *
     * @return  Returns the {@code words} field of this instance.
     */
    public Dictionary getWords() {
        return words;
    }
    
    /**
     * Mutator method for {@code words} field.
     *
     * @param d The value to assign to {@code words} field of this instance.
     */
    public void setWords(Dictionary d) {
        words = d;
    }
    
    /**
     * Accessor method for {@code currentWord} field.
     *
     * @return  Returns the {@code currentWord} field of this instance.
     */
    public String getCurrentWord() {
        return currentWord;
    }
    
    /**
     * Mutator method for {@code currentWord} field.
     *
     * @param word  The value to assign to {@code currentWord} field of this 
     *              instance.
     */
    public void setCurrentWord(String word) {
        currentWord = word;
    }
    
    /**
     * Accessor method for {@code alreadyGuessed} field.
     *
     * @return  Returns the {@code alreadyGuessed} field of this instance.
     */
    public String getAlreadyGuessed() {
        return alreadyGuessed;
    }
    
    /**
     * Mutator method for {@code alreadyGuessed} field.
     *
     * @param guessed   The value to assign to {@code alreadyGuessed} field of
     *                  this instance.
     */
    public void setAlreadyGuessed(String guessed) {
        alreadyGuessed = guessed;
    }
    
    /**
     * Accessor method for {@code correctGuesses} field.
     *
     * @return  Returns the {@code correctGuesses} field of this instance.
     */
    public String getCorrectGuesses() {
        return correctGuesses;
    }
    
    /**
     * Mutator method for {@code correctGuesses} field.
     *
     * @param guesses   The value to assign to {@code correctGuesses} field of
     *                  this instance.
     */
    public void setCorrectGuesses(String guesses) {
        correctGuesses = guesses;
    }
    
    /**
     * Accessor method for {@code guessesMade} field.
     *
     * @return  Returns the {@code guessesMade} field of this instance.
     */
    public int getGuessesMade() {
        return guessesMade;
    }
    
    /**
     * Mutator method for {@code guessesMade} field.
     *
     * @param guesses   The value to assign to {@code guessesMade} field of this
     *                  instance.
     */
    public void setGuessesMade(int guesses) {
        guessesMade = guesses;
    }
    
    /**
     * Accessor method for {@code actor} field.
     *
     * @return  Returns the {@code actor} field of this instance.
     */
    public Actor getActor() {
        return actor;
    }
    
    /**
     * Mutator method for {@code guessesMade} field.
     *
     * @param a The value to assign to {@code guessesMade} field of this 
                instance.
     */
    public void setActor(Actor a) {
        actor = a;
        setMaxGuesses(actor.getImageArray().length - 1);
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
