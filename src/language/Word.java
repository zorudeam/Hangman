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
        this(randomAlphaString(length));
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
    public static String sanitizeWord(String str) {
        return str.replaceAll("\\s+", "").toLowerCase();
    }
    
    /**
     * "Sanitizes" and returns a given {@code char} by converting it to 
     * lowercase.
     * 
     * @param c The {@code char} to sanitize.
     * @return A sanitized version of the given {@code char}.
     */
    public static char sanitizeCharacter(char c) {
        return Character.toLowerCase(c);
    }
    
    /**
     * Generates a {@code String} containing random characters with the 
     * specified length.
     * 
     * @param length The amount of characters in the word to generate.
     * @return A {@code String} with random characters of the specified length.
     */
    private static String randomAlphaString(int length) {
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
     * Returns the length of this word.
     * 
     * @return The length of this word.
     */
    public int length() {
        return characters.length();
    }
    
    /**
     * Calculates the amount of consonants contained in this {@code Word}. 
     * Returns a number from {@code 0} (no consonants) to the length of this 
     * word (no vowels).
     * 
     * @return The amount of consonants in this {@code Word}.
     */
    public int consonantCount() {
        return characters.length() - vowelCount();
    }
    
    /**
     * Calculates the amount of vowels contained in this {@code Word}. Returns a
     * number from {@code 0} (no vowels) to the length of this word (no 
     * consonants) depending on the occurrences of the characters {@code 'a'}, 
     * {@code 'e'}, {@code i'}, {@code 'o'}, and {@code 'u'}.
     * 
     * @return The amount of vowels in this {@code Word}.
     */
    public int vowelCount() {
        int amount = 0;
        for (int i = 0; i < characters.length(); i++) {
            if (isVowel(characters.charAt(i))) {
                amount++;
            }
        }
        return amount;
    }

    /**
     * Checks if a given character is a vowel. Vowels include the letters 
     * {@code 'a'}, {@code 'e'}, {@code i'}, {@code 'o'}, and {@code 'u'}.
     * 
     * @param key The {@code char} to test.
     * @return {@code true} if the argument provided is a vowel, {@code false}
     *         otherwise.
     */
    private boolean isVowel(char key) {
        char[] vowels = {
            'a', 'e', 'i', 'o', 'u'
        };
        for (int i = 0; i < vowels.length; i++) {
            if (key == vowels[i]) {
                return true;
            }
        }
        return false;
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
     * <p> This implementation simply compares the characters of this word with
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
