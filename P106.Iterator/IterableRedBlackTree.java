import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.NoSuchElementException;
import javax.accessibility.AccessibleRelationSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * This class extends RedBlackTree into a tree that supports iterating over the values it
 * stores in sorted, ascending order.
 */
public class IterableRedBlackTree<T extends Comparable<T>>
                extends RedBlackTree<T> implements IterableSortedCollection<T> {

    private Comparable<T> max = null;
    private Comparable<T> min = null;

    /**
     * Allows setting the start (minimum) value of the iterator. When this method is called,
     * every iterator created after it will use the minimum set by this method until this method
     * is called again to set a new minimum value.
     * @param min the minimum for iterators created for this tree, or null for no minimum
     */
    public void setIteratorMin(Comparable<T> min) { this.min = min; }

    /**
     * Allows setting the stop (maximum) value of the iterator. When this method is called,
     * every iterator created after it will use the maximum set by this method until this method
     * is called again to set a new maximum value.
     * @param min the maximum for iterators created for this tree, or null for no maximum
     */
    public void setIteratorMax(Comparable<T> max) { this.max = max; }

    /**
     * Returns an iterator over the values stored in this tree. The iterator uses the
     * start (minimum) value set by a previous call to setIteratorMin, and the stop (maximum)
     * value set by a previous call to setIteratorMax. If setIteratorMin has not been called
     * before, or if it was called with a null argument, the iterator uses no minimum value
     * and starts with the lowest value that exists in the tree. If setIteratorMax has not been
     * called before, or if it was called with a null argument, the iterator uses no maximum
     * value and finishes with the highest value that exists in the tree.
     */
    public Iterator<T> iterator() { return new RBTIterator<T>(root, min, max); }

    /**
     * Nested class for Iterator objects created for this tree and returned by the iterator method.
     * This iterator follows an in-order traversal of the tree and returns the values in sorted,
     * ascending order.
     */
    protected static class RBTIterator<R> implements Iterator<R> {

         // stores the start point (minimum) for the iterator
         Comparable<R> min = null;
         // stores the stop point (maximum) for the iterator
         Comparable<R> max = null;
         // stores the stack that keeps track of the inorder traversal
         Stack<BinaryTreeNode<R>> stack = null;

        /**
         * Constructor for a new iterator if the tree with root as its root node, and
         * min as the start (minimum) value (or null if no start value) and max as the
         * stop (maximum) value (or null if no stop value) of the new iterator.
         * @param root root node of the tree to traverse
         * @param min the minimum value that the iterator will return
         * @param max the maximum value that the iterator will return
         */
        public RBTIterator(BinaryTreeNode<R> root, Comparable<R> min, Comparable<R> max) { 
            this.min = min;
            this.max = max;
            stack = new Stack<>();
            buildStackHelper(root);
        }

        /**
         * Helper method for initializing and updating the stack. This method both
         * - finds the next data value stored in the tree (or subtree) that is
         * between start(minimum) and stop(maximum) point (including start and stop points themselves), and
         * - builds up the stack of ancestor nodes that contain values between start(minimum) and stop(maximum) values
         * (including start and stop values themselves) so that those nodes can be visited in the future.
         * @param node the root node of the subtree to process
         */
        private void buildStackHelper(BinaryTreeNode<R> node) { 
            if (node == null) {
                return; // base case
            }
        
            // explore right subtree if current node less than min 
            if (min != null && min.compareTo(node.data) > 0) {
                buildStackHelper(node.childRight()); // recursive case 1
            } 
            else {
                // push nodes within the range of min to max (inclusive range)
                if(max == null || max.compareTo(node.data) >= 0) {
                    stack.push(node);
                }
                // explore left subtree for smaller values that are still in range
                buildStackHelper(node.childLeft()); // recursive case 2
            }
        }

        /**
         * Returns true if the iterator has another value to return, and false otherwise.
         */
        public boolean hasNext() { 
            return !stack.isEmpty();
        }

        /**
         * Returns the next value of the iterator.
         * @throws NoSuchElementException if the iterator has no more values to return
         */
        public R next() { 
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements to visit."); // if no more nodes to explore
            }
            BinaryTreeNode<R> node = stack.pop();
            R data = node.data;
            buildStackHelper(node.childRight()); // build stack with right subtree of current node
            return data; // return value in stack
        }
    }

    /**
     * Tree consisting of integers and duplicates, tests iteration results with null range
     */
    @Test
    public void test1() {
        // create new RBT and insert initial values
        IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
        tree.insert(30);
        tree.insert(20);
        tree.insert(20); // duplicate
        tree.insert(40);
        tree.insert(50);

        // iterator with no min/max settings     
        Iterator<Integer> iterator = tree.iterator();

        // check iterator results against expected values
        ArrayList<Integer> expected = new ArrayList<>();
        while (iterator.hasNext()) {
            expected.add(iterator.next());
        }
        Assertions.assertTrue(expected.equals(Arrays.asList(20, 20, 30, 40, 50)));
    }

    /**
     * Tree consisting of strings and a string-based min/max range, tests iteration results 
     */
    @Test
    public void test2() {
        // create new RBT and insert initial values
        IterableRedBlackTree<String> tree1 = new IterableRedBlackTree<>();
        
        tree1.insert("avocado");
        tree1.insert("blueberry");
        tree1.insert("cabbage");
        tree1.insert("dillpickle");
        
        tree1.setIteratorMin("blueberry");
        tree1.setIteratorMax("cabbage");
        
        // iterator with defined string-based min/max restraints     
        Iterator<String> iterator1 = tree1.iterator();
        
        // check iterator results over given range
        Assertions.assertTrue(iterator1.hasNext());
        Assertions.assertEquals("blueberry", iterator1.next());
        Assertions.assertTrue(iterator1.hasNext());
        Assertions.assertEquals("cabbage", iterator1.next());
        Assertions.assertFalse(iterator1.hasNext());

        // create new RBT and insert initial values
        IterableRedBlackTree<String> tree2 = new IterableRedBlackTree<>();
        
        tree2.insert("apricot");
        tree2.insert("blackberry");
        tree2.insert("cauliflower");
        tree2.insert("dumplings");
        
        tree2.setIteratorMin("cauliflower");
        
        // iterator with only a specified stop point, no start point     
        Iterator<String> iterator2 = tree2.iterator();
        
        // check iterator results over given range
        Assertions.assertTrue(iterator2.hasNext());
        Assertions.assertEquals("cauliflower", iterator2.next());
        Assertions.assertTrue(iterator2.hasNext());
        Assertions.assertEquals("dumplings", iterator2.next());
        Assertions.assertFalse(iterator2.hasNext());

    }

    /**
     * Tree consisting of integers and integer-based min/max range, tests iteration results 
     */
    @Test
    public void test3() {
        // create new RBT and insert initial values
        IterableRedBlackTree<Integer> tree1 = new IterableRedBlackTree<>();
        
        tree1.insert(10);
        tree1.insert(20);
        tree1.insert(30);
        tree1.insert(40);
        tree1.insert(50);
        
        tree1.setIteratorMin(20);
        tree1.setIteratorMax(40);
        
        // iterator with defined integer-based min/max range     
        Iterator<Integer> iterator1 = tree1.iterator();
        
        // check iterator results against expected values
        ArrayList<Integer> expected1 = new ArrayList<>();
        while (iterator1.hasNext()) {
            expected1.add(iterator1.next());
        }
        Assertions.assertTrue(expected1.equals(Arrays.asList(20, 30, 40)));

        // create new RBT and insert initial values
        IterableRedBlackTree<Integer> tree2 = new IterableRedBlackTree<>();
        
        tree2.insert(5);
        tree2.insert(10);
        tree2.insert(15);
        tree2.insert(20);
        tree2.insert(25);
        
        tree2.setIteratorMin(15);
        
        // iterator with only a specified start point, no stop point     
        Iterator<Integer> iterator2 = tree2.iterator();
        
        // check iterator results against expected values
        ArrayList<Integer> expected2 = new ArrayList<>();
        while (iterator2.hasNext()) {
            expected2.add(iterator2.next());
        }
        Assertions.assertTrue(expected2.equals(Arrays.asList(15, 20, 25)));
    }
}
