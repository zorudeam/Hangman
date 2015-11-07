/*
 * Data Type Information:
 * 
 * Data Type      Size (bits)           Minimum Value             Maximum Value    
 * boolean                  1                   false                      true    
 * byte                     8                    -128                       127    
 * short                   16                  -32768                     32767    
 * char                    16                       0                     65535    
 * int                     32             -2147483648                2147483647    
 * float                   32                 1.4e-45              3.4028235e38    
 * long                    64    -9223372036854775808       9223372036854775807    
 * double                  64               -4.9e-324    1.7976931348623157e308    
 * Object                 n/a                     n/a                       n/a
 * 
 * ...          Default Value        Signed/Unsigned?    Wrapper Class
 * boolean              false                     n/a       java.lang.Boolean
 * byte                     0                     n/a              ...Byte
 * short                    0                     n/a              ...Short
 * char                     0                     n/a              ...Character
 * int                      0                     n/a              ...Integer
 * float                 0.0f                     n/a              ...Float
 * long                    0l                     n/a              ...Long
 * double                0.0d                     n/a              ...Double
 * Object                null                     n/a       has no wrapper, 
 *                                                          always exists as an
 *                                                          instance of
 *                                                                 ...Object
 */
package functions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The {@code Utilities} class contains commonly used methods.
 *
 * @author Oliver Abdulrahim
 */
public class Utilities {

    /**
     * Field for managing the generation of pseudorandom values. This ensures
     * that all random values have the same generation seed.
     */
    public static final Random r = new Random();

    /**
     * Don't let anyone instantiate this class.
     */
    private Utilities() {
        throw new InstantiationError();
    }
    
    /**
     * Generates a random array of integers with the specified parameters.
     *
     * @param arraySize The size of the array to be generated.
     * @param bound The highest number that can be generated in the array 
     *        [{@code 0}, {@code bound}).
     * @return The generated random integer array.
     */
    public static int[] randomArray(int arraySize, int bound) {
        int[] array = new int[arraySize];
        for (int i = 0; i < array.length; i++) {
            array[i] = r.nextInt(bound);
        }
        return array;
    }

    /**
     * Generates and returns a pseudorandom integer value between {@code min} 
     * and {@code max} (inclusive).
     * 
     * @param min The minimum range value, inclusive.
     * @param max The maximum range value, inclusive.
     * @return A pseudorandom integer value between {@code min} and {@code max}
     *         arguments(inclusive).
     */
    public static int randomInteger(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * Generates and returns a random RGB {@code Color}.
     * 
     * @return A random {@code Color}.
     */
    public static java.awt.Color randomColor() {
        return new java.awt.Color(randomInteger(0, 255), 
                randomInteger(0, 255),
                randomInteger(0, 255));
    }
    
    /**
     * Generates and returns a {@code String} containing all integers within 
     * {@code lower} and {@code upper}. For integers that are multiples of 
     * {@code 3} or {@code 5}, appends {@code "Fizz"} or {@code "Buzz"}, 
     * respectively. For integers which are multiples of both, appends 
     * {@code "FizzBuzz"}.
     * 
     * @param lower The lower, inclusive bound for integers to append.
     * @param upper The upper, inclusive bound for integers to append.
     * @return The generated {@code String} of integers.
     */
    public static String fizzBuzz(int lower, int upper) {
        StringBuilder sb = new StringBuilder();
        for (int i = lower; i <= upper; i++) {
            String current = "";
            current += (i % 3) == 0 ? "Fizz" : "";
            current += (i % 5) == 0 ? "Buzz" : "";
            sb.append((!current.isEmpty()) ? current : i);
        }
        return sb.toString();
    }
    
    /**
     * Checks if a number is even using a bitwise implementation. If the given 
     * argument is even, return {@code true}, otherwise return {@code false}.
     * 
     * @param number The number to test.
     * @return {@code true} if and only if {@code number} is even, otherwise 
     *         returns {@code false}.
     */
    public static boolean isEven(long number) {
        return (number & 1) == 0;
    }
    
    /**
     * Checks if a number is a power of two using a bitwise implementation. 
     * Powers of two include {@code {1, 2, 4, 8, 16...}}. Checks the 
     * {@code number} argument with the next integral type for matching bit 
     * values. If the argument is a power of two, return {@code true}, otherwise
     * return {@code false}. 
     * 
     * @param number The number to test.
     * @return {@code true} if and only if {@code number} is a power of 
     *         two, otherwise returns {@code false}.
     */
    public static boolean isPowerOfTwo(long number) {
        return (number & (number + 1)) == 0;
    }

    /**
     * Iterative implementation of a binary search, which has &Omicron;(log n) 
     * worst case scenario. This method only returns accurate results for sorted 
     * arrays.
     * 
     * @see java.util.Arrays#binarySearch(java.lang.Object[], java.lang.Object)
     * @param a The array to search for {@code key} in.
     * @param firstIndex The first index value to constrain the search to.
     * @param endIndex The last index value to constrain the search to.
     * @param key The item to search for in {@code a}.
     * @return The first index of {@code key} in the given array {@code a}. If 
     *         there is no occurrence, return the inverse of {@code toIndex}. 
     */
    public static int binarySearch(int[] a, int firstIndex, int endIndex,
            int key) 
    {
        int low = firstIndex;
        int high = endIndex - 1;
        while (low <= high) {
            int middle = (low + high) >>> 1;
            int middleValue = a[middle];
            if (middleValue < key) {
                low = middle + 1;
            } 
            else if (middleValue > key) {
                high = middle - 1;
            } 
            else {
                return middle; // The key was found, return its index value.
            }
        }
        return -(low + 1); // The key was not found, return -toIndex.
    }
    
    /**
     * Iterative implementation of a selection sort, which has &Omicron;(n<sup>2
     * </sup>) total worst case performance. This implementation sorts in 
     * ascending order.
     * 
     * @param a The array to sort using this algorithm. 
     */
    public static void selectionSort(int[] a) {
        for (int i = 0; i < a.length; i++) {
            int smallIndex = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[j] < a[smallIndex]) {
                    smallIndex = j;
                }
            }
            final int temp = a[i]; // Swap the next smallest with this position.
            a[i] = a[smallIndex];
            a[smallIndex] = temp;
        }
    }
    
    /**
     * Iterative implementation of a selection sort, which has &Omicron;(n) 
     * total worst case performance. This implementation sorts in ascending 
     * order.
     * 
     * @param a The array to sort using this algorithm. 
     */
    public static void insertionSort(int[] a) {
        for (int i = 0; i < a.length; i++) {
            int temp = a[i];   // The element to insert.
            int nextIndex = i; // Next index of insertion candidate.
            while (nextIndex > 0 && temp < a[nextIndex - 1]) {
                a[nextIndex] = a[nextIndex - 1];
                nextIndex--;
            }
            a[nextIndex] = temp;
        }
    }
    
    /**
     * Sorts a given array of integers in ascending order using a recursive 
     * merge sort.
     * 
     * @param a The array to sort using this algorithm.
     */
    public static void mergeSort(int[] a) {
        merge(a, new int[a.length], 0, a.length - 1);
    }
    
    /**
     * Merges two adjacent array parts, each of which has been already sorted in
     * ascending order.
     * 
     * @param a The array to sort using this algorithm.
     * @param temp A temporary array to aid in the recursive merge process.
     * @param fromIndex The index to begin sorting in {@code a}.
     * @param middleIndex The ending index of {@code a} of this part of the 
     *        merge process.
     * @param toIndex The index to stop sorting in {@code a}.
     * @see #mergeSort(int[]) 
     * @see #merge(int[], int, int, int[]) 
     */
    private static void finishMerge(int a[], int[] temp, int fromIndex,
            int middleIndex, int toIndex) 
    {
        int i = fromIndex;
        int j = middleIndex + 1;
        int k = fromIndex;
        while (i <= middleIndex && j <= toIndex) {
            if (a[i] < a[j]) {
                temp[k] = a[i];
                i++;
            }
            else {
                temp[k] = a[j];
                j++;
            }
            k++;
        }
        while (i <= middleIndex) {
            temp[k] = a[i];
            i++;
            k++;
        }
        while (j <= toIndex) {
            temp[k] = a[j];
            j++;
            k++;
        }
        for (k = fromIndex; k <= toIndex; k++) { // Fill and finish a[]int.
            a[k] = temp[k];
        }
    }
    
    /**
     * Helper method for {@link #mergeSort(int[])}. Sorts the elements
     * between the indeces {@code fromIndex} and {@code toIndex} in the given 
     * {@code a} array.
     * 
     * @param a array to subsort using this algorithm. 
     * @param temp A temporary array to aid in the recursive merge process.
     * @param fromIndex The index to begin sorting in {@code a}.
     * @param toIndex The index to stop sorting in {@code a}.
     * @see #mergeSort(int[])
     * @see #finishMerge(int[], int, int, int, int[])
     */
    private static void merge(int[] a, int[] temp, int fromIndex, int toIndex) {
        if (fromIndex < toIndex) {
            int middleIndex = (fromIndex + toIndex) / 2;
            merge(a, temp, fromIndex, middleIndex);
            merge(a, temp, middleIndex + 1, toIndex);
            finishMerge(a, temp, fromIndex, middleIndex, toIndex);
        }
    }
    
    /**
     * Generates and returns a timestamp of the current instant at the time of
     * invocation using the following simple pattern:
     * 
     * <pre>{@code
     * new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
     * }</pre>
     * 
     * @return A timestamp representing the current instant in time.
     */
    public static String timestamp() {
        return new SimpleDateFormat("MM/dd/yyyy h:mm:ss a").format(new Date());
    }

}
