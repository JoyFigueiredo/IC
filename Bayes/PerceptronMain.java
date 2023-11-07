
import java.io.File;
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
    private static ArrayList<Amostra> baseTeste = new ArrayList<>();
    private static ArrayList<Amostra> baseTesteUm = new ArrayList<>();
    private static ArrayList<Amostra> baseTesteZero = new ArrayList<>();

    private static double[][] mediaTreino = new double[2][4];
    private static double[][] DesvioTreino = new double[2][4];
    private static double[][] mediaTeste = new double[2][4];
    private static double[][] DesvioTeste = new double[2][4];

    public static void main(String[] args) {
        base = ReaderWriter.aux.readWindow();

        for (int i = 0; i < base.length; i++) {
            // Verifica Y
            if (base[i].Y[0] == 1) {
                baseUm.add(new Amostra(base[i].X, base[i].Y));
            } else if (base[i].Y[0] == 0) {
                baseZero.add(new Amostra(base[i].X, base[i].Y));
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
        baseTreinoUm.addAll(baseUm.subList(0, (int) (0.7 * baseUm.size())));
        baseTreinoZero.addAll(baseZero.subList(0, (int) (0.7 * baseZero.size())));

        baseTesteUm.addAll(baseUm.subList((int) (0.7 * baseUm.size()), baseUm.size()));
        baseTesteZero.addAll(baseZero.subList((int) (0.7 * baseZero.size()), baseZero.size()));
        
        baseTeste.addAll(baseTesteUm);
        baseTeste.addAll(baseTesteZero);

         baseTreino.addAll(baseTreinoUm);
         baseTreino.addAll(baseTreinoZero);


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

        //Classe 1
        for (int j = 0; j < 4; j++) { 
            somatorioZero = 0;
            for (int i = 0; i < baseTreinoZero.size(); i++) {
                somatorioZero += Math.pow((baseTreinoZero.get(i).X[j] - mediaTreino[0][j]),2); //Somatorio(Xij - media0j)
            }
            DesvioTreino[0][j] = Math.sqrt(somatorioZero/4);
            
        }

        //Classe 2
        for (int j = 0; j < 4; j++) {
            somatorioUm = 0;
            for (int i = 0; i < baseTreinoUm.size(); i++) {
                somatorioUm += Math.pow((baseTreinoUm.get(i).X[j] - mediaTreino[1][j]),2); //Somatorio(Xij - media0j)
            }
            DesvioTreino[1][j] = Math.sqrt(somatorioUm/4);
            
        }

        ArrayList<Double> resultadosTreinoc1 = new ArrayList<>();
        ArrayList<Double> resultadosTreinoc2 = new ArrayList<>();
        ArrayList<Double> resultadosTreino = new ArrayList<>();
        /*
         * =======================================================
         *              Gaussiano + Produtorio P(x/C1)
         * =======================================================
         */
        double result1 = 0;
        double result2 = 0;
        double aux, r;
        double quantC1 = baseTreinoZero.size(); //0
        double quantC2 = baseTreinoUm.size(); //0

        //Classe 1
        for (int i = 0; i < baseTreino.size(); i++) {
            for (int j = 0; j < 4; j++) {
                aux = -(Math.pow(baseTreino.get(i).X[j] - mediaTreino[0][j], 2) / (2 * Math.pow(DesvioTreino[0][j], 2)));
                r = 2 * Math.PI * DesvioTreino[0][j];
                if(j==0){
                    result1 = Math.exp(aux) / Math.sqrt(r);
                }else{
                    result1 = result1 * Math.exp(aux) / Math.sqrt(r);
                }
            }
            result1 = result1 * (quantC1/(quantC1+quantC2));
            resultadosTreinoc1.add(result1); 
           // System.out.println(resultadosTreinoc1[i]);
        }
        
        //Classe 2
        for (int i = 0; i < baseTreino.size(); i++) {
            for (int j = 0; j < 4; j++) {
                aux = -(Math.pow(baseTreino.get(i).X[j] - mediaTreino[1][j], 2) / (2 * Math.pow(DesvioTreino[1][j], 2)));
                r = 2 * Math.PI * DesvioTreino[1][j];
                if(j==0){
                    result2 = Math.exp(aux) / Math.sqrt(r);
                }else{
                    result2 = result2 * Math.exp(aux) / Math.sqrt(r);
                }
            }
            result2 = result2 * (quantC1/(quantC1+quantC2));
            resultadosTreinoc2.add(result2); 
            //System.out.println(resultadosTreinoc2.get(i));

        }

        int somaErro=0;
        int quantAmostraTreino = baseTreino.size();
        for (int i = 0; i < baseTreino.size(); i++) {
            //if(resultadosTreinoc1.get(i) > resultadosTreinoc2.get(i) && baseTreino.get(i).Y[0] != 0){
            //    resultadosTreino.add(0.0);
            //}else{
            //    resultadosTreino.add(1.0);
            //    somaErro++;
            //}

            if(resultadosTreinoc1.get(i) > resultadosTreinoc2.get(i) && baseTreino.get(i).Y[0] != 0){
                somaErro++;
            }
            if(resultadosTreinoc2.get(i) > resultadosTreinoc1.get(i) && baseTreino.get(i).Y[0] != 1){
                somaErro++;
            }

            //System.out.println(resultadosTreino.get(i));
        }
        System.out.println("Quantidade de amostras de treino: " + quantAmostraTreino + " -- Quantidade de Erros:  "+ somaErro);
       
       
        // ========================================================
        // For de Teste
        // ========================================================
        
        /*
            * ====================================================
            *  Calculo de Medias
            * ====================================================
         */
        somaUm = 0;
        somaZero = 0;
        //Classe 1
        for (int i = 0; i < 4; i++) {
            for (int a = 0; a < baseTesteZero.size(); a++) {
                somaZero += baseTesteZero.get(i).X[i];
            }
            mediaTeste[0][i] = somaZero / baseTesteZero.size();
        }

        //Classe 2
        for (int i = 0; i < 4; i++) {
            for (int a = 0; a < baseTesteUm.size(); a++) {
                somaUm += baseTesteUm.get(i).X[i];
            }
            mediaTeste[1][i] = somaUm / baseTesteUm.size();
        }

        /*
            * ====================================================
            *  Desvio Padrao
            * ====================================================
         */

        somatorioUm = 0;
        somatorioZero = 0;

        //Classe 1
        for (int j = 0; j < 4; j++) { 
            somatorioZero = 0;
            for (int i = 0; i < baseTesteZero.size(); i++) {
                somatorioZero += Math.pow((baseTesteZero.get(i).X[j] - mediaTeste[0][j]),2); //Somatorio(Xij - media0j)
            }
            DesvioTeste[0][j] = Math.sqrt(somatorioZero/4);
            
        }

        //Classe 2
        for (int j = 0; j < 4; j++) {
            somatorioUm = 0;
            for (int i = 0; i < baseTesteUm.size(); i++) {
                somatorioUm += Math.pow((baseTesteUm.get(i).X[j] - mediaTeste[1][j]),2); //Somatorio(Xij - media0j)
            }
            DesvioTeste[1][j] = Math.sqrt(somatorioUm/4);
            
        }

        ArrayList<Double> resultadosTestec1 = new ArrayList<>();
        ArrayList<Double> resultadosTestec2 = new ArrayList<>();
        ArrayList<Double> resultadosTeste = new ArrayList<>();
        /*
         * =======================================================
         *              Gaussiano + Produtorio P(x/C1)
         * =======================================================
         */
        result1 = 0;
        result2 = 0;
        double quantC1t = baseTesteZero.size(); //0
        double quantC2t = baseTesteUm.size(); //0

        //Classe 1
        for (int i = 0; i < baseTeste.size(); i++) {
            for (int j = 0; j < 4; j++) {
                aux = -(Math.pow(baseTeste.get(i).X[j] - mediaTeste[0][j], 2) / (2 * Math.pow(DesvioTeste[0][j], 2)));
                r = 2 * Math.PI * DesvioTeste[0][j];
                if(j==0){
                    result1 = Math.exp(aux) / Math.sqrt(r);
                }else{
                    result1 = result1 * Math.exp(aux) / Math.sqrt(r);
                }
            }
            result1 = result1 * (quantC1t/ (quantC1t+quantC2t));
            resultadosTestec1.add(result1); 
           // System.out.println(resultadosTreinoc1[i]);
        }
        
        //Classe 2
        for (int i = 0; i < baseTeste.size(); i++) {
            for (int j = 0; j < 4; j++) {
                aux = -(Math.pow(baseTeste.get(i).X[j] - mediaTeste[1][j], 2) / (2 * Math.pow(DesvioTeste[1][j], 2)));
                r = 2 * Math.PI * DesvioTeste[1][j];
                if(j==0){
                    result2 = Math.exp(aux) / Math.sqrt(r);
                }else{
                    result2 = result2 * Math.exp(aux) / Math.sqrt(r);
                }
            }
            result2 = result2 * (quantC1t/(quantC1t + quantC2t));
            resultadosTestec2.add(result2); 
            //System.out.println(resultadosTreinoc2.get(i));

        }

        somaErro=0;
        int quantAmostraTeste = baseTeste.size();
        for (int i = 0; i < baseTeste.size(); i++) {
            if(resultadosTestec1.get(i) > resultadosTestec2.get(i)){
                resultadosTeste.add(0.0);
            }else{
                resultadosTeste.add(1.0);
                somaErro++;
            }
            //System.out.println(resultadosTreino.get(i));
        }
        System.out.println("Quantidade de amostras de teste: " + quantAmostraTeste + " -- Quantidade de Erros:  "+ somaErro);
    }

}
