/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.cam2D;

import cezeri.cad2D.CadArc;
import cezeri.cad2D.CadCircle;
import cezeri.cad2D.CadLine;
import java.io.File;
import java.util.ArrayList;

public class TestFileDXFReader {

    public static void main(String[] args) {
        try {
            File file = new File("feeder.dxf");
            ArrayList<CadLine> lines = ReadFileDXF.getLinesFromAutocadFile(file.getAbsolutePath());
            if (lines != null) {
                for (int index = 0; index < lines.size(); index++) {
                    System.out.println("Line " + (index + 1));
                    double x1=((CadLine)lines.get(index)).p1x;
                    double y1=((CadLine)lines.get(index)).p1y;
                    double x2=((CadLine)lines.get(index)).p2x;
                    double y2=((CadLine)lines.get(index)).p2y;
                    System.out.print("(" + x1 + "," + y1 + ")");
                    System.out.print(" to (" + x2 + "," + y2 + ")\n");
                }
            }
            ArrayList<CadCircle> circles = ReadFileDXF.getCirclesFromAutocadFile(file.getAbsolutePath());
            if (circles != null) {
                for (int index = 0; index < circles.size(); index++) {
                    System.out.println("Circle " + (index + 1));
                    double cx=((CadCircle)circles.get(index)).cx;
                    double cy=((CadCircle)circles.get(index)).cy;
                    double r=((CadCircle)circles.get(index)).radious;
                    System.out.print("(Center pos:" + cx + "," + cy + ")");
                    System.out.print(" radious: (" + r + ")\n");
                }
            }
//            ArrayList<CadArc> arcs = ReadFileDXF.getArcsFromAutocadFile(file.getAbsolutePath());
//            if (arcs != null) {
//                for (int index = 0; index < arcs.size(); index++) {
//                    System.out.println("Arc " + (index + 1));
//                    System.out.print("(Center pos:" + arcs.get(index).getCenter().x + "," + arcs.get(index).getCenter().y + ")");
//                    System.out.print(" radious: (" + arcs.get(index).getRadious() + ")\n");
//                    System.out.print(" start angle: (" + arcs.get(index).getStartAngle() + ")\n");
//                    System.out.print(" end angle: (" + arcs.get(index).getEndAngle() + ")\n");
//                    System.out.print(" is counter clock wise: (" + arcs.get(index).isCounterClockWise() + ")\n");
//                }
//            }
        } catch (Exception e) {

        }
    }
}
