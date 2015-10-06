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
import java.util.function.Consumer;
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
     * Stores all words considered to be "easy" contained within this object.
     */
    private List<Word> easyWords;
    
    /**
     * Stores all words considered to be "medium" contained within this object.
     */
    private List<Word> mediumWords;
    
    /**
     * Stores all words considered to be "hard" contained within this object.
     */
    private List<Word> hardWords;
    
    /**
     * Stores the difficulty of the last word that was successfully retrieved
     * from this object, or {@link Difficulty#DEFAULT} if there has not been a 
     * word previously retrieved from this object or if there was an error in
     * attempting to do so.
     */
    private Difficulty difficultyCache;
    
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
        easyWords = new ArrayList<>();
        mediumWords = new ArrayList<>();
        hardWords = new ArrayList<>();
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
        try (Scanner input = new Scanner(target)) {
            while(input.hasNext()) {
                Word w = new Word(input.nextLine());
                // Interesting type inference in case statements - did not know 
                // that Java inferred types for enum switch statements.
                switch (judgeDifficulty(w)) {
                    case EASY:
                        easyWords.add(w);
                        break;
                    case MEDIUM:
                        mediumWords.add(w);
                        break;
                    case HARD:
                        hardWords.add(w);
                        break;
                    // No default case needed, judgeDifficulty(Word) always 
                    // returns a Difficulty object
                }
            }
        }
        words.put(Difficulty.EASY, easyWords);
        words.put(Difficulty.MEDIUM, mediumWords);
        words.put(Difficulty.HARD, hardWords);
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
            words.values().forEach((List<Word> wordList) -> {
                wordList.stream().anyMatch((word) -> word == w);
            });
        }
        return contains;
    }
    
    /**
     * Returns an iterator over the words contained within this dictionary. 
     * 
     * <p> The iterator returned by this method is ordered by word difficulty 
     * and is backed by the data stored by this dictionary, so changes to the 
     * dictionary are reflected on the iterator, and vice versa.
     * 
     * @return An iterator over the words contained within this object.
     */
    @Override
    public Iterator<Word> iterator() {
        return getAllWords().iterator();
    }

    /**
     * Inserts into this dictionary the given {@code Word}, taking into account
     * its difficulty, returning {@code true} if the given word was inserted 
     * into this object, {@code false} otherwise.
     * 
     * @param w The {@code Word} to add.
     * @return {@code true} if the given word was inserted into this object,
     *         {@code false} otherwise.
     */
    @Override
    public boolean add(Word w) {
        List<Word> peers = getListOf(judgeDifficulty(w));
        return peers.add(w);
    }
    
    /**
     * Removes a given word from this dictionary, returning {@code true} if the
     * word was removed, {@code false} otherwise (i.e. the given word does not 
     * exist in this dictionary).
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
     * Returns the amount of words currently contained within this object.
     * 
     * @return The amount of words currently contained within this object.
     */
    @Override
    public int size() {
        int size = words.values()
                        .stream()
                        .mapToInt(List :: size)
                        .sum(); // Nice terminal operation dude.
        return size;
    }
    
    /**
     * Returns a list containing all words of the given difficulty that are 
     * contained within this dictionary.
     * 
     * @param difficulty The difficulty of list to retrieve.
     * @return A list containing all words of the given difficulty.
     */
    public List<Word> getListOf(Difficulty difficulty) {
        if (!hasWordOf(difficulty)) {
            throw new NoSuchWordException("Could not retrieve word.", difficulty);
        }
        return Collections.unmodifiableList(words.get(difficulty));
    }
    
    /**
     * Returns a list containing all words of the previously used difficulty.
     * 
     * @return A cache of the previously used difficulty list.
     */
    public List<Word> cacheList() {
        return Collections.unmodifiableList(getListOf(difficultyCache));
    }
    
    /**
     * Returns a list containing all the words stored within this dictionary.
     * 
     * @return A list containing all the words of this dictionary.
     */
    public List<Word> getAllWords() {
        List<Word> allWords = new ArrayList<>();
        words.values()
             .stream()
             .forEach((wordList) -> allWords.addAll(wordList));
        return allWords;
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
     * Selects and returns a random {@code Word} from this object.
     * 
     * @return A random {@code Word} from this object.
     */
    public Word getAnyWord() {
        Difficulty d = Difficulty.ALL
                                 .stream()
                                 .findAny() // Terminal operation
                                 .get();
        difficultyCache = d;
        return getWordOf(d);
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
        return words.containsKey(difficulty) && !words.get(difficulty).isEmpty();
    }
    
    /**
     * Returns a randomly selected word of the given difficulty.
     * 
     * <p> This method populates the given list if it is empty. Then it 
     * retrieves a randomly selected element from it. in the case that there are
     * no elements of the specified difficulty contained within this object, 
     * this method throws a {@code NoSuchWordException}.
     * 
     * @param difficulty The difficulty of the word to return.
     * @return A randomly selected word of the given difficulty.
     * @throws NoSuchWordException If there are no words of the given 
     *         difficulty
     */
    protected Word getWordOf(Difficulty difficulty) {
        if (hasWordOf(difficulty)) {
            List<Word> wordsOf = getListOf(difficulty);
            difficultyCache = difficulty;
            return wordsOf.get(Utilities.r.nextInt(wordsOf.size()));
        }
        throw new NoSuchWordException("Could not retrieve word.", difficulty);
    }
    
// Utility methods   
    
    /**
     * Returns a formatted {@code String} containing this object's map of words.
     * 
     * @return A formatted {@code String} containing this object's map.
     */
    @Override
    public String toString() {
        return "Dictionary {"
                + "\n  EASY   = " + easyWords
                + "\n  MEDIUM = " + mediumWords
                + "\n  HARD   = " + hardWords
                + "\n  difficultyCache = " + difficultyCache
                + '\n' 
                + '}';
    }

}
