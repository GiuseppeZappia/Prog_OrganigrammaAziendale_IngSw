package GUI;

import Observer.CambiamentoUnitaListener;
import Utilities.Dipendente;
import Utilities.Ruolo;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

import Composite.OrganigrammaElement;

public class PannelloDisegno extends JPanel implements CambiamentoUnitaListener {
    private final LinkedList<OrganigrammaElement> unitaDisegnate = new LinkedList<>();
    private HashMap<OrganigrammaElement,Point> puntiOccupati=new HashMap<>();
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
            puntiOccupati.clear();
            disegnaTutto(g2, orgGest, rettX, 60);

        }
        revalidate();//per fargli aggiornare dimensioni e garantire di aggiungere scrollBarr se serve
    }


    private void disegnaTutto(Graphics2D g,OrganigrammaElement orgGest,int x,int y){
        int rettLarg=150;
        int rettAlt=50;
        int spazioTraRettOri=60;
        int spazioTraRettVer=60;
        g.drawRect(x,y , rettLarg, rettAlt);
        puntiOccupati.put(orgGest,new Point(x, y));//aggiungo punto del rett disegnato
        FontMetrics metrics = g.getFontMetrics(g.getFont());//prendo caratteristiche del testo che inserisco sul panel
        int textX = x + (rettLarg - metrics.stringWidth(orgGest.getNome())) / 2; //cosi trovo la x dove mettere testo, in pratica la sto centrando in base alla lunghezza nel rettangolo lasciando da ambo i lati stessa distanza con rettangolo
        int textY = y + ((rettAlt - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(orgGest.getNome(), textX, textY);
        maxLarg=Math.max(maxLarg,x+rettLarg);
        maxAlt=Math.max(maxAlt,y+rettAlt);
        //QUA HAI RIMOSSO CONTROLLO !(orgGest instanceof SottoUnitaOrganizzativa) &&
        //PER FARE PROVE ALBERO INFINITO
        if(  !orgGest.getChild().isEmpty()) {//perche sottounita non hanno figli
            int spazioOccupatoDaiFigli=calcolaSpazioOccupato(orgGest,rettLarg,spazioTraRettOri);
            int startX = x +(rettLarg-spazioOccupatoDaiFigli)/2;
            int centroX=x+rettLarg/2;
            int bottomY=y+rettAlt;

            g.drawLine(centroX,bottomY,centroX,bottomY+spazioTraRettVer/2);
            int lineaOrizzY=bottomY+spazioTraRettVer/2;
            int i=0;
            Point coordPrimoFiglio=null;
            Point coordultimoFiglio=null;
            for(OrganigrammaElement unita : orgGest.getChild()) {//SO CHE AVRO SOLO UNITAGESTIONE
                int spazioOccupatoFiglio=calcolaSpazioOccupato(unita,rettLarg,spazioTraRettOri);
                int childX=startX;
                int childY = y + rettAlt + spazioTraRettVer;
                int aggiuntaCollis=0;
                if(childX<0){
                    childX=0;
                }

                while(collisione(childX,childY)){
                    childX+=spazioTraRettOri;
                    aggiuntaCollis+=spazioTraRettOri;
                }
                int figlioCentrX=childX+rettLarg/2;
                g.drawLine(figlioCentrX,lineaOrizzY,figlioCentrX,lineaOrizzY+spazioTraRettVer/2);
                disegnaTutto(g,unita,childX,childY);
                startX+=spazioOccupatoFiglio+spazioTraRettOri+aggiuntaCollis;
                if(i==0){
                    coordPrimoFiglio=new Point(childX+rettLarg/2,childY);
                }
                if(i== orgGest.getChildCount()-1){
                    coordultimoFiglio=new Point(childX+rettLarg/2,childY);
                }
                i++;
            }
            if(coordPrimoFiglio!=null && coordultimoFiglio!=null) {
               g.drawLine(coordPrimoFiglio.x,lineaOrizzY,coordultimoFiglio.x,lineaOrizzY);
            }
        }
    }


    private boolean collisione(int childX,int childY){
        for(Point punto : puntiOccupati.values()){
            if(childY== punto.y && ((childX>=punto.x && childX<=punto.x+150)||(punto.x>=childX && punto.x<=childX+150))){
                return true;
            }
        }
        return false;
    }

    private int calcolaSpazioOccupato(OrganigrammaElement elem,int rettLarg, int spazio){
        //qua elimino per prova (elem instanceof SottoUnitaOrganizzativa)||
        //per albero infinito
        if(elem.getChild().isEmpty()){
            return rettLarg;
        }
        int spazioOccuapato=0;
        for(OrganigrammaElement figlio : elem.getChild()){
            spazioOccuapato+=calcolaSpazioOccupato(figlio,rettLarg,spazio)+spazio;
        }
        return spazioOccuapato-spazio;
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
    public void rimossoFiglio(OrganigrammaElement figlio) {
            unitaDisegnate.remove(figlio);
            this.repaint();
    }


    public void aggiungiUnita(OrganigrammaElement unita) {
        unitaDisegnate.add(unita);
        unita.addListener(this);
        repaint();
    }


}