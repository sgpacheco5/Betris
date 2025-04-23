package main;

/* 
- draws play area
- manages tetrominoes
- handles gameplay actions (deleting lines, adding scores)
*/

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Random;

import betromino.Betromino;
import betromino.Block;
import betromino.betromino_Bar;
import betromino.betromino_L;
import betromino.betromino_Linverted;
import betromino.betromino_Square;
import betromino.betromino_T;
import betromino.betromino_Z;
import betromino.betromino_Zinverted;

public class PlayManager {

    // main play area
    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;
    
    // betromino
    Betromino current;
    final int BETROMINO_START_X;
    final int BETROMINO_START_Y;
    Betromino next;
    final int NEXT_BETROMINO_X;
    final int NEXT_BETROMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>(); // keeps track of position of static blocks
    
    // score
    int level = 1;
    int lines;
    int score;
    
    // misc
    public static int dropInterval = 60; // drop betromino every 60 frames
    boolean gameOver;
    

    public PlayManager() {
        // main play area frame
        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2); // left border of game frame
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;
        
        BETROMINO_START_X = left_x + (WIDTH/2) - Block.SIZE; //starts block in center of game panel 
        BETROMINO_START_Y = top_y + Block.SIZE;
        
        NEXT_BETROMINO_X = right_x+175;
        NEXT_BETROMINO_Y = top_y+500;
        
        // set the starting betromino and starting next betromino
        current = pickBetromino();
        current.setXY(BETROMINO_START_X, BETROMINO_START_Y);
        next = pickBetromino();
        next.setXY(NEXT_BETROMINO_X, NEXT_BETROMINO_Y);
    }
    private Betromino pickBetromino() {
    	Betromino betromino = null;
    	int i = new Random().nextInt(7);
    	
    	switch(i) {
    	case 0: betromino = new betromino_L();break;
    	case 1: betromino = new betromino_Linverted();break;
    	case 2: betromino = new betromino_Bar();break;
    	case 3: betromino = new betromino_Square();break;
    	case 4: betromino = new betromino_T();break;
    	case 5: betromino = new betromino_Z();break;
    	case 6: betromino = new betromino_Zinverted();break;
    	}
    	
    	return betromino;
    }

    public void update() {
    	// check if current betromino is active
    	if(current.active == false) {
    		// put betromino in static blocks array when it is deactivated
    		staticBlocks.add(current.b[0]);
    		staticBlocks.add(current.b[1]);
    		staticBlocks.add(current.b[2]);
    		staticBlocks.add(current.b[3]);
    		
    		current.deactivating = false;
    		
    		// check if game over (if the current betromino collided with a block and can't move)
    		if(current.b[0].x == BETROMINO_START_X && current.b[0].y == BETROMINO_START_Y) {
    			gameOver = true;
    			GamePanel.music.stop();
    			GamePanel.se.play(4, false);
    		}
    		
    		// update current with next betromino
    		current = next;
    		current.setXY(BETROMINO_START_X, BETROMINO_START_Y);
    		next = pickBetromino();
    		next.setXY(NEXT_BETROMINO_X, NEXT_BETROMINO_Y);
    		
    		// when a betromino is deactivated, check if lines can be deleted
    		checkDelete();
    	} else {
        	current.update();
    	}
    }
    private void checkDelete() {
    	// top left corner of play area
    	int x = left_x;
    	int y = top_y;
    	int blockCount = 0;
    	int lineCount = 0;
    	// scan from top left to bottom right
    	while(x<right_x && y<bottom_y) {
    		// for each grid space, loop through every block in static blocks array to check if a block is in that spot, and increment blockCount if this is the case
    		for(int i=0; i<staticBlocks.size(); i++) { 
    			if(staticBlocks.get(i).x == x && staticBlocks.get(i).y == y) {
    				// increase the count if there is a static block
    				blockCount++;
    			}
    		}
    		x+=Block.SIZE; // next column (scanning in increments of block size obviously)
    		if(x==right_x) { // at end of row
    			if(blockCount==12) {
    				// reverse loop: start at last index in static blocks and keep decrementing until 0 index is hit
    				for(int i = staticBlocks.size()-1; i>-1; i--) {
    					// remove all blocks in current y line
    					if(staticBlocks.get(i).y==y) {
    						staticBlocks.remove(i);
    					}
    				}
    				lineCount++;
    				lines++;
    				// drop speed - increase if line score hits a certain number
    				if(lines%10 == 0 && dropInterval>1) {
    					level++;
    					if(dropInterval>10) {
    						dropInterval -= 10;
    					}
    					else {
    						dropInterval -= 1;
    					}
    				}
    				
    				// slide blocks above deleted line down by one
    				for(int i=0; i<staticBlocks.size(); i++) {
    					if(staticBlocks.get(i).y<y) {
    						staticBlocks.get(i).y += Block.SIZE;
    					}
    				}
    			}
				// add score
				if(lineCount>0) {
					GamePanel.se.play(1, false);
					int singleLineScore = 10 * level;
					score += singleLineScore * lineCount;
				}
    			
    			blockCount=0; // reset count
    			x = left_x; // reset x to start at left
    			y += Block.SIZE; // increment y to move to next row
    		}
    	}
    	
    }

    public void draw(Graphics2D g2) {
    	
        // Fill play area with light grey background
        g2.setColor(new Color(230, 230, 230));  // Light grey background
        g2.fillRect(left_x -4, top_y-4, WIDTH+8, HEIGHT+8);
        
        // Draw dark grey grid for play area
        g2.setColor(new Color(180, 180, 180));  // Dark grey for grid
        g2.setStroke(new BasicStroke(1f));
        
        // Draw vertical grid lines
        for (int x = left_x; x <= left_x + WIDTH; x += 30) {
            g2.drawLine(x, top_y, x, top_y + HEIGHT);
        }
        
        // Draw horizontal grid lines
        for (int y = top_y; y <= top_y + HEIGHT; y += 30) {
            g2.drawLine(left_x, y, left_x + WIDTH, y);
        }
    	
        // draw play area frame
        g2.setColor(Color.darkGray);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x - 4, top_y - 4, WIDTH + 8, HEIGHT + 8); // since stroke is 4px, have to do these calculations so that collision occurs inside of outline instead of outside
        
        // frame that shows next up betromino
        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setColor(new Color(230, 230, 230));  // Light grey background
        g2.fillRect(x, y, 200, 200);
        g2.setColor(Color.darkGray);
        g2.setFont(new Font("Ariel", Font.PLAIN, 25));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("UP NEXT", x + 50, y + 60);
       
        // score frame
        g2.drawRect(x, top_y, 200, 300);
        g2.setColor(new Color(230, 230, 230));  // Light grey background
        g2.fillRect(x, top_y, 200, 300);
        g2.setColor(Color.darkGray);
        x+=40;
        y= top_y+90;
        g2.drawString("LEVEL: " + level, x, y); y+=70;
        g2.drawString("LINES: " + lines, x, y); y+=70;
        g2.drawString("SCORE: " + score, x, y);
        
        // how to play frame
        x = left_x - 400;
        y= top_y+30;
        g2.drawRect(x, top_y, 300, 400);
        g2.setColor(new Color(230, 230, 230));  // Light grey background
        g2.fillRect(x, top_y, 300, 400);
        g2.setColor(Color.darkGray);
        x+=40;
        g2.drawString("ROTATE:^", x, y);y+=70;
        g2.drawString("LEFT:  <", x, y);y+=70;
        g2.drawString("RIGHT: >", x, y);y+=70;
        g2.drawString("DOWN:  v", x, y);y+=70;
        g2.drawString("HARD DROP: space", x, y);y+=70;
        g2.drawString("PAUSE: c", x, y);
        
        
        // draw current betromino
        if(current != null) {
        	current.draw(g2);
        }
        // draw next betromino
        next.draw(g2);
        
        // draw static betromino array
        for(int i = 0; i<staticBlocks.size(); i++) {
        	staticBlocks.get(i).draw(g2);
        }
        
        // draw pause screen or game over screen
        int boxWidth = 300;
        int boxHeight = 150;
        int pause_x = PlayManager.left_x + (WIDTH - boxWidth) / 2;
        int pause_y = PlayManager.top_y + (HEIGHT - boxHeight) / 2;

        if (gameOver || KeyHandler.pausePressed) {
            // Box fill
            g2.setColor(Color.lightGray);
            g2.fillRect(pause_x, pause_y, boxWidth, boxHeight);

            // Outline
            g2.setColor(Color.darkGray);
            g2.drawRect(pause_x, pause_y, boxWidth, boxHeight);

            // Text
            g2.setColor(Color.darkGray);
            g2.setFont(g2.getFont().deriveFont(50f));

            String text = gameOver ? "GAME OVER" : "PAUSED";
            int textWidth = g2.getFontMetrics().stringWidth(text);
            x = pause_x + (boxWidth - textWidth) / 2;
            y = pause_y + (boxHeight / 2) + 20;
            g2.drawString(text, x, y);
        }
  
    }
}

