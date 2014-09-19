package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/7/21 0021.
 */
public class UndirectedGraphNode {
    public int label;
    public List<UndirectedGraphNode> neighbors;
    public UndirectedGraphNode(int x) { label = x; neighbors = new ArrayList<UndirectedGraphNode>(); }
}
