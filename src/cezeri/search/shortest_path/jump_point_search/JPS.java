package cezeri.search.shortest_path.jump_point_search;

import cezeri.matrix.CPoint;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Clint Mullins
 * @referenced Javascript version of JPS by aniero / https://github.com/aniero
 */
public class JPS {

    Grid grid;
    int xMax, yMax, xIsland, yIsland, startX, startY, endX, endY;  //variables for reference grid
    int dxMax, dyMax, dstartX, dstartY, dendX, dendY;       //variables for Large Nod
    int[] tmpXY;
    int[][] neighbors;
    float ng;
    boolean draw;
    Node tmpNode, cur;
    Node[] successors, possibleSuccess;
    public ArrayList<Node> trail;

    /**
     * Initializer; sets up variables, creates reference grid and actual grid,
     * gets start and end points, initiates search
     *
     * @param xMax (int) maximum x value on map + 1 (if xMax==100, actual x
     * maximum is 99)
     * @param yMax (int) maximum y value on map + 1 (if yMax==100, actual y
     * maximum is 99)
     * @param xIsland (int) when using uniform map generation, how many islands
     * on the x axis
     * @param yIsland (int) when using uniform map generation, how many islands
     * on the y axis
     * @param uniform (boolean) if true then land created is a uniform checkered
     * pattern (depending on xIsland, yIsland), if false then random land
     * generated
     */
    public JPS(int xMax, int yMax, int xIsland, int yIsland, boolean uniform, boolean draw, Node[][] preMadeGrid) {
        this.xMax = xMax; //maximum x value on map + 1 (if xMax==100, actual x maximum is 99)
        this.yMax = yMax; //maximum y value on map + 1 (if yMax==100, actual y maximum is 99)
        this.xIsland = xIsland; //when using uniform map generation, how many islands on the x axis
        this.yIsland = yIsland; //when using uniform map generation, how many islands on the y axis
        this.draw = draw;
        if (preMadeGrid == null) {
            grid = new Grid(xMax, yMax, xIsland, yIsland, uniform);  //grid is created
        } else {
            grid = new Grid(xMax, yMax, xIsland, yIsland, preMadeGrid);  //preMadeGrid is passed in because there CAN BE ONLY ONE GRID
        }
        int[] startPos = grid.getOpenPos(); //startPos returns random {x,y} that does not lie on an obstacle 
        this.startX = startPos[0];   //the start point x value
        this.startY = startPos[1];	  //the start point y value
        int[] endPos = grid.getOpenPos(); //startPos returns random {x,y} that does not lie on an obstacle 
        this.endX = endPos[0];	  //the end point x value
        this.endY = endPos[1];	  //the end point y value
        long timeStart = System.currentTimeMillis();
        search();
        long timeEnd = System.currentTimeMillis();
        System.out.println("Time: " + (timeEnd - timeStart) + " ms");
    }

    public JPS(int xMax, int yMax, boolean draw, List<CPoint> lst) {
        this.xMax = xMax; //maximum x value on map + 1 (if xMax==100, actual x maximum is 99)
        this.yMax = yMax; //maximum y value on map + 1 (if yMax==100, actual y maximum is 99)
        this.draw = draw;
        grid = new Grid(xMax, yMax, lst);  //grid is created
        int[] startPos = grid.getOpenPos(); //startPos returns random {x,y} that does not lie on an obstacle 
        this.startX = startPos[0];   //the start point x value
        this.startY = startPos[1];	  //the start point y value
        int[] endPos = grid.getOpenPos(); //startPos returns random {x,y} that does not lie on an obstacle 
        this.endX = endPos[0];	  //the end point x value
        this.endY = endPos[1];	  //the end point y value
        long timeStart = System.currentTimeMillis();
        search();
        long timeEnd = System.currentTimeMillis();
        System.out.println("Time: " + (timeEnd - timeStart) + " ms");
    }

    public JPS(Grid grid,CPoint start, CPoint end, boolean draw) {
        this.xMax = grid.xMax; //maximum x value on map + 1 (if xMax==100, actual x maximum is 99)
        this.yMax = grid.yMax; //maximum y value on map + 1 (if yMax==100, actual y maximum is 99)
        this.draw = draw;
        this.grid=grid;
        int[] startPos = {start.column,start.row}; //startPos returns random {x,y} that does not lie on an obstacle 
        this.startX = startPos[0];   //the start point x value
        this.startY = startPos[1];	  //the start point y value
        int[] endPos = {end.column,end.row}; //startPos returns random {x,y} that does not lie on an obstacle 
        this.endX = endPos[0];	  //the end point x value
        this.endY = endPos[1];	  //the end point y value
        long timeStart = System.currentTimeMillis();
        search();
        long timeEnd = System.currentTimeMillis();
//        System.out.println("Time: " + (timeEnd - timeStart) + " ms");
    }

    /**
     * Orchestrates the Jump Point Search; it is explained further in comments
     * below.
     */
    public void search() {
//        System.out.println("Jump Point Search\n----------------");
//        System.out.println("Start X: " + startX + " Y: " + startY);  //Start and End points are printed for reference
//        System.out.println("End   X: " + endX + " Y: " + endY);
        grid.getNode(startX, startY).updateGHFP(0, 0, null);
        grid.heapAdd(grid.getNode(startX, startY));  //Start node is added to the heap
        while (true) {
            cur = grid.heapPopNode();              //the current node is removed from the heap. 
            if (draw) {
                grid.drawVisited(cur.x, cur.y);
            }  //draw current point
            if (cur.x == endX && cur.y == endY) {		//if the end node is found
//                System.out.println("Path Found!");  //print "Path Found!"
                if (draw) {
                    grid.drawStart(startX, startY);
                    grid.drawEnd(endX, endY);
                    grid.picPrint("2 - JumpPoints");
                } //draw start, end, and print the picture sans path
                trail = grid.pathCreate(cur);    //the path is then created
                if (draw) {
                    grid.picPrint("3 - PathAndPoints");
                }   //printed the picture with path
                break;				//loop is done
            }
            possibleSuccess = identifySuccessors(cur);  //get all possible successors of the current node
            for (int i = 0; i < possibleSuccess.length; i++) {     //for each one of them
                if (possibleSuccess[i] != null) {				//if it is not null
                    grid.heapAdd(possibleSuccess[i]);		//add it to the heap for later use (a possible future cur)
                }
            }
            if (grid.heapSize() == 0) {						//if the grid size is 0, and we have not found our end, the end is unreachable
                System.out.println("No Path....");			//print "No Path...." to (lolSpark) notify user
                if (draw) {
                    grid.picPrint("3 - No Path");
                } 		//print picture without path
                break;										//loop is done
            }
        }
    }

    /**
     * returns all nodes jumped from given node
     *
     * @param node
     * @return all nodes jumped from given node
     */
    public Node[] identifySuccessors(Node node) {
        successors = new Node[8];				//empty successors list to be returned
        neighbors = getNeighborsPrune(node);    //all neighbors after pruned
        for (int i = 0; i < neighbors.length; i++) { //for each of these neighbors
            tmpXY = jump(neighbors[i][0], neighbors[i][1], node.x, node.y); //get next jump point
            if (tmpXY[0] != -1) {								//if that point is not null( {-1,-1} )
                int x = tmpXY[0];
                int y = tmpXY[1];
                ng = (grid.toPointApprox(x, y, node.x, node.y) + node.g);   //get the distance from start
                if (grid.getNode(x, y).f <= 0 || grid.getNode(x, y).g > ng) {  //if this node is not already found, or we have a shorter distance from the current node
                    grid.getNode(x, y).updateGHFP(grid.toPointApprox(x, y, node.x, node.y) + node.g, grid.toPointApprox(x, y, endX, endY), node); //then update the rest of it
                    successors[i] = grid.getNode(x, y);  //add this node to the successors list to be returned
                }
            }
        }
        return successors;  //finally, successors is returned
    }

    /**
     * jump method recursively searches in the direction of parent (px,py) to
     * child, the current node (x,y). It will stop and return its current
     * position in three situations:
     *
     * 1) The current node is the end node. (endX, endY) 2) The current node is
     * a forced neighbor. 3) The current node is an intermediate step to a node
     * that satisfies either 1) or 2)
     *
     * @param x (int) current node's x
     * @param y (int) current node's y
     * @param px (int) current.parent's x
     * @param py (int) current.parent's y
     * @return (int[]={x,y}) node which satisfies one of the conditions above,
     * or null if no such node is found.
     */
    public int[] jump(int x, int y, int px, int py) {
        int[] jx = {-1, -1}; //used to later check if full or null
        int[] jy = {-1, -1}; //used to later check if full or null
        int dx = (x - px) / Math.max(Math.abs(x - px), 1); //because parents aren't always adjacent, this is used to find parent -> child direction (for x)
        int dy = (y - py) / Math.max(Math.abs(y - py), 1); //because parents aren't always adjacent, this is used to find parent -> child direction (for y)

        if (!grid.walkable(x, y)) { //if this space is not grid.walkable, return a null. 
            return tmpInt(-1, -1); //in this system, returning a {-1,-1} equates to a null and is ignored. 
        }
        if (x == this.endX && y == this.endY) {   //if end point, return that point. The search is over! Have a beer.			
            return tmpInt(x, y);
        }
        if (dx != 0 && dy != 0) {  //if x and y both changed, we are on a diagonally adjacent square: here we check for forced neighbors on diagonals
            if ((grid.walkable(x - dx, y + dy) && !grid.walkable(x - dx, y))
                    || //we are moving diagonally, we don't check the parent, or our next diagonal step, but the other diagonals
                    (grid.walkable(x + dx, y - dy) && !grid.walkable(x, y - dy))) {  //if we find a forced neighbor here, we are on a jump point, and we return the current position
                return tmpInt(x, y);
            }
        } else { //check for horizontal/vertical
            if (dx != 0) { //moving along x
                if ((grid.walkable(x + dx, y + 1) && !grid.walkable(x, y + 1))
                        || //we are moving along the x axis
                        (grid.walkable(x + dx, y - 1) && !grid.walkable(x, y - 1))) {  //we check our side nodes to see if they are forced neighbors  
                    return tmpInt(x, y);
                }
            } else {
                if ((grid.walkable(x + 1, y + dy) && !grid.walkable(x + 1, y))
                        || //we are moving along the y axis
                        (grid.walkable(x - 1, y + dy) && !grid.walkable(x - 1, y))) {	 //we check our side nodes to see if they are forced neighbors 
                    return tmpInt(x, y);
                }
            }
        }

        if (dx != 0 && dy != 0) { //when moving diagonally, must check for vertical/horizontal jump points
            jx = jump(x + dx, y, x, y);
            jy = jump(x, y + dy, x, y);
            if (jx[0] != -1 || jy[0] != -1) {
                return tmpInt(x, y);
            }
        }
        if (grid.walkable(x + dx, y) || grid.walkable(x, y + dy)) { //moving diagonally, must make sure one of the vertical/horizontal neighbors is open to allow the path
            return jump(x + dx, y + dy, x, y);
        } else { //if we are trying to move diagonally but we are blocked by two touching corners of adjacent nodes, we return a null
            return tmpInt(-1, -1);
        }
    }

    /**
     * Encapsulates x,y in an int[] for returning. A helper method for the jump
     * method
     *
     * @param x (int) point's x coordinate
     * @param y (int) point's y coordinate
     * @return ([]int) bundled x,y
     */
    public int[] tmpInt(int x, int y) {
        int[] tmpIntsTmpInt = {x, y};  //create the tmpInt's tmpInt[]
        return tmpIntsTmpInt;         //return it
    }

    /**
     * Returns nodes that should be jumped based on the parent location in
     * relation to the given node.
     *
     * @param node (Node) node which has a parent (not the start node)
     * @return (ArrayList<Node>) list of nodes that will be jumped
     */
    public int[][] getNeighborsPrune(Node node) {
        Node parent = node.parent;    //the parent node is retrieved for x,y values
        int x = node.x;
        int y = node.y;
        int px, py, dx, dy;
        int[][] neighbors = new int[5][2];
        //directed pruning: can ignore most neighbors, unless forced
        if (parent != null) {
            px = parent.x;
            py = parent.y;
            //get the normalized direction of travel
            dx = (x - px) / Math.max(Math.abs(x - px), 1);
            dy = (y - py) / Math.max(Math.abs(y - py), 1);
            //search diagonally
            if (dx != 0 && dy != 0) {
                if (grid.walkable(x, y + dy)) {
                    neighbors[0] = (tmpInt(x, y + dy));
                }
                if (grid.walkable(x + dx, y)) {
                    neighbors[1] = (tmpInt(x + dx, y));
                }
                if (grid.walkable(x, y + dy) || grid.walkable(x + dx, y)) {
                    neighbors[2] = (tmpInt(x + dx, y + dy));
                }
                if (!grid.walkable(x - dx, y) && grid.walkable(x, y + dy)) {
                    neighbors[3] = (tmpInt(x - dx, y + dy));
                }
                if (!grid.walkable(x, y - dy) && grid.walkable(x + dx, y)) {
                    neighbors[4] = (tmpInt(x + dx, y - dy));
                }
            } else {
                if (dx == 0) {
                    if (grid.walkable(x, y + dy)) {
                        if (grid.walkable(x, y + dy)) {
                            neighbors[0] = (tmpInt(x, y + dy));
                        }
                        if (!grid.walkable(x + 1, y)) {
                            neighbors[1] = (tmpInt(x + 1, y + dy));
                        }
                        if (!grid.walkable(x - 1, y)) {
                            neighbors[2] = (tmpInt(x - 1, y + dy));
                        }
                    }
                } else {
                    if (grid.walkable(x + dx, y)) {
                        if (grid.walkable(x + dx, y)) {
                            neighbors[0] = (tmpInt(x + dx, y));
                        }
                        if (!grid.walkable(x, y + 1)) {
                            neighbors[1] = (tmpInt(x + dx, y + 1));
                        }
                        if (!grid.walkable(x, y - 1)) {
                            neighbors[2] = (tmpInt(x + dx, y - 1));
                        }
                    }
                }
            }
        } else {//return all neighbors
            return grid.getNeighbors(node); //adds initial nodes to be jumped from!
        }

        return neighbors; //this returns the neighbors, you know
    }
}
