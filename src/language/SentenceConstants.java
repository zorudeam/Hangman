/*
 * A fancy license header.
 */
package language;

/**
 * This interface stores a collection of constants related to the {@code Word}
 * and {@code Sentence} classes.
 * 
 * @author Oliver Abdulrahim
 */
public interface SentenceConstants {
    
    /**
     * Supplies a default paragraph length argument in the case that no length 
     * is provided during construction.
     */
    int DEFAULT_PARAGRAPH_LENGTH = 6;
    
    /**
     * Supplies a default sentence length argument in the case that no length is
     * provided during construction.
     */
    int DEFAULT_SENTENCE_LENGTH = 10;
    
    /**
     * Supplies a default word length argument in the case that no length is 
     * provided during construction.
     */
    int DEFAULT_WORD_LENGTH = 5;
    
    /**
     * Property that defines a {@code Word} that is a regular word with no 
     * special formatting requirements.
     */
    int DEFAULT_WORD = 0;
    
    /**
     * Property that defines a {@code Word} that is a proper noun.
     */
    int PROPER_NOUN = 1;
    
    /**
     * Property that defines a {@code Word} that is the first in its sentence.
     * This has the same effect as the {@link #PROPER_NOUN} property.
     * 
     * @see #PROPER_NOUN
     */
    int LEADING_WORD = 1;
    
    /**
     * Property that defines a {@code Word} that is the last in its sentence.
     */
    int TRAILING_WORD = 2;
    
    /**
     * Property that defines a {@code Word} that is followed by a comma.
     */
    int COMMA_DELINEATED_WORD = 3;
    
    /**
     * Null object that provides predictable behavior.
     */
    Word NULL_WORD = new Word("hello", DEFAULT_WORD);
    
}