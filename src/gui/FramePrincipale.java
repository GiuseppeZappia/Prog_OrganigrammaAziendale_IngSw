package gui;

import javax.swing.*;
import java.awt.*;

public class FramePrincipale extends JFrame {
    private PannelloDisegno pannelloDisegno;//SEPARO IN QUESTO MODO I VARI COMPONENTI FAVORENDO RIUSABILITA ED EVOLVIBILITA'
    private PanelBottoni panelBottoni;//SEPARO IN QUESTO MODO I VARI COMPONENTI FAVORENDO RIUSABILITA ED EVOLVIBILITA'
    private JFileChooser fileChooser;
    private MenuAlto menuAlto;

    public FramePrincipale() {
            setTitle("Organigramma Maker");
//            setExtendedState(JFrame.MAXIMIZED_BOTH);//dimens
            setSize(800, 600);
            setLocationRelativeTo(null);//spawna al centro
            setLayout(new BorderLayout());//metto quel layout della doc carino
            fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileFilterOrganigramma());//COSI POSSO USARE FILE CON MIA ESTENSIONE SOLO
            fileChooser.setAcceptAllFileFilterUsed(false);//cosi non accetta anche tutti i file nel menu estensioni ma solo il mio

            pannelloDisegno=new PannelloDisegno();
            JScrollPane barreScorr=new JScrollPane(pannelloDisegno);
            add(barreScorr,BorderLayout.CENTER);//lo metto al centro cosi lì disegno organigramma, inoltre lo decoro con JScrollPane cosi ho barre laterali
            panelBottoni=new PanelBottoni(pannelloDisegno); //PANEL CON BOTTONI CREATO
            add(new JScrollPane(panelBottoni),BorderLayout.SOUTH);//metto il layout con i bottoni in basso nel layout principale del mio frame DECORO anche questo cosi in caso puo fare scroll se diminuisce dimensioni per vedere tutti bottoni
            menuAlto=new MenuAlto(fileChooser,pannelloDisegno,this);
            setJMenuBar(menuAlto);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //chiusura del programma quando chiudo gui
            setVisible(true); //meglio lasciarla alla fine cosi non ho problemi con le altre info
    }
}
