package language;

import java.util.EnumSet;

/**
 * {@code WordProperties} is an enumeration of properties which define how 
 * objects of {@link Word} are formatted and also provides for "difficulty"
 * properties for use in a word game.
 * 
 * @author Oliver Abdulrahim
 */
public enum Difficulty {
    
// Difficulty properties    
    
    /**
     * Defines a {@code Word} with easy difficulty.
     */
    EASY,
    
    /**
     * Defines a {@code Word} with medium difficulty.
     */
    MEDIUM,
    
    /**
     * Defines a {@code Word} with hard difficulty.
     */
    HARD;
    
    /**
     * Provides for a set of all properties enumerated in this class.
     */
    public static final EnumSet<Difficulty> ALL = 
            EnumSet.allOf(Difficulty.class);
    
}
