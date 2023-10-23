
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PerceptronMain {

    private static Amostra[] base;
    private static ArrayList<Amostra> baseUm = new ArrayList<>();
    private static ArrayList<Amostra> baseZero = new ArrayList<>();
    private static ArrayList<Amostra> baseTreino = new ArrayList<>();
    private static ArrayList<Amostra> baseTeste = new ArrayList<>();
    private static ArrayList<double> produtorio = new ArrayList<>();

    public static void main(String[] args) {
        base = ReaderWriter.aux.readWindow();

        for (int i = 0; i < base.length; i++) {

            /*
             * ==================================================
             *                   Normalização
             * ==================================================
             */
            for (int j = 0; j < base[i].X.length; j++) {
                double aux = (base[i].X[j]) / (12500);
                base[i].X[j] = aux;
            }

            /*
             * ==================================================
             *                   primeira parte 0.005
             * ==================================================
             */
            // Verifica Y
            if (base[i].Y[0] == 1) {
                baseUm.add(new Amostra(base[i].X, new double[]{0.995}));
            } else if (base[i].Y[0] == 0) {
                baseZero.add(new Amostra(base[i].X, new double[]{0.005}));
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

        // ====================================
        // For de Treino
        // ====================================
        // Base de treino 70% da base de cada classe
        // Não Faz ajuste dos pesos e nem calculo dos deltas
        double[][] mediaTreino = new double[2][4];
        double[][] mediaTeste = new double[2][4];
        
        double[][] DesvioTreino = new double[2][4];
        double[][] DesvioTeste = new double[2][4];

        double [] quantUmTreino = new double[4];
        double [] quantZeroTreino = new double[4];
        double [] quantUmTeste = new double[4];
        double [] quantZeroTeste = new double[4];

        double [] resultadosTreino = new double[baseTreino.size()];
        /*
            * ====================================================
            *  Calculo de Medias
            * ====================================================
         */
        double somaUm, somaZero;
        int zero = 0, um = 0;

        for (int i = 0; i < 4; i++) {
            somaUm = 0;
            somaZero = 0;
            zero = 0;
            um = 0;

            for (int a = 0; a < baseTreino.size(); a++) {
                if (baseTreino.get(a).Y[0] == 0) {
                    somaZero += baseTreino.get(a).X[i];
                    zero++;
                } else {
                    somaUm += baseTreino.get(a).X[i];
                    um++;
                }
            }
            quantUmTreino[i] = um;
            quantZeroTreino[i] = zero;

            mediaTreino[0][i] = somaZero / zero;
            mediaTreino[1][i] = somaUm / um;
        }

        /*
            * ====================================================
            *  Desvio Padrao
            * ====================================================
         */
        double somatorioUm, somatorioZero;

        for (int j = 0; j < 4; j++) {
            somatorioUm = 0;
            somatorioZero = 0;
            for (int i = 0; i < baseTreino.size(); i++) {
                if (baseTreino.get(i).Y[0] == 0) {
                    somatorioUm += (baseTreino.get(i).X[j] - mediaTreino[0][j]);
                } else {
                    somatorioZero += (baseTreino.get(i).X[j] - mediaTreino[1][j]);
                }
            }
            DesvioTreino[0][j] = somatorioUm;
            DesvioTreino[1][j] = somatorioZero;
        }

        /*
         * =======================================================
         *              Gaussiano + Produtorio P(x/C1)
         * =======================================================
         */
        double result;

        for (int i = 0; i < baseTreino.size(); i++) {
            double aux, r;
            for (int j = 0; j < 4; j++) {
                if(baseTreino.get(i).Y[0] == 0){
                    aux = -Math.pow(baseTreino.get(i).X[j]- mediaTreino[0][j], 2)/(2*Math.pow(DesvioTreino[0][j],2));
                    r = 2 * Math.PI * DesvioTreino[0][j];
                }else{
                    aux = -Math.pow(baseTreino.get(i).X[j]- mediaTreino[1][j], 2)/(2*Math.pow(DesvioTreino[1][j],2));
                    r = 2 * Math.PI * DesvioTreino[1][j];
                }
                result *= Math.exp(aux)/Math.sqrt(r); 
            }
            resultadosTreino[i] = result; 
        }

        /*
            *==================================================== 
            *               Produtorio  
            *====================================================
         */
        


        // ========================================================
        // For de Teste
        // ========================================================
        
    }

}
