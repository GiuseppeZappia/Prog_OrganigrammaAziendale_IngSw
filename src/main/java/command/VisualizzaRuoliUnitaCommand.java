package command;

import composite.OrganigrammaElement;
import composite.utilities.Ruolo;
import gui.PannelloDisegno;
import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class VisualizzaRuoliUnitaCommand implements Command{
    private OrganigrammaElement elem;
    private PannelloDisegno pd;

    public VisualizzaRuoliUnitaCommand(OrganigrammaElement elem, PannelloDisegno pd) {
        this.elem = elem;
        this.pd = pd;
    }

    @Override
    public boolean doIt(){
        Frame framePrincipale= (Frame) SwingUtilities.getWindowAncestor(pd);
        JDialog finestra=new JDialog(framePrincipale,"Elenco Ruoli",true);//mettendo true so che nel frattempo utente non puo usare resto dell'app ma in caso deve chiudere dialogo
        Collection<Ruolo> ruoliElem=elem.getRuoliDisponibili();
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

    @Override
    public boolean undoIt(){
        return false;
    }
}
