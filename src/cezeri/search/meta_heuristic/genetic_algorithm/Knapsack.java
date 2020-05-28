package cezeri.search.meta_heuristic.genetic_algorithm;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Mutator;
import io.jenetics.Phenotype;
import io.jenetics.RouletteWheelSelector;
import io.jenetics.SinglePointCrossover;
import io.jenetics.TournamentSelector;
import io.jenetics.engine.Engine;
import static io.jenetics.engine.EvolutionResult.toBestPhenotype;
import io.jenetics.engine.EvolutionStatistics;
import static io.jenetics.engine.Limits.bySteadyFitness;
import java.util.stream.Stream;


//The main class.
public class Knapsack {

    public static void main(String[] args) {
        int nItems = 15;
        double ksSize = nItems * 100.0 / 3.0;

        KnapsackFF ff = new KnapsackFF(Stream.generate(KnapsackItem::random)
            .limit(nItems)
            .toArray(KnapsackItem[]::new), ksSize);

        Engine<BitGene, Double> engine = Engine.builder(ff, BitChromosome.of(nItems, 0.5))
            .populationSize(500)
            .survivorsSelector(new TournamentSelector<>(5))
            .offspringSelector(new RouletteWheelSelector<>())
            .alterers(new Mutator<>(0.115), new SinglePointCrossover<>(0.16))
            .build();

        EvolutionStatistics<Double, ?> statistics = EvolutionStatistics.ofNumber();

        Phenotype<BitGene, Double> best = engine.stream()
            .limit(bySteadyFitness(7))
            .limit(100)
            .peek(statistics)
            .collect(toBestPhenotype());

        System.out.println(statistics);
        System.out.println(best);
    }
}