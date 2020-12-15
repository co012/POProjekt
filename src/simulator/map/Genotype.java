package simulator.map;

import java.util.Arrays;
import java.util.Random;

public class Genotype {
    public static final int GENS_TYPES_NUMBER = 8;
    public static final int GENOTYPE_LENGTH = 32;
    private final int[] genotypeArray;
    private final int[] geneFrequencyArray;
    private final Random random = new Random();

    public static final Genotype EMPTY = new Genotype(null);

    public Genotype() {
        genotypeArray = new int[GENOTYPE_LENGTH];
        for (int i = 0; i < GENOTYPE_LENGTH; i++) {
            genotypeArray[i] = random.nextInt(GENS_TYPES_NUMBER);
        }
        fixGenotype();

        geneFrequencyArray = getGenTypeFrequency();


    }

    private Genotype(Void empty) {
        genotypeArray = null;
        geneFrequencyArray = new int[GENS_TYPES_NUMBER];
    }

    public Genotype(Genotype genotype1, Genotype genotype2) {

        int cut1 = random.nextInt(GENOTYPE_LENGTH);
        int cut2 = random.nextInt(GENOTYPE_LENGTH);

        int firstCut = Math.min(cut1, cut2);
        int secondCut = Math.max(cut1, cut2);
        genotypeArray = new int[GENOTYPE_LENGTH];

        System.arraycopy(genotype1.genotypeArray, 0, genotypeArray, 0, firstCut);
        System.arraycopy(genotype2.genotypeArray, firstCut, genotypeArray, firstCut, secondCut - firstCut);
        System.arraycopy(genotype1.genotypeArray, secondCut, genotypeArray, secondCut, GENOTYPE_LENGTH - secondCut);
        //TODO:Do i need to make genotypes ratio constant
        fixGenotype();

        geneFrequencyArray = getGenTypeFrequency();

    }

    private void fixGenotype() {
        int[] genFrequency = getGenTypeFrequency();

        for (int i = 0; i < GENS_TYPES_NUMBER; i++) {
            if (genFrequency[i] > 0) continue;
            insertGen(i);
        }

    }

    public int[] getGenTypeFrequency() {
        int[] genFrequency = new int[GENS_TYPES_NUMBER];
        for (int i = 0; i < GENOTYPE_LENGTH; i++) {
            genFrequency[genotypeArray[i]]++;
        }
        return genFrequency;
    }

    private void insertGen(int gen) {
        int[] genFrequency = getGenTypeFrequency();

        int i = random.nextInt(GENS_TYPES_NUMBER);

        while (genFrequency[i] < 2) {
            i++;
            i %= GENS_TYPES_NUMBER;
        }

        for (int j = 0; j < GENOTYPE_LENGTH; j++) {
            if (genotypeArray[j] != i) continue;
            genotypeArray[j] = gen;
            return;
        }
    }

    public int getRandomGen() {
        int i = random.nextInt(GENOTYPE_LENGTH);
        return genotypeArray[i];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genotype genotype = (Genotype) o;
        return Arrays.equals(geneFrequencyArray, genotype.geneFrequencyArray);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(geneFrequencyArray);
    }

    public int[] getGeneFrequencyArray() {
        return Arrays.copyOf(geneFrequencyArray, GENS_TYPES_NUMBER);
    }

    @Override
    public String toString() {
        return "Genotype{" +
                "genotypeArray=" + Arrays.toString(genotypeArray) +
                '}';
    }
}
