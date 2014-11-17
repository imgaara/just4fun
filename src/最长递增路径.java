import java.util.*;

/**
 * Created by Administrator on 2014/10/25 0025.
 */
public class 最长递增路径 {

    static class Edge {
        int id;
        int first;
        int second;
        int w;

        public Edge (int id, int first, int second, int w) {
            this.id = id;
            this.first = first;
            this.second = second;
            this.w = w;
        }

        @Override
        public int hashCode(){
            return id;
        }

        public boolean equals(Object rhs) {
            if (rhs instanceof Edge) {
                return ((Edge) rhs).id == id;
            }

            return false;
        }
    }

    static class Info {
        int index = -1;
        public int max = 0;
    }

    static class Context {
        List<Info> f = new ArrayList<Info>();
        Map<Integer, Integer> mapping = new HashMap<Integer, Integer>();

        public void addInfo(Info i, Edge e) {
            i.index = f.size();
            i.max = e.w;
            f.add(i);
            addMapping(e, i);
        }

        public void addPath(Edge e, Info i) {
            addMapping(e, i);
        }

        public void addMapping(Edge e, Info i) {
            Integer last = mapping.get(e.second);
            if (last == null || (i.index > last)) {
                mapping.put(e.second, i.index);
            }
        }
    }
    /**
     *
     * @return
     */
    public int maxIncreasePathLength(List<Edge> edges) {
        Context c = new Context();

        int upgradeCount = 1;
        for (int i = 0; i < edges.size(); ++i) {
            Edge temp = edges.get(i);
            if (i != 0 && edges.get(i).w != edges.get(i - 1).w) {
                upgradeCount = 1;
            }

            Integer otherCanConnectInfos1 = c.mapping.get(temp.first);
            Integer otherCanConnectInfos2 = c.mapping.get(temp.second);

            {
                Edge cur = temp;
                upgradeCount = process(c, cur, upgradeCount, otherCanConnectInfos1);
            }

            {
                Edge cur = new Edge(-temp.id, temp.second, temp.first, temp.w);
                upgradeCount = process(c, cur, upgradeCount, otherCanConnectInfos2);
            }
        }

        return c.f.size();
    }

    private int process(Context c, Edge cur, int upgradeCount, Integer otherCanConnectInfos) {
        if (null == otherCanConnectInfos) {
            if (c.f.size() == 0) {
                Info first = new Info();
                c.addInfo(first, cur);
                upgradeCount--;
            } else {
                c.addPath(cur, c.f.get(0));
            }
        } else {
            Integer index = otherCanConnectInfos;
            Info curInfo = c.f.get(index);
            if (cur.w > curInfo.max) {
                if (index == c.f.size() - 1) {
                    if (upgradeCount > 0) {
                        Info info = new Info();
                        c.addInfo(info, cur);
                        upgradeCount--;
                    }
                } else {
                    int upIndex = index + 1;
                    c.addPath(cur, c.f.get(upIndex));
                }
            }
        }
        return upgradeCount;
    }

    public static void main(String[] args) {
        Scanner ss = new Scanner(System.in);
        final int N = ss.nextInt();
        final int M = ss.nextInt();

        List<Edge> edges = new ArrayList<Edge>();
        for (int i = 0; i < M; ++i) {
            edges.add(new Edge(i + 1, ss.nextInt(), ss.nextInt(), ss.nextInt()));
        }

        Collections.sort(edges, new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
                if (o1.w < o2.w) {
                    return -1;
                } else if (o1.w > o2.w)   {
                    return 1;
                }
                return 0;
            }
        });
        System.out.println(new 最长递增路径().maxIncreasePathLength(edges));
    }
}
