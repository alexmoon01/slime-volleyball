/**
 * The Ball that will be hit.
 * 
 * @author Alex
 */
public class Ball {

  private static final double G = 1; //The acceleration of gravity
  //How bouncy the slime is
  private static final double COEF_OF_BOUNCE = .2;
  
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
   * Yeets the ball. I hate physics. So much.
   * @param yeeter The slime who is doing the yeeting
   */
  public void yeet(Slime yeeter) {
    double hypotenuse = yeeter.getWidth() / 2 + RADIUS;
    double angleOfImpact = Math.atan2(yPos - yeeter.getyPos(), xPos - yeeter.getxPos());
    double angleOfApproach = Math.atan2(yVel, xVel);
    //Reflects the angle of approach across the angle of impact. Trust me I did math for this.
    double initAngleOfDeparture = (2 * angleOfImpact) - angleOfApproach;
    //Applies this transformation to the ball
    double newXVel = Math.cos(initAngleOfDeparture);
    double newYVel = Math.sin(initAngleOfDeparture);
    //Takes into account the velocity of the yeeter
    newXVel += Math.cos(angleOfImpact) * yeeter.getxVel() * COEF_OF_BOUNCE;
    newYVel += Math.sin(angleOfImpact) * yeeter.getyVel() * COEF_OF_BOUNCE;
    
    xVel = (int)newXVel;
    yVel = (int)newYVel;
    //Fuck physics
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
