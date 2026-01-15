/**
 * A linked list of character data objects.
 * (Actually, a list of Node objects, each holding a reference to a character
 * data object.
 * However, users of this class are not aware of the Node objects. As far as
 * they are concerned,
 * the class represents a list of CharData objects. Likwise, the API of the
 * class does not
 * mention the existence of the Node objects).
 */
public class List {

    // Points to the first node in this list
    private Node head;

    // The number of elements in this list
    private int n;

    /** Constructs an empty list. */
    public List() {
        head = null;
        n = 0;
    }

    /** Returns the number of elements in this list. */
    public int getSize() {
        return n;
    }

    /** Returns the CharData of the first element in this list. */
    public CharData getFirst() {
        if (head == null) {
            return null;
        }
        return head.cp;
    }

    /**
     * Adds a CharData object with the given character to the beginning of this
     * list.
     */
    public void addFirst(char c) {
        Node temp = new Node(new CharData(c));
        if (head != null) {
            temp.next = head;
        }
        head = temp;
        n++;
    }

    /** GIVE Textual representation of this list. */
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        Node temp = head;
        while (temp != null) {
         sb.append(temp.cp.toString());
           temp = temp.next;
            if (temp != null) {
                sb.append(" ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * Returns the index of the first CharData object in this list
     * that has the same chr value as the given char,
     * or -1 if there is no such object in this list.
     */
    public int indexOf(char c) {
        Node temp = head;
        int pos = 0;
        while (temp != null) {
            if (temp.cp.chr == c) {
                return pos;
            }
            temp = temp.next;
            pos++;
        }
        return -1; // Value not found
    }

    /**
     * If the given character exists in one of the CharData objects in this list,
     * increments its counter. Otherwise, adds a new CharData object with the
     * given chr to the beginning of this list.
     */
    public void update(char c) {
        Node temp = head;
        while (temp != null) {
            if (temp.cp.chr == c) {
                temp.cp.count++;
                return;
            }
            temp = temp.next;
        }
        // If the given character doesn't exist in one of the CharData objects in this
        // list, adds it.
        addFirst(c);
    }

    /**
     * If the given character exists in one of the CharData objects
     * in this list, removes this CharData object from the list and returns
     * true. Otherwise, returns false.
     */
    public boolean remove(char c) {
        Node temp = head;
        Node prev = null;
        while (temp != null) {
            if (temp.cp.chr == c) {
                if (prev == null) {
                    head = temp.next;
                } else {
                    prev.next = temp.next;
                    temp = temp.next;
                }
                n--;
                return true;
            } else {
                prev = temp;
                temp = temp.next;
            }
        }
        return false;
    }

    /**
     * Returns the CharData object at the specified index in this list.
     * If the index is negative or is greater than the size of this list,
     * throws an IndexOutOfBoundsException.
     */
    public CharData get(int index) {
        if (index < 0 || index >= n) {
            throw new IndexOutOfBoundsException(
         "The CharData object's index is negative or greater than the size of this list");
        }
        Node temp = head;
        for (int i = 0; i < index; i++) {
        temp = temp.next;
        }
        return temp.cp;
    }

    /**
     * Returns an array of CharData objects, containing all the CharData objects in
     * this list.
     */
    public CharData[] toArray() {
        CharData[] arr = new CharData[n];
        Node temp = head;
        int i = 0;
        while (temp != null) {
        arr[i++] = temp.cp;
        temp = temp.next;
        }
        return arr;
    }

    /**
     * Returns an iterator over the elements in this list, starting at the given
     * index.
     */
    public ListIterator listIterator(int index) {
        // If the list is empty, there is nothing to iterate
        if (n == 0)
        return null;
        // Gets the element in position index of this list
        Node temp = head;
        int i = 0;
        while (i < index) {
        temp = temp.next;
        i++;
        }
        // Returns an iterator that starts in that element
        return new ListIterator(temp);
    }
}