import java.util.*;

/**
 * Created by Administrator on 2014/9/20 0020.
 */
public class MaxDistence1 {
    public int MaxDistence11(int[] nums) {
        if (null == nums || nums.length == 0) {
            return 0;
        }

        final int n = nums.length;
        int[] DP =  new int[n];
        DP[0] = 0;
        for (int i = 1; i < n; ++i) {
            for (int j = i - 1; j >= 0; --j) {
                if (nums[j] <= nums[i]) {
                    DP[i] = Math.max(DP[i], DP[j] + i - j);
                }
            }
        }

        return DP[n - 1];
    }

    public int MaxDistence12(int[] nums) {
        if (null == nums || nums.length == 0) {
            return 0;
        }

        final int n = nums.length;
        TreeMap<Integer, Integer> tm = new TreeMap<Integer, Integer>();
        int max = 0;
        for (int i = 0; i < n; ++i) {
            Map.Entry<Integer, Integer> pair = tm.floorEntry(nums[i]);
            if (null == pair) {
                tm.put(nums[i], i);
            } else {
                max = Math.max(max, i - pair.getValue());
            }
        }

        return max;
    }


    public static void main(String[] args) {
        Scanner ss = new Scanner(System.in);
        int n = ss.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; ++i) {
            a[i] = ss.nextInt();
        }

        int x = new MaxDistence1().MaxDistence12(a);
        System.out.println(x);
    }

}
