/*
 * A fancy license header.
 */
package language;

import utilities.collections.LinkedList;

/**
 * The {@code Paragraph} class provides for a framework for a very rudimentarily
 * structure for paragraphs in a grammatical context.
 * 
 * @author Oliver Abdulrahim
 */
public class Paragraph {
        
    /**
     * Stores the {@code Sentences}s contained in this {@code Paragraph}.
     */
    private LinkedList<Sentence> sentences;
    
    /**
     * Constructs a new {@code Paragraph} with the the default amount of 
     * sentences and random word characters and length.
     */
    public Paragraph() {
        this(SentenceConstants.DEFAULT_PARAGRAPH_LENGTH, 
             SentenceConstants.DEFAULT_SENTENCE_LENGTH);
    }
    
    /**
     * Constructs a new {@code Paragraph} with the the specified amount of 
     * sentences, sentences with the specified length, and random word 
     * characters and length.
     * 
     * @param paragraphLength The amount of sentences in the {@code Paragraph}.
     * @param sentenceLength The amount of words in the {@code Sentence}.
     */
    public Paragraph(int paragraphLength, int sentenceLength) {
        sentences = new LinkedList<>();
        for (int i = 0; i < paragraphLength; i++) {
            sentences.add(new Sentence(sentenceLength));
        }
    }
    
    @SuppressWarnings("unchecked")
    public Paragraph(Paragraph other) {
        sentences = (LinkedList<Sentence>) other.sentences.clone();
    }
    
   /**
     * Returns a {@code String} representation of this {@code Sentence}.
     * 
     * @return A {@code String} that represents the {@code Word}s contained in 
     *         this {@code Sentence}.
     */
    @Override
    public String toString() {
        StringBuilder paragraph = new StringBuilder();
        sentences.stream().forEach((sentence) -> {
            paragraph.append(sentence.toString()).append(' ');
        });
        return paragraph.toString();
    }
 
}