package GUI;

import javax.swing.*;
import java.awt.*;
import Composite.OrganoGestione;
import Mediator.ChangeManagerMediator;
import Mediator.ConcreteChangheManagerMediator;

public class FramePrincipale extends JFrame {
    private PannelloDisegno pannelloDisegno;//SEPARO IN QUESTO MODO I VARI COMPONENTI FAVORENDO RIUSABILITA ED EVOLVIBILITA'
    private PanelBottoni panelBottoni;//SEPARO IN QUESTO MODO I VARI COMPONENTI FAVORENDO RIUSABILITA ED EVOLVIBILITA'


    public FramePrincipale() {
            setTitle("Organigramma Maker");
            setSize(800,800);//dimens
            setLocationRelativeTo(null);//spawna al centro
            setLayout(new BorderLayout());//metto quel layout della doc carino
            pannelloDisegno=new PannelloDisegno();
            add(new JScrollPane(pannelloDisegno),BorderLayout.CENTER);//lo metto al centro cosi lì disegno organigramma, inoltre lo decoro con JScrollPane cosi ho barre laterali
            panelBottoni=new PanelBottoni(pannelloDisegno); //PANEL CON BOTTONI CREATO
            add(new JScrollPane(panelBottoni),BorderLayout.SOUTH);//metto il layout con i bottoni in basso nel layout principale del mio frame DECORO anche questo cosi in caso puo fare scroll se diminuisce dimensioni per vedere tutti bottoni
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //chiusura del programma quando chiudo gui
            setVisible(true); //meglio lasciarla alla fine cosi non ho problemi con le altre info

    }
}
