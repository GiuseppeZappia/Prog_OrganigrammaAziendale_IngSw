package gui;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class MenuAlto extends JMenuBar{

    public MenuAlto(JFileChooser fileChooser,JFrame frame){
        JMenu menuFile=new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);//MNEMONIC COSI CON ALT+F UTENTE PUO APRIRE, MIGLIORE UX
        JMenuItem nuovo=new JMenuItem("Nuovo",new ImageIcon("./src/images/icons8-aggiungi-file-15.png"));
        nuovo.setMnemonic(KeyEvent.VK_N);
        JMenuItem carica=new JMenuItem("Carica",new ImageIcon("./src/images/icons8-caricare-12.png"));

        carica.addActionListener(e->{
            fileChooser.showOpenDialog(frame);

        });
        carica.setMnemonic(KeyEvent.VK_C);
        JMenuItem salva=new JMenuItem("Salva",new ImageIcon("./src/images/icons8-salva-12.png"));
        salva.addActionListener(e->{
            fileChooser.showSaveDialog(frame);
        });
        salva.setMnemonic(KeyEvent.VK_S);



        menuFile.add(nuovo);
        menuFile.add(carica);
        menuFile.add(salva);

        JMenu visualizza=new JMenu("Visualiza");
        visualizza.setMnemonic(KeyEvent.VK_V);
        JMenuItem visualDipendenti=new JMenuItem("Visualizza dipendenti");
        visualDipendenti.setMnemonic(KeyEvent.VK_D);
        JMenuItem visualizzaTuttiRuoli=new JMenuItem("Visualizza ruoli");
        visualizzaTuttiRuoli.setMnemonic(KeyEvent.VK_R);

        visualizza.add(visualDipendenti);
        visualizza.add(visualizzaTuttiRuoli);

        add(menuFile);
        add(visualizza);
    }

}
