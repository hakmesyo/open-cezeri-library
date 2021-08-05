/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.types;

import ai.djl.nn.Block;
import java.util.Arrays;

/**
 *
 * @author cezerilab
 */
public class TDJLModel {
    private int NUM_CHANNEL=3;
    private int IMAGE_WIDTH=224;
    private int IMAGE_HEIGHT=224;
    private int NUM_OUTPUT=10;
    private int BLOCK_TYPE=TBlockType.MLP;
    private Block BLOCK;
    private int[] MLP_HIDDEN={128,64};
    private int RESNET_LAYER=50;
    
    public TDJLModel(int NUM_CHANNEL,int IMAGE_WIDTH,int IMAGE_HEIGHT,int NUM_OUTPUT,int BLOCK_TYPE){
        this.NUM_CHANNEL=NUM_CHANNEL;
        this.IMAGE_WIDTH=IMAGE_WIDTH;
        this.IMAGE_HEIGHT=IMAGE_HEIGHT;
        this.NUM_OUTPUT=NUM_OUTPUT;
        this.BLOCK_TYPE=BLOCK_TYPE;
        if (BLOCK_TYPE==TBlockType.MLP) {
            MLP_HIDDEN=new int[]{128,64};
        }
        if (BLOCK_TYPE==TBlockType.ResNetV50) {
            RESNET_LAYER=50;
        }
        this.BLOCK=null;
    }
    
    public TDJLModel(int NUM_CHANNEL,int IMAGE_WIDTH,int IMAGE_HEIGHT,int NUM_OUTPUT,int BLOCK_TYPE, Block block){
        this.NUM_CHANNEL=NUM_CHANNEL;
        this.IMAGE_WIDTH=IMAGE_WIDTH;
        this.IMAGE_HEIGHT=IMAGE_HEIGHT;
        this.NUM_OUTPUT=NUM_OUTPUT;
        this.BLOCK_TYPE=BLOCK_TYPE;
        this.BLOCK=block;
    }

    @Override
    public String toString() {
        return "NUM_CHANNEL=" + NUM_CHANNEL + "\nIMAGE_WIDTH=" + IMAGE_WIDTH + "\nIMAGE_HEIGHT=" + IMAGE_HEIGHT + "\nNUM_OUTPUT=" + NUM_OUTPUT + "\nBLOCK_TYPE=" + BLOCK_TYPE + "\nMLP_HIDDEN=" + Arrays.toString(MLP_HIDDEN) + "\nRESNET_LAYER=" + RESNET_LAYER;
    }
    
    
}
