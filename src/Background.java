import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Driver and background class
 * 
 * @author Alex
 */
public class Background extends JFrame {

  private Slime player1; //Player 1, controlled with WASD
  private Slime player2; //Player 2, controlled with Arrows
  private Ball ball; //The ball to be hit.
  
  private JPanel mainPanel; //The main display panel
  private final int X_DIM; //The width of the display
  private final int Y_DIM; //The height of the display
  
  public Background() {
    X_DIM = 500;
    Y_DIM = 500;
    
    ball = new Ball(100, 100, 0, 500, 500, 10);
    player1 = new Slime(50, 50, 0, 500, 500, null, null, null, 10, 10, ball);
    
    mainPanel = new JPanel() {
      //This is the main paint method. Whenever the JPanel updates
      //it calls this method, so I'm just gonna hack the mainframe
      //instead of doing something that I'm supposed to do.
      @Override
      public void paint(Graphics g) {
        player1.paint(g);
      }
    };
    this.add(mainPanel); //Adds mainPanel as the main display

    this.setVisible(true);
    mainPanel.setSize(X_DIM, Y_DIM); //Actually setting the size
    this.setSize(mainPanel.getSize());

  }
  
  public static void main(String[] args) {
    new Background();
  }
  
}
