package language;

import java.io.InputStream;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * The {@code Dictionary} class stores all parseable tokens from a given file 
 * and maps the contents to their respective "difficulty" depending on their
 * character contents. This provides for a structure suitable for use in a word 
 * game.
 * 
 * <p> This {@code Collection} implements all imposed methods. Notable 
 * operations include:
 *   <ul> 
 *     <li> {@link #add(language.Word) The add operation}, which in this 
 *          implementation, inserts a word into this object based on its 
 *          difficulty.
 *     <li> {@link #remove(java.lang.Object) The remove operation}, which in 
 *          this implementation, removes a word from this object based on its
 *          difficulty.
 *     <li> {@link #size() The size method}, which in this implementation, 
 *          returns the total amount of words across all mapped difficulties
 *          contained within this object.
 *   </ul>
 * <p> Any tokens with alphabetical characters contained an object of this class
 * are converted to objects of the {@link language.Word} class. See the 
 * {@link Word#Word(java.lang.String)} constructor for more detailed information
 * on what this entails.
 * 
 * @author Oliver Abdulrahim
 * @see language.Word The type of object stored in this <code>Collection</code>.
 * @see language.NoSuchWordException Thrown to indicate that an object of this
 *      class does not contain a word of a given difficulty.
 */
public class Dictionary
    extends AbstractCollection<Word>
{

    /**
     * Stores the resource location of the default dictionary.
     */
    public static final InputStream DEFAULT_STREAM = Dictionary.class
            .getResourceAsStream("/resources/dictionary.txt");
    
    /**
     * Contains this object's word dictionary. This map is sorted by word 
     * difficulty
     */
    private final Map<Difficulty, List<Word>> words;
    
    /**
     * Stores the difficulty of the last word that was successfully retrieved
     * from this object, or {@link Difficulty#DEFAULT} if there has not been a 
     * word previously retrieved from this object or if there was an error in
     * attempting to do so.
     */
    private Difficulty difficultyCache;
    
    /**
     * Stores {@code false} before the construction of this object is complete
     * and {@code true} after the operation is complete. This suppresses any
     * exceptional behavior that would ordinarily occur after construction.
     * 
     * @see #hasWordOf(language.Difficulty) This property prevents the 
     *      propagation of any {@code NoSuchWordException} at instantiation.
     */
    private boolean constructed = false;
    
    /**
     * Instantiates a new, empty {@code Dictionary} using the default word
     * repository.
     * 
     * @see #DEFAULT_STREAM The <code>InputStream</code> containing the word 
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
     * Instantiates a new {@code Dictionary} and fills this object's map with 
     * the contents of the specified {@code File}. Each token in the file is 
     * parsed and placed into the map in a line-by-line basis.
     *
     * @param target The {@code File} to read into this object's map.
     */
    public Dictionary(InputStream target) {
        words = new TreeMap<>();
        difficultyCache = Difficulty.DEFAULT;
        if (target != null) {
            constructDictionary(target);
        }
    }
    
    /**
     * Each token in the given {@code InputStream} is parsed and placed into the
     * map in a line-by-line basis using a {@code Scanner} implementation.
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
        constructed = true;
    }
    
    /**
     * Returns {@code true} if this object contains at least one word of the
     * given difficulty, {@code false} otherwise.
     * 
     * @param d The difficulty to test.
     * @return {@code true} if this object contains at least one word of the
     *         given difficulty, {@code false} otherwise.
     */
    private boolean hasWordOf(Difficulty d) {
        return (words.containsKey(d) && !words.get(d).isEmpty()) || !constructed;
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
     * Minimum amount of vowels for a medium word. This value is always less 
     * than the {@link #EASY_VOWEL_THRESHOLD}.
     */
    private static final int MEDIUM_VOWEL_THRESHOLD = EASY_VOWEL_THRESHOLD - 1;
    
    /**
     * Minimum length for a medium word. This value is always less than the
     * {@link #EASY_LENGTH_THRESHOLD}.
     */
    private static final int MEDIUM_LENGTH_THRESHOLD = EASY_VOWEL_THRESHOLD - 2;
    
// Dictionary operations (static)
    
    /**
     * Determines if a given word is "easy" in difficulty. Words that are longer
     * in length and have more vowels are generally considered easy words.
     * 
     * @param w The {@code Word} to test for difficulty.
     * @return {@code true} if the given word is considered to be "easy" in
     *         difficulty.
     */
    public static final boolean isEasyWord(Word w) {
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
    public static final boolean isMediumWord(Word w) {
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
    public static final boolean isHardWord(Word w) {
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
    public static Difficulty judgeDifficulty(Word w) {
        if (isEasyWord(w)) {
            return Difficulty.EASY;
        }
        else if (isMediumWord(w)) {
            return Difficulty.MEDIUM;
        }
        else {
            return Difficulty.HARD;
        }
    }
    
// Dictionary operations (non-static)
    
    /**
     * Returns a list containing all words of the given difficulty that are 
     * contained within this dictionary.
     * 
     * @param d The difficulty of list to retrieve.
     * @return A list containing all words of the given difficulty.
     * @throws NoSuchWordException If there are no words of the given difficulty
     *         contained within this object.
     */
    protected List<Word> getListOf(Difficulty d) {
        if (!hasWordOf(d)) {
            difficultyCache = Difficulty.DEFAULT;
            throw new NoSuchWordException("Could not retrieve word.", d);
        }
        return words.get(d);
    }
    
    /**
     * Returns an unmodifiable list containing all words of the previously used
     * difficulty.
     * 
     * @return A cache of the previously used difficulty list.
     */
    public List<Word> cacheList() {
        return Collections.unmodifiableList(getListOf(difficultyCache));
    }
    
    /**
     * Returns an unmodifiable list containing all the words stored within this 
     * dictionary.
     * 
     * @return A list containing all the words of this dictionary.
     */
    public List<Word> getAllWords() {
        List<Word> allWords = new ArrayList<>();
        words.values()
             .stream()
             .forEach(wordList -> allWords.addAll(wordList));
        return Collections.unmodifiableList(allWords);
    }
    
    /**
     * Returns a randomly selected word of easy difficulty contained within this
     * object.
     * 
     * @return A randomly selected word of easy difficulty.
     */
    public Word getEasyWord() {
        return getWordOf(Difficulty.EASY);
    }
    
    /**
     * Returns a randomly selected word of medium difficulty contained within 
     * this object.
     * 
     * @return A randomly selected word of medium difficulty.
     */
    public Word getMediumWord() {
        return getWordOf(Difficulty.MEDIUM);
    }
    
    /**
     * Returns a randomly selected word of hard difficulty contained within this
     * object.
     * 
     * @return A randomly selected word of hard difficulty.
     */
    public Word getHardWord() {
        return getWordOf(Difficulty.HARD);
    }
    
    /**
     * Selects and returns a random {@code Word} from this object.
     * 
     * @return A random {@code Word} from this object.
     */
    public Word getAnyWord() {
        Difficulty d = Difficulty.ALL.stream().findAny().get();
        difficultyCache = d;
        return getWordOf(d);
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
    public Word getWordOf(Difficulty d) {
        if (hasWordOf(d)) {
            difficultyCache = d;
            List<Word> wordsOf = getListOf(d);
            int index = ThreadLocalRandom.current().nextInt(wordsOf.size());
            return wordsOf.get(index);
        }
        throw new NoSuchWordException("Could not retrieve word.", d);
    }
    
// Collection operations
    
    /**
     * Returns {@code true} if this dictionary contains no words, {@code false}
     * otherwise.
     * 
     * @return {@code true} if this object contains no words, {@code false}
     *         otherwise.
     */
    @Override
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
    @Override
    public boolean contains(Object o) {
        boolean contains = false;
        if (o instanceof Word) {
            final Word w = (Word) o;
            contains = getAllWords().stream().anyMatch(word -> word == w);
        }
        return contains;
    }
    
    /**
     * Returns an iterator over the words contained within this dictionary. 
     * 
     * <p> The iterators returned by this method are view-only; in other words, 
     * modification of the backing structure is not supported. Any attempts to
     * perform such operations will result in an
     * {@code UnsupportedOperationException}.
     * 
     * <p> Supported {@code Iterator} operations include:
     *   <ul>
     *     <li> {@link Iterator#forEachRemaining(java.util.function.Consumer)}
     *     <li> {@link Iterator#next() (java.util.function.Consumer)}
     *     <li> {@link Iterator#hasNext()}
     *   </ul>
     * 
     * @return An iterator over all the words contained within this object.
     */
    @Override
    public Iterator<Word> iterator() {
        return getAllWords().iterator();
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
    @Override
    public boolean add(Word w) {
        // No need to test this value against anything, judgeDifficulty(Word)
        // always returns a Difficulty enumeration
        List<Word> peers = getListOf(judgeDifficulty(w));
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
    @Override
    public boolean remove(Object o) {
        if (o instanceof Word) {
            final Word w = (Word) o;
            List<Word> peers = getListOf(judgeDifficulty(w));
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
    @Override
    public int size() {
        // This finds the sum of the sizes of all the lists mapped to words
        int size = words.values()
                .stream()
                .mapToInt(List :: size)
                .sum();
        return size;
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
        return "Dictionary {\n\t" 
            + Difficulty.ALL
                .stream()
                .filter(d -> words.containsKey(d))
                .map(d -> d.toString() + " = " + words.get(d))
                .collect(Collectors.joining("\n\t"))
            + "\n}";
    }

}
