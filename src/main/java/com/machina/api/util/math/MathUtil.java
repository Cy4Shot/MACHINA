package com.machina.api.util.math;

import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.DoubleFunction;

public class MathUtil {

    public static final double TWO_PI = Math.PI * 2;

    public static float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }

    public static Vec3 lerp(Vec3 a, Vec3 b, float t) {
        return a.add(b.subtract(a).scale(t));
    }

    public static float bezier(float t, Float... f) {
        return (float) (f[0] * (-Math.pow(t, 3) + 3 * Math.pow(t, 2) - 3 * t + 1)
                + f[1] * (3 * Math.pow(t, 3) - 6 * Math.pow(t, 2) + 3 * t)
                + f[2] * (-3 * Math.pow(t, 3) + 3 * Math.pow(t, 2)) + f[3] * (Math.pow(t, 3)));
    }

    public static Vec3 bezier(float t, Vec3... f) {
        return f[0].scale(-Math.pow(t, 3) + 3 * Math.pow(t, 2) - 3 * t + 1)
                .add(f[1].scale(3 * Math.pow(t, 3) - 6 * Math.pow(t, 2) + 3 * t))
                .add(f[2].scale(-3 * Math.pow(t, 3) + 3 * Math.pow(t, 2))).add(f[3].scale(Math.pow(t, 3)));
    }

    public static float quadraticBezier(float t, float p0, float p1, float p2) {
        return (1 - t) * (1 - t) * p0 + 2 * (1 - t) * t * p1 + t * t * p2;
    }

    public static double positiveModulo(double pNumerator, double pDenominator) {
        return (pNumerator % pDenominator + pDenominator) % pDenominator;
    }

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public static float cos(float pAngle) {
        return (float) Math.cos(pAngle);
    }

    public static float sin(float pAngle) {
        return (float) Math.sin(pAngle);
    }

    public static <K, V extends Comparable<? super V>> LinkedHashMap<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());
        Collections.reverse(list);

        LinkedHashMap<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static double binarySearch(double low, double high, double target, DoubleFunction<Double> func,
                                      double tolerance) {
        double mid = 0.0;
        while (Math.abs(high - low) > tolerance) {
            mid = (low + high) / 2.0;
            double result = func.apply(mid);
            if (Math.abs(result - target) <= tolerance) {
                return mid;
            } else if (result < target) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return mid;
    }

    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    public static float length(float x, float y) {
        return (float) Math.sqrt(x * x + y * y);
    }

    public static float length(float x, float y, float z) {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public static float dot(float x1, float y1, float z1, float x2, float y2, float z2) {
        return x1 * x2 + y1 * y2 + z1 * z2;
    }

    public static float dot(float x1, float y1, float x2, float y2) {
        return x1 * x2 + y1 * y2;
    }

    public static int min(int a, int b) {
        return Math.min(a, b);
    }

    public static int min(int a, int b, int c) {
        return min(a, min(b, c));
    }

    public static int max(int a, int b) {
        return Math.max(a, b);
    }

    public static float min(float a, float b) {
        return Math.min(a, b);
    }

    public static float max(float a, float b) {
        return Math.max(a, b);
    }

    public static float max(float a, float b, float c) {
        return max(a, max(b, c));
    }

    public static int max(int a, int b, int c) {
        return max(a, max(b, c));
    }

    public static int floor(float x) {
        return x < 0 ? (int) (x - 1) : (int) x;
    }

    public static float randRange(RandomSource random, float min, float max) {
        return min + random.nextFloat() * (max - min);
    }

    public static float randRangeSigned(RandomSource random, float min, float max) {
        float val = randRange(random, min, max);
        return random.nextBoolean() ? val : -val;
    }

    public static <T> T randomInList(Set<T> set, Random random) {
        return randomInList(new ArrayList<>(set), random);
    }

    public static <T> T randomInList(List<T> set, Random random) {
        int i = random.nextInt(set.size());
        return set.get(i);
    }

    public static <T> T randomInList(List<T> set, RandomSource random) {
        int i = random.nextInt(set.size());
        return set.get(i);
    }

    private static final int SHUFFLE_THRESHOLD = 5;

    private static void swap(Object[] arr, int i, int j) {
        Object tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void shuffle(List<?> list, RandomSource rnd) {
        int size = list.size();
        if (size < SHUFFLE_THRESHOLD || list instanceof RandomAccess) {
            for (int i = size; i > 1; i--)
                Collections.swap(list, i - 1, rnd.nextInt(i));
        } else {
            Object[] arr = list.toArray();
            for (int i = size; i > 1; i--)
                swap(arr, i - 1, rnd.nextInt(i));
            ListIterator it = list.listIterator();
            for (Object e : arr) {
                it.next();
                it.set(e);
            }
        }
    }

    public static int numTrue(boolean... bs) {
        int num = 0;
        for (boolean b : bs) {
            if (b)
                num++;
        }
        return num;
    }

    public static int unsignedClampToInt(long l) {
        return l > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) l;
    }
}