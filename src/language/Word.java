package language;

import utilities.functions.StringUtilities;

/**
 * The {@code Word} class provides for a structure for words in the context of a
 * playable word game.
 * 
 * @author Oliver Abdulrahim
 */
public final class Word 
    implements Comparable<Word>
{

    /**
     * Supplies a default word length argument in the case that no length is 
     * provided during construction.
     */
    public static final int DEFAULT_WORD_LENGTH = 5;
    
    /**
     * Null object that provides predictable behavior.
     */
    public static final Word NULL_WORD = new Word("Hello World!");
    
    /**
     * Stores the characters represented by this {@code Word}.
     */
    private final String characters;
    
    /**
     * Constructs a {@code Word} with random characters and the default length,
     * as specified by {@link #DEFAULT_WORD_LENGTH}.
     * 
     * @see #DEFAULT_WORD_LENGTH The length argument used by this constructor.
     */
    public Word() {
        this(DEFAULT_WORD_LENGTH);
    }
    
    /**
     * Constructs a {@code Word} with random characters and the specified 
     * length.
     * 
     * @param length The length of the {@code Word}.
     */
    public Word(int length) {
        this(getRandomWord(length));
    }
    
    /**
     * Constructs a {@code Word} with the specified characters.
     * 
     * @param characters The characters to use to create this {@code Word}.
     */
    public Word(String characters) {
        this.characters = sanitizeWord(characters);
    }
    
    /**
     * Constructs an exact copy of the given {@code Word}.
     * 
     * @param other The word whose attributes to copy.
     */
    public Word(Word other) {
        this(other.characters);
    }
    
    /**
     * "Sanitizes" and returns a given {@code String}, removing any and all 
     * spaces and converting all remaining characters to lowercase.
     * 
     * @param str The {@code String} to sanitize.
     * @return A sanitized version of the given {@code String}.
     */
    private static String sanitizeWord(String str) {
        return str.replaceAll("\\s+", "").toLowerCase();
    }
    
    /**
     * Generates a {@code String} containing random characters with the 
     * specified length.
     * 
     * @param length The amount of characters in the word to generate.
     * @return A {@code String} with random characters of the specified length.
     */
    private static String getRandomWord(int length) {
        return StringUtilities.random('a', 'z', length);
    }
    
    /**
     * Returns the characters contained by this {@code Word}. These characters
     * will always be in lowercase.
     * 
     * @return The characters contained in this {@code Word}.
     */
    public String characters() {
        return characters;
    }
    
    /**
     * Returns a {@code String} representation of this {@code Word}.
     * 
     * @return A {@code String} that represents the characters contained in this
     *         {@code Word}.
     */
    @Override
    public String toString() {
        return characters();
    }
    
    /**
     * Compares a given {@code Word} with this one for order.
     * 
     * <p> This implementation simply compares that characters of this word with
     * the given one, returning a negative integer if this object is less than 
     * {@code other}, zero if they are equal (in other words, 
     * {@code this.equals(other)} evaluates to {@code true}), or a positive 
     * integer if this object is greater than the other.
     * 
     * @param other The {@code Word} to compare to this one.
     * @return A negative integer, zero, or a positive integer if this object's
     *         characters are less than, equal to, or greater than the given 
     *         {@code Word}, respectively.
     * @see String#compareTo(java.lang.String)
     */
    @Override
    public int compareTo(Word other) {
        return this.characters().compareTo(other.characters());
    }
    
}