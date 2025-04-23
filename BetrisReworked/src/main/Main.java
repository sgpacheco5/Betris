package main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {


        JFrame window = new JFrame("Betris"); // jframe is a java class
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false); // fixed window size

        // add game panel to window
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack(); // makes gp same size as window

        window.setLocationRelativeTo(null); // window placed in center of screen
        window.setVisible(true);

        gp.launchGame();

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (gp.gameThread != null && gp.gameThread.isAlive()) {
                    gp.gameThread = null; // let the game loop exit
                    System.out.println("Game thread stopped.");
                }
            }
        });


    }

}

