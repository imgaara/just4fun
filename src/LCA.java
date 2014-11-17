import util.TreeNode;
import util.Utils;

/**
 * Created by Administrator on 2014/11/11 0011.
 */
public class LCA {
    public TreeNode lca(TreeNode root, int a, int b) {
        if (null == root) {
            return null;
        }

        if (root.val == a || root.val == b) {
            return root;
        }

        TreeNode leftLCA = lca(root.left, a, b);
        TreeNode rightLCA = lca(root.right, a, b);
        if (leftLCA != null && rightLCA != null) {
            return root;
        }

        return leftLCA == null ? rightLCA : leftLCA;
    }

    public void tarjan() {
        TreeNode root = Utils.generateTree("1", "2", "3", "4", "5", "6", "7", "8", "9");


    }

    public static void main(String[] args) {
        TreeNode root = Utils.generateTree("1", "2", "3", "4", "5", "6", "7", "8", "9");
        LCA lca = new LCA();
        System.out.println(lca.lca(root, 1, 2).val);
        System.out.println(lca.lca(root, 1, 6).val);
        System.out.println(lca.lca(root, 1, 9).val);
        System.out.println(lca.lca(root, 5, 9).val);
        System.out.println(lca.lca(root, 8, 9).val);
        System.out.println(lca.lca(root, 6, 9).val);
    }
}
