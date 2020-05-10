package cezeri.search.shortest_path.jump_point_search;

import cezeri.matrix.CPoint;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        int xMax = 1000;  //size of grid x direction
        int yMax = 1000;  //size of the grid y direction
        int xIsland = 10; //islands along the x direction
        int yIsland = 10; //islands along the y direction
        boolean uniform = false; //true for uniform land generation, false for random land generation
        boolean draw = true;
        Node[][] grid = null;

        //original code
//		JPS jpsg = new JPS(xMax,yMax,xIsland,yIsland,uniform,draw,grid);
        //my addition
        int rMax = 30;
        int rMin = 2;
        int nIsland = 1000;
        List<CPoint> lst = generateRandomObstacleIsland(xMax, yMax, nIsland, rMin, rMax);
        JPS jpsg = new JPS(xMax, yMax, draw, lst);
    }

    private static List<CPoint> generateRandomObstacleIsland(int xMax, int yMax, int nIsland, int rMin, int rMax) {
        List<CPoint> ret = new ArrayList();
        for (int i = 0; i < nIsland; i++) {
            int r = (int) (rMin + Math.random() * (rMax - rMin));
            int px = (int) (Math.random() * xMax);
            int py = (int) (Math.random() * yMax);
            for (int j = 0; j < r; j++) {
                for (int k = 0; k < r; k++) {
                    if (py + k < yMax && px + j < xMax) {
                        CPoint cp = new CPoint(py + k, px + j);
                        ret.add(cp);
                    }
                }
            }
        }
        return ret;
    }
}
