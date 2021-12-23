package cezeri.machine_learning.classifiers.neural_net;

public class TestIris {

    public static int TRAINING_SAMPLES = 150;
    public static int EPOCHS = 25000;
    public static double LEARNING_RATE = 0.01;
    public static int INPUT_NODES = 4;
    public static int OUTPUT_NODES = 3;

    public static void main(String[] args) {

        double[][][] input = new double[TRAINING_SAMPLES][1][INPUT_NODES];
        double[][][] output = new double[TRAINING_SAMPLES][1][OUTPUT_NODES];
        
        
        input = Datasets.loadIrisData().get("X");
        output = Datasets.loadIrisData().get("Y");

        //int size = input.length;

        Model model = new Model();
        model.addLayer(new Layer(INPUT_NODES));
        model.addLayer(new Layer(3));
        model.addLayer(new Layer(3));
        model.addLayer(new Layer(3));
        model.addLayer(new Layer(13));
        model.addLayer(new Layer(OUTPUT_NODES));
        model.fit(input, output, LEARNING_RATE, EPOCHS);
        for(int i=0; i<input.length; i++){
            model.predict(input[i], output[i], i+1);
        }






    }

}
