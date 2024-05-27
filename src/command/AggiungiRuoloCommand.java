package command;

import composite.OrganigrammaElement;
import composite.utilities.Ruolo;
import exceptions.RuoloGiaPresenteNellUnitaException;
import gui.PannelloDisegno;
import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.LinkedList;

public class AggiungiRuoloCommand implements Command{
    private PannelloDisegno pd;
    private OrganigrammaElement elem;

    public AggiungiRuoloCommand(PannelloDisegno pd, OrganigrammaElement elem) {
        this.pd = pd;
        this.elem = elem;
    }

    @Override
    public boolean doIt() {
        JDialog finestra=apriFinestraDialogoComplessa(pd);
        finestra.setVisible(true);
        return false;
    }

    @Override
    public boolean undoIt() {
        return false;
    }

    private JDialog apriFinestraDialogoComplessa(PannelloDisegno pd){
        //DEVO PASSARE AL JDIALOG L'OWNER, LO RICAVO DAL MIO PANNELLO DI DISEGNO
        //FACCIO CAST SENZA INSTANCEOF PERCHE SONO SICURO CHE IL JPANEL IN QUESTIONE SIA QUELLO CHE HA COME PADRE IL FRAME
        Frame framePrincipale= (Frame) SwingUtilities.getWindowAncestor(pd);
        JDialog finestra=new JDialog(framePrincipale,"Inserimento ruolo",true);//mettendo true so che nel frattempo utente non puo usare resto dell'app ma in caso deve chiudere dialogo
        finestra.setLocationRelativeTo(null);//al centro
        JPanel pannello=new JPanel();
        GroupLayout layout = new GroupLayout(pannello);
        pannello.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel nomeRuolo=new JLabel("Nome: ");
        JTextField campoNome=new JTextField(20);

        JLabel descrizioneRuolo=new JLabel("Descrizione: ");
        JTextField campoDescrizione=new JTextField(20);
        JLabel requisitiRuolo=new JLabel("Requisiti: ");
        JTextField campoRequisiti=new JTextField(20);
        JLabel stipendioRuolo=new JLabel("Stipendio: ");
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        JFormattedTextField campoStipendio = new JFormattedTextField(numberFormat);
        campoStipendio.setColumns(10);

        JButton scegliTraPresenti=new JButton("Sfoglia");
        JButton ok=new JButton("OK");
        JButton cancel=new JButton("Cancella");

        scegliTraPresenti.addActionListener(e->{
            JDialog finestraRuoliPrese= apriFinestraRuoliPresenti();
            finestraRuoliPrese.setVisible(true);
            finestra.dispose();
        });

        ok.addActionListener(e -> {
            String nome=campoNome.getText();
            String descrizione=campoDescrizione.getText();
            String requisiti=campoRequisiti.getText();
            Number stipendio=(Number) campoStipendio.getValue();
            if (nome == null || descrizione==null || requisiti==null ||stipendio==null) {//SE PREME CANCELLA RESTITUISCO SENZA FARE NULLA
                JOptionPane.showMessageDialog(pd, "Parametri mancanti o non validi", "Errore nell'inserimento", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(nome.trim().isEmpty()||descrizione.trim().isEmpty()||requisiti.trim().isEmpty()||stipendio.intValue()<0){
                JOptionPane.showMessageDialog(pd, "Parametri mancanti o non validi", "Errore nell'inserimento", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Ruolo ruolo=new Ruolo(nome,descrizione,requisiti,stipendio);
            if(esisteRuolo(ruolo,pd)){
                JOptionPane.showMessageDialog(pd, "Ruolo già presente", "Errore nell'inserimento", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                elem.addRuolo(ruolo);
            } catch (RuoloGiaPresenteNellUnitaException ex) {
                throw new RuntimeException(ex);
            }
            finestra.dispose();
        });

        cancel.addActionListener(e -> {
            finestra.dispose();
        });

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nomeRuolo)
                        .addComponent(descrizioneRuolo)
                        .addComponent(requisitiRuolo)
                        .addComponent(stipendioRuolo))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(campoNome)
                        .addComponent(campoDescrizione)
                        .addComponent(campoRequisiti)
                        .addComponent(campoStipendio))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scegliTraPresenti)
                        .addComponent(ok)
                        .addComponent(cancel)));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nomeRuolo)
                        .addComponent(campoNome))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(requisitiRuolo)
                        .addComponent(campoRequisiti)
                        .addComponent(scegliTraPresenti))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(stipendioRuolo)
                        .addComponent(campoStipendio)
                        .addComponent(ok))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(descrizioneRuolo)
                        .addComponent(campoDescrizione)
                        .addComponent(cancel)));
        finestra.add(pannello);
        finestra.pack();
        return finestra;
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
            String scelto= (String) opzioni.getSelectedItem();
            Ruolo ruolo=trovaRuolo(scelto);
            try {
                elem.addRuolo(ruolo);
            } catch (RuoloGiaPresenteNellUnitaException ex) {
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
        for(OrganigrammaElement elem: pd.getUnitaDisegnate()){
            if(!elem.equals(this.elem)){//cosi non mostro tra ruoli possibili quelli che gia ci sno per questa unita
            for(Ruolo r: elem.getRuoliDisponibili()){
                lista.add(r.getNome());
            }
        }}
        return lista.toArray(new String[lista.size()]);
    }

    private Ruolo trovaRuolo(String scelto){
        for(OrganigrammaElement elem: pd.getUnitaDisegnate()) {
            for (Ruolo r : elem.getRuoliDisponibili()) {
                    if(r.getNome().equals(scelto)){
                        return r;
                    }
            }
        }
        return null;//non restituisco mai null perche passo una stringa che è stata scelta tra nomi di ruoli esistenti
    }

    private boolean esisteRuolo(Ruolo ruolo,PannelloDisegno pd){
        for(OrganigrammaElement elem : pd.getUnitaDisegnate()){
            for(Ruolo r:elem.getRuoliDisponibili()){
                if(r.equals(ruolo)){
                    return true;
                }
            }
        }
        return false;
    }
}