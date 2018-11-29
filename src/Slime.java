/**
 * A player, controlled for now by three keys
 * 
 * @author Alex
 */
public class Slime {

  private int xPos; //X-Position of Slime
  private int yPos; //Y-Position of Slime
  private int xVel; //X-Velocity of Slime (Right is positive)
  private int yVel; //Y-Velocity of Slime (Up is positive)
  
  private final int MIN_X; //The x-value of the left boundary
  private final int MAX_X; //The x-value of the right boundary
  private final int MAX_Y; //The floor
  private final int WIDTH; //The width of the slime
  private final int HEIGHT; //The height of the slime
  
  private final int G; //The acceleration of gravity
  
  private final char UP_KEY; //Character that represents keybinding of jump
  private final char LEFT_KEY; //Character that represents keybinding of moving left
  private final char RIGHT_KEY; //Character that represents keybinding of moving right
  
  private Ball ball; //Private instance of the ball for easy access
  
  public Slime(int x, int y, int minX, int maxX, int maxY, char up, int width, int height,
      char left, char right, Ball ball) {
    xPos = x;
    yPos = y;
    xVel = 0;
    yVel = 0;
    
    WIDTH = width;
    HEIGHT = height;
    //Corrects boundaries for width and height of slime
    MIN_X = minX + (WIDTH / 2);
    MAX_X = maxX - (WIDTH / 2);
    MAX_Y = maxY - (HEIGHT / 2);
    
    UP_KEY = up;
    LEFT_KEY = left;
    RIGHT_KEY = right;
    
    G = 1;
    
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
    if (yPos < MAX_Y) {
      yVel += G;
    } else {
      yPos = MAX_Y;
      yVel = 0;
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
   * @return character up key binding
   */
  public char getUpKey() {
    return UP_KEY;
  }

  /**
   * Getter for left key binding
   * @return character left key binding
   */
  public char getLeftKey() {
    return LEFT_KEY;
  }

  /**
   * Getter for right key binding
   * @return character right key binding
   */
  public char getRightKey() {
    return RIGHT_KEY;
  }

  /**
   * Getter for ball
   * @return ball
   */
  public Ball getBall() {
    return ball;
  }
}

/**
 * The eye of the slime
 * 
 * @author Alex
 */
class Eye {
  
}
