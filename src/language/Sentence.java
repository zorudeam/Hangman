/*
 * A fancy license header.
 */
package language;

import utilities.collections.LinkedList;

/**
 * The {@code Sentence} class provides for a framework for a very rudimentarily
 * structure for sentences in a grammatical context.
 * 
 * @author Oliver Abdulrahim
 */
public final class Sentence {
    
    /**
     * Stores the {@code Word}s contained in this {@code Sentence}.
     */
    private LinkedList<Word> words;
    
    /**
     * Constructs a new {@code Sentence} with the the default amount of words
     * and random word characters.
     */
    public Sentence() {
        this(SentenceConstants.DEFAULT_SENTENCE_LENGTH);
    }
    
    /**
     * Constructs a new {@code Sentence} with the the specified amount of words
     * and random word characters.
     * 
     * @param length The amount of words in the {@code Sentence}.
     */
    public Sentence(int length) {
        words = new LinkedList<>();
        for (int i = 0; i < length; i++) {
            words.add(new Word());
        }
    }
    
    @SuppressWarnings("unchecked")
    public Sentence(Sentence other) {
        words = (LinkedList<Word>) other.words.clone();
    }
    
    /**
     * Returns a formatted copy of the word based on its properties at the given
     * index position {@code i} in this {@code Sentence}.
     * 
     * @param i The index for the word to format.
     * @return A formatted copy of the word at the given index.
     */
    public String formatWordAt(int i) {
        String word = words.get(i).characters();
        if (i == 0) {
            word = Character.toUpperCase(word.charAt(0)) 
                    + word.substring(1, word.length());
        }
        else if (Word.isProperNoun(words.get(i))) {
            word = Character.toUpperCase(word.charAt(0)) 
                    + word.substring(1, word.length());
        }
        else if (Word.isTrailingWord(words.get(i))) {
            word += '.';
        }
        return word;
    }
    
    /**
     * Returns a {@code String} representation of this {@code Sentence}.
     * 
     * @return A {@code String} that represents the {@code Word}s contained in 
     *         this {@code Sentence}.
     */
    @Override
    public String toString() {
        StringBuilder sentence = new StringBuilder();
        String word;
        for (int i = 0; i < words.size(); i++) {
            word = formatWordAt(i);
            if (i == words.size() - 1) {
                sentence.append(word);
            }
            else {
                sentence.append(formatWordAt(i)).append(' ');
            }
        }
        return sentence.append('.').toString();
    }

}