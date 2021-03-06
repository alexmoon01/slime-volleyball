import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * A player, controlled for now by three keys
 * 
 * @author Alex
 */
public class Slime extends JComponent {

  public static final double G = 1; //The acceleration of gravity
  
  private int xPos; //X-Position of Slime
  private int yPos; //Y-Position of Slime
  private int xVel; //X-Velocity of Slime (Right is positive)
  private int yVel; //Y-Velocity of Slime (Down is positive)
  private int moveSpeed; //The speed of the slime when moving side to side
  private int jumpSpeed; //The initial speed of the slime when it jumps
  
  private final int MIN_X; //The x-value of the left boundary
  private final int MAX_X; //The x-value of the right boundary
  private final int MAX_Y; //The floor
  private final int WIDTH; //The width of the slime
  private final int HEIGHT; //The height of the slime
    
  private final String UP_KEY; //Keybinding of jump
  private final String LEFT_KEY; //Keybinding of moving left
  private final String RIGHT_KEY; //Keybinding of moving right
  private boolean rightPressed; //Keeps track of whether the right key is pressed
  private boolean leftPressed; //Keeps track of whether the left key is pressed
  
  private Ball ball; //Private instance of the ball for easy access
  private int ticksSinceBallHit; //Removes the possibility of immediately catching the ball
  private final int TICKS_THRESHOLD; //The minimum amount of ticks between ball hits.
  private int score; //The score of this slime
  
  private BufferedImage slime; //The image of the slime
  
  /**
   * A mess of a constructor that basically instantiates anything that could even possibly
   * have to do with the slime. Don't worry it makes sense.<p>
   * 
   * The action definitions may look a bit confusing, but the basic idea is:<p>
   * 
   * 1. Define each action by defining an anonymous java.awt.AbstractAction.<br>
   * 2. Assign each keyboard input to an action defined by a string.<br>
   * 3. Associate each string with the above-defined action.
   * 
   * 
   * @param x The starting x position of the slime
   * @param y The starting y position of the slime
   * @param minX The furthest left the slime is allowed to go
   * @param maxX The furthest right the slime is allowed to go
   * @param maxY The downest the slime is allowed to go
   * @param up The keybinding for the Up key ("W" or "UP")
   * @param left The keybinding for the Left key ("A" or "LEFT")
   * @param right The keybinding for the Right key ("D" or "RIGHT")
   * @param width The width of the slime
   * @param height The height of the slime
   * @param ball The ball to be hit.
   */
  @SuppressWarnings("serial")
  public Slime(int x, int y, int minX, int maxX, int maxY, String up, 
      String left, String right, int width, int height, Ball ball) {
    xPos = x;
    yPos = y;
    xVel = 0;
    yVel = 0;
    moveSpeed = 10;
    jumpSpeed = 15;
    //Imports the image of the slime cus APPARENTLY Java can't draw semicircles
    if (up.equals("W")) {
      try {
        slime = ImageIO.read(new File("GreenSlime.png"));
      } catch (IOException e1) {
        System.out.println("Oops");
        slime = null;
      }
    } else {
      try {
        slime = ImageIO.read(new File("RedSlime.png"));
      } catch (IOException e1) {
        System.out.println("Oops");
        slime = null;
      }
    }
    
    score = 0;
    
    WIDTH = width;
    HEIGHT = height;
    //Corrects boundaries for width and height of slime
    MIN_X = minX;
    MAX_X = maxX - WIDTH;
    MAX_Y = maxY - HEIGHT;
    TICKS_THRESHOLD = 4;
    ticksSinceBallHit = TICKS_THRESHOLD + 1;
    
    UP_KEY = up;
    LEFT_KEY = left;
    RIGHT_KEY = right;
    rightPressed = false;
    leftPressed = false;
    
    //Defining the jump action
    Action jump = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (yPos >= MAX_Y) {
          yVel = -jumpSpeed;
          yPos--;
        }
      }
    };
    //Defining the move left action
    Action moveLeft = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        xVel = -moveSpeed;
        leftPressed = true;
      }
    };
    //Defining the move right action
    Action moveRight = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        xVel = moveSpeed;
        rightPressed = true;
      }
    };
    //Defining the stop action for moving left
    Action stopLeft = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        leftPressed = false;
        if (rightPressed) {
          xVel = moveSpeed;
        } else {
          xVel = 0;
        }
      }
    };
    //Defining the stop action for moving right (trust me this is necessary)
    Action stopRight = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        rightPressed = false;
        if (leftPressed) {
          xVel = -moveSpeed;
        } else {
          xVel = 0;
        }
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
    
    //Stops moving left when left key released
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released " + LEFT_KEY), "Stop Left");
    this.getActionMap().put("Stop Left", stopLeft);
    
    //Stops moving right when right key released
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released " + RIGHT_KEY), "Stop Right");
    this.getActionMap().put("Stop Right", stopRight);
    
    
    this.ball = ball;
  }
  
  /**
   * Draws the slime on the given canvas.
   * @param g The graphics object upon which this will draw itself.
   */
  public void draw(Graphics g) {
    g.setColor(Color.BLACK);
    int fontSize = 40;
    g.setFont(new Font("Times New Roman", Font.BOLD, fontSize));
    g.drawString(String.valueOf(score), (MIN_X + MAX_X) / 2 + fontSize, 40);
    g.drawImage(slime, xPos, yPos, WIDTH, HEIGHT, null);
  }
  
  /**
   * Moves the slime based on current x and y velocity
   */
  public void move() {
    //If statement prevents slime from leaving boundaries
    if (!(xPos >= MAX_X && xVel > 0) && !(xPos <= MIN_X && xVel < 0)) {
      xPos += xVel;
    }
    if (xPos >= MAX_X) {
      xPos = MAX_X;
    }
    if (xPos <= MIN_X) {
      xPos = MIN_X;
    }
    //Moves slime up and down, then resolves gravity and floor
    yPos += yVel;
    if (yPos < MAX_Y) {
      yVel += G; //Accelerates for gravity
    } else {
      yPos = MAX_Y;
      yVel = 0;
    }
    
    //Ensures that the slime doesn't hit the ball, 
    //immediately catch up, and hit it again. 
    if (ticksSinceBallHit > TICKS_THRESHOLD && hitBall()) {
      ticksSinceBallHit = 0;
    } else {
      ticksSinceBallHit++;
    }
    
    //Checks if the ball is grounded.
    if(ball.isGrounded()) {
      //Increments score if ball is on the right side of the net
      if (ball.getxPos() > MAX_X + WIDTH || ball.getxPos() + ball.getRadius() * 2 < MIN_X) {
        score++;
        ball.respawn(this);
      }
    }
  }
  
  /**
   * Checks if the slime is touching the ball. If so, hucks the ball
   * wherever is appropriate. Redefines the center of the slime as 
   * the middle of its base, to take advantage of the fact that the
   * slime is semicircular, reducing the calculations to relatively
   * simple radius calculations.
   * @return Whether the ball was hit.
   */
  public boolean hitBall() {
    double circleCenterY = (double)yPos + (double)HEIGHT; //Redefining the wheel
    double circleCenterX = (double)xPos + (double)WIDTH / 2;
    double ballCenterX = ball.getxPos() + ball.getRadius(); //Redefining the wheel AGAIN
    double ballCenterY = ball.getyPos() + ball.getRadius();
    
    //Distance = sqrt((ballX - thisX)^2 + (ballY - thisY)^2)
    double delXSquared = Math.pow(ballCenterX - circleCenterX, 2);
    double delYSquared = Math.pow(ballCenterY - circleCenterY, 2);
    double trueDistance = Math.sqrt(delXSquared + delYSquared);
    
    //If the ball and the slime are touching, yeets the ball into the nether realm
    if (trueDistance <= HEIGHT + ball.getRadius() && ballCenterY <= circleCenterY) {
      ball.yeet(this);
      return true;
    }
    return false;
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
