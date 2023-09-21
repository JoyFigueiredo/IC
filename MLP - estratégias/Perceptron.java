import java.util.Random;

public class Perceptron extends RNA{

    private int qtdIn, qtdOut;
    private double[][] W; //pesos
    private double ni;

    public Perceptron(int qtdIn, int qtdOut, double ni){
        this.qtdIn = qtdIn;
        this.qtdOut = qtdOut;
        this.W = new double[qtdIn+1][qtdOut];
        this.ni = ni;
        Random rand = new Random();

        // Gerar aletorio os primeiros
        for(int j=0; j < qtdIn;j++){
            for(int i=0; i <qtdOut;i++){
                W[j][i] = Math.random() * 0.6 - 0.3;
            }
        }

    }

    public double[] treinar(double[] Xin, double[] Y){
        double[] X = new double[Xin.length+1];
       // System.arraycopy(Xin, 0, X, 0, Xin.length);
       

        //concat 
        
        for(int i=0; i<Xin.length;i++){
            X[i] = Xin[i];
        }
        X[Xin.length] = 1;

        double[] teta = new double[Y.length];

        for(int j=0; j< Y.length;j++){
            double u = 0;
            for(int i=0; i<X.length;i++){
                u += X[i]*W[i][j];
            }

            teta[j] = 1/(1+Math.exp(-u)); 
        }

        for(int j=0; j < Y.length;j++){
            for(int i=0; i < X.length;i++){
                this.W[i][j]+= ni*(Y[j]-teta[j])*X[i];
            }
        }

        return teta;
    }

    @Override
    public double[] executar(double[] Xin) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'executar'");
    }
}