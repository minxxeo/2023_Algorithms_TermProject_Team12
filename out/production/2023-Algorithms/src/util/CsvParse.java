package util;

import com.sun.corba.se.impl.orbutil.graph.Graph;

import javax.swing.plaf.IconUIResource;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Collections.addAll;
import static java.util.Collections.sort;

public class CsvParse
{
    public static void main(String[] args)
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("assets/conzon_info.csv"), "EUC-KR"));
            Map<Integer, String> conzonDict = new HashMap<>();
            List<List<ConzonInfo>> adjacent = new ArrayList<>();

            for (int i = 0 ; i < 1100; i++)
            {
                adjacent.add(new ArrayList<>());
                dijtest[i] = 2147483647;
            }

            Stream<String> lines = br.lines();
            br.readLine();
            lines.forEach(str -> parse_conzon(str, conzonDict ,adjacent));
//            conzonDict.forEach((idx, str) -> System.out.println(idx + " : " + str));


            /*Test graph connectivity*/
            dijtest[305] = 0;
            dfs(305,adjacent, conzonDict);
            System.out.println(conzonDict.get(305) + "부터 "+ conzonDict.get(486)+"까지의 거리 : "+ (double)dijtest[486] / 1000 + "km");
            int cur = 486;
            while(true)
            {
                System.out.print(conzonDict.get(cur) + " - ");
                if(cur == 305)
                    break;
                cur = dijloc[cur];
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
   static void parse_conzon(String str, Map<Integer, String> dict, List<List<ConzonInfo>> adja)
    {
        String[] element = str.split(",");
        try
        {
            int idx = Integer.parseInt(element[3]);
            String par = element[9];
            String[] split = par.split("-");

            if(!dict.containsKey(idx))
                dict.put(idx, split[0]);

            int from = Integer.parseInt(element[3]);
            int to = Integer.parseInt(element[4]);
            int dist = (int)Double.parseDouble(element[1]);
            adja.get(from).add(new ConzonInfo(to, dist));

        }catch (NumberFormatException ignored)
        {

        }
    }
    static boolean[] visited = new boolean[1100];
    static int[] dijtest = new int[1100];
    static int[] dijloc = new int[1100];
    static void dfs(int n, List<List<ConzonInfo>> adja, Map<Integer, String> dict)
    {
        for (ConzonInfo node : adja.get(n))
        {
            if(dijtest[n] + node.getDist() < dijtest[node.getId()])
            {
                dijtest[node.getId()] = dijtest[n] + node.getDist();
                dijloc[node.getId()] = n;
                dfs(node.getId(), adja, dict);
            }
        }
    }
}

class ConzonInfo
{
    private final int id;
    private final int dist;

    public ConzonInfo(int id, int dist)
    {
        this.id = id;
        this.dist = dist;
    }

    public int getId()
    {
        return id;
    }

    public int getDist()
    {
        return dist;
    }
}