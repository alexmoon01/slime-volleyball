import java.awt.Color;
import java.awt.Graphics;
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
  private static int netHeight; //The height of the net
  private static int netWidth; //The width of the net
  
  private JPanel mainPanel; //The main display panel
  private final int X_DIM; //The width of the display
  private final int Y_DIM; //The height of the display
  
  private final int tick; //Amount of milliseconds between each tick.
  
  public Background() {
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    
    X_DIM = 1000;
    Y_DIM = 500;
    
    tick = 50;
    
    ball = new Ball(100, 100, 0, X_DIM - 16, Y_DIM - 25, 10);
    player1 = new Slime(X_DIM / 4, Y_DIM - 25, 0, X_DIM / 2 - 8, Y_DIM - 25, "W", "A", "D", 100, 50, ball);
    player2 = new Slime(3 * X_DIM / 4, Y_DIM - 25, X_DIM / 2 - 8, X_DIM - 16, Y_DIM - 25, "UP", "LEFT","RIGHT", 100, 50, ball);
    netHeight = 100;
    netWidth = 4;
    
    mainPanel = new JPanel() {
      //This is the main paint method. Whenever the JPanel updates
      //it calls this method, so I'm just gonna hack the mainframe
      //instead of doing something that I'm supposed to do.
      @Override
      public void paint(Graphics g) {
        //Filling background
        g.setColor(new Color(17, 57, 122));
        g.fillRect(0, 0, X_DIM, Y_DIM);
        //Drawing sprites
        player1.draw(g);
        player2.draw(g);
        ball.draw(g);
        //Drawing net
        g.setColor(Color.BLACK);
        g.fillRect(X_DIM / 2 - 8 - (netWidth / 2), Y_DIM - 25 - netHeight, netWidth, netHeight);
      }
    };
    this.add(mainPanel); //Adds mainPanel as the main display
    this.add(player1); //Adds the first player
    this.add(player2); //Adds the second player
    this.add(ball); //Adds the ball

    this.setVisible(true);
    mainPanel.setSize(X_DIM, Y_DIM); //Actually setting the size
    this.setSize(mainPanel.getSize());
    
    while(true) {
      try {
        //Pauses the program for a lil bit
        Thread.sleep(tick);
        //Moves players and refreshes the canvas
        player1.move();
        player2.move();
        ball.move();
        mainPanel.repaint();
      } catch (InterruptedException e1) {
        System.exit(1);
      }
      
    }

  }
  
  public static void main(String[] args) {
    new Background();
  }

  /**
   * Getter for the height of the net
   * @return The height of the net
   */
  public static int getNetHeight() {
    return netHeight;
  }

  /**
   * Getter for the width of the net
   * @return The width of the net
   */
  public static int getNetWidth() {
    return netWidth;
  }
  
}
