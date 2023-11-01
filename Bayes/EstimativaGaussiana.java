import java.util.List;

public class EstimativaGaussiana {
    public static double distribDensidadeProbKde(double x, List<Double> xiVet) {
        double media = calculaMedia(xiVet);
        double dp = calculateStandardDeviation(xiVet);
        int N = xiVet.size();
        double h = 1.06 * dp * Math.pow(N, -1.0 / 5.0);

        double result = 0.0;
        for (Double xi : xiVet) {
            result += (1.0 / h) * Kernel(x, xi, h);
        }
        result = result * (1.0 / N);
        return result;
    }

    private static double calculaMedia(List<Double> values) {
        double soma = 0.0;
        for (Double value : values) {
            soma += value;
        }
        return soma / values.size();
    }

    private static double calculateStandardDeviation(List<Double> values) {
        double media = calculaMedia(values);
        double soma = 0.0;
        for (Double value : values) {
            soma += Math.pow(value - media, 2);
        }
        return Math.sqrt(soma / values.size());
    }

    private static double Kernel(double x, double xi, double h) {
        double r = 1.0 / Math.sqrt(2 * Math.PI);
        r = r * Math.exp(-Math.pow((x - xi) / h, 2) / 2);
        return r;
    }
}
