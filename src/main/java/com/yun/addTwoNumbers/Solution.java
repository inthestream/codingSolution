package com.yun.addTwoNumbers;

public class Solution {

    public static void main(String[] args) {
        ListNode l1 = new ListNode();
        l1.val = 9;

        ListNode l2 = new ListNode();
        l2.val = 9;

        ListNode l3 = new ListNode();
        l3.val = 9;

        ListNode l4 = new ListNode();
        l4.val = 9;

        ListNode l5 = new ListNode();
        l5.val = 9;

        ListNode l6 = new ListNode();
        l6.val = 9;

        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
        l5.next = l6;


        System.out.println(addTwoNumbers(l1, l5));


    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode head = new ListNode();
        ListNode tail = head;
        boolean isOver = false;

        ListNode l1Node = l1;
        ListNode l2Node = l2;

        if ((l1.val == 0 && l2.val == 0) && (l1.next == null && l2.next == null)) return head;

        while(l1Node != null && l2Node != null) {
            ListNode node = new ListNode();
            node.val = l1Node.val + l2Node.val;

            if (isOver) {
                node.val++;
                isOver = false;
            }

            if (node.val >= 10) {
                node.val = node.val % 10;
                isOver = true;
            }

            tail.next = node;
            tail = node;

            l1Node = l1Node.next;
            l2Node = l2Node.next;
        }

        ListNode additional = l1Node == null ? l2Node : l1Node;

        while (additional != null) {
            ListNode node = new ListNode();
            node.val = additional.val;

            if (isOver) {
                node.val++;
                isOver = false;
            }

            if (node.val >= 10) {
                node.val = node.val % 10;
                isOver = true;
            }

            tail.next = node;
            tail = node;

            additional = additional.next;
        }


        if (isOver) {
            ListNode last = new ListNode();
            last.val = 1;
            tail.next = last;
        }

        return head.next;

    }




    static class ListNode {
        int val;
        ListNode next;

        ListNode() {}

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val; this.next = next;
        }
    }
}


