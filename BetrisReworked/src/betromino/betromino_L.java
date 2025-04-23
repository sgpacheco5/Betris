package betromino;

import java.awt.Color;

public class betromino_L extends Betromino{
	public betromino_L() {
		create(new Color(153, 204, 255));
	}
	
	public void setXY(int x, int y) {
		// o
		// o <- the block that all other blocks are rotated relative to (set this one to b[0])
		// o o
		b[0].x = x;
		b[0].y = y;
		b[1].x = b[0].x;
		b[1].y = b[0].y - Block.SIZE; // top of l
		b[2].x = b[0].x;
		b[2].y = b[0].y + Block.SIZE; // bottom left
		b[3].x = b[0].x + Block.SIZE;
		b[3].y = b[0].y + Block.SIZE; // bottom right
	}
	public void getDirection1() {
		tempB[0].x = b[0].x;
		tempB[0].y = b[0].y;
		tempB[1].x = b[0].x;
		tempB[1].y = b[0].y - Block.SIZE;
		tempB[2].x = b[0].x;
		tempB[2].y = b[0].y + Block.SIZE;
		tempB[3].x = b[0].x + Block.SIZE;
		tempB[3].y = b[0].y + Block.SIZE;
		
		updateXY(1);
	}
	public void getDirection2() {
		// o o o
		// o
		tempB[0].x = b[0].x;
		tempB[0].y = b[0].y;
		tempB[1].x = b[0].x + Block.SIZE;
		tempB[1].y = b[0].y;
		tempB[2].x = b[0].x - Block.SIZE;
		tempB[2].y = b[0].y;
		tempB[3].x = b[0].x - Block.SIZE;
		tempB[3].y = b[0].y + Block.SIZE;
		
		updateXY(2);
	}
	public void getDirection3() {
		// o o
		//   o
		//   o
		tempB[0].x = b[0].x;
		tempB[0].y = b[0].y;
		tempB[1].x = b[0].x;
		tempB[1].y = b[0].y + Block.SIZE;
		tempB[2].x = b[0].x;
		tempB[2].y = b[0].y - Block.SIZE;
		tempB[3].x = b[0].x - Block.SIZE;
		tempB[3].y = b[0].y - Block.SIZE;
		
		updateXY(3);
	}
	public void getDirection4() {
		//     o
		// o o o
		tempB[0].x = b[0].x;
		tempB[0].y = b[0].y;
		tempB[1].x = b[0].x - Block.SIZE;
		tempB[1].y = b[0].y;
		tempB[2].x = b[0].x + Block.SIZE;
		tempB[2].y = b[0].y;
		tempB[3].x = b[0].x + Block.SIZE;
		tempB[3].y = b[0].y - Block.SIZE;
		
		updateXY(4);
	}

}
