package algorithms;

import entity.ConzonNode;

import java.util.List;

public class Calculator
{
    /*
     * Name        : calcTexi
     * Author      : MinSeok Choi
     * Date        : 2023-11-27
     * argument    : int, int
     * return      : int
     * description : Calculate Texi fee of distance and time.
     */
    static public int calcTexi(int dist, int time)
    {
        int teximeter = 131;
        int rst = (dist / teximeter) * 100;
        rst += time / 30 * 100;
        return (int) (rst * 1.2) + 4800;
    }

    /*
     * Name        : calcToll
     * Author      : MinSeok Choi
     * Date        : 2023-11-27
     * argument    : List<Integer>, List<List<ConzonNode>>
     * return      : int
     * description : Get tollgate fee by real data
     */
    public static int calcToll(List<Integer> line, List<List<ConzonNode>> adjacent)
    {
        System.out.print("Tollgate Fee : ");
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