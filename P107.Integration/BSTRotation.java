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

        // Ensure that the child is actually a child of the parent, otherwise throw an IllegalArgumentException
        if (parent.childLeft() != child && parent.childRight() != child) {
            throw new IllegalArgumentException("Parent and child are not directly related");
        }

        // get the grandparent node
        BinaryTreeNode<T> grandparent = parent.parent();

        // left rotation (parent's right child becomes the new parent)
        if (parent.childRight() == child) {
            if(child.childLeft() == null) {
                parent.setChildRight(null); // no child left subtree means set to null
            }
            else {
                parent.setChildRight(child.childLeft()); // attach child left subtree to parent's right
                child.childLeft().setParent(parent);
            }
            // make current parent left child to current child
            child.setChildLeft(parent);
        } else {
        // right rotation (parent's left child becomes the new parent)
            if(child.childRight() == null) {
                parent.setChildLeft(null); // no child right subtree means set to null
            }
            else {
                parent.setChildLeft(child.childRight()); // attach child right subtree to parent's left
                child.childRight().setParent(parent);
            }
            // make current parent right child to current child
            child.setChildRight(parent);
        }

        // change pointers after rotation
        parent.setParent(child);
        child.setParent(grandparent);

        // change grandparent pointers as necessary
        if (grandparent == null) {
            root = child; // if no grandparent, new root becomes the child
        } else {
            if (grandparent.childRight() == parent) {
                grandparent.setChildRight(child);
            } else {
                grandparent.setChildLeft(child);
            }
        }
    }
}
 