/*
 * A fancy license header.
 */
package language;

//<editor-fold defaultstate="collapsed" desc="Imports">

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import utilities.collections.LinkedList;

//</editor-fold>

/**
 * The {@code Dictionary} class stores all parseable tokens from a given file 
 * and stores them in a dictionary structure. Various operations can be 
 * performed on this list, such as finding the shortest token or the amount of 
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
     * Contains this object's word dictionary.
     */
    private final LinkedList<Word> list;
    
    /**
     * Instantiates a new, empty {@code Dictionary} with no words in its list.
     */
    public Dictionary() {
        this(null);
    }
    
    /**
     * Instantiates a new {@code Dictionary} and fills this object's 
     * list with the contents of the specified {@code File}. Each token in the 
     * file is parsed and placed into the list in a line-by-line basis.
     *
     * @param target The {@code File} to read into this object's list.
     */
    public Dictionary(File target) {
        list = new LinkedList<>();
        if (target != null) {
            constructDictionary(target);
            sort();
        }
    }
    
    /**
     * Each token in the given file is parsed and placed into the list in a 
     * line-by-line basis.
     *
     * @param target The {@code File} to read into this object's list.
     */
    private void constructDictionary(File target) {
        try {
            Scanner input = new Scanner(target.getAbsoluteFile());
            while(input.hasNext()) {
                list.add(Word.constructWord(input.nextLine()));
            }
        } 
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Sorts this object's list in ascending order.
     */
    public void sort() {
        Comparator<Word> c = (w1, w2) -> {
            return w1.compareTo(w2);
        };
        Collections.sort(list, c);
    }
    
    /**
     * Adds a given {@code Word} into this object in forward alphabetical order.
     * If the given {@code Word} already exists in the list, this method does 
     * nothing.
     * 
     * @param element The {@code String} to add.
     */
    public void add(Word element) {
        if (!list.contains(element)) {
            list.add(element);
            sort();
        }
    }
    
    /**
     * Removes an the item at the specified index in this object's list.
     * 
     * @param index The index value of the object to remove.
     */
    public void remove(int index) {
        list.remove(index);
    }
    
    /**
     * Finds and returns the {@code Word} in this object's list with the 
     * shortest length.
     * 
     * @return The shortest {@code Word} in this object's list.
     */
    public Word getShortest() {
        Word shortest = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (shortest.compareTo(list.get(i)) < 0) {
                shortest = list.get(i);
            }
        }
        return shortest;
    }
    
    /**
     * Finds and returns the {@code Word} in this object's list with the 
     * longest length.
     * 
     * @return The longest {@code Word} in this object's list.
     */
    public Word getLongest() {
        Word longest = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (longest.compareTo(list.get(i)) > 0) {
                longest = list.get(i);
            }
        }
        return longest;
    }
    
    /**
     * Returns a formatted {@code String} containing this object's list.
     * 
     * @return A formatted {@code String} containing this object's list.
     */
    @Override
    public String toString() {
        return "Dictionary{"
                + "list = " + list
                + '}';
    }

}