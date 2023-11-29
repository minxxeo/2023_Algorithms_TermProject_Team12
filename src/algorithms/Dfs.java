package algorithms;

import entity.ConzonNode;
import javafx.util.Pair;
import util.ConzonInfo;

import java.util.*;

public class Dfs
{
    int[] dist = new int[1100];
    int[] prev_loc = new int[1100];
    private final List<List<ConzonNode>> adjacent;
    private final Map<Integer,String> conzonDict;
    private final Map<Integer,String> lineInfo;

    public Dfs()
    {
        adjacent = ConzonInfo.getAdjacent();
        conzonDict = ConzonInfo.getConzonDict();
        lineInfo = ConzonInfo.getLineInfo();
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
    void dijkstra(int from) //By distance
    {
        Arrays.fill(dist, 2147483647);
        Arrays.fill(prev_loc, 0);
        PriorityQueue<Node> pq = new PriorityQueue<>();
        dist[from] = 0;
        pq.offer(new Node(from, 0));
        while(!pq.isEmpty())
        {
            int cur = pq.poll().idx;
            for(ConzonNode curNode : adjacent.get(cur))
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
    public void printRoute(List<Integer> line)
    {
        for (int i = line.size() - 1; i >= 0; i--)
        {
            System.out.print(conzonDict.get(line.get(i)) + (i != 0 ? " - " : "\n"));
        }
    }

    public void printLine(List<Integer> line)
    {
        int cur = -1;
        List<String> rst = new ArrayList<>();

        for (int i = line.size() - 2; i >= 0; i--)
        {
            for(ConzonNode iter :adjacent.get(line.get(i)))
            {
                if(iter.getId() == line.get(i + 1) && iter.getLine() != cur)
                {
                    cur = iter.getLine();
                    rst.add(lineInfo.get(cur));
                }
            }
        }
        for(int i = 0; i < rst.size(); i++)
        {
            System.out.print(rst.get(i) + (i != rst.size() -1 ? " - " : "\n"));
        }
    }
    public int calcToll(List<Integer> line)
    {
        double cost = 44.3, rst = 0;
        for (int i = line.size() - 2; i >= 0; i--)
        {
            for(ConzonNode iter :adjacent.get(line.get(i)))
            {
                if(iter.getId() == line.get(i + 1) )
                {
                    if(iter.getLanecnt() >= 6)
                    {
                        rst += (double)iter.getDist() / 1000 * cost * 1.2;
                    }
                    else
                    {
                        rst += (double)iter.getDist() / 1000  * cost;
                    }
                }
            }
        }
        rst += 1800;
        return (int)rst;
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