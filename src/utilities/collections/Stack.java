/*
 * A fancy license header.
 */
package utilities.collections;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * The {@code Stack} class contains a last-in first-out (LIFO) stack data 
 * structure with fail-fast iterators and dynamic structure resizing. Elements 
 * with {@code null} values are <em>not</em> permitted in this structure, as 
 * contracted by the {@link #add(java.lang.Object)}, 
 * {@link #set(int, java.lang.Object)} and {@link #push(java.lang.Object)}
 * operations.
 * 
 * <p> This implementation is <em>not</em> synchronized. In order to achieve 
 * proper concurrent operation, instances of this class should be synchronized 
 * on some object that naturally encapsulates the stack.
 * 
 * <p> If no such object exists, the list should be "wrapped" using the
 * {@link java.util.Collections#synchronizedList(java.util.List)} method using
 * the following idiom:
 * 
 * <blockquote><pre>
 * List list = {@link java.util.Collections#synchronizedList(java.util.List)
 * Collections.synchronizedList(new Stack(...));}</pre>
 * </blockquote>
 * 
 * @param <E> The type of object to store within this {@code Stack}.
 * @author Oliver Abdulrahim
 */
public class Stack<E>
    extends AbstractList<E>
    implements Cloneable, Serializable
{

    /**
     * Defines the default capacity of a {@code Stack} if no size is provided
     * during construction.
     * 
     * @see #Stack()
     */
    private static final int DEFAULT_CAPACITY = 10;
    
    /**
     * Defines an empty {@code Object} array instance for use when resizing this
     * {@code Stack}.
     */
    private static final Object[] EMPTY_ELEMENT_DATA = {};

    /**
     * The serial version ID of the {@code Stack} class.
     */
    private static final long serialVersionUID = 3109256773218160485L;
    
    /**
     * Stores this {@code Stack}'s elements. The elements contained within this
     * indexed array are ordered only by how elements are entered into it, or in
     * other words, there exists no priority system other than the natural order
     * of the elements. Elements are added using the {@code 0} index as the 
     * head, or top, of the stack structure.
     * 
     * <p> This array is dynamically resized as elements are added to the 
     * structure, therefore, it is always the case that {@link #size}, which 
     * stores the amount of "physical" elements, is greater than or equal to the
     * length of this array ({@code size >= elementData.length}).
     * 
     * @see #size Stores the amount of "physical" elements in this 
     *      {@code Stack}.
     * @see #trimToSize() Trims the length of this array to exactly the amount 
     *      of elements that it stores.
     */
    private transient Object[] elementData;
    
    /**
     * Stores the amount of elements currently contained within this 
     * {@code Stack}. While this value may be equal to the number of elements 
     * in {@link #elementData}, or in other words, 
     * {@code size == elementData.length}, this is not an absolute requirement
     * due to the resizing nature of the element structure.
     * 
     * @see #elementData
     * @see #size() 
     */
    private transient int size;
    
    /**
     * Creates a new {@code Stack} with the default number of elements of 
     * {@code 10}.
     * 
     * @see #DEFAULT_CAPACITY
     * @see #Stack(int) Invoked by this constructor with the default capacity 
     *      argument.
     */
    public Stack() {
        this(DEFAULT_CAPACITY);
    }
    
    /**
     * Constructs a new {@code Stack} with the specified size. 
     * 
     * @param size The initial size to allocate.
     * @throws NegativeArraySizeException if the given size argument is 
     *         negative.
     */
    public Stack(int size) {
        if (size < 0) {
            throw new NegativeArraySizeException("size : " + size + " < 0 !");
        }
        elementData = new Object[size];
        this.size = 0; // No "physical" elements yet.
    }
    
    /**
     * Constructs a new {@code Stack}, adding all elements from the specified
     * {@code Collection} to this object's {@link #elementData}.
     * 
     * @param c The {@code Collection} to load elements from. This must contain
     *        elements which are lower-bounded objects of type {@code E}.
     *        Additionally, there may not be any {@code null} elements contained
     *        within this {@code Collection}.
     * @throws NullPointerException if the specified argument {@code c} is 
     *         {@code null}, or if the given {@code Collection} contains any
     *         {@code null} elements.
     */
    public Stack(Collection<? extends E> c) {
        requireNonNullCollection(c);
        initFromStack(c);
    }
    
    /**
     * Initializes this {@code Stack} with the elements in the given 
     * {@code Collection}. If the given argument is an instance of this class,
     * immediately retrieves its data; otherwise treat it as a regular 
     * {@code Collection}.
     * 
     * @param c The {@code Collection} to load elements from. This must contain
     *          elements which are lower-bounded objects of type {@code E}.
     * @see #initFromCollection(java.util.Collection)
     */
    private void initFromStack(Collection<? extends E> c) {
        if (c.getClass() == Stack.class) {
            elementData = c.toArray();
            size = c.size();
        }
        else {
            initFromCollection(c);
        }
    }

    /**
     * Initializes this {@code Stack} with the elements in the given 
     * {@code Collection}.
     * 
     * @param c The {@code Collection} to load elements from. This must contain
     *          elements which are lower-bounded objects of type {@code E}.
     */
    private void initFromCollection(Collection<? extends E> c) {
        Object[] elements = c.toArray();
        // Ensures elements is of type Object[] in the case of invalid toArray()
        if (elements.getClass() != Object[].class) {
            elements = Arrays.copyOf(elements, elements.length, Object[].class);
        }
        elementData = elements;
        size = elements.length;
    }
    
    /**
     * Ensures that a given {@code Collection} is not {@code null} and contains
     * no {@code null} elements.
     * 
     * @param c The {@code Collection} to test for non-nullity.
     * @throws NullPointerException if the given {@code Collection} refers to
     *         {@code null} or contains {@code null} a element or elements.
     */
    private void requireNonNullCollection(Collection<?> c) {
        Objects.requireNonNull(c, "Invalid null Collection!");
        for (Object element : c) {
            if (element == null) {
                throw new NullPointerException("Invalid null element in c!");
            }
        }
    }
    
    /**
     * Ensures that the given index argument is a valid, accessible index value
     * within this object's {@link #elementData}.
     * 
     * @param index The index value or index values to test for validity.
     * @throws IndexOutOfBoundsException if the specified index value or values
     *         are out of range.
     */
    private void rangeCheck(int... index) {
        for (int i = 0; i < index.length; i++) {
            if (index[i] < 0 || index[i] >= elementData.length) {
                throw new IndexOutOfBoundsException(
                        outOfBoundsMessage(index[i]));
            }
        }
    }
    
    /**
     * Ensures that the given index argument is a valid, accessible index value
     * within this object's {@link #elementData}. This method is intended for
     * use within any operations that add elements to this collection.
     * 
     * @param index The index value or index values to test for validity.
     * @throws IndexOutOfBoundsException if the specified index value or values
     *         are out of range.
     * @see #add(java.lang.Object)
     * @see #add(int, java.lang.Object)
     */
    private void rangeCheckForAdd(int... index) {
        for (int i = 0; i < index.length; i++) {
            if (index[i] < 0 || index[i] > elementData.length) {
                throw new IndexOutOfBoundsException(
                        outOfBoundsMessage(index[i]));
            }
        }
    }
    
    /**
     * Generates a standard error message intended for use as a detail for any 
     * thrown {@code IndexOutOfBoundsException} within this class.
     * 
     * @param index The invalid index to generate a message with.
     * @return A formatted {@code String} for use as a detail message for an 
     *         {@code IndexOutOfBoundsException}.
     */
    private String outOfBoundsMessage(int index) {
        return "Index = " + index + ", Capacity = " + elementData.length;
    }
    
    /**
     * Returns the element at the given index in this object's 
     * {@link #elementData}. Intended for use within this class in order to
     * prevent verbose usage of the {@link SuppressWarnings} annotation.
     * 
     * @param index The index of the element to return.
     * @return The element in {@link #elementData} at the given index.
     */
    @SuppressWarnings("unchecked")
    private E elementData(int index) {
        return (E) elementData[index];
    }
    
    /**
     * Trims the capacity of this {@code Stack} to be the current {@link #size}.
     */
    public void trimToSize() {
        modCount++;
        if (elementData.length > size) {
            elementData = (size == 0)
                    ? EMPTY_ELEMENT_DATA
                    : Arrays.copyOf(elementData, size);
        }
    }
    
    /**
     * Increases the capacity of this {@code Stack}, ensuring it {@link #size()}
     * is at least one greater than the specified argument.
     * 
     * @param minimumCapacity The minimum capacity.
     */
    public void ensureCapacity(int minimumCapacity) {
        int expand = (elementData != EMPTY_ELEMENT_DATA) 
                ? 0 
                : DEFAULT_CAPACITY;
        if (minimumCapacity > expand) {
            ensureExplicitCapacity(minimumCapacity);
        }
    }
    
    /**
     * Ensures that this {@code Stack} has a valid capacity, or size by 
     * comparing its {@link #elemnetData} against the given capacity argument.
     * This method is intended for use only within this class.
     * 
     * @param minimumCapacity The minimum capacity.
     * @see #ensureExplicitCapacity(int) 
     */
    private void internalEnsureCapacity(int minimumCapacity) {
        if (elementData == EMPTY_ELEMENT_DATA) {
            minimumCapacity = Math.max(DEFAULT_CAPACITY, minimumCapacity);
        }
        ensureExplicitCapacity(minimumCapacity);
    }
    
    /**
     * Ensures that this {@code Stack} has a valid capacity, or size by 
     * comparing its {@link #elemnetData} against the given capacity argument.
     * This method is separate from the {@link #internalEnsureCapacity(int)}
     * method to simplify access for other operations. 
     * 
     * <p> Since it may grow the size of this object's {@link #elementData},
     * this method constitutes a structural modification.
     * 
     * @param minimumCapacity The minimum capacity.
     * @see #internalEnsureCapacity(int) 
     */
    private void ensureExplicitCapacity(int minimumCapacity) {
        modCount++;
        if (minimumCapacity - elementData.length > 0) {
            grow(minimumCapacity);
        }
    }
    
    /**
     * Increases the capacity of this object's {@link #elementData}. Makes
     * certain that the new, resized data can contain at the very least the
     * amount of elements specified. 
     * 
     * @param minimumCapacity The new minimum capacity.
     */
    private void grow(int minimumCapacity) {
        int oldCapacity = elementData.length;
        // Grow by 50% to accommodate the desired capacity
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        
        // Next two tests check if newCapacity has overflowed.
        if (newCapacity - minimumCapacity < 0) {
            newCapacity = minimumCapacity;
        }
        if (newCapacity - CollectionUtilities.MAX_ARRAY_SIZE > 0) {
            newCapacity = (newCapacity > CollectionUtilities.MAX_ARRAY_SIZE) 
                ? Integer.MAX_VALUE
                : CollectionUtilities.MAX_ARRAY_SIZE;
        }
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
    
    /**
     * Retrieves the integer size, or the total amount of elements contained
     * within this {@code Stack}.
     * 
     * @return The {@link #size} of this stack.
     * @see #size The instance variable that is returned by this method.
     */
    @Override
    public int size() {
        return size;
    }
    
    /**
     * Tests if this {@code Stack} object contains no elements, simply testing
     * if {@code size() == 0}.
     * 
     * @return {@code true} if this stack has no elements, {@code false}
     *         otherwise.
     * @see #size()
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Retrieves, but does not remove, the top of this stack, which is the last 
     * and newest element. If this stack is empty, throws an 
     * {@code EmptyStackException}.
     * 
     * @return The element at the top of this stack.
     * @throws EmptyStackException if the stack is empty.
     */
    public E peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return elementData(0);
    }
    
    /**
     * Retrieves and removes the top of this stack, which is the last and newest
     * element.
     * 
     * <p> This method call constitutes a structural modification and is 
     * equivalent to invoking {@code remove(0)}, except that this method throws
     * an {@code EmptyStackException} if this structure is empty.
     * 
     * @return The element removed from top of this stack.
     * @throws EmptyStackException if the stack is empty.
     */
    public E pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        modCount++;
        size--;
        return remove(0);
    }
    
    /**
     * Inserts the specified element into the top of this stack and returns it,
     * rejecting all {@code null} arguments from the structure.
     * 
     * <p> This method call constitutes a structural modification and is 
     * equivalent to invoking {@code add(element)}, the only difference being 
     * that this method returns the specified element.
     * 
     * @param element The element of to add to the top of this stack.
     * @return The element that was added to the stack.
     */
    public E push(E element) {
        add(0, element); // Increments modCount
        return element;
    }

    /**
     * Adds a given element at the top of this stack. This operation constitutes
     * a structural modification.
     * 
     * @param element The element to add to the top of this stack.
     * @return {@code true} if the given object was added to this structure.
     * @throws NullPointerException if the specified argument {@code element} 
     *         is {@code null}.
     */
    @Override
    public boolean add(E element) {
        Objects.requireNonNull(element);
        internalEnsureCapacity(size + 1); // Increments modCount
        
        elementData[size] = element;
        size++;
        return true;
    }
    
    /**
     * Adds a given element at the specified position in this {@code Stack}. 
     * This operation constitutes a structural modification.
     * 
     * @param index The index in to add the given element.
     * @param element The element to add.
     * @throws IndexOutOfBoundsException if the specified index value is out of
     *         range.
     * @throws NullPointerException if the specified argument {@code element} 
     *         is {@code null}.
     */
    @Override
    public void add(int index, E element) {
        Objects.requireNonNull(element);
        rangeCheckForAdd(index);
        internalEnsureCapacity(size + 1); // Increments modCount

        System.arraycopy(elementData, index, elementData, index + 1, 
                size - index);
        elementData[index] = element;
        size++;
    }

    /**
     * Replaces the element at the specified position in this {@code Stack} with
     * the specified element. Returns the element previously at the specified
     * index.
     *
     * @param index The index of the element to replace.
     * @param element The element to be stored at the specified index.
     * @return The element previously at the specified index.
     * @throws IndexOutOfBoundsException if the specified index value is out of
     *         range.
     * @throws NullPointerException if the specified argument {@code element} 
     *         is {@code null}.
     */
    @Override
    public E set(int index, E element) {
        Objects.requireNonNull(element);
        rangeCheck(index);
        
        E old = elementData(index);
        elementData[index] = element;
        return old;
    }
    
    /**
     * Returns the element at the specified position in this {@code Stack}.
     * 
     * @param index The index of the element to retrieve.
     * @return The element at the specified index.
     * @throws IndexOutOfBoundsException if the specified index value is out of
     *         range.
     */
    @Override
    public E get(int index) {
        rangeCheck(index);
        return elementData(index);
    }
    
    /**
     * Removes the first occurrence of the given {@code Object} from this 
     * {@code Stack}, if such an element exists. This method begins at the 
     * {@code 0} index and iterates upward. Returns {@code true} if the given 
     * object was removed from this stack. This operation constitutes a 
     * structural modification.
     * 
     * @param o The object attempt to remove.
     * @return {@code true} if the given object was removed from this stack's
     *         data.
     */
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        int i = indexOf(o);
        if (i != -1) {
            internalRemove(i); // Increments modCount
            return true;
        }
        return false;
    }

    /**
     * Removes and returns the object in this {@code Stack} at the given index
     * value. All elements succeeding the element at {@code i} are shifted left
     * towards the {@code 0} index. This operation constitutes a structural 
     * modification.
     * 
     * @param index The index of the object to remove.
     * @return The object that was removed from the given index.
     * @throws IndexOutOfBoundsException if the specified index value is out of
     *         range.
     */
    @Override
    public E remove(int index) {
        rangeCheck(index);        
        modCount++;

        E old = elementData(index);
        int moved = size - index - 1;
        if (moved > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, moved);
        }
        size--;
        elementData[size] = null;
        return old;
    }
    
    /**
     * Fast, private remove method for use within this class only. Does not 
     * return the removed element, nor does it check the given index for 
     * appropriate range. This operation constitutes a structural modification.
     * 
     * @param index The index to remove.
     */
    private void internalRemove(int index) {
        modCount++;
        int moved = size - index - 1;
        if (moved > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, moved);
        }
    }
    
    /**
     * Removes all elements contained within this {@code Stack}. After this
     * method call, {@link #isEmpty()} should always return {@code true}. This 
     * operation constitutes a structural modification.
     */
    @Override
    public void clear() {
        modCount++;
        for (int i = 0; i < size; i++) {
            elementData[i] = null;
        }
        size = 0;
    }
    
    /**
     * Inserts to this {@code Stack} all elements contained within the given 
     * {@code Collection} argument at the {@code size} index. This operation 
     * constitutes a structural modification.
     * 
     * @param c The {@code Collection} containing the elements to to this 
     *          {@code Stack}.
     * @return {@code true} if the add operation was successful, 
     *         {@code false} otherwise.
     * @throws NullPointerException if the specified argument {@code c} is 
     *         {@code null}.
     */
    @Override
    public final boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }
        requireNonNullCollection(c);
        
        Object[] array = c.toArray();
        int newElements = array.length;
        internalEnsureCapacity(size + newElements); // Increments modCount
        
        System.arraycopy(array, 0, elementData, size, newElements);
        size += newElements;
        return newElements != 0;
    }

    /**
     * Inserts to this {@code Stack} all elements contained within the given 
     * {@code Collection} argument at the specified index. All succeeding 
     * elements are shifted right, or away from the {@code 0} index. This 
     * operation constitutes a structural modification.
     * 
     * @param index The index to begin inserting the elements from the specified
     *              {@code Collection}.
     * @param c The {@code Collection} containing the elements to to this 
     *          {@code Stack}.
     * @return {@code true} if the add operation was successful, {@code false} 
     *         otherwise.
     * @throws IndexOutOfBoundsException if the specified index value is out of
     *         range.
     * @throws NullPointerException if the specified argument {@code c} is 
     *         {@code null}.
     */
    @Override
    public final boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);
        requireNonNullCollection(c);
        
        Object[] array = c.toArray();
        int newElements = array.length;
        internalEnsureCapacity(size + newElements); // Increments modCount
        
        int moved = size - index;
        if (moved > 0) {
            System.arraycopy(elementData, index, elementData, 
                    index + newElements, moved);
        }
        
        System.arraycopy(array, 0, elementData, size, newElements);
        size += newElements;
        return newElements != 0;
    }
    
    /**
     * Removes from this stack's structure all elements within the given range, 
     * shifting all succeeding elements left towards the {@code 0} index. If 
     * {@code fromIndex} is equal to {@code toIndex}, this method has no effect.
     * This operation constitutes a structural modification.
     * 
     * @param fromIndex The index of the first element to be removed.
     * @param toIndex The index after the last element to be removed.
     */
    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        modCount++;
        int moved = size - toIndex;
        System.arraycopy(elementData, toIndex, elementData, fromIndex, moved);
        
        int newSize = size - (toIndex - fromIndex);
        for (int i = newSize; i < size; i++) {
            elementData[i] = null;
        }
        size = newSize;
    }

    /**
     * Searches this stack's elements for a given element. If this list contains
     * the specified element, returns {@code true}, otherwise returns 
     * {@code false}.
     * 
     * @param o The object to search for.
     * @return {@code true} if the specified object {@code o} occurs at least
     *         once within the elements contained in this object, {@code false}
     *         otherwise.
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }
    
    /**
     * Tests if this {@code Stack} contains all the elements within the 
     * specified {@code Collection}. While the ordering of the objects does not 
     * matter, it is required that the given elements are not {@code null} and 
     * are exactly equal to any of their equivalents within this object, as
     * contracted by their respective {@link Object#equals(java.lang.Object)}
     * methods.
     * 
     * @param c The {@code Collection} whose elements to test for containment
     *        within this {@code Stack}.
     * @return {@code true} if this object contains all the elements specified
     *         within the given argument, {@code false} otherwise.
     * @throws NullPointerException if the specified argument {@code c} is 
     *         {@code null}.
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        requireNonNullCollection(c);
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns the first index of the given {@code Object} argument, or 
     * {@code -1} if it does not occur within this collection. This method
     * traverses {@link #elementData} in <em>forward</em> order, starting at the
     * {@code 0} index and iterating increasingly to {@link #size}.
     * 
     * @param o The {@code Object} to search for within this {@code Stack}.
     * @return The last index value of the object argument, or {@code -1} if it
     *         does not occur.
     */
    @Override
    public int indexOf(Object o) {
        if (o == null) {
            return -1;
        }
        for (int i = 0; i < size; i++) {
            if (elementData[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the last index of the given {@code Object} argument, or 
     * {@code -1} if it does not occur within this collection. This method
     * traverses {@link #elementData} in <em>reverse</em> order, starting at the
     * {@link #size} index and iterating decreasingly to {@code 0}.
     * 
     * @param o The {@code Object} to search for within this {@code Stack}.
     * @return The last index value of the object argument, or {@code -1} if it
     *         does not occur.
     */
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i > 0; i--) {
            if (elementData[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Removes from this {@code Stack} all elements which satisfy the given
     * {@code Predicate} argument. This operation constitutes a structural 
     * modification.
     * 
     * @param filter The {@code Predicate} which returns {@code true} for any
     *        elements that are to be removed from this collection.
     * @return {@code true} if this {@code Collection} was modified by this 
     *         method, {@code false} otherwise.
     * @throws NullPointerException if the specified {@code Predicate} argument
     *         is {@code null}.
     */
    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        modCount++;
        Objects.requireNonNull(filter);
        
        boolean removed = false;
        for (int i = 0; i < size; i++) {
            if (filter.test(elementData(i))) {
                internalRemove(i); // Increments modCount
                removed = true;
            }
        }
        return removed;
    }
    
    /**
     * Replaces each element within this {@code Collection} all results of 
     * applying the given {@code UnaryOperator} on that element. This operation 
     * may constitute a structural modification.
     * 
     * @param operator The {@code UnaryOperator} to apply to each element in
     *        this {@code Stack}.
     * @throws NullPointerException if the specified argument {@code c} is 
     *         {@code null}.
     */
    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        Objects.requireNonNull(operator);
        for (int i = 0; i < size; i++) {
            set(i, operator.apply(elementData(i)));
        }
    }
    
    /**
     * Removes all elements that <em>are</em> shared between this {@code Stack}
     * and the given {@code Collection}. This operation may constitute a 
     * structural modification.
     * 
     * @param c The {@code Collection} whose elements to test for removal from
     *        this object.
     * @return {@code true} if this {@code Collection} was modified by this
     *         method, {@code false} otherwise.
     * @throws NullPointerException if the specified argument {@code c} is 
     *         {@code null}.
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        requireNonNullCollection(c);
        return batchRemove(c, false);
    }
    
    /**
     * Removes all elements that are <em>not</em> shared between this 
     * {@code Stack} and the given {@code Collection}. This operation may 
     * constitute a structural modification.
     * 
     * @param c The {@code Collection} whose elements to test for removal from
     *        this object.
     * @return {@code true} if this {@code Collection} was modified by this
     *         method, {@code false} otherwise.
     * @throws NullPointerException if the specified argument {@code c} is 
     *         {@code null}.
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        requireNonNullCollection(c);
        return batchRemove(c, true);
    }
    
    /**
     * Retains or removes all elements shared between this {@code Stack} and the
     * specified {@ocde Collection}, intended for use only by the 
     * {@link #removeAll(java.util.Collection)} and the 
     * {@link #retainAll(java.util.Collection)} methods.
     * 
     * @param c The {@code Collection} whose elements to test for removal from
     *        this object.
     * @param complement The state that defines whether to retain or remove the
     *        elements shared between this {@code Stack} and the given 
     *        {@code Collection} argument.
     * @return {@code true} if this {@code Collection} was modified by this
     *         method, {@code false} otherwise.
     * @throws NullPointerException if the specified argument {@code c} is 
     *         {@code null}.
     * @see #removeAll(java.util.Collection)
     * @see #retainAll(java.util.Collection)
     */
    private boolean batchRemove(Collection<?> c, boolean complement) {
        final Object[] elements = elementData;
        int i = 0, j = 0;
        boolean modified = false;
        try {
            for ( ; i < size; i++) {
                if (c.contains(elementData[i]) == complement) {
                    elements[j] = elements[i];
                    j++;
                }
            }
        }
        finally { // In the case that contains(Object) throws an exception
            if (i != size) {
                System.arraycopy(elements, i, elements, j, size - i);
                j += size - i;
            }
            if (j != size) {
                for (int k = j; k < size; k++) {
                    elements[k] = null;
                }
                modCount += size - j;
                size = j;
                modified = true;
            }
        }
        return modified;
    }
    
    /**
     * Saves and writes (serializes) this {@code Stack}.
     * 
     * @param s The output stream for this {@code Stack}.
     * @throws IOException if some error occurs while attempting to write the
     *         object.
     * @serialData The length of this object's {@link #elementData} and all its
     *             elements in the proper order.
     */
    private void writeObject(ObjectOutputStream s) throws IOException {
        int expectedModCount = modCount;
        
        // Writes the element count
        s.defaultWriteObject();
        
        // Writes out the size
        s.writeInt(size);
        
        // Write all elements in the order they appear in elementData
        for (int i = 0; i < size; i++) {
            s.writeObject(elementData[i]);
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }
    
    /**
     * Applies the given {@code Consumer} to each element in this {@code Stack}.
     * 
     * @param action The {@code Consumer} to accept each element within this
     *        {@code Collection}.
     * @throws NullPointerException if the specified {@code Consumer} argument
     *         is {@code null}.
     */
    @Override
    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        for (int i = 0; i < size; i++) {
            action.accept(elementData(i));
        }
    }
    
    /**
     * Sorts this {@code Stack} using a given {@code Comparator}. 
     * 
     * @param c The {@code Comparator} to use in the sorting process.
     * @throws NullPointerException if the specified argument {@code c} is 
     *         {@code null}.
     * @see Arrays#sort(java.lang.Object[]) 
     * @see Arrays#asList(java.lang.Object...) 
     */
    @Override
    @SuppressWarnings("unchecked")
    public void sort(Comparator<? super E> c) {
        Objects.requireNonNull(c);
        Arrays.sort(elementData, (Comparator) c);
    }
    
    /**
     * Generates and returns a {@code List} with the elements contained within 
     * this {@code Stack} from the specified range, ({@code fromIndex},
     * inclusive, {@code toIndex} exclusive). If {@code fromIndex} is equal to
     * {@code toIndex}, the returned list will be empty.
     * 
     * @param fromIndex The index to begin the sublist.
     * @param toIndex The index to end the sublist.
     * @throws IndexOutOfBoundsException if either {@code fromIndex} or 
     *         {@code toIndex} are out of range.
     * @return A sublist containing the elements within the specified range from
     *         this {@code Stack}.
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        rangeCheck(fromIndex, toIndex);
        return new SubList(0, fromIndex, toIndex);
    }
    
    /**
     * Provides for a sub-list implementation that underlies a {@code Stack}
     * object and specifies a range within a particular {@code Stack}'s data.
     * Instances of this class may only be obtained through the 
     * {@link #subList(int, int)} method.
     * 
     * @see #subList(int, int) Generates instances of this class.
     */
    private class SubList
        extends AbstractList<E> 
        implements RandomAccess
    {
        
        /**
         * Stores the offset, or the beginning index to use, when accessing 
         * elements in this {@code SubList}'s parent.
         */
        private final int parentOffset;
        
        /**
         * Stores the offset, or the beginning index to use, when accessing
         * elements within this {@code SubList}.
         */
        private final int offset;
        
        /**
         * Stores the size, or the amount of elements, in this {@code SubList}.
         */
        private int size;

        /**
         * Creates a new {@code SubList} with the specified arguments.
         * 
         * @param offset The original shift of the {@code SubList}.
         * @param fromIndex The beginning index of the {@code SubList} in 
         *                  relation to the underlying {@code Stack}
         *                  to use, inclusive.
         * @param toIndex The ending index of the {@code SubList} in relation to
         *                the underlying {@code Stack}, exclusive.
         */
        SubList(int offset, int fromIndex, int toIndex) {
            this.parentOffset = fromIndex;
            this.offset = offset + fromIndex;
            this.size = toIndex - fromIndex;
            this.modCount = Stack.this.modCount;
        }
        
        /**
         * Ensures that the given index argument is a valid, accessible index 
         * value within this object's {@link #elementData}.
         * 
         * @param index The index value or index values to test for validity.
         */
        private void rangeCheck(int... index) {
            for (int i = 0; i < index.length; i++) {
                if (index[i] < 0 || index[i] >= elementData.length) {
                    throw new IndexOutOfBoundsException(
                            outOfBoundsMessage(index[i]));
                }
            }
        }

        /**
         * Ensures that the given index argument is a valid, accessible index 
         * value within this object's {@link #elementData}. This method is 
         * intended for use only for the {@link #add(java.lang.Object)} or 
         * {@link #add(int, java.lang.Object)} operations.
         * 
         * @param index The index value or index values to test for validity.
         * @see #add(java.lang.Object)
         * @see #add(int, java.lang.Object)
         */
        private void addRangeCheck(int... index) {
            for (int i = 0; i < index.length; i++) {
                if (index[i] < 0 || index[i] > elementData.length) {
                    throw new IndexOutOfBoundsException(
                            outOfBoundsMessage(index[i]));
                }
            }
        }
        
        /**
         * Generates a standard error message intended for use as a detail for 
         * any thrown {@code IndexOutOfBoundsException} within this class.
         * 
         * @param index The invalid index to generate a message with.
         * @return A formatted {@code String} for use as a detail message for an
         *         {@code IndexOutOfBoundsException}.
         */
        private String outOfBoundsMessage(int index) {
            return "Index = " + index + ", Size = " + this.size;
        }
        
        /**
         * Checks this {@code SubLists}'s {@link modCount} against the
         * {@link #modCount} of the underlying {@code Stack}, throwing a 
         * {@code ConcurrentModificationException} if they are not equal.
         *
         * @throws ConcurrentModificationException if the expected
         *         modification count does not equal that of the underlying
         *         {@code Stack}.
         */
        private void comodificationCheck() {
            if (Stack.this.modCount != this.modCount) {
                throw new ConcurrentModificationException();
            }
        }

        /**
         * Adds a given element at the specified position in this {@code Stack}. 
         * This operation constitutes a structural modification.
         * 
         * @param index The index in to add the given element.
         * @param element The element to add.
         * @throws NullPointerException if the specified argument 
         *         {@code element} is {@code null}.
         */
        @Override
        public void add(int index, E element) {
            Objects.requireNonNull(element);
            addRangeCheck(index);
            comodificationCheck();
            
            Stack.this.add(parentOffset + index, element);
            this.modCount = Stack.this.modCount;
            this.size++;
        }

        /**
         * Replaces the element at the specified position in this {@code Stack} 
         * with the specified element. Returns the element previously at the 
         * specified index.
         *
         * @param index The index of the element to replace.
         * @param element The element to be stored at the specified index.
         * @return The element previously at the specified index.
         * @throws IndexOutOfBoundsException if the given index value is 
         *         invalid.
         * @throws NullPointerException if the specified argument 
         *         {@code element} is {@code null}.
         */
        @Override
        public E set(int index, E element) {
            Objects.requireNonNull(element);
            rangeCheck(index);
            comodificationCheck();
            
            E oldElement = Stack.this.elementData(offset + index);
            Stack.this.elementData[offset + index] = element;
            return oldElement;
        }

        /**
         * Returns the element at the specified position in this {@code Stack}.
         * 
         * @param index The index of the element to retrieve.
         * @return The element at the specified index.
         */
        @Override
        public E get(int index) {
            rangeCheck(index);
            comodificationCheck();
            
            return Stack.this.elementData(offset + index);
        }
        
        /**
         * Retrieves the integer size, or the total amount of elements contained
         * within this {@code SubList}.
         * 
         * @return The {@link #size instance variable of this {@code SubList}.
         * @see #size The value that is returned by this method. Stores the 
         *      amount of "physical" elements in this stack.
         */
        @Override
        public int size() {
            comodificationCheck();
            return this.size;
        }

        /**
         * Removes and returns the object in this {@code Stack} at the given 
         * index value. All elements succeeding the element at {@code i} are 
         * shifted left towards the {@code 0} index.
         * 
         * @param index The index of the object to remove.
         * @return The object that was removed from the given index.
         */
        @Override
        public E remove(int index) {
            rangeCheck(index);
            comodificationCheck();
            
            E removed = Stack.this.remove(parentOffset + index);
            this.modCount = Stack.this.modCount;
            this.size--;
            return removed;
        }
        
        /**
         * Removes from this stack's structure all elements within the given 
         * range, shifting all succeeding elements left towards the {@code 0} 
         * index. If {@code fromIndex} is equal to {@code toIndex}, this method 
         * has no effect.
         * 
         * @param fromIndex The index of the first element to be removed.
         * @param toIndex The index after the last element to be removed.
         */
        @Override
        protected void removeRange(int fromIndex, int toIndex) {
            if (fromIndex == toIndex) {
                return;
            }
            rangeCheck(fromIndex, toIndex);
            Stack.this.removeRange(parentOffset + fromIndex, 
                    parentOffset + toIndex);
            this.modCount = Stack.this.modCount;
            this.size -= toIndex - fromIndex;
        }

        /**
         * Inserts to this {@code Stack} all elements contained within the given 
         * {@code Collection} argument at the {@code size} index. This
         * operation constitutes a structural modification.
         * 
         * @param c The {@code Collection} containing the elements to to this 
         *          {@code Stack}.
         * @return {@code true} if the add operation was successful, 
         *         {@code false} otherwise.
         * @throws NullPointerException if the specified argument {@code c} is 
         *         {@code null}.
         */
        @Override
        public final boolean addAll(Collection<? extends E> c) {
            return addAll(this.size, c);
        }

        /**
         * Inserts to this {@code Stack} all elements contained within the given 
         * {@code Collection} argument at the specified index. All succeeding 
         * elements are shifted right, or away from the {@code 0} index. This
         * operation constitutes a structural modification.
         * 
         * @param index The index to begin inserting the elements from the 
         *        specified {@code Collection}.
         * @param c The {@code Collection} containing the elements to to 
         *        this {@code Stack}.
         * @return {@code true} if the add operation was successful, 
         *         {@code false} otherwise.
         * @throws NullPointerException if the specified argument {@code c} is 
         *         {@code null}.
         */
        @Override
        public final boolean addAll(int index, Collection<? extends E> c) {
            requireNonNullCollection(c);
            addRangeCheck(index);

            if (c.isEmpty()) {
                return false;
            }
            comodificationCheck();
            Stack.this.addAll(parentOffset + index, c);
            this.size += c.size();
            return true;
        }
        
        /**
         * Returns an {@code Iterator} over the elements contained in this
         * collection. This iterator is fail-fast, meaning that in the case of
         * any concurrent modification during iteration, it fails as soon as the
         * modification is detected, preventing any unexpected behavior.
         *
         * @return An iterator over the elements contained in this collection.
         */
        @Override
        public Iterator<E> iterator() {
            return listIterator();
        }

        /**
         * Returns an {@code ListIterator} over the elements contained in this
         * collection. This iterator is fail-fast, meaning that in the case of
         * any concurrent modification during iteration, it fails as soon as the
         * modification is detected, preventing any unexpected behavior.
         *
         * @param index The index to begin the {@code ListIterator} to return.
         * @return A {@code ListIterator} beginning at the specified index over
         *         the elements contained in this collection.
         */
        @Override
        public ListIterator<E> listIterator(int index) {
            comodificationCheck();
            rangeCheck(index);
            final int offset = this.offset;
            
            // Simple anonymous inner class for concise code for this iterator
            return new ListIterator<E>() {
            
                /**
                 * The index of the next object in this iterator, or the index 
                 * of the object that would be produced by the next call to
                 * {@link #next()}.
                 */
                int cursor = index;

                /**
                 * Index of element returned by most recent call to 
                 * {@link #next()}. If there is no such element or after any 
                 * calls to {@link #remove()}, this is set to {@code -1},
                 */
                int lastReturned = -1;

                /**
                 * Stores the expected modification count for this iterator. If 
                 * at any time this value does not equal 
                 * {@link AbstractList#modCount}, this iterator will throw an
                 * {@code ConcurrentModificationException} to prevent any 
                 * unexpected or unintended behavior.
                 */
                int expectedModCount = Stack.this.modCount;

                /**
                 * Returns {@code true} if this iteration can traverse backwards
                 * without causing any {@code NoSuchElementException} to be 
                 * thrown.
                 *
                 * @return {@code true} if this iterator can traverse backwards,
                 *         {@code false} otherwise.
                 */
                @Override
                public boolean hasPrevious() {
                    return cursor != 0;
                }

                /**
                 * Tests if there are more elements in this iteration, returning 
                 * {@code true} if {@link #next()} would return an element 
                 * rather than throwing an exception.
                 *
                 * @return {@code true} if the iteration has more elements, 
                 *         otherwise returns {@code false}.
                 */
                @Override
                public boolean hasNext() {
                    if (cursor != SubList.this.size) {
                        return true;
                    }
                    checkForComodification();
                    return false;
                }

                /**
                 * Returns the previous element in this iteration.
                 *
                 * @return The previous element in this iteration.
                 * @throws ConcurrentModificationException if the expected
                 *         modification count does not equal that of the 
                 *         underlying {@code AbstractList}.
                 */
                @Override
                public E previous() {
                    checkForComodification();
                    int i = cursor - 1;
                    if (i < 0 ) {
                        throw new NoSuchElementException("No remaining "
                                + "elements!");
                    }
                    Object[] elements = Stack.this.elementData;
                    if (offset + i  >= elements.length) {
                        throw new ConcurrentModificationException();
                    }
                    cursor = i;
                    lastReturned = i;
                    return Stack.this.elementData(offset + lastReturned);
                }

                /**
                 * Retrieves the next element in the iteration.
                 *
                 * @return The next element in the iteration.
                 * @throws ConcurrentModificationException if the expected 
                 *         modification count does not equal that of the 
                 *         underlying {@code AbstractList}.
                 * @throws NoSuchElementException if the iteration has no more 
                 *         elements.
                 */
                @Override
                public E next() {
                    checkForComodification();
                    int i = cursor;
                    if (i >= SubList.this.size) {
                        throw new NoSuchElementException("No remaining "
                                + "elements!");
                    }
                    Object[] elements = Stack.this.elementData;
                    if (offset + i  >= elements.length) {
                        throw new ConcurrentModificationException();
                    }
                    cursor = i + 1;
                    lastReturned = i;
                    return Stack.this.elementData(offset + lastReturned);
                }

                /**
                 * Returns the previous index of this iteration.
                 *
                 * @return The previous index of this iteration.
                 * @see #nextIndex
                 */
                @Override
                public int previousIndex() {
                    return cursor - 1;
                }

                /**
                 * Returns the next index of this iteration.
                 *
                 * @return The next index of this iteration.
                 * @see #nextIndex
                 */
                @Override
                public int nextIndex() {
                    return cursor;
                }

                /**
                 * Adds the given element to the underlying 
                 * {@code AbstractList}.
                 *
                 * @param element The element to add to the backing 
                 *        {@code AbstractList}.
                 * @throws ConcurrentModificationException if the expected
                 *         modification count does not equal that of the 
                 *         underlying {@code Stack}.
                 * @throws NullPointerException if the specified argument is
                 *         {@code null}.
                 */
                @Override
                public void add(E element) {
                    Objects.requireNonNull(element);
                    checkForComodification();

                    int index = cursor;
                    Stack.this.add(index, element);
                    lastReturned = -1;
                    cursor = index + 1;
                    expectedModCount = Stack.this.modCount;
                }

                /**
                 * Sets the given element to the underlying {@code AbstractList}
                 * at the current cursor index.
                 *
                 * @param element The element to set at the specified index in 
                 *        the backing {@code AbstractList}.
                 * @throws ConcurrentModificationException if the expected
                 *         modification count does not equal that of the 
                 *         underlying {@code AbstractList}.
                 * @throws IllegalStateException if the {@link #next()} method 
                 *         has not previously been called or the 
                 *         {@link #remove()} method has already been called 
                 *         after the last call to the {@link #next()} method.
                 * @throws NullPointerException if the specified argument is
                 *         {@code null}.
                 */
                @Override
                public void set(E element) {
                    if (lastReturned < 0) {
                        throw new IllegalStateException("No current index!");
                    }
                    Objects.requireNonNull(element);
                    checkForComodification();

                    Stack.this.set(lastReturned, element);
                    expectedModCount = Stack.this.modCount;
                }

                /**
                 * Removes from the underlying {@code AbstractList} the last 
                 * element returned by this iterator. This method can be called 
                 * only once per call to {@link #next()}. The behavior of an 
                 * iterator is unspecified if the underlying collection is 
                 * modified while the iteration is in progress in any way other 
                 * than by calling this method.
                 * 
                 * @throws ConcurrentModificationException if the expected 
                 *         modification count does not equal that of the 
                 *         underlying {@code AbstractList}.
                 * @throws IllegalStateException if the {@link #next()} method 
                 *         has not previously been called or the 
                 *         {@link #remove()} method has already been called 
                 *         after the last call to the {@link #next()} method.
                 */
                @Override
                public void remove() {
                    checkForComodification();
                    if (lastReturned != -1) {
                        Stack.this.internalRemove(lastReturned);
                        if (lastReturned < cursor) {
                            cursor--;
                        }
                        lastReturned = -1;
                        expectedModCount = Stack.this.modCount;
                    }
                    else {
                        throw new IllegalStateException("No previous call to "
                                + "remove()");
                    }
                }

                /**
                 * Checks this iterator's {@link #expectedModCount} against the
                 * {@link #modCount} of the underlying {@code AbstractList}, 
                 * throwing a {@code ConcurrentModificationException} if they 
                 * are not equal.
                 *
                 * @throws ConcurrentModificationException if the expected
                 * modification count does not equal that of the underlying
                 * {@code AbstractList}.
                 */
                void checkForComodification() {
                    if (Stack.this.modCount != expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                }
            };
            
        }

    }
    
    /**
     * Returns an array containing all elements within this {@code Stack} in 
     * their appropriate, natural order.
     * 
     * @return An array containing the elements in this {@code Collection}.
     */
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elementData, elementData.length);
    }
    
    /**
     * Returns an {@code Iterator} over the elements contained in this 
     * collection. This iterator is fail-fast, meaning that in the case of any
     * concurrent modification during iteration, it fails as soon as the
     * modification is detected, preventing any unexpected behavior.
     *
     * @return An iterator over the elements contained in this 
     *         collection.
     */
    @Override
    public Iterator<E> iterator() {
        return new StackIterator();
    }

    /**
     * Returns a {@code ListIterator} over the elements contained in this 
     * collection. This iterator is fail-fast, meaning that in the case of any
     * concurrent modification during iteration, it fails as soon as the
     * modification is detected, preventing any unexpected behavior.
     *
     * @return A {@code ListIterator} over the elements contained in 
     *         this collection.
     */
    @Override
    public ListIterator<E> listIterator() {
        return new StackListIterator();
    }

    /**
     * Returns an {@code ListIterator} over the elements contained in this 
     * collection. This iterator is fail-fast, meaning that in the case of any
     * concurrent modification during iteration, it fails as soon as the
     * modification is detected, preventing any unexpected behavior.
     *
     * @param index The index to begin the {@code ListIterator} to return.
     * @return A {@code ListIterator} beginning at the specified index over the 
     *         elements contained in this collection.
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        rangeCheck(index);
        
        return new StackListIterator(index);
    }
    
    /**
     * The iterator for this {@code Stack}, contains implementation of all
     * imposed methods. Instances are retrieved using the 
     * {@link Stack#iterator()} method.
     * 
     * @see Stack#iterator()
     * @see java.util.Iterator
     */
    private class StackIterator 
        implements Iterator<E> 
    {
        
        /**
         * The index of the next object in this iterator, or the index of the
         * object that would be produced by the next call to {@link #next()}.
         */
        int cursor;

        /**
         * Index of element returned by most recent call to {@link #next()}. If
         * there is no such element or after any calls to {@link #remove()}, 
         * this is set to {@code -1}.
         */
        int lastReturned;

        /**
         * Stores the expected modification count for this iterator. If at any
         * time this value does not equal {@link AbstractList#modCount}, this
         * iterator will throw an {@code ConcurrentModificationException} to
         * prevent any unexpected or unintended behavior.
         */
        int expectedModCount;
        
        /**
         * Initializes a new {@code StackIterator} with the default values, 
         * using the current {@link AbstractList#modCount} as the expected
         * modification count.
         */
        StackIterator() {
            cursor = 0;
            lastReturned = -1;
            expectedModCount = modCount;
        }
        
        /**
         * Tests if there are more elements in this iteration, returning 
         * {@code true} if {@link #next()} would return an element rather than 
         * throwing an exception.
         *
         * @return {@code true} if the iteration has more elements, otherwise 
         *         returns {@code false}.
         */
        @Override
        public boolean hasNext() {
            if (cursor != Stack.this.size) {
                return true;
            }
            checkForComodification();
            return false;
        }

        /**
         * Retrieves the next element in the iteration.
         *
         * @return The next element in the iteration.
         * @throws ConcurrentModificationException if the expected modification
         *         count does not equal that of the underlying {@code Stack}.
         * @throws NoSuchElementException if the iteration has no more elements.
         */
        @Override
        public E next() {
            checkForComodification();
            if (hasNext()) {
                int index = cursor;
                lastReturned = index;
                cursor = index + 1;
                return Stack.this.elementData(index);
            }
            else {
                throw new NoSuchElementException("No remaining elements!");
            }
        }
        
        /**
         * Removes from the underlying {@code Stack} the last element returned 
         * by this iterator. This method can be called only once per call to 
         * {@link #next()}. The behavior of an iterator is unspecified if the 
         * underlying collection is modified while the iteration is in progress 
         * in any way other than by calling this method.
         * 
         * @throws ConcurrentModificationException if the expected modification
         *         count does not equal that of the underlying {@code Stack}.
         * @throws IllegalStateException if the {@link #next()} method has not 
         *         previously been called or the {@link #remove()} method has 
         *         already been called after the last call to the 
         *         {@link #next()} method.
         */
        @Override
        public void remove() {
            checkForComodification();
            if (lastReturned != -1) {
                Stack.this.internalRemove(lastReturned); // Increments modCount
                if (lastReturned < cursor) {
                    cursor--;
                }
                lastReturned = -1;
                expectedModCount = Stack.this.modCount;
            }
            else {
                throw new IllegalStateException("No previous call to remove()");
            }
        }

        /**
         * Checks this iterator's {@link #expectedModCount} against the 
         * {@link #modCount} of the underlying {@code Stack}, throwing a
         * {@code ConcurrentModificationException} if they are not equal.
         * 
         * @throws ConcurrentModificationException if the expected modification
         *         count does not equal that of the underlying {@code Stack}.
         */
        void checkForComodification() {
            if (Stack.this.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

    }
    
    /**
     * The iterator for this {@code Collection}, contains implementation of all
     * imposed methods. Instances are retrieved using the 
     * {@link Stack#listIterator()} and {@link Stack#listIterator(int)} methods.
     * 
     * @see Stack#listIterator()
     * @see Stack#listIterator(int)
     * @see java.util.ListIterator
     */
    private class StackListIterator 
        extends StackIterator
        implements ListIterator<E> 
    {
        
        /**
         * Initializes a new {@code StackListIterator} with the default values, 
         * using the current {@link AbstractList#modCount} as the expected
         * modification count.
         */
        StackListIterator() {
            this(0);
        }
        
        /**
         * Initializes a new {@code StackListIterator} with the given starting 
         * index value and uses the current {@link AbstractList#modCount} as the
         * expected modification count.
         * 
         * @param index The index to begin the iterator.
         */
        StackListIterator(int index) {
            super();
            cursor = index;
        }
        
        /**
         * Returns {@code true} if this iteration can traverse backwards without
         * causing any {@code NoSuchElementException} to be thrown.
         * 
         * @return {@code true} if this iterator can traverse backwards, 
         *         {@code false} otherwise.
         */
        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }
        
        /**
         * Returns the previous element in this iteration.
         * 
         * @return The previous element in this iteration.
         * @throws ConcurrentModificationException if the expected modification
         *         count does not equal that of the underlying {@code Stack}.
         */
        @Override
        public E previous() {
            checkForComodification();
            if (hasPrevious()) {
                int index = cursor - 1;
                lastReturned = index;
                cursor = index;
                return Stack.this.elementData(index);
            }
            else {
                throw new NoSuchElementException("No remaining elements!");
            }
        }
        
        /**
         * Returns the previous index of this iteration.
         * 
         * @return The previous index of this iteration.
         * @see #nextIndex()
         */
        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        /**
         * Returns the next index of this iteration.
         * 
         * @return The next index of this iteration.
         * @see #previousIndex() 
         */
        @Override
        public int nextIndex() {
            return cursor;
        }
        
        /**
         * Adds the given element to the underlying {@code Stack}.
         * 
         * @param element The element to add to the backing {@code Stack}.
         * @throws ConcurrentModificationException if the expected modification
         *         count does not equal that of the underlying {@code Stack}.
         * @throws NullPointerException if the specified argument is 
         *         {@code null}.
         */
        @Override
        public void add(E element) {
            Objects.requireNonNull(element);
            checkForComodification();

            int index = cursor;
            Stack.this.add(index, element); // Increments modCount
            lastReturned = -1;
            cursor = index + 1;
            expectedModCount = Stack.this.modCount;
        }

        /**
         * Sets the given element to the underlying {@code Stack} at the current
         * cursor index.
         * 
         * @param element The element to set at the specified index in the 
         *        backing {@code Stack}.
         * @throws ConcurrentModificationException if the expected modification
         *         count does not equal that of the underlying {@code Stack}.
         * @throws IllegalStateException if the {@link #next()} method has not 
         *         previously been called or the {@link #remove()} method has 
         *         already been called after the last call to the 
         *         {@link #next()} method.
         * @throws NullPointerException if the specified argument is 
         *         {@code null}.
         */
        @Override
        public void set(E element) {
            if (lastReturned < 0) {
                throw new IllegalStateException("No current index!");
            }
            Objects.requireNonNull(element);
            checkForComodification();
            
            Stack.this.set(lastReturned, element);
            expectedModCount = Stack.this.modCount;
        }
        
    }
    
    /**
     * This implementation returns a "shallow" copy of this {@code Stack}. This 
     * method copies, but does not clone, all elements in this collection.
     * 
     * @return A shallow copy of this {@code Stack}.
     * @throws InternalError if the caller object is not of type {@code Stack}.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object clone() {
        try {
            Stack<E> clone = (Stack<E>) super.clone();
            clone.modCount = this.modCount;
            clone.size = this.size;
            clone.elementData = new Object[this.elementData.length];
            
            // Initialize the clone with the elements in this object
            System.arraycopy(this.elementData, 0, clone.elementData, 0, 
                             this.elementData.length);
            return clone;
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError("Invalid object called clone()", e);
        }
    }
    
    /**
     * Compares the specified object with this {@code Stack} for equality. 
     * Returns {@code true} if the given argument is an instance of {@code List}
     * and has the same size as this collection. In this implementation, two
     * independent lists are equal if they contain the same elements in the same
     * order.
     * 
     * @param other The object to test against this one for equality.
     * @return {@code true} if the given argument has the same size and the same
     *         elements (in the same order) as this {@code Stack}, otherwise 
     *         {@code false}.
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        if (other == null || !(other instanceof List)) {
            return false;
        }
        if (other == this) {
            return true;
        }
        final List<?> theSecond = (List<?>) other;
        for (int i = 0; i < size; i++) {
            Object o1 = elementData[i];
            Object o2 = theSecond.get(i);
            if (!o1.equals(o2)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns a {@code String} representation of this {@code Stack}, delimiting
     * each element in this collection with a single comma ({@code ,}) and the
     * entire collection with square-brackets ({@code []}). Any occurrences of 
     * this object within its own structure will result in an element 
     * represented by {@code "(this Stack)"} to be appended.
     * 
     * @return A {@code String} representation of this {@code Stack}'s data
     *         structure.
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder().append('[');
        for (int i = 0; i < size; i++) {
            Object next = elementData[i];
            sb.append((next == this)
                    ? "(this Stack)" 
                    : next);
            sb.append((i >= size - 1)
                    ? ']' 
                    : ", ");
        }
        return sb.toString();
    }
    
}