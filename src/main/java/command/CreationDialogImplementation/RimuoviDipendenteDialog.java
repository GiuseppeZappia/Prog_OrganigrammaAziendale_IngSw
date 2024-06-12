package command.CreationDialogImplementation;

import composite.OrganigrammaElement;
import composite.utilities.Dipendente;
import exceptions.DipendenteNonPresenteNellUnitaException;
import gui.PannelloDisegno;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class RimuoviDipendenteDialog implements CreateDialog {
    private PannelloDisegno pd;
    private OrganigrammaElement elem;

    @Override
    public JDialog createDialog(PannelloDisegno pd, OrganigrammaElement elem) {
        this.pd = pd;
        this.elem = elem;
        Frame framePrincipale = (Frame) SwingUtilities.getWindowAncestor(pd);
        JDialog finestra = new JDialog(framePrincipale, "Eliminazione dipendente", true);//mettendo true so che nel frattempo utente non puo usare resto dell'app ma in caso deve chiudere dialogo
        JPanel pannello = new JPanel();
        GroupLayout layout = new GroupLayout(pannello);
        pannello.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        JLabel messaggio = new JLabel("Scegli un dipendente tra quelli presenti");
        String[] nomeDipendenti = listaDipendentiPresenti();
        JComboBox<String> opzioni = new JComboBox<>(nomeDipendenti);
        JButton ok = new JButton("OK");
        if (nomeDipendenti.length == 0) {//non ho nemmeno un dipendente da rimuovere
            ok.setEnabled(false);
        }
        JButton cancel = new JButton("Cancella");
        ok.addActionListener(e -> {
            String scelto = (String) opzioni.getSelectedItem();
            Dipendente dipendente = trovaDipendente(scelto);
            try {
                this.elem.removeDipendente(dipendente);
            } catch (DipendenteNonPresenteNellUnitaException ex) {
                throw new RuntimeException(ex);
            }
            finestra.dispose();
        });
        cancel.addActionListener(e -> {
            finestra.dispose();
        });
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(messaggio)
                .addComponent(opzioni, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(ok)
                        .addComponent(cancel))
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(messaggio)
                .addComponent(opzioni, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(ok)
                        .addComponent(cancel))
        );
        finestra.add(pannello);
        finestra.pack();
        return finestra;
    }

    private Dipendente trovaDipendente(String scelto) {
        String[] partiDipendente = scelto.split("-");
        Dipendente fittizio = new Dipendente(partiDipendente[0], partiDipendente[1], null, null, Integer.parseInt(partiDipendente[2]));
        for (OrganigrammaElement eleme : pd.getUnitaDisegnate()) {
            for (Dipendente d : eleme.getDipendenti()) {
                if (d.equals(fittizio)) {
                    return d;
                }
            }
        }
        return null;//MAI RESTITUISCE PERCHE SCELGIAMO NOI DA LISTBOX
    }

    private String[] listaDipendentiPresenti() {
        Collection<Dipendente> listaDipendentiUnita = elem.getDipendenti();
        String[] ret = new String[listaDipendentiUnita.size()];
        int i = 0;
        for (Dipendente d : listaDipendentiUnita) {
            ret[i] = d.getNome() + "-" + d.getCognome() + "-" + d.getEta();
            i++;
        }
        return ret;
    }

}

