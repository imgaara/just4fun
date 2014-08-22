import java.io.*;
import java.util.*;

/**
 * Created by Administrator on 2014/8/22 0022.
 */
public class Seven_segment_Display {

    static class Result {
        String val;
        int mask;
    }

    int[] SEG = {
            0xFC, // 0
            0x60, // 1
            0xDA, // 2
            0xF2, // 3
            0x66, // 4
            0xB6, // 5
            0xBE, // 6
            0xE0, // 7
            0xFE, // 8
            0xF6, // 9
    };

    public void resolve(String file) throws IOException {
        List<List<String>> tests = read(file);
        List<Result> results =  resolve(tests);
        write(results, file+".out");
    }

    private List<Result> resolve(List<List<String>> tests) {
        List<Result> results = new ArrayList<Result>();

        int count = 1;
        for (List<String> test : tests) {
            System.out.println("#################################### " + count++);
            Set<Integer> result = new HashSet<Integer>();
            for (int i = 0; i < 10; ++i) {
                result.add(i);
            }

            for (int i = 0; i < test.size(); ++i) {
                int cur = Integer.parseInt(test.get(i)+"0", 2);
                Set<Integer> set2 = possibleDigits(cur);
                System.out.println(set2);

                Iterator<Integer> it = set2.iterator();
                while (it.hasNext()) {
                    int curDig = it.next();
                    int last = (curDig + 1) % 10;
                    if (!result.contains(last)) {
                        it.remove();
                    }
                }

                result = set2;
            }

            Result r = new Result();
            if (result.size() == 1) {
                int last = result.iterator().next();
                int mask = 0;
                int can = 0xFF;
                for (int i = 0; i < test.size(); ++i) {
                    int cur = (last + i) % 10;
                    can &= (~Integer.parseInt(test.get(test.size() - 1 - i)+"0", 2));
                    mask |= SEG[cur] ^ Integer.parseInt(test.get(test.size() - 1 - i)+"0", 2);
                }

                int next = (last - 1 + 10) % 10;
                r.mask = mask;
                System.out.println(toString(mask));
                System.out.println(toString(can));
                String val1 = toString(SEG[next] & (~mask));
                String val2 = toString(SEG[next] & (~can));
                if (val1.equals(val2)) {
                    r.val = val1;
                } else {
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + val1 + " " + val2);
                    r.val = "ERROR!";
                }
            } else {
                r.val = "ERROR!";
            }
            results.add(r);
        }

        return results;
    }

    private String toString(int val) {
        StringBuilder sb = new StringBuilder();
        int mask = 0x80;
        for (int i = 0; i < 7; ++i) {
            if (((mask >> i) & val) == 0) {
                sb.append('0');
            } else {
                sb.append('1');
            }
        }

        return sb.toString();
    }

    public Set<Integer> possibleDigits(int curBin) {
        Set<Integer> set = new HashSet<Integer>();
        for (int k = SEG.length - 1; k >= 0; --k) {
            int curDig = SEG[k];
            if ((curBin & curDig) == curBin) {
                set.add(k);
            }
        }

        return set;
    }

    private List<List<String>> read(String file) throws FileNotFoundException {
        Scanner ss = new Scanner(new FileInputStream(file));
        int T = Integer.parseInt(ss.nextLine());
        List<List<String>> tests = new ArrayList<List<String>>();

        for (int i = 0 ; i < T; ++i) {
            int N = ss.nextInt();
            List<String> test = new ArrayList<String>();
            for (int j = 0; j < N; ++j) {
                test.add(ss.next());
            }
            tests.add(test);
        }

        return tests;
    }

    private void write(List<Result> results, String out) throws IOException {
        BufferedWriter w = new BufferedWriter(new FileWriter(out));
        for (int i = 0; i < results.size(); ++i) {
            w.write("Case #");
            w.write(String.valueOf(i+1));
            w.write(": ");
            w.write(results.get(i).val);
            w.write("\n");
        }
        w.close();
    }

    public static void main(String[] args) throws IOException {
        new Seven_segment_Display().resolve("D:\\IdeaProjects\\just4fun\\A-small-practice.in");
    }
}
