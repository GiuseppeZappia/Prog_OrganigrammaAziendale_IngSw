package GUI;

import Command.AggiungiFiglioCommand;
import Command.CommandHandler;
import Command.DisegnaElementCommand;
import Command.HistoryCommandHandler;
import Composite.OrganoGestione;
import Exceptions.FiglioUnitaNonValidoException;
import Exceptions.SubjectSenzaListenerInAscoltoException;
import Mediator.ChangeManagerMediator;
import Mediator.ConcreteChangheManagerMediator;
import Composite.OrganigrammaElement;
import Composite.SottoUnitaOrganizzativa;
import javax.swing.*;

import Composite.UnitaOrganizzativa;

import java.awt.*;
import java.util.LinkedList;

public class PanelBottoni extends JPanel {
    private final JButton creaOrgano, creaUnita, creaSottoUnita, salva;
    private final CommandHandler cmdHandler = new HistoryCommandHandler();
    private final ChangeManagerMediator mediator = new ConcreteChangheManagerMediator();

    public PanelBottoni(PannelloDisegno pd) {
        setLayout(new FlowLayout(FlowLayout.LEFT));//layout che mette bottoni tutti belli in riga da sx
        creaOrgano = new JButton("Crea Organo Gestione");
        creaUnita = new JButton("Aggiungi Unita");
        creaSottoUnita = new JButton("Aggiungi Sottounità");
        salva = new JButton("Salva");
        add(creaOrgano);
        add(creaUnita);
        add(creaSottoUnita);
        add(salva);

        //LI INSERISCO COME LISTENER DI THIS COSI SO COSA FARE QUANDO VENGONO PREMUTI
        creaOrgano.addActionListener(e -> {//implemento metodo dell'interfaccia ActionListener che si chiama ActionPerformed
                    String nomeOrganoGestione = JOptionPane.showInputDialog(pd, "Nome Organo Gestione:", "Creazione Organo Gestione", JOptionPane.QUESTION_MESSAGE);
                    if (nomeOrganoGestione == null) {//SE PREME CANCELLA RESTITUISCO SENZA FARE NULLA
                        return;
                    }
                    if (nomeOrganoGestione.trim().isEmpty()) {//SE PROVA AD INVIARE SENZA SCRIVERE NULLA DO ERRORE
                        JOptionPane.showMessageDialog(pd, "Impossibile creare unità senza nome", "Errore nell'inserimento", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    OrganigrammaElement elem = new OrganoGestione(nomeOrganoGestione, mediator);
                    cmdHandler.handleCommand(new DisegnaElementCommand(pd, elem));
                    creaOrgano.setEnabled(false);//DOPO AVER INSERITO UN ORGANO NON NE POSSO INSERIRE PIU' LO RIMETTO VISIBILE DOPO AVER CANCELLATO TUTTO PER CREARNE UNO NUOVO
                    //ALTERNATIVAMENTE METTO IF CHE SE C'È GIA GESTIONE NELLA LISTA LANCIA DIALOG ERRORE
                }
        );

        //BOTTONE CREA UNITA'
        creaUnita.addActionListener(e -> {
            cmdHandler.handleCommand(new AggiungiFiglioCommand(pd,mediator));
        });

        creaSottoUnita.addActionListener(e -> {
            JDialog finestra=apriFinestraDialogoComplessa(pd);
            finestra.setVisible(true);
        });





    }

    private boolean nomeGiaPresente(String nome,LinkedList<OrganigrammaElement> presenti){
        for(OrganigrammaElement unita:presenti){
            if(unita.getNome().equals(nome)){
                return true;
            }
        }
        return false;
    }
    private JDialog apriFinestraDialogoComplessa(PannelloDisegno pd){
            //DEVO PASSARE AL JDIALOG L'OWNER, LO RICAVO DAL MIO PANNELLO DI DISEGNO
            //FACCIO CAST SENZA INSTANCEOF PERCHE SONO SICURO CHE IL JPANEL IN QUESTIONE SIA QUELLO CHE HA COME PADRE IL FRAME
            Frame framePrincipale= (Frame) SwingUtilities.getWindowAncestor(pd);
            JDialog finestra=new JDialog(framePrincipale,"Inserimento Sottounità",true);//mettendo true so che nel frattempo utente non puo usare resto dell'app ma in caso deve chiudere dialogo
            finestra.setLocationRelativeTo(null);//al centro
            JPanel pannello=new JPanel();
            GroupLayout layout = new GroupLayout(pannello);
            pannello.setLayout(layout);
            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);

            JLabel nomeSottounita=new JLabel("Nome Sottounità Organizzativa:");
            JTextField campoNomeSottounita=new JTextField(20);

            JLabel sceltaPadre=new JLabel("Scegli il padre:");
            String[] nomiUnita=new String[pd.getUnitaDisegnate().size()];
            int i=0;
            for(OrganigrammaElement elem:pd.getUnitaDisegnate()){
                if(elem instanceof UnitaOrganizzativa){//faccio comparire nella lista solo i possibili padri
                    nomiUnita[i]=elem.getNome();
                    i++;
                }
            }
            JComboBox<String> opzioni=new JComboBox<>(nomiUnita);
            JButton ok=new JButton("OK");
            if(nomiUnita.length-1<=0){//TOLGO UNO PERCHE NON DEVO CONSIDERARE ORGANOGESTIONE TRA POSSIBILI PADRI
                ok.setEnabled(false);//NON FACCIO PREMERE OK SE NON C'È ALMENO UN POSSIBILE PADRE
            }
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
                if(nomeGiaPresente(nome,pd.getUnitaDisegnate())){
                    JOptionPane.showMessageDialog(pd, "È già presente un'unità con questo nome", "Errore nell'inserimento", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                SottoUnitaOrganizzativa sottunita=new SottoUnitaOrganizzativa(nome,mediator);
                sottunita.addListener(pd);
                String padreScelto= (String) opzioni.getSelectedItem();
                OrganigrammaElement padre=trovaPadre(padreScelto,pd);
                try {if(padre!=null){//perche nella scelta della listbox da spazio vuoto che non fa fare selezione ma se scelto da null
                    padre.addChild(sottunita);}
                } catch (FiglioUnitaNonValidoException | SubjectSenzaListenerInAscoltoException ex) {
                    throw new RuntimeException(ex);
                }
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

    private OrganigrammaElement trovaPadre(String nome,PannelloDisegno pd){
        for(OrganigrammaElement elem:pd.getUnitaDisegnate()){
            if(elem.getNome().equals(nome)){
                return elem;
            }
        }
        return null; //NON SUCCEDE MAI, DALLA CHECKBOX SCEGLIE NOME VALIDO E PRESENTE TRA QUESTI
    }

}
