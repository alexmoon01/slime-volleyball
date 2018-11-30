import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * A player, controlled for now by three keys
 * 
 * @author Alex
 */
public class Slime extends JComponent {

  private static final double G = 1; //The acceleration of gravity
  
  private int xPos; //X-Position of Slime
  private int yPos; //Y-Position of Slime
  private int xVel; //X-Velocity of Slime (Right is positive)
  private int yVel; //Y-Velocity of Slime (Down is positive)
  
  private final int MIN_X; //The x-value of the left boundary
  private final int MAX_X; //The x-value of the right boundary
  private final int MAX_Y; //The floor
  private final int WIDTH; //The width of the slime
  private final int HEIGHT; //The height of the slime
    
  private final String UP_KEY; //Keybinding of jump
  private final String LEFT_KEY; //Keybinding of moving left
  private final String RIGHT_KEY; //Keybinding of moving right
  
  private Ball ball; //Private instance of the ball for easy access
  
  private BufferedImage greenSlime;
  
  /**
   * A mess of a constructor that basically instantiates anything that could even possibly
   * have to do with the slime. Don't worry it makes sense.
   * 
   * @param x The starting x position of the slime
   * @param y The starting y position of the slime
   * @param minX The furthest left the slime is allowed to go
   * @param maxX The furthest right the slime is allowed to go
   * @param maxY The downest the slime is allowed to go
   * @param up The keybinding for the Up key
   * @param left The keybinding for the Left key
   * @param right The keybinding for the Right key
   * @param width The width of the slime
   * @param height The height of the slime
   * @param ball The ball to be hit.
   */
  public Slime(int x, int y, int minX, int maxX, int maxY, String up, 
      String left, String right, int width, int height, Ball ball) {
    xPos = x;
    yPos = y;
    xVel = 0;
    yVel = 0;
    //Imports the image of the slime cus APPARENTLY Java can't draw semicircles
    try {
      greenSlime = ImageIO.read(new File("GreenSlime.png"));
    } catch (IOException e1) {
      System.out.println("Oops");
      greenSlime = null;
    }
    
    WIDTH = width;
    HEIGHT = height;
    //Corrects boundaries for width and height of slime
    MIN_X = minX;
    MAX_X = maxX + WIDTH;
    MAX_Y = maxY + HEIGHT;
    
    UP_KEY = up;
    LEFT_KEY = left;
    RIGHT_KEY = right;
    //Defining the jump action
    Action jump = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (yPos >= MAX_Y) {
          yVel = -10;
          yPos--;
        }
      }
    };
    //Defining the move left action
    Action moveLeft = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        xVel = -10;
        System.out.println("Can I get a hell yes?");
      }
    };
    //Defining the move right action
    Action moveRight = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        xVel = 10;
      }
    };
    //Defining the stop action
    Action stop = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        xVel = 0;
      }
    };
    //Binding the jump action to the up key
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(UP_KEY), "Up Pressed");
    this.getActionMap().put("Up Pressed", jump);
    //Binding the move left action the the left key
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(LEFT_KEY), "Move Left");
    this.getActionMap().put("Move Left", moveLeft);
    //Binding the move right action to the right key
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(RIGHT_KEY), "Move Right");
    this.getActionMap().put("Move Right", moveRight);
    //Binding the stop action to the release of either the left or right key
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(RIGHT_KEY + " released"), "Stop");
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(LEFT_KEY + " released"), "Stop");
    this.getActionMap().put("Stop", stop);
    
    
    this.ball = ball;
  }
  
  /**
   * Moves the slime based on current x and y velocity
   */
  public void move() {
    //If statement prevents slime from leaving boundaries
    if (!(xPos == MAX_X && xVel > 0) && !(xPos == MIN_X && xVel < 0)) {
      xPos += xVel;
    }
    //Moves slime up and down, then resolves gravity and floor
    yPos += yVel;
    if (yPos < MAX_Y - HEIGHT - 200) {
      yVel += G;
    } else {
      yPos = MAX_Y - HEIGHT - 100;
      yVel = 0;
    }
    hitBall();
  }
  
  /**
   * Checks if the slime is touching the ball. If so, hucks the ball
   * wherever is appropriate. Redefines the center of the slime as 
   * the middle of its base, to take advantage of the fact that the
   * slime is semicircular, reducing the calculations to relatively
   * simple radius calculations.
   */
  public void hitBall() {
    int circleCenterY = yPos + HEIGHT; //Redefining the wheel
    int circleCenterX = xPos + WIDTH / 2;
    
    //Distance = sqrt((ballX - thisX)^2 + (ballY - thisY)^2)
    double delXSquared = Math.pow(ball.getxPos() - circleCenterX, 2);
    double delYSquared = Math.pow(ball.getyPos() - circleCenterY, 2);
    double trueDistance = Math.sqrt(delXSquared + delYSquared);
    
    //If the ball and the slime are touching, yeets the ball into the nether realm
    if (trueDistance <= (WIDTH / 2) + ball.getRadius()) {
      ball.yeet(this);
    }
  }

  /**
   * Getter for x position
   * @return x position
   */
  public int getxPos() {
    return xPos;
  }

  /**
   * Getter for y position
   * @return y position
   */
  public int getyPos() {
    return yPos;
  }

  /**
   * Getter for x velocity
   * @return x velocity
   */
  public int getxVel() {
    return xVel;
  }

  /**
   * Getter for y velocity
   * @return y velocity
   */
  public int getyVel() {
    return yVel;
  }

  /**
   * Getter for up key binding
   * @return up key binding
   */
  public String getUpKey() {
    return UP_KEY;
  }

  /**
   * Getter for left key binding
   * @return left key binding
   */
  public String getLeftKey() {
    return LEFT_KEY;
  }

  /**
   * Getter for right key binding
   * @return right key binding
   */
  public String getRightKey() {
    return RIGHT_KEY;
  }

  /**
   * Getter for ball
   * @return ball
   */
  public Ball getBall() {
    return ball;
  }
  
  /**
   * Getter for width
   * @return width
   */
  public int getWidth() {
    return WIDTH;
  }
  
  /**
   * Getter for height
   * @return height
   */
  public int getHeight() {
    return HEIGHT;
  }
}

/**
 * The eye of the slime
 * 
 * @author Alex
 */
class Eye {
  
}
