package example;

import algorithms.Calculator;
import algorithms.Dfs;
import javafx.util.Pair;
import util.EachNodes;

import java.util.List;

public class Example
{
    public static void main(String[] args)
    {
        EachNodes eachNodes = new EachNodes();

        Pair<Integer, Integer> conzonii = eachNodes.getConzonID().get("0010CZE300");
        System.out.println(eachNodes.getConzonDict().get(conzonii.getKey()));
        System.out.println(eachNodes.getConzonDict().get(conzonii.getValue()));

        Dfs dfs = new Dfs(eachNodes);
        //Pair 최소거리, 최소거리의 루트
        Pair<Integer, List<Integer>> aToB = dfs.find_A_to_B(305, 209);
        System.out.println(eachNodes.getConzonDict().get(305) + "부터 " + eachNodes.getConzonDict().get(209) + "까지의 거리 : " + (double) aToB.getKey() / 1000 + "km");
        dfs.printLine(aToB.getValue());
        int toll = dfs.calcToll(aToB.getValue());
        System.out.println("toll = " + toll);
        int texi = Calculator.calcTexi(aToB.getKey());
        System.out.println("texi = " + (texi + toll));
    }

}
