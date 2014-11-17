package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Administrator on 2014/10/23 0023.
 */
public class SegmentTree<T, X> {
    public static class SegmentTreeNode<T> {
        int start;
        int end;
        T value;
        SegmentTreeNode<T> left;
        SegmentTreeNode<T> right;

        public SegmentTreeNode(int start, int end, T value) {
            this.start = start;
            this.end = end;
            this.value = value;
        }
    }

    public static interface SegmentTreeNodeValueGenerator<T, X> {
        public T generate(T left, T right);
        public void infoUp(SegmentTreeNode<T> root);
        public void infoDown(SegmentTreeNode<T> root);
        public T build(X one);
    }

    public static <T, X> SegmentTreeNode<T> build(List<X> arry, SegmentTreeNodeValueGenerator<T, X> generator) {
        return innerBuild(arry, 0, arry.size() - 1, generator);
    }

    private static <T, X> SegmentTreeNode<T> innerBuild(List<X> arry, int start, int end, SegmentTreeNodeValueGenerator<T, X> generator) {
        if (start == end) {
            return new SegmentTreeNode<T>(start, end, generator.build(arry.get(start)));
        }

        int mid = (start + end) >>> 1;
        SegmentTreeNode<T> root = new SegmentTreeNode<T>(start, end, null);
        SegmentTreeNode<T> left = innerBuild(arry, start, mid, generator);
        SegmentTreeNode<T> right = innerBuild(arry, mid + 1, end, generator);
        root.left = left;
        root.right = right;
        root.value = generator.generate(left.value, right.value);
        return root;
    }

    private SegmentTreeNode<T> root;
    private SegmentTreeNodeValueGenerator<T, X> generator;
    public SegmentTree(List<X> arry, SegmentTreeNodeValueGenerator<T, X> generator) {
        this.root = build(arry, generator);
        this.generator = generator;
    }

    public T findRangeValue(int start, int end) {
        return findRangeValue_(this.root, start, end);
    }

    public T findRangeValue_(SegmentTreeNode<T> root, int start, int end) {
        if (null == root) {
            return null;
        }

        if (start > root.end || end < root.start) {
            return null;
        }

        T ret;

        if (start <= root.start && end >= root.end) {
            ret = root.value;
            return ret;
        }

        generator.infoDown(root);
        int mid = (root.start + root.end) >>> 1;
        if (end <= mid) {
            ret = findRangeValue_(root.left, start, end);
        } else if (start > mid) {
            ret = findRangeValue_(root.right, start, end);
        } else {
            ret = generator.generate(findRangeValue_(root.left, start, mid), findRangeValue_(root.right, mid + 1, end));
        }
        return ret;
    }

    public static interface Operator<T> {
        public void operate(SegmentTreeNode<T> node);
    }

    public void operate(int start, int end, Operator<T> operator) {
        operate_(root, start, end, operator);
    }

    private void operate_(SegmentTreeNode<T> root, int start, int end, Operator<T> operator) {
        if (null == root) {
            return;
        }

        if (start > root.end || end < root.start) {
            return;
        }


        if (start <= root.start && end >= root.end) {
            operator.operate(root);
            return;
        }

        generator.infoDown(root);
        int mid = (root.start + root.end) >>> 1;
        if (end <= mid) {
            operate_(root.left, start, end, operator);
        } else if (start > mid) {
            operate_(root.right, start, end, operator);
        } else {
            operate_(root.left, start, mid, operator);
            operate_(root.right, mid + 1, end, operator);
        }

        generator.infoUp(root);
    }

}
