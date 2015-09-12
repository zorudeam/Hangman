/**
 * TODO - Get a job.
 */
package hangman;

//<editor-fold defaultstate="collapsed" desc="Imports">

import java.util.Arrays;
import javax.swing.ImageIcon;

//</editor-fold>

/**
 * The {@code Actor} enumeration is a structure for constant values relating to 
 * the "Hangman" game class. Each enumeration has a standard set of images with
 * pre-defined physical properties for use in a graphical user interface.
 * 
 * @author Oliver Abdulrahim
 */
public enum Actor {
    
    //<editor-fold defaultstate="collapsed" desc="Enumeration Types">
    
    /**
     * Enumeration that represents a classic Stick Figure. This enumeration
     * should have {@code 8} images in total.
     */
    STICK_FIGURE("Stick Figure",
            getImages("Human", 8)
    ),
    
    /**
     * Enumeration that represents a Snowman. This enumeration should have 
     * {@code 8} images in total.
     */
    SNOWMAN("Snowman",
            getImages("Snowman", 8)
    );
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Attributes and Constants">
    
    /**
     * Field used for identifying the object and for ease of display purposes in
     * graphical interfaces.
     */
    private final String actorName;
    
    /**
     * Field that contains this object's images for use in a graphical user
     * display. This image array should be iterated in its entirety before the
     * "end" of the Hangman game.
     */
    private final ImageIcon[] imageArray;
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Accessor Code">
    
    /**
     * Accessor method for {@code actorName} field.
     *
     * @return  Returns the {@code actorName} field of this instance.
     */
    public String getActorName() {
        return actorName;
    }
    
    /**
     * Accessor method for {@code imageArray} field.
     *
     * @return  Returns the {@code imageArray} field of this instance.
     */
    public ImageIcon[] getImageArray() {
        return imageArray;
    }

    //</editor-fold>
    
    /**
     * Default and only constructor, instantiates all fields relating to the 
     * {@code Actor} class with the given arguments.
     * 
     * @param name      The {@code String} to assign to {@code actorName}.
     * @param images    The {@code ImageIcon[]} to assign to {@code imageArray}.     
     */
    private Actor(String name, ImageIcon[] images) {
        actorName  = name;
        imageArray = images;
    }
    
    /**
     * Method that generates an array of images using the given enumeration's 
     * {@code ImageIcon} icon sets. These sets are located within the 
     * "resources" folder of the project.
     * 
     * @param enumName      The name of the enumeration to retrieve the images 
     *                      of.
     * @param imageCount    The amount of images that are stored for the
     *                      specified enumeration.
     * @return              
     */
    public static final ImageIcon[] getImages(String enumName, int imageCount) {
        ImageIcon[] images = new ImageIcon[imageCount];
        for (int i = 0; i < images.length; i++) {
            images[i] = new ImageIcon("resources/actors/" + enumName + "/" + i
                    + ".png");
        }
        return images;
    }
    
    /**
     * Method that generates a {@code String} array of the names of every
     * enumerated type in this {@code enum}.
     * 
     * @return  A {@code String} array of names.
     */
    public static final String[] getNames() {
        String[] names = new String[Actor.values().length];
        for (int i = 0; i < names.length; i++) {
            names[i] = Actor.values()[i].getActorName();
        }
        return names;
    }
    
    /**
     * Returns a {@code String} representation of the object. In general, the
     * {@code toString} method returns a string that "textually represents" this 
     * object.
     * 
     * @return  Returns a formatted {@code String} that represents this object.
     */
    @Override
    public String toString() {
        return "Actor{" 
             + "actorName = "    + actorName 
             + ", imageArray = " + Arrays.deepToString(imageArray)
             + '}';
    }
    
}
