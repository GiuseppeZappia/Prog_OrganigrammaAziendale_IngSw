package gui;

import command.*;
import composite.OrganoGestione;
import exceptions.FiglioUnitaNonValidoException;
import exceptions.SubjectSenzaListenerInAscoltoException;
import memento.Memento;
import observer.CambiamentoUnitaListener;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.*;

import composite.OrganigrammaElement;

public class PannelloDisegno extends JPanel implements CambiamentoUnitaListener {
    private final LinkedList<OrganigrammaElement> unitaDisegnate = new LinkedList<>();
    private final HashMap<OrganigrammaElement, Point> puntiOccupati = new HashMap<>();
    private int maxLarg, maxAlt = 800;
    private PopupMenuOperazioni popupMenuClickMouse;
    private CommandHandler cmdHandler = new HistoryCommandHandler();

    public PannelloDisegno() {
        setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(2000, 2000));
        repaint();
        this.popupMenuClickMouse = new PopupMenuOperazioni(this, cmdHandler);
    }

    public PannelloDisegnoMemento creaMemento() {
        LinkedHashMap<OrganigrammaElement, LinkedList<OrganigrammaElement>> figliPresenti = new LinkedHashMap<>();
        OrganoGestione orgGest = (OrganoGestione) unitaDisegnate.getFirst();
        figliPresenti.put(orgGest, new LinkedList<>(orgGest.getChild()));
        for (OrganigrammaElement figlio : orgGest.getChild()) {
            LinkedList<OrganigrammaElement> figliDeiFigli = new LinkedList<>();
            figliDeiFigli.addAll(figlio.getChild());//aggiunto tutte le sotto unità di quella unita
            figliPresenti.put(figlio, figliDeiFigli);
            trovaFigliUlteriori(figlio.getChild(), figliPresenti);
        }
        return new PannelloDisegnoMemento(figliPresenti);
    }

    private void trovaFigliUlteriori(Collection<OrganigrammaElement> figli, LinkedHashMap<OrganigrammaElement, LinkedList<OrganigrammaElement>> figliPresenti) {
        for (OrganigrammaElement elem : figli) {
            figliPresenti.put(elem, new LinkedList<>());//COSI IN OGNI CASO INSERISCO IL FIGLIO ANCHE SE DOVESSE ESSERE FOGLIA
            if (!elem.getChild().isEmpty()) {
                Collection<OrganigrammaElement> listaFigli = elem.getChild();
                LinkedList<OrganigrammaElement> figliDeiFigli = new LinkedList<>();
                figliDeiFigli.addAll(elem.getChild());
                figliPresenti.put(elem, figliDeiFigli);
                trovaFigliUlteriori(listaFigli, figliPresenti);
            }
        }
    }

    public void ripristinaMemento(Memento memento) {
        if(memento instanceof PannelloDisegnoMemento pannellomemento){
        unitaDisegnate.clear();
        puntiOccupati.clear();
        for (Map.Entry entry : pannellomemento.getStato().entrySet()) {
            OrganigrammaElement padre = (OrganigrammaElement) entry.getKey();
            padre.rimuoviFigliTutti();
            padre.addListener(this);
            if (!unitaDisegnate.contains(padre)) {
                unitaDisegnate.add(padre);//magari l'ho aggiunto con la addChiild o magari no, quindi controllo
            }
            LinkedList<OrganigrammaElement> figli = (LinkedList<OrganigrammaElement>) entry.getValue();
            for (OrganigrammaElement elem : figli) {
                try {
                    padre.addChild(elem);
                } catch (FiglioUnitaNonValidoException | SubjectSenzaListenerInAscoltoException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        this.repaint();
        }
    }


    //visibilità package cosi lo accedo in popupmenu
    OrganigrammaElement trovaPuntoCliccato(int x, int y) {
        OrganigrammaElement ret = null;
        for (Map.Entry entry : puntiOccupati.entrySet()) {
            Point punto = (Point) entry.getValue();
            //CAMBIARE QUI METTENDO VALORE RETTANFOLO VARIABILE E NON 150 O 50
            if ((x >= punto.x && y >= punto.y) && (x <= punto.x + 150 && y <= punto.y + 50)) {
                ret = (OrganigrammaElement) entry.getKey();
            }
        }
        return ret;
    }

    public LinkedList<OrganigrammaElement> getUnitaDisegnate() {
        return unitaDisegnate;
    }

    @Override
    public Dimension preferredSize() {
        return new Dimension(maxLarg, maxAlt);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!unitaDisegnate.isEmpty()) {
            Graphics2D g2 = (Graphics2D) g;
            OrganigrammaElement orgGest = unitaDisegnate.getFirst();//FAI IN MODO CHE AGGIUNGA SEMPRE PER PRIMO QUESTO OPPURE IMPL != QUI
            //CERCO PUNTO CENTRALE FINESTRA PER DISEGNARE RETTANGOLO
            int rettX = (getWidth() - 150) / 2; //dove 150 è dim rett
            puntiOccupati.clear();
            disegnaTutto(g2, orgGest, rettX, 60);
        }
        revalidate();//per fargli aggiornare dimensioni e garantire di aggiungere scrollBar se serve
    }

    private void disegnaTutto(Graphics2D g, OrganigrammaElement orgGest, int x, int y) {
        int rettLarg = 150;
        int rettAlt = 50;
        int spazioTraRettOri = 60;
        int spazioTraRettVer = 60;
        g.drawRect(x, y, rettLarg, rettAlt);
        puntiOccupati.put(orgGest, new Point(x, y));//aggiungo punto del rett disegnato

        Font font = g.getFont();
        Font ridimensionato = fontRimpicciolito(g, orgGest.getNome(), rettLarg - 10);
        g.setFont(ridimensionato);
        FontMetrics metrics = g.getFontMetrics(ridimensionato);//prendo caratteristiche del testo che inserisco sul panel
        int textX = x + (rettLarg - metrics.stringWidth(orgGest.getNome())) / 2; //cosi trovo la x dove mettere testo, in pratica la sto centrando in base alla lunghezza nel rettangolo lasciando da ambo i lati stessa distanza con rettangolo
        int textY = y + ((rettAlt - metrics.getHeight()) / 2) + metrics.getAscent();

        g.drawString(orgGest.getNome(), textX, textY);
        maxLarg = Math.max(maxLarg, x + rettLarg);
        maxAlt = Math.max(maxAlt, y + rettAlt);

        if (!orgGest.getChild().isEmpty()) {
            int spazioOccupatoDaiFigli = calcolaSpazioOccupato(orgGest, rettLarg, spazioTraRettOri);
            int startX = x + (rettLarg - spazioOccupatoDaiFigli) / 2;//rettLarg-spazioOcc perche se non avessi figli spazioOcc è rettLarg quindi disegno in X, sotto mio padre
            int centroX = x + rettLarg / 2;
            int bottomY = y + rettAlt;

            int lineaOrizzY = bottomY + spazioTraRettVer / 2;//coordinta y della mia linea
//            g.drawLine(centroX,bottomY,centroX,lineaOrizzY); //linea dal mio rett attuale in giu

            int i = 0;
            Point coordPrimoFiglio = null;
            Point coordultimoFiglio = null;
            for (OrganigrammaElement unita : orgGest.getChild()) {//SO CHE AVRO SOLO UNITAGESTIONE
                //int spazioOccupatoFiglio=calcolaSpazioOccupato(unita,rettLarg,spazioTraRettOri);
                int childX = startX;
                int childY = y + rettAlt + spazioTraRettVer;

                if (childX < 0) {
                    childX = 0;
                }
                while (collisione(childX, childY) != 0) {//controllo se c'è collisione tra i rettangoli, restiuisco 0 se non c'è, la distanza da usare per staccarli (mi serve solo per le x, la y rimane quella) se invece ci fosse
                    childX = collisione(childX, childY) + spazioTraRettOri;//restituisco punto piu a dx della collisione quindi bordo rettangolo e ci aggiungo spazioOriz
                }

                int figlioCentrX = childX + rettLarg / 2;
//                g.drawLine(figlioCentrX,lineaOrizzY,figlioCentrX,lineaOrizzY+spazioTraRettVer/2);

                disegnaTutto(g, unita, childX, childY);

                //startX=spazioOccupatoFiglio+spazioTraRettOri;
                if (i == 0) {
                    coordPrimoFiglio = new Point(childX + rettLarg / 2, childY);
                }
                if (i == orgGest.getChildCount() - 1) {
                    coordultimoFiglio = new Point(childX + rettLarg / 2, childY);
                }
                g.drawLine(x + rettLarg / 2, y + rettAlt, childX + rettLarg / 2, childY);
                i++;
            }
            if (coordPrimoFiglio != null && coordultimoFiglio != null) {
//               g.drawLine(coordPrimoFiglio.x,lineaOrizzY,coordultimoFiglio.x,lineaOrizzY);
            }
//            g.drawLine(coordPrimoFiglio.x,lineaOrizzY,x+rettLarg/2,y+rettAlt+spazioTraRettVer/2);//se ci fosse un solo figlio infatti i due if disegnerebbero un singolo punto cosi no, se invece ce ne sono di piu questo non ha effetto perche comunque disegnerebbe su una linea gia fatta
        }
        g.setFont(font);//RIMETTO FONT ORIGINALE PER QUELLI DOPO SENNO DOPO CHE RIMPICCIOLISCO UNO ME LI RIMPICCIOLISCE TUTTI
    }

    //qua passare larghezza come parametro visto che per prova avevo messo valore, cosi se cambiera mai non devo andare a cercare tutto nel codice
    private int collisione(int childX, int childY) {
        for (Point punto : puntiOccupati.values()) {
            //eliminato da 2 partentesi && collisione(childX,childY)
            if (childY == punto.y && ((childX >= punto.x && childX <= punto.x + 150) || (punto.x >= childX) || (childX - (punto.x + 150) < 60))) {
                return punto.x + 150;
            }
        }
        return 0;
    }

    private Font fontRimpicciolito(Graphics g, String testo, int maxLarg) {
        Font originale = g.getFont();
        FontMetrics metrics = g.getFontMetrics(originale);
        int largTesto = metrics.stringWidth(testo);

        while (largTesto > maxLarg) {
            Font font = g.getFont();
            float nuovaDim = font.getSize() - 1;
            if (nuovaDim <= 0) {
                break;
            }
            Font nuovoFont = font.deriveFont(nuovaDim);
            g.setFont(nuovoFont);
            metrics = g.getFontMetrics(nuovoFont);
            largTesto = metrics.stringWidth(testo);
        }
        return g.getFont();
    }

//    private int calcolaSpazioOccupato(OrganigrammaElement elem,int rettLarg, int spazio){
//        //qua elimino per prova (elem instanceof SottoUnitaOrganizzativa)||
//        //per albero infinito
//        if(elem.getChild().isEmpty()){
//            return rettLarg;
//        }
//        int spazioOccupato=0;
//        for(OrganigrammaElement figlio : elem.getChild()){
//            spazioOccupato+=calcolaSpazioOccupato(figlio,rettLarg,spazio)+spazio;
//        }
//        return spazioOccupato-spazio;
//    }

    private int calcolaSpazioOccupato(OrganigrammaElement elem, int rettLarg, int spazio) {
        //qua elimino per prova (elem instanceof SottoUnitaOrganizzativa)||
        //per albero infinito
        if (elem.getChild().isEmpty()) {
            return rettLarg;
        }
        int spazioOccupato = 0;
        for (OrganigrammaElement figlio : elem.getChild()) {
            spazioOccupato += rettLarg + spazio;
        }
        return spazioOccupato - spazio;
    }

    @Override
    public void inseritoFiglio(OrganigrammaElement padre, OrganigrammaElement figlio) {
        unitaDisegnate.add(figlio);
        this.repaint();
    }

    @Override
    public void rimossoFiglio(OrganigrammaElement figlio) {
        unitaDisegnate.remove(figlio);
        puntiOccupati.remove(figlio);//NON occupa piu quei punti
        this.repaint();
    }

    //MAGARI NON CHIAMO QUESTO MA FACCIO CREATOoRGANOGESTIONE() NEL ORGANOGEST CHE
    //CHIAMA MEDIATOR CREATOORGANO() CHE AGGIUNGO E CHE A SUA VOLTA FA QUESTO
    public void aggiungiUnita(OrganigrammaElement unita) {
        unitaDisegnate.add(unita);
        this.repaint();
    }

    private class PannelloDisegnoMemento implements Memento, Serializable {
        private LinkedHashMap<OrganigrammaElement,LinkedList<OrganigrammaElement>> figliPresenti=new LinkedHashMap<>();

        public PannelloDisegnoMemento(LinkedHashMap<OrganigrammaElement,LinkedList<OrganigrammaElement>> figli) {
            this.figliPresenti=figli;
        }

        public LinkedHashMap<OrganigrammaElement, LinkedList<OrganigrammaElement>> getStato() {
            return figliPresenti;
        }
    }
}