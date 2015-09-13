package game;

import javax.swing.ImageIcon;

/**
 * The {@code Actor} enumeration is a structure for constant values relating to 
 * the "Hangman" game class. Each enumeration has a standard set of images with
 * pre-defined physical properties for use in a graphical user interface.
 * 
 * @author Oliver Abdulrahim
 */
public enum Actor {
    
    /**
     * Enumeration that represents a classic Stick Figure. This enumeration
     * should have {@code 8} images in total.
     */
    HUMAN("Stick Figure", getImages("Human", 8)
    ),
    
    /**
     * Enumeration that represents a Snowman. This enumeration should have 
     * {@code 8} images in total.
     */
    SNOWMAN("Snowman", getImages("Snowman", 8)
    );
    
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
    
    /**
     * Accessor method for {@code actorName} field.
     *
     * @return  Returns the {@code actorName} field of this instance.
     */
    public String getActorName() {
        return actorName;
    }

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
    
}
