package Algorithm_Termproject;

import algorithms.Calculator;
import algorithms.Dfs;
import entity.ConzonNode;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import util.ConzonInfo;
import util.LiveData;

import java.util.List;
import java.util.Map;

class live_dataTest
{
    @Test
    void testLiveData()
    {
        Map<String, Integer> traffic = LiveData.getAverageTime();
        ConzonInfo.initialize();
        ConzonInfo.initialize(traffic);

        Dfs dfs = new Dfs();

        int from = 305, to = 309;
        Pair<Integer, List<Integer>> start2End = dfs.getStart2End(from, to, 2);
        Pair<Integer, List<Integer>> aToB = dfs.getStart2End(from, to,1);
        int totalTime = start2End.getKey();
        String result = "";
        if (totalTime / 3600 != 0) result = totalTime / 3600 + "시간 ";
        totalTime %= 3600;
        if (totalTime / 60 != 0) result += totalTime / 60 + "분 ";
        totalTime %= 60;
        result += totalTime + "초";
        System.out.println(ConzonInfo.getConzonDict().get(from) + "부터 " + ConzonInfo.getConzonDict().get(to) + "까지의 걸리는 시간: " + result);
        totalTime = start2End.getKey();
        int dist = 0;
        int cur = to;
        List<Integer> value = start2End.getValue();

        for (int i = 0; i < value.size() - 1; i++)
        {
            for(ConzonNode iter : ConzonInfo.getAdjacent().get(value.get(i)))
            {
                if(iter.getId() == value.get(i + 1))
                {
                    dist += iter.getDist();
                    break;
                }
            }
        }
        int texifee = Calculator.calcTexi(dist, totalTime);
        int toll = dfs.calcToll(value);
        dfs.printRoute(value);
        dfs.printLine(value);
        System.out.println("toll = " + toll);
        System.out.println("dist = " + dist / 1000);
        System.out.println("가장 짧은 dist = " + aToB.getKey() / 1000);
        System.out.println("texifee = " +( texifee + toll));
    }

    @Test
    void printConzonSpeedInfo()
    {
        Map<String, Integer> t = LiveData.getAverageTime();
        int i = 0;
        for (String s : t.keySet())
        {
            System.out.println(i + ". " + s + ": " + t.get(s));
            i += 1;
        }
    }
}