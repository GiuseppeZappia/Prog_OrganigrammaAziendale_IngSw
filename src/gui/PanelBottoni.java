package gui;

import command.*;
import mediator.ChangeManagerMediator;
import mediator.ConcreteChangheManagerMediator;

import javax.swing.*;
import java.awt.*;

import composite.OrganigrammaElement;
import composite.OrganoGestione;
import composite.UnitaOrganizzativa;

public class PanelBottoni extends JPanel {
    private final JButton creaOrgano,creaSottoUnita, salva,rimuoviUnità,undo,redo;
    private final HistoryCommandHandler cmdHandler = new HistoryCommandHandler();
    private final ChangeManagerMediator mediator = new ConcreteChangheManagerMediator();

    public PanelBottoni(PannelloDisegno pd) {
        setLayout(new FlowLayout(FlowLayout.LEFT));//layout che mette bottoni tutti belli in riga da sx
        creaOrgano = new JButton("Crea Organo Gestione");
        creaSottoUnita = new JButton("Aggiungi Sottounità");
        salva = new JButton("Salva");
        rimuoviUnità=new JButton("Rimuovi unità");
        undo=new JButton("Undo");
        redo=new JButton("Redo");
        add(creaOrgano);
        add(creaSottoUnita);
        add(salva);
        add(rimuoviUnità);
        add(undo);
        add(redo);

        undo.addActionListener(e-> cmdHandler.undo());

        redo.addActionListener(e-> cmdHandler.redo());


        //LI INSERISCO COME LISTENER DI THIS COSI SO COSA FARE QUANDO VENGONO PREMUTI
        creaOrgano.addActionListener(e -> {//implemento metodo dell'interfaccia ActionListener che si chiama ActionPerformed
                    if(!pd.getUnitaDisegnate().isEmpty()){
                        JOptionPane.showMessageDialog(pd, "Impossibile inserire due Organi gestione contemporaneamente", "Errore nell'inserimento", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String nomeOrganoGestione = JOptionPane.showInputDialog(pd, "Nome Organo Gestione:", "Creazione Organo Gestione", JOptionPane.QUESTION_MESSAGE);
                    if (nomeOrganoGestione == null) {//SE PREME CANCELLA RESTITUISCO SENZA FARE NULLA
                        return;
                    }
                    if (nomeOrganoGestione.trim().isEmpty()) {//SE PROVA AD INVIARE SENZA SCRIVERE NULLA DO ERRORE
                        JOptionPane.showMessageDialog(pd, "Impossibile creare unità senza nome", "Errore nell'inserimento", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    OrganigrammaElement elem = new OrganoGestione(nomeOrganoGestione, mediator);
                    cmdHandler.handleCommand(new DisegnaElementCommand(pd,mediator,elem));
        }
        );

        creaSottoUnita.addActionListener(e -> {
            if(pd.getUnitaDisegnate().isEmpty()){
                JOptionPane.showMessageDialog(pd, "Impossibile creare unità senza aver inserito un organo gestione", "Operazione non valida", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JDialog finestra=apriFinestraDialogoComplessaAggiuntaSottoUnita(pd);
            finestra.setVisible(true);
        });

        rimuoviUnità.addActionListener(e -> {
           //cmdHandler.handleCommand(new RimuoviFiglioCommand(pd));
            JDialog finestra=apriFinestraDialogoComplessaRimozione(pd);
            finestra.setVisible(true);
        });


    }


    private JDialog apriFinestraDialogoComplessaAggiuntaSottoUnita(PannelloDisegno pd){
        //DEVO PASSARE AL JDIALOG L'OWNER, LO RICAVO DAL MIO PANNELLO DI DISEGNO
        //FACCIO CAST SENZA INSTANCEOF PERCHE SONO SICURO CHE IL JPANEL IN QUESTIONE SIA QUELLO CHE HA COME PADRE IL FRAME
        Frame framePrincipale= (Frame) SwingUtilities.getWindowAncestor(pd);
        JDialog finestra=new JDialog(framePrincipale,"Inserimento Unità",true);//mettendo true so che nel frattempo utente non puo usare resto dell'app ma in caso deve chiudere dialogo
        finestra.setLocationRelativeTo(null);//al centro
        JPanel pannello=new JPanel();
        GroupLayout layout = new GroupLayout(pannello);
        pannello.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel nomeSottounita=new JLabel("Nome Unità Organizzativa:");
        JTextField campoNomeSottounita=new JTextField(20);

        JLabel sceltaPadre=new JLabel("Scegli il padre:");
        JComboBox<String> opzioni=new JComboBox<>(trovaUnita(pd.getUnitaDisegnate().size(),pd));
        JButton ok=new JButton("OK");
        JButton cancel=new JButton("Cancella");
        ok.addActionListener(e -> {
            String nome=campoNomeSottounita.getText();
            if (nome == null) {//SE PREME CANCELLA RESTITUISCO SENZA FARE NULLA
                return;
            }
            if(nome.trim().isEmpty()){
                JOptionPane.showMessageDialog(pd, "Impossibile creare unità senza nome", "Errore nell'inserimento", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(trovaElemento(nome,pd)!=null){
                JOptionPane.showMessageDialog(pd, "È già presente un'unità con questo nome", "Errore nell'inserimento", JOptionPane.ERROR_MESSAGE);
                return;
            }
            UnitaOrganizzativa unita=new UnitaOrganizzativa(nome,mediator);
            unita.addListener(pd);
            String padreScelto= (String) opzioni.getSelectedItem();
            OrganigrammaElement padre=trovaElemento(padreScelto,pd);
            cmdHandler.handleCommand(new AggiungiFiglioUnitaCommand(unita,padre));
            finestra.dispose();
        });

        cancel.addActionListener(e -> {
            finestra.dispose();
        });

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nomeSottounita)
                        .addComponent(sceltaPadre))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(campoNomeSottounita)
                        .addComponent(opzioni))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(ok)
                        .addComponent(cancel)));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nomeSottounita)
                        .addComponent(campoNomeSottounita)
                        .addComponent(ok))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(sceltaPadre)
                        .addComponent(opzioni)
                        .addComponent(cancel)));
        finestra.add(pannello);
        finestra.pack();
        return finestra;
    }


    private JDialog apriFinestraDialogoComplessaRimozione(PannelloDisegno pd){
        Frame framePrincipale= (Frame) SwingUtilities.getWindowAncestor(pd);
        JDialog finestra=new JDialog(framePrincipale,"Rimozione Unità",true);//mettendo true so che nel frattempo utente non puo usare resto dell'app ma in caso deve chiudere dialogo
        finestra.setLocationRelativeTo(null);//al centro
        JPanel pannello=new JPanel();
        GroupLayout layout = new GroupLayout(pannello);
        pannello.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        JLabel nomeUnita=new JLabel("Scegli l'unità da rimuovere (N.B. La rimozione di un'unità non foglia comporta la rimozione di tutti i suoi figli)");

        String[] nomiUnita=trovaUnita(pd.getUnitaDisegnate().size(),pd);
        JComboBox<String> opzioni=new JComboBox<>(nomiUnita);
        JButton ok=new JButton("OK");
        if(nomiUnita.length==0){//non ho nemmeno un'unita da rimuovere
            ok.setEnabled(false);
        }
        JButton cancel=new JButton("Cancella");

        ok.addActionListener(e -> {

            String scelto= (String) opzioni.getSelectedItem();
            OrganigrammaElement daRimuovere=trovaElemento(scelto,pd);
            if(daRimuovere!=null){//perche nella scelta della listbox da spazio vuoto che non fa fare selezione ma se scelto da null
                if(daRimuovere instanceof OrganoGestione){
                    cmdHandler.handleCommand(new RimuoviTuttoCommand(pd,(OrganoGestione)daRimuovere));
                }
                else{
                    OrganigrammaElement padre=trovaPadre(daRimuovere,pd);
                    cmdHandler.handleCommand(new RimuoviFiglioCommand(padre,daRimuovere));
                }
            }
            finestra.dispose();
        });

        cancel.addActionListener(e -> {
            finestra.dispose();
        });
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(nomeUnita)
                .addComponent(opzioni,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(ok)
                        .addComponent(cancel))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(nomeUnita)
                .addComponent(opzioni,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(ok)
                        .addComponent(cancel))
        );

        finestra.add(pannello);
        finestra.pack();
        return finestra;
    }



    private OrganigrammaElement trovaPadre(OrganigrammaElement daRimuovere,PannelloDisegno pd) {
        for(OrganigrammaElement elem:pd.getUnitaDisegnate()){
            if(elem.getChild().contains(daRimuovere))
                return elem;
        }
        return null;//non restituisce mai null visto che lo sceglie tra quelli possibili dalla mia checkbox
    }

    private String[] trovaUnita(int dim,PannelloDisegno pd){
        String[] ret=new String[dim];
        int i=0;
        for(OrganigrammaElement elem:pd.getUnitaDisegnate()){
            ret[i]=elem.getNome();
            i++;
        }
        return ret;
    }

    private OrganigrammaElement trovaElemento(String nome,PannelloDisegno pd){
        for(OrganigrammaElement elem:pd.getUnitaDisegnate()){
            if(elem.getNome().equals(nome)){
                return elem;
            }
        }
        return null; //NON SUCCEDE MAI, DALLA CHECKBOX SCEGLIE NOME VALIDO E PRESENTE TRA QUESTI
    }
}
