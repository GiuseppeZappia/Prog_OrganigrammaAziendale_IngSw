package gui;

import command.*;
import composite.OrganigrammaElement;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.*;

public class MenuAlto extends JMenuBar{
    private CommandHandler cmdHandler=new HistoryCommandHandler();;

    public MenuAlto(JFileChooser fileChooser,PannelloDisegno pd,JFrame frame){
        JMenu menuFile=new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);//MNEMONIC COSI CON ALT+F UTENTE PUO APRIRE, MIGLIORE UX
        JMenuItem nuovo=new JMenuItem("Nuovo",new ImageIcon("./src/resources/icons8-aggiungi-file-15.png"));
        nuovo.setMnemonic(KeyEvent.VK_N);
        JMenuItem carica=new JMenuItem("Carica",new ImageIcon("./src/resources/icons8-caricare-12.png"));

        carica.addActionListener(e->{
            if(fileChooser.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION){
                cmdHandler.handleCommand(new CaricaOrganigrammaCommand(fileChooser.getSelectedFile(),pd));
            };

        });
        carica.setMnemonic(KeyEvent.VK_C);
        JMenuItem salva=new JMenuItem("Salva",new ImageIcon("./src/resources/icons8-salva-12.png"));
        salva.addActionListener(e->{
            if(fileChooser.showSaveDialog(frame)==JFileChooser.APPROVE_OPTION){
                cmdHandler.handleCommand(new SalvaOrganigrammaCommand(fileChooser.getSelectedFile(),pd));
            };
        });
        salva.setMnemonic(KeyEvent.VK_S);

        menuFile.add(nuovo);
        menuFile.add(carica);
        menuFile.add(salva);

        JMenu visualizza=new JMenu("Visualizza");
        visualizza.setMnemonic(KeyEvent.VK_V);
        JMenuItem visualDipendenti=new JMenuItem("Visualizza dipendenti");
        visualDipendenti.setMnemonic(KeyEvent.VK_D);
        visualDipendenti.addActionListener(e->{
            cmdHandler.handleCommand(new VisualizzaDipendentiTotaliCommand(pd));
        });
        JMenuItem visualizzaTuttiRuoli=new JMenuItem("Visualizza ruoli");
        visualizzaTuttiRuoli.setMnemonic(KeyEvent.VK_R);
        visualizzaTuttiRuoli.addActionListener(e->{
            cmdHandler.handleCommand(new VisualizzaRuoliTotaliCommand(pd));

        });
        visualizza.add(visualDipendenti);
        visualizza.add(visualizzaTuttiRuoli);

        add(menuFile);
        add(visualizza);
    }

}


