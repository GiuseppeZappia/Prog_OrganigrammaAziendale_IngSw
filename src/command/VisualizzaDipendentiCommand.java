package command;

import composite.OrganigrammaElement;
import composite.utilities.Dipendente;
import composite.utilities.Ruolo;
import gui.PannelloDisegno;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class VisualizzaDipendentiCommand implements Command{
    private PannelloDisegno pd;
    private OrganigrammaElement elem;

    public VisualizzaDipendentiCommand(PannelloDisegno pd, OrganigrammaElement elem){
        this.pd = pd;
        this.elem = elem;
    }

    @Override
    public boolean doIt() {
        Frame framePrincipale= (Frame) SwingUtilities.getWindowAncestor(pd);
        JDialog finestra=new JDialog(framePrincipale,"Elenco Dipendenti",true);//mettendo true so che nel frattempo utente non puo usare resto dell'app ma in caso deve chiudere dialogo
//        finestra.setLocationRelativeTo(pd);
        Collection<Dipendente> dipendentiElem=elem.getDipendenti();
        String[] dipendentiToStringArray=dipendentiElem.stream().map(Dipendente::toString).toArray(String[]::new);
        JList<String> listaDipendenti=new JList<>(dipendentiToStringArray);
        JScrollPane jScrollPane=new JScrollPane(listaDipendenti);
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
    public boolean undoIt() {
        return false;
    }
}
