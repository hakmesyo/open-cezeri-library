package cezeri.search.meta_heuristic.genetic_algorithm;

import java.util.function.Function;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;

public class KnapsackFF implements Function<Genotype<BitGene>, Double> {
    private KnapsackItem[] items;
    private double size;

    public KnapsackFF(KnapsackItem[] items, double size) {
        this.items = items;
        this.size = size;
    }

    @Override
    public Double apply(Genotype<BitGene> gt) {
        KnapsackItem sum = ((BitChromosome) gt.getChromosome()).ones()
            .mapToObj(i -> items[i])
            .collect(KnapsackItem.toSum());
        return sum.size <= this.size ? sum.value : 0;
    }
}
