package language;

import java.util.EnumSet;
import java.util.Set;

/**
 * {@code Difficulty} is an enumeration of properties which define how objects 
 * of {@link Word} are formatted and also provides for "difficulty" properties 
 * for use in a word game.
 * 
 * <p> The properties defined in this class are designed to be 
 * implementation-dependant and have no values stored to them other than their
 * names.
 * 
 * @author Oliver Abdulrahim
 */
public enum Difficulty {
    
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
    HARD,
    
    /**
     * Defines a {@code Word} with default difficultly.
     */
    DEFAULT;
    
    /**
     * Provides for a set of all properties enumerated in this class.
     */
    public static final Set<Difficulty> ALL = // Pryda 10 VOL III is out
            EnumSet.allOf(Difficulty.class);
    
}
