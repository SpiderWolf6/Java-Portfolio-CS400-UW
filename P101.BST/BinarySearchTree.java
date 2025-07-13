public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T> {
    protected BinaryTreeNode<T> root;

    /**
     * Checks if the tree is empty.
     * @return true if the tree is empty (root is null), false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return root == null;  // Returns true if root is null, indicating an empty tree
    }

    /**
     * Clears the tree, setting the root to null.
     * This effectively removes all nodes from the tree.
     */
    @Override
    public void clear() {
        root = null;  // Removes all nodes by setting the root to null
    }

    /**
     * Returns the number of elements in the tree.
     * @return the number of elements in the tree.
     */
    @Override
    public int size() {
        return sizeHelper(root);  // Uses a helper function to recursively calculate size
    }

    /**
     * Helper method to recursively calculate the size of the tree.
     * @param node the node to start the count from.
     * @return the total number of nodes in the tree.
     */
    private int sizeHelper(BinaryTreeNode<T> node) {
        if (node == null) {
            return 0;  // Base case: if node is null, size is 0
        }
        return 1 + sizeHelper(node.left) + sizeHelper(node.right);  // Counts the current node + left & right subtrees
    }

    /**
     * Checks if a value exists in the tree.
     * @param data the value to search for.
     * @return true if the value is found, false otherwise.
     */
    @Override
    public boolean contains(Comparable<T> data) {
        return containsHelper(data, root);  // Calls the helper method to check for value
    }

    /**
     * Helper method to recursively check if a value exists in the tree.
     * @param val the value to search for.
     * @param node the node to start the search from.
     * @return true if the value is found, false otherwise.
     */
    private boolean containsHelper(Comparable<T> val, BinaryTreeNode<T> node) {
        if (node == null) {
            return false;  // Base case: if node is null, value isn't found
        }
        if(node.getData() == val) {
            return true;  // Base case: if node data matches, return true
        }
        // Recursively check left and right subtrees
        return containsHelper(val, node.left) || containsHelper(val, node.right);
    }

    /**
     * Inserts a new data value into the tree.
     * @param data the value to insert into the tree.
     * @throws NullPointerException if the provided data is null.
     */
    @Override
    public void insert(T data) throws NullPointerException {
        if (data == null) {
            throw new NullPointerException();  // Throws exception if data is null
        }
        BinaryTreeNode<T> node = new BinaryTreeNode<>(data);  // Creates a new node with the data
        if (root == null) {
            root = node;  // If the tree is empty, set root to the new node
        } else {
            insertHelper(node, root);  // Otherwise, call helper method to insert the node
        }
    }

    /**
     * Helper method to insert a new node into the tree.
     * @param newNode the new node to insert.
     * @param subtree the current node in the tree to compare and insert under.
     */
    protected void insertHelper(BinaryTreeNode<T> newNode, BinaryTreeNode<T> subtree) {
        int comparison = newNode.getData().compareTo(subtree.getData());  // Compare the new node's data with current node's data

        // Insert into the left subtree if the new node's data is smaller or equal
        if (comparison <= 0) {
            if (subtree.childLeft() == null) {
                subtree.setChildLeft(newNode);  // Insert the new node if left child is null
            } else {
                insertHelper(newNode, subtree.childLeft());  // Otherwise, recursively insert in the left subtree
            }
        }
        
        // Insert into the right subtree if the new node's data is greater
        if (comparison > 0) {
            if (subtree.childRight() == null) {
                subtree.setChildRight(newNode);  // Insert the new node if right child is null
            } else {
                insertHelper(newNode, subtree.childRight());  // Otherwise, recursively insert in the right subtree
            }
        }
    }

    /**
     * Runs a test to validate the basic functionality of the tree and test the size, isEmpty, and clear methods (test 1).
     * @return true if all tests pass, false otherwise
     */
    public boolean test1() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.insert(4);
        if(tree.size() != 1 && tree.isEmpty() != false) {
            return false;
        }
        tree.insert(5);
        tree.insert(6);
        if(tree.size() != 3 && tree.isEmpty() != false) {
            return false;
        }
        tree.clear();
        if(tree.size() != 0 && tree.isEmpty() != true) {
            return false;
        }
        return true;
    }

    /**
     * Runs a test to validate the insertion and structure of nodes with integers (test 2).
     * @return true if all tests pass, false otherwise
     */
    public boolean test2() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.insert(10);
        tree.insert(7);
        tree.insert(6);
        if(tree.root.childLeft().getData() != 7) {
            return false;
        }
        if(tree.root.childLeft().childLeft().getData() != 6) {
            return false;
        }
        tree.insert(2);
        if(tree.root.childLeft().childLeft().childLeft().getData() != 2) {
            return false;
        }
        tree.clear();
        if(tree.size() != 0 || tree.isEmpty() != true) {
            return false;
        }
        tree.insert(8);
        tree.insert(15);
        tree.insert(11);
        if(tree.root.childRight().getData() != 15) {
            return false;
        }
        if(tree.root.childRight().childLeft().getData() != 11) {
            return false;
        }
        tree.insert(10);
        if(tree.root.childRight().childLeft().childLeft().getData() != 10) {
            return false;
        }
        tree.clear();
        return true;
    }

    /**
     * Runs a test to validate string comparisons and ordering in the tree (test 3).
     * @return true if all tests pass, false otherwise
     */
    public boolean test3() {
        BinarySearchTree<String> tree = new BinarySearchTree<>();
        tree.insert("Pineapple");
        tree.insert("Apple");
        if(tree.root.getData().compareTo(tree.root.childLeft().getData()) <= 0) {
            return false;
        }
        tree.clear();
        if(tree.size() != 0 || tree.isEmpty() != true) {
            return false;
        }
        tree.insert("Orange"); 
        tree.insert("Banana");
        tree.insert("Cucumber");
        tree.insert("Mango");
        if(tree.root.childLeft().childRight().childRight().getData().compareTo("Mango") != 0) {
            return false;
        }
        return true;
    }

    /**
     * Runs a test to validate the contains method (test 4).
     * @return true if all tests pass, false otherwise
     */
    public boolean test4() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.insert(5);
        tree.insert(2);
        tree.insert(3);
        tree.insert(4);
        if(!tree.contains(5) || !tree.contains(2) || !tree.contains(3) || !tree.contains(4)) {
            return false;
        }
        return true;
    }

    /**
     * Main method to run all the test cases.
     */
    // public static void main(String[] args) {
    //     BinarySearchTree<Integer> test = new BinarySearchTree<>();
    //     System.out.println("test1: " + test.test1());
    //     System.out.println("test2: " + test.test2());
    //     System.out.println("test3: " + test.test3());
    //     System.out.println("test4: " + test.test4());
    // }
}