import java.awt.*;
import java.io.Serializable;

public class PaintingNode implements Serializable {
    private int x,y,id;

    public static int RADIUS = 10;

    public PaintingNode(int id, int x, int y){
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public void paint(Graphics g){
        g.setColor(Color.BLUE);
        g.fillOval(x,y,RADIUS,RADIUS);
        g.setColor(Color.LIGHT_GRAY);
        g.drawString(""+id,x,y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PaintingNode && ((PaintingNode)obj).id == id;
    }

    @Override
    public String toString() {
        return "id";
    }
}
