package betromino;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Block extends Rectangle{
	public int x, y;
	public static final int SIZE = 30;
	public Color c;
	
	public Block(Color c) {
		this.c=c;
	}
    public void draw(Graphics2D g2) {
        // Fill the entire block with the color
        g2.setColor(c);
        g2.fillRect(x, y, SIZE, SIZE);
        
        // Draw the outline exactly on the grid lines
        g2.setColor(Color.darkGray);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRect(x, y, SIZE, SIZE);
    }
}
