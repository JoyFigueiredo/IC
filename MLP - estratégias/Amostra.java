public class Amostra {
    static int qtdIn=4, qtdOut=1, amostra=748;

    double[] X;
    double[] Y;

    public Amostra() {
        this.X = new double[qtdIn];
        this.Y = new double[qtdOut];
    }

    public Amostra(double[] X, double[] Y) {
        this.X = X;
        this.Y = Y;
    }
}