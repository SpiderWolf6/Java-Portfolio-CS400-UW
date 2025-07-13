public class BSTRotation<T extends Comparable<T>> extends BinarySearchTree<T> {
    /**
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a left child of the provided parent, this
     * method will perform a right rotation. When the provided child is a right
     * child of the provided parent, this method will perform a left rotation.
     * When the provided nodes are not related in one of these ways, this
     * method will either throw a NullPointerException: when either reference is
     * null, or otherwise will throw an IllegalArgumentException.
     *
     * @param child is the node being rotated from child to parent position 
     * @param parent is the node being rotated from parent to child position
     * @throws NullPointerException when either passed argument is null
     * @throws IllegalArgumentException when the provided child and parent
     *     nodes are not initially (pre-rotation) related that way
     */
    protected void rotate(BinaryTreeNode<T> child, BinaryTreeNode<T> parent) throws NullPointerException, IllegalArgumentException {
        if (child == null || parent == null) {
            throw new NullPointerException("One of the references is null");
        }
    
        // Right rotation (parent's left child becomes the new parent)
        if (parent.childLeft() == child) {
            // If parent has a parent, update parent's parent (grandparent)
            if (parent.parent() != null) {
                parent.parent().setChildLeft(child);  // Update grandparent's reference to the new parent
                child.setParent(parent.parent());     // Set child to have the grandparent as its parent
            } else {
                root = child;  // If parent is the root, update root to child
                child.setParent(null);  // Root's parent is null
            }
    
            // Perform the actual rotation
            BinaryTreeNode<T> childRightChild = child.childRight();
            child.setChildRight(parent);  // Child takes parent as its right child
            parent.setChildLeft(childRightChild);  // Parent's left child is now child’s right child
    
            // Update the parent of the parent node
            parent.setParent(child);  // Parent now has child as its parent
    
            // If child’s right child was not null, set its parent to parent
            if (childRightChild != null) {
                childRightChild.setParent(parent);
            }
        }
        // Left rotation (parent's right child becomes the new parent)
        else if (parent.childRight() == child) {
            // If parent has a parent, update parent's parent (grandparent)
            if (parent.parent() != null) {
                parent.parent().setChildRight(child);  // Update grandparent's reference to the new parent
                child.setParent(parent.parent());     // Set child to have the grandparent as its parent
            } else {
                root = child;  // If parent is the root, update root to child
                child.setParent(null);  // Root's parent is null
            }
    
            // Perform the actual rotation
            BinaryTreeNode<T> childLeftChild = child.childLeft();
            child.setChildLeft(parent);  // Child takes parent as its left child
            parent.setChildRight(childLeftChild);  // Parent's right child is now child's left child
    
            // Update the parent of the parent node
            parent.setParent(child);  // Parent now has child as its parent
    
            // If child’s left child was not null, set its parent to parent
            if (childLeftChild != null) {
                childLeftChild.setParent(parent);
            }
        } else {
            throw new IllegalArgumentException("Parent and child are not directly related");
        }
    }    

    /**
     * Runs a test to validate if the rotate method is correctly throwing errors (test 1).
     * @return true if all tests pass, false otherwise
     */
    public boolean test1() {
        BSTRotation<Integer> tree = new BSTRotation<>();
        tree.insert(10);
        try {
            tree.rotate(tree.root, tree.root.parent());
            return false;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        } 
        tree.insert(5);
        try {
            tree.rotate(tree.root.childRight(), tree.root);
            return false;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        tree.insert(1);
        try {
            tree.rotate(tree.root.childLeft().childLeft(), tree.root);
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        tree.clear();
        return true;
    }

    /**
     * Runs a test to validate the basic functionality of the rotate method with parent-child pairs at the root (test 2).
     * @return true if all tests pass, false otherwise
     */
    public boolean test2() {
        BSTRotation<Integer> tree = new BSTRotation<>();
        tree.insert(6);
        tree.insert(7);
        tree.rotate(tree.root.childRight(), tree.root);
        if(tree.root.getData() != 7 || tree.root.parent() != null || tree.root.childLeft().getData() != 6 || 
        tree.root.childLeft().parent().getData() != 7 || tree.root.childRight() != null) {
            return false;
        }
        tree.clear();
        tree.insert(9);
        tree.insert(8);
        tree.rotate(tree.root.childLeft(), tree.root);
        if(tree.root.getData() != 8 || tree.root.parent() != null || tree.root.childRight().getData() != 9 || 
        tree.root.childRight().parent().getData() != 8 || tree.root.childLeft() != null) {
            return false;
        }
        return true;
    }

    /**
     * Runs a test to validate the basic functionality of the rotate method with parent-child pairs not at the root, located elsewhere in the tree (test 3).
     * @return true if all tests pass, false otherwise
     */
    public boolean test3() {
        BSTRotation<Integer> tree = new BSTRotation<>();
        tree.insert(11);
        tree.insert(12);
        tree.insert(13);
        tree.insert(14);
        tree.rotate(tree.root.childRight().childRight(), tree.root.childRight());
        if(tree.root.childRight().parent().getData() != 11 || tree.root.childRight().getData() != 13 || 
        tree.root.childRight().childRight().getData() != 14 || tree.root.childRight().childLeft().getData() != 12 || 
        tree.root.childRight().childLeft().parent().getData() != 13) {
            return false;
        }
        tree.clear();
        tree.insert(29);
        tree.insert(28);
        tree.insert(27);
        tree.insert(26);
        tree.rotate(tree.root.childLeft().childLeft(), tree.root.childLeft());
        if(tree.root.childLeft().parent().getData() != 29 || tree.root.childLeft().getData() != 27 || 
        tree.root.childLeft().childRight().getData() != 28 || tree.root.childLeft().childLeft().getData() != 26 || 
        tree.root.childLeft().childRight().parent().getData() != 27) {
            return false;
        }
        return true;
    }

    /**
     * Runs a test to validate the rotate method with more complex parent-child pairs with shared children and nested subtrees (test 4).
     * @return true if all tests pass, false otherwise
     */
    public boolean test4() {
        BSTRotation<Integer> tree = new BSTRotation<>();
        tree.insert(20);
        tree.insert(15);
        tree.insert(25);
        tree.rotate(tree.root.childRight(), tree.root);
        if (tree.root.getData() != 25 || tree.root.childLeft().getData() != 20 || tree.root.childRight() != null) {
            return false;
        }
    
        tree.clear();
    
        tree.insert(30);
        tree.insert(25);
        tree.insert(35);
        tree.insert(22);
        tree.insert(27);
        tree.rotate(tree.root.childLeft(), tree.root);
        if (tree.root.getData() != 25 || tree.root.childLeft().getData() != 22 || 
            tree.root.childRight().getData() != 30 || tree.root.childRight().childLeft().getData() != 27) {
            return false;
        }
    
        tree.clear();
    
        tree.insert(50);
        tree.insert(30);
        tree.insert(70);
        tree.insert(20);
        tree.insert(40);
        tree.insert(60);
        tree.insert(80);
        tree.rotate(tree.root.childLeft(), tree.root);
        if (tree.root.getData() != 30 || tree.root.childLeft().getData() != 20 || 
            tree.root.childRight().getData() != 50 || tree.root.childRight().childLeft().getData() != 40) {
            return false;
        }
        tree.clear();
        return true;
    }
    
    /**
     * Main method to run all the test cases.
     */
    // public static void main(String[] args) {
    //     BSTRotation<Integer> test = new BSTRotation<>();
    //     System.out.println("test1: " + test.test1());
    //     System.out.println("test2: " + test.test2());
    //     System.out.println("test3: " + test.test3());
    //     System.out.println("test4: " + test.test4());
    // }
}
