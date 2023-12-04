package entity;

public class ConzonNode
{
    private final int id;
    private final int dist;
    private final int line;
    private final int lanecnt;
    private int time;

    /*
     * Name        : setTime
     * Author      : Junseo Choi
     * Date        : 2023-11-27
     * argument    : int
     * return      : void
     * description : Set the time between two highway conzones.
    */
    public void setTime(int time)
    {
        this.time = time;
    }   
    /*
     * Name        : getTime
     * Author      : Junseo Choi
     * Date        : 2023-11-27
     * argument    : none
     * return      : int
     * description : Gets the time taken between two highway conzones.
    */
    public int getTime()
    {
        return time;
    }

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