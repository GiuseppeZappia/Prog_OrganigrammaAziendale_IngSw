package command;

import composite.OrganigrammaElement;
import composite.utilities.Dipendente;
import gui.PannelloDisegno;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class VisualizzaDipendentiTotaliCommand implements Command{
    private PannelloDisegno pd;


    public VisualizzaDipendentiTotaliCommand(PannelloDisegno pd ){
        this.pd = pd;
    }

    @Override
    public boolean doIt() {
        Frame framePrincipale= (Frame) SwingUtilities.getWindowAncestor(pd);
        JDialog finestra=new JDialog(framePrincipale,"Elenco Dipendenti Azienda",true);//mettendo true so che nel frattempo utente non puo usare resto dell'app ma in caso deve chiudere dialogo
        Collection<Dipendente> dipendentiElem=trovaDipendenti();
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

    private Collection<Dipendente> trovaDipendenti() {
        Collection<Dipendente> ret=new ArrayList<>();
        for(OrganigrammaElement elem:pd.getUnitaDisegnate()){
            ret.addAll(elem.getDipendenti());
        }
        return ret;
    }

    @Override
    public boolean undoIt() {
        return false;
    }
}
