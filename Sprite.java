import java.awt.Color;
//Object for any sprite
public abstract class Sprite{
  //Variables for the x and y position of the sprite
  protected int x;
  protected int y;
  //The color grid representing the image
  protected Color[][] colorGrid;
  
  //Creates the new sprite with the given properties
  public Sprite(int x, int y, Color[][] colorGrid){
    this.x = x;
    this.y = y;
    this.colorGrid = colorGrid;
  }
  
  //Gets the value for the x position
  public int getX(){
    return x; 
  }
  
  //Gets the value for the y position
  public int getY(){
    return y; 
  }
  
  //Gets the color grid
  public Color[][] getColorGrid(){
    return colorGrid; 
  }
  
  //Changes the x position
  public void setX(int x){
    this.x = x;
  }
  
  //Changes the y position
  public void setY(int y){
    this.y = y;
  }
}