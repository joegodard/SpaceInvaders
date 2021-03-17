import java.awt.Color;
//Object for bullets
public class Bullet extends Sprite{
  //The Sprite for a player bullet
  public static final Color [][] BULLET_SHAPE = 
  {
   {Display.BLACK},
   {Display.BLACK},
   {Display.BLACK},
   {Display.BLACK},
   {Display.BLACK}
  };
  
  //Creates bullet with specified position
  public Bullet(int x, int y){
    super(x, y, BULLET_SHAPE); 
  }
}