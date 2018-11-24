/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.cam2D;

import cezeri.cad2D.CadArc;
import cezeri.cad2D.CadCircle;
import cezeri.cad2D.CadLine;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.kabeja.dxf.Bounds;
import org.kabeja.dxf.DXFArc;
import org.kabeja.dxf.DXFCircle;
import org.kabeja.dxf.DXFConstants;
import org.kabeja.dxf.DXFDocument;
import org.kabeja.dxf.DXFLine;
import org.kabeja.parser.ParseException;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;

public class ReadFileDXF {

    public static ArrayList<CadLine> getLinesFromAutocadFile(String filePath) throws ParseException {
        ArrayList<CadLine> lines = new ArrayList<>();
        Parser parser = ParserBuilder.createDefaultParser();
        parser.parse(filePath);
        DXFDocument doc = parser.getDocument();
        List<DXFLine> lst_line = doc.getDXFLayer("layername ... whatever").getDXFEntities(DXFConstants.ENTITY_TYPE_LINE);
        if (lst_line==null) {
            lines=null;
            return lines;
        }
        for (int index = 0; index < lst_line.size(); index++) {
            Bounds bounds = lst_line.get(index).getBounds();
            CadLine line = new CadLine(null,
                    bounds.getMinimumX(),
                    bounds.getMinimumY(),
                    bounds.getMaximumX(),
                    bounds.getMaximumY(),
                    Color.WHITE);
            lines.add(line);
        }
        return lines;
    }
    
    public static ArrayList<CadCircle> getCirclesFromAutocadFile(String filePath) throws ParseException {
        ArrayList<CadCircle> circles = new ArrayList<>();
        Parser parser = ParserBuilder.createDefaultParser();
        parser.parse(filePath);
        DXFDocument doc = parser.getDocument();
        List<DXFCircle> lst_circle = doc.getDXFLayer("layername ... whatever").getDXFEntities(DXFConstants.ENTITY_TYPE_CIRCLE);
        if (lst_circle==null) {
            circles=null;
            return circles;
        }
        for (int index = 0; index < lst_circle.size(); index++) {
            CadCircle circle = new CadCircle(null,
                    lst_circle.get(index).getCenterPoint().getX(),
                    lst_circle.get(index).getCenterPoint().getY(),
                    lst_circle.get(index).getRadius(),Color.WHITE);
            circles.add(circle);
        }
        return circles;
    }
    
    public static ArrayList<CadArc> getArcsFromAutocadFile(String filePath) throws ParseException {
        ArrayList<CadArc> arcs = new ArrayList<>();
        Parser parser = ParserBuilder.createDefaultParser();
        parser.parse(filePath);
        DXFDocument doc = parser.getDocument();
        List<DXFArc> lst_arc = doc.getDXFLayer("layername ... whatever").getDXFEntities(DXFConstants.ENTITY_TYPE_ARC);
        if (lst_arc==null) {
            arcs=null;
            return arcs;
        }
        for (int index = 0; index < lst_arc.size(); index++) {
            CadArc arc = new CadArc(null,
                    lst_arc.get(index).getCenterPoint().getX(),
                    lst_arc.get(index).getCenterPoint().getY(),
                    lst_arc.get(index).getRadius(),
                    lst_arc.get(index).getStartAngle(),
                    lst_arc.get(index).getEndAngle(),
                    lst_arc.get(index).isCounterClockwise(),
                    Color.WHITE
                    );
            arcs.add(arc);
        }
        return arcs;
    }
}
