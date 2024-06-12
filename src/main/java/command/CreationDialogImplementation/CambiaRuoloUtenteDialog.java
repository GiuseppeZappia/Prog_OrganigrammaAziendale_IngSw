package command.CreationDialogImplementation;
import composite.OrganigrammaElement;
import composite.utilities.Dipendente;
import composite.utilities.Ruolo;
import exceptions.DipendenteNonPresenteNellUnitaException;
import gui.PannelloDisegno;
import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class CambiaRuoloUtenteDialog implements CreateDialog {
    private PannelloDisegno pd;
    private OrganigrammaElement elem;

    @Override
    public JDialog createDialog(PannelloDisegno pd, OrganigrammaElement elem) {
        this.pd = pd;
        this.elem = elem;
        Frame framePrincipale = (Frame) SwingUtilities.getWindowAncestor(pd);
        JDialog finestra = new JDialog(framePrincipale, "Cambio ruolo", true);//mettendo true so che nel frattempo utente non puo usare resto dell'app ma in caso deve chiudere dialogo
        JPanel pannello = new JPanel();
        GroupLayout layout = new GroupLayout(pannello);
        pannello.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancella");
        JLabel messaggio = new JLabel("Scegli il dipendente a cui cambiare il ruolo");
        JLabel scegliRuolo = new JLabel("Scegli un nuovo ruolo per questo dipendente");
        String[] nomiDipendenti = listaDipendentiPresenti();
        JComboBox<String> opzioni = new JComboBox<>(nomiDipendenti);
        if (nomiDipendenti.length == 0) {//non ho nemmeno un dipendente
            ok.setEnabled(false);
        }
        String[] nomiRuoli = listaRuoliPresenti();
        JComboBox<String> opzioniRuoli = new JComboBox<>(nomiRuoli);
        ok.addActionListener(e -> {
            String dipendenteScelto = (String) opzioni.getSelectedItem();
            String ruoloScelto = (String) opzioniRuoli.getSelectedItem();
            Dipendente dipendente = trovaDipendente(dipendenteScelto);
            Ruolo ruolo = trovaRuolo(ruoloScelto);
            try {
                dipendente.cambiaRuolo(ruolo, elem);
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
                .addComponent(scegliRuolo)
                .addComponent(opzioni, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(opzioniRuoli)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(ok)
                        .addComponent(cancel))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(messaggio)
                .addComponent(opzioni, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(scegliRuolo)
                .addComponent(opzioniRuoli, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(ok)
                        .addComponent(cancel))
        );
        finestra.add(pannello);
        finestra.pack();
        return finestra;
    }

    private Ruolo trovaRuolo(String scelto) {
        for (OrganigrammaElement elem : pd.getUnitaDisegnate()) {
            for (Ruolo r : elem.getRuoliDisponibili()) {
                if (r.getNome().equals(scelto)) {
                    return r;
                }
            }
        }
        return null;//non restituisco mai null perche passo una stringa che è stata scelta tra nomi di ruoli esistenti
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

    private String[] listaRuoliPresenti() {
        Collection<Ruolo> ruoli = elem.getRuoliDisponibili();
        String[] ret = new String[ruoli.size()];
        int i = 0;
        for (Ruolo elem : ruoli) {
            ret[i] = elem.getNome();
            i++;
        }
        return ret;
    }
}