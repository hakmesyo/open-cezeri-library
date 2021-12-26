/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.h2d_mlp;

import cezeri.matrix.CMatrix;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author cezerilab
 */
public class MlpLayer {

    static int id = 0;
    String layerName = "";
    List<Channel> channels;
    int nchannels;
    int nrows;
    int ncols;
    int size;
    LayerType layerType;
    MlpLayer prevLayer;
    MlpLayer nextLayer;
    MlpModel model;
    ActivationType activationType;
    Random rnd;

    //for input and hidden layers
    public MlpLayer(MlpModel model, LayerType layerType, ActivationType act) {
        this.model = model;
        this.nchannels = model.nchannels;
        this.rnd = model.rnd;
        this.layerType = layerType;
        if (layerType == layerType.input) {
            this.prevLayer = null;
            this.layerName = "Input Layer " + id++;
            this.nrows = model.nrows;
            this.ncols = model.ncols;
        } else if (layerType == layerType.hidden) {
            this.nrows = (int)(Math.ceil(model.nrows/(Math.pow(2, id))));
            this.ncols = (int)(Math.ceil(model.ncols/(Math.pow(2, id))));
            this.prevLayer = model.getLastLayer();
            this.layerName = "Hidden Layer " + id++;
        }
        this.size = nrows * ncols;
        this.activationType = act;
        this.channels = new ArrayList();
        for (int i = 0; i < this.nchannels; i++) {
            this.channels.add(new Channel(this, nrows, ncols, i));
        }
    }

    //for output layer
    public MlpLayer(MlpModel model, LayerType type, int output, ActivationType act) {
        this.model = model;
        this.rnd = model.rnd;
        this.nchannels = model.nchannels;
        this.prevLayer = model.getLastLayer();
        this.layerName = "Output Layer " + id++;
        this.nrows = output;
        this.ncols = 1;
        this.size = nrows * ncols;
        this.layerType = type;
        this.activationType = act;
        this.channels = new ArrayList();
        for (int i = 0; i < this.nchannels; i++) {
            this.channels.add(new Channel(this, nrows, ncols, i));
        }
    }

}
