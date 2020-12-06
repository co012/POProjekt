package simulator;

import java.util.Random;

public class Genotype {
    private static final int GENS_TYPES_NUMBER = 8;
    private static final int GENOTYPE_LENGTH = 32;
    private final int[] genotypeArray;

    public Genotype(){
        genotypeArray = new int[GENOTYPE_LENGTH];
        Random random = new Random();
        for (int i = 0; i < GENOTYPE_LENGTH; i++) {
            genotypeArray[i] = random.nextInt(GENS_TYPES_NUMBER);
        }
        fixGenotype();
    }

    public Genotype(Genotype genotype1,Genotype genotype2){
        Random random = new Random();
        int cut1 = random.nextInt(GENOTYPE_LENGTH);
        int cut2 = random.nextInt(GENOTYPE_LENGTH);

        int firstCut = Math.min(cut1,cut2);
        int secondCut = Math.max(cut1,cut2);
        genotypeArray = new int[GENOTYPE_LENGTH];

        System.arraycopy(genotype1.genotypeArray, 0, genotypeArray, 0, firstCut);
        System.arraycopy(genotype2.genotypeArray, firstCut, genotypeArray, firstCut, secondCut - firstCut);
        System.arraycopy(genotype1.genotypeArray, secondCut, genotypeArray, secondCut, GENOTYPE_LENGTH - secondCut);

        fixGenotype();
    }

    private void fixGenotype() {
        int[] genFrequency = getGenTypeFrequency();

        for (int i = 0; i < GENOTYPE_LENGTH; i++) {
            if(genFrequency[i] > 0) continue;
            insertGen(i);
        }


    }

    public int[] getGenTypeFrequency(){
        int[] genFrequency = new int[GENS_TYPES_NUMBER];
        for (int i = 0; i < GENOTYPE_LENGTH; i++) {
            genFrequency[genotypeArray[i]]++;
        }
        return genFrequency;
    }

    private void insertGen(int gen){
        int[] genFrequency = getGenTypeFrequency();
        Random random = new Random();

        int i = random.nextInt(GENS_TYPES_NUMBER);

        while (genFrequency[i] < 2){
            i++;
            i%=GENS_TYPES_NUMBER;
        }

        for (int j = 0; j < GENOTYPE_LENGTH; j++) {
            if(genotypeArray[j] != i) continue;

            genotypeArray[j] = gen;
            return;
        }


    }
}
