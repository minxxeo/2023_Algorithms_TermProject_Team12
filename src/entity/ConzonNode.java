package entity;

public class ConzonNode
{
    private final int id;
    private final int dist;
    private final int line;
    private final int lanecnt;

    public int getLanecnt()
    {
        return lanecnt;
    }

    public int getId()
    {
        return id;
    }

    public int getDist()
    {
        return dist;
    }

    public int getLine()
    {
        return line;
    }

    public ConzonNode(int id, int dist, int line, int lanecnt)
    {
        this.id = id;
        this.dist = dist;
        this.line = line;
        this.lanecnt = lanecnt;
    }
}