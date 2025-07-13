import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.ArrayList;

public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType>{

    protected LinkedList<Pair>[] table = null;
    private int size = 0;
    private ArrayList<KeyType> keys = new ArrayList<>();

    protected class Pair {

        public KeyType key;
        public ValueType value;

        public Pair(KeyType key, ValueType value) {
            this.key = key;
            this.value = value;
        }

    }

    // single argument constructor that initializes a hashmap with given capacity
    public HashtableMap(int capacity) {
        this.table = new LinkedList[capacity];
    }

    // default constructor that initializes a hashmap with default capacity of 64
    public HashtableMap() {
        this(64);
    }

  /**
   * Inserts a pair containing the provided key and value objects at the correct position in the hashmap
   * @param key a KeyType, value a ValueType
   * @throws IllegalArgumentException if the hashmap already contains this key
   * @throws NoSuchElementException if the passed key is null
   * @return None
   */
    @Override
    public void put(KeyType key, ValueType value) throws IllegalArgumentException {
        if(key == null) {
            throw new NoSuchElementException("key is null"); // check if key is null
        }
        if(this.containsKey(key)) {
            throw new IllegalArgumentException("key already exists in hashmap"); // check if key already exists
        }
        int id = Math.abs(key.hashCode() % this.table.length); // use hashCode() to get the hash index
        if (this.table[id] == null) {
            this.table[id] = new LinkedList<>(); // if this index is empty, initialize a new LinkedList there
        }
        this.table[id].add(new Pair(key, value)); // add pair to the correct index
        keys.add(key);
        size++; // increment size
        if((double)size / this.table.length >= 0.8) {
            resizeHelper(); // check the load factor and if necessary, resize and rehash using the helper method
        }
    }

  /**
   * Checks whether a key-value pair containing the given key exists in the hashmap
   * @param key a KeyType
   * @return true if the hashmap contains the key, false otherwise
   */
    @Override
    public boolean containsKey(KeyType key) {
        int id = Math.abs(key.hashCode() % this.table.length); // use hashCode() to get the hash index
        if (this.table[id] != null) {
            for(Pair pair : this.table[id]) {
                if(pair.key.equals(key)) {
                    return true; // if the key is found in the LinkedList at the hash index, then return true
                }
            }
        }
        return false; // key not found
    }

  /**
   * Finds and returns the value stored at the key provided as an argument
   * @param key a KeyType
   * @throws NoSuchElementException if the given key does not exist in hashmap
   * @return the value stored at the given key, a ValueType object
   */
    @Override
    public ValueType get(KeyType key) throws NoSuchElementException {
        if(!this.containsKey(key)) {
            throw new NoSuchElementException("key is not contained in this collection"); // check if key doesn't exist
        }
        int id = Math.abs(key.hashCode() % this.table.length); // use hashCode() to get the hash index
        if (this.table[id] != null) {
            for(Pair pair : this.table[id]) {
                if(pair.key.equals(key)) {
                    return pair.value; // if the key is found in the LinkedList at the hash index, then return the value at the key
                }
            }
        }
        return null; // key not found
    }

  /**
   * Finds and removes the key-value pair, with the key provided as an argument
   * and returns a reference to the value of the pair
   * @param key a KeyType
   * @throws NoSuchElementException if the given key does not exist in hashmap
   * @return the value stored at the removed key, a ValueType object
   */
    @Override
    public ValueType remove(KeyType key) throws NoSuchElementException {
        if(!this.containsKey(key)) {
            throw new NoSuchElementException("key is not contained in this collection"); // check if key doesn't exist
        }
        int id = Math.abs(key.hashCode() % this.table.length); // use hashCode() to get the hash index
        if (this.table[id] != null) {
            for(Pair pair : this.table[id]) {
                if(pair.key.equals(key)) {
                    ValueType val = pair.value;
                    this.table[id].remove(pair); // if the key is found in the LinkedList at the hash index, then remove the corresponding pair
                    keys.remove(key);
                    size --; // decrement size after removal
                    return val; // return a reference to the removed value
                }
            }
        }
        return null; // key not found
    }

  /**
   * Sets all elements of the hashtable to null (clears table)
   * @param None
   * @return None
   */
    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            this.table[i] = null; // iterate through and clear the hashtable
        }
        size = 0; // reset size
    }

  /**
   * Returns size of this hashtable
   * @param None
   * @return hashtable's size, an int
   */
    @Override
    public int getSize() {
        return size;
    }

  /**
   * Returns capacity of this hashtable
   * @param None
   * @return hashtable's length, an int
   */
    @Override
    public int getCapacity() {
        return this.table.length;
    }

  /**
   * Private helper method to handle resizing and rehashing of hashtable
   * when a high load factor is reached
   * @param None
   * @return None
   */
    private void resizeHelper(){
        LinkedList<Pair>[] new_table = new LinkedList[this.table.length * 2]; // initialize a new table with double the size of the current hashtable
        for (LinkedList<Pair> current_index : this.table) { // loop through the current hashtable
            if(current_index != null) { // if current index is non-null, transfer contents to new table
                for(Pair pair : current_index) {
                        int new_index = Math.abs(pair.key.hashCode() % new_table.length); // use hashCode() to get the NEW hash index
                        if (new_table[new_index] == null) {
                            new_table[new_index] = new LinkedList<>(); // if this index is empty, initialize a new LinkedList there
                        }
                        new_table[new_index].add(pair); // add pair to the new table
                }
            }
        }
        this.table = new_table; // set new table as current hashmap
    }

    @Override
    public List<KeyType> getKeys() {
        return keys;
    }

    // tester method for testing both implementations of the constructor (check default and single argument)
    @Test
    public void testConstructors() {
        HashtableMap map = new HashtableMap<>();
        Assertions.assertEquals(64, map.getCapacity(), "Expected capacity should be 64 by default.");
        HashtableMap map2 = new HashtableMap<>(40);
        Assertions.assertEquals(40, map2.getCapacity(), "Expected capacity is 40.");
    }

    // tester method for testing basic functionality (single pair input at each index) of hashtable
    @Test
    public void testBasicFunctionality() {
        HashtableMap map = new HashtableMap<>(9); // create a new hashtable
        Assertions.assertEquals(9, map.getCapacity(), "The hashmap should have a capacity of nine.");
        // insert pairs for testing purposes
        map.put(0, "first");
        map.put(4, "second");
        map.put(8, "third");
        // run assertions below for all of the different methods (containsKey, get, getSize, clear, remove)
        Assertions.assertEquals(true, map.containsKey(8), "Pair with key value of 8 is expected to exist in the hashmap.");
        Assertions.assertEquals("first", map.get(0), "Expected value is 'first' for key '0'.");
        Assertions.assertEquals(3, map.getSize(), "Expected size is three after adding elements.");
        Assertions.assertEquals("second", map.remove(4), "Removing '4' key should delete value 'second' from hashmap.");
        Assertions.assertEquals(2, map.getSize(), "Expected size is two after removing an element.");
        map.clear();
        Assertions.assertEquals(0, map.getSize(), "Map should contain no elements after clearing.");
    }

    // tester method for testing complex functionality (multiple pair inputs at each index) of hashtable
    @Test
    public void testComplexFunctionality() {
        HashtableMap map = new HashtableMap<>(26); // create a new hashtable
        Assertions.assertEquals(26, map.getCapacity(), "The hashmap should have a capacity of 26.");
        // insert pairs for testing purposes
        map.put("apple", "first");
        map.put("tangerine", "second");
        map.put("tomatoes", "third");
        map.put("cucumber", "fourth");
        map.put("cauliflower", "fifth");
        map.put("watermelon", "sixth");
        // run assertions below for all of the different methods (containsKey, get, getSize, clear, remove)
        Assertions.assertEquals(true, map.containsKey("tomatoes"), "Pair with key value of 'tomatoes' is expected to exist in the hashmap.");
        Assertions.assertEquals("first", map.get("apple"), "Expected value is 'first' for key 'apple'.");
        Assertions.assertEquals(6, map.getSize(), "Expected size is six after adding elements.");
        Assertions.assertEquals("fourth", map.remove("cucumber"), "Removing 'cucumber' key should delete value 'fourth' from hashmap.");
        Assertions.assertEquals(5, map.getSize(), "Expected size is five after removing an element.");
        map.clear();
        Assertions.assertEquals(0, map.getSize(), "Map should contain no elements after clearing.");
    }

    // tester method for testing rehashing and resizing helper method and related functionality
    @Test
    public void testResizingAndRehashing() {
        HashtableMap map = new HashtableMap<>(5); // create a new hashtable
        // insert pairs for testing purposes
        map.put(0, "first");
        map.put(1, "second");
        map.put(2, "third");
        map.put(3, "fourth");
        Assertions.assertEquals(10, map.getCapacity(), "Hashmap should have resized and rehashed because load factor was reached.");
        map.put(4, "fifth"); // after this insertion, expected to resize/rehash because load factor met
        // run assertions below for all of the different methods (containsKey, get, getSize, clear, remove)
        Assertions.assertEquals(true, map.containsKey(2), "Expected to contain key value of 2 in the hashmap.");
        Assertions.assertEquals("fifth", map.get(4), "Expected value is 'fifth' for key '4'.");
        Assertions.assertEquals(5, map.getSize(), "Expected size is five after resizing and rehashing.");
        Assertions.assertEquals("second", map.remove(1), "Removing '1' key should delete value 'second' from hashmap.");
        Assertions.assertEquals(4, map.getSize(), "Expected size is four after removing an element.");
        map.clear();
        Assertions.assertEquals(0, map.getSize(), "Map should contain no elements after clearing.");
    }

    // tester method for testing all edge cases involving error handling in the class (check if all errors are thrown and handled as expected)
    @Test
    public void testExceptions() {
        HashtableMap map = new HashtableMap<>(6); // create a new hashtable
        map.put(0, "first");
        try {
            map.put(null, "second"); // expected exception
            Assertions.assertTrue(false, "NoSuchElementException expected when attempting to input a null key into hashmap.");
        } catch (NoSuchElementException e) {
            System.out.println("Null key cannot be entered.");
        }
        try {
            map.put(0, "second"); // expected exception
            Assertions.assertTrue(false, "IllegalArgumentException expected when attempting to input a key that already exists.");
        } catch (IllegalArgumentException e) {
            System.out.println("Key already exists in hashmap.");
        }
        map.put(1, "second");
        try {
            map.get(2); // expected exception
            Assertions.assertTrue(false, "NoSuchElementException expected when attempting to fetch at a nonexistent key.");
        } catch (NoSuchElementException e) {
            System.out.println("Key not found in hashmap.");
        }
        try {
            map.remove(2); // expected exception
            Assertions.assertTrue(false, "NoSuchElementException expected when attempting to remove a nonexistent key.");
        } catch (NoSuchElementException e) {
            System.out.println("Key not found in hashmap.");
        }
    }

}