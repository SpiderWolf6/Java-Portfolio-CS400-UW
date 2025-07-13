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
}