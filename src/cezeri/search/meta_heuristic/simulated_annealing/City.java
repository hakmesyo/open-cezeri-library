package cezeri.search.meta_heuristic.simulated_annealing;

import java.util.Random;
import java.util.SplittableRandom;
import lombok.Data;
import org.apache.commons.math3.random.JDKRandomGenerator;

@Data
public class City {

    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public City() {
        this.x = 50 + (int) (Math.random() * 400);
        this.y = 50 + (int) (Math.random() * 400);
    }

    public City(boolean isSeeded,Random rnd) {
        if (!isSeeded) {
            this.x = 50 + (int) (Math.random() * 400);
            this.y = 50 + (int) (Math.random() * 400);
        } else {
            this.x = 50 + rnd.nextInt(400);
            this.y = 50 + rnd.nextInt(400);
        }
    }

    public double distanceToCity(City city) {
        int x = Math.abs(getX() - city.getX());
        int y = Math.abs(getY() - city.getY());
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

}
