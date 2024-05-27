package command.FactoryDialog;

import composite.OrganigrammaElement;
import composite.utilities.Dipendente;
import composite.utilities.Ruolo;
import exceptions.DipendenteGiaEsistenteException;
import gui.PannelloDisegno;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.LinkedList;

public class AggiungiDipendenteDialogFactory implements CreateDialog {
    private PannelloDisegno pd;
    private OrganigrammaElement elem;

    public AggiungiDipendenteDialogFactory() {}

    @Override
    public JDialog createDialog(PannelloDisegno pd, OrganigrammaElement elem) {
        this.pd = pd;
        this.elem = elem;
        //DEVO PASSARE AL JDIALOG L'OWNER, LO RICAVO DAL MIO PANNELLO DI DISEGNO
        //FACCIO CAST SENZA INSTANCEOF PERCHE SONO SICURO CHE IL JPANEL IN QUESTIONE SIA QUELLO CHE HA COME PADRE IL FRAME
        Frame framePrincipale = (Frame) SwingUtilities.getWindowAncestor(pd);
        JDialog finestra = new JDialog(framePrincipale, "Inserimento dipendente", true);//mettendo true so che nel frattempo utente non puo usare resto dell'app ma in caso deve chiudere dialogo
        finestra.setLocationRelativeTo(null);//al centro
        JPanel pannello = new JPanel();
        GroupLayout layout = new GroupLayout(pannello);
        pannello.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel nomeDipendente = new JLabel("Nome: ");
        JTextField campoNome = new JTextField(20);

        JLabel cognomeDipendnente = new JLabel("Cognome: ");
        JTextField campoCognome = new JTextField(20);
        JLabel cittaDipendente = new JLabel("Città: ");
        JTextField campoCitta = new JTextField(20);
        JLabel indirizzoDipendente = new JLabel("Indirizzo: ");
        JLabel etaDipendente = new JLabel("Età: ");
        JLabel ruoloDipendente = new JLabel("Ruolo: ");
        JTextField campoIndirizzo = new JTextField(20);
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        JFormattedTextField campoEta = new JFormattedTextField(numberFormat);
        campoEta.setColumns(10);

        JButton scegliTraPresenti = new JButton("Sfoglia");
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancella");
        String[] nomiRuoli = listaRuoliPresenti();
        JComboBox<String> opzioni = new JComboBox<>(nomiRuoli);
        if (nomiRuoli.length == 0) {//non ho nemmeno un ruolo per quella unita
            ok.setEnabled(false);
        }

        scegliTraPresenti.addActionListener(e -> {
            JDialog finestraRuoliPrese = apriFinestraDipendentiPresenti();
            finestraRuoliPrese.setVisible(true);

            finestra.dispose();
        });

        ok.addActionListener(e -> {
            String nome = campoNome.getText();
            String cognome = campoCognome.getText();
            String citta = campoCitta.getText();
            String indirizzo = campoIndirizzo.getText();
            Number eta = (Number) campoEta.getValue();
            String nomeRuolo = (String) opzioni.getSelectedItem();
            Ruolo ruolo = trovaRuolo(nomeRuolo);
            if (nome == null || cognome == null || citta == null || indirizzo == null || eta == null) {//SE PREME CANCELLA RESTITUISCO SENZA FARE NULLA
                JOptionPane.showMessageDialog(pd, "Parametri mancanti o non validi", "Errore nell'inserimento", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (nome.trim().isEmpty() || cognome.trim().isEmpty() || citta.trim().isEmpty() || indirizzo.trim().isEmpty() || eta.intValue() < 0) {
                JOptionPane.showMessageDialog(pd, "Parametri mancanti o non validi", "Errore nell'inserimento", JOptionPane.ERROR_MESSAGE);
                return;
            }//POTREI UNIRE I DUE IF, LO FACCIO PER MAGGIORRE CHIAREZZA
            Dipendente dipendente = new Dipendente(nome, cognome, citta, indirizzo, eta.intValue());
            try {
                dipendente.aggiungiDipendenteAdUnita(this.elem, ruolo);//LO AGGIUNGO ALL'UNITA E POI CON CHIAMATA SOTTO:
                this.elem.addDipendenti(dipendente);//FACCIO IN MODO CHE QUELLA UNITA SE LO MEMORIZZI
            } catch (DipendenteGiaEsistenteException ex) {
                JOptionPane.showMessageDialog(pd, "Dipendente già presente, sceglierlo tramite la funzione 'Sfoglia'", "Errore nell'inserimento", JOptionPane.ERROR_MESSAGE);
            }
            finestra.dispose();
        });

        cancel.addActionListener(e -> {
            finestra.dispose();
        });


        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nomeDipendente)
                        .addComponent(cognomeDipendnente)
                        .addComponent(cittaDipendente)
                        .addComponent(indirizzoDipendente)
                        .addComponent(etaDipendente)
                        .addComponent(ruoloDipendente))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(campoNome)
                        .addComponent(campoCognome)
                        .addComponent(campoCitta)
                        .addComponent(campoIndirizzo)
                        .addComponent(campoEta)
                        .addComponent(opzioni))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scegliTraPresenti)
                        .addComponent(ok)
                        .addComponent(cancel)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nomeDipendente)
                        .addComponent(campoNome))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(cognomeDipendnente)
                        .addComponent(campoCognome))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(cittaDipendente)
                        .addComponent(campoCitta))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(indirizzoDipendente)
                        .addComponent(campoIndirizzo)
                        .addComponent(scegliTraPresenti))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(etaDipendente)
                        .addComponent(campoEta)
                        .addComponent(ok))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(ruoloDipendente)
                        .addComponent(opzioni)
                        .addComponent(cancel)));
        finestra.add(pannello);
        finestra.pack();
        return finestra;
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

    private JDialog apriFinestraDipendentiPresenti() {
        Frame framePrincipale = (Frame) SwingUtilities.getWindowAncestor(pd);
        JDialog finestra = new JDialog(framePrincipale, "Scelta tra dipendenti presenti", true);//mettendo true so che nel frattempo utente non puo usare resto dell'app ma in caso deve chiudere dialogo
        finestra.setLocationRelativeTo(null);//al centro
        JPanel pannello = new JPanel();
        GroupLayout layout = new GroupLayout(pannello);
        pannello.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancella");
        JLabel messaggio = new JLabel("Scegli un dipendente tra quelli presenti");
        JLabel scegliRuolo = new JLabel("Scegli un ruolo per questo dipendente");
        String[] nomiRuoli = listaRuoliPresenti();
        JComboBox<String> opzioniRuoli = new JComboBox<>(nomiRuoli);
        if (nomiRuoli.length == 0) {
            ok.setEnabled(false);
        }
        String[] nomiDipendenti = listaDipendentiPresenti();
        JComboBox<String> opzioni = new JComboBox<>(nomiDipendenti);
        if (nomiDipendenti.length == 0) {//non ho nemmeno un'unita da rimuovere
            ok.setEnabled(false);
        }

        ok.addActionListener(e -> {
            String dipendenteScelto = (String) opzioni.getSelectedItem();
            String ruoloScelto = (String) opzioniRuoli.getSelectedItem();
            Dipendente dipendente = trovaDipendente(dipendenteScelto);
            Ruolo ruolo = trovaRuolo(ruoloScelto);
            try {
                dipendente.aggiungiDipendenteAdUnita(this.elem, ruolo);
                elem.addDipendenti(dipendente);
            } catch (DipendenteGiaEsistenteException ex) {
                throw new RuntimeException(ex);//qua non ci va mai perche faccio scegliere solo dipendenti di altre unita
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
        LinkedList<String> lista = new LinkedList<>();
        for (OrganigrammaElement elem : pd.getUnitaDisegnate()) {
            if (!elem.equals(this.elem)) {//cosi non do possibilita di inserire in un ruolo un dipendente dello stesso
                for (Dipendente d : elem.getDipendenti()) {
                    lista.add(d.getNome() + "-" + d.getCognome() + "-" + d.getEta());
                }
            }
        }
        return lista.toArray(new String[lista.size()]);
    }

}


