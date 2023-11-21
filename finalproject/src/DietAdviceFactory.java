public class DietAdviceFactory {
    public static DietAdvice createDietAdvice(DietStrategy strategy) {
        if (strategy instanceof LoseWeightStrategy) {
            return new LoseWeightDietAdvice();
        } else if (strategy instanceof GainWeightStrategy) {
            return new GainWeightDietAdvice();
        } else if (strategy instanceof GainMuscleMassStrategy) {
            return new GainMuscleMassDietAdvice();
        } else if (strategy instanceof ReduceFatStrategy) {
            return new FatReductionDietAdvice();
        } else {
            return null;
        }
    }
}
