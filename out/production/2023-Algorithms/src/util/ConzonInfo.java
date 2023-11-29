package util;

import entity.ConzonNode;
import javafx.util.Pair;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Collections.sort;

public class ConzonInfo
{
    static private Map<Integer, String> conzonDict;
    static private List<List<ConzonNode>> adjacent;
    static private Map<String, Pair<Integer, Integer>> conzonID;
    static private Map<Integer, String> lineInfo;

    static public void initialize()
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("assets/conzon_info.csv"), "EUC-KR"));
            conzonDict = new HashMap<>();
            adjacent = new ArrayList<>();
            conzonID = new HashMap<>();
            for (int i = 0; i < 1100; i++)
            {
                adjacent.add(new ArrayList<>());
            }

            Stream<String> lines = br.lines();
            br.readLine();
            lines.forEach(ConzonInfo::parse_conzon);
            br.close();

            BufferedReader lineBr = new BufferedReader(new InputStreamReader(new FileInputStream("assets/line_info.csv"), "EUC-KR"));
            lineInfo = new HashMap<>();
            lines = lineBr.lines();
            lineBr.readLine();
            lines.forEach(ConzonInfo::parse_line);
            lineBr.close();

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    static public void initialize(Map<String, Integer> t)
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("conzon_info.csv"), "EUC-KR"));
            for (int i = 0; i < 1100; i++)
            {
                adjacent.add(new ArrayList<>());
            }

            Stream<String> lines = br.lines();
            br.readLine();
            lines.forEach(str -> parse_conzon_t(str, t));

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    static void parse_conzon(String str)
    {
        String[] element = str.split(",");
        try
        {
            int idx = Integer.parseInt(element[3]);
            String par = element[9];
            String[] split = par.split("-");

            if (!conzonDict.containsKey(idx))
                conzonDict.put(idx, split[0]);

            int from = Integer.parseInt(element[3]);
            int to = Integer.parseInt(element[4]);
            int dist = (int) Double.parseDouble(element[1]);
            int line = (int) Double.parseDouble(element[6]);
            int lanecnt = (int) Double.parseDouble(element[5]);
            adjacent.get(from).add(new ConzonNode(to, dist, line, lanecnt));

            conzonID.put(element[0], new Pair<>(from, to));
        } catch (NumberFormatException ignored)
        {
        }
    }

    static void parse_conzon_t(String str, Map<String, Integer> t)
    {
        String[] element = str.split(",");
        try
        {
            int idx = Integer.parseInt(element[3]);
            String par = element[9];
            String[] split = par.split("-");

            if (!conzonDict.containsKey(idx))
                conzonDict.put(idx, split[0]);

            int from = Integer.parseInt(element[3]);
            int to = Integer.parseInt(element[4]);
            int dist = (int) Double.parseDouble(element[1]);
            int time = 0;

            if (t.containsKey(par))
            {
                time = t.get(par);
            } else
            {
                time = (int) ((dist / Double.parseDouble(element[7])) * 3600 / 1000);
            }

            for (ConzonNode iter : adjacent.get(from))
            {
                if (iter.getId() == to)
                {
                    iter.setTime(time);
                    break;
                }
            }
        } catch (NumberFormatException ignored)
        {
        }
    }

    static void parse_line(String str)
    {
        String[] element = str.split(",");
        try
        {
            int line = Integer.parseInt(element[0]);
            lineInfo.put(line, element[1]);
        } catch (NumberFormatException ignored)
        {
        }
    }

    public static Map<Integer, String> getLineInfo()
    {
        return lineInfo;
    }

    public static Map<Integer, String> getConzonDict()
    {
        return conzonDict;
    }

    public static List<List<ConzonNode>> getAdjacent()
    {
        return adjacent;
    }

    public static Map<String, Pair<Integer, Integer>> getConzonID()
    {
        return conzonID;
    }
}

