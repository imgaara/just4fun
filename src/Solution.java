import util.UndirectedGraphNode;

import java.util.*;

/**
 * Created by Administrator on 2014/8/25 0025.
 */
public class Solution {
    static class Point {
        int i;
        int j;
        public Point(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }
    public void solve(char[][] board) {
        if (null == board || board.length == 0) {
            return;
        }

        final int m = board.length;
        final int n = board[0].length;
        boolean[][] status = new boolean[m][n];

        Queue<Point> q = new LinkedList<Point>();

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (board[i][j] == 'X') {
                    continue;
                }

                if (status[i][j]) {
                    continue;
                }

                q.add(new Point(i, j));
                List<Point> whiteList = new ArrayList<Point>();
                boolean can = false;
                while (!q.isEmpty()) {
                    Point cur = q.poll();
                    if (isOut(cur, m, n)) {
                        continue;
                    }

                    if (board[cur.i][cur.j] == 'X') {
                        continue;
                    }

                    if (status[cur.i][cur.j]) {
                        continue;
                    }

                    if (isCan(cur, m, n)) {
                        can = true;
                    }

                    status[cur.i][cur.j] = true;
                    whiteList.add(cur);

                    q.offer(new Point(cur.i + 1, cur.j));
                    q.offer(new Point(cur.i, cur.j + 1));
                    q.offer(new Point(cur.i - 1, cur.j));
                    q.offer(new Point(cur.i, cur.j - 1));
                }

                if (!can) {
                    for (Point p : whiteList) {
                        board[p.i][p.j] = 'X';
                    }
                }
            }
        }
    }

    public String countAndSay(int n) {
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();
        s1.append("1");

        while (n-- > 1) {
            int k = 0;
            for (int i = 0; i < s1.length() ; ) {
                if (i != 0 && k != 0 && s1.charAt(i) != s1.charAt(i - 1)) {
                    char num = s1.charAt(i-1);
                    s2.append(k).append(num);
                    k = 0;
                } else {
                    i++;
                    k++;
                }
            }

            s2.append(k).append(s1.charAt(s1.length() - 1));
            StringBuilder temp = s1;
            s1 = s2;
            s2 = temp;
            s2.delete(0, s2.length());
        }

        return s1.toString();
    }

    private boolean isOut(Point p, int m, int n) {
        return p.i < 0 || p.i >= m || p.j < 0 || p.j >= n;
    }

    private boolean isCan(Point p, int m, int n) {
        return p.i == 0 || p.i == m - 1 || p.j == 0 || p.j == n - 1;
    }

    public static void main(String[] args) {
        System.out.println(new Solution().countAndSay(4));
    }
}
