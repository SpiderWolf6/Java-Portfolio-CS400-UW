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
                newNode.setParent(subtree); // Set the parent of the new node
            } else {
                insertHelper(newNode, subtree.childLeft());  // Otherwise, recursively insert in the left subtree
            }
        }

        // Insert into the right subtree if the new node's data is greater
        if (comparison > 0) {
            if (subtree.childRight() == null) {
                subtree.setChildRight(newNode);  // Insert the new node if right child is null
                newNode.setParent(subtree); // Set the parent of the new node
            } else {
                insertHelper(newNode, subtree.childRight());  // Otherwise, recursively insert in the right subtree
            }
        }
    }
}