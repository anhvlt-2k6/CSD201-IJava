/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Q15;

import java.io.File;
import java.io.FileWriter;
import static java.lang.Math.sqrt;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class BSTTree {

    BSTNode root;
    int snode;
    int length;
    String resultPreOder = "";
    String resultInOder = "";
    String resultPostOrder = "";
    String resultLeaf = "";
    String resultInternal = "";
    String resultExternal = "";
    String resultPreEven = "";
    String resultPreOdd = "";
    String resultPrime = "";
    String resultDFS = "";
    String resultBFS = "";
    String resultFindNode = "";
    String resultDup = "";
    String resultOnlyLeftChild = "";
    String resultOnlyRightChild = "";
    String resultOnlyOneChild = "";
    String resultPreGreater = "";
    String resultPreOderLevel = "";
    String resultnodeHaveChild = "";
    String resultPreHeight = "";
    String resultSibling = "";
    String resultPreOrderOrder = "";
    String resultFullBT = "";

    public BSTTree() {
        this.root = null;
    }

    void addNode(int data) {
        if (root == null) {
            root = new BSTNode(data);

        } else {
            BSTNode currentNode = root;
            boolean isAdded = false;
            while (!isAdded) {
                if (data < currentNode.data) {
                    if (currentNode.hasLeftChild()) {
                        currentNode = currentNode.left;
                    } else {
                        BSTNode newNode = new BSTNode(data);
                        currentNode.setLeft(newNode);
                        newNode.setParent(currentNode);
                        isAdded = true;
                    }

                } else if (data > currentNode.data) {
                    if (currentNode.hasRightChild()) {
                        currentNode = currentNode.right;
                    } else {
                        BSTNode newNode = new BSTNode(data);
                        currentNode.setRight(newNode);
                        newNode.setParent(currentNode);
                        isAdded = true;

                    }

                } else {
//                    currentNode.count = currentNode.count + 1; // if NO DUPLICATE, we comment it
                    isAdded = true;
                }
            }
        }
    }

    boolean removeNode(int data) {
        BSTNode current = findNode(data);
        return removeNode(current);
    }

    boolean removeNode(BSTNode current) {
        if (current == null) {
            return false;
        }
        current.count = current.count - 1;
        if (current.count == 0) {
            if (current.isLeaf()) {
                current.parent.removeLeafChild(current);
                return true;
            } else {
                BSTNode temp = null;
                if (current.hasLeftChild()) {
                    temp = current.left.findMaxNode();
//                if (current.hasRightChild()) {            //Nếu thay bằng con nhỏ nhất bên phải
//                    temp = current.right.findMinNode();   //Nếu thay bằng con nhỏ nhất bên phải
                } else {
                    temp = current.right.findMinNode();
//                    temp = current.left.findMaxNode();
                }
                current.data = temp.data;
                current.count = temp.count;
                temp.count = 1;
                return removeNode(temp);
            }
        } else {
            return true;
        }
    }

    boolean isFullTree(BSTNode currentNode) {
        if (currentNode == null) {
            return true;
        }
        if (currentNode.isLeaf()) {
            return true;
        }
        if (currentNode.hasTwoChild()) {
            return (isFullTree(currentNode.left) && isFullTree(currentNode.right)); //is full
        }
        return false;
    }

    boolean isPrime(int data) {
        if (data > 1) {
            if (data < 4) {
                return true;
            } else {
                for (int i = 1; i <= sqrt(data); i++) {
                    if (data % i == 0) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    void findPrime(BSTNode currentNode) {

        if (currentNode == null) {
            return;
        }
        if (isPrime(currentNode.data)) {
            for (int i = 0; i < currentNode.count; i++) {
                System.out.println(currentNode.data);
                resultPrime += "," + currentNode.data;
            }
        }
        findPrime(currentNode.left);
        findPrime(currentNode.right);
    }

    void findPrime() {
        findPrime(root);
    }

    void DFS() {
        Stack<BSTNode> s = new Stack<>();
        s.add(root);
        BSTNode currentNode;
        while (!s.isEmpty()) {
            currentNode = s.pop();
            if (currentNode != null) {
                for (int i = 0; i < currentNode.count; i++) {
                    System.out.println(currentNode.data);
                    resultDFS += "," + currentNode.data;
                }
                s.add(currentNode.right);
                s.add(currentNode.left);
            }

        }
    }

    void BFS() {
        Queue<BSTNode> s = new LinkedList<>();
        s.add(root);
        BSTNode currentNode;
        while (!s.isEmpty()) {
            currentNode = s.poll();
            if (currentNode != null) {

                for (int i = 0; i < currentNode.count; i++) {
                    System.out.println(currentNode.data);
                    resultBFS += "," + currentNode.data;
                }
                s.add(currentNode.left);
                s.add(currentNode.right);

            }

        }

    }

    BSTNode findNode(int data) {
        BSTNode currentNode = root;
        length = 0;
        while (currentNode != null) {
            resultFindNode += "->" + currentNode.data;
            length++;
            if (data < currentNode.data) {
                currentNode = currentNode.left;
            } else if (data > currentNode.data) {
                currentNode = currentNode.right;
            } else {
                return currentNode;
            }
        }
        resultFindNode = "  No"; // Notice Substring;
        return null;
    }

    void preOder(BSTNode currentNode) {
        if (currentNode == null) {
            return;
        }
        for (int i = 0; i < currentNode.count; i++) {
            System.out.println(currentNode.data);
            resultPreOder += "," + currentNode.data;
        }
        preOder(currentNode.left);
        preOder(currentNode.right);
    }

    void postOrder(BSTNode currentNode) {
        if (currentNode == null) {
            return;
        }
        postOrder(currentNode.left);
        postOrder(currentNode.right);
        for (int i = 0; i < currentNode.count; i++) {
            System.out.println(currentNode.data);
            resultPostOrder += "," + currentNode.data;
        }
    }

    void inOrder(BSTNode currentNode) {
        if (currentNode == null) {
            return;
        }

        inOrder(currentNode.left);
        for (int i = 0; i < currentNode.count; i++) {
            System.out.println(currentNode.data);
            resultInOder += "," + currentNode.data;
        }
        inOrder(currentNode.right);
    }

    void inOrder() {
        inOrder(root);
    }

    void postOrder() {
        postOrder(root);
    }

    void preOrder() {
        preOder(root);
    }

    void preOderLevel(BSTNode currentNode) {
        if (currentNode == null) {
            return;
        }
        for (int i = 0; i < currentNode.count; i++) {
            System.out.println(currentNode.data);
            resultPreOderLevel += "," + currentNode.data + "[" + currentNode.level + "]";
        }
        preOderLevel(currentNode.left);
        preOderLevel(currentNode.right);
    }

    void preOrderSub(int data) { //Preorder of subtree with root as data
        resultPreOder = "";
        preOder(findNode(data));
    }

    void findLeafNode(BSTNode currentNode) {

        if (currentNode == null) {
            return;
        }
        if (currentNode.isLeaf()) {
            for (int i = 0; i < currentNode.count; i++) {
                System.out.println(currentNode.data);
                resultLeaf += "," + currentNode.data;
            }
        }
        findLeafNode(currentNode.left);
        findLeafNode(currentNode.right);
    }

    void findLeafNode() {
        findLeafNode(root);
    }

    void findInternalNode(BSTNode currentNode) {

        if (currentNode == null) {
            return;
        }
        if (currentNode.isInside()) {
            for (int i = 0; i < currentNode.count; i++) {
                System.out.println(currentNode.data);
                resultInternal += "," + currentNode.data;
            }
        }
        findInternalNode(currentNode.left);
        findInternalNode(currentNode.right);
    }

    void findInternalNode() {
        findInternalNode(root);
    }

    void findExternalNode(BSTNode currentNode) {

        if (currentNode == null) {
            return;
        }
        if (!currentNode.isInside()) {
            for (int i = 0; i < currentNode.count; i++) {
                System.out.println(currentNode.data);
                resultExternal += "," + currentNode.data;
            }
        }
        findExternalNode(currentNode.left);
        findExternalNode(currentNode.right);
    }

    void findExternalNode() {
        findExternalNode(root);
    }

    void findPreEven(BSTNode currentNode) { //Preorder only Even

        if (currentNode == null) {
            return;
        }
        if (currentNode.data % 2 == 0) {
            for (int i = 0; i < currentNode.count; i++) {

                System.out.println(currentNode.data);
                resultPreEven += "," + currentNode.data;
            }
        }

        findPreEven(currentNode.left);
        findPreEven(currentNode.right);
    }

    void findPreEven() {
        findPreEven(root);
    }

    void findPreOdd(BSTNode currentNode) { //Preorder only Odd

        if (currentNode == null) {
            return;
        }
        if (currentNode.data % 2 == 0) {
            for (int i = 0; i < currentNode.count; i++) {

                System.out.println(currentNode.data);
                resultPreOdd += "," + currentNode.data;
            }
        }

        findPreOdd(currentNode.left);
        findPreOdd(currentNode.right);
    }

    void findPreOdd() {
        findPreOdd(root);
    }

    void findPreGreater(BSTNode currentNode, int k) { //Preorder only Even

        if (currentNode == null) {
            return;
        }
        if (currentNode.data > k) {
            for (int i = 0; i < currentNode.count; i++) {

                System.out.println(currentNode.data);
                resultPreGreater += "," + currentNode.data;
            }
        }

        findPreGreater(currentNode.left, k);
        findPreGreater(currentNode.right, k);
    }

    void findPreGreater(int k) {
        findPreGreater(root, k);
    }

    void findDup(BSTNode currentNode) {

        if (currentNode == null) {
            return;
        }
        if (currentNode.count > 1) {
            for (int i = 0; i < currentNode.count; i++) {

                System.out.println(currentNode.data);
                resultDup += "," + currentNode.data;
            }
        }
        findDup(currentNode.left);
        findDup(currentNode.right);
    }

    void findDup() {
        findDup(root);
    }

    void findOnlyLeftChild(BSTNode currentNode) {

        if (currentNode == null) {
            return;
        }
        if (currentNode.hasLeftChildOnly()) {
            for (int i = 0; i < currentNode.count; i++) {

                System.out.println(currentNode.data);
                resultOnlyLeftChild += "," + currentNode.data;
            }
        }
        findOnlyLeftChild(currentNode.left);
        findOnlyLeftChild(currentNode.right);
    }

    void findOnlyLeftChild() {
        findOnlyLeftChild(root);
    }

    void findOnlyRightChild(BSTNode currentNode) {

        if (currentNode == null) {
            return;
        }
        if (currentNode.hasRightChildOnly()) {
            for (int i = 0; i < currentNode.count; i++) {

                System.out.println(currentNode.data);
                resultOnlyRightChild += "," + currentNode.data;
            }
        }
        findOnlyRightChild(currentNode.left);
        findOnlyRightChild(currentNode.right);
    }

    void findOnlyRightChild() {
        findOnlyRightChild(root);
    }
    
    void findOnlyOneChild(BSTNode currentNode) {
        if (currentNode == null) {
            return;
        }
        if (currentNode.hasRightChildOnly()||currentNode.hasLeftChildOnly()) {
            for (int i = 0; i < currentNode.count; i++) {
                System.out.println(currentNode.data);
                resultOnlyOneChild += "," + currentNode.data;
            }
        }
        findOnlyOneChild(currentNode.left);
        findOnlyOneChild(currentNode.right);
    }

    void findOnlyOneChild() {
        findOnlyOneChild(root);
    }


    void nodeHaveOneChild(BSTNode currentNode) {
        if (currentNode == null) {
            return;
        }
        nodeHaveOneChild(currentNode.left);
        if (currentNode.hasOneChild()) {
            for (int i = 0; i < currentNode.count; i++) {

                System.out.println(currentNode.data);
                resultnodeHaveChild += "," + currentNode.data;
            }
        }
        nodeHaveOneChild(currentNode.right);
    }

    int height(BSTNode currentNode) {
        if (currentNode == null) {
            return -1;
        }
        int max1 = height(currentNode.left);
        int max2 = height(currentNode.right);
        if (max1 < max2) {
            return max2 + 1;
        } else {
            return max1 + 1;
        }
    }

    void preHeight(BSTNode currentNode) {
        if (currentNode == null) {
            return;
        }
        for (int i = 0; i < currentNode.count; i++) {
            System.out.println(height(currentNode));
            resultPreHeight += "," + currentNode.data + "[" + height(currentNode) + "]";
        }
        preHeight(currentNode.left);
        preHeight(currentNode.right);
    }
    
    void preOrderOrder(BSTNode currentNode, int index) {
        if (currentNode == null) {
            return;
        }
        for (int i = 0; i < currentNode.count; i++) {
            System.out.println(index);
            resultPreOrderOrder += "," + currentNode.data + "[" + index + "]";
        }
        preOrderOrder(currentNode.left,2*index+1);
        preOrderOrder(currentNode.right,2*(index+1));
    }    

    int findMaxNode() {
        return root.findMaxNode().data;
    }

    int findMinNode() {
        return root.findMinNode().data;
    }

    void siblingNode(int a) {
        BSTNode currentNode = findNode(a);
        if (currentNode == null || currentNode.parent == null) {
            resultSibling += ",null";
        } else {            
            if (currentNode.parent.left == currentNode) {
                if (currentNode.parent.right == null) {
                    resultSibling += ",null";
                } else {
                    resultSibling += ","+currentNode.parent.right.data;
                }
            } else if (currentNode.parent.right == currentNode) {
                if (currentNode.parent.left == null) {
                    resultSibling += ",null";
                } else {
                    resultSibling += ","+currentNode.parent.left.data;
                }
            }
        }
    }

    void writeDataToFile(String fileName) {
//        System.out.println();
//        preOrder(); 
//        inOrder();
//        postOrder();
//        BFS();
//        DFS();
//        findLeafNode();
//        findInternalNode();
//        findPrime();
//        findPreEven();
//        findDup();
//        findOnlyLeftChild();
//        findOnlyRightChild();
//        findOnlyOneChild();
//        findPreGreater(2);
//        preOrderSub(4);
//        findNode(snode);
//        preOderLevel(root);
//        preOder(findNode(9));
//        nodeHaveOneChild(root);
//        System.out.println(height(findNode(7)));
//        preHeight(root);
//        preOrderOrder(root,0);
//        BSTNode n = siblingNode(1);
//        if (n != null) {
//            System.out.println(n.data);
//        }


        try {
            FileWriter out = new FileWriter(new File(fileName));
//            out.write(resultSibling.substring(1));
//            out.write(resultPreOder.substring(1) + "\r\n");
//            out.write(resultPostOrder.substring(1) + "\n");
//            out.write(resultInOder.substring(1) + "\n");
//            out.write(resultBFS.substring(1) + "\r\n");
//            out.write(resultDFS.substring(1) + "\r\n");
//            out.write(resultLeaf.substring(1) + "\n");
//            out.write(resultInternal.substring(1) + "\n");
//            out.write(resultPrime.substring(1) + "\n");
//            out.write(resultPreEven.substring(1) + "\n");
//            out.write(resultDup.substring(1) + "\n");
//            out.write(resultOnlyLeftChild.substring(1) + "\n");
//            out.write(resultOnlyRightChild.substring(1) + "\n");
//            out.write(resultOnlyOneChild.substring(1));
//            out.write(resultPreGreater.substring(1) + "\n");
//            out.write(resultPreOder.substring(1) + "\r\n"); //For Pre subtree
//            out.write(String.valueOf(length-1)+"\n");
//            out.write(resultFindNode.substring(2) + "\r\n"); 
//            out.write(resultPreOderLevel.substring(1) + "\r\n"); 
//            out.write(resultnodeHaveChild.substring(1));
//            out.write(height(findNode(7))+"");
//            out.write(resultPreHeight.substring(1));
//            out.write(resultPreOrderOrder .substring(1));
//            System.out.println(findMaxNode());
//            out.write(findMaxNode() + "\n");
//            System.out.println(findMinNode());
//            out.write(findMinNode() + "");
//            System.out.println(resultSibling);
//            System.out.println("null");
            
            out.write(isFullTree(root) ? "Yes" : "No");
            out.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void readDataFile(String fileName) {
        try {
            File input = new File(fileName);
            Scanner sc = new Scanner(input);
            //Read for insert
            int n = sc.nextInt();
            for (int i = 0; i < n; i++) {
                int data = sc.nextInt();
                addNode(data);
            }
//            snode = sc.nextInt();       //Đọc để tìm nút            
            //read for sibling
//            int m = sc.nextInt();
//            for (int i = 0; i < m; i++) {
//                int data = sc.nextInt();
//                siblingNode(data);
//            }

            //Read for delete
//            int m = sc.nextInt();
//            for (int i = 0; i < m; i++) {
//                int data = sc.nextInt();
//                removeNode(data);
//            }
            sc.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
