package entity;

public class ConzonInfo
{
    private final int id;
    private final int dist;

    public ConzonInfo(int id, int dist)
    {
        this.id = id;
        this.dist = dist;
    }

    public int getId()
    {
        return id;
    }

    public int getDist()
    {
        return dist;
    }
}