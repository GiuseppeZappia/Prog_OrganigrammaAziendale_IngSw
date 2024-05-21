package Command;

import Composite.OrganigrammaElement;
import Exceptions.FiglioUnitaNonValidoException;
import Exceptions.SubjectSenzaListenerInAscoltoException;
import GUI.PannelloDisegno;
import Mediator.ChangeManagerMediator;
import Composite.UnitaOrganizzativa;
import Composite.SottoUnitaOrganizzativa;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class AggiungiFiglioUnitaCommand implements Command {
    private PannelloDisegno pd;
    private ChangeManagerMediator mediator;

    public AggiungiFiglioUnitaCommand(PannelloDisegno pd,ChangeManagerMediator mediator){
        this.pd = pd;
        this.mediator = mediator;
    }

    @Override
    public boolean disegna() {
        JDialog finestra=apriFinestraDialogoComplessa(pd);
        finestra.setVisible(true);
        return true;
    }

    @Override
    public boolean rimuovi() {
        return false;
    }


    private boolean nomeGiaPresente(String nome, LinkedList<OrganigrammaElement> presenti){
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
