package simulator.statistics;

import simulator.map.Genotype;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsPack {
    public final int day;

    public final int animalsNumber;
    public final int plantsNumber;
    public final double avgEnergy;
    public final double avgChildrenNumber;
    public final double lifeExpectancy;
    public final Map.Entry<Genotype, Long> mostPopularGenotypeAndFrequency;


    public StatisticsPack(int day, int animalsNumber, int plantsNumber, double avgEnergy, double avgChildrenNumber, double lifeExpectancy, Map.Entry<Genotype, Long> mostPopularGenotypeAndFrequency) {
        this.day = day;
        this.animalsNumber = animalsNumber;
        this.plantsNumber = plantsNumber;
        this.avgEnergy = avgEnergy;
        this.avgChildrenNumber = avgChildrenNumber;
        this.lifeExpectancy = lifeExpectancy;
        this.mostPopularGenotypeAndFrequency = mostPopularGenotypeAndFrequency;
    }


    @Override
    public String toString() {
        return "StatisticsPack{" +
                "day=" + day +
                ", animalsNumber=" + animalsNumber +
                ", plantsNumber=" + plantsNumber +
                ", avgEnergy=" + avgEnergy +
                ", avgChildrenNumber=" + avgChildrenNumber +
                ", lifeExpectancy=" + lifeExpectancy +
                ", mostPopularGenotypeAndFrequency=" + mostPopularGenotypeAndFrequency.getKey().toString() + ":" + mostPopularGenotypeAndFrequency.getValue().toString() +
                '}';
    }

    public static StatisticsPack getAvgStatisticsPack(List<StatisticsPack> statisticsPacks, int toDay) {
        int animalsNumber = 0;
        int plantsNumber = 0;
        double avgEnergy = 0;
        double avgChildrenNumber = 0;
        double lifeExpectancy = 0;

        HashMap<Genotype, Long> mostPopularGenotypesToDay = new HashMap<>();
        for (StatisticsPack statisticsPack : statisticsPacks) {
            //assumption: statisticsPacks is ordered by day
            if (statisticsPack.day > toDay) break;
            animalsNumber += statisticsPack.animalsNumber;
            plantsNumber += statisticsPack.plantsNumber;
            avgEnergy += statisticsPack.avgEnergy;
            avgChildrenNumber += statisticsPack.avgChildrenNumber;
            lifeExpectancy += statisticsPack.lifeExpectancy;

            Genotype mostPopularGenotype = statisticsPack.mostPopularGenotypeAndFrequency.getKey();
            long frequencyValue = statisticsPack.mostPopularGenotypeAndFrequency.getValue();


            if (mostPopularGenotypesToDay.containsKey(mostPopularGenotype)) {
                long totalFrequency = mostPopularGenotypesToDay.get(mostPopularGenotype);
                mostPopularGenotypesToDay.replace(mostPopularGenotype, totalFrequency + frequencyValue);
            } else {
                mostPopularGenotypesToDay.put(mostPopularGenotype, frequencyValue);
            }
        }

        animalsNumber /= statisticsPacks.size();
        plantsNumber /= statisticsPacks.size();
        avgEnergy /= statisticsPacks.size();
        avgChildrenNumber /= statisticsPacks.size();
        lifeExpectancy /= statisticsPacks.size();

        Map.Entry<Genotype, Long> mostPopularGenotypeAndFrequency = mostPopularGenotypesToDay.entrySet()
                .stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .orElse(Map.entry(Genotype.EMPTY, 0L));

        return new StatisticsPack(
                toDay,
                animalsNumber,
                plantsNumber,
                avgEnergy,
                avgChildrenNumber,
                lifeExpectancy,
                mostPopularGenotypeAndFrequency
        );

    }
}
