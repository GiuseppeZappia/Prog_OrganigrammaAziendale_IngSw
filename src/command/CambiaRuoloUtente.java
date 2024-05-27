package command;

import composite.OrganigrammaElement;
import composite.utilities.Dipendente;
import composite.utilities.Ruolo;
import exceptions.DipendenteNonPresenteNellUnitaException;
import gui.PannelloDisegno;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class CambiaRuoloUtente implements Command{
    private PannelloDisegno pd;
    private Dipendente dipendente;
    private OrganigrammaElement elem;
    private Ruolo ruoloDaEliminare;

    public CambiaRuoloUtente(PannelloDisegno pd, Dipendente dipendente, OrganigrammaElement elem, Ruolo ruoloDaEliminare) {
        this.pd = pd;
        this.dipendente = dipendente;
        this.elem = elem;
        this.ruoloDaEliminare=ruoloDaEliminare;
    }


    @Override
    public boolean doIt() {
        JDialog finestraRuoliPrese= apriFinestraRuoliPresenti();
        finestraRuoliPrese.setVisible(true);
        return false;
    }

    @Override
    public boolean undoIt() {
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
        JLabel messaggio=new JLabel("Scegli il nuovo ruolo di "+dipendente.getNome()+" "+dipendente.getCognome());

        String[] nomiRuoli=listaRuoliPresenti();
        JComboBox<String> opzioni=new JComboBox<>(nomiRuoli);
        JButton ok=new JButton("OK");
        JButton cancel=new JButton("Cancella");
        ok.addActionListener(e -> {
            String scelto= (String) opzioni.getSelectedItem();
            Ruolo ruolo=trovaRuolo(scelto);
            try {
                dipendente.cambiaRuolo(ruolo,this.elem);
            } catch (DipendenteNonPresenteNellUnitaException ex ) {
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

    private String[] listaRuoliPresenti(){
        LinkedList<String> lista=new LinkedList<>();
        for(Ruolo ruolo: elem.getRuoliDisponibili()){
            if(!ruolo.equals(this.ruoloDaEliminare)){
                lista.add(ruolo.getNome());
            }
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

}
