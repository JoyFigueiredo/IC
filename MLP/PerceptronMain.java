import java.util.ArrayList;
import java.util.Collections;

public class PerceptronMain {
    /*
     * private static double [][][] base = new double [][][]{
     * {{0,0},{0}},
     * {{0,1},{1}},
     * {{1,0},{1}},
     * {{1,1},{0}}
     * };
     */
    private static Amostra[] base;
    private static ArrayList<Amostra> baseUm = new ArrayList<>();
    private static ArrayList<Amostra> baseZero = new ArrayList<>();
    private static ArrayList<Amostra> baseTreino = new ArrayList<>();
    private static ArrayList<Amostra> baseTeste = new ArrayList<>();

    public static void main(String[] args) {
        base = ReaderWriter.aux.readWindow();

        for (int i = 0; i < base.length; i++) {
            double[] X = base[i].X;
            double[] Y = base[i].Y;

            // Verifica Y
            if (Y[Y.length - 1] == 1) {
                baseUm.add(base[i]);
            } else if (Y[Y.length - 1] == 0) {
                baseZero.add(base[i]);
            }
        }
        /*
         * ====================================
         *          EMBARALHAR BASES
         * ====================================
         */

        Collections.shuffle(baseUm);
        Collections.shuffle(baseZero);
        /*
         * ==========================================
         *    DIVIDIR E ADD TREINO 70% E TESTE 30%
         * ==========================================
         */
        // sublist(inicio da sublista, termino da mesma);
        // ja foi embaralhada;
        baseTreino.addAll(baseUm.subList(0, (int) (0.7 * baseUm.size())));
        baseTreino.addAll(baseZero.subList(0, (int) (0.7 * baseZero.size())));

        baseTeste.addAll(baseUm.subList((int) (0.7 * baseUm.size()), baseUm.size()));
        baseTeste.addAll(baseZero.subList((int) (0.7 * baseZero.size()), baseZero.size()));

        int qtdIn = 4;
        int qtdOut = 1;
        double erroClaEpocaTeste, erroClaEpocaTreino, erroApEpocaTeste, erroApEpocaTreino;

        // RNA p = new Perceptron(4, 1, 0.000001);
        RNA p = new MLP(qtdIn, 2, qtdOut, 0.0001);
        

        for (int e = 0; e < 100000; e++) {
            erroApEpocaTeste = 0;
            erroApEpocaTreino = 0;
            erroClaEpocaTeste = 0;
            erroClaEpocaTreino = 0;

            // ====================================
            //          For de Treino
            // ====================================

            // Base de treino 70% da base de cada classe
            // Não Faz ajuste dos pesos e nem calculo dos deltas
            for (int a = 0; a < baseTreino.size(); a++) {

                double[] X = baseTreino.get(a).X;
                double[] Y = baseTreino.get(a).Y;
                double[] teta = p.treinar(X, Y);

                double tetaT[] = new double[teta.length];
                for (int i = 0; i < teta.length; i++) {
                    tetaT[i] = (teta[i] >= 0.5) ? 1 : 0;// teta trechoud
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

            // ========================================================
            // For de Teste
            // ========================================================
            for (int a = 0; a < baseTeste.size(); a++) {

                double[] X = baseTeste.get(a).X;
                double[] Y = baseTeste.get(a).Y;
                double[] teta = p.teste(X, Y);

                double tetaT[] = new double[teta.length];
                for (int i = 0; i < teta.length; i++) {
                    tetaT[i] = (teta[i] >= 0.5) ? 1 : 0;// teta trechoud
                }

                double erroAmostra = 0;
                double erroCla = 0;

                for (int j = 0; j < teta.length; j++) {
                    erroAmostra += Math.abs(Y[j] - teta[j]);
                    erroCla += Math.abs(Y[j] - tetaT[j]);
                }

                erroApEpocaTeste += erroAmostra;
                erroClaEpocaTeste += (erroCla > 0) ? 1 : 0; // maior que 0 manda 1
            }

            System.out.printf("Epoca Treino: %d - erro: %f - erro Classificacao: %f \n", e, erroApEpocaTreino, erroClaEpocaTreino);
            System.out.printf("Epoca Teste: %d - erro: %f - erro Classificacao: %f \n", e, erroApEpocaTeste, erroClaEpocaTeste);
        }
    }
}