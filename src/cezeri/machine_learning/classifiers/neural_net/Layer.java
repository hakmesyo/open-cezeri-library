package cezeri.machine_learning.classifiers.neural_net;
import java.io.FileWriter;

public class Layer {
    double[][] z;
    int nodes;
    String activation_type;

    public Layer(int no_nodes){
        this.nodes = no_nodes;
        z = new double[1][no_nodes];
        activation_type = "sigmoid";
    }

    public Layer(int no_nodes, String activation_type){
        this.activation_type = activation_type;
    }
    
    public void applyActivation(){
        if(activation_type.equals("sigmoid")){
            sigmoid();
        }
        else if(activation_type.equals("relu")){
            relu();
        }
    }


    public void applyActivation(String softmax) {
        if(softmax.equals("softmax")){
            double[][] exp_z = new double[z.length][z[0].length];
            double exp_z_sum = 0.0;
            for(int i=0; i<z.length; i++){
                for(int j=0; j<z[0].length; j++){
                    exp_z[i][j] = Math.pow(Math.E, z[i][j]);
                    exp_z_sum += exp_z[i][j];
                }
            }
            for(int i=0; i<z.length; i++){
                for(int j=0; j<z[0].length; j++){
                    z[i][j] = exp_z[i][j] / exp_z_sum;
                }
            }
        }
    }

    public double[][] sigmoidDerivative(){
        double[][] temp_z = this.z;
        double[][] delta_z_right = new double[temp_z.length][temp_z[0].length];
        for(int i=0; i<temp_z.length; i++){
            for(int j=0; j<temp_z[0].length; j++){

                delta_z_right[i][j] = temp_z[i][j] * (1 - temp_z[i][j]);

            }
        }
        return delta_z_right;
    }

    private void sigmoid(){
        for(int i=0; i<z.length; i++){
            for(int j=0; j<z[0].length; j++){
                z[i][j] = (1/(1+Math.pow(Math.E, -1 * z[i][j])));
            }
        }
    }

    private void relu(){
        for(int i=0; i<z.length; i++){
            for(int j=0; j<z[0].length; j++){
                z[i][j] = Math.max(0, z[i][j]);
            }
        }
    }

    public Layer feed_input(double[][] input){
        for(int i=0; i<z.length; i++){
            for(int j=0; j<z[0].length; j++){
                z[i][j] = input[i][j];
            }
        }
        return this;
    }

    public void set_activation(double[][] activation){
        for(int i=0; i<z.length; i++){
            for(int j=0; j<z[0].length; j++){
                z[i][j] = activation[i][j];
            }
        }
    }

    public double[][] get_activation(){
        return this.z;
    }

    public double getSum(){
        double sum = 0.0;
        for(int i=0; i<z.length; i++){
            for(int j=0; j<z[0].length; j++){
                sum+=z[i][j];
            }
        }
        return sum;
    }

}
