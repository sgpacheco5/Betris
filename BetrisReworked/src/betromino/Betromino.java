package betromino;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GamePanel;
import main.KeyHandler;
import main.PlayManager;

public class Betromino {
	public Block b[] = new Block[4]; // block array
	public Block tempB[] = new Block[4];
	int autoDropCounter = 0;
	public int direction = 1; // initializes at 1, but can also be 2, 3, or 4
	boolean leftCollision, rightCollision, bottomCollision;
	public boolean active = true;
	public boolean deactivating; // REMEMBER TO SET NON_PACKAGE PRIVATE VARIABLES AS PUBLIC
	int deactivateCounter = 0;
	
	public void create(Color c) {
		b[0] = new Block(c);
		b[1] = new Block(c);
		b[2] = new Block(c);
		b[3] = new Block(c);
		tempB[0] = new Block(c);
		tempB[1] = new Block(c);
		tempB[2] = new Block(c);
		tempB[3] = new Block(c);
	}
	public void setXY(int x, int y) {}
	public void updateXY(int direction) {
		checkRotationCollision();
		if(leftCollision == false && rightCollision == false && bottomCollision == false) {
			this.direction = direction; // oop refresher: "this" accesses fields and methods of direction object
			// further explanation: "this" sets this betromino that is calling updateXY to have direction in parameter
			b[0].x = tempB[0].x; // using a separate tempB array in order to be able to handle collission upon rotation update
			b[0].y = tempB[0].y;
			b[1].x = tempB[1].x;
			b[1].y = tempB[1].y;
			b[2].x = tempB[2].x;
			b[2].y = tempB[2].y;
			b[3].x = tempB[3].x;
			b[3].y = tempB[3].y;
		}
	}
	public void getDirection1() {} // left all of these blank since overide them in every sub betromino class
	public void getDirection2() {}
	public void getDirection3() {}
	public void getDirection4() {}
	
	public void checkMovementCollision() {
		leftCollision = false;
		rightCollision = false;
		bottomCollision = false;
		//static blocks collision check
		checkStaticBlockCollision();
		
		// frame collision check
		// left wall - if block array contains a block whose x collides with left wall x, 
		for(int i=0; i<b.length; i++) {
			if(b[i].x == PlayManager.left_x) {
				leftCollision = true;
			}
		}
		// right wall (add block size to get right of block coords)
		for(int i=0; i<b.length; i++) {
			if(b[i].x+Block.SIZE == PlayManager.right_x) {
				rightCollision = true;
			}
		}
		// bottom wall
		for(int i=0; i<b.length; i++) {
			if(b[i].y+Block.SIZE == PlayManager.bottom_y) {
				bottomCollision = true;
			}
		}
	}
	public void checkRotationCollision() {
		leftCollision = false;
		rightCollision = false;
		bottomCollision = false;
		//static blocks collision check
		checkStaticBlockCollision();

		// frame collision check
		// left wall
		for(int i=0; i<b.length; i++) {
			if(tempB[i].x < PlayManager.left_x) { // check if tempB left x is outside left of frame
				leftCollision = true;
			}
		}
		// right wall (add block size to get right of block coords)
		for(int i=0; i<b.length; i++) {
			if(tempB[i].x+Block.SIZE > PlayManager.right_x) { // check if tempB right x is outside right of frame
				rightCollision = true;
			}
		}
		// bottom wall
		for(int i=0; i<b.length; i++) {
			if(tempB[i].y+Block.SIZE > PlayManager.bottom_y) {
				bottomCollision = true;
			}
		}
	}
	
	public void checkStaticBlockCollision() {
		for(int i = 0; i<PlayManager.staticBlocks.size(); i++) {
			int targetX = PlayManager.staticBlocks.get(i).x;
			int targetY = PlayManager.staticBlocks.get(i).y;
			// check down
			for(int ii=0; ii<b.length; ii++) {
				if(b[ii].y + Block.SIZE == targetY && b[ii].x == targetX) { // if bottom of block is same position of top of static block, and they are in the same position horizontally, they collided
					bottomCollision=true;
				}
			}
			// check left
			for(int ii=0; ii<b.length; ii++) {
				if(b[ii].x-Block.SIZE==targetX && b[ii].y==targetY) {
					leftCollision=true;
				}
			}
			// check right
			for(int ii=0; ii<b.length; ii++) {
				if(b[ii].x+Block.SIZE==targetX && b[ii].y==targetY) {
					rightCollision=true;
				}
			}
			

        }
	}
	
	public void hardDrop() {
	    // Keep moving down until we hit something
	    while (!bottomCollision) {
	        // Move down one block
	        b[0].y += Block.SIZE;
	        b[1].y += Block.SIZE;
	        b[2].y += Block.SIZE;
	        b[3].y += Block.SIZE;
	        
	        // Check for collision after moving
	        checkMovementCollision();
	    }
	    	if (bottomCollision) {
	    	active=false;
	    	GamePanel.se.play(3, false);
	    }
	    
	}
	
	public void update() {
		
		if(deactivating) {
			deactivating();
		}
		if(KeyHandler.hardDropPressed) {
			hardDrop();
			KeyHandler.hardDropPressed = false;
			return;
		}
		
		//move the betromino
		if(KeyHandler.upPressed) {
			GamePanel.se.play(5, false);
			switch(direction) {
			case 1: getDirection2();break;
			case 2: getDirection3();break;
			case 3: getDirection4();break;
			case 4: getDirection1();break;
			}
			KeyHandler.upPressed = false;
		}
		
		checkMovementCollision(); // check for collision before handling movement
		
		if(KeyHandler.downPressed) {
			if(bottomCollision == false) {
				b[0].y += Block.SIZE;
				b[1].y += Block.SIZE;
				b[2].y += Block.SIZE;
				b[3].y += Block.SIZE;
			}
			autoDropCounter = 0; // when moved down, reset the autoDropCounter
			KeyHandler.downPressed = false;
		}
		if(KeyHandler.leftPressed) {
			if(leftCollision == false) {
				b[0].x -= Block.SIZE;
				b[1].x -= Block.SIZE;
				b[2].x -= Block.SIZE;
				b[3].x -= Block.SIZE;
			}
			KeyHandler.leftPressed = false;
		}
		if(KeyHandler.rightPressed) {
			if(rightCollision == false) {
				b[0].x += Block.SIZE;
				b[1].x += Block.SIZE;
				b[2].x += Block.SIZE;
				b[3].x += Block.SIZE;
			}
			KeyHandler.rightPressed = false;
		}
		// deactivate betromino when it collides with bottom of play area
		if(bottomCollision) {
			if(deactivating == false) {
				GamePanel.se.play(3, false);
			}
			deactivating = true;
		}
		else {
			autoDropCounter++; // increases every frame
			if(autoDropCounter == PlayManager.dropInterval) {
				// move down
				b[0].y += Block.SIZE;
				b[1].y += Block.SIZE;
				b[2].y += Block.SIZE;
				b[3].y += Block.SIZE;
				autoDropCounter = 0;
			}
		}
	}
	private void deactivating() {
		deactivateCounter++;
		// wait 30 frames until deactivating
		if(deactivateCounter == 30) {
			deactivateCounter = 0;
			checkMovementCollision();// checks if bottom of betromino is still colliding with static or frame
			// if above is true, deactivate
			if(bottomCollision) {
				active=false;
			}
		}
	}
	public void draw(Graphics2D g2) {
		g2.setColor(b[0].c);
		g2.fillRect(b[0].x, b[0].y, Block.SIZE, Block.SIZE);
		g2.fillRect(b[1].x, b[1].y, Block.SIZE, Block.SIZE);
		g2.fillRect(b[2].x, b[2].y, Block.SIZE, Block.SIZE);
		g2.fillRect(b[3].x, b[3].y, Block.SIZE, Block.SIZE);
	}

}
