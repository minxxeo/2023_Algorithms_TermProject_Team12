package Algorithm_Termproject;

import algorithms.Calculator;
import algorithms.Dfs;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import util.ConzonInfo;
import util.ConzonNameLoc;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DistanceTest
{
    @Test
    void checkDist()
    {
        ConzonInfo.initialize();

        Dfs dfs = new Dfs();
        Pair<Integer, List<Integer>> aToB = dfs.getStart2EndWithAStar(498, 30, 1);
        int toll = Calculator.calcToll(aToB.getValue(), ConzonInfo.getAdjacent());
        System.out.println("toll = " + toll);
        int texi = Calculator.calcTexi(aToB.getKey(), 100010);
        System.out.println("texi = " + (texi + toll));
    }
    @Test
    void checkTimeCmp()
    {
        ConzonInfo.initialize();

        Dfs dfs = new Dfs();
        //Pair 최소거리, 최소거리의 루트
        System.out.println("==============Dijkstra==============");
        long startTime = System.nanoTime();
        Set<Integer> keys = ConzonInfo.getConzonDict().keySet();
        Set<Integer> finalKeys1 = keys;
        keys.forEach(iter->{
            finalKeys1.forEach(iter2 ->{
                dfs.getStart2End(iter, iter2, 1);

            });
        });
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        double seconds = (double) elapsedTime / 1_000_000_000.0;
        System.out.println("경과 시간: " + seconds + " 초");


        System.out.println("==============A*==============");
        startTime = System.nanoTime();
        keys = ConzonInfo.getConzonDict().keySet();
        Set<Integer> finalKeys = keys;
        keys.forEach(iter->{
            finalKeys.forEach(iter2 ->{
                dfs.getStart2EndWithAStar(iter, iter2, 1);
            });
        });
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
        seconds = (double) elapsedTime / 1_000_000_000.0;
        System.out.println("경과 시간: " + seconds + " 초");
        System.out.println("호출 횟수: " + keys.size() * keys.size());

    }
}
