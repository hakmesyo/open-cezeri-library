/*
 * The MIT License
 *
 * Copyright 2019 BAP1.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cezeri.factory;

import cezeri.gui.FrameScatterPlot;
import cezeri.matrix.CMatrix;
import cezeri.types.TDataVisualization;


/**
 *
 * @author BAP1
 */
public class FactoryScatterPlot extends TDataVisualization {    
    private static FactoryScatterPlot instance=null;
    
    private FactoryScatterPlot(CMatrix jm, String type) {
        this.jm = jm;
        this.type = type;
    }

    private static FactoryScatterPlot getNewScatterPlotInstance(CMatrix jm) {
        instance=new FactoryScatterPlot(jm, "scatter");
        return instance;
    }

    public static FactoryScatterPlot getScatterPlotInstance(CMatrix jm) {
        if (instance!=null) {
            return instance;
        }else{
            return getNewScatterPlotInstance(jm);
        }        
    }

    @Override
    public CMatrix buildAndShow() {
        FrameScatterPlot frm = new FrameScatterPlot(jm);
        frm.setVisible(true);
        return jm;
    }
    
    

}
