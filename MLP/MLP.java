import java.util.Random;

public class MLP extends RNA {
    double[][] WH, WO;
    private int qtdIn, qtdOut, qntH;
    double ni;

    public MLP(int qtdIn, int qntH, int qtdOut, double ni) {
        this.qtdIn = qtdIn;
        this.qtdOut = qtdOut;
        this.qntH = qntH;
        this.ni = ni;

        this.WH = new double[qtdIn + 1][qntH];
        this.WO = new double[qntH + 1][qtdOut];

        //Random rand = new Random();

        // Gerar aletorio os primeiros
        for (int j = 0; j < qtdIn; j++) {
            for (int i = 0; i < qntH; i++) {
                WH[j][i] = Math.random() * 0.6 - 0.3;
            }
        }

        for (int j = 0; j < qntH; j++) {
            for (int i = 0; i < qtdOut; i++) {
                WO[j][i] = Math.random() * 0.6 - 0.3;
            }
        }

    }

    @Override
    public double[] treinar(double[] Xin, double[] Y) {
        //double[] saida = executar(Xin);

        double[] X = new double[Xin.length + 1];
        // System.arraycopy(Xin, 0, X, 0, Xin.length);
        // concat
        for (int i = 0; i < Xin.length; i++) {
            X[i] = Xin[i];
        }
        X[Xin.length] = 1;

        //Obter saida dos neuroneos intermediarios
        double[] H = new double[qntH+1];

        for(int h = 0;h< H.length-1; h++){
            double u = 0;
            for(int i=0; i<X.length;i++){
                u += X[i]*this.WH[i][h];
            }

            H[h] = 1/(1+Math.exp(-u)); 
        }
        H[H.length-1] = 1;

        //obtem a saida da camada
        double[] saida = new double[qtdOut];

        for(int j = 0;j < qtdOut; j++){
            double u = 0;
            for(int h=0; h<H.length;h++){
                u += H[h]*this.WO[h][j];
            }

            saida[j] = 1/(1+Math.exp(-u)); 
        }
    
        //======================================
        //         Calculo dos deltas
        //======================================
        double []DO = new double[qtdOut];

        for(int j = 0;j < DO.length; j++){
            DO[j]= saida[j]* (1-saida[j])*(Y[j]-saida[j]);
        }

        double []DH = new double[qntH];

        for(int h = 0;h< DH.length-1; h++){
            double soma = 0;
            for(int j = 0;j < saida.length; j++){
                soma += DO[j]*WO[h][j];
            }
            DH[h] = H[h]*(1-H[h])*soma;
        }

        //======================================
        //         Ajuste dos pesos
        //======================================
        for(int i = 0;i< WH.length; i++){
            for(int h = 0;h< WH[0].length; h++){
                WH[i][h] += ni * DH[h]* X[i];
            }
        }

        for(int i = 0;i< WO.length; i++){
            for(int h = 0;h< WO[0].length; h++){
                WO[i][h] += ni * DO[h]* H[i];
            }
        }

        return saida;
    }

    @Override
    public double[] executar(double[] Xin) {
        double[] X = new double[Xin.length + 1];
        // System.arraycopy(Xin, 0, X, 0, Xin.length);
        // concat
        for (int i = 0; i < Xin.length; i++) {
            X[i] = Xin[i];
        }
        X[Xin.length] = 1;

        //Obter saida dos neuroneos intermediarios
        double[] H = new double[qntH+1];

        for(int h = 0;h< H.length-1; h++){
            double u = 0;
            for(int i=0; i<X.length;i++){
                u += X[i]*this.WH[i][h];
            }

            H[h] = 1/(1+Math.exp(-u)); 
        }
        H[H.length-1] = 1;

        //obtem a saida da camada
        double[] saida = new double[qtdOut];

        for(int j = 0;j < qtdOut; j++){
            double u = 0;
            for(int h=0; h<H.length;h++){
                u += H[h]*this.WO[h][j];
            }

            saida[j] = 1/(1+Math.exp(-u)); 
        }

        return saida;
    }
}
