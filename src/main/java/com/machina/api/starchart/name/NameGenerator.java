package com.machina.api.starchart.name;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.machina.api.util.math.NormalDistribution;

public abstract class NameGenerator {
    record NameLengthInfo(double mean, double standardDeviation) {
    }

    record ProbabilityTable(Map<String, Map<String, Double>> probabilities, NameLengthInfo nameLengthInfo) {
    }

    private static NameLengthInfo getNameLengthInfo(String input) {
        String[] names = input.split(" ");
        double[] namesLengths = Arrays.stream(names).mapToDouble(String::length).toArray();
        double mean = Arrays.stream(namesLengths).average().orElse(0.0);
        double standardDeviation = Math
                .sqrt(Arrays.stream(namesLengths).map(x -> (x - mean) * (x - mean)).average().orElse(0.0));
        return new NameLengthInfo(mean, standardDeviation);
    }

    private static int getNameLength(NameLengthInfo nameLengthInfo, int minimumLength) {
        double mean = nameLengthInfo.mean;
        double standardDeviation = nameLengthInfo.standardDeviation;
        NormalDistribution normalDistribution = new NormalDistribution(mean, standardDeviation);
        int length = (int) Math.round(normalDistribution.sample());
        return Math.max(length, minimumLength);
    }

    private static Map<String, Double> countOccurrences(String input, Map<String, Double> occurrenceTable, int length) {
        for (int i = 0; i <= input.length() - length; i++) {
            String occurrence = input.substring(i, i + length);
            occurrenceTable.put(occurrence, occurrenceTable.getOrDefault(occurrence, 0.0) + 1.0);
        }
        return occurrenceTable;
    }

    private static Map<String, Map<String, Double>> cumulate(Map<String, Double> map) {
        double total = map.values().stream().mapToDouble(Double::doubleValue).sum();
        Map<String, Double> cumulativeSubMap = new LinkedHashMap<>();
        double cumulative = 1.0;

        for (Entry<String, Double> entry : map.entrySet()) {
            cumulative -= entry.getValue() / total;
            cumulativeSubMap.put(entry.getKey(), Math.round(cumulative * 1e6) / 1e6);
        }
        return Map.of("", cumulativeSubMap);
    }

    private static ProbabilityTable buildProbabilityTable(String input, int length) {
        NameLengthInfo nameLengths = getNameLengthInfo(input.toLowerCase());
        Map<String, Double> occurrencesTable = countOccurrences(input.toLowerCase(), new HashMap<>(), length);
        Map<String, Map<String, Double>> table = new HashMap<>();

        for (Entry<String, Double> entry : occurrencesTable.entrySet()) {
            String key = entry.getKey();
            String mainKey = key.substring(0, 1);
            String subKey = key.substring(1);
            double value = entry.getValue();

            if (subKey.chars().allMatch(Character::isLowerCase)) {
                table.computeIfAbsent(mainKey, k -> new HashMap<>()).put(subKey, value);
            }
        }

        table.replaceAll((k, v) -> cumulate(v).get(""));
        return new ProbabilityTable(table, nameLengths);
    }

    private static String pickString(Map<String, Double> values, Random rnd) {
        double randomValue = rnd.nextDouble();
        return values.entrySet().stream().filter(entry -> randomValue >= entry.getValue()).map(Entry::getKey)
                .findFirst().orElseThrow(() -> new RuntimeException("Can't pick letter"));
    }

    private static String buildName(String nameSoFar, int charLeft, ProbabilityTable probabilityTable, Random rand) {
        String lastChar = nameSoFar.substring(nameSoFar.length() - 1);
        String addition;

        if (probabilityTable.probabilities.containsKey(lastChar)) {
            addition = pickString(probabilityTable.probabilities.get(lastChar), rand);
        } else {
            addition = pickString(probabilityTable.probabilities.get(" "), rand);
        }

        String newName = nameSoFar + addition;
        int newCharLeft = charLeft - addition.length();

        if (newCharLeft > 0) {
            return buildName(newName, newCharLeft, probabilityTable, rand);
        } else if (newCharLeft < 0) {
            return newName.substring(0, newName.length() - 1);
        } else {
            return newName;
        }
    }

    private static String generateRandomName(ProbabilityTable probabilityTable, int minimumLength, Random rand) {
        int nameLength = getNameLength(probabilityTable.nameLengthInfo, minimumLength);
        String lowerCaseName = buildName(" ", nameLength, probabilityTable, rand);
        return Character.toUpperCase(lowerCaseName.charAt(1)) + lowerCaseName.substring(2);
    }

    private final ProbabilityTable table;

    public NameGenerator() {
        table = buildProbabilityTable(getNames(), getLength());
    }

    public String gen(Random rand) {
        return generateRandomName(table, getMinLength(), rand);
    }

    public abstract String getNames();

    public abstract int getLength();

    public abstract int getMinLength();
}
