package language;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import utilities.functions.Utilities;

/**
 * The {@code Dictionary} class retrieves all parseable tokens from a given file 
 * and stores them in a dictionary structure. Various operations can be 
 * performed on this map, such as finding the shortest token or the amount of 
 * vowels in any particular string of characters. Additionally, items may be 
 * added or removed at any time.
 * 
 * <p> Any tokens with alphabetical characters contained in the 
 * {@code Dictionary} are converted to lowercase.
 * 
 * @author Oliver Abdulrahim
 */
public class Dictionary {
    
    /**
     * Stores the resource location of the default dictionary.
     */
    public static final InputStream DEFAULT_STREAM = Dictionary.class
            .getResourceAsStream("/resources/dictionary.txt");
    
    /**
     * Stores the amount of parsable lines in the default stream.
     * 
     * @see #DEFAULT_STREAM
     */
    private static final int DEFAULT_STREAM_CONTENTS = 235_886;
    
    /**
     * Contains this object's word dictionary. This object ordered by word 
     * difficulty.
     */
    private final List<Word> words;
    
    /**
     * Stores the previously used difficulty, or {@link Difficulty#MEDIUM} if 
     * there has not been a word previously retrieved by this object.
     */
    private Difficulty difficultyCache; 
    
    /**
     * Stores the index value for the last easy word stored by this object. This
     * value is guaranteed to be the least of the pivots defined by this class.
     * 
     * <p> If this value is equal to {@code -1}, then there are no words of the
     * given difficulty contained within this dictionary
     * 
     * @see #lastMediumIndex
     * @see #lastHardIndex
     */
    private int lastEasyIndex;
    
    /**
     * Stores the index value for the last medium word stored by this object. 
     * This value is guaranteed to be greater than the easy pivot and less than 
     * the hard pivot defined by this class.
     * 
     * <p> If this value is equal to {@code -1}, then there are no words of the
     * given difficulty contained within this dictionary
     * 
     * @see #lastEasyIndex
     * @see #lastHardIndex
     */
    private int lastMediumIndex;
    
    /**
     * Stores the index value for the last hard word stored by this object. This
     * value is guaranteed to be the greatest of the pivots defined by this 
     * class.
     * 
     * <p> If this value is equal to {@code -1}, then there are no words of the
     * given difficulty contained within this dictionary
     * 
     * @see #lastEasyIndex
     * @see #lastMediumIndex
     */
    private int lastHardIndex;
    
    /**
     * Instantiates a new, empty {@code Dictionary} with no words in its map.
     */
    public Dictionary() {
        this(DEFAULT_STREAM);
    }
    
    /**
     * Instantiates a new {@code Dictionary} with the specified {@code String}
     * path.
     * 
     * @param path The {@code String} path for the {@code File} to read into 
     *        this object's map.
     */
    public Dictionary(String path) {
        this(Dictionary.class.getResourceAsStream(path));
    }
    
    /**
     * Instantiates a new {@code Dictionary} and fills this object's map with 
     * the contents of the specified {@code File}. Each token in the file is 
     * parsed and placed into the map in a line-by-line basis.
     *
     * @param target The {@code File} to read into this object's map.
     */
    public Dictionary(InputStream target) {
        // TODO : Handle ArrayList resize better
        words = new ArrayList<>(DEFAULT_STREAM_CONTENTS);
        difficultyCache = Difficulty.MEDIUM;
        lastEasyIndex = -1;
        lastMediumIndex = -1;
        lastHardIndex = -1;
        if (target != null) {
            constructDictionary(target);
        }
    }
    
    /**
     * Each token in the given file is parsed and placed into the map in a 
     * line-by-line basis.
     *
     * @param target The {@code File} to read into this object's map.
     */
    private void constructDictionary(InputStream target) {
        try (Scanner input = new Scanner(target)) {
            while(input.hasNext()) {
                Word w = new Word(input.nextLine());
                insert(w);
            }
        }
    }
    
    /**
     * Returns an enumerated property depending on the difficulty of the given 
     * {@code Word}.
     * 
     * @param w The word to judge the difficulty of.
     * @return An enumerated property depending on the difficulty of the given 
     *         {@code Word}.
     */
    public static Difficulty judgeDifficulty(Word w) {
        Difficulty difficulty = Difficulty.HARD;
        if (isEasyWord(w)) {
            difficulty = Difficulty.EASY;
        }
        else if (isMediumWord(w)) {
            difficulty = Difficulty.MEDIUM;
        }
        return difficulty;
    }
    
// Collection Operations    
    
    /**
     * Inserts the given word in order, depending on its difficulty, into this
     * dictionary.
     * 
     * @param w The word to insert.
     */
    protected void insert(Word w) {
        // Interesting type inference in case statements - did not know that 
        // Java inferred types for enum switch statements.
        int index = -1;
        System.out.println(judgeDifficulty(w));
        switch (judgeDifficulty(w)) {
            case EASY:
                lastEasyIndex++;
                index = lastEasyIndex;
                break;
            case MEDIUM:
                lastMediumIndex++;
                index = lastMediumIndex;
                break;
            case HARD:
                lastHardIndex++;
                index = words.size() - lastHardIndex;
                break;
            // No default case needed, judgeDifficulty(Word) always returns a 
            // Difficulty object
        }
        words.add(index, w);
    }
    
    public static void main(String[] args) {
        Dictionary test = new Dictionary();
    }
    
    /**
     * Removes a given word from this dictionary, returning {@code true} if the
     * word was removed, {@code false} if not, (i.e. the given word does not 
     * exist in this dictionary).
     * 
     * @param w The word to remove.
     * @return {@code true} if the word was removed, {@code false} otherwise.
     */
    protected boolean remove(Word w) {
        boolean removed = words.remove(w);
        if (removed) {
            switch (judgeDifficulty(w)) {
                case EASY:
                    lastEasyIndex--;
                    break;
                case MEDIUM:
                    lastMediumIndex--;
                    break;
                case HARD:
                    lastHardIndex--;
                    break;
                // No default case needed since judgeDifficulty(Word) always 
                // returns a Difficulty object
            }
        }
        return removed;
    }
    
    /**
     * Returns a list containing all words of the given difficulty. This method
     * assumes that the given difficulty
     * 
     * @param difficulty The difficulty of list to retrieve.
     * @return A list containing all words of the given difficulty.
     */
    protected List<Word> getListOf(Difficulty difficulty) {
        if (!hasWordOf(difficulty)) {
            throw new NoSuchWordException("Could not retrieve word.", difficulty);
        }
        int fromIndex = -1;
        int toIndex = fromIndex;
        switch (difficulty) {
            case EASY:
                fromIndex = 0;
                toIndex = lastEasyIndex;
                break;
            case MEDIUM:
                fromIndex = lastEasyIndex;
                toIndex = lastEasyIndex + lastMediumIndex;
                break;
            case HARD:
                fromIndex = words.size() - lastHardIndex;
                toIndex = words.size();
                break;
        }
        // Add 1 to avoid the case that from == to, which would result in an
        // empty SubList (toIndex is exclusive in ArrayList method 
        // subList(fromIndex:int, toIndex:int))
        return new ArrayList<>(words.subList(fromIndex, toIndex + 1));
    }
    
    /**
     * Returns a list containing all words of the previously used difficulty.
     * 
     * @return A list containing all words of the previously used difficulty.
     */
    public List<Word> cacheList() {
        return Collections.unmodifiableList(getListOf(difficultyCache));
    }
    
    /**
     * Returns a list containing all the words of this dictionary.
     * 
     * @return A list containing all the words of this dicitonary.
     */
    public List<Word> getWords() {
        return Collections.unmodifiableList(words);
    }
    
    /**
     * Returns the amount of words currently contained within this object.
     * 
     * @return The amount of words currently contained within this object.
     */
    public int size() {
        return words.size();
    }

// Difficulty tuning variables

    /**
     * Minimum amount of vowels for an easy word.
     */
    private static final int EASY_VOWEL_THRESHOLD = 4;
    
    /**
     * Minimum length for an easy word.
     */
    private static final int EASY_LENGTH_THRESHOLD = 7;
    
    /**
     * Minimum amount of vowels for a medium word.
     */
    private static final int MEDIUM_VOWEL_THRESHOLD = 3;
    
    /**
     * Minimum length for a medium word.
     */
    private static final int MEDIUM_LENGTH_THRESHOLD = 5;
    
// Difficulty and Dictionary Operations
    
    /**
     * Determines if a given word is "easy" in difficulty. Words that are longer
     * in length and have more vowels are generally considered easy words.
     * 
     * @param w The {@code Word} to test for difficulty.
     * @return {@code true} if the given word is considered to be "easy" in
     *         difficulty.
     */
    public static boolean isEasyWord(Word w) {
        return w.vowelCount() >= EASY_VOWEL_THRESHOLD
                && w.length() >= EASY_LENGTH_THRESHOLD;
    }
    
    /**
     * Determines if a given word is "medium" in difficulty.
     * 
     * @param w The {@code Word} to test for difficulty.
     * @return {@code true} if the given word is considered to be "medium" in
     *         difficulty.
     */
    public static boolean isMediumWord(Word w) {
        return w.vowelCount() >= MEDIUM_VOWEL_THRESHOLD
                && w.length() >= MEDIUM_LENGTH_THRESHOLD;
    }
    
    /**
     * Determines if a given word is "hard" in difficulty. Words that are short
     * in length and contain mostly consonants are generally considered hard 
     * words.
     * 
     * @param w The {@code String} to test for difficulty.
     * @return {@code true} if the given word is considered to be "hard" in
     *         difficulty.
     */
    public static boolean isHardWord(Word w) {
        return !isEasyWord(w) && !isMediumWord(w);
    }
    
    /**
     * Returns a random {@code Word} from this object's {@code map}.
     * 
     * @return A random {@code String} from this object's {@code map}.
     */
    public Word getAnyWord() {
        if (!words.isEmpty()) {
            return words.get(Utilities.r.nextInt(words.size()));
        }
        throw new IllegalStateException("There are no words in this dictionary.");
    }
    
    /**
     * Returns a randomly selected word of the given difficulty.
     * 
     * <p> In the case that there are no elements of the specified difficulty 
     * contained within this object, this method throws an 
     * {@code NoSuchWordException}.
     * 
     * @param difficulty The difficulty of the word to return.
     * @return A randomly selected word of the given difficulty.
     */
    protected Word getWordOf(Difficulty difficulty) {
        requireWordOf(difficulty);
        int from = -1;
        int to = from;
        switch (difficulty) {
            case EASY:
                from = 0;
                to = lastEasyIndex;
                break;
            case MEDIUM:
                from = lastEasyIndex;
                to = lastMediumIndex;
                break;
            case HARD:
                from = lastHardIndex;
                to = words.size();
                break;
        }
        return words.get(Utilities.randomInteger(from, to));
    }

    /**
     * Requires that a word of a given difficulty can be retrieved from this
     * dictionary, throwing a {@code NoSuchWordException} if there are no words
     * of the given difficulty stored in this object.
     * 
     * @param difficulty The difficulty to test.
     * @throws NoSuchWordException If there are no words of the given 
     *         difficulty.
     */
    private void requireWordOf(Difficulty difficulty) {
        if (!hasWordOf(difficulty)) {
             throw new NoSuchWordException("Could not retrieve word.", difficulty);
        }
    }
    
    /**
     * Returns {@code true} if this object contains at least one word of the
     * given difficulty, {@code false} otherwise.
     * 
     * @param difficulty The difficulty to test.
     * @return {@code true} if this object contains at least one word of the
     *         given difficulty, {@code false} otherwise.
     */
    private boolean hasWordOf(Difficulty difficulty) {
        boolean hasWordOf = false;
        switch (difficulty) {
            case EASY:
                hasWordOf = lastEasyIndex > -1;
                break;
            case MEDIUM:
                hasWordOf = lastMediumIndex > -1;
                break;
            case HARD:
                hasWordOf = lastHardIndex > -1;
                break;
        }
        return hasWordOf;
    }
    
    /**
     * Returns a randomly selected word of easy difficulty mapped to this 
     * object.
     * 
     * @return A randomly selected word of easy difficulty.
     */
    public Word getEasyWord() {
        return getWordOf(Difficulty.EASY);
    }
    
    /**
     * Returns a randomly selected word of medium difficulty mapped to this 
     * object.
     * 
     * @return A randomly selected word of medium difficulty.
     */
    public Word getMediumWord() {
        return getWordOf(Difficulty.MEDIUM);
    }
    
    /**
     * Returns a randomly selected word of hard difficulty mapped to this 
     * object.
     * 
     * @return A randomly selected word of hard difficulty.
     */
    public Word getHardWord() {
        return getWordOf(Difficulty.HARD);
    }
    
    /**
     * Returns a formatted {@code String} containing this object's map of words.
     * 
     * @return A formatted {@code String} containing this object's map.
     */
    @Override
    public String toString() {
        // TODO : Format toString by difficulty
        return "Dictionary{"
                + "words = " + words
                + '}';
    }
    
}
