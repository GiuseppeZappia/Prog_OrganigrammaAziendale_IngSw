package GUI;

import Command.*;
import Composite.OrganoGestione;
import Mediator.ChangeManagerMediator;
import Mediator.ConcreteChangheManagerMediator;
import Composite.OrganigrammaElement;
import javax.swing.*;
import java.awt.*;

public class PanelBottoni extends JPanel {
    private final JButton creaOrgano, creaUnita, creaSottoUnita, salva;
    private final CommandHandler cmdHandler = new HistoryCommandHandler();
    private final ChangeManagerMediator mediator = new ConcreteChangheManagerMediator();

    public PanelBottoni(PannelloDisegno pd) {
        setLayout(new FlowLayout(FlowLayout.LEFT));//layout che mette bottoni tutti belli in riga da sx
        creaOrgano = new JButton("Crea Organo Gestione");
        creaUnita = new JButton("Aggiungi Unita");
        creaSottoUnita = new JButton("Aggiungi Sottounità");
        salva = new JButton("Salva");
        add(creaOrgano);
        add(creaUnita);
        add(creaSottoUnita);
        add(salva);

        //LI INSERISCO COME LISTENER DI THIS COSI SO COSA FARE QUANDO VENGONO PREMUTI
        creaOrgano.addActionListener(e -> {//implemento metodo dell'interfaccia ActionListener che si chiama ActionPerformed
                    cmdHandler.handleCommand(new DisegnaElementCommand(pd,mediator));
                    creaOrgano.setEnabled(false);//DOPO AVER INSERITO UN ORGANO NON NE POSSO INSERIRE PIU' LO RIMETTO VISIBILE DOPO AVER CANCELLATO TUTTO PER CREARNE UNO NUOVO
                    //ALTERNATIVAMENTE METTO IF CHE SE C'È GIA GESTIONE NELLA LISTA LANCIA DIALOG ERRORE
                }
        );

        //BOTTONE CREA UNITA'
        creaUnita.addActionListener(e -> {
            cmdHandler.handleCommand(new AggiungiFiglioOrganoCommand(pd,mediator));
        });

        creaSottoUnita.addActionListener(e -> {
            cmdHandler.handleCommand(new AggiungiFiglioUnitaCommand(pd,mediator));
        });

    }


}
