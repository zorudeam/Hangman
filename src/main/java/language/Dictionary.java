package language;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * The {@code Dictionary} class stores all parseable tokens from a given file
 * and maps the contents to their respective "difficulty" depending on their
 * character contents. This provides for a structure suitable for use in a word
 * game.
 *
 * <p> This class contains collection-like methods. Notable operations include:
 *   <ul>
 *     <li> {@link #add(Word) The add operation}, which inserts a word into the 
 *          object based on its difficulty.
 *     <li> {@link #remove(java.lang.Object) The remove operation}, which 
 *          removes a word from the object based on its
 *          difficulty.
 *     <li> {@link #size() The size method}, which returns the total amount of 
 *          words across all mapped difficulties contained within this object.
 *   </ul>
 *
 * <p> All parsed tokens contained an object of this class are converted to
 * objects of the {@link Word} class. See the
 * {@link Word#Word(java.lang.String)} constructor for more detailed information
 * on what this entails.
 *
 * @author Oliver Abdulrahim
 * @see language.Difficulty The difficulties that are mapped to {@code Word} 
 *      objects within this object.
 * @see Word The type of object stored by this class.
 * @see language.NoSuchWordException Thrown to indicate that an object of this
 *      class does not contain a word of a given difficulty.
 */
public class Dictionary {

    /**
     * Stores the resource location of the default dictionary.
     */
    public static final InputStream DEFAULT_STREAM = Dictionary.class
            .getResourceAsStream("/dictionary.txt");

    /**
     * Contains this object's word dictionary, grouping difficulty enumerations
     * as provided by {@link Difficulty} with all words that match them.
     */
    private final Map<Difficulty, List<Word>> words;

    /**
     * Stores the difficulty of the last word that was successfully retrieved
     * from this object, or {@link Difficulty#DEFAULT} if there has not been a
     * word previously retrieved from this object.
     */
    private Difficulty difficultyCache;

    /**
     * Instantiates a new, empty {@code Dictionary} using the default word
     * repository.
     *
     * @see #DEFAULT_STREAM The {@code InputStream} containing the word
     *      repository.
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
     * Creates a new {@code Dictionary} that is a shallow copy of the given one.
     * This constructor copies, but does not clone, each of the individual
     * words mapped to the given object.
     *
     * @param other The object to copy to this one.
     */
    public Dictionary(Dictionary other) {
        words = new EnumMap<>(other.words);
        difficultyCache = other.difficultyCache;
    }

    /**
     * Instantiates a new {@code Dictionary} and fills this object's map with
     * the contents of the specified {@code File}. Each token in the file is
     * parsed and placed into the map in a line-by-line basis.
     *
     * @param target The {@code File} to read into this object's map.
     */
    public Dictionary(InputStream target) {
        words = new EnumMap<>(Difficulty.class);
        difficultyCache = Difficulty.DEFAULT;
        if (target != null) {
            constructDictionary(target);
        }
    }

    /**
     * Each token in the given {@code InputStream} is parsed and placed into the
     * map based on the difficulty in a line-by-line basis using a
     * {@code Scanner} implementation.
     *
     * @param target The {@code File} to read into this object's map.
     */
    private void constructDictionary(InputStream target) {
        Difficulty.ALL.stream().forEach(d -> words.put(d, new ArrayList<>()));
        try (Scanner input = new Scanner(target)) {
            while(input.hasNext()) {
                // No need to sanitize input here, the Word(String) constructor
                // already does that.
                Word w = new Word(input.nextLine());
                add(w);
            }
        }
    }

    /**
     * Returns {@code true} if this object contains at least one word of the
     * given difficulty, {@code false} otherwise.
     *
     * @param d The difficulty to test.
     * @return {@code true} if this object contains at least one word of the
     *         given difficulty, {@code false} otherwise.
     */
    protected boolean hasWordOf(Difficulty d) {
        return words.containsKey(d) && !words.get(d).isEmpty();
    }

// Difficulty tuning variables

    /**
     * Minimum length for an easy word. This value must be greater
     * than the {@link #MEDIUM_LENGTH_THRESHOLD}.
     */
    private static final int EASY_LENGTH_THRESHOLD = 7;

    /**
     * Minimum amount of vowels for an easy word. This value must be greater
     * than the {@link #MEDIUM_VOWEL_THRESHOLD}.
     */
    private static final int EASY_VOWEL_THRESHOLD = 4;

    /**
     * Minimum length for a medium word. This value is always less than the
     * {@link #EASY_LENGTH_THRESHOLD}.
     */
    private static final int MEDIUM_LENGTH_THRESHOLD = EASY_LENGTH_THRESHOLD - 2;

    /**
     * Minimum amount of vowels for a medium word. This value is always less
     * than the {@link #EASY_VOWEL_THRESHOLD}.
     */
    private static final int MEDIUM_VOWEL_THRESHOLD = EASY_VOWEL_THRESHOLD - 1;

// Dictionary operations (static)

    /**
     * Determines if a given word is "easy" in difficulty. Words that are longer
     * in length and have more vowels are generally considered easy words.
     *
     * @param w The {@code Word} to test for difficulty.
     * @return {@code true} if the given word is considered to be "easy" in
     *         difficulty.
     */
    protected static boolean isEasyWord(Word w) {
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
    protected static boolean isMediumWord(Word w) {
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
    protected static boolean isHardWord(Word w) {
        return !isEasyWord(w) && !isMediumWord(w);
    }

    /**
     * Returns an enumerated property depending on the difficulty of the given
     * {@code Word}.
     *
     * <p> This implementation always returns a difficulty and ignores the
     * {@link Difficulty#DEFAULT} enumeration.
     *
     * @param w The word to judge the difficulty of.
     * @return An enumerated property depending on the difficulty of the given
     *         {@code Word}.
     */
    protected static Difficulty judgeDifficulty(Word w) {
        Difficulty d = Difficulty.HARD;
        if (isEasyWord(w)) {
            d = Difficulty.EASY;
        }
        else if (isMediumWord(w)) {
            d = Difficulty.MEDIUM;
        }
        return d;
    }

// Dictionary operations (non-static)

    /**
     * Returns an unmodifiable list containing all words of the given difficulty
     * that are contained within this dictionary.
     *
     * <p> The lists returned by this method are view-only; in other words,
     * modification of the backing structure is not supported. Any attempts to
     * perform such operations will result in an
     * {@code UnsupportedOperationException}.
     *
     * @param d The difficulty of list to retrieve.
     * @return A list containing all words of the given difficulty.
     * @throws NoSuchWordException If there are no words of the given difficulty
     *         contained within this object.
     */
    public List<Word> listOf(Difficulty d) {
        if (!hasWordOf(d)) {
            throw new NoSuchWordException("Could not retrieve word.", d);
        }
        return Collections.unmodifiableList(internalListOf(d));
    }

    /**
     * Returns an list containing all words of the given difficulty that are
     * contained within this dictionary, or an empty one if there are no words
     * of the given difficulty.
     *
     * <p> This method is identical to the
     * {@link #listOf(language.Difficulty)} except that it does not throw a
     * {@code NoSuchWordException} in the case that there is no word of the
     * given difficulty or if the list containing words of the given difficulty
     * is empty.
     *
     * @param d The difficulty of list to retrieve.
     * @return A list containing all words of the given difficulty.
     * @see #listOf(language.Difficulty)
     */
    protected final List<Word> internalListOf(Difficulty d) {
        return words.get(d);
    }

    /**
     * Returns an unmodifiable list containing all words of the previously used
     * difficulty.
     *
     * <p> The lists returned by this method are view-only; in other words,
     * modification of the backing structure is not supported. Any attempts to
     * perform such operations will result in an
     * {@code UnsupportedOperationException}.
     *
     * @return A cache of the previously used difficulty list.
     */
    public List<Word> cacheList() {
        List<Word> lastUsed = internalListOf(difficultyCache);
        return Collections.unmodifiableList(lastUsed);
    }

    /**
     * Returns an unmodifiable list containing all the words stored within this
     * dictionary.`
     *
     * <p> The lists returned by this method are view-only; in other words,
     * modification of the backing structure is not supported. Any attempts to
     * perform such operations will result in an
     * {@code UnsupportedOperationException}.
     *
     * @return A view-only list containing all the words of this dictionary.
     */
    public List<Word> allWords() {
        List<Word> allWords = words.values()
                .stream()
                .flatMap(List :: stream)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(allWords);
    }

    /**
     * Returns a randomly selected word of easy difficulty contained within this
     * object.
     *
     * @return A randomly selected word of easy difficulty.
     */
    public final Word randomEasyWord() {
        return randomWordOf(Difficulty.EASY);
    }

    /**
     * Returns a randomly selected word of medium difficulty contained within
     * this object.
     *
     * @return A randomly selected word of medium difficulty.
     */
    public final Word getMediumWord() {
        return randomWordOf(Difficulty.MEDIUM);
    }

    /**
     * Returns a randomly selected word of hard difficulty contained within this
     * object.
     *
     * @return A randomly selected word of hard difficulty.
     */
    public final Word randomHardWord() {
        return randomWordOf(Difficulty.HARD);
    }

    /**
     * Selects and returns a random {@code Word} from this object.
     *
     * @return A random {@code Word} from this object.
     */
    public final Word randomWord() {
        Difficulty d = words.keySet()
                .stream()
                .findAny()
                .get();
        return randomWordOf(d);
    }

    /**
     * Returns a randomly selected word of the given difficulty.
     *
     * <p> In the case that there are no elements of the specified difficulty
     * contained within this object, this method throws a
     * {@code NoSuchWordException}.
     *
     * @param d The difficulty of the word to return.
     * @return A randomly selected word of the given difficulty.
     * @throws NoSuchWordException If there are no words of the given difficulty
     *         contained within this object.
     */
    public Word randomWordOf(Difficulty d) {
        if (hasWordOf(d)) {
            difficultyCache = d;
            List<Word> wordsOf = internalListOf(d);
            int index = ThreadLocalRandom.current().nextInt(wordsOf.size());
            return wordsOf.get(index);
        }
        throw new NoSuchWordException("Could not retrieve word.", d);
    }

// "Collection" operations

    /**
     * Returns {@code true} if this dictionary contains no words, {@code false}
     * otherwise.
     *
     * @return {@code true} if this object contains no words, {@code false}
     *         otherwise.
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns {@code true} if this dictionary contains the given word,
     * {@code false} otherwise.
     *
     * @param o The object to search for in this object.
     * @return {@code true} if this object contains the given word,
     *         {@code false} otherwise.
     */
    public boolean contains(Object o) {
        boolean contains = false;
        if (o instanceof Word) {
            final Word w = (Word) o;
            contains = allWords()
                    .stream()
                    .anyMatch(word -> word.equals(w));
        }
        return contains;
    }

    /**
     * Inserts into this dictionary the given {@code Word}, taking into account
     * its difficulty and returning {@code true} if the given word was inserted
     * into this object, {@code false} otherwise.
     *
     * @param w The {@code Word} to add.
     * @return {@code true} if the given word was inserted into this object,
     *         {@code false} otherwise.
     */
    public boolean add(Word w) {
        // No need to test this value against anything, judgeDifficulty(Word)
        // always returns a Difficulty enumeration
        List<Word> peers = internalListOf(judgeDifficulty(w));
        return peers.add(w);
    }

    /**
     * Removes a given object from this dictionary, returning {@code true} if
     * a word representing the object was removed, {@code false} otherwise (i.e.
     * the given object is not of type {@code Word} or does not exist in this
     * dictionary).
     *
     * @param o The object to remove from this dictionary.
     * @return {@code true} if the word was removed, {@code false} otherwise.
     */
    public boolean remove(Object o) {
        if (o instanceof Word) {
            Word w = (Word) o;
            List<Word> peers = internalListOf(judgeDifficulty(w));
            return peers.remove(w);
        }
        return false;
    }

    /**
     * Returns the total amount of words currently contained within this object
     * across all mapped difficulties.
     *
     * @return The amount of words currently contained within this object.
     */
    public int size() {
        // Need to find the summation of the sizes of each list value mapped to
        // the difficulty keys in this object.
        return words.values()
                .stream()
                .mapToInt(List :: size)
                .sum();
    }

// Utility methods

    /**
     * Returns {@code String} containing all words mapped to this object,
     * formatted to their difficulty.
     *
     * @return A formatted {@code String} containing the words belonging to
     *          this object.
     */
    @Override
    public String toString() {
        return "Dictionary {\n    "
            + words.keySet()
                .stream()
                .map(d -> d + " = " + words.get(d))
                .collect(Collectors.joining("\n    "))
            + "\n}";
    }
    
}
