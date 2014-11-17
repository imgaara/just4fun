package util;

/**
 * Created by Administrator on 2014/8/3 0003.
 */
public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;
    public TreeNode(int x) { val = x; }


    static interface TreeIterator {
        TreeNode next();
        boolean hasNext();
    }

    static class PreOrderTreeIterator implements TreeIterator {
        private TreeNode root;
        private TreeNode cur;
        private TreeNode next;
        private TreeNode prev;

        public PreOrderTreeIterator(TreeNode root) {
            this.root = root;
            cur = root;
            next = fetchNext();
        }

        @Override
        public TreeNode next() {
            TreeNode r = next;
            next = fetchNext();
            return r;
        }

        private TreeNode fetchNext() {
            TreeNode r = null;
            while (r == null && cur != null) {
                if (null == cur.left) {
                    r = cur;
                    prev = cur;
                    cur = cur.right;
                } else {
                    TreeNode node = cur.left;
                    while (node.right != null && node.right != cur) {
                        node = node.right;
                    }

                    if (node.right == null) {
                        node.right = cur;
                        r = cur;
                        prev = cur;
                        cur = cur.left;
                    } else {
                        node.right = null;
                        cur = cur.right;
                    }
                }
            }

            return r;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }
    }


    public static void main(String[] args) {
        TreeNode root = Utils.generateTree("1", "2");
        TreeIterator iterator = new PreOrderTreeIterator(root);
        while (iterator.hasNext()) {
            System.out.println(iterator.next().val);
        }
    }
}
