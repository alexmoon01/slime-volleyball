import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
  
  public Background() {
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    
    X_DIM = 500;
    Y_DIM = 500;
    
    ball = new Ball(100, 100, 0, 500, 500, 10);
    player1 = new Slime(100, 100, 0, 500, 500, "W", "A", "D", 50, 50, ball);
    
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
        super.paintComponents(g);
        System.out.println(player1.getxPos());
        g.drawImage(greenSlime, player1.getxPos(), 
            player1.getyPos(), null);
      }
    };
    this.add(mainPanel); //Adds mainPanel as the main display

    this.setVisible(true);
    mainPanel.setSize(X_DIM, Y_DIM); //Actually setting the size
    this.setSize(mainPanel.getSize());
    
    while(true) {
      player1.move();
      mainPanel.repaint();
    }

  }
  
  public static void main(String[] args) {
    new Background();
  }
  
}
