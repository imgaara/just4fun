package util;

import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Administrator on 2014/11/17 0017.
 */
public class BufferedReader {

    final int TEMP_SIZE = 200;
    final int BUFF_SIZE = TEMP_SIZE * 2;
    char[] buff = new char[BUFF_SIZE];
    char[] temp = new char[TEMP_SIZE];
    int start = 0;
    int end = 0;
    boolean hasMore = true;
    FileReader fr;

    public BufferedReader(FileReader fr) {
        this.fr = fr;
    }

    public String readLine() throws IOException {
        if (!hasMore) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        do {
            readMore();
            while (!isEmpty() && buff[start] != '\n') {
                sb.append(buff[start]);
                start = (start + 1) % BUFF_SIZE;
            }

            if (isEmpty()) {
                readMore();
            } else {
                start = (start + 1) % BUFF_SIZE;
                return sb.toString();
            }
        } while (!isEmpty());

        return sb.toString();
    }

    private void readMore() throws IOException {
        if (!hasMore || !isEmpty()) {
            return;
        }

        int ret = read4k(temp);
        if (-1 == ret) {
            hasMore = false;
            return;
        }

        int firstHalfSize = BUFF_SIZE - end;
        if (firstHalfSize >= ret) {
            System.arraycopy(temp, 0, buff, end, ret);
            end = (end + ret) % BUFF_SIZE;
        } else {
            System.arraycopy(temp, 0, buff, end, firstHalfSize);
            int secondHalf = ret - firstHalfSize;
            System.arraycopy(temp, firstHalfSize, buff, 0, secondHalf);
            end = secondHalf;
        }

    }

    private boolean isEmpty() {
        return start == end;
    }

    private int read4k(char[] out) throws IOException {
        return fr.read(out);
    }

    public static void main(String[] args) {
        try {
            BufferedReader r = new BufferedReader(new FileReader("D:\\IdeaProjects\\just4fun\\A-small-practice.in2"));
            String line ;
            while ((line = r.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
