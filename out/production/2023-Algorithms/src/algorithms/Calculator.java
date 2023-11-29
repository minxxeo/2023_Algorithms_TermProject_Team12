package algorithms;

public class Calculator
{
   static public int calcTexi(int dist, int time)
    {
        int teximeter = 131;
        int rst = (dist / teximeter) * 100;
        rst += time / 30 * 100;
        return (int)(rst* 1.2) + 4800;
    }
}
