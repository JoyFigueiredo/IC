import java.util.ArrayList;

public class Gaussiana {
    
    public double densidadeGaussiana(double x, double media, double desvioPadrao) {
        double r = 1 / (Math.sqrt(2 * Math.PI * desvioPadrao));
        r *= Math.exp(-((x - media) * (x - media)) / (2 * desvioPadrao * desvioPadrao));
        return r;
    }

    public double distribuicaoDensidadeGaussiana(double x, ArrayList<Double> vet) {
        double mean = calculaMedia(vet);
        double dp = calculateStandardDeviation(vet);

        double r = densidadeGaussiana(x, mean, dp);
        return r;
    }

    public double calculaMedia(ArrayList<Double> data) {
        double sum = 0.0;
        for (double value : data) {
            sum += value;
        }
        return sum / data.size();
    }

    public double calculateStandardDeviation(ArrayList<Double> data) {
        double mean = calculaMedia(data);
        double sumOfSquares = 0.0;
        for (double value : data) {
            double diff = value - mean;
            sumOfSquares += diff * diff;
        }
        return Math.sqrt(sumOfSquares / (data.size() - 1)); // Usando n-1 para calcular a amostra dp
    }
}


