import java.util.ArrayList;
//Class to run the game
public class SpaceInvaders{
  //The distance from the side of the border where the aliens are created,
  //As well as the size of the hitbox for the aliens
  private static final int MARGIN = 20;
  //The speeds that the bullets, player and aliens move
  private static final int BULLET_SPEED = 2, PLAYER_SPEED = 5, ALIEN_SPEED = 1;
  //The space between each alien
  private static final int SPACES = 40;
  //The amount the aliens jump down each time
  private static final int ALIEN_JUMP = 10;
  //The distance aliens need to be from the bottom of the screen for the player to lose
  private static final int LOSE_DISTANCE = 70;
  //The frequency that aliens shoot at
  private static final double FREQUENCY = 0.98;
  //The height and width of the playing screen
  private static int height, width;
  //The current status of the game, whether it is won or lost
  private static int status;
  //Keeps track of the number of bullets and aliens
  private static int numBullets, numAliens;
  //The direction the aliens are moving
  private static int alienDirection;
  //Keeps track of all the objects to be drawn
  private static ArrayList<Sprite> items;
  
  //Constructor for the game
  public SpaceInvaders(int height, int width){
    //Sets height and width of the screen
    this.height = height;
    this.width = width;
    //Sets initial status to continue
    status = Display.CONTINUE;
    //Sets initial number of bullets to 0
    numBullets = 0;
    //Sets the aliens to move right initially
    alienDirection = Display.MOVE_RIGHT;
    //Initializes the array list
    items = new ArrayList<Sprite>();
    //Creates the player ship in the middle of the screen at the bottom
    Ship player = new Ship(width/2, (int)(height*0.9));
    items.add(player);
    //Creates the aliens and stores the number of them in numAliens
    numAliens = createAliens(4,4);
  }
  
  //Updates periodically
  public void update(){
    //Checks whether a bullet has gone offscreen
    checkBulletPosition();
    //Checks whether a bullet has hit an alien
    checkContact();
    //Checks if the aliens need to change direction
    checkAlienDirection();
    //Gets the aliens to shoot
    alienShoot();
    //Moves the bullets and the aliens
    moveBullets();
    moveAliens();
    
  }
  
  //Returns the array of items to be drawn
  public ArrayList<Sprite> getItems(){
    return items;
  }
  
  //Determines and returns the status of the game
  public int status(){
    //If there are no aliens left, the game is won
    if(numAliens == 0)
      status = Display.WIN;
    //Loops through all items
    for(int i = 0; i < items.size(); i++){
      //If the items is an alien, and if it has reached the bottom, the game is lost
      if((items.get(i) instanceof Alien) &&
         (items.get(i).getY() >= (height-LOSE_DISTANCE))){
        status = Display.LOSE;
      }
    }
    return status; 
  }
  
  //Moves the player ship
  public void move(int direction){
    //Move the ship left
    if(direction == Display.MOVE_LEFT && items.get(0).getX() > 0)
      items.get(0).setX(items.get(0).getX() - PLAYER_SPEED);
    //Move the ship right
    else if(direction == Display.MOVE_RIGHT && items.get(0).getX() < width)
      items.get(0).setX(items.get(0).getX() + PLAYER_SPEED);
  }
  
  //Shoots a bullet
  public void shoot(){
    //Restrict bullets on screen to two
    if(numBullets < 2){
      //Add the new bullet to the array at the player's position
      items.add(new Bullet(items.get(0).getX(), 
                           items.get(0).getY()));
      //Increment the number of bullets
      numBullets++;
    }
  }
  
  //Creates the alien grid of the given size
  private int createAliens(int rows, int columns){
    //Loops through rows and columns
    for(int row = 0; row < rows; row++){
      for(int col = 0; col < columns; col++){
        //Add a new alien to the array SPACES away from the last one
        items.add(new Alien((int)(col*SPACES + MARGIN), 
                            (int)(row*SPACES + MARGIN)));
      }
    }
    //Returns the number of aliens in the grid
    return rows*columns;
  }
  
  //Checks if a bullet has gone off screen
  private void checkBulletPosition(){
    //Loops through the array of items
    for(int i = 0; i < items.size(); i++){
      //If it is a bullet and has gone off the top of the screen,
      if(items.get(i) instanceof Bullet && items.get(i).getY() < 0){
        //Remove the bullet from the array, decrement numBullets
        items.remove(i);
        numBullets--;
        //Decrement the index to recheck that number as everything has shifted left after removal
        i--;
      }
      //If it is an alien bullet and has gone off the bottom of the screen
      else if(items.get(i) instanceof AlienBullet && items.get(i).getY() > height){
        //Remove the bullet from the array
        items.remove(i);
        i--;
      }
    }
  }
  
  //Moves the bullets on screen
  private void moveBullets(){
    //Loops through all items
    for(int i = 0; i < items.size(); i++){
      //If it is a bullet, move it up by BULLET_SPEED
      if(items.get(i) instanceof Bullet)
        items.get(i).setY(items.get(i).getY() - BULLET_SPEED);
      else if(items.get(i) instanceof AlienBullet)
        items.get(i).setY(items.get(i).getY() + BULLET_SPEED);
    }
  }
  
  //Checks if a bullet has made contact with an alien
  private void checkContact(){
    //Loops through all items to find a bullet
    for(int i = 0; i < items.size(); i++){
      if(items.get(i) instanceof Bullet){
        //Loops through all items again to find an alien
        for(int j = 0; j < items.size(); j++){
          if(items.get(j) instanceof Alien && collision(items.get(i), items.get(j))){
            //If the two items collide, kill the alien
            killAlien(i,j);
            //Decrement index in case the two bullets are next to each other in the array
            i--;
            //Immediately break the inner loop
            break;
          }
        }
      }
      //If an alien bullet makes contact with the player, set the status to lose
      else if(items.get(i) instanceof AlienBullet && collision(items.get(i), items.get(0))){
        status = Display.LOSE;
      }
    }
  }
  
  //Checks if two sprites collide
  private boolean collision(Sprite a, Sprite b){
     boolean collision = false;
     //If the two objects are within MARGIN distance from each other
     if(Math.sqrt((Math.pow((b.getX()-a.getX()),2) + 
                   Math.pow((b.getY()-a.getY()),2))) < MARGIN){
       //Collision is set to true
       collision = true;
     }
     return collision; 
  }
  
  //Kills the given alien with the given bullet
  private void killAlien(int bullet, int alien){
    //Removes the bullet and decrements numBullets
    items.remove(bullet);
    numBullets--;
    //Removes the alien and decrements numAliens
    items.remove(alien);
    numAliens--;
  }
  
  //Moves the aliens on the screen
  private void moveAliens(){
    //Loops through items to find an alien
    for(int i = 0; i < items.size(); i++){
      if(items.get(i) instanceof Alien){
        //Moves the aliens to the right by ALIEN_SPEED
        if(alienDirection == Display.MOVE_RIGHT)
          items.get(i).setX(items.get(i).getX() + ALIEN_SPEED);
        //Moves the aliens to the left by ALIEN_SPEED
        else if(alienDirection == Display.MOVE_LEFT)
          items.get(i).setX(items.get(i).getX() - ALIEN_SPEED);
      }
    }
  }
  
  //Checks if the aliens should change direction
  private void checkAlienDirection(){
    //Loops through items to find an alien
    for(int i = 0; i < items.size(); i++){
      if(items.get(i) instanceof Alien){
        //If one of the aliens hits the left border of the screen
        if(items.get(i).getX() <= 0){
          //Move the aliens right and jump them down
          alienDirection = Display.MOVE_RIGHT;
          shiftAliensDown();
        }
        //If one of the aliens hits the right border of the screen
        else if(items.get(i).getX() >= width){
          //Move the aliens left and jump them down
          alienDirection = Display.MOVE_LEFT;
          shiftAliensDown();
        }
      }
    }
  }
  
  //Moves the aliens down
  private void shiftAliensDown(){
    //Loops through items to find an alien
    for(int i = 0; i < items.size(); i++){
      if(items.get(i) instanceof Alien)
        //Move them down by ALIEN_JUMP
        items.get(i).setY(items.get(i).getY() + ALIEN_JUMP);
    }
  }
  
  //Makes the aliens shoot at the player
  private void alienShoot(){
    //Randomly shoot at the given frequency
    if(Math.random() > FREQUENCY){
      //An array that holds the indexes of all aliens in items
      int[] indices = alienIndices();
      //Grabs a random index from that array
      int index = indices[(int)(Math.random()*indices.length)];
      //Adds a new alien bullet at the given aliens position
      items.add(new AlienBullet(items.get(index).getX(), items.get(index).getY()));
    }
  }
  
  //Grabs the indexes of all the aliens in items
  private int[] alienIndices(){
    //The position in the indices array
    int position = 0;
    //Creates a new int array for the indices
    int[] indices = new int[numAliens];
    //Loops through all items in items
    for(int i = 0; i < items.size(); i++){
      if(items.get(i) instanceof Alien){
        //Adds the indices of each alien to the array
        indices[position] = i;
        position++;
      }
    }
    //Return that array
    return indices;
  }
}