import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Driver and background class
 * 
 * @author Alex
 */
public class Background extends JFrame {

  private Slime player1; //Player 1, controlled with WASD
  private Slime player2; //Player 2, controlled with Arrows
  private Ball ball; //The ball to be hit.
  
  private BufferedImage greenSlime;
  
  private JPanel mainPanel; //The main display panel
  private final int X_DIM; //The width of the display
  private final int Y_DIM; //The height of the display
  
  private final int tick; //Amount of milliseconds between each tick.
  
  public Background() {
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    
    X_DIM = 1000;
    Y_DIM = 500;
    
    tick = 10;
    
    ball = new Ball(100, 100, 0, 500, 500, 10);
    player1 = new Slime(100, 100, 0, 500, Y_DIM - 25, "W", "A", "D", 100, 50, ball);
    
    try {
      greenSlime = ImageIO.read(new File("GreenSlime.png"));
    } catch (IOException e) {
      greenSlime = null;
    }
    
    mainPanel = new JPanel() {
      //This is the main paint method. Whenever the JPanel updates
      //it calls this method, so I'm just gonna hack the mainframe
      //instead of doing something that I'm supposed to do.
      @Override
      public void paint(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, X_DIM, Y_DIM);
        player1.draw(g);
      }
    };
    this.add(mainPanel); //Adds mainPanel as the main display
    this.add(player1); //Adds the first player

    this.setVisible(true);
    mainPanel.setSize(X_DIM, Y_DIM); //Actually setting the size
    this.setSize(mainPanel.getSize());
    
    while(true) {
      try {
        //Pauses the program for a lil bit
        Thread.sleep(tick);
        //Moves players and refreshes the canvas
        player1.move();
        mainPanel.repaint();
      } catch (InterruptedException e1) {
        System.exit(1);
      }
      
    }

  }
  
  public static void main(String[] args) {
    new Background();
  }
  
}
