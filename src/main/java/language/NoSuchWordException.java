package language;

/**
 * Thrown to indicate that a word of a particular type does not exist in a given
 * or arbitrary context.
 * 
 * @author Oliver Abdulrahim
 */
public class NoSuchWordException 
    extends RuntimeException
{
    
    private static final long serialVersionUID = 837456234897L;
    
    /**
     * Creates a new instance of {@code NoSuchWordException} without a detail
     * message.
     */
    public NoSuchWordException() {
        super();
    }
    
    /**
     * Creates a new instance of {@code NoSuchWordException} without a detail
     * message.
     * 
     * @param message The message to construct this exception with.
     * @param difficulty The difficulty that caused the exception.
     */
    public NoSuchWordException(String message, Difficulty difficulty) {
        super(message + " (There are no words of the given "
                + "difficulty \"" + difficulty + "\" contained within this "
                + "dictionary.)");
    }
    
}
