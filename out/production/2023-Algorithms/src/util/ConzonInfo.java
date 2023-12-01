package util;

import entity.ConzonNode;
import javafx.util.Pair;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Collections.sort;

public class ConzonInfo
{
    static private Map<Integer, ConzonNameLoc> conzonDict;
    static private List<List<ConzonNode>> adjacent;
    static private Map<String, Pair<Integer, Integer>> conzonID;
    static private Map<Integer, String> lineInfo;
    static private int[][] linedist = new int[1100][1100];

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

            BufferedReader conzonloc = new BufferedReader(new InputStreamReader(new FileInputStream("assets/conzonloc.csv"), "UTF-8"));
            lines = conzonloc.lines();
            conzonloc.readLine();
            lines.forEach(ConzonInfo::parse_location);
            conzonloc.close();


            Set<Integer> keys = getConzonDict().keySet();
            Set<Integer> finalKeys1 = keys;
            keys.forEach(iter->{

            finalKeys1.forEach(iter2 ->{
                linedist[iter][iter2] =distance(conzonDict.get(iter).getX(), conzonDict.get(iter).getY(), conzonDict.get(iter2).getX(),conzonDict.get(iter2).getY());
            });
            });

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
    private static double deg2rad(double deg){
        return (deg * Math.PI/180.0);
    }
    //radian(라디안)을 10진수로 변환
    private static double rad2deg(double rad){
        return (rad * 180 / Math.PI);
    }

    private static int distance(double lat1, double lon1, double lat2, double lon2){
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))* Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))*Math.cos(deg2rad(lat2))*Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60*1.1515*1609.344;

        return (int)dist; //단위 meter
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
                conzonDict.put(idx, new ConzonNameLoc(split[0],0,0));


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

    static void parse_location(String str)
    {
        String[] element = str.split(",");
        try
        {
            String name = (element[15].replaceAll("\"", ""));
            double x =Double.parseDouble (element[16].replaceAll("\"", ""));
            double y = Double.parseDouble (element[17].replaceAll("\"", ""));

            int keyByValue = getKeyByValue(conzonDict, name);
            conzonDict.replace(keyByValue, new ConzonNameLoc(name, x,y));
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
                conzonDict.put(idx, new ConzonNameLoc(split[0], 0, 0) );

            int from = Integer.parseInt(element[3]);
            int to = Integer.parseInt(element[4]);
            int dist = (int) Double.parseDouble(element[1]);
            int time = 0;

            if (t.containsKey(par))
            {
                time = t.get(par);
            } else
            {
                time = (int) ((dist / (Double.parseDouble(element[7])-20)) * 3600 / 1000);
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
    public static int getKeyByValue(Map<Integer, ConzonNameLoc> map, String value) {
        int findKey = -1;
        for(Map.Entry<Integer, ConzonNameLoc> entry : map.entrySet()){
            // 동일한 값이 있으면 반복문을 종료합니다.
            if(entry.getValue().getName().equals(value)) {
                findKey = entry.getKey();
                break;
            }
        }
        return findKey;
    }
    public static Map<Integer, String> getLineInfo()
    {
        return lineInfo;
    }

    public static Map<Integer, ConzonNameLoc> getConzonDict()
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

    public static int[][] getLinedist()
    {
        return linedist;
    }
}

