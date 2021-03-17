import java.awt.Color;
//Object for Alien Bullets
public class AlienBullet extends Sprite{
  //Sprite for an alien bullet
  public static final Color [][] ALIEN_BULLET = 
  {
   {Display.BLACK, null},
   {null, Display.BLACK},
   {Display.BLACK, null},
   {null, Display.BLACK},
   {Display.BLACK, null}
  };
  
  //Creates an alien bullet at the specified position
  public AlienBullet(int x, int y){
    super(x, y, ALIEN_BULLET); 
  }
}