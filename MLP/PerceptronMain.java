public class PerceptronMain {
    /* 
    private static double [][][] base = new double [][][]{
        {{0,0},{0}},
        {{0,1},{1}},
        {{1,0},{1}},
        {{1,1},{0}}
    };
    */
    private static Amostra[] base;
    public static void main(String[] args) {
        base = ReaderWriter.aux.readWindow();
        int qtdIn = 4;
        int qtdOut = 1;

        //RNA p = new Perceptron(4, 1, 0.000001);
        RNA p = new MLP(qtdIn,4,qtdOut , 0.0001);
        
        for (int e = 0; e < 100000; e++) {
            double erroEpoca = 0;
            double erroClaEpoca = 0;

            for (int a = 0; a < base.length; a++) {

                double[] X = base[a].X;
                double[] Y = base[a].Y;
                double[] teta = p.treinar(X, Y);

                double tetaT[] = new double[teta.length];
                for (int i = 0; i < teta.length; i++) {
                    tetaT[i] = (teta[i] >= 0.5) ? 1 : 0;//teta trechoud
                }

                double erroAmostra = 0;
                double erroCla = 0;

                for (int j = 0; j < teta.length; j++) {
                    erroAmostra += Math.abs(Y[j] - teta[j]);
                    erroCla += Math.abs(Y[j] - tetaT[j]);
                }

                erroEpoca += erroAmostra;
                erroClaEpoca += (erroCla > 0) ? 1 : 0; // maior que 0 manda 1
            }

            System.out.printf("Epoca: %d - erro: %f - erro Classificacao: %f \n", e,erroEpoca, erroClaEpoca);
        }
    }
}

