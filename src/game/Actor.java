package game;

import java.util.Arrays;
import javax.swing.ImageIcon;

/**
 * The {@code Actor} enumeration is a structure for constant values relating to 
 * images for a game of hangman. Each enumeration has a predefined set of images 
 * for use in a graphical user interface.
 * 
 * @author Oliver Abdulrahim
 */
public enum Actor {
    
    /**
     * Represents a classic Stick Figure. This enumeration should have {@code 8}
     * images in total.
     */
    HUMAN("Stick Figure", getImages("Human", 8)),
    
    /**
     * Represents a Snowman. This enumeration should have {@code 8} images in 
     * total.
     */
    SNOWMAN("Snowman", getImages("Snowman", 8));
    
    /**
     * Used for identifying the object and for display purposes in graphical 
     * interfaces.
     */
    private final String name;
    
    /**
     * Contains this object's images for use in a graphical interface. This 
     * image array should be iterated in its entirety before the "loss state" of
     * any individual hangman game.
     */
    private final ImageIcon[] images;
    
    /**
     * Returns the name of this actor.
     *
     * @return The name of this actor.
     */
    public String getName() {
        return name;
    }

    /**
     * Creates an actor with the given arguments.
     * 
     * @param name The name of this actor.
     * @param images The images associated with this actor.     
     */
    private Actor(String name, ImageIcon[] images) {
        this.name = name;
        this.images = images;
    }
    
    /**
     * Returns a copy of the array of this actor's images.
     *
     * @return a copy of the array of this actor's images.
     */
    public ImageIcon[] getImageArray() {
        return Arrays.copyOf(images, images.length);
    }
    
    /**
     * Generates and returns an array of images using the given enumeration's 
     * {@code ImageIcon} icon sets.
     * 
     * @param name The name of the enumeration whose images to retrieve.
     * @param imageCount The amount of images that are stored for the specified 
     *        enumeration.
     * @return An array containing the individual images of the given enum.
     */
    private static ImageIcon[] getImages(String name, int imageCount) {
        ImageIcon[] images = new ImageIcon[imageCount];
        for (int i = 0; i < images.length; i++) {
            String path = "resources/actors/" + name + '/' + i + ".png";
            images[i] = new ImageIcon(path);
        }
        return images;
    }
    
    /**
     * Generates a {@code String} array of the names of every enumerated type in
     * this class.
     * 
     * @return A {@code String} array of the names of every enumerated type.
     */
    public static final String[] allNames() {
        String[] names = new String[Actor.values().length];
        for (int i = 0; i < names.length; i++) {
            names[i] = Actor.values()[i].getName();
        }
        return names;
    }
    
}