import java.util.Arrays;

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

    public static final BigInteger ONE = new BigInteger("1");
    public static final BigInteger TWO = new BigInteger("2");
    public static final BigInteger ZERO = new BigInteger("0");

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
            while ((idx < str.length() - 1) && (str.charAt(idx) == ' ' || str.charAt(idx) == '\t' || str.charAt(idx) == '0')) {
                ++idx;
            }
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
                    c = 0;
                }
            }
        }

        this.numberArray = tempArray;
        len = numberArray.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!isPositive) {
            sb.append('-');
        }

        for (int i = numberArray.length - len; i < numberArray.length; ++i) {
            String cur = String.valueOf(numberArray[i]);
            if (sb.length() > 0) {
                for (int j = 0; j < MAX_LEN_PER_SEG - cur.length(); ++j) {
                    sb.append('0');
                }
            }
            sb.append(cur);
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

            for (k = 0; k < newA.length - 1 && newA[k] == 0; ++k) {
            }
            return new BigInteger().initWithArr(newA, newA.length - k).setPositive(isPositive);
        } else {
            if (isGE(rhs)) {
                return del(rhs.reverse());
            } else {
                return rhs.del(reverse());
            }
        }
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
                        newA[k] += LARGE_BASE;
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
                        newA[k] += LARGE_BASE;
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
        int[] newA = new int [this.numberArray.length + rhs.numberArray.length];

        for (int i = numberArray.length - 1; i >= 0; --i) {
            long c = 0L;
            int j = rhs.numberArray.length - 1;
            for (; j >=0; --j) {
                long curSum = (long)numberArray[i] * (long)rhs.numberArray[j] + newA[i + j + 1] + c;
                newA[i + j + 1] = (int) (curSum % LARGE_BASE);
                c = curSum / LARGE_BASE;
            }

            while (c > 0) {
                long curSum = newA[i + j + 1] + c;
                newA[i + j + 1] = (int) (curSum % LARGE_BASE);
                c = curSum / LARGE_BASE;
                --j;
            }
        }

        int idx = 0;
        for (;idx < newA.length - 1 && newA[idx] == 0; ++idx) {
        }

        return new BigInteger().initWithArr(newA, newA.length - idx).setPositive(isPositive == rhs.isPositive);
    }

    @Override
    public BigInteger div(BigInteger rhs) {
        if (isAbsGE(rhs)) {
            BigInteger one = ONE;
            BigInteger rhsAbs = rhs.lightAbsCopy();
            if (rhsAbs.equals(one)) {
                return deepCopy().setPositive(isPositive == rhs.isPositive);
            }

            BigInteger thisAbs = this.lightAbsCopy();
            BigInteger result = ZERO;
            while (thisAbs.isGE(rhsAbs)) {
                BigInteger subSum = rhsAbs;
                BigInteger lastSubSum = subSum;
                BigInteger subResult = ONE;
                BigInteger lastSubResult = ONE;
                while (thisAbs.isGE(subSum)) {
                    lastSubSum = subSum;
                    lastSubResult = subResult;
                    subSum = subSum.mul(TWO);
                    if (subSum.isGT(thisAbs)) {
                        break;
                    }
                    subResult = subResult.mul(TWO);
                }

                subSum = lastSubSum;
                result = result.add(lastSubResult);
                thisAbs = thisAbs.del(subSum);
            }

            return result.setPositive(isPositive == rhs.isPositive);
        } else {
            return new BigInteger().initWithArr(new int[]{0}, 1).setPositive(true);
        }
    }

    @Override
    public boolean isGE(BigInteger rhs) {
        if (this.isPositive && !rhs.isPositive) {
            return true;
        } else if (!this.isPositive && rhs.isPositive){
            return false;
        } else {
            int thisLen = len;
            int rhsLen = rhs.len;
            if (this.isPositive) {
                if (thisLen == rhsLen) {
                    int i = numberArray.length - thisLen, j = rhs.numberArray.length - rhsLen;
                    for (; i < numberArray.length; ++i, ++j) {
                        if (numberArray[i] == rhs.numberArray[j]) {
                            continue;
                        } else if (numberArray[i] < rhs.numberArray[j]) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                    return numberArray[numberArray.length - 1] >= rhs.numberArray[rhs.numberArray.length - 1] ? true : false;
                } else {
                    return thisLen > rhsLen ? true : false;
                }
            } else {
                if (thisLen == rhsLen) {
                    for (int i = numberArray.length - thisLen, j = rhs.numberArray.length - rhsLen; i < numberArray.length; ++i, ++j) {
                        if (numberArray[i] == rhs.numberArray[j]) {
                            continue;
                        } else if (numberArray[i] > rhs.numberArray[j]) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                    return numberArray[numberArray.length - 1] <= rhs.numberArray[rhs.numberArray.length - 1] ? true : false;
                } else {
                    return thisLen < rhsLen ? true : false;
                }
            }
        }
    }

    @Override
    public boolean isGT(BigInteger rhs) {
        if (this.isPositive && !rhs.isPositive) {
            return true;
        } else if (!this.isPositive && rhs.isPositive){
            return false;
        } else {
            int thisLen = len;
            int rhsLen = rhs.len;
            if (this.isPositive) {
                if (thisLen == rhsLen) {
                    for (int i = numberArray.length - thisLen, j = rhs.numberArray.length - rhsLen; i < numberArray.length; ++i, ++j) {
                        if (numberArray[i] == rhs.numberArray[j]) {
                            continue;
                        } else if (numberArray[i] <= rhs.numberArray[j]) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                    return numberArray[numberArray.length - 1] > rhs.numberArray[rhs.numberArray.length - 1] ? true : false;
                } else {
                    return thisLen > rhsLen ? true : false;
                }
            } else {
                if (thisLen == rhsLen) {
                    for (int i = numberArray.length - thisLen, j = rhs.numberArray.length - rhsLen; i < numberArray.length; ++i, ++j) {
                        if (numberArray[i] == rhs.numberArray[j]) {
                            continue;
                        } else if (numberArray[i] > rhs.numberArray[j]) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                    return numberArray[numberArray.length - 1] < rhs.numberArray[rhs.numberArray.length - 1] ? true : false;
                } else {
                    return thisLen < rhsLen ? true : false;
                }
            }
        }
    }

    @Override
    public boolean isAbsGE(BigInteger rhs) {
        int thisLen = numberArray[0] == 0 ? numberArray.length - 1 : numberArray.length;
        int rhsLen = rhs.numberArray[0] == 0 ? rhs.numberArray.length - 1 : rhs.numberArray.length;
        if (thisLen == rhsLen) {
            return numberArray[numberArray.length - thisLen] >= rhs.numberArray[rhs.numberArray.length - rhsLen] ? true : false;
        } else {
            return thisLen > rhsLen ? true : false;
        }
    }

    @Override
    public boolean equals(BigInteger rhs) {
        if (this.numberArray.length == rhs.numberArray.length && this.len == rhs.len) {
            for (int i = numberArray.length - len; i < numberArray.length; ++i) {
                if (numberArray[i] != rhs.numberArray[i]) {
                    return false;
                }
            }

            return true;
        }

        return false;
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

    private BigInteger reverse() {
        return new BigInteger().initWithArr(numberArray, len).setPositive(!isPositive);
    }

    private BigInteger lightAbsCopy() {
        return new BigInteger().initWithArr(numberArray, len).setPositive(true);
    }

    private BigInteger deepCopy() {
        return new BigInteger().initWithArr(Arrays.copyOf(numberArray, numberArray.length), len).setPositive(isPositive);
    }

    private BigInteger rightShift() {
        int[] newA = new int[numberArray.length];
        long c = 0L;
        for (int i = numberArray.length - len; i < numberArray.length; ++i) {
            long extend = (long)numberArray[i] * LARGE_BASE;
            extend = extend >>> 1;
            newA[i] = (int)(extend / LARGE_BASE + c);
            c = extend % LARGE_BASE;
        }

        int idx = 0;
        for (;idx < newA.length - 1 && newA[idx] == 0; ++idx) {
        }
        return  new BigInteger().initWithArr(newA, newA.length - idx).setPositive(isPositive);
    }

    public static void main(String[] args) {
        BigInteger c= new BigInteger("10000000000000000000001000000000000000000000000000000100000000000000000000000000000");
        BigInteger b = new BigInteger("99999999999999999");
        BigInteger a = new BigInteger("333");
        System.out.println(c);
        System.out.println(ZERO.add(TWO));
        System.out.println(b.toString());
        System.out.println(a.toString());
        System.out.println(b.add(a));
        System.out.println(b.del(a));
        System.out.println(b.mul(a));
        System.out.println(b.rightShift());
        System.out.println(b.div(a));
    }
}
