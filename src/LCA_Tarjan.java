import com.sun.corba.se.spi.ior.MakeImmutable;
import util.Interval;
import util.TreeNode;
import util.Utils;

import java.util.*;

/**
 * Created by Administrator on 2014/11/16 0016.
 */
public class LCA_Tarjan {
    int[] parent = new int[100];

    int[] ancestor = new int[100]; //已访问节点集合的祖先

    int[] rank = new int[100];
    int[] visited = new int[100];

    public void makeSet(final int i) {
        parent[i] = i;
        rank[i] = 1;
    }

    public int find(int i) {
        if (parent[i] == i) {
            return i;
        } else {
            parent[i] = find(parent[i]);
            return parent[i];
        }
    }

    public void unin(int x, int y) {
        if (y == 0) {
            return;
        }

        int a = find(x);
        int b = find(y);

        if (a == b) {
            return ;
        } else if (rank[a] > rank[b]) {
            parent[b] = a;
            rank[a] += rank[b];
        } else {
            parent[a] = b;
            rank[b] += rank[a];
        }
    }

    public void LCA(TreeNode r, int[][] q) {
        makeSet(r.val);

        if (r.left != null) {
            LCA(r.left, q);
            unin(r.val, r.left.val);
            ancestor[find(r.val)] = r.val;
        }

        if (r.right != null){
            LCA(r.right, q);
            unin(r.val, r.right.val);
            ancestor[find(r.val)] = r.val;
        }


        visited[r.val] = 1;
        for (int v : q[r.val]) {
            if (visited[v] == 1) {
                System.out.printf("%d, %d, %d \n", r.val, v, ancestor[find(v)]);
            }
        }
    }

    public static void main(String[] args) {
        TreeNode root = Utils.generateTree("1", "2", "3", "4", "5", "6", "7", "8", "9");
        LCA_Tarjan lca = new LCA_Tarjan();
        int[][] q = new int[100][100];

        q[1][0] = 2;
        q[1][1] = 6;
        q[1][2] = 9;

        q[2][0] = 1;

        q[5][0] = 9;
        q[8][0] = 9;

        q[6][0] = 9;
        q[6][1] = 1;

        q[9][0] = 1;
        q[9][1] = 5;
        q[9][2] = 8;
        q[9][3] = 6;
        q[7][0] = 4;
        q[4][0] = 7;

        lca.LCA(root, q);
    }
}
