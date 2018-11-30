/**
 * The Ball that will be hit.
 * 
 * @author Alex
 */
public class Ball {

  private static final int G = 1; //The acceleration of gravity
  
  private int xPos; //X-Position of ball
  private int yPos; //Y-Position of ball
  private int xVel; //X-Velocity of ball (Right is positive)
  private int yVel; //Y-Velocity of ball (Down is positive)
  
  private final int MIN_X; //The x-value of the left boundary
  private final int MAX_X; //The x-value of the right boundary
  private final int MAX_Y; //The floor
  private final int RADIUS; //The radius of the ball
  
  /**
   * Initializes all critical fields in the Ball object
   * 
   * @param x Initial x position
   * @param y Initial y position
   * @param minX Left border position
   * @param maxX Right border position
   * @param maxY Floor position
   * @param radius The radius of the ball
   */
  public Ball (int x, int y, int minX, int maxX, 
      int maxY, int radius) {
    xPos = x;
    yPos = y;
    xVel = 0;
    yVel = 0;
    
    MIN_X = minX;
    MAX_X = maxX;
    MAX_Y = maxY;
    RADIUS = radius;
  }
  
  /**'
   * Yeets the ball
   * @param yeeter The slime who is doing the yeeting
   */
  public void yeet(Slime yeeter) {
    //TODO Make this work, like do math and shit
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
   * Getter for radius
   * @return radius
   */
  public int getRadius() {
    return RADIUS;
  }
  
  
}
