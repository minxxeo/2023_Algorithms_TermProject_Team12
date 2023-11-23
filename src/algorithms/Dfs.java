package algorithms;

import entity.ConzonInfo;

import java.util.List;
import java.util.Map;

public class Dfs
{
    private int[] dijtest;
    private int[] dijloc;

    void find_A_to_B(int from, int to, List<List<ConzonInfo>> adjacent, Map<Integer, String> conzonDict)
    {
        dijtest = new int[1100];
        dijloc = new int[1100];
        /*Test graph connectivity*/
        dijtest[305] = 0;
        dfs(305, adjacent, conzonDict);
        System.out.println(conzonDict.get(305) + "부터 " + conzonDict.get(486) + "까지의 거리 : " + (double) dijtest[486] / 1000 + "km");
        int cur = 486;
        while (true)
        {
            System.out.print(conzonDict.get(cur) + " - ");
            if (cur == 305)
                break;
            cur = dijloc[cur];
        }
    }

    void dfs(int n, List<List<ConzonInfo>> adja, Map<Integer, String> dict)
    {
        for (ConzonInfo node : adja.get(n))
        {
            if (dijtest[n] + node.getDist() < dijtest[node.getId()])
            {
                dijtest[node.getId()] = dijtest[n] + node.getDist();
                dijloc[node.getId()] = n;
                dfs(node.getId(), adja, dict);
            }
        }
    }
}
