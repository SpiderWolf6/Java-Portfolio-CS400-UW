import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class RedBlackTree<T extends Comparable<T>> extends BSTRotation<T> {

    // inherits root field from BSTRotation (inherited from BinarySearchTree)
    // in order to access color of the root, cast root reference to RBTNode (ex: ((RBTNode<T>)this.root).isRed)

    /**
     * Checks if a new red node in the RedBlackTree causes a red property violation
     * by having a red parent. If this is not the case, the method terminates without
     * making any changes to the tree. If a red property violation is detected, then
     * the method repairs this violation and any additional red property violations
     * that are generated as a result of the applied repair operation.
     * @param newNode a newly inserted red node, or a node turned red by previous repair
    */
    protected void ensureRedProperty(RBTNode<T> newNode) {
        if(newNode == root) {
            return; // if current node is root, done checking for errors
        }
        if(newNode.isRed() && newNode.parent().isRed()) {
            RBTNode<T> parent = newNode.parent(); // store parent reference
            RBTNode<T> grandparent = parent.parent(); // store grandparent reference
            RBTNode<T> aunt = (grandparent.childLeft() == parent) ? grandparent.childRight() : grandparent.childLeft(); // determine aunt
            if (aunt != null && aunt.isRed()) {
                grandparent.flipColor(); // flip grandparent, parent, and aunt colors
                parent.flipColor();
                aunt.flipColor();
                ensureRedProperty(grandparent);  // recursively check properties from grandparent node
            }
            else {
                if((grandparent.childRight() == parent && parent.childRight() == newNode) || (grandparent.childLeft() == parent && parent.childLeft() == newNode)) {
                    rotate(parent, grandparent); // perform rotation on parent-grandparent pair
                    parent.flipColor(); // flip colors after rotation
                    grandparent.flipColor();
                }
                else {
                    rotate(newNode, parent); // perform first rotation on parent-child pair
                    rotate(newNode, grandparent); // perform second rotation on original child-grandparent pair
                    newNode.flipColor(); // flip colors after second rotation
                    grandparent.flipColor();
                    ensureRedProperty(parent); // recursively check cascading violations from parent node
                }
            }
        }
    }

    /**
     * Override insert method from BinarySearchTree to add red-black nodes to a Red Black Tree
     * @param data an input of data type T contained in the node
    */
    @Override
    public void insert(T data) throws NullPointerException{
        if(data == null) {
            throw new NullPointerException();
        }
        RBTNode<T> node = new RBTNode<>(data);
        if (isEmpty()) {
            root = node;
        }
        else {
            insertHelper(node, root);
            ensureRedProperty(node);
        }
        ((RBTNode<T>)this.root).isRed = false;
    }

    /**
     * Runs a test using the fifth question from the RBTInsert quiz. 
    */
    @Test
    public void test1() {
        RedBlackTree<String> tree = new RedBlackTree<>();
        tree.insert("C");
        tree.insert("D");
        tree.insert("F"); // already aligned, triggers null-aunt one rotation recolor repair 
        tree.insert("B"); 
        tree.insert("G");
        String expected = "[ D(b), C(b), F(b), B(r), G(r) ]";
        String actual = tree.root.toLevelOrderString();
        System.out.println(actual);
        Assertions.assertEquals(expected, actual, "Tree structure after insertions is incorrect"); // check expected vs actual tree structure
    }
    
    /**
     * Runs a test using a more complicated tree with cascading violations and multiple repair methods.
    */
    @Test
    public void test2() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(50);
        tree.insert(30);
        tree.insert(70);
        tree.insert(20); // triggers red-aunt recolor repair operation
        tree.insert(40);
        tree.insert(60);
        tree.insert(80);
        tree.insert(10); // triggers red-aunt recolor repair, which cascades up to the parent-grandparent pair
        tree.insert(25); // cascading violation from previous step triggers black-aunt double rotation recolor repair 
        tree.insert(5); // triggers red-aunt recolor repair operation
        String expected = "[ 30(b), 20(r), 50(r), 10(b), 25(b), 40(b), 70(b), 5(r), 60(r), 80(r) ]";
        String actual = tree.root.toLevelOrderString();
        System.out.println(actual);
        Assertions.assertEquals(expected, actual, "Tree structure after insertions is incorrect"); // check expected vs actual tree structure
    }

    /**
     * Runs a test using a more straightforward tree to test simpler repair operation.
    */
    @Test
    public void test3() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(10);
        tree.insert(20);
        tree.insert(30); // already aligned, triggers null-aunt one rotation recolor repair 
        tree.insert(15); // unaligned with parent and grandparent, but only triggers red-aunt recolor repair operation
        tree.insert(25);
        tree.insert(5);
        String expected = "[ 20(b), 10(b), 30(b), 5(r), 15(r), 25(r) ]";
        String actual = tree.root.toLevelOrderString();
        System.out.println(actual);
        Assertions.assertEquals(expected, actual, "Tree structure after insertions is incorrect"); // check expected vs actual tree structure
    }

    /**
     * Main method to run all the test cases.
     */
    // public static void main(String[] args) {
    //     RedBlackTree<Integer> test = new RedBlackTree<>();
        // test.test1();
        // test.test2();
        // test.test3();
    // }

}
