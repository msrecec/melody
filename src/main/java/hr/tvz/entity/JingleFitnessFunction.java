package hr.tvz.entity;

import org.jgap.Chromosome;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

import java.util.ArrayList;

public class JingleFitnessFunction extends FitnessFunction {


    /**
     *
     */
    private static final long serialVersionUID = 8388979018240378718L;
    public static ArrayList<Integer> l;

    public JingleFitnessFunction(ArrayList<Integer> arg) {
        l = arg;
    }

    public static ArrayList<Integer> getGenes(Chromosome chrom) {
        ArrayList<Integer> geneList = new ArrayList<Integer>();
        for (int i = 0; i < l.size(); i++) {
            geneList.add(getGeneAt(i, chrom));
        }
        return geneList;
    }

    public static Integer getGeneAt(int i, Chromosome chrom) {
        return (Integer) chrom.getGene(i).getAllele();
    }

    protected double evaluate(IChromosome chrom) {
        double fitness = 6 * l.size();
        ArrayList<Integer> geneList = getGenes((Chromosome) chrom);
        for (int i = 0; i < l.size(); i++) {
            fitness -= Math.abs(l.get(i) - geneList.get(i));
        }
        return fitness;
    }


}
