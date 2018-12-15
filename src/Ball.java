import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
 * The Ball that will be hit.
 * 
 * @author Alex
 */
public class Ball extends JComponent {

  private static final double G = Slime.G / 2; //The acceleration of gravity
  //How bouncy the slime is (ABSOLUTELY DO NOT MAKE THIS GREATER THAN 1 IF YOU HATE FUN)
  private static final double COEF_OF_BOUNCE = .8;
  //How sticky the slime is (Keep under one unless u rly wanna yeet this ball)
  private static final double COEF_OF_FRIC = .25;
  
  private final int START_X; //Starting x
  private final int START_Y; //Starting y
  
  private int xPos; //X-Position of ball
  private int yPos; //Y-Position of ball
  private double xVel; //X-Velocity of ball (Right is positive)
  private double yVel; //Y-Velocity of ball (Down is positive)
  
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
    START_X = x;
    START_Y = y;
    xPos = START_X;
    yPos = START_Y;
    xVel = 0;
    yVel = 0;
    
    MIN_X = minX;
    MAX_X = maxX;
    MAX_Y = maxY;
    RADIUS = radius;
  }
  
  /**
   * Moves the ball according to its position and velocity.
   */
  public void move() {
    //Doesn't move if it's grounded.
    if (!isGrounded()) {
      //Bounces off the walls
      if ((xPos + 2 * RADIUS >= MAX_X && xVel > 0) || 
          (xPos <= MIN_X && xVel < 0)) {
        xVel *= -1;
      }
      xPos += xVel;
      yPos += yVel;
      yVel += G;
      //Bounces off the net
      if (xPos <= MAX_X / 2 + Background.getNetWidth()
          && xPos + 2 * RADIUS >= MAX_X / 2 - Background.getNetWidth()
          && yPos + 2 * RADIUS >= MAX_Y - Background.getNetHeight()) {
        //Bounces off the top of the net
        if (yPos + RADIUS <= MAX_Y - Background.getNetHeight()) {
          yVel = -1 * Math.abs(yVel);
        } else {
          //Bounces off the side of the net
          xVel *= -1;
        }
      }
    }
  }
  
  /**
   * Checks whether the ball is on the ground.
   * @return True if the ball is on the ground.
   */
  public boolean isGrounded() {
    return yPos + (RADIUS * 2) >= MAX_Y;
  }
  
  /**
   * Respawns the slime over the winner of the last point
   * @param pointWinner The slime who won the point
   */
  public void respawn(Slime pointWinner) {
    yPos = START_Y;
    xPos = pointWinner.getxPos() + pointWinner.getWidth() / 2 - RADIUS;
    xVel = 0;
    yVel = 0;
  }
  
  /**
   * Draws the ball on the given canvas
   * @param g The canvas on which the method will paint.
   */
  public void draw(Graphics g) {
    g.setColor(Color.BLACK);
    g.fillOval(xPos, yPos, RADIUS * 2, RADIUS * 2);
  }
  
  /**'
   * Yeets the ball. I hate physics. So much.
   * @param yeeter The slime who is doing the yeeting
   */
  public void yeet(Slime yeeter) {
    
    //The true center of the slime
    double yeetX = yeeter.getxPos() + (yeeter.getWidth() / 2);
    double yeetY = yeeter.getyPos() + yeeter.getHeight();
    //The true center of the ball
    double ballTrueX = xPos + RADIUS;
    double ballTrueY = yPos + RADIUS;
    //The initial distance between the ball and the slime
    double trueDistance = Math.sqrt(Math.pow(yeetX - ballTrueX, 2) + Math.pow(yeetY - ballTrueY, 2));
    
    //The angle at which the ball is moving
    double angleOfApproach = Math.atan2((double)yVel, (double)xVel);
    
    //Oh my GOD this was annoying. Iterates position back along velocity vector until on the surface of the slime.
    while (trueDistance < yeeter.getHeight() + RADIUS) {
      ballTrueX -= Math.cos(angleOfApproach);
      ballTrueY -= Math.sin(angleOfApproach);
      trueDistance = Math.sqrt(Math.pow(yeetX - ballTrueX, 2) + Math.pow(yeetY - ballTrueY, 2));
    }
    
    //Finding the magnitude of the velocity of the ball
    double magVelocity = Math.sqrt(Math.pow(xVel, 2) + Math.pow(yVel, 2));
    
    //The angle at which the ball meets the slime
    double angleOfImpact = Math.atan2(ballTrueY - yeetY, ballTrueX - yeetX);
    
    //Reflects the angle of approach across the angle of impact. Trust me I did math for this.
    double initAngleOfDeparture = (2 * angleOfImpact) - angleOfApproach;
    
    //Applies this transformation to the ball
    double newXVel = -1 * Math.cos(initAngleOfDeparture) * magVelocity * COEF_OF_BOUNCE;
    double newYVel = -1 * Math.sin(initAngleOfDeparture) * magVelocity * COEF_OF_BOUNCE;
    
    //Takes into account the velocity of the yeeter
    newXVel += Math.sin(angleOfImpact) * (double)(xVel - yeeter.getxVel()) * COEF_OF_FRIC;
    newYVel += -1 * Math.abs(Math.cos(angleOfImpact) * (double)(yVel - yeeter.getyVel()) * COEF_OF_FRIC);
    
    xVel = newXVel;
    yVel = newYVel;
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
