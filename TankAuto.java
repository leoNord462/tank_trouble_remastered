import java.util.ArrayList;

// This class is a basic AI
// The goal was to implements a Djikstra or an AI algorythym to rush the ennemy
// Sadly, it won't come before the next update, stay tuned !

// for the moment, the tank just orient toward the ennemy and shoots when it is close enough
public class TankAuto extends Tank {
  public ArrayList<PathFindingNode> map = new ArrayList<PathFindingNode>(); // list of nodes forming the map

  public Vector cellAimed = new Vector(); // position of the cell we should go
  public ArrayList<Vector> path = new ArrayList<Vector>();; // path to follow to reach player

  public TankAuto(int number, int x, int y, GamePanel gp) {
    super(number, x, y, gp.im.autoTank, gp.im.autoExplosion, gp);
    modelMap(); // model the current map to be used by BFS
  }

  public void modelMap() {

    // define number of rows and collums
    int R = gp.currentMap.tiles.length;
    int C = gp.currentMap.tiles[0].length;

    // loop though every cell of the map
    for(int r=0; r<R; r++) {
      for(int c=0; c<C; c++) {
        Tile currentTile = gp.currentMap.tiles[r][c];

        // don't take walls as usable cells
        if(currentTile.collision) continue;

        // create a new node with an unique id
        PathFindingNode node = new PathFindingNode(c + r * C);  // again the magic formula to idenify a cell by a number

        // define possible dirrections
        int[] dr = new int[] {1, -1, 0, 0}; // row direction
        int[] dc = new int[] {0, 0, 1, -1}; // collum direction

        // check every direction and add neighbours to the node
        for(int i = 0; i < 4; i++) {
          // position of the neighboor
          int rr = r + dr[i];
          int cc = c + dc[i];

          // verify bounds
          if(rr < 0 || cc < 0) continue;
          if(rr >= R || cc >= C) continue;

          // don't add wall as neighbours
          if(gp.currentMap.tiles[rr][cc].collision) continue;

          // add the neighboor to the list of reachable nodes by the current node
          node.neighbours.add(cc + rr * C); // id of the neighboor
        }

        // finnally add the node and its neighbours
        map.add(node);
      }
    }
  }

  @Override
  public void collision() {
    super.collision();
    if(collision) dash(-10); // bounce walls
  }

  @Override
  public void keyPressed() {
    upPressed = false;
    downPressed = false;
    leftPressed = false;
    rightPressed = false;
    shotPressed = false;

    // focused the cell where the player is
    for(Tank t: gp.players) {
      if(t != this) {
        cellAimed.x = t.getX();
        cellAimed.y = t.getY();
      }
    }

    int angleDiff = 0;
    double distance = 0;
    int deltaX = cellAimed.x - this.getX(); // difference of position in the x-direction between this tank and targeted player
    int deltaY = cellAimed.y - this.getY(); // difference of position in the y-direction

    // CALCULATE DISTANCE TO THE TARGET
    distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    // shoot
    if(distance < 500) shotPressed = true; // cirtually pressed the shot button

    // COMPUTE ANGLE TO ROTATE
    if(deltaY == 0) deltaY = 1; // do not divide by zero trick
    double angleGoal = Math.toDegrees(Math.atan(deltaX / deltaY)); // orient the tanking
    if(Math.abs(angleGoal) < 90 && deltaY < 0) angleGoal += 180;
    angleDiff = (int)(this.angle - angleGoal);
    if(Math.abs(angleDiff) > 3) {
      if(Math.abs(angleDiff) > 360 - 12) {
        if(angleDiff < 0)
          leftPressed = true;
        else
          rightPressed = true;
      } else {
        if(angleDiff > 0)
          leftPressed = true;
        else
          rightPressed = true;
      }
      this.angle %= 360;
    }
    if(Math.abs(angleDiff) <=  60 || Math.abs(angleDiff) >= 300) upPressed = true; // always move forward
  }


}
