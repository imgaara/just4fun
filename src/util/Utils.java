package util;

import java.util.List;

/**
 * Created by Administrator on 2014/7/20 0020.
 */
public class Utils {
    public static void printList(ListNode head) {
        while (null != head) {
            System.out.print(head.val);
            System.out.print(',');
            head = head.next;
        }
        System.out.println();
    }

    public static ListNode genList(int needNum) {
        ListNode head = new ListNode(0, null);
        ListNode tail = head;
        for (int i = 1; i < needNum; ++i) {
            tail.next = new ListNode(i, null);
            tail = tail.next;
        }

        return head;
    }

    public static ListNode genList(int ... args) {
        assert(args.length > 0);
        ListNode head = new ListNode(args[0], null);
        ListNode tail = head;
        for (int i = 1; i < args.length; ++i) {
            tail.next = new ListNode(args[i], null);
            tail = tail.next;
        }

        return head;
    }

    public static void print2DCharArray(char[][] arr) {
        for (int i = 0; i < arr.length; ++i) {
            System.out.println(arr[i]);
        }
    }

    public static char[][] strArrayTo2DArray(String[] strs) {
        char[][] array = new char[strs.length][strs[0].length()];
        for (int i = 0; i < strs.length; ++i) {
            array[i] = strs[i].toCharArray();
        }

        return array;
    }

    public static <T> void printListList(List<List<T>> list) {
        for (List<?> l : list) {
            System.out.println(l);
        }
    }
}
