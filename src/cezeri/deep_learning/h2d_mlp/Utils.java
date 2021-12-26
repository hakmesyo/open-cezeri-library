/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.h2d_mlp;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author cezerilab
 */
public class Utils {
    
    public static double getRandomWeight(Random rnd) {
        return -1.0 + (rnd.nextDouble() * 2);
    }

    public static void dump(MlpLayer layer) {
        System.out.println("");
        for (int i = 0; i < layer.nchannels; i++) {
            Channel ch=layer.channels.get(i);
            for (int j = 0; j < ch.nodes.length; j++) {
                for (int k = 0; k < ch.nodes[0].length; k++) {
                    Node node=ch.nodes[j][k];
                    System.out.println("node:"+node.toString()+" weights:"+Arrays.toString(node.weightsToOutputLayer));
                }
            }
        }
    }
    
}
