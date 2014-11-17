package util;

import java.util.*;

/**
 * Created by Administrator on 2014/10/25 0025.
 */
public class Temp {
    String[] pad = new String[]{
            "",
            "abc",
            "def",
            "ghi",
            "jkl",
            "mno",
            "pqrs",
            "tuv",
            "wxyz",
    };

    public List<String> letterCombinations(String digits) {
        List<String> ret = new ArrayList<String>();
        StringBuilder cur = new StringBuilder();
        DFS(digits, 0, cur, ret);
        return ret;
    }

    private void DFS(String digtis, int start, StringBuilder cur, List<String> ret) {
        if (start >= digtis.length()) {
            ret.add(cur.toString());
            return;
        }

        String chs = pad[digtis.charAt(start) - '1'];
        for (int i = 0; i < chs.length(); ++i) {
            cur.append(chs.charAt(i));
            DFS(digtis, start + 1, cur, ret);
            cur.deleteCharAt(cur.length() - 1);
        }
    }

    public static void main(String[] args) {
        System.out.println(new Temp().letterCombinations("23"));
    }
}
