package algorithms;

import com.sun.javaws.IconUtil;
import entity.ConzonInfo;

import java.util.*;
import javafx.util.Pair;

public class Dfs
{
    int[] dist = new int[1100];
    int[] prev_loc = new int[1100];
    int[] time = new int[1100];
    private final List<List<ConzonNode>> adjacent;
    private final Map<Integer,String> conzonDict;
    private final Map<Integer,String> lineInfo;
    public List<String> getStart2End(int from, int to, List<List<ConzonInfo>> adjacent, Map<Integer, String> conzonDict) {
        dijkstra_speed(from, adjacent);
        int totalTime = time[to];
        String result ="";
        if(totalTime/3600 != 0)result = totalTime/3600+"시간 ";
        totalTime %= 3600;
        if(totalTime/60 != 0) result += totalTime/60+"분 ";
        totalTime %= 60;
        result += totalTime +"초";
        System.out.println(conzonDict.get(from) + "부터 " + conzonDict.get(to) + "까지의 걸리는 시간: " + result);
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
        List<String> conzonList = new ArrayList<>();
        for(int i = line.size()-1; i > 0; i--)
            conzonList.add(line.get(i)+"-"+line.get(i-1));

        return conzonList;
    }
    public Pair<Integer, List<Integer>> find_A_to_B(int from, int to )
    {
        /*Test graph connectivity*/
        dijkstra(from);

        int cur = to;
        List<Integer> line = new ArrayList<>();
        while (true)
        {
            line.add(cur);
            if (cur == from)
                break;
            cur = prev_loc[cur];
        }
        return new Pair<>(dist[to],line);
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
    void dijkstra_speed(int from, List<List<ConzonInfo>> adjacent) //By distance
    {
        Arrays.fill(time, 2147483647);
        Arrays.fill(prev_loc, 0);
        PriorityQueue<Node> pq = new PriorityQueue<>();
        time[from] = 0;
        pq.offer(new Node(from, 0));
        while(!pq.isEmpty())
        {
            int cur = pq.poll().idx;
            for(ConzonInfo curNode : adjacent.get(cur))
            {
                if(time[curNode.getId()] > time[cur] + curNode.getTime())
                {
                    time[curNode.getId()] = time[cur] + curNode.getTime();
                    prev_loc[curNode.getId()] = cur;
                    pq.offer(new Node(curNode.getId(), time[cur] + curNode.getTime()));
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