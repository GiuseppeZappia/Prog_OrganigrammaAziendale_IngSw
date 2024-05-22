package GUI;

import Observer.CambiamentoUnitaListener;
import Utilities.Dipendente;
import Utilities.Ruolo;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import Composite.SottoUnitaOrganizzativa;
import Composite.UnitaOrganizzativa;
import Composite.OrganigrammaElement;
import Composite.OrganoGestione;

public class PannelloDisegno extends JPanel implements CambiamentoUnitaListener {
    private final LinkedList<OrganigrammaElement> unitaDisegnate = new LinkedList<>();
    private int maxLarg,maxAlt=800;

    public PannelloDisegno() {
        setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(2000, 2000));
        repaint();
    }

    public LinkedList<OrganigrammaElement> getUnitaDisegnate() {
        return unitaDisegnate;
    }
    //FAI IN MODO CHE BOTTONE AGGIUNGI ORGANO SI SPEGNE DOPO CHE È STATO CLICCATO LA PRIMA VOLTA E RIAPPARE QUANDO VIENE ELIMINATO
    //ORGANO DI GESTIONE QUINDI DOPO CHE O RIMUOVO TUTTO O RIMUOVO SOLO LUI

    @Override
    public Dimension preferredSize(){
        return new Dimension(maxLarg,maxAlt);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!unitaDisegnate.isEmpty()) {
            Graphics2D g2 = (Graphics2D) g;
            OrganigrammaElement orgGest = unitaDisegnate.getFirst();//FAI IN MODO CHE AGGIUNGA SEMPRE PER PRIMO QUESTO OPPURE IMPL != QUI
            //CERCO PUNTO CENTRALE FINESTRA PER DISEGNARE RETTANGOLO
            int rettX=(getWidth()-150)/2; //dove 150 è dim rett
            disegnaTutto(g2, orgGest, rettX, 60);
        }
        revalidate();//per fargli aggiornare dimensioni e garantire di aggiungere scrollBarr se serve
    }


    private void disegnaTutto(Graphics2D g,OrganigrammaElement orgGest,int x,int y){
            int rettLarg=150;
            int rettAlt=50;
            g.drawRect(x,y , rettLarg, rettAlt);
            FontMetrics metrics = g.getFontMetrics(g.getFont());//prendo caratteristiche del testo che inserisco sul panel
            int textX = x + (rettLarg - metrics.stringWidth(orgGest.getNome())) / 2; //cosi trovo la x dove mettere testo, in pratica la sto centrando in base alla lunghezza nel rettangolo lasciando da ambo i lati stessa distanza con rettangolo
            int textY = y + ((rettAlt - metrics.getHeight()) / 2) + metrics.getAscent();
            g.drawString(orgGest.getNome(), textX, textY);
            maxLarg=Math.max(maxLarg,x+rettLarg);
            maxAlt=Math.max(maxAlt,y+rettAlt);
            if( !(orgGest instanceof SottoUnitaOrganizzativa) && !orgGest.getChild().isEmpty()) {//perche sottounita non hanno figli
                int numFigli=orgGest.getChildCount();
                int spazioTraRettangoli=rettLarg*2-60;//per la divisione dello spazio
                int largTot=(numFigli-1)*spazioTraRettangoli;
                int startX = x - largTot/2;
                int i=0;
                for(OrganigrammaElement unita : orgGest.getChild()) {//SO CHE AVRO SOLO UNITAGESTIONE
                    int childX=startX+i*spazioTraRettangoli;
                    int childY = y + rettAlt + 60;
                    //g.drawLine(x + rettLarg / 2, y + rettAlt, childX + rettLarg / 2, childY);
                    i++;
                    disegnaTutto(g,unita,childX,childY);
                }
            }
    }


    @Override
    public void ruoloCambiato(Ruolo r, LinkedList<Dipendente> dipendenti) {

    }

    @Override
    public void inseritoFiglio(OrganigrammaElement padre, OrganigrammaElement figlio) {
            unitaDisegnate.add(figlio);
            this.repaint();
    }

    @Override
    public void rimossoFiglio(OrganigrammaElement padre, OrganigrammaElement figlio) {

    }


    public void rimuoviUnita(OrganigrammaElement element){
        unitaDisegnate.remove(element);
        repaint();//ricontrolla sti due
    }


    public void aggiungiUnita(OrganigrammaElement unita) {
        unitaDisegnate.add(unita);
        unita.addListener(this);
        repaint();
    }
}
