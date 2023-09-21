import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ReaderWriter {
        public static ReaderWriter aux = new ReaderWriter();
    

    private CelulaNew files = new CelulaNew();

    /**
     *
     * @return Retorna a lista de de arquvios presente na raiz
     */
    public String getFiles() {
        //Busca na raiz do projeto
        File file = new File("./");
        StringBuilder string = new StringBuilder();
        int i = 3;
        this.files.next = null;
        CelulaNew files = this.files;
        for (File li : file.listFiles()) {
            String url = li.getName();
            //Verifica somente os arquivos que possuem pgm
            if (url.contains(".pgm")) {
                files.next = new CelulaNew(li);
                files = files.next;
                string.append(String.format("%02d- %s\n", i++, url));
            }
        }
        return string.toString();
    }

    /**
     *
     * @param i = indica o arquivo desejado
     * @return o arquivo se exitir e nulo se não existir
     */
    public Amostra[] read(int i) {
        CelulaNew files = this.files.next;
        for (int j = 0; j < i; j++) {
            files = files.next;
            if (files == null) {
                return null;
            }
        }
        return read(files.file);
    }

    /**
     *
     * @return pede o arquivo por janela
     */
    public Amostra[] readWindow() {
        JFileChooser escolhedor = new JFileChooser();

        escolhedor.setFileFilter(new FileNameExtensionFilter("Somente diretórios e .data", "data"));

        JFrame jf = new JFrame();
        jf.setVisible(true);
        int opcaoEscolhida = escolhedor.showOpenDialog(jf); //Janela para abrir um arquivo
        jf.dispose();
        if (opcaoEscolhida == JFileChooser.APPROVE_OPTION) {
            return read(escolhedor.getSelectedFile());
        } else {
            escolhedor.cancelSelection();
            return null;
        }
    }

    /**
     *
     * @param url - Recebe por console o url de onde vai gravar
     * @return o arquivo se existir ou nulo se não existir.
     */
    public Amostra[] readConsole(String url) {
        if (!url.contains(".pgm")) {
            url = url.concat(".pgm");
        }
        File file = new File(url);
        if (file.canRead()) {
            return read(file);
        }
        return null;
    }

    protected Amostra[] read(File file) {
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            //Discarta o p2 que é padrão para todo pgm lido nesse formato do problema
            
            //Linha Coluna Saida
            Amostra[] amostra = new Amostra[Amostra.amostra];
            
            //Preeche a matriz
            for (int i = 0; i < Amostra.amostra; i++) {
                amostra[i] = new Amostra();
                String linha = br.readLine();
                String[] particao = linha.replaceAll(" ", "").split(",");
                for (int j = 0; j < Amostra.qtdIn; j++) {
                    amostra[i].X[j] =  Double.parseDouble(particao[j]);
                }
                for (int j = 0; j < Amostra.qtdOut; j++) {
                    amostra[i].Y[j] =  Double.parseDouble(particao[j + Amostra.qtdIn]);
                }
            }
            fr.close();
            br.close();
            return amostra;//return mc;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean exist(String url) {
        File file = new File(url);
        return file.exists();
    }

    private class CelulaNew {

        File file;
        CelulaNew next;

        public CelulaNew() {
        }

        public CelulaNew(File file) {
            this.file = file;
        }

    }
}
