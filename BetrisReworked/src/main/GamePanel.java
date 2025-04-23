package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
   public static final int WIDTH = 1280;
   public static final int HEIGHT = 720;
   final int FPS = 60;
   Thread gameThread; // manages the execution of logic, updates, and rendering in a separate thread
   PlayManager pm;
   public static Sound music = new Sound();
   public static Sound se = new Sound();

   public GamePanel() {
	   // panel settings
       this.setPreferredSize(new Dimension(1280, 720));
       this.setBackground(new Color(229, 204, 255));
       this.setLayout((LayoutManager) null);
       
       // implement key listener
       this.addKeyListener(new KeyHandler()); // retrieves key input
       this.setFocusable(true);

       pm = new PlayManager();
       
   }
   

   
   public void launchGame() {
       gameThread = new Thread(this);
       gameThread.start(); // calls the run() method
       
       music.play(0, true);
       music.loop();
   }

   @Override
   public void run() {
       // game loop - call update() and repaint() 60 times per second
       double drawInterval = 1000000000 / FPS;
       double delta = 0;
       long lastTime = System.nanoTime();
       long currentTime;

       while (gameThread != null) {
           currentTime = System.nanoTime();

           //calculate time since last draw and restart game loop when a full drawInterval has occurred
           delta += (currentTime - lastTime) / drawInterval;
           lastTime = currentTime;

           if (delta >= 1) {
               update(); // moves game objects, checks input, etc.
               repaint(); // redraws game screen
               delta--; // reset to 0
           }
       }
   }

   private void update() {
	   // only call update when pause condition not true
	   if(KeyHandler.pausePressed == false && pm.gameOver == false) {
	       pm.update();
	   }
   }

   public void paintComponent(Graphics g) {
       super.paintComponent(g);

       Graphics2D g2 = (Graphics2D) g;
       pm.draw(g2);
   }
}
