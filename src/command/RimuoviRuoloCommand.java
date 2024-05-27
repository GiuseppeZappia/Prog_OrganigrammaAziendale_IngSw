package command;

import composite.OrganigrammaElement;
import composite.utilities.Dipendente;
import composite.utilities.Ruolo;
import exceptions.RuoloGiaPresenteNellUnitaException;
import exceptions.RuoloNonPresenteNellUnitaException;
import exceptions.SubjectSenzaListenerInAscoltoException;
import gui.PannelloDisegno;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Map;

public class RimuoviRuoloCommand implements Command{
    private OrganigrammaElement elem;
    private PannelloDisegno pd;

    public RimuoviRuoloCommand(PannelloDisegno pd,OrganigrammaElement elem) {
        this.elem = elem;
        this.pd = pd;
    }

    @Override
    public boolean doIt() {
        JDialog finestraRuoliPrese= apriFinestraRuoliPresenti();
        finestraRuoliPrese.setVisible(true);
        return false;
    }

    private JDialog apriFinestraRuoliPresenti(){
        Frame framePrincipale= (Frame) SwingUtilities.getWindowAncestor(pd);
        JDialog finestra=new JDialog(framePrincipale,"Scelta tra ruoli presenti",true);//mettendo true so che nel frattempo utente non puo usare resto dell'app ma in caso deve chiudere dialogo
        finestra.setLocationRelativeTo(null);//al centro
        JPanel pannello=new JPanel();
        GroupLayout layout = new GroupLayout(pannello);
        pannello.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        JLabel messaggio=new JLabel("Scegli un ruolo tra quelli presenti");

        String[] nomiRuoli=listaRuoliPresenti();
        JComboBox<String> opzioni=new JComboBox<>(nomiRuoli);
        JButton ok=new JButton("OK");
        if(nomiRuoli.length==0){//non ho nemmeno un'unita da rimuovere
            ok.setEnabled(false);
        }
        JButton cancel=new JButton("Cancella");

        ok.addActionListener(e -> {
            if(nomiRuoli.length==1 && !elem.getDipendenti().isEmpty()){
                JOptionPane.showMessageDialog(pd, "Impossibile rimuovere l'unico ruolo rimasto essendo assegnato ad alcuni figli, prima di" +
                                " procedere aggiungere un ruolo da assegnare ai figli o rimuoverli",
                        "Errore nella rimozione", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String scelto= (String) opzioni.getSelectedItem();
            Ruolo ruolo=trovaRuolo(scelto);
            if(presentiDipendentiConQuelRuolo(ruolo)){
                JOptionPane.showMessageDialog(pd, "Impossibile rimuovere il ruolo," +
                                "assicurarsi di assegnarne uno nuovo ai dipendenti a cui è assegnato prima di procedere",
                        "Errore nella rimozione", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                elem.removeRuolo(ruolo);
            } catch (RuoloNonPresenteNellUnitaException | SubjectSenzaListenerInAscoltoException ex ) {
                throw new RuntimeException(ex);
            }
            finestra.dispose();
        });
        cancel.addActionListener(e -> {
            finestra.dispose();
        });
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(messaggio)
                .addComponent(opzioni,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(ok)
                        .addComponent(cancel))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(messaggio)
                .addComponent(opzioni,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(ok)
                        .addComponent(cancel))
        );

        finestra.add(pannello);
        finestra.pack();
        return finestra;
    }

    private boolean presentiDipendentiConQuelRuolo(Ruolo ruolo) {
        return elem.getPersonale().containsValue(ruolo);//SE NELLA MAPPA PERSONALE C'È IL RUOLO TRA I VALORI VUOL DIRE CHE UN DIPENDENTE HA QUELLO
    }

    private String[] listaRuoliPresenti(){
        LinkedList<String> lista=new LinkedList<>();
        for(Ruolo ruolo: elem.getRuoliDisponibili()){
            lista.add(ruolo.getNome());
           }
        return lista.toArray(new String[lista.size()]);
    }

    private Ruolo trovaRuolo(String scelto){
            for (Ruolo r : elem.getRuoliDisponibili()) {
                if(r.getNome().equals(scelto)){
                    return r;
                }
            }
        return null;//non restituisco mai null perche passo una stringa che è stata scelta tra nomi di ruoli esistenti
    }

    @Override
    public boolean undoIt() {
        return false;
    }
}
