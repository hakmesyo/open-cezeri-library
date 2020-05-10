package cezeri.search.shortest_path.jump_point_search;

import cezeri.matrix.CPoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Holds a Node[][] 2d array "grid" for path-finding tests, all drawing is done
 * through here.
 *
 * @author Clint Mullins
 *
 */
public class Grid {

    private Node[][] grid;
    private boolean uniform;
    private int xIsland, yIsland, xMin, yMin;
    public int xMax, yMax;
    private int[] trailCol = {255, 255, 0};
    private int[] expandedCol = {0, 0, 0};
    private int[] visitedCol = {255, 255, 255};
    private int[] landCol = {10, 100, 10};
    private int[] waterCol = {0, 0, 255};
    private int[] startCol = {0, 255, 0};
    private int[] endCol = {255, 0, 0};
    private PixelDraw map;
    private Heap heap;

    /**
     * Grid is created, Land is generated in either uniform or random fashion,
     * landscape 'Map' is created in printed.
     *
     *
     * @param xMax - (int) maximum x coordinate
     * @param yMax - (int) maximum y coordinate
     * @param xIsland (int) number of islands along x axis
     * @param yIsland (int) number of islands along y axis
     * @param uniform (boolean) if true then land is generated in a uniform
     * fashion, if false then land is randomly generated
     */
    public Grid(int xMax, int yMax, int xIsland, int yIsland, boolean uniform) {
        this.xMax = xMax;
        this.yMax = yMax;
        this.xIsland = xIsland;
        this.yIsland = yIsland;
        this.xMin = this.yMin = 0;
        this.uniform = uniform;
        map = new PixelDraw(this.xMax, this.yMax);
        grid = new Node[this.xMax][this.yMax];
        if (uniform) {
            uniformLandGenerator();
        } else {
            randomLandGenerator(xMax / 20, xMax / 6);
        }
        map.picPrint("1 - Map");
        heap = new Heap();
    }

    public Grid(int xMax, int yMax, List<CPoint> obsList) {
        this.xMax = xMax;
        this.yMax = yMax;
        this.xMin = this.yMin = 0;
        map = new PixelDraw(this.xMax, this.yMax);
        grid = new Node[this.xMax][this.yMax];
        landGenerator(obsList);
        map.picPrint("1 - Map");
        heap = new Heap();
    }

    /**
     * This is the constuctor used for comparison. It can be passed an entire
     * Node[][] grid.
     *
     *
     * @param xMax - (int) maximum x coordinate
     * @param yMax - (int) maximum y coordinate
     * @param xIsland (int) number of islands along x axis
     * @param yIsland (int) number of islands along y axis
     * @param grid (Node[][]) an entire grid is passed through for comparison
     */
    public Grid(int xMax, int yMax, int xIsland, int yIsland, Node[][] grid) {
        this.xMax = xMax;
        this.yMax = yMax;
        this.xIsland = xIsland;
        this.yIsland = yIsland;
        this.xMin = this.yMin = 0;
        map = new PixelDraw(this.xMax, this.yMax);
        this.grid = grid;
        map.picPrint("1 - Map");
        heap = new Heap();
    }

    /**
     * returns all adjacent nodes that can be traversed
     *
     * @param node (Node) finds the neighbors of this node
     * @return (int[][]) list of neighbors that can be traversed
     */
    public int[][] getNeighbors(Node node) {
        int[][] neighbors = new int[8][2];
        int x = node.x;
        int y = node.y;
        boolean d0 = false; //These booleans are for speeding up the adding of nodes.
        boolean d1 = false;
        boolean d2 = false;
        boolean d3 = false;

        if (walkable(x, y - 1)) {
            neighbors[0] = (tmpInt(x, y - 1));
            d0 = d1 = true;
        }
        if (walkable(x + 1, y)) {
            neighbors[1] = (tmpInt(x + 1, y));
            d1 = d2 = true;
        }
        if (walkable(x, y + 1)) {
            neighbors[2] = (tmpInt(x, y + 1));
            d2 = d3 = true;
        }
        if (walkable(x - 1, y)) {
            neighbors[3] = (tmpInt(x - 1, y));
            d3 = d0 = true;
        }
        if (d0 && walkable(x - 1, y - 1)) {
            neighbors[4] = (tmpInt(x - 1, y - 1));
        }
        if (d1 && walkable(x + 1, y - 1)) {
            neighbors[5] = (tmpInt(x + 1, y - 1));
        }
        if (d2 && walkable(x + 1, y + 1)) {
            neighbors[6] = (tmpInt(x + 1, y + 1));
        }
        if (d3 && walkable(x - 1, y + 1)) {
            neighbors[7] = (tmpInt(x - 1, y + 1));
        }
        return neighbors;
    }

//---------------------------Passability------------------------------//
    /**
     * Tests an x,y node's passability
     *
     * @param x (int) node's x coordinate
     * @param y (int) node's y coordinate
     * @return (boolean) true if the node is obstacle free and on the map, false
     * otherwise
     */
    public boolean walkable(int x, int y) {
        if (uniform) {
            if ((x < xMax && y < yMax) //smaller than max
                    && (x >= xMin && y >= yMin) //larger than min
                    && (Math.sin(Math.PI + xIsland * 2.0 * Math.PI * x / 1000.0) + Math.cos(Math.PI / 2.0 + yIsland * 2.0 * Math.PI * y / 1000.0) > -.1)) {   //walkable
                return true;
            }
            return false;
        } else {    //for randomized land generation, all nodes always contain correct "pass" boolean
            try {
                return getNode(x, y).pass;
            } catch (Exception e) {
                return false;
            }
        }
    }
//--------------------------------------------------------------------//

//---------------------------MAP DRAWING------------------------------//
    /**
     * Draws visited pixel to the map
     *
     * @param x (int) point to be drawn's x coordinate
     * @param y (int) point to be drawn's y coordinate
     */
    public void drawVisited(int x, int y) {
        map.drawPixel(x, y, visitedCol);
    }

    /**
     * Draws expanded pixel to the map
     *
     * @param x (int) point to be drawn's x coordinate
     * @param y (int) point to be drawn's y coordinate
     */
    public void drawExpanded(int x, int y) {
        map.drawPixel(x, y, expandedCol);
    }

    /**
     * Saves the picture to a png file in the folder of the program
     *
     * @param name (String) the file will be called 'name'
     */
    public void picPrint(String name) {
        map.picPrint(name);
    }

    /**
     * Draws a line from point (x,y) to point (px,py). The line is a nice mellow
     * yellow.
     *
     * @param x (int) start point's x coordinate
     * @param y (int) start point's y coordinate
     * @param px (int) end point's x coordinate
     * @param py (int) end point's y coordinate
     */
    public void drawLine(int x, int y, int px, int py) {
        map.drawLine(x, y, px, py, trailCol);
    }

    /**
     * Draws a start point at (x,y)
     *
     * @param x (int) start point's x coordinate
     * @param y (int) start point's y coordinate
     */
    public void drawStart(int x, int y) {
        map.drawPOI(x, y, startCol);
    }

    /**
     * Draws an end point at (x,y)
     *
     * @param x (int) end point's x coordinate
     * @param y (int) end point's y coordinate
     */
    public void drawEnd(int x, int y) {
        map.drawPOI(x, y, endCol);
    }

    public ArrayList<Node> pathCreate(Node node) {
        ArrayList<Node> trail = new ArrayList<Node>();
//        System.out.println("Tracing Back Path...");
        while (node.parent != null) {
            try {
                trail.add(0, node);
            } catch (Exception e) {
            }
            drawLine(node.parent.x, node.parent.y, node.x, node.y);
            node = node.parent;
        }
//        System.out.println("Path Trace Complete!");
        return trail;
    }
//-----------------------------------------------------------------//

//--------------------------HEAP-----------------------------------//	
    /**
     * Adds a node's (x,y,f) to the heap. The heap is sorted by 'f'.
     *
     * @param node (Node) node to be added to the heap
     */
    public void heapAdd(Node node) {
        float[] tmp = {node.x, node.y, node.f};
        heap.add(tmp);
    }

    /**
     * @return (int) size of the heap
     */
    public int heapSize() {
        return heap.getSize();
    }

    /**
     * @return (Node) takes data from popped float[] and returns the correct
     * node
     */
    public Node heapPopNode() {
        float[] tmp = heap.pop();
        return getNode((int) tmp[0], (int) tmp[1]);
    }
//-----------------------------------------------------------------//

//-----------------------LAND GENERATION---------------------------//
    /**
     * Generates land based on a formula. Land forms like a checkered pattern.
     */
    public void uniformLandGenerator() {
        for (int i = 0; i < this.xMax; i++) {
            for (int j = 0; j < this.yMax; j++) {
                grid[i][j] = new Node(i, j);
                if (grid[i][j].setPass(walkable(i, j))) {
                    map.drawPixel(i, j, waterCol);
                } else {
                    map.drawPixel(i, j, landCol);
                }
            }
        }
    }

    /**
     * Generates land based on random factors. Land forms like an ugly hanging
     * gardens.
     *
     * @param amount (int) number of islands to produce
     * @param size (int) general size of islands (random size is directly
     * correlated to this number).
     */
    public void randomLandGenerator(int amount, int size) {
        for (int i = 0; i < this.xMax; i++) {
            for (int j = 0; j < this.yMax; j++) {
                grid[i][j] = new Node(i, j);
                grid[i][j].f = -1;
                map.drawPixel(i, j, waterCol);
            }
        }
        Random rand = new Random();
        int centerX, centerY;
        int num1;
        int num2;
        for (int i = 0; i < amount; i++) {
            centerX = rand.nextInt(xMax);
            centerY = rand.nextInt(yMax);
            num1 = rand.nextInt(size);
            for (int j = 0; j < num1; j++) {
                num2 = rand.nextInt(size);
                for (int k = 0; k < num2; k++) {
                    try {
                        grid[centerX + j][centerY + k].pass = false;
                        map.drawPixel(centerX + j, centerY + k, landCol);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    public void landGenerator(List<CPoint> lst) {
        for (int i = 0; i < this.xMax; i++) {
            for (int j = 0; j < this.yMax; j++) {
                grid[i][j] = new Node(i, j);
                grid[i][j].f = -1;
                map.drawPixel(i, j, waterCol);
            }
        }
        if (lst != null) {
            for (CPoint cp : lst) {
                grid[cp.column][cp.row].pass = false;
                map.drawPixel(cp.column, cp.row, landCol);
            }

        }
    }

    /**
     * Finds an open spot. Used for finding random start/end points.
     *
     * @return int[] open spot
     */
    public int[] getOpenPos() {
        Random rand = new Random();
        while (true) {
            int tA = rand.nextInt(xMax);   //gets random x
            int tB = rand.nextInt(yMax);   //gets random y
            if (walkable(tA, tB)) { 	   //if this (x,y) pair is walkable (not an obstacle and on the map)
                int[] tmpInt = {tA, tB}; //combine the approved x and y
                return tmpInt;	   //return this pair!
            }
        }
    }
//---------------------------------------------------------//

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
     * getFunc - Node at given x, y
     *
     * @param x (int) desired node x coordinate
     * @param y (int) desired node y coordinate
     * @return (Node) desired node
     */
    public Node getNode(int x, int y) {
        try {
            return grid[x][y];
        } catch (Exception e) {
            return null;
        }
    }

    public float toPointApprox(float x, float y, int tx, int ty) {
        return (float) Math.sqrt(Math.pow(Math.abs(x - tx), 2) + Math.pow(Math.abs(y - ty), 2));
    }
}
