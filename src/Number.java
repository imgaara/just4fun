/**
 * Created by Administrator on 2014/8/1 0001.
 */
public abstract class Number<T extends Number<T>> {
    protected boolean isPositive;
    public boolean isPositive() {
        return isPositive;
    }

    public abstract T add(T rhs);
    public abstract T del(T rhs);
    public abstract T mul(T rhs);
    public abstract T div(T rhs);
    public abstract boolean isGE(T rhs);
    public abstract boolean isGT(T rhs);
    public abstract boolean isAbsGE(T rhs);
    public abstract boolean equals(T rhs);
}
