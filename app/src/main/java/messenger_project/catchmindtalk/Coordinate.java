package messenger_project.catchmindtalk;

public class Coordinate {

    public float oldX;
    public float oldY;
    public float X;
    public float Y;
    public float strokeWidth;
    public int color;

    public Coordinate (float oldx,float oldy,float x, float y, float SW, int Color){

        this.oldX = oldx;
        this.oldY = oldy;
        this.X = x;
        this.Y = y;
        this.strokeWidth = SW;
        this.color = Color;

    }


}
