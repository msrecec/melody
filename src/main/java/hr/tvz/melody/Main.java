package hr.tvz.melody;

import hr.tvz.entity.JingleFitnessFunction;
import org.jfugue.player.Player;
import org.jgap.*;
import org.jgap.impl.CrossoverOperator;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.MutationOperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final int NUMBER_OF_EVOLUTIONS = 10000;
    private static final int POPULATION = 10;
    private static final int PLAY_RATE = 10;
    private static String TARGET = "E D C D E E E D D D E G G";

    public static void main(String[] args) throws InvalidConfigurationException {
        int evolutions = 0;
        TARGET = stringToInt(TARGET);
        Player player = new Player();
        ArrayList<Integer> TARGET_INT = new ArrayList<>();
        for (int i = 0; i < TARGET.length(); i++) {
            TARGET_INT.add(Character.getNumericValue(TARGET.charAt(i)));
        }

        Configuration conf = new DefaultConfiguration();
        FitnessFunction myFunc = new JingleFitnessFunction(TARGET_INT);
        conf.setFitnessFunction(myFunc);
        conf.setKeepPopulationSizeConstant(true);
        conf.setPreservFittestIndividual(true);
        conf.getGeneticOperators().clear();
        conf.addGeneticOperator(new MutationOperator(conf, 10));
        conf.addGeneticOperator(new CrossoverOperator(conf, 90));

        Gene[] sampleGenes = new Gene[TARGET.length()];

        for (int i = 0; i < TARGET.length(); i++) {
            sampleGenes[i] = new IntegerGene(conf, 1, 7);
        }

        Chromosome sampleChromosome = new Chromosome(conf, sampleGenes);

        conf.setSampleChromosome(sampleChromosome);
        conf.setPopulationSize(POPULATION);
        Genotype population = Genotype.randomInitialGenotype(conf);

        Chromosome theBest;
        ArrayList<Integer> geneList = new ArrayList<>();

        long startTime = System.nanoTime();

        ArrayList<String> jingleList = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_EVOLUTIONS; i++) {
            if (i % PLAY_RATE == 0) {

                Chromosome best = (Chromosome) population.getFittestChromosome();

                List<Integer> temp = Arrays
                        .stream(best.getGenes())
                        .map(gene -> (Integer)gene.getAllele())
                        .collect(Collectors.toList());

                jingleList.add(intToString(temp));
            }
            if (population.getFittestChromosome().getFitnessValue() == 6 * TARGET.length()) {
                break;
            }

            population.evolve();
            evolutions++;
        }
        long timeElapsed = System.nanoTime() - startTime;

        for(int i = 0; i < jingleList.size(); ++i) {
            System.out.println("Playing " + i * PLAY_RATE + ". evolution jingle");
            System.out.println(jingleList.get(i));
            player.play(jingleList.get(i));
        }

        theBest = (Chromosome) population.getFittestChromosome();

        geneList.clear();

        for (int j = 0; j < TARGET.length(); j++) {
            geneList.add((Integer) theBest.getGenes()[j].getAllele());
        }

        String jingle = intToString(geneList);
        System.out.println("Best Jingle:\n" + jingle);

        System.out.println("Best fitness " + theBest.getFitnessValue());
        System.out.println("Number of evolutions " + evolutions);
        System.out.println((timeElapsed / 1000000) + "ms");
        player.play(jingle);
    }

    static private String stringToInt(String s) {
        s = s.replaceAll("\\s", "");
        s = s.replaceAll("A", "1");
        s = s.replaceAll("H", "2");
        s = s.replaceAll("C", "3");
        s = s.replaceAll("D", "4");
        s = s.replaceAll("E", "5");
        s = s.replaceAll("F", "6");
        s = s.replaceAll("G", "7");
        return s;
    }

    static private String intToString(List<Integer> list) {
        String s = "";
        for (int i = 0; i < list.size(); i++) {
            s += list.get(i) + " ";
        }
        s.trim();
        s = s.replaceAll("1", "A");
        s = s.replaceAll("2", "H");
        s = s.replaceAll("3", "C");
        s = s.replaceAll("4", "D");
        s = s.replaceAll("5", "E");
        s = s.replaceAll("6", "F");
        s = s.replaceAll("7", "G");
        return s;
    }
}
