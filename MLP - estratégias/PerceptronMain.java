import java.util.ArrayList;
import java.util.Collections;

public class PerceptronMain {
    /*
     *Estimator de densidade gaussiano
     * f(x; μ, σ) = 1 / (σ √(2π)) exp(-(x - μ)^2 / (2 σ^2))
     * μ é a média da distribuição
     * σ é o desvio padrão da distribuição
     * 
     * Estimator KDE
     * f(x; X) = 1 / n ∑_{i=1}^n K((x - xi) / h)
     * X é o conjunto de treinamento
     * n é o tamanho do conjunto de treinamento
     * K é uma função kernel
     * h é um parâmetro de largura
     * 
     * Classificador bayesiano
     * P(C | x) = P(x | C) P(C) / P(x)
     * C é a classe
     * x é o ponto de dados
     * P(x | C) é a verossimilhança
     * P(C) é a probabilidade a priori
     * P(x) é a probabilidade da evidência
     * 
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

            for (int j = 0; j < base[i].X.length; j++) {
                double aux = (base[i].X[j]) / (12500);
                base[i].X[j] = aux;
            }

            // Verifica Y
            if (base[i].Y[0] == 1) {
                baseUm.add(new Amostra(base[i].X, new double[] { 0.995 }));
            } else if (base[i].Y[0] == 0) {
                baseZero.add(new Amostra(base[i].X, new double[] { 0.005 }));
            }

        }
        /*
         * for(int i=0; i<10;i++){
         * System.out.println(baseUm.get(i).toString());
         * System.out.println(baseZero.get(i).toString());
         * }
         */

        /*
         * ====================================
         * EMBARALHAR BASES
         * ====================================
         */

        Collections.shuffle(baseUm);
        Collections.shuffle(baseZero);
        /*
         * ==========================================
         * DIVIDIR E ADD TREINO 70% E TESTE 30%
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
        RNA p = new MLP(qtdIn, 6, qtdOut, 0.0001);
        EstimativaGaussiana eg = new EstimativaGaussiana();

        for (int e = 0; e < 1000; e++) {
            erroApEpocaTeste = 0;
            erroApEpocaTreino = 0;
            erroClaEpocaTeste = 0;
            erroClaEpocaTreino = 0;

            // ====================================
            // For de Treino
            // ====================================

            // Base de treino 70% da base de cada classe
            // Não Faz ajuste dos pesos e nem calculo dos deltas
            for (int a = 0; a < baseTreino.size(); a++) {

                double[] X = baseTreino.get(a).X;
                double[] Y = baseTreino.get(a).Y;
                double[] teta = p.treinar(X, Y);

                double tetaT[] = new double[teta.length];
                for (int i = 0; i < teta.length; i++) {
                    tetaT[i] = (teta[i] >= 0.5) ? 0.995 : 0.005;// teta trechoud
                }

                double erroAmostra = 0;
                double erroCla = 0;

                for (int j = 0; j < teta.length; j++) {
                    erroAmostra += Math.abs(Y[j] - teta[j]);
                    erroCla += Math.abs(Y[j] - tetaT[j]);
                }

                erroApEpocaTreino += erroAmostra;
                erroClaEpocaTreino += (erroCla > 0) ? 0.995 : 0.005; // maior que 0 manda 1

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
                    tetaT[i] = (teta[i] >= 0.5) ? 0.995 : 0.005;// teta trechoud
                }

                double erroAmostra = 0;
                double erroCla = 0;

                for (int j = 0; j < teta.length; j++) {
                    erroAmostra += Math.abs(Y[j] - teta[j]);
                    erroCla += Math.abs(Y[j] - tetaT[j]);
                }

                erroApEpocaTeste += erroAmostra;
                erroClaEpocaTeste += (erroCla > 0) ? 0.995 : 0.005; // maior que 0 manda 1
            }

            System.out.printf(
                    "Epoca Treino: %d - erro: %f - erro Cla: %f  Epoca Teste: %d - erro: %f - erro Cla: %f \n",
                    e, erroApEpocaTreino, erroClaEpocaTreino, e, erroApEpocaTeste, erroClaEpocaTeste);
        }
    }
}