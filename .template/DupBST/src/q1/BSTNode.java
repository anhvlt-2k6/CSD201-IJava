/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q1;

public class BSTNode {
    public BSTNode() {}

    int data;
    BSTNode left;
    BSTNode right;
    int level;
    int count;
    BSTNode parent;

    public BSTNode(int data) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.level = 0;
        this.count = 1;
        this.parent = null;
    }

    boolean isLeaf() { // check node is leaf
        return left == null && right == null;
    }

    boolean hasLeftChild() { //check node has left child
        return left != null;
    }

    boolean hasRightChild() {  //check node has left child
        return right != null;
    }

    boolean hasOneChild() { // check Node has one child
        return (left != null && right == null) || (left == null && right != null);
    }

    boolean hasTwoChild() { // check Node has two child
        return left != null && right != null;
    }

    boolean hasLeftChildOnly() { //check node has only left
        return left != null && right == null;
    }

    boolean hasRightChildOnly() {  //check node has only left
        return right != null && left == null;
    }

    boolean isRoot() { // check node has root
        return parent == null;
    }

    boolean isInside() { // (internal) node is not a leaf or a root
        return !isLeaf() && !isRoot();
    }

    void setParent(BSTNode parent) { // setter for node parent
        this.parent = parent;
        this.level = this.parent.level + 1;
    }
    
    int getLevel(BSTNode node){
        return node.level;
    }

    void setLeft(BSTNode left) { // set left for node and set left node has parent
        this.left = left;
        if (left != null) {
            this.left.setParent(this);
        }
    }

    void setRight(BSTNode right) { // set right for node and set right node has parent
        this.right = right;
        if (right != null) {
            this.right.setParent(this);
        }
    }

    boolean removeLeafChild(BSTNode node) {
        if (node == null) {
            return false;
        }
        if (node.isLeaf()) {
            if (this.hasLeftChild()) { // check node is left child to remove
                if (this.left.data == node.data) {
                    this.setLeft(null);
                    return true;
                }
            }
             if (this.hasRightChild()) { // check node is right child to remove
                if (this.right.data == node.data) {
                    this.setRight(null);
                    return true;
                }
            }
        }
        return false;
    }
    BSTNode findMinNode(){
        BSTNode currentNode = this;
        while( currentNode.hasLeftChild()){
            currentNode = currentNode.left;
        }
        return currentNode;
    }
    BSTNode findMaxNode(){
        BSTNode currentNode = this;
        while( currentNode.hasRightChild()){
            currentNode = currentNode.right;
        }
        return currentNode;
    }

}
