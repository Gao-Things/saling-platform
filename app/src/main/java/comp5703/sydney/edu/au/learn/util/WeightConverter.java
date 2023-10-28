package comp5703.sydney.edu.au.learn.util;

public class WeightConverter {

    // 定义常量: 1 kg = 1000 g，1 oz ≈ 28.35 g
    private static final double KG_TO_GRAMS = 1000;
    private static final double OZ_TO_GRAMS = 28.35;

    /**
     * Convert kilograms to grams.
     *
     * @param kg weight in kilograms
     * @return weight in grams
     */
    public static double kgToGrams(double kg) {
        return kg * KG_TO_GRAMS;
    }

    /**
     * Convert ounces to grams.
     *
     * @param oz weight in ounces
     * @return weight in grams
     */
    public static double ozToGrams(double oz) {
        return oz * OZ_TO_GRAMS;
    }
}

