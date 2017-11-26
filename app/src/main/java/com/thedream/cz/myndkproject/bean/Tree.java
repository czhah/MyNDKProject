package com.thedream.cz.myndkproject.bean;

import com.thedream.cz.myndkproject.utils.PrintUtil;

/**
 * Created by cz on 2017/11/25.
 */

public class Tree {

    private Node rootNode;

    public void insert(int key, double value) {
        Node curNode = new Node(key, value);
        if (rootNode == null) {
            rootNode = curNode;
        } else {
            Node node = rootNode;
            Node parent = rootNode;
            boolean isLeft = true;
            while (node != null) {
                parent = node;
                if (key < node.iData) {
                    node = node.leftNode;
                    isLeft = true;
                } else {
                    node = node.rightNode;
                    isLeft = false;
                }
            }
            if (isLeft) {
                parent.leftNode = curNode;
            } else {
                parent.rightNode = curNode;
            }
        }
    }

    public double find(int key) {
        Node node = findNode(key);
        if (node != null) return node.dData;
        return 0;
    }

    private Node findNode(int key) {
        Node cur = rootNode;
        while (cur != null) {
            if (key == cur.iData) {
                return cur;
            } else if (key < cur.iData) {
                cur = cur.leftNode;
            } else {
                cur = cur.rightNode;
            }
        }
        return null;
    }

    public void delete(int key) {
        if (rootNode == null) return;
        Node parent = rootNode;
        Node cur = rootNode;
        boolean isLeft = true;
        boolean hasKey = false;
        while (cur != null) {
            if (key == cur.iData) {
                hasKey = true;
                break;
            }
            parent = cur;
            if (key < cur.iData) {
                isLeft = true;
                cur = parent.leftNode;
            } else {
                isLeft = false;
                cur = parent.rightNode;
            }
        }

        if (!hasKey) {
            PrintUtil.printCZ("没有找到");
            return;
        }
        if (cur.leftNode == null && cur.rightNode == null) {
            //  没有子节点
            if (cur == rootNode) {
                rootNode = null;
                return;
            }
            if (isLeft) {
                parent.leftNode = null;
            } else {
                parent.rightNode = null;
            }
        } else if (cur.rightNode == null) {
            //  有一个左节点
            if (cur == rootNode) {
                rootNode = rootNode.leftNode;
                rootNode.leftNode = null;
                return;
            }
            if (isLeft) {
                parent.leftNode = cur.leftNode;
            } else {
                parent.rightNode = cur.leftNode;
            }
        } else if (cur.leftNode == null) {
            //  有一个右节点
            if (cur == rootNode) {
                rootNode = rootNode.rightNode;
                rootNode.rightNode = null;
                return;
            }
            if (isLeft) {
                parent.leftNode = cur.rightNode;
            } else {
                parent.rightNode = cur.rightNode;
            }
        } else {
            //  有两个节点
            if (cur == rootNode) {
                //  这里断掉根节点好难判断啊!!!
                Node left = rootNode.leftNode;
                Node right = rootNode.rightNode;
                if (left.leftNode == null && left.rightNode == null) {
                    rootNode = left;
                    rootNode.rightNode = right;
                    return;
                } else if (left.rightNode == null) {
                    rootNode = left;
                    rootNode.rightNode = right;
                    return;
                } else if (left.leftNode == null) {
                    Node rrNode = left.rightNode;
                    rootNode = left;
                    rootNode.rightNode = right;
                    //  获取
                }


                return;
            }


        }

    }

    public void preOrder() {
        if (rootNode == null) return;
        preOrder(rootNode);
    }

    private void preOrder(Node node) {
        if (node != null) {
            PrintUtil.printCZ(node.toString());
            preOrder(node.leftNode);
            preOrder(node.rightNode);
        }
    }

    public void inOrder() {
        if (rootNode == null) return;
        inOrder(rootNode);
    }

    private void inOrder(Node node) {
        if (node != null) {
            inOrder(node.leftNode);
            PrintUtil.printCZ(node.toString());
            inOrder(node.rightNode);
        }
    }


    class Node {
        public int iData;
        public double dData;
        public Node leftNode;
        public Node rightNode;

        public Node(int key, double value) {
            this.iData = key;
            this.dData = value;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "iData=" + iData +
                    ", dData=" + dData +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return iData == node.iData;
        }

        @Override
        public int hashCode() {
            return iData;
        }
    }
}
