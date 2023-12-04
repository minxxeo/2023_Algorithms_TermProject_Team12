package algorithms;

import java.util.*;

import entity.ConzonNode;
import javafx.util.Pair;
import util.ConzonInfo;
import util.ConzonNameLoc;

public class Dfs
{
    int[] dist = new int[1100];
    int[] prev_loc = new int[1100];
    int[] time = new int[1100];
    private final List<List<ConzonNode>> adjacent;
    private final Map<Integer, ConzonNameLoc> conzonDict;
    private final Map<Integer, String> lineInfo;

    public Dfs()
    {
        this.adjacent = ConzonInfo.getAdjacent();
        this.conzonDict = ConzonInfo.getConzonDict();
        this.lineInfo = ConzonInfo.getLineInfo();

    }

    public Pair<Integer, List<Integer>> getStart2End(int from, int to, int mode)
    {
        dijkstra(from, mode);
        int cur = to;
        List<Integer> line = new ArrayList<>();
        while (true)
        {
            line.add(cur);
            if (cur == from)
                break;
            cur = prev_loc[cur];
        }
        return new Pair<>(dist[to], line);
    }
    public Pair<Integer, List<Integer>> getStart2EndWithAStar(int from, int to, int mode)
    {
        aStar(from, to);
        int cur = to;
        List<Integer> line = new ArrayList<>();
        while (true)
        {
            line.add(cur);
            if (cur == from)
                break;
            cur = prev_loc[cur];
        }
        return new Pair<>(dist[to], line);
    }

    void dijkstra(int from, int mode) //By distance mode 1 : dist, mode 2 : time
    {
        Arrays.fill(dist, 2147483647);
        Arrays.fill(prev_loc, 0);
        PriorityQueue<Node> pq = new PriorityQueue<>();
        int newdist;
        dist[from] = 0;
        boolean[] fin = new boolean[1100];
        fin[from] = true;
        pq.offer(new Node(from, 0));
        while (!pq.isEmpty())
        {
            int cur = pq.poll().idx;
            for (ConzonNode curNode : adjacent.get(cur))
            {
                newdist = dist[cur] + (mode == 1 ? curNode.getDist() : curNode.getTime());
                if (dist[curNode.getId()] > newdist&& !fin[curNode.getId()])
                {
                    dist[curNode.getId()] = newdist;
                    prev_loc[curNode.getId()] = cur;
                    fin[curNode.getId()] = true;
                    pq.offer(new Node(curNode.getId(), newdist));
                }
            }
        }
    }

    void aStar(int from, int to)
    {
        Arrays.fill(dist, 2147483647);
        Arrays.fill(prev_loc, 0);
        PriorityQueue<AStarNode> pq = new PriorityQueue<>();
        int[][] linedist = ConzonInfo.getLinedist();
        int newdist;
        dist[from] = 0;
        boolean[] fin = new boolean[1100];
        pq.offer(new AStarNode(from, 0, linedist[from][to]));
        while (!pq.isEmpty())
        {
            int cur = pq.poll().idx;
            if (cur == to) {
                break;
            }
            for (ConzonNode curNode : adjacent.get(cur))
            {
                newdist = dist[cur] + curNode.getDist();
                if (dist[curNode.getId()] > newdist && !fin[curNode.getId()])
                {
                    dist[curNode.getId()] = newdist;
                    prev_loc[curNode.getId()] = cur;
                    fin[curNode.getId()] = true;
                    pq.offer(new AStarNode(curNode.getId(), newdist, linedist[curNode.getId()][to]));
                }
            }
        }
    }

    public void printRoute(List<Integer> line)
    {
        for (int i = line.size() - 1; i >= 0; i--)
        {
            if(conzonDict.get(line.get(i)).getName().contains("IC") && !(i == 0 || i == line.size() - 1) )
                continue;
            System.out.print(conzonDict.get(line.get(i)).getName() + (i != 0 ? " - " : "\n"));
        }
    }

    public void printLine(List<Integer> line)
    {
        System.out.print("노선 : ");
        int cur = -1;
        List<String> rst = new ArrayList<>();

        for (int i = line.size() - 2; i >= 0; i--)
        {
            for (ConzonNode iter : adjacent.get(line.get(i)))
            {
                if (iter.getId() == line.get(i + 1) && iter.getLine() != cur)
                {
                    cur = iter.getLine();
                    rst.add(lineInfo.get(cur));
                }
            }
        }
        for (int i = 0; i < rst.size(); i++)
        {
            System.out.print(rst.get(i) + (i != rst.size() - 1 ? " - " : "\n"));
        }
    }

    public int calcToll(List<Integer> line)
    {
        System.out.print("경로 : ");
        double cost = 44.3, rst = 0;
        for (int i = line.size() - 2; i >= 0; i--)
        {
            for (ConzonNode iter : adjacent.get(line.get(i)))
            {
                if (iter.getId() == line.get(i + 1))
                {
                    if (iter.getLanecnt() >= 6)
                    {
                        rst += (double) iter.getDist() / 1000 * cost * 1.2;
                    } else
                    {
                        rst += (double) iter.getDist() / 1000 * cost;
                    }
                }
            }
        }
        rst += 1800;
        return (int) rst;
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
class AStarNode implements Comparable<AStarNode>
{
    int idx;
    int cost, h;

    public AStarNode(int idx, int cost, int h)
    {
        this.idx = idx;
        this.cost = cost;
        this.h = h;
    }

    @Override
    public int compareTo(AStarNode o)
    {
        return Integer.compare(this.cost + this.h, o.cost+ o.h);
    }
}