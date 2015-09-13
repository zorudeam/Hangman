package language;

import java.util.EnumSet;

/**
 * {@code WordProperties} is an enumeration of properties which define how 
 * objects of {@link Word} are formatted and also provides for "difficulty"
 * properties for use in a word game.
 * 
 * @author Oliver Abdulrahim
 */
public enum WordProperties {
    
// Grammatical properties
    
    /**
     * Defines a {@code Word} that is a proper noun.
     */
    PROPER_NOUN,
    
    /**
     * Defines a {@code Word} that is the first in its sentence. This has the 
     * same effect as the {@link #PROPER_NOUN} property.
     * 
     * @see #PROPER_NOUN
     */
    LEADING_WORD,
    
    /**
     * Defines a {@code Word} that is the last in its sentence.
     */
    TRAILING_WORD,
    
    /**
     * Defines a {@code Word} that is followed by a comma or another form of
     * punctuation other than "{@code .!?}".
     */
    DELINEATED_WORD,
    
// Difficulty properties    
    
    /**
     * Defines a {@code Word} with default difficulty.
     */
    DEFAULT_DIFFICULTY,
    
    /**
     * Defines a {@code Word} with easy difficulty.
     */
    EASY_WORD,
    
    /**
     * Defines a {@code Word} with medium difficulty.
     */
    MEDIUM_WORD,
    
    /**
     * Defines a {@code Word} with hard difficulty.
     */
    HARD_WORD;
    
    /**
     * Provides for a set of all properties enumerated in this class.
     */
    public static final EnumSet<WordProperties> PROPERTIES = 
            EnumSet.allOf(WordProperties.class);
    
}
