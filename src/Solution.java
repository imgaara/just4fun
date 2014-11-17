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


    static class Pair {
        int first;
        int second;

        @Override
        public boolean equals(Object rhs) {
            if (rhs instanceof  Pair) {
                return ((Pair) rhs).first == first && ((Pair) rhs).second == second;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return first * 37 + second;
        }
    }
    public List<List<Integer>> fourSum(int[] num, int target) {
        List<List<Integer>> ret = new ArrayList<List<Integer>>();
        if (num.length < 4) {
            return ret;
        }

        Map<Integer, List<Pair>> map = new HashMap<Integer, List<Pair>>();
        final int n = num.length;
        Arrays.sort(num);

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < i; ++j) {
                Pair p = new Pair();
                p.first = i;
                p.second = j;
                int sum = num[i] + num[j];
                List<Pair> list = map.get(sum);
                if (null == list) {
                    list = new ArrayList<Pair>();
                    map.put(sum, list);
                }
                list.add(p);
            }
        }

        Set<String> s = new HashSet<String>();
        for (Map.Entry<Integer, List<Pair>> entry : map.entrySet()) {
            int a = entry.getKey();
            int b = target - a;
            List<Pair> otherList = map.get(b);
            if (null != otherList) {
                for (int i = 0; i < entry.getValue().size(); ++i) {
                    for (int j = 0; j < otherList.size(); ++j) {
                        Pair x = entry.getValue().get(i);
                        Pair y = otherList.get(j);
                        if (x.equals(y)) {
                            continue;
                        } else if (x.first == y.first || x.second == y.second || x.first == y.second || x.second == y.first) {
                            continue;
                        }

                        List<Integer> one = Arrays.asList(num[x.first], num[x.second], num[y.first], num[y.second]);
                        Collections.sort(one);
                        String key = tokey(one.get(0), one.get(1), one.get(2), one.get(3));
                        if (s.contains(key)) {
                            continue;
                        }
                        s.add(key);
                        ret.add(one);
                    }
                }
            }
        }

        return ret;
    }

    private String tokey(int a, int b, int c, int d) {
        StringBuilder sb = new StringBuilder();
        sb.append(a).append("-").append(b).append("-").append(c).append("-").append(d);
        return sb.toString();
    }


    public String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) {
            return "";
        }

        if (strs.length == 1) {
            return strs[0];
        }

        StringBuilder sb = new StringBuilder();

        int i = 0;
        while (true) {
            Character tag = null;
            for (int j = 0; j < strs.length; ++j) {
                String cur = strs[j];
                if (i >= cur.length()) {
                    return sb.toString();
                }

                if (null == tag) {
                    tag = cur.charAt(i);
                }

                if (tag != cur.charAt(i)) {
                    return sb.toString();
                }
            }
            sb.append(tag);
            ++i;
        }
    }

    public static void main(String[] args) {
        System.out.println(new Solution().fourSum(new int[] {1,0,-1,0,-2,2}, 0));
    }
}
