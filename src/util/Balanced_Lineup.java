package util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Administrator on 2014/10/24 0024.
 */
public class Balanced_Lineup {
    public static class SegmentTree<T, X> {
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

        public static abstract class SegmentTreeNodeValueGenerator<T, X> {
            public abstract T generate(T left, T right);
            public abstract T build(X one);
        }

        public static <T, X> SegmentTreeNode<T> build(List<X> arry, SegmentTreeNodeValueGenerator<T, X> generator) {
            return innerBuild(arry, 0, arry.size() - 1, generator);
        }

        private static <T, X> SegmentTreeNode<T> innerBuild(List<X> arry, int start, int end, SegmentTreeNodeValueGenerator<T, X> generator) {
            if (start == end) {
                return new SegmentTreeNode<T>(start, end, generator.generate(generator.build(arry.get(start)), generator.build(arry.get(end))));
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
    }

    static class Pair extends AbstractMap.SimpleEntry<Integer, Integer> {
        public Pair(int a, int b) {
            super(a, b);
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

        SegmentTree<Pair, Integer> minMaxTree = new SegmentTree<Pair, Integer>(list, new SegmentTree.SegmentTreeNodeValueGenerator<Pair, Integer>() {
            @Override
            public Pair generate(Pair left, Pair right) {
                return new Pair(Math.min(left.getKey(), right.getKey()), Math.max(left.getValue(), right.getValue()));
            }

            @Override
            public Pair build(Integer one) {
                return new Pair(one, one);
            }
        });

        int[] r = new int[Q];
        for (int i = 0; i < Q; ++i) {
            Pair p = minMaxTree.findRangeValue(start[i], end[i]);
            r[i] = p.getValue() - p.getKey();
        }

        for (int i = 0; i < Q; ++i) {
            System.out.println(r[i]);
        }
    }
}
