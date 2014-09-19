/**
 * 
 */
package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @file	Reader.java
 * @package	tools
 * @project	ZTest
 * @author	www.imgaara.com
 * @time	2014-6-21
 *
 * 
 */
public class Reader
{
    
    public static List<String>  readLines() throws IOException {
        BufferedReader r = new BufferedReader(new FileReader("data.txt"));
        String line = null;
        List<String> list = new ArrayList<String>();
        
        while ((line = r.readLine()) != null) {
            list.add(line);
        }
        
        return list;
    }
}
