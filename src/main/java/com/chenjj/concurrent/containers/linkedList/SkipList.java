package com.chenjj.concurrent.containers.linkedList;

import java.util.Random;

/**
 * 关于跳表可以看这篇文章
 * https://mp.weixin.qq.com/s?__biz=MzIxMjE5MTE1Nw==&mid=2653211187&idx=1&sn=c062ab9598cf0af12acbf849478bb0d3&chksm=8c99b8e9bbee31ff9b1c86cfb32030b4cbabc0b98e9be850efe46fffb6eb6bac335f8b2b7b43&mpshare=1&scene=1&srcid=1103aT3YOjQ1ERgtHEgr2mzn&sharer_sharetime=1604406957146&sharer_shareid=58714c43bff8d32784ccc2cb555b2c13&key=db62da5b3886bc562c3330c562bb4277c18aa95b3360c214c529917ed3c370eb12c8e7dd53513b666c02f22de09e3b882da5578565c5cc5241524a01418602cc4b9c4b861476868293cf3240e7c1ff653692d9ef7346a431a1f5597f9e28fd115d1ce17362977b5771d5073a5dd1800b253a435d39196aa9795289661445af41&ascene=1&uin=MzQyOTAzNDA4Nw%3D%3D&devicetype=Windows+10+x64&version=6300002f&lang=zh_CN&exportkey=AX%2Fkp6SlXM7BB%2F9Y749cahg%3D&pass_ticket=%2BForgiDBN8yDTSBbr%2FQizZ5NlX9L9%2BmscwDIWaHg6qijU%2BVKJ9Ru%2FML29jQS56w0&wx_header=0
 */
public class SkipList {
    //结点“晋升”的概率
    private static final double PROMOTE_RATE = 0.5;
    private Node head, tail;
    private int maxLevel;
    // 元素个数
    private int size;

    public SkipList() {
        head = new Node(Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE);
        head.right = tail;
        tail.left = head;
    }

    //查找结点
    public Node search(int data) {
        Node p = findNode(data);
        if (p.data == data) {
            //System.out.println("找到结点：" + data);
            return p;
        }
        //System.out.println("未找到结点：" + data);
        return null;
    }

    //找到值对应的前置结点
    private Node findNode(int data) {
        Node node = head;
        while (true) {
            while (node.right.data != Integer.MAX_VALUE && node.right.data <= data) {
                node = node.right;
            }
            if (node.down == null) {
                break;
            }
            node = node.down;
        }
        return node;
    }

    //插入结点
    public void add(int data) {
        Node preNode = findNode(data);
        //如果data相同，直接返回
        if (preNode.data == data) {
            return;
        }
        Node node = new Node(data);
        appendNode(preNode, node);
        int currentLevel = 0;
        //随机决定结点是否“晋升”
        Random random = new Random();
        while (random.nextDouble() < PROMOTE_RATE) {
            //如果当前层已经是最高层，需要增加一层
            if (currentLevel == maxLevel) {
                addLevel();
            }
            //找到上一层的前置节点
            while (preNode.up == null) {
                preNode = preNode.left;
            }
            preNode = preNode.up;
            //把“晋升”的新结点插入到上一层
            Node upperNode = new Node(data);
            appendNode(preNode, upperNode);
            upperNode.down = node;
            node.up = upperNode;
            node = upperNode;
            currentLevel++;
        }
        size++;
    }

    //在前置结点后面添加新结点
    private void appendNode(Node preNode, Node newNode) {
        newNode.left = preNode;
        newNode.right = preNode.right;
        preNode.right.left = newNode;
        preNode.right = newNode;
    }

    //增加一层
    private void addLevel() {
        maxLevel++;
        Node p1 = new Node(Integer.MIN_VALUE);
        Node p2 = new Node(Integer.MAX_VALUE);
        p1.right = p2;
        p2.left = p1;
        p1.down = head;
        head.up = p1;
        p2.down = tail;
        tail.up = p2;
        head = p1;
        tail = p2;
    }

    //删除结点
    public boolean remove(int data) {
        Node removedNode = search(data);
        if (removedNode == null) {
            return false;
        }

        int currentLevel = 0;
        while (removedNode != null) {
            removedNode.right.left = removedNode.left;
            removedNode.left.right = removedNode.right;
            //如果不是最底层，且只有无穷小和无穷大结点，删除该层
            if (currentLevel != 0 && removedNode.left.data == Integer.MIN_VALUE && removedNode.right.data == Integer.MAX_VALUE) {
                removeLevel(removedNode.left);
            } else {
                currentLevel++;
            }
            removedNode = removedNode.up;
        }
        size--;
        return true;
    }

    //删除一层
    private void removeLevel(Node leftNode) {
        Node rightNode = leftNode.right;
        //如果删除层是最高层
        if (leftNode.up == null) {
            leftNode.down.up = null;
            rightNode.down.up = null;
        } else {
            leftNode.up.down = leftNode.down;
            leftNode.down.up = leftNode.up;
            rightNode.up.down = rightNode.down;
            rightNode.down.up = rightNode.up;
        }
        maxLevel--;
    }

    //输出底层链表
    public void printList() {
        Node node = head;
        while (node.down != null) {
            node = node.down;
        }
        while (node.right.data != Integer.MAX_VALUE) {
           System.out.print(node.right.data + " ");
            node = node.right;
        }
        System.out.println();
    }

    public int getSize() {
        return size;
    }

    //链表结点类
    private class Node {
        public int data;
        //跳表结点的前后和上下都有指针
        public Node up, down, left, right;

        public Node(int data) {
            this.data = data;
        }
    }
}
