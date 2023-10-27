
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PerceptronMain {

    private static Amostra[] base;

    /*
    * ======================================================================
    *                  Arrays e Vetores
    * ======================================================================
     */
    private static ArrayList<Amostra> baseUm = new ArrayList<>();
    private static ArrayList<Amostra> baseZero = new ArrayList<>();
    private static ArrayList<Amostra> baseTreinoUm = new ArrayList<>();
    private static ArrayList<Amostra> baseTreinoZero = new ArrayList<>();
    private static ArrayList<Amostra> baseTreino = new ArrayList<>();
    private static ArrayList<Amostra> baseTesteUm = new ArrayList<>();
    private static ArrayList<Amostra> baseTesteZero = new ArrayList<>();

    private static double[][] mediaTreino = new double[2][4];
    private static double[][] mediaTeste = new double[2][4];

    private static double[][] DesvioTreino = new double[2][4];
    private static double[][] DesvioTeste = new double[2][4];

    private static double[] quantUmTreino = new double[4];
    private static double[] quantZeroTreino = new double[4];
    private static double[] quantUmTeste = new double[4];
    private static double[] quantZeroTeste = new double[4];

    public static void main(String[] args) {
        base = ReaderWriter.aux.readWindow();

        for (int i = 0; i < base.length; i++) {
            // Verifica Y
            if (base[i].Y[0] == 1) {
                baseUm.add(new Amostra(base[i].X, base[i].Y));
            } else if (base[i].Y[0] == 0) {
                baseZero.add(new Amostra(base[i].X, base[i].Y));
            }
            baseTreino.add(new Amostra(base[i].X, base[i].Y));

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
        baseTreinoUm.addAll(baseUm.subList(0, (int) (0.7 * baseUm.size())));
        baseTreinoZero.addAll(baseZero.subList(0, (int) (0.7 * baseZero.size())));

        baseTesteUm.addAll(baseUm.subList((int) (0.7 * baseUm.size()), baseUm.size()));
        baseTesteZero.addAll(baseZero.subList((int) (0.7 * baseZero.size()), baseZero.size()));

        // ====================================
        // For de Treino
        // ====================================
        // Base de treino 70% da base de cada classe
        // Não Faz ajuste dos pesos e nem calculo dos deltas

        /*
            * ====================================================
            *  Calculo de Medias
            * ====================================================
         */
        double somaUm = 0, somaZero = 0;
        //Classe 1
        for (int i = 0; i < 4; i++) {
            for (int a = 0; a < baseTreinoZero.size(); a++) {
                somaZero += baseTreinoZero.get(i).X[i];
            }
            mediaTreino[0][i] = somaZero / baseTreinoZero.size();
        }

        //Classe 2
        for (int i = 0; i < 4; i++) {
            for (int a = 0; a < baseTreinoUm.size(); a++) {
                somaUm += baseTreinoUm.get(i).X[i];
            }
            mediaTreino[1][i] = somaUm / baseTreinoUm.size();
        }

        /*
            * ====================================================
            *  Desvio Padrao
            * ====================================================
         */

        double somatorioUm, somatorioZero;

        for (int j = 0; j < 4; j++) { 
            somatorioZero = 0;
            
            //Classe 1
            for (int i = 0; i < baseTreinoZero.size(); i++) {
                somatorioZero += (baseTreinoZero.get(i).X[j] - mediaTreino[0][j]);
            }
            DesvioTreino[0][j] = Math.sqrt((Math.pow(somatorioZero, 2)/4));
            
        }

        //Classe 2
        for (int j = 0; j < 4; j++) {
            somatorioUm = 0;
            for (int i = 0; i < baseTreinoUm.size(); i++) {
                somatorioUm += (baseTreinoUm.get(i).X[j] - mediaTreino[1][j]);
            }

            DesvioTreino[1][j] = Math.sqrt((Math.pow(somatorioUm, 2)/4));

        }

        double[] resultadosTreinoc1 = new double[baseTreino.size()];
        double[] resultadosTreinoc2 = new double[baseTreino.size()];
        double[] resultadosTreino = new double[baseTreino.size()];
        /*
         * =======================================================
         *              Gaussiano + Produtorio P(x/C1)
         * =======================================================
         */
        double result1 = 1;
        double result2 = 1;
        double aux, r;
        int quantC1 = baseTreinoZero.size(); //0
        int quantC2 = baseTreinoUm.size(); //0

        //Classe 1
        for (int i = 0; i < baseTreino.size(); i++) {
            
         result1 = 1;
            //classe 1
            for (int j = 0; j < 4; j++) {
                aux = -(Math.pow(baseTreino.get(i).X[j] - mediaTreino[0][j], 2) / (2 * Math.pow(DesvioTreino[0][j], 2)));
                r = 2 * Math.PI * DesvioTreino[0][j];
                result1 = result1 * Math.exp(aux) / Math.sqrt(r);
            }
            result1 = result1 * (quantC1/(quantC1+quantC2));
            resultadosTreinoc1[i] = result1; 
        }
            
        for (int i = 0; i < baseTreino.size(); i++) {
         result2 = 1;
            //classe 1
            for (int j = 0; j < 4; j++) {
                aux = -(Math.pow(baseTreino.get(i).X[j] - mediaTreino[1][j], 2) / (2 * Math.pow(DesvioTreino[1][j], 2)));
                r = 2 * Math.PI * DesvioTreino[1][j];
                result2 = result2 * Math.exp(aux) / Math.sqrt(r);
            }
            result2 = result2 * (quantC2/(quantC1+quantC2));
            resultadosTreinoc2[i] = result2;

        }

        for (int i = 0; i < baseTreino.size(); i++) {
            if(resultadosTreinoc1[i] > resultadosTreinoc2[i]){
                resultadosTreino[i] = 0;
            }else{
                resultadosTreino[i] = 1;
            }
            System.out.println(resultadosTreino[i]);
        }
        // ========================================================
        // For de Teste
        // ========================================================
    }

}
