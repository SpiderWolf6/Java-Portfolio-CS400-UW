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
}