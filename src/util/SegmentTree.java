package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Administrator on 2014/10/23 0023.
 */
public class SegmentTree<T> {
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

    public static abstract class SegmentTreeNodeValueGenerator<T> {
        public abstract T generate(T left, T right);
    }

    public static <T> SegmentTreeNode<T> build(List<T> arry, SegmentTreeNodeValueGenerator<T> generator) {
        return innerBuild(arry, 0, arry.size() - 1, generator);
    }

    private static <T> SegmentTreeNode<T> innerBuild(List<T> arry, int start, int end, SegmentTreeNodeValueGenerator<T> generator) {
        if (start == end) {
            return new SegmentTreeNode<T>(start, end, generator.generate(arry.get(start), arry.get(end)));
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
    private SegmentTreeNodeValueGenerator<T> generator;
    public SegmentTree(List<T> arry, SegmentTreeNodeValueGenerator<T> generator) {
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

        if (start <= root.start && end >= root.end) {
            return root.value;
        }

        int mid = (root.start + root.end) >>> 1;
        if (end <= mid) {
            return findRangeValue_(root.left, start, end);
        } else if (start > mid) {
            return findRangeValue_(root.right, start, end);
        } else {
            return generator.generate(findRangeValue_(root.left, start, mid), findRangeValue_(root.right, mid + 1, end));
        }
    }

    public static void main(String[] args) {
        Scanner ss = new Scanner(System.in);
        int N = ss.nextInt();
        int Q = ss.nextInt();
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < N; ++i) {
            list.add(ss.nextInt());
        }

        int[] start = new int[Q];
        int[] end = new int[Q];
        for (int i = 0; i < Q; ++i) {
            start[i] = ss.nextInt() - 1;
            end[i] = ss.nextInt() - 1;
        }

        ss.close();

        SegmentTree<Integer> minTree = new SegmentTree<Integer>(list, new SegmentTreeNodeValueGenerator<Integer>() {
            @Override
            public Integer generate(Integer left, Integer right) {
                return Math.min(left, right);
            }
        });

        SegmentTree<Integer> maxTree = new SegmentTree<Integer>(list, new SegmentTreeNodeValueGenerator<Integer>() {
            @Override
            public Integer generate(Integer left, Integer right) {
                return Math.max(left, right);
            }
        });

        for (int i = 0; i < Q; ++i) {
            int max = maxTree.findRangeValue(start[i], end[i]);
            int min =  minTree.findRangeValue(start[i], end[i]);
            System.out.println(max - min);
        }
    }
}
