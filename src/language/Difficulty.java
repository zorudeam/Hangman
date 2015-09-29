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
    
// Difficulty properties    
    
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
    public static final EnumSet<WordProperties> ALL = 
            EnumSet.allOf(WordProperties.class);
    
}
