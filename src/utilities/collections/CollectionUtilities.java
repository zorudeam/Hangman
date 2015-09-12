/*
 * A fancy license header.
 */
package utilities.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * The {@code CollectionUtilities} class contains utility methods related to the
 * {@link Collection} hierarchy.
 * 
 * @author Oliver Abdulrahim
 * @see Collection
 */
public final class CollectionUtilities {
    
    /**
     * The maximum allocable size of array. This value is one full bit less than
     * (2<sup>31</sup> - 1). Any attempts to allocate an array length larger 
     * than this value may exceed the VM limit and result in an 
     * {@link java.lang.OutOfMemoryError}.
     */
    public static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    
    /**
     * Defines an empty {@code Object} array for convenience.
     */
    public static final Object[] EMPTY_OBJECT_ARRAY = {};
    
    /**
     * Don't let anyone instantiate this class.
     */
    private CollectionUtilities() {
    
    }

    /**
     * Ensures that a given {@code Collection} is not {@code null} and contains
     * no {@code null} elements.
     * 
     * @param c The {@code Collection} to test for non-nullity.
     * @throws NullPointerException if the given {@code Collection} refers to
     *         {@code null} or contains {@code null} a element or elements.
     */
    public static void requireNonNullCollection(Collection<?> c) {
        Objects.requireNonNull(c, "Invalid null Collection!");
        c.stream().forEach((element) -> {
            if (element == null) {
                throw new NullPointerException("Invald null element in c");
            }
        });
    }
    
    /**
     * Ensures that a given {@code Collection} returns an appropriate array of
     * objects from its {@link Collection#toArray()} method. This method is
     * typically used in the case of an invalid {@code toArray()} implementation
     * to prevent any unexpected or undesired behavior.
     * 
     * @param c The {@code Collection} to convert to a {@code Object} array.
     * @return An array containing the {@link Collection#toArray()} of the given
     *         argument.
     */
    @SuppressWarnings("unchecked")
    public static Object[] ensureValidToArray(Collection<?> c) {
        Object[] elements = c.toArray();
        if (elements.getClass() != Object[].class) { 
            elements = Arrays.copyOf(elements, elements.length, Object[].class);
        }
        return elements;
    }
    
    /**
     * Trims the capacity of a given {@code Object} array to the given capacity
     * argument.
     * 
     * @param elements The array of objects whose length to trim to the desired 
     *                 capacity.
     * @param desiredCapacity The desired new length of the given array.
     * @return The trimmed version of the given array.
     */
    public static Object[] trimToSize(Object[] elements, int desiredCapacity) {
        if (elements.length > desiredCapacity) {
            elements = (desiredCapacity == 0)
                    ? EMPTY_OBJECT_ARRAY
                    : Arrays.copyOf(elements, desiredCapacity);
        }
        return elements;
    }
    
    /**
     * Generates a {@code String} representation of the range of a given indexed
     * array.
     * 
     * @param array The array to generate a range {@code String} for.
     * @return A {@code String} representing the range of the given array.
     */
    public static String generateArrayRange(Object[] array) {
        return "[0, " + array.length + ']';
    }
    
    /**
     * Generates a standard error message intended for use as a detail message
     * for any thrown {@code IndexOutOfBoundsException}.
     * 
     * @param c The {@code Collection} to generate a message for.
     * @param index The invalid index to generate a message with.
     * @return A formatted {@code String} for use as a detail message for an 
     *         {@code IndexOutOfBoundsException}.
     */
    public static String outOfBoundsMessage(Collection<?> c, int index) {
        Objects.requireNonNull(c, "Invalid null Collection!");
        return outOfBoundsMessage(ensureValidToArray(c), index);
    }
    
    /**
     * Generates a standard error message intended for use as a detail message
     * for any thrown {@code IndexOutOfBoundsException}.
     * 
     * @param array The array to generate a message for.
     * @param index The invalid index to generate a message with.
     * @return A formatted {@code String} for use as a detail message for an 
     *         {@code IndexOutOfBoundsException}.
     */
    public static String outOfBoundsMessage(Object[] array, int index) {
        Objects.requireNonNull(array, "Invalid null array!");
        return "Index = " + index + ", Capacity = " + array.length;
    }
    
}