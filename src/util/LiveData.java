package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LiveData
{
    private final static String KEY = "8820206620";
    private final static String BASE_URL = "http://data.ex.co.kr/openapi/odtraffic/trafficAmountByRealtime?"
            + "key=" + KEY + "&type=json";

    /*
     * Name        : getListComponent
     * Author      : Junseo Choi
     * Date        : 2023-11-26
     * argument    : none
     * return      : JSONArray
     * description : Receives a list of current speed measured by VDS for each highway section through API.
    */
    public static JSONArray getListComponent()
    {
        JSONArray list = null;
        String result = ""; // Result for parsing

        try
        {
            URL url = new URL(BASE_URL);

            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

            result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            list = (JSONArray) jsonObject.get("list");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }
    
    /*
     * Name        : getAverageTime
     * Author      : Junseo Choi
     * Date        : 2023-11-28
     * argument    : none
     * return      : Map<String, Integer>
     * description : Calculate the current speed of each section of the highway in JSONArray format.
    */
    public static Map<String, Integer> getAverageTime()
    {
        System.out.println("Fetching realtime data");
        JSONArray list = getListComponent();

        Map<String, Integer> traffic = new HashMap<String, Integer>();
        Map<String, Integer> tCount = new HashMap<String, Integer>();
        try
        {
            for (int i = 0; i < list.size(); i++)
            {
                /* Take information of json list */
                JSONObject temp = (JSONObject) list.get(i);

                /*  Parsing component */
                String conzoneID = (String) temp.get("conzoneName");
                int timeAvg = Integer.parseInt((String) temp.get("timeAvg"));
                int grade = Integer.parseInt((String) temp.get("grade"));

                if (grade == 0) continue; // 판정 불가 케이스

                /* 콘존 정보가 없는 경우 새롭게 추가한다 */

                if (!traffic.containsKey(conzoneID))
                {
                    traffic.put(conzoneID, timeAvg);
                    tCount.put(conzoneID, 1);
                }
                /* 콘존 정보가 있으면 기존의 콘존 정보를 가져와서 시간을 더해주고 개수를 증가시킴*/
                else
                {
                    traffic.put(conzoneID, traffic.get(conzoneID) + timeAvg);
                    tCount.put(conzoneID, tCount.get(conzoneID) + 1);
                }
            }
            System.out.println("Finished");

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return traffic;
    }
}