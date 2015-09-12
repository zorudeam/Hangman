/*
 * A fancy license header.
 */
package incremental;

import javax.swing.ImageIcon;

/**
 * The {@code Player} class provides for a general structure for a player that 
 * participates in an arbitrary game.
 * 
 * @author Oliver Abdulrahim
 */
public class Player {
    
    /**
     * Defines a default name for use if no name is provided during 
     * construction.
     * 
     * @see #Player()
     * @see #Player(javax.swing.ImageIcon)
     */
    private static final String DEFAULT_NAME = "No-Name";
    
    /**
     * Defines a default score for use if no score is provided during 
     * construction or if the player's score is to be reset.
     * 
     * @see #Player()
     * @see #Player(java.lang.String)
     * @see #Player(javax.swing.ImageIcon)
     */
    private static final double DEFAULT_SCORE = 0.0d;
    
// TODO - Add a default image!
    
    /**
     * Defines a default icon for use if no name is provided during 
     * construction or if the player's icon is to be reset.
     * 
     * @see #Player()
     * @see #Player(java.lang.String)
     */
    private static final ImageIcon DEFAULT_IMAGE = null;
    
    /**
     * Identifies this {@code Player} instance.
     * 
     * @see #getName()
     */
    private final String name;
    
    /**
     * Defines an image that visually represents this {@code Player}.
     * 
     * @see #getIcon()
     * @see #setIcon(javax.swing.ImageIcon)
     */
    private ImageIcon icon;
    
    /**
     * Defines the score of this {@code Player}.
     * 
     * @see #getScore()
     * @see #setScore(double)
     */
    private double score;
    
    /**
     * Defines the state of this {@code Player} with respect to the game, 
     * {@code true} representing the victor of the game.
     * 
     * @see #isWinner()
     * @see #setWinner(boolean)
     */
    private boolean winner;

    /**
     * Constructs a new {@code Player} with the default name, score, and icon.
     */
    public Player() {
        this(DEFAULT_NAME, DEFAULT_IMAGE, DEFAULT_SCORE);
    }
    
    /**
     * Constructs a new {@code Player} with the given values.
     * 
     * @param n The name for this {@code Player}.
     * @param i The icon for this {@code Player}.
     * @param s The score for this {@code Player}.
     */
    public Player(String n, ImageIcon i, double s) {
        name = n;
        icon = i;
        score = s;
        winner = false;
    }

    /**
     * Returns this {@code Player}'s name.
     * 
     * @return The name for this instance.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns this {@code Player}'s icon.
     * 
     * @return The icon for this instance.
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * Sets the icon for this {@code Player}.
     * 
     * @param i The icon for this instance.
     */
    public void setIcon(ImageIcon i) {
        icon = i;
    }

    /**
     * Returns this {@code Player}'s score.
     * 
     * @return The score for this instance.
     */
    public double getScore() {
        return score;
    }
    
    /**
     * Sets the score for this {@code Player}.
     * 
     * @param s The score for this instance.
     */
    public void setScore(double s) {
        score = s;
    }

    /**
     * Resets the score of this {@code Player} to the default score.
     */
    public void resetScore() {
        score = DEFAULT_SCORE;
    }
    
    /**
     * Returns this {@code Player}'s win state.
     * 
     * @return The winner state for this instance.
     */
    public boolean isWinner() {
        return winner;
    }
    
    /**
     * Sets the win state for this {@code Player}.
     * 
     * @param state The winner state for this instance.
     */
    public void setWinner(boolean state) {
        winner = state;
    }
    
    /**
     * Returns a {@code String} representation of this {@code Player}.
     * 
     * @return A {@code String} that represents this instance.
     */
    @Override
    public String toString() {
        return "Player{"
                + "name = "     + name
                + ", score = "  + score
                + ", icon = "   + icon
                + ", winner = " + winner
                + '}';
    }
    
}
