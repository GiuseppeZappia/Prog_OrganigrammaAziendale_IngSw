package command;

import composite.OrganigrammaElement;
import composite.utilities.Ruolo;
import gui.PannelloDisegno;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class VisualizzaRuoliTotaliCommand implements Command {
    private PannelloDisegno pd;

    public VisualizzaRuoliTotaliCommand(PannelloDisegno pd) {
        this.pd=pd;
    }

    @Override
    public boolean doIt() {
        Frame framePrincipale= (Frame) SwingUtilities.getWindowAncestor(pd);
        JDialog finestra=new JDialog(framePrincipale,"Elenco Ruoli Azienda",true);//mettendo true so che nel frattempo utente non puo usare resto dell'app ma in caso deve chiudere dialogo
        Collection<Ruolo> ruoliElem=trovaRuoli();
        String[] ruoliToStringArray=ruoliElem.stream().map(Ruolo::toString).toArray(String[]::new);
        JList<String> listaRuoli=new JList<>(ruoliToStringArray);
        JScrollPane jScrollPane=new JScrollPane(listaRuoli);
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

    private Collection<Ruolo> trovaRuoli() {
        Collection<Ruolo> ret=new ArrayList<>();
        for(OrganigrammaElement elem:pd.getUnitaDisegnate()){
            for(Ruolo ruolo: elem.getRuoliDisponibili()){
            if(!ret.contains(ruolo)){
                ret.add(ruolo);
                }
            }
        }
        return ret;
    }

    @Override
    public boolean undoIt() {
        return false;
    }
}
