package gui;

import javax.swing.*;
import java.awt.*;

public class FramePrincipale extends JFrame {
    //SEPARO IN QUESTO MODO I VARI COMPONENTI FAVORENDO RIUSABILITA ED EVOLVIBILITA'
    private PannelloDisegno pannelloDisegno;
    private PanelBottoni panelBottoni;
    private JFileChooser fileChooser;
    private MenuAlto menuAlto;

    public FramePrincipale() {
            //opzioni finestra aperta
            setTitle("Organigramma Maker");
            setExtendedState(JFrame.MAXIMIZED_BOTH);//dimens
            setMinimumSize(new Dimension(800, 600));
            setLocationRelativeTo(null);//spawna al centro
            setLayout(new BorderLayout());//metto layout border

            //file
            fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileFilterOrganigramma());//COSI POSSO USARE FILE CON MIA ESTENSIONE SOLO
            fileChooser.setAcceptAllFileFilterUsed(false);//cosi non accetta anche tutti i file nel menu estensioni ma solo il mio

            //pannellodisegno
            pannelloDisegno=new PannelloDisegno();
            JScrollPane barreScorr=new JScrollPane(pannelloDisegno);
            add(barreScorr,BorderLayout.CENTER);//lo metto al centro cosi lì disegno organigramma, inoltre lo decoro con JScrollPane cosi ho barre laterali

            //bottoni
            panelBottoni=new PanelBottoni(pannelloDisegno); //PANEL CON BOTTONI CREATO
            add(new JScrollPane(panelBottoni),BorderLayout.SOUTH);//metto il layout con i bottoni in basso nel layout principale del mio frame
            //DECORO anche questo cosi in caso puo fare scroll se diminuisce dimensioni per vedere tutti bottoni(ANCHE SE NON SERVIREBBE VISTO CHE HO MESSO DIMENSIONE
            //MINIMA DELLA FINESTRA OLTRE LA QUALE NON PUO DIMINUIRE

            //menu in alto con opzioni
            menuAlto=new MenuAlto(fileChooser,pannelloDisegno,this);
            setJMenuBar(menuAlto);

            //operazioni di chiusura e visibilita
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //chiusura del programma quando chiudo gui
            setVisible(true); //meglio lasciarla alla fine cosi non ho problemi con le altre info
    }
}
