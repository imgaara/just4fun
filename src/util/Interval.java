package util;

/**
 * Created by Administrator on 2014/7/27 0027.
 */
public class Interval {
    public int start;
    public int end;
    public Interval() { start = 0; end = 0; }
    public Interval(int s, int e) { start = s; end = e; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append(start);
        sb.append(',');
        sb.append(end);
        sb.append(']');

        return sb.toString();
    }
}
