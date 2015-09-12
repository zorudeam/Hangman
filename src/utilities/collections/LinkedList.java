/*
 * A fancy license header.
 */
package utilities.collections;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * The {@code LinkedList} class contains an implementation of a 
 * non-circular, sequential, doubly-linked list structure that 
 * implements all imposed methods from {@code List} and {@code Deque}. 
 * All elements, including those with {@code null} reference, <em>are</em> 
 * permitted in this structure.
 * 
 * <p> The iterators provided by this class are fail-fast. In the case that the
 * the underlying linked list is structurally modified in any way other than the
 * iterator's {@code remove} and {@code add} methods, a 
 * {@code ConcurrentModificationException} would be thrown.
 * 
 * <p> This implementation is <em>not</em> synchronized. In order to achieve 
 * proper concurrent operation, instances of this class should be synchronized 
 * on some object that naturally encapsulates the list. If no such object 
 * exists, the list should be "wrapped" using the
 * {@link java.util.Collections#synchronizedList(java.util.List)} method using
 * the following idiom:
 * 
 * <blockquote><pre>
 * List list = {@link java.util.Collections#synchronizedList(java.util.List)
 * Collections.synchronizedList(new LinkedList(...));}</pre>
 * </blockquote>
 * 
 * @param <E> The type of object to store within this {@code LinkedList}.
 * @author Oliver Abdulrahim
 */
public class LinkedList<E>
    extends AbstractSequentialList<E>
    implements Deque<E>, Cloneable, Serializable 
{
    
    /**
     * The serial version ID of the {@code LinkedList} class.
     */
    private static final long serialVersionUID = -4960013190757023220L;

    /**
     * Points to the first node in the structure.
     */
    private transient Node<E> first;
    
    /**
     * Points to the last node in the structure.
     */
    private transient Node<E> last;
    
    /**
     * A doubly-linked node implementation for this list.
     * 
     * @param <E> The type of object stored within a {@code Node}.
     */
    private static class Node<E> {
        
        /**
         * The data stored by this {@code Node}.
         */
        E item;
        
        /**
         * The node that precedes this one in the linked structure. If 
         * this refers to {@code null}, then this node is the first in the list.
         */
        Node<E> previousLink;
        
        /**
         * The node that succeeds this one in the linked structure. If 
         * this refers to {@code null}, then this node is the last in the list.
         */
        Node<E> nextLink;
        
        /**
         * Constructs a {@code Node} with the given arguments.
         * 
         * @param item The data for this node.
         * @param previousLink The link preceding this one.
         * @param nextLink The link succeeding this one.
         */
        Node(E item, Node<E> previousLink, Node<E> nextLink) {
            this.item = item;
            this.previousLink = previousLink;
            this.nextLink = nextLink;
        }
        
    }
    
    /**
     * Stores the amount of elements currently contained within this 
     * {@code LinkedList}.
     * 
     * @see #size() 
     */
    private transient int size;
    
    /**
     * Creates a new, empty {@code LinkedList} with no elements and size 
     * {@code 0}.
     */
    public LinkedList() {
        first = null;
        last = null;
        size = 0;
    }
    
    /**
     * Constructs a new {@code LinkedList}, adding all elements from the 
     * specified {@code Collection} to this object's data.
     * 
     * @param c The {@code Collection} to load elements from. This must contain
     *        elements which are lower-bounded objects of type {@code E}.
     * @throws NullPointerException if the specified argument {@code c} is 
     *         {@code null}.
     */
    public LinkedList(Collection<? extends E> c) {
        Objects.requireNonNull(c, "Invalid null Collection!");
        addAll(c); // Increments modCount
    }
    
    /**
     * Generates a standard error message intended for use as a detail 
     * for any thrown {@code IndexOutOfBoundsException} within this class.
     * 
     * @param index The invalid index to generate a message with.
     * @return A formatted {@code String} for use as a detail message for an 
     *         {@code IndexOutOfBoundsException}.
     */
    private String outOfBoundsMessage(int index) {
        // assert index < 0 && index >= size : "What are you doing, man?";
        return "Index = " + index + ", Size = " + size;
    }
    
    /**
     * Checks if a given argument is the index value of an existing element
     * within this list.
     * 
     * @param index The index value to test for validity.
     * @return {@code true} if the given index is a valid index value for an
     *         element in the data of this list, {@code false} otherwise.
     */
    private boolean isElementIndex(int index) {
        return index >=0 || index >= size;
    }
    
    /**
     * Checks if a given argument is a valid index value for an add operation 
     * within this list.
     * 
     * @param index The index value to test for validity.
     * @return {@code true} if the given index is a valid index value for an
     *         element in the data of this list, {@code false} otherwise.
     */
    private boolean isPositionIndex(int index) {
        return index >= 0 || index > size;
    }
    
    /**
     * Checks if a given argument is a valid index value for an existing element
     * in the data of this list. 
     * 
     * @param index The index value or index values to test for validity.
     * @throws IndexOutOfBoundsException if the specified index value is out of 
     *         range.
     */
    private void rangeCheck(int index) {
        if (!isElementIndex(index)) {
            throw new IndexOutOfBoundsException(outOfBoundsMessage(index));
        }
    }
    
    /**
     * Checks if a given argument is a valid index value for an existing element
     * in the data of this list.
     * 
     * @param index The index value or index values to test for validity.
     * @throws IndexOutOfBoundsException if the specified index value is out of 
     *         range.
     */
    private void rangeCheckForAdd(int index) {
        if (!isPositionIndex(index)) {
            throw new IndexOutOfBoundsException(outOfBoundsMessage(index));
        }
    }

    /**
     * Returns the {@code Node} at the given index. This method traverses the
     * list in forward order if the given index is less than half the size.
     * Otherwise, the list is traversed in backwards order.
     * 
     * @param index The index of the {@code Node} to return.
     * @return The {@code Node} at the given index.
     */
    private Node<E> nodeAt(int index) {
        Node<E> n;
        if (index < (size / 2)) {
            n = first;
            for (int i = 0; i < index; i++) {
                n = n.nextLink;
            }
            return n;
        }
        else {
            n = last;
            for (int i = size - 1; i > index; i--) {
                n = n.previousLink;
            }
            return n;
        }
    }
        
    /**
     * Links the given element as the first link in the structure. In the case
     * that the old first element is {@code null}, the first and last link are
     * made to refer to the same link.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @param element The data for the new first link of the structure.
     */
    private void linkFirst(E element) {
        final Node<E> oldFirst = first;
        final Node<E> newFirst = new Node<>(element, null, first);
        first = newFirst;
        if (oldFirst == null) {
            last = newFirst;
        }
        else {
            oldFirst.previousLink = newFirst;
        }
        size++;
        modCount++;
    }
    
    /**
     * Links the given element as the last link in the structure. In the case
     * that the old last element is {@code null}, the first and last link are
     * made to refer to the same link.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @param element The data for the new last link of the structure.
     */
    private void linkLast(E element) {
        final Node<E> oldLast = last;
        final Node<E> newLast = new Node<>(element, last, null);
        last = newLast;
        if (oldLast == null) {
            first = newLast;
        }
        else {
            oldLast.nextLink = newLast;
        }
        size++;
        modCount++;
    }
    
    /**
     * Links the given element directly before the first non-{@code null} link
     * preceding {@code succeeding}.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @param element The data for the new node to link before the given 
     *                element.
     * @param succeeding The node to link a new node before. This method assumes
     *                   non-nullity this given object.
     */
    private void linkBefore(E element, Node<E> succeeding) {
        final Node<E> previous = succeeding.previousLink;
        final Node<E> newNode = new Node<>(element, previous, succeeding);
        if (previous == null) {
            first = newNode;
        }
        else {
            previous.nextLink = newNode;
        }
        size++;
        modCount++;
    }
    
    /**
     * Unlinks the first node in the structure, returning the data that was
     * previously contained by it.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @param f The first node to remove from this structure.
     * @return The data previously contained by the given node.
     */
    private E unlinkFirst(Node<E> f) {
        final E element = f.item;
        final Node<E> next = f.nextLink;
        f.item = null;
        f.nextLink = null;
        first = next;
        if (next == null) {
            last = null;
        }
        else {
            next.previousLink = null;
        }
        size--;
        modCount++;
        return element;
    }
    
    /**
     * Unlinks the last node in the structure, returning the data that was
     * previously contained by it.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @param l The last node to remove from this structure.
     * @return The data previously contained by the given node.
     */
    private E unlinkLast(Node<E> l) {
        final E element = l.item;
        final Node<E> previous = l.previousLink;
        l.item = null;
        l.previousLink = null;
        last = previous;
        if (previous == null) {
            first = null;
        }
        else {
            previous.nextLink = null;
        }
        size--;
        modCount++;
        return element;
    }
    
    /**
     * Unlinks, or removes, a given {@code Node} from this LinkedList's 
     * structure.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @param n The node to unlink.
     * @return The data previously contained by the given node.
     */
    private E unlink(Node<E> n) {
        final E element = n.item;
        final Node<E> previous = n.previousLink;
        final Node<E> next = n.nextLink;
        if (previous == null) {
            first = next;
        }
        else {
            previous.nextLink = next;
            n.previousLink = null;
        }
        if (next == null) {
            last = previous;
        }
        else {
            next.previousLink = previous;
            n.nextLink = null;
        }
        n.item = null;
        size--;
        modCount++;
        return element;
    }
    
    /**
     * Returns the first element contained within this {@code LinkedList}.
     * 
     * @return The first element in this list.
     * @throws NoSuchElementException if the list has no first element. This
     *         only occurs if the list is empty (in other words, 
     *         {@code isEmpty()} is {@code true}.
     */
    @Override
    public E getFirst() {
        final Node<E> first = this.first;
        if (first == null) {
            throw new NoSuchElementException("The list is empty!");
        }
        return first.item;
    }
    
    /**
     * Returns the last element contained within this {@code LinkedList}.
     * 
     * @return The last element in this list.
     * @throws NoSuchElementException if the list has no last element. This
     *         only occurs if the list is empty (in other words, 
     *         {@code isEmpty()} is {@code true}.
     */
    @Override
    public E getLast() {
        final Node<E> last = this.last;
        if (last == null) {
            throw new NoSuchElementException("The list is empty!");
        }
        return last.item;
    }
    
    /**
     * Removes and returns the last element contained within this 
     * {@code LinkedList}.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @return The data contained by the element previously contained by the
     *         first element in the list.
     * @throws NoSuchElementException if the list has no first element. This 
     *         only occurs if the list is empty (in other words, 
     *         {@code isEmpty()} is {@code true}.
     */
    @Override
    public E removeFirst() {
        final Node<E> first = this.first;
        if (first == null) {
            throw new NoSuchElementException("The list is empty!");
        }
        return unlinkFirst(first); // Increments modCount
    }
    
    /**
     * Removes and returns the last element contained within this 
     * {@code LinkedList}.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @return The data contained by the element previously contained by the
     *         first element in the list.
     * @throws NoSuchElementException if the list has no first element. This 
     *         only occurs if the list is empty (in other words, 
     *         {@code isEmpty()} is {@code true}.
     */
    @Override
    public E removeLast() {
        final Node<E> last = this.last;
        if (last == null) {
            throw new NoSuchElementException("The list is empty!");
        }
        return unlinkLast(last); // Increments modCount
    }

    /**
     * Adds the given element at the beginning of this {@code LinkedList}.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @param element The element to add at the beginning of this list.
     */
    @Override
    public void addFirst(E element) {
        linkFirst(element); // Increments modCount
    }
    
    /**
     * Adds the given element at the end of this {@code LinkedList}.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @param element The element to add at the end of this list.
     */
    @Override
    public void addLast(E element) {
        linkLast(element); // Increments modCount
    }
    
    /**
     * Searches this {@code LinkedList}'s elements for a given element. If this 
     * list contains the specified element, returns {@code true}, otherwise 
     * returns {@code false}.
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
     * Retrieves the integer size, or the total amount of elements contained
     * within this {@code LinkedList}.
     * 
     * @return The {@link #size} of this {@code LinkedList}.
     * @see #size The instance variable that is returned by this method.
     */
    @Override
    public int size() {
        return size;
    }
    
    /**
     * Adds the given element at the end of this {@code LinkedList}. Calls to 
     * this method are identical to {@link #addLast(java.lang.Object)}.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @param element The element to append at the end of this list.
     * @return {@code true} if the element was added (always returns 
     *         {@code true} if the operation does not cause any exceptions to 
     *         occur.
     */
    @Override
    public boolean add(E element) {
        linkLast(element); // Increments modCount
        return true;
    }
    
    /**
     * Removes the first occurrence of the given {@code Object} from this 
     * {@code LinkedList}, if such an element exists. This method begins at the 
     * {@code 0} index and iterates upward. Returns {@code true} if the given 
     * object was removed from this list. In the case that the given element is
     * {@code null}, this method will remove all occurrences of the {@code null}
     * reference.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @param o The object attempt to remove.
     * @return {@code true} if the given object was removed from this 
     *         {@code LinkedList}'s data.
     */
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (Node<E> n = first; n != null; n = n.nextLink) {
                if (n.item == null) {
                    unlink(n); // Increments modCount
                    return true;
                }
            }
        }
        else {
            for (Node<E> n = first; n != null; n = n.nextLink) {
                if (o.equals(n.item)) {
                    unlink(n); // Increments modCount
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Inserts into this {@code LinkedList} all elements contained within the 
     * given {@code Collection} argument at the {@code size} index. This 
     * operation uses the iterator of the given collection, therefore, care 
     * should be taken when inserting elements from a structure that uses an
     * ordering system. Finally, should the given collection be modified during
     * the run of this method, the resulting behavior is undefined.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @param c The {@code Collection} containing the elements to to this 
     *          {@code LinkedList}.
     * @return {@code true} if the add operation was successful, {@code false} 
     *         otherwise.
     * @throws NullPointerException if the specified argument {@code c} is 
     *         {@code null}.
     */
    @Override
    public final boolean addAll(Collection<? extends E> c) {
        return addAll(size, c); // Increments modCount
    }
    
    /**
     * Inserts into this {@code LinkedList} all elements contained within the 
     * given {@code Collection} argument at the given index. This operation uses
     * the iterator of the given collection, therefore, care should be taken 
     * if the order of the elements that are being inserted must be preserved. 
     * Finally, should the given collection be modified during the run of this 
     * method, the resulting behavior is undefined.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @param index The index 
     * @param c The {@code Collection} containing the elements to to this 
     *          {@code LinkedList}.
     * @return {@code true} if the add operation was successful, {@code false} 
     *         otherwise.
     * @throws NullPointerException if the specified argument {@code c} is 
     *         {@code null}.
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean addAll(int index, Collection<? extends E> c) {
        Objects.requireNonNull(c, "Invalid null Collection!");
        rangeCheckForAdd(index);
        
        Object[] elements = c.toArray();
        int newElementCount = elements.length;
        if (newElementCount == 0) {
            return false;
        }
        
        Node<E> previous, succeeding;
        if (index == size) {
            succeeding = null;
            previous = last;
        }
        else {
            succeeding = nodeAt(index);
            previous = succeeding.previousLink;
        }
        
        for (Object obj : elements) {
            E element = (E) obj;
            Node<E> newNode = new Node<>(element, previous, null);
            if (previous == null) {
                first = newNode;
            }
            else {
                previous.nextLink = newNode;
            }
            previous = newNode;
        }
        
        if (succeeding == null) {
            last = previous;
        }
        else {
            previous.nextLink = succeeding;
            succeeding.previousLink = previous;
        }
        
        size += newElementCount;
        modCount++;
        return true;
    }
    
    /**
     * Removes all elements contained within this {@code LinkedList}. After this
     * method call, {@link #isEmpty()} should always return {@code true}. 
     * 
     * <p> This operation constitutes a structural modification.
     */
    @Override
    public void clear() {
        Node<E> next;
        for (Node<E> n = first; n != null; n = next) {
            next = n.nextLink;
            n.item = null;
            n.previousLink = null;
            n.nextLink = null;
        }
        first = null;
        last = null;
        size = 0;
        modCount++;
    }
    
    /**
     * Returns the element at the specified position in this {@code LinkedList}.
     * 
     * @param index The index of the element to retrieve.
     * @return The element at the specified index.
     * @throws IndexOutOfBoundsException if the specified index value is out of
     *         range.
     */
    @Override
    public E get(int index) {
        rangeCheck(index);
        return nodeAt(index).item;
    }

    /**
     * Replaces the element at the specified position in this {@code LinkedList}
     * with the specified element. Returns the element previously at the 
     * specified index.
     *
     * @param index The index of the element to replace.
     * @param element The element to be stored at the specified index.
     * @return The element previously at the specified index.
     * @throws IndexOutOfBoundsException if the specified index value is out of
     *         range.
     */
    @Override
    public E set(int index, E element) {
        rangeCheck(index);
        Node<E> n = nodeAt(index);
        E oldItem = n.item;
        n.item = element;
        return oldItem;
    }
    
    /**
     * Adds a given element at the specified position in this 
     * {@code LinkedList}, shifting the element previously at the given index
     * and all succeeding elements to the right towards the {@code size - 1} 
     * index.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @param index The index in to add the given element.
     * @param element The element to add.
     * @throws IndexOutOfBoundsException if the specified index value is out of
     *         range.
     */
    @Override
    public void add(int index, E element) {
        rangeCheckForAdd(index);
        if (index == size) {
            linkLast(element); // Increments modCount
        }
        else {
            linkBefore(element, nodeAt(index)); // Increments modCount
        }
    }
    
    /**
     * Removes and returns the object in this {@code LinkedList} at the given 
     * index value. All elements succeeding the element at the given index are 
     * shifted left towards the {@code 0} index. 
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @param index The index of the object to remove.
     * @return The object that was removed from the given index.
     * @throws IndexOutOfBoundsException if the specified index value is out of
     *         range.
     */
    @Override
    public E remove(int index) {
        rangeCheck(index);
        return unlink(nodeAt(index)); // Increments modCount
    }

    /**
     * Returns the first index of the given {@code Object} argument, or 
     * {@code -1} if it does not occur within this {@code LinkedList}. This 
     * method traverses this list's structure in <em>forward</em> order, 
     * starting at the {@code 0} index and iterating increasingly to the 
     * {@code size - 1} index.
     * 
     * @param o The {@code Object} to search for within this {@code LinkedList}.
     * @return The last index value of the object argument, or {@code -1} if it
     *         does not occur.
     */
    @Override
    public int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            for (Node<E> n = first; n != null; n = n.nextLink, index++) {
                if (n.item == null) {
                    return index;
                }
            }
        }
        else {
            for (Node<E> n = first; n != null; n = n.nextLink, index++) {
                if (o.equals(n.item)) {
                    return index;
                }
            }
        }
        return -1;
    }
    
    /**
     * Returns the last index of the given {@code Object} argument, or 
     * {@code -1} if it does not occur within this collection. This method
     * traverses this list's structure in <em>reverse</em> order, starting at 
     * the {@code size - 1} index and iterating decreasingly to {@code 0}.
     * 
     * @param o The {@code Object} to search for within this {@code LinkedList}.
     * @return The last index value of the object argument, or {@code -1} if it
     *         does not occur.
     */
    @Override
    public int lastIndexOf(Object o) {
        int index = size;
        if (o == null) {
            for (Node<E> n = last; n != null; n = n.previousLink) {
                index--;
                if (n.item == null) {
                    return index;
                }
            }
        }
        else {
            for (Node<E> n = last; n != null; n = n.previousLink) {
                index--;
                if (o.equals(n.item)) {
                    return index;
                }
            }
        }
        return -1;
    }
    
    /**
     * Retrieves, but does not remove, the head of this {@code LinkedList}, 
     * which is the first and newest element. 
     * 
     * <p> This method behaves in the same way as {@link #element()}, except 
     * that it does not throw an exception in the case that the list is empty;
     * it instead, in such a case, returns {@code null}.
     * 
     * @return The element at the head of this list. If the list is empty 
     *         ({@code isEmpty()} is {@code true}), returns {@code null}.
     */
    @Override
    public E peek() {
        final Node<E> first = this.first;
        return (first == null)
                ? null
                : first.item;
    }

    /**
     * Retrieves, but does not remove, the head of this {@code LinkedList},
     * which is the first and newest element.
     * 
     * @return The element at the head of this list.
     * @throws NoSuchElementException if this {@code LinkedList} is empty.
     */
    @Override
    public E element() {
        return getFirst();
    }
    
    /**
     * Retrieves and removes the head of this {@code LinkedList}, which is the
     * first and newest element in the structure.
     * 
     * <p> This method behaves in the same way as {@link #remove()}, except 
     * that it does not throw an exception in the case that the list is empty;
     * it instead, in such a case, returns {@code null}.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @return The element at the head of this list. If the list is empty 
     *         ({@code isEmpty()} is {@code true}), returns {@code null}.
     */
    @Override
    public E poll() {
        final Node<E> first = this.first;
        return (first == null)
                ? null
                : unlinkFirst(first); // Increments modCount
    }
    
    /**
     * Retrieves and removes the head of this {@code LinkedList}, which is the
     * first and newest element in the structure.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @return The element at the head of this list.
     * @throws NoSuchElementException if this {@code LinkedList} is empty.
     */
    @Override
    public E remove() {
        return removeFirst(); // Increments modCount
    }
    
    /**
     * Inserts the specified element to the end of this {@code LinkedList}.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @param element The element to add to the tail of this list.
     * @return {@code true} if the element was added to this list.
     */
    @Override
    public boolean offer(E element) {
        return add(element); // Increments modCount
    }
    
    /**
     * Inserts the specified element to the front of this {@code LinkedList}.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @param element The element to add to the front of this list.
     * @return {@code true} if the element was added to this list.
     */
    @Override
    public boolean offerFirst(E element) {
        addFirst(element); // Increments modCount
        return true;
    }
    
    /**
     * Inserts the specified element to the end of this {@code LinkedList}.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @param element The element to add to the tail of this list.
     * @return {@code true} if the element was added to this list.
     */
    @Override
    public boolean offerLast(E element) {
        addLast(element); // Increments modCount
        return true;
    }
    
    /**
     * Retrieves, but does not remove, the head of this {@code LinkedList}, 
     * which is the first and newest element. If this list is empty, returns 
     * {@code null}.
     * 
     * <p> This method behaves in the same way as {@link #element()}, except 
     * that it does not throw an exception in the case that the list is empty;
     * it instead, in such a case, returns {@code null}.
     * 
     * @return The element at the head of this list. If the list is empty 
     *         ({@code isEmpty()} is {@code true}), returns {@code null}.
     */
    @Override
    public E peekFirst() {
        final Node<E> first = this.first;
        return (first == null)
                ? null
                : first.item;
    }
    
    /**
     * Retrieves, but does not remove, the last element in this 
     * {@code LinkedList}. If this list is empty, returns {@code null}.
     * 
     * @return The element at the tail of this list. If the list is empty 
     *         ({@code isEmpty()} is {@code true}), returns {@code null}.
     */
    @Override
    public E peekLast() {
        final Node<E> last = this.last;
        return (last == null)
                ? null
                : last.item;
    }
    
    /**
     * Retrieves and removes the head of this {@code LinkedList}, which is the
     * first and newest element in the structure.
     * 
     * <p> This method behaves in the same way as {@link #remove()}, except 
     * that it does not throw an exception in the case that the list is empty;
     * it instead, in such a case, returns {@code null}.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @return The element at the head of this list. If the list is empty 
     *         ({@code isEmpty()} is {@code true}), returns {@code null}.
     */
    @Override
    public E pollFirst() {
        final Node<E> first = this.first;
        return (first == null)
                ? null
                : unlinkFirst(first); // Increments modCount
    }
    
    /**
     * Retrieves and removes the tail of this {@code LinkedList}.
     * 
     * <p> This operation constitutes a structural modification.
     * 
     * @return The element at the head of this list. If the list is empty 
     *         ({@code isEmpty()} is {@code true}), returns {@code null}.
     */
    @Override
    public E pollLast() {
        final Node<E> last = this.last;
        return (last == null)
                ? null
                : unlinkLast(last); // Increments modCount
    }
    
    /**
     * Inserts the specified element into the top of the stack represented by
     * this list.
     * 
     * <p> This method call constitutes a structural modification and is 
     * equivalent to invoking {@link #addFirst(java.lang.Object)}.
     * 
     * @param element The element of to add to the top of the stack.
     */
    @Override
    public void push(E element) {
        addFirst(element); // Increments modCount
    }
    
    /**
     * Retrieves and removes the top of the stack represented by this list, 
     * which is the first and newest element.
     * 
     * <p> This method call constitutes a structural modification and is 
     * equivalent to invoking {@code remove(0)}, except that this method throws
     * a {@code NoSuchElementException} if this structure is empty.
     * 
     * @return The element removed from top of the stack represented by this 
     *         list.
     * @throws NoSuchElementException if the list is empty.
     */
    @Override
    public E pop() {
        return removeFirst(); // Increments modCount
    }
    
    /**
     * Removes the first occurrence of the given object from this list.
     * 
     * <p> If the given element exists in this list and is removed, this 
     * operation constitutes a structural modification.
     * 
     * @param o The object to attempt to remove from this list.
     * @return {@code true} if the given object was removed from this list.
     */
    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o); // Increments modCount
    }
    
    /**
     * Removes the last occurrence of the given object from this list.
     * 
     * <p> If the given element exists in this list and is removed, this 
     * operation constitutes a structural modification.
     * 
     * @param o The object to attempt to remove from this list.
     * @return {@code true} if the given object was removed from this list.
     */
    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            for (Node<E> n = last; n != null; n = n.previousLink) {
                if (n.item == null) {
                    unlink(n); // Increments modCount
                    return true;
                }
            }
        }
        else {
            for (Node<E> n = last; n != null; n = n.previousLink) {
                if (o.equals(n.item)) {
                    unlink(n); // Increments modCount
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns an {@code Iterator} over the elements contained in this 
     * collection.
     * 
     * <p> This iterators returned by this method traverse the list in
     * reverse sequential order.  The elements will be returned in order from
     * last (tail) to first (head).
     * 
     * <p> The iterators returned by this method are fail-fast, meaning that in 
     * the case of any concurrent modification during iteration, they will fail
     * as soon as the modification is detected, preventing any unexpected 
     * behavior.
     * 
     * @return A {@code Iterator} that traverses the elements contained in this 
     *         collection in reverse order.
     */
    @Override
    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }

    /**
     * Returns an {@code ListIterator} over the elements contained in this 
     * collection. 
     * 
     * <p> The iterators returned by this method are fail-fast, meaning that in 
     * the case of any concurrent modification during iteration, they will fail
     * as soon as the modification is detected, preventing any unexpected 
     * behavior.
     *
     * @param index The index to begin the {@code ListIterator} to return.
     * @return A {@code ListIterator} beginning at the specified index over the 
     *         elements contained in this collection.
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        rangeCheckForAdd(index);
        return new LinkedListIterator(index);
    }

    /**
     * The iterator for this {@code Collection}, contains implementation of all
     * imposed methods. Instances are retrieved using the 
     * {@link LinkedList#listIterator(int)} method.
     * 
     * @see LinkedList#listIterator(int)
     */
    private class LinkedListIterator 
        implements ListIterator<E> 
    {

        /**
         * The {@code Node} storing the element returned by most recent call to 
         * {@link #next()}. If there is no such element or after any calls to 
         * {@link #remove()}, this is set to {@code null}. 
         */
        Node<E> lastReturned;

        /**
         * The {@code Node} storing the element that would be returned by the
         * next call to {@link #next()}. If there is no such element, this is 
         * set to {@code null}. 
         */
        Node<E> next;
        
        /**
         * The index of the next object in this iterator, or the index of the
         * object that would be produced by the next call to {@link #next()}.
         */
        int nextIndex;
        
        /**
         * Stores the expected modification count for this iterator. If at any
         * time this value does not equal {@link AbstractList#modCount}, this
         * iterator will throw an {@code ConcurrentModificationException} to
         * prevent any unexpected or unintended behavior.
         */
        int expectedModCount;
        
        /**
         * Initializes a new {@code LinkedListIterator} with the default values, 
         * using the current {@link AbstractList#modCount} as the expected
         * modification count.
         */
        LinkedListIterator() {
            this(0);
        }
        
        /**
         * Initializes a new {@code LinkedListIterator} with the given starting 
         * index value and uses the current {@link AbstractList#modCount} as the
         * expected modification count.
         * 
         * @param index The index to begin this iteration.
         */
        LinkedListIterator(int index) {
            super();
            next = (index == size)
                    ? null
                    : LinkedList.this.nodeAt(index);
            nextIndex = index;
            expectedModCount = LinkedList.this.modCount;
        }
        
        /**
         * Checks this iterator's {@link #expectedModCount} against the 
         * {@link #modCount} of the underlying {@code LinkedList}, throwing a
         * {@code ConcurrentModificationException} if they are not equal.
         * 
         * @throws ConcurrentModificationException if the expected modification
         *         count does not equal that of the underlying 
         *         {@code LinkedList}.
         */
        void checkForComodification() {
            if (LinkedList.this.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
        
        /**
         * Applies the given {@code Consumer} to each remaining element in this
         * iteration. 
         *
         * @param action The action to be performed for each element remaining
         *               in this iteration.
         * @throws NullPointerException if the specified action is {@code null}.
         */
        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            for ( ; nextIndex < size; nextIndex++) {
                action.accept(next.item);
                lastReturned = next;
                next = next.nextLink;
                nextIndex++;
            }
            checkForComodification();
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
            if (nextIndex != 0) {
                return true;
            }
            checkForComodification();
            return false;
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
            if (nextIndex != LinkedList.this.size) {
                return true;
            }
            checkForComodification();
            return false;
        }
        
        /**
         * Returns the previous element in this iteration.
         * 
         * @return The previous element in this iteration.
         * @throws ConcurrentModificationException if the expected modification
         *         count does not equal that of the underlying 
         *         {@code LinkedList}.
         * @throws NoSuchElementException if the iteration has no more elements.
         */
        @Override
        public E previous() {
            checkForComodification();
            if (hasPrevious()) {
                next = (next == null)
                        ? last
                        : next.previousLink;
                lastReturned = next;
                nextIndex--;
                return lastReturned.item;
            }
            else {
                throw new NoSuchElementException("No remaining elements!");
            }
        }
        
        /**
         * Retrieves the next element in the iteration.
         *
         * @return The next element in the iteration.
         * @throws ConcurrentModificationException if the expected modification
         *         count does not equal that of the underlying 
         *         {@code LinkedList}.
         * @throws NoSuchElementException if the iteration has no more elements.
         */
        @Override
        public E next() {
            checkForComodification();
            if (hasNext()) {
                lastReturned = next;
                next = next.nextLink;
                nextIndex++;
                return lastReturned.item;
            }
            else {
                throw new NoSuchElementException("No remaining elements!");
            }
        }
        
        /**
         * Returns the previous index value of this iteration.
         * 
         * @return The previous index of this iteration.
         * @see #nextIndex
         */
        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        /**
         * Returns the next index value of this iteration.
         * 
         * @return The next index of this iteration.
         * @see #previousIndex()
         */
        @Override
        public int nextIndex() {
            return nextIndex;
        }
        
        /**
         * Adds the given element to the underlying {@code LinkedList}.
         * 
         * @param element The element to add to the backing {@code LinkedList}.
         * @throws ConcurrentModificationException if the expected modification
         *         count does not equal that of the underlying 
         *         {@code LinkedList}.
         */
        @Override
        public void add(E element) {
            checkForComodification();
            lastReturned = null;
            if (next == null) {
                linkLast(element); // Increments modCount
            }
            else {
                linkBefore(element, next); // Increments modCount
            }
            nextIndex++;
            expectedModCount++;
        }

        /**
         * Sets the given element to the underlying {@code LinkedList} at the 
         * current cursor index.
         * 
         * @param element The element to set at the specified index in the 
         *        backing {@code LinkedList}.
         * @throws ConcurrentModificationException if the expected modification
         *         count does not equal that of the underlying 
         *         {@code LinkedList}.
         * @throws IllegalStateException if the {@link #next()} method has not 
         *         previously been called or the {@link #remove()} method has 
         *         already been called after the last call to the 
         *         {@link #next()} method.
         */
        @Override
        public void set(E element) {
            if (lastReturned == null) {
                throw new IllegalStateException("No current index!");
            }
            checkForComodification();
            lastReturned.item = element;
        }

        /**
         * Removes from the underlying {@code LinkedList} the last element 
         * returned by this iterator. This method can be called only once per 
         * call to {@link #next()}. The behavior of an iterator is unspecified 
         * if the underlying collection is modified while the iteration is in 
         * progress in any way other than by calling this method.
         * 
         * @throws ConcurrentModificationException if the expected modification
         *         count does not equal that of the underlying 
         *         {@code LinkedList}.
         * @throws IllegalStateException if the {@link #next()} method has not 
         *         previously been called or the {@link #remove()} method has 
         *         already been called after the last call to the 
         *         {@link #next()} method.
         */
        @Override
        public void remove() {
            checkForComodification();
            if (lastReturned == null) {
                throw new IllegalStateException("No previous call to remove()");
            }
            Node<E> lastReturnedNext = lastReturned.nextLink;
            unlink(lastReturned); // Increments modCount
            if (next == lastReturned) {
                next = lastReturnedNext;
            }
            else {
                nextIndex--;
            }
            lastReturned = null;
            expectedModCount++;
        }
        
    }
    
    /**
     * The iterator that provides for reverse order traversal for this 
     * {@code Collection}. Contains implementation of all imposed methods. 
     * Instances are retrieved using the {@link LinkedList#descendingIterator()}
     * method.
     * 
     * @see LinkedList#descendingIterator()
     */
    private class DescendingIterator
        implements Iterator<E> {

        /**
         * Adapter that provides for descending iteration. Methods are simply
         * called from this object in opposite place, or in other words, when
         * the next element in this iteration is desired, this iterator returns 
         * the previous element from {@code iterator}, and vice-versa.
         */
        final LinkedListIterator iterator 
                = new LinkedListIterator(LinkedList.this.size);
        
        /**
         * Returns the previous element in this descending iteration.
         * 
         * @return The previous element in this iteration.
         * @throws ConcurrentModificationException if the expected modification
         *         count does not equal that of the underlying 
         *         {@code LinkedList}.
         * @throws NoSuchElementException if the iteration has no more elements.
         */
        @Override
        public boolean hasNext() {
            return iterator.hasPrevious();
        }
        
        /**
         * Retrieves the next element in the descending iteration.
         *
         * @return The next element in the iteration.
         * @throws ConcurrentModificationException if the expected modification
         *         count does not equal that of the underlying 
         *         {@code LinkedList}.
         * @throws NoSuchElementException if the iteration has no more elements.
         */
        @Override
        public E next() {
            return iterator.previous();
        }
        
        /**
         * Removes from the underlying {@code LinkedList} the last element 
         * returned by this descending iterator. This method can be called only
         * once per call to {@link #next()}. 
         * 
         * <p> The behavior of an iterator is unspecified if the underlying 
         * collection is modified while the iteration is in progress in any way 
         * other than by calling this method.
         * 
         * @throws ConcurrentModificationException if the expected modification
         *         count does not equal that of the underlying 
         *         {@code LinkedList}.
         * @throws IllegalStateException if the {@link #next()} method has not 
         *         previously been called or the {@link #remove()} method has 
         *         already been called after the last call to the 
         *         {@link #next()} method.
         */
        @Override
        public void remove() {
            iterator.remove();
        }
        
    }

    /**
     * Returns an array containing all elements within this {@code LinkedList}
     * in their appropriate, natural order.
     * 
     * @return An array containing the elements in this {@code Collection}.
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Node<E> n = this.first;
        for (int i = 0; n != null; n = n.nextLink, i++) {
            array[i] = n.item;
        }
        return array;
    }

    /**
     * Tests if this {@code LinkedList} contains all the elements within the 
     * specified {@code Collection}. While the ordering of the objects does not 
     * matter, it is required that the given elements are exactly equal to any 
     * of their equivalents within this object, as contracted by their 
     * respective {@link Object#equals(java.lang.Object)} methods.
     * 
     * @param c The {@code Collection} whose elements to test for containment
     *        within this {@code LinkedList}.
     * @return {@code true} if this object contains all the elements specified
     *         within the given collection, {@code false} otherwise.
     * @throws NullPointerException if the specified argument {@code c} is 
     *         {@code null}.
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        Objects.requireNonNull(c, "Invalid null collection!");
        return c.stream().allMatch(this :: contains);
    }
    
    /**
     * Performs the given action for each element contained within this 
     * {@code LinkedList} in <em>forward</em> order. The behavior of this method
     * is undefined if this list is modified during the iteration process.
     * 
     * @param action The action to apply to each element in this list.
     * @throws NullPointerException if the specified argument {@code action} is 
     *         {@code null}.
     */
    @Override
    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        for(Node<E> n = first; n != null; n = n.nextLink) {
            action.accept(n.item);
        }
    }
        
    /**
     * This implementation returns a "shallow" copy of this {@code LinkedList}. 
     * This method copies, but does not clone, the individual elements in 
     * contained within this collection.
     * 
     * @return A shallow copy of this {@code LinkedList} instance.
     * @throws InternalError if the caller object is not of type 
     *         {@code LinkedList}.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object clone() {
        try {
            LinkedList<E> clone = (LinkedList<E>) super.clone();
            clone.first =  null;
            clone.last = null;
            clone.size = 0;
            clone.modCount = 0;
            // Initialize the clone with the elements in this object
            for (Node<E> n = this.first; n != null; n = n.nextLink) {
                clone.add(n.item); // Increments modCount
            }
            return clone;
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError("Invalid object called clone()", e);
        }
    }

    
    /**
     * Compares the specified object with this {@code LinkedList} for equality. 
     * Returns {@code true} if the given argument is an instance of {@code List}
     * and has the same size as this collection. In this implementation, two
     * independent lists are equal if they contain the same elements in the same
     * order.
     * 
     * @param o The object to test against this one for equality.
     * @return {@code true} if the given argument has the same size and the same
     *         elements (in the same order) as this {@code LinkedList}, 
     *         otherwise {@code false}.
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == null || !(o instanceof List)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        final List<?> other = (List<?>) o;
        Node<E> thisNext = this.first;
        for (int i = 0; thisNext != null; thisNext = thisNext.nextLink, i++) {
            Object otherNext = other.get(i);
            if (!(thisNext.item == null && otherNext == null) || 
                    !thisNext.item.equals(otherNext)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns a {@code String} representation of this {@code LinkedList}, 
     * delimiting each element in this collection with a single comma 
     * ({@code ,}) and the entire collection with square-brackets ({@code []}). 
     * Any occurrences of this object within its own structure will result in an
     * element represented by {@code "(this LinkedList)"} to be appended.
     * 
     * @return A {@code String} representation of this {@code LinkedList}'s data
     *         structure.
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder().append('[');
        Node<E> next = this.first;
        for (int i = 0; next != null; next = next.nextLink, i++) {
            sb.append((next.item == this)
                    ? "(this LinkedList)" 
                    : next.item);
            sb.append((i >= size - 1)
                    ? ']' 
                    : ", ");
        }
        return sb.toString();
    }
    
}