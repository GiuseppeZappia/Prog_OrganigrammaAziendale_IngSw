package gui;

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
    private int maxLarg = this.getWidth();
    private int maxAlt = this.getHeight();
    private int rettLarg = 150;
    private int rettAlt = 50;
    int spazioTraRettOri = 60;
    int spazioTraRettVer = 60;

    public PannelloDisegno() {
        setBackground(Color.WHITE);
        repaint();
    }

    public PannelloDisegnoMemento creaMemento() {
        LinkedHashMap<OrganigrammaElement, LinkedList<OrganigrammaElement>> figliPresenti = new LinkedHashMap<>();
        if (!unitaDisegnate.isEmpty()) {
            OrganoGestione orgGest = (OrganoGestione) unitaDisegnate.getFirst();
            figliPresenti.put(orgGest, new LinkedList<>(orgGest.getChild()));
            for (OrganigrammaElement figlio : orgGest.getChild()) {
                LinkedList<OrganigrammaElement> figliDeiFigli = new LinkedList<>();
                figliDeiFigli.addAll(figlio.getChild());//aggiungo tutte le sotto unità di quella unita
                figliPresenti.put(figlio, figliDeiFigli);
                trovaFigliUlteriori(figlio.getChild(), figliPresenti);
            }
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
        if (memento instanceof PannelloDisegnoMemento pannellomemento) {
            unitaDisegnate.clear();
            puntiOccupati.clear();
            for (Map.Entry entry : pannellomemento.getStato().entrySet()) {
                OrganigrammaElement padre = (OrganigrammaElement) entry.getKey();
                padre.rimuoviFigliTutti();
                padre.addListener(this);
                if (!unitaDisegnate.contains(padre)) {
                    unitaDisegnate.add(padre);//magari l'ho aggiunto con la addChild o magari no, quindi controllo
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
            if ((x >= punto.x && y >= punto.y) && (x <= punto.x + rettLarg && y <= punto.y + rettAlt)) {
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
        if (unitaDisegnate.isEmpty()) {
            return new Dimension(0, 0);
        }
        return new Dimension(maxLarg, maxAlt);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!unitaDisegnate.isEmpty()) {
            Graphics2D g2 = (Graphics2D) g;
            //FACCIO INFATTI MODO CHE IL PRIMO ELEMENTO DELLA LISTA SIA SEMPRE L'ORGANO DI GESTIONE
            OrganigrammaElement orgGest = unitaDisegnate.getFirst();
            //CERCO PUNTO CENTRALE FINESTRA PER DISEGNARE RETTANGOLO
            int rettX = (getWidth() - rettLarg) / 2; //dove 150 è dim rett
            puntiOccupati.clear();
            disegnaTutto(g2, orgGest, rettX, spazioTraRettVer);
        }
        revalidate();//per fargli aggiornare dimensioni e garantire di aggiungere scrollBar se serve
    }

    private void disegnaTutto(Graphics2D g, OrganigrammaElement orgGest, int x, int y) {
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
        maxAlt = Math.max(maxAlt, y + rettAlt + 20);

        if (!orgGest.getChild().isEmpty()) {
            int spazioOccupatoDaiFigli = calcolaSpazioOccupato(orgGest, rettLarg, spazioTraRettOri);
            //rettLarg-spazioOcc perche se non avessi figli spazioOcc è rettLarg quindi disegno in X, sotto mio padre
            int startX = x + (rettLarg - spazioOccupatoDaiFigli) / 2;

            int i = 0;
            for (OrganigrammaElement unita : orgGest.getChild()) {//SO CHE AVRO SOLO UNITAGESTIONE
                int childX = startX;
                int childY = y + rettAlt + spazioTraRettVer;

                if (childX < 0) {
                    childX = 0;
                }
                while (collisione(childX, childY) != 0) {//controllo se c'è collisione tra i rettangoli, restiuisco 0 se non c'è, la distanza da usare per staccarli (mi serve solo per le x, la y rimane quella) se invece ci fosse
                    childX = collisione(childX, childY) + spazioTraRettOri;//restituisco punto piu a dx della collisione quindi bordo rettangolo e ci aggiungo spazioOriz
                }

                disegnaTutto(g, unita, childX, childY);

                g.drawLine(x + rettLarg / 2, y + rettAlt, childX + rettLarg / 2, childY);
                i++;
            }
        }
        g.setFont(font);//RIMETTO FONT ORIGINALE PER QUELLI DOPO SENNO DOPO CHE RIMPICCIOLISCO UNO ME LI RIMPICCIOLISCE TUTTI
    }

    private int collisione(int childX, int childY) {
        for (Point punto : puntiOccupati.values()) {
            if (childY == punto.y && ((childX >= punto.x && childX <= punto.x + rettLarg) || (punto.x >= childX) || (childX - (punto.x + rettLarg) < spazioTraRettOri))) {
                return punto.x + rettLarg;
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

    private int calcolaSpazioOccupato(OrganigrammaElement elem, int rettLarg, int spazio) {

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
    public void inseritoFiglio(OrganigrammaElement figlio) {
        unitaDisegnate.add(figlio);
        this.repaint();
    }

    @Override
    public void rimossoFiglio(OrganigrammaElement figlio) {
        unitaDisegnate.remove(figlio);
        puntiOccupati.remove(figlio);//non occupa piu quei punti
        this.repaint();
    }

    //MEMENTO PER ELIMINAZIONE PADRE E CARICARE ORGANIGRAMMA
    private static class PannelloDisegnoMemento implements Memento, Serializable {
        private static final long serialVersionUID = 1L;

        private LinkedHashMap<OrganigrammaElement, LinkedList<OrganigrammaElement>> figliPresenti;

        public PannelloDisegnoMemento(LinkedHashMap<OrganigrammaElement, LinkedList<OrganigrammaElement>> figli) {
            this.figliPresenti = figli;
        }

        public LinkedHashMap<OrganigrammaElement, LinkedList<OrganigrammaElement>> getStato() {
            return figliPresenti;
        }
    }
}