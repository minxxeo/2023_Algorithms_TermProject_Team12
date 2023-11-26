package algorithms;

import com.sun.javaws.IconUtil;
import entity.ConzonInfo;

import java.util.*;

public class Dfs
{
    int[] dist = new int[1100];
    int[] prev_loc = new int[1100];

    public void find_A_to_B(int from, int to, List<List<ConzonInfo>> adjacent, Map<Integer, String> conzonDict)
    {
        /*Test graph connectivity*/
        dijkstra(from, adjacent);
        System.out.println(conzonDict.get(from) + "부터 " + conzonDict.get(to) + "까지의 거리 : " + (double) dist[to] / 1000 + "km");
        int cur = to;

        List<String> line = new ArrayList<>();
        while (true)
        {
            line.add(conzonDict.get(cur));
            if (cur == from)
                break;
            cur = prev_loc[cur];
        }
        for (int i = line.size() - 1; i >= 1; i--)
        {
            System.out.print(line.get(i) + " - ");
        }
        System.out.println(line.get(0));
    }
    void dijkstra(int from, List<List<ConzonInfo>> adjacent) //By distance
    {
        Arrays.fill(dist, 2147483647);
        Arrays.fill(prev_loc, 0);
        PriorityQueue<Node> pq = new PriorityQueue<>();
        dist[from] = 0;
        pq.offer(new Node(from, 0));
        while(!pq.isEmpty())
        {
            int cur = pq.poll().idx;
            for(ConzonInfo curNode : adjacent.get(cur))
            {
                if(dist[curNode.getId()] > dist[cur] + curNode.getDist())
                {
                    dist[curNode.getId()] = dist[cur] + curNode.getDist();
                    prev_loc[curNode.getId()] = cur;
                    pq.offer(new Node(curNode.getId(), dist[cur] +  curNode.getDist()));
                }
            }
        }
    }
}



class Node implements Comparable<Node>
{
    int idx;
    int cost;

    public Node(int idx, int cost)
    {
        this.idx = idx;
        this.cost = cost;
    }

    @Override
    public int compareTo(Node o)
    {
        return Integer.compare(this.cost, o.cost);
    }
}