import java.util.Arrays;
import java.util.BitSet;

/**
 * Created by Administrator on 2014/8/1 0001.
 */
public class BigInteger extends Number<BigInteger> {
    private int[] numberArray;
    private int len;
    private static final int MAX_LEN_PER_SEG = 9;
    private static final int MAX_INT_PER_SEG = 999999999;
    private static final long LARGE_BASE = MAX_INT_PER_SEG + 1;
    private static final int BASE = MAX_INT_PER_SEG / 10 + 1;

    public BigInteger(String str) {
        if (null == str || str.length() == 0) {
            throw new RuntimeException("not valid number " + str);
        }

        int idx = str.indexOf('-');
        isPositive = idx == -1;
        if (!isPositive) {
            idx++;
        } else {
            idx = 0;
            while (str.charAt(idx) == ' ' || str.charAt(idx) == '\t' || str.charAt(idx) == '0');
        }

        int segNumber = (str.length() - idx) / MAX_LEN_PER_SEG + 1;
        int[] tempArray = new int[segNumber];

        for (int i = idx; i < str.length(); ++i) {
            char ch = str.charAt(i);
            if (ch < '0' || ch > '9') {
                throw new RuntimeException("not valid number " + str);
            }

            int c = ch - '0';
            for (int k = tempArray.length - 1; k >= 0; --k) {
                int cur = tempArray[k];
                if (cur >= BASE) {
                    tempArray[k] = (cur % BASE) * 10 + c;
                    c = cur / BASE;
                } else {
                    tempArray[k] = cur * 10 + c;
                    break;
                }
            }
        }

        this.numberArray = tempArray;
        len = numberArray.length;
    }

    private BigInteger () {

    }

    private BigInteger initWithArr(int[] arr, int len) {
        this.numberArray = arr;
        this.len = len;
        return this;
    }

    private BigInteger setPositive(boolean positive) {
        isPositive = positive;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!isPositive) {
            sb.append('-');
        }

        for (int i = numberArray.length - len; i < numberArray.length; ++i) {
            sb.append(numberArray[i]);
        }

        return sb.toString();
    }

    @Override
    public BigInteger add(BigInteger rhs) {
        if (this.isPositive && rhs.isPositive || !this.isPositive && !rhs.isPositive) {
            int[] newA = new int [Math.max(this.numberArray.length, rhs.numberArray.length) + 1];

            long c = 0L;
            int i = numberArray.length - 1;
            int j = rhs.numberArray.length - 1;
            int k = newA.length - 1;
            while (i >= 0 && j >= 0) {
                long curSum = numberArray[i] + rhs.numberArray[j] + c;
                newA[k] = (int) (curSum % LARGE_BASE);
                c = curSum / LARGE_BASE;
                --i;
                --j;
                --k;
            }

            while (i >= 0) {
                long curSum = numberArray[i] + c;
                newA[k] = (int) (curSum % LARGE_BASE);
                c = curSum / LARGE_BASE;
                --i;
                --k;
            }

            while (j >= 0) {
                long curSum = rhs.numberArray[j] + c;
                newA[k] = (int) (curSum % LARGE_BASE);
                c = curSum / LARGE_BASE;
                --j;
                --k;
            }

            if (c > 0) {
                newA[k] = (int)c;
            }

            return new BigInteger().initWithArr(newA, c > 0 ? newA.length: newA.length - 1).setPositive(isPositive);
        } else {
            if (isGE(rhs)) {
                return del(rhs.reverse());
            } else {
                return rhs.del(reverse());
            }
        }
    }

    private BigInteger reverse() {
        return new BigInteger().initWithArr(numberArray, len).setPositive(!isPositive);
    }

    @Override
    public BigInteger del(BigInteger rhs) {
        if (isPositive && rhs.isPositive) {
            if (isGE(rhs)) {
                int[] newA = new int [Math.max(this.numberArray.length, rhs.numberArray.length)];
                long c = 0L;
                int i = numberArray.length - 1;
                int j = rhs.numberArray.length - 1;
                int k = newA.length - 1;
                while (i >= 0 && j >= 0) {
                    newA[k] = (int) (numberArray[i] - c - rhs.numberArray[j]);
                    if (newA[k] < 0) {
                        newA[k] += BASE;
                        c = 1;
                    } else {
                        c = 0;
                    }
                    --i;
                    --j;
                    --k;
                }

                while (i >= 0) {
                    newA[k] = (int) (numberArray[i] - c);
                    if (newA[k] < 0) {
                        newA[k] += BASE;
                        c = 1;
                    } else {
                        c = 0;
                    }
                    --i;
                    --k;
                }

                for (k = 0; k < newA.length - 1 && newA[k] == 0; ++k) {
                }

                return new BigInteger().initWithArr(newA, newA.length - k).setPositive(true);
            } else {
                return rhs.del(this).reverse();
            }
        } else if (!isPositive && !rhs.isPositive) {
            return reverse().del(rhs.reverse()).reverse();
        } else if (isPositive && !rhs.isPositive) {
            return add(rhs.reverse());
        } else {
            return reverse().add(rhs).reverse();
        }
    }

    @Override
    public BigInteger mul(BigInteger rhs) {
        return null;
    }

    @Override
    public BigInteger div(BigInteger rhs) {
        return null;
    }

    @Override
    public boolean isGE(BigInteger rhs) {
        if (this.isPositive && !rhs.isPositive) {
            return true;
        } else if (!this.isPositive && rhs.isPositive){
            return false;
        } else {
            int thisLen = numberArray[0] == 0 ? numberArray.length - 1 : numberArray.length;
            int rhsLen = rhs.numberArray[0] == 0 ? rhs.numberArray.length - 1 : rhs.numberArray.length;
            if (this.isPositive) {
                if (thisLen == rhsLen) {
                    return numberArray[numberArray.length - thisLen] >= rhs.numberArray[rhs.numberArray.length - rhsLen] ? true : false;
                } else {
                    return thisLen > rhsLen ? true : false;
                }
            } else {
                if (thisLen == rhsLen) {
                    return numberArray[numberArray.length - thisLen] <= rhs.numberArray[rhs.numberArray.length - rhsLen] ? true : false;
                } else {
                    return thisLen < rhsLen ? true : false;
                }
            }
        }
    }

    public static void main(String[] args) {
        BigInteger b = new BigInteger("99999999999999999999999999999999999999999999999999999999999999999999999");
        BigInteger a = new BigInteger("88888888888888888888888888888888888888888888888888888888888888888888888");
        System.out.println(b.toString());
        System.out.println(a.toString());
        System.out.println(b.add(a));
        System.out.println(b.del(a));
    }
}
