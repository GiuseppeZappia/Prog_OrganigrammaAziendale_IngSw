package command;

import composite.OrganigrammaElement;
import composite.utilities.Dipendente;
import composite.utilities.Ruolo;
import gui.PannelloDisegno;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class VisualizzaPersonaleTotaleCommand implements Command {
    private PannelloDisegno pd;

    public VisualizzaPersonaleTotaleCommand(PannelloDisegno pd) {
        this.pd=pd;
    }

    @Override
    public boolean doIt() throws IOException {
        Frame framePrincipale= (Frame) SwingUtilities.getWindowAncestor(pd);
        JDialog finestra=new JDialog(framePrincipale,"Elenco Personale Azienda",true);//mettendo true so che nel frattempo utente non puo usare resto dell'app ma in caso deve chiudere dialogo
        Collection<String> personaleElem=trovaPersonale();
        String[] personaleToStringArray=personaleElem.toArray(new String[personaleElem.size()]);
        JList<String> listaPersonale=new JList<>(personaleToStringArray);
        JScrollPane jScrollPane=new JScrollPane(listaPersonale);
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

    private Collection<String> trovaPersonale() {
        Collection<String> ret=new ArrayList<>();
        for(OrganigrammaElement elem:pd.getUnitaDisegnate()){
            for(Map.Entry entry:elem.getPersonale().entrySet() ) {
                Dipendente dipendente=(Dipendente) entry.getKey();
                String nomeDipendent=dipendente.getNome();
                Ruolo ruolo=(Ruolo) entry.getValue();
                String nomeRuolo=ruolo.getNome();
                String daInserire=nomeDipendent+" "+nomeRuolo;
                if(!ret.contains(daInserire)){
                    ret.add(daInserire);
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
