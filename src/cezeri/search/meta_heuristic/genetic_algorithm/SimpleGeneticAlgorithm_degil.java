package cezeri.search.meta_heuristic.genetic_algorithm;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;


public class SimpleGeneticAlgorithm_degil {

    private static Integer eval(Genotype<BitGene> gt) {
        return gt.getChromosome()
            .as(BitChromosome.class)
            .bitCount();
    }

    public static void main(String[] args) {
        Factory<Genotype<BitGene>> gtf = Genotype.of(BitChromosome.of(10, 0.5));
        System.out.println("Before the evolution:\n" + gtf);

        Engine<BitGene, Integer> engine = Engine.builder(SimpleGeneticAlgorithm_degil::eval, gtf)
            .build();

        Genotype<BitGene> result = engine.stream()
            .limit(500)
            .collect(EvolutionResult.toBestGenotype());

        System.out.println("After the evolution:\n" + result);

    }

}
