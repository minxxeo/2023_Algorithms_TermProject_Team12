package example;

import algorithms.Dfs;
import javafx.util.Pair;
import util.EachNodes;

public class Example
{
    public static void main(String[] args)
    {
        EachNodes eachNodes = new EachNodes();
        Pair<Integer, Integer> conzonii = eachNodes.getConzonID().get("0010CZE300");
        System.out.println(eachNodes.getConzonDict().get(conzonii.getKey()));
        System.out.println(eachNodes.getConzonDict().get(conzonii.getValue()));
        Dfs dfs = new Dfs();
        dfs.find_A_to_B(1, 10, eachNodes.getAdjacent(), eachNodes.getConzonDict());
    }
}
