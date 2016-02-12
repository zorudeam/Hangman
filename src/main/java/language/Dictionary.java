package language;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The {@code Dictionary} class contains an associative grouping of
 * {@link Word Word} objects by their {@link Difficulty}. This provides for a
 * structure suitable for use in a word game.
 *
 * <p> This class contains collection-like methods. Notable operations include:
 *   <ul>
 *     <li> {@link #add(Word) The add operation}, which inserts a word into the
 *          object based on its difficulty.
 *     <li> {@link #remove(Word) The remove operation}, which
 *          removes a word from the object based on its
 *          difficulty.
 *     <li> {@link #size() The size method}, which returns the total amount of
 *          words across all mapped difficulties contained within this object.
 *   </ul>
 *
 * <p> All parsed tokens contained an object of this class are converted to
 * objects of the {@link Word} class. See the
 * {@link Word#Word(java.lang.String) Word(String} constructor for a more
 * detailed specification.
 *
 * @author Oliver Abdulrahim
 * @see Difficulty The difficulties that are mapped to {@code Word} objects
 *      within this object.
 * @see Word The type of object stored by this class.
 * @see NoSuchWordException Thrown to indicate that an object of this class does
 *      not contain a word of a given difficulty.
 */
public final class Dictionary {

    /**
     * Stores the resource location of the default dictionary.
     */
    private static final InputStream DEFAULT_STREAM = Dictionary.class
            .getResourceAsStream("/dictionary.txt");

    /**
     * Contains this object's word dictionary, grouping words of similar
     * difficulty (as specified in {@link Difficulty}) in {@code List}s.
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
     * Constructs a {@code Dictionary} with the specified {@code String}
     * path.
     *
     * @param path The {@code String} path for the {@code File} to read into
     *        this object's map.
     */
    public Dictionary(String path) {
        this(Dictionary.class.getResourceAsStream(path));
    }

    /**
     * Constructs a {@code Dictionary} that is a shallow copy of the given one.
     * This constructor copies, but does not clone, each of the individual words
     * mapped to the given object.
     *
     * @param other The object to copy to this one.
     */
    public Dictionary(Dictionary other) {
        words = new EnumMap<>(other.words);
        difficultyCache = other.difficultyCache;
    }

    /**
     * Constructs a {@code Dictionary} using the contents of the given
     * {@code InputStream}. Each token in the stream is parsed and placed into
     * the map in a line-by-line basis.
     *
     * @param is The {@code InputStream} containing the words to read into this
     *        object.
     */
    private Dictionary(InputStream is) {
        words = new EnumMap<>(Difficulty.class);
        difficultyCache = Difficulty.DEFAULT;
        Optional.ofNullable(is)
                .ifPresent(this :: constructDictionary);
    }

    /**
     * Pareses each token in the given {@code InputStream} and places them into
     * this object's {@link #words} based on their difficulty in a line-by-line
     * basis using a {@code Scanner} implementation.
     *
     * @param target The {@code File} to read into this object's map.
     */
    private void constructDictionary(InputStream target) {
        Map<Difficulty, List<Word>> empty = Difficulty.ALL
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        d -> new ArrayList<>())
                );
        words.putAll(empty);
        // Need a Path object to use Files.lines() :(
        try (Scanner input = new Scanner(target)) {
            while(input.hasNext()) {
                // No need to sanitize the String here, the Word(String)
                // constructor already does that.
                Word w = new Word(input.nextLine());
                add(w);
            }
        }
    }

    /**
     * Tests if this object contains words of the specified difficulty,
     * returning {@code true} if this object contains at least one word matching
     * the given object, {@code false} otherwise.
     *
     * @param d The difficulty to test.
     * @return {@code true} if this object contains at least one word of the
     *         specified difficulty, {@code false} otherwise.
     */
    private boolean hasWordOf(Difficulty d) {
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
     * Determines if the given word is "easy" in difficulty, returning
     * {@code true} if the specified object is such a word, {@code false}
     * otherwise.
     *
     * <p> Words that are longer in length and have more vowels are generally
     * considered easy words.
     *
     * @param w The {@code Word} to test for difficulty.
     * @return {@code true} if the given word is considered to be "easy" in
     *         difficulty, {@code false} otherwise.
     */
    private static boolean isEasyWord(Word w) {
        return w.vowelCount() >= EASY_VOWEL_THRESHOLD
                && w.length() >= EASY_LENGTH_THRESHOLD;
    }

    /**
     * Determines if the given word is "medium" in difficulty, returning
     * {@code true} if the specified object is such a word, {@code false}
     * otherwise.
     *
     * <p> Words that are medium in length and contain fewer vowels are
     * generally considered medium words.
     *
     * @param w The {@code Word} to test for difficulty.
     * @return {@code true} if the given word is considered to be "medium" in
     *         difficulty, {@code false} otherwise.
     */
    private static boolean isMediumWord(Word w) {
        return w.vowelCount() >= MEDIUM_VOWEL_THRESHOLD
                && w.length() >= MEDIUM_LENGTH_THRESHOLD;
    }

    /**
     * Determines if the given word is "hard" in difficulty, returning
     * {@code true} if the specified object is such a word, {@code false}
     * otherwise.
     *
     * @param w The {@code String} to test for difficulty.
     * @return {@code true} if the given word is considered to be "hard" in
     *         difficulty, {@code false} otherwise.
     */
    private static boolean isHardWord(Word w) {
        return !isEasyWord(w) && !isMediumWord(w);
    }

    /**
     * Returns an enumerated property depending on the difficulty of the given
     * {@code Word}.
     *
     * <p> This implementation always returns a difficulty and ignores the
     * {@link Difficulty#DEFAULT default enumeration}.
     *
     * @param w The word to judge the difficulty of.
     * @return An enumerated property depending on the difficulty of the given
     *         {@code Word}.
     */
    private static Difficulty judgeDifficulty(Word w) {
        Difficulty d;
        if (isEasyWord(w)) {
            d = Difficulty.EASY;
        }
        else if (isMediumWord(w)) {
            d = Difficulty.MEDIUM;
        }
        else {
            d = Difficulty.HARD;
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
    private List<Word> internalListOf(Difficulty d) {
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
     * dictionary.
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
    public Word randomEasyWord() {
        return randomWordOf(Difficulty.EASY);
    }

    /**
     * Returns a randomly selected word of medium difficulty contained within
     * this object.
     *
     * @return A randomly selected word of medium difficulty.
     */
    public Word getMediumWord() {
        return randomWordOf(Difficulty.MEDIUM);
    }

    /**
     * Returns a randomly selected word of hard difficulty contained within this
     * object.
     *
     * @return A randomly selected word of hard difficulty.
     */
    public Word randomHardWord() {
        return randomWordOf(Difficulty.HARD);
    }

    /**
     * Selects and returns a random {@code Word} from this object.
     *
     * @return A random {@code Word} from this object.
     */
    public Word randomWord() {
        Difficulty d = words.keySet()
                .stream()
                .findFirst()
                .orElse(Difficulty.DEFAULT);
        return randomWordOf(d);
    }

    /**
     * Returns a randomly-selected word of the given difficulty.
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
     * @param w The word to search for in this object.
     * @return {@code true} if this object contains the given word,
     *         {@code false} otherwise.
     */
    public boolean contains(Word w) {
        return allWords()
                .stream()
                .anyMatch(word -> word.equals(w));
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
     * @param w The word to remove from this dictionary.
     * @return {@code true} if the word was removed, {@code false} otherwise.
     */
    public boolean remove(Word w) {
        List<Word> peers = internalListOf(judgeDifficulty(w));
        return peers.remove(w);
    }

    /**
     * Returns the total amount of words currently contained within this object
     * across all mapped difficulties.
     *
     * @return The amount of words currently contained within this object.
     * @implNote Can also perform a reduction directly using
     *           {@link Stream#reduce(Object, BinaryOperator) a reduction}, but
     *           this implementation uses a more clear mapping operation
     *           followed by a summation.
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
     * @return A formatted {@code String} containing the words belonging to this
     *         object.
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
