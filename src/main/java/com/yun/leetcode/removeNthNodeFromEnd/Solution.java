package com.yun.leetcode.removeNthNodeFromEnd;

import java.util.ArrayList;
import java.util.List;


public class Solution {

    /**
     * Runtime 2 ms Beats 6.61% Memory 42.9 MB Beats 5.39%
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null) return new ListNode();

        List<ListNode> arr = new ArrayList<>();
        ListNode end;
        int sz = 0;

        end = head;
        arr.add(end);

        sz++;
        while (end.next != null) {
            arr.add(end.next);
            end = end.next;
            sz++;
        }

        if (sz == 1) {
            return null;
        }

        if (sz == n) {
            return head.next;
        }

        int prev = sz - n - 1;
        int next = sz - n + 1;

        if (next == sz) {
            arr.get(prev).next = null;
        } else {
            arr.get(prev).next = arr.get(next);
        }

        return head;
    }

    public static void main(String[] args) {
        Solution s = new Solution();

        //ListNode n5 = s.new ListNode(5, null);
        //ListNode n4 = s.new ListNode(4, n5);
        //ListNode n3 = s.new ListNode(3, n4);
        ListNode n2 = s.new ListNode(2, null);
        ListNode n1 = s.new ListNode(1, n2);

        s.removeNthFromEnd(n1, 2);

    }

    public class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
}
