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
        
        for (int i = 0; i < base.length; i++) {
            double[] X = base[i].X;
            double[] Y = base[i].Y;
        }

        int qtdIn = 4;
        int qtdOut = 1;
        double erroClaEpocaTeste, erroClaEpocaTreino, erroApEpocaTeste, erroApEpocaTreino;

        //RNA p = new Perceptron(4, 1, 0.000001);
        RNA p = new MLP(qtdIn,4,qtdOut , 0.0001);
        
        for (int e = 0; e < 100000; e++) {
            erroApEpocaTeste = 0;
            erroApEpocaTreino = 0;
            erroClaEpocaTeste = 0;
            erroClaEpocaTreino = 0;

            //====================================
            //      For de Treino
            //====================================

            //Base de treino 70% da base de cada classe
            //Faz ajuste dos pesos e nem calculo dos deltas
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

                erroApEpocaTreino += erroAmostra;
                erroClaEpocaTreino += (erroCla > 0) ? 1 : 0; // maior que 0 manda 1
            }

            //========================================================
            //         For de Teste
            //========================================================
            for (int a = 0; a < base.length; a++) {

                double[] X = base[a].X;
                double[] Y = base[a].Y;
                double[] teta = p.teste(X, Y);

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

                erroApEpocaTreino += erroAmostra;
                erroClaEpocaTreino += (erroCla > 0) ? 1 : 0; // maior que 0 manda 1
            }

            System.out.printf("Epoca Treino: %d - erro: %f - erro Classificacao: %f \n", e,erroApEpocaTreino, erroClaEpocaTreino);
            System.out.printf("Epoca Teste: %d - erro: %f - erro Classificacao: %f \n", e,erroApEpocaTeste, erroClaEpocaTeste);
        }
    }
}

