package util;

import java.util.*;

/**
 * Created by Administrator on 2014/10/24 0024.
 */
public class Main {
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

    static class SumEntry {
        private long sum;
        private int size;
        private long extra;

        public SumEntry(long sum, int size, long extra) {
            this.sum = sum;
            this.size = size;
            this.extra = extra;
        }
    }

    static class AddOperator implements SegmentTree.Operator<SumEntry> {
        private long addNum;
        public void setAddNum(long addNum){
            this.addNum = addNum;
        }

        @Override
        public void operate(SegmentTree.SegmentTreeNode<SumEntry> node) {
            long add = node.value.size * addNum;
            node.value.sum += add;
            node.value.extra += addNum;
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
        long[] c = new long[Q];
        boolean[] o = new boolean[Q];
        for (int i = 0; i < Q; ++i) {
            String token = ss.next();
            start[i] = ss.nextInt() - 1;
            end[i] = ss.nextInt() - 1;
            if ("C".equals(token)) {
                c[i] = ss.nextLong();
                o[i] = true;
            } else {
                o[i] = false;
            }
        }

        ss.close();

        SegmentTree<SumEntry, Integer> sumTree = new SegmentTree<SumEntry, Integer>(list, new SegmentTree.SegmentTreeNodeValueGenerator<SumEntry, Integer>() {
            @Override
            public SumEntry generate(SumEntry left, SumEntry right) {
                return new SumEntry(left.sum + right.sum, left.size + right.size, 0);
            }

            @Override
            public void infoUp(SegmentTree.SegmentTreeNode<SumEntry> root) {
                if (root.start != root.end) {
                    root.value.sum = root.left.value.sum + root.right.value.sum;
                }
            }

            @Override
            public void infoDown(SegmentTree.SegmentTreeNode<SumEntry> root) {
                if (root.value.extra == 0) {
                    return;
                }

                if (root.start != root.end) {
                    long add = root.value.extra;
                    long leftAdd = root.left.value.size * add;
                    long rightAdd = root.right.value.size * add;

                    root.left.value.extra += add;
                    root.left.value.sum += leftAdd;
                    root.right.value.extra += add;
                    root.right.value.sum += rightAdd;
                }
                root.value.extra = 0;
            }

            @Override
            public SumEntry build(Integer one) {
                return new SumEntry(one, 1, 0);
            }
        });

        long[] r = new long[Q];
        AddOperator addOperator = new AddOperator();
        for (int i = 0; i < Q; ++i) {
            if (o[i]) {
                if (c[i] != 0) {
                    addOperator.setAddNum(c[i]);
                    sumTree.operate(start[i], end[i], addOperator);
                }
            } else {
                SumEntry p = sumTree.findRangeValue(start[i], end[i]);
                r[i] = p.sum;
            }
        }

        for (int i = 0; i < Q; ++i) {
            if (o[i]) {
                continue;
            }
            System.out.println(r[i]);
        }
    }
}
