package entity;

public class ConzonInfo
{
    private final int id;
    private final int dist;
    private int time;
    
    public ConzonInfo(int id, int dist, int time)
    {
        this.id = id;
        this.dist = dist;
        this.time = time;
    }

    public int getId()
    {
        return id;
    }

    public int getDist()
    {
        return dist;
    }
    public int getTime() {
    	return time;
    }
    public void setTime(int t) {
    	this.time = t;
    }
}