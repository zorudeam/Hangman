package language;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.functions.Utilities;

/**
 * The {@code Dictionary} class stores all parseable tokens from a given file 
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
public final class Dictionary {
    
    /**
     * Contains this object's word dictionary. This map is sorted by word 
     * difficulty
     */
    private final Map<Word, WordProperties> words;
    
    /**
     * Instantiates a new, empty {@code Dictionary} with no words in its map.
     */
    public Dictionary() {
        this(null);
    }
    
    /**
     * Instantiates a new {@code Dictionary} and fills this object's map with 
     * the contents of the specified {@code File}. Each token in the file is 
     * parsed and placed into the map in a line-by-line basis.
     *
     * @param target The {@code File} to read into this object's map.
     */
    public Dictionary(File target) {
        words = new TreeMap<>();
        easyWords = new ArrayList<>();
        mediumWords = new ArrayList<>();
        hardWords = new ArrayList<>();
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
    private void constructDictionary(File target) {
        try {
            Scanner input = new Scanner(target.getAbsoluteFile());
            while(input.hasNext()) {
                Word w = new Word(input.nextLine());
                words.put(w, judgeDifficulty(w));
            }
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, 
                    "Could not find file: " + target, ex);
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
    public static WordProperties judgeDifficulty(Word w) {
        if (isEasyWord(w)) {
            return WordProperties.EASY_WORD;
        }
        else if (isMediumWord(w)) {
            return WordProperties.MEDIUM_WORD;
        }
        else {
            return WordProperties.HARD_WORD;
        }
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
     * Caches the easy words contained within this object.
     */
    private List<Word> easyWords;
    
    /**
     * Caches the medium words contained within this object.
     */
    private List<Word> mediumWords;
    
    /**
     * Caches the hard words contained within this object.
     */
    private List<Word> hardWords;
    
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
    
// Collection operations
    
    /**
     * Adds a given {@code Word} into this object. If the given {@code Word} 
     * already exists in the map, this method does nothing.
     * 
     * @param w The {@code Word} to add.
     * @throws NullPointerException If the given word is {@code null}.
     */
    public void add(Word w) {
        Objects.requireNonNull(w);
        words.put(w, judgeDifficulty(w));
    }
    
    /**
     * Removes the specified word from this object's map.
     * 
     * @param w The object to remove.
     */
    public void remove(Word w) {
        words.remove(w);
    }
    
    /**
     * Returns a random {@code Word} from this object's {@code map}.
     * 
     * @return A random {@code String} from this object's {@code map}.
     */
    public Word getAnyWord() {
        List<Word> list = new ArrayList<>(words.keySet());
        return list.get(Utilities.r.nextInt(words.size()));
    }
    
    /**
     * Returns a randomly selected word of the given difficulty from the given 
     * list, or from this object's set of words if the specified list is empty.
     * 
     * @param list The list to populate with elements, if it is empty.
     * @param difficulty The difficulty of the word to return.
     * @return A randomly selected word of the given difficulty.
     */
    protected Word getWordOf(List<Word> list, WordProperties difficulty) {
        if (list.isEmpty()) {
            words.forEach((Word key, WordProperties value) -> {
            if (value == difficulty) {
                list.add(key);
            }
            });
        }
        return list.get(Utilities.r.nextInt(list.size()));
    }
    
    /**
     * Returns a randomly selected word of easy difficulty mapped to this 
     * object.
     * 
     * @return A randomly selected word of easy difficulty.
     */
    public Word getEasyWord() {
        return getWordOf(easyWords, WordProperties.EASY_WORD);
    }
    
    /**
     * Returns a randomly selected word of medium difficulty mapped to this 
     * object.
     * 
     * @return A randomly selected word of medium difficulty.
     */
    public Word getMediumWord() {
        return getWordOf(mediumWords, WordProperties.MEDIUM_WORD);
    }
    
    /**
     * Returns a randomly selected word of hard difficulty mapped to this 
     * object.
     * 
     * @return A randomly selected word of hard difficulty.
     */
    public Word getHardWord() {
        return getWordOf(hardWords, WordProperties.HARD_WORD);
    }
    
    /**
     * Returns a set containing all entries mapped to this object.
     * 
     * @return A set of all entries in this object.
     */
    public Set<Map.Entry<Word, WordProperties>> entrySet() {
        return words.entrySet();
    }
    
    /**
     * Returns a set containing all keys mapped to this object.
     * 
     * @return A set of all keys in this object.
     */
    public Set<Word> keySet() {
        return words.keySet();
    }
    
    /**
     * Returns a formatted {@code String} containing this object's map.
     * 
     * @return A formatted {@code String} containing this object's map.
     */
    @Override
    public String toString() {
        return "Dictionary{"
                + "Words in this dictionary = " + words
                + '}';
    }

}