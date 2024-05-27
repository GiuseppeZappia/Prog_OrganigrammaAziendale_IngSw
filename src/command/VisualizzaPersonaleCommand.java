package command;

import composite.OrganigrammaElement;
import composite.utilities.Dipendente;
import composite.utilities.Ruolo;
import gui.PannelloDisegno;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class VisualizzaPersonaleCommand implements Command {
    private final OrganigrammaElement elem;
    private final PannelloDisegno pd;

    public VisualizzaPersonaleCommand(PannelloDisegno pd, OrganigrammaElement elem) {
        this.elem = elem;
        this.pd = pd;
    }

    //PER QUESTI METODI MOLTO SIMILI TRA LORO POTREI METTERE METODO DEFAUL?? OPPURE NO PERCHE SAREBBE PUBBLICO?
    //MAGARI FAI CLASSE ASTRATTA DOVE METTI PROTECTED/PACKAGE QUESTI METODI

    @Override
    public boolean doIt() {
        Frame framePrincipale= (Frame) SwingUtilities.getWindowAncestor(pd);
        JDialog finestra=new JDialog(framePrincipale,"Elenco Personale",true);//mettendo true so che nel frattempo utente non puo usare resto dell'app ma in caso deve chiudere dialogo
        Map<Dipendente,Ruolo> mappa=elem.getPersonale();
        String[] personaleArray=creaArrayPersonale(mappa);
        JList<String> personale=new JList<>(personaleArray);
        JScrollPane jScrollPane=new JScrollPane(personale);
        finestra.add(jScrollPane,BorderLayout.CENTER);
        JButton chiudi=new JButton("Chiudi");
        chiudi.addActionListener(e->{
            finestra.dispose();
        });
        finestra.add(chiudi,BorderLayout.SOUTH);
        finestra.pack();
        finestra.setVisible(true);
        return false;
    }

    private String[] creaArrayPersonale(Map<Dipendente, Ruolo> mappa) {
        String[] personaleArray=new String[mappa.size()];
        int i = 0;
        for (Map.Entry<Dipendente, Ruolo> entry : mappa.entrySet()) {
            personaleArray[i++]="<"+entry.getKey().getNome()+" "+entry.getKey().getCognome()+","+entry.getValue().getNome()+">";
        }
        return personaleArray;
    }

    @Override
    public boolean undoIt() {
        return false;
    }
}
