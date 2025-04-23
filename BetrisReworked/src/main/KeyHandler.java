package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
	
	public static boolean upPressed, downPressed, leftPressed, rightPressed, pausePressed, hardDropPressed;

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_UP) {
			upPressed = true;
		}
		if(code == KeyEvent.VK_LEFT) {
			leftPressed = true;
		}
		if(code == KeyEvent.VK_DOWN) {
			downPressed = true;
		}
		if(code == KeyEvent.VK_RIGHT) {
			rightPressed = true;
		}
		if(code == KeyEvent.VK_SPACE) {
			hardDropPressed = true;
		}
		if(code == KeyEvent.VK_C) {
			if(pausePressed) { // if current condition is pausePressed = True, space will set it to false
				pausePressed = false;
				GamePanel.music.play(0, true);
				GamePanel.music.loop();
			}
			else {
				pausePressed = true;
				GamePanel.music.stop();
			}
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}
