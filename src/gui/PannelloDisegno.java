package gui;

import command.*;
import observer.CambiamentoUnitaListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import composite.OrganigrammaElement;

public class PannelloDisegno extends JPanel implements CambiamentoUnitaListener {
    private final LinkedList<OrganigrammaElement> unitaDisegnate = new LinkedList<>();
    private final HashMap<OrganigrammaElement,Point> puntiOccupati=new HashMap<>();
    private int maxLarg,maxAlt=800;
    private JPopupMenu popupMenuClickMouse;
    private OrganigrammaElement cliccato=null;
    private CommandHandler cmdHandler=new HistoryCommandHandler();

    public PannelloDisegno() {
        setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(2000, 2000));
        repaint();
        popupMenuClickMouse = new JPopupMenu();
        inizializzaPopupMenu();
    }

    private void inizializzaPopupMenu() {

        JMenuItem menuItem1=new JMenuItem("Aggiungi ruolo");
        JMenuItem menuItem2=new JMenuItem("Rimuovi ruolo");
        JMenuItem menuItem3=new JMenuItem("Aggiungi dipendente");
        JMenuItem menuItem4=new JMenuItem("Rimuovi dipendente");
        JMenuItem menuItem5=new JMenuItem("Cambia Ruolo Dipendente");
        JMenuItem menuItem6=new JMenuItem("Lista dipendenti");
        JMenuItem menuItem7=new JMenuItem("Lista ruoli");
        JMenuItem menuItem8=new JMenuItem("Lista personale");
        menuItem1.addActionListener(e->cmdHandler.handleCommand(new AggiungiRuoloCommand(this,cliccato)));
        menuItem2.addActionListener(e->cmdHandler.handleCommand(new RimuoviRuoloCommand(this,cliccato)));
        menuItem3.addActionListener(e-> cmdHandler.handleCommand(new AggiungiDipendenteCommand(this,cliccato)));
        menuItem4.addActionListener(e-> cmdHandler.handleCommand(new RimuoviDipendenteCommand(this,cliccato)));
        menuItem5.addActionListener(e-> cmdHandler.handleCommand(new CambiaRuoloUtente(this,cliccato)));
        menuItem6.addActionListener(e-> cmdHandler.handleCommand(new VisualizzaDipendentiCommand(this,cliccato)));
        menuItem7.addActionListener(e->cmdHandler.handleCommand(new VisualizzaRuoliCommand(cliccato,this)));
        menuItem8.addActionListener(e-> cmdHandler.handleCommand(new VisualizzaPersonaleCommand(this,cliccato)));
        popupMenuClickMouse.add(menuItem1);
        popupMenuClickMouse.add(menuItem2);
        popupMenuClickMouse.add(menuItem3);
        popupMenuClickMouse.add(menuItem4);
        popupMenuClickMouse.add(menuItem5);
        popupMenuClickMouse.add(menuItem6);
        popupMenuClickMouse.add(menuItem7);
        popupMenuClickMouse.add(menuItem8);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                OrganigrammaElement elementoCliccato=trovaPuntoCliccato(e.getX(),e.getY());
                if(e.getButton()==MouseEvent.BUTTON3 && elementoCliccato!=null){
                    mostraPopupMenu(e.getX(),e.getY());
                    cliccato=elementoCliccato;
                }
            }
        });
    }

    private OrganigrammaElement trovaPuntoCliccato(int x,int y){
        OrganigrammaElement ret=null;
        for(Map.Entry entry:puntiOccupati.entrySet()){
                Point punto=(Point)entry.getValue();
                //CAMBIARE QUI METTENDO VALORE RETTANFOLO VARIABILE E NON 150 O 50
                if((x>=punto.x && y>=punto.y)&&(x<=punto.x+150 && y<=punto.y+50)){
                    ret=(OrganigrammaElement)entry.getKey();
                }
        }
        return ret;
    }

    private void mostraPopupMenu(int x,int y){
        popupMenuClickMouse.show(this,x,y);
    }

    public LinkedList<OrganigrammaElement> getUnitaDisegnate() {
        return unitaDisegnate;
    }

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
        revalidate();//per fargli aggiornare dimensioni e garantire di aggiungere scrollBar se serve
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

        if(!orgGest.getChild().isEmpty()) {
            int spazioOccupatoDaiFigli=calcolaSpazioOccupato(orgGest,rettLarg,spazioTraRettOri);
            int startX = x +(rettLarg-spazioOccupatoDaiFigli)/2;//rettLarg-spazioOcc perche se non avessi figli spazioOcc è rettLarg quindi disegno in X, sotto mio padre
            int centroX=x+rettLarg/2;
            int bottomY=y+rettAlt;

            int lineaOrizzY=bottomY+spazioTraRettVer/2;//coordinta y della mia linea
//            g.drawLine(centroX,bottomY,centroX,lineaOrizzY); //linea dal mio rett attuale in giu

            int i=0;
            Point coordPrimoFiglio=null;
            Point coordultimoFiglio=null;
            for(OrganigrammaElement unita : orgGest.getChild()) {//SO CHE AVRO SOLO UNITAGESTIONE
                //int spazioOccupatoFiglio=calcolaSpazioOccupato(unita,rettLarg,spazioTraRettOri);
                int childX=startX;
                int childY = y + rettAlt + spazioTraRettVer;

                if(childX<0){
                    childX=0;
                }
                while(collisione(childX,childY)!=0){//controllo se c'è collisione tra i rettangoli, restiuisco 0 se non c'è, la distanza da usare per staccarli (mi serve solo per le x, la y rimane quella) se invece ci fosse
                    childX=collisione(childX,childY)+spazioTraRettOri;//restituisco punto piu a dx della collisione quindi bordo rettangolo e ci aggiungo spazioOriz
                }

                int figlioCentrX=childX+rettLarg/2;
//                g.drawLine(figlioCentrX,lineaOrizzY,figlioCentrX,lineaOrizzY+spazioTraRettVer/2);

                disegnaTutto(g,unita,childX,childY);

                //startX=spazioOccupatoFiglio+spazioTraRettOri;
                if(i==0){
                    coordPrimoFiglio=new Point(childX+rettLarg/2,childY);
                }
                if(i==orgGest.getChildCount()-1){
                    coordultimoFiglio=new Point(childX+rettLarg/2,childY);
                }
                g.drawLine(x + rettLarg / 2, y + rettAlt, childX + rettLarg / 2, childY);
                i++;
            }
            if(coordPrimoFiglio!=null && coordultimoFiglio!=null) {
//               g.drawLine(coordPrimoFiglio.x,lineaOrizzY,coordultimoFiglio.x,lineaOrizzY);
            }
//            g.drawLine(coordPrimoFiglio.x,lineaOrizzY,x+rettLarg/2,y+rettAlt+spazioTraRettVer/2);//se ci fosse un solo figlio infatti i due if disegnerebbero un singolo punto cosi no, se invece ce ne sono di piu questo non ha effetto perche comunque disegnerebbe su una linea gia fatta
        }
    }



    //qua passare larghezza come parametro visto che per prova avevo messo valore, cosi se cambiera mai non devo andare a cercare tutto nel codice
    private int collisione(int childX,int childY){
        for(Point punto : puntiOccupati.values()){
            //eliminato da 2 partentesi && collisione(childX,childY)
            if(childY==punto.y && ((childX>=punto.x && childX<=punto.x+150)||(punto.x>=childX) ||(childX- (punto.x+150)<60))){
                return punto.x+150;
            }
        }
        return 0;
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

    private int calcolaSpazioOccupato(OrganigrammaElement elem,int rettLarg, int spazio){
        //qua elimino per prova (elem instanceof SottoUnitaOrganizzativa)||
        //per albero infinito
        if(elem.getChild().isEmpty()){
            return rettLarg;
        }
        int spazioOccupato=0;
        for(OrganigrammaElement figlio : elem.getChild()){
            spazioOccupato+=rettLarg+spazio;
        }
        return spazioOccupato-spazio;
    }

//    @Override
//    public void ruoloCambiato(Ruolo r, LinkedList<Dipendente> dipendenti) {
//        for(Dipendente d:dipendenti){
//            cmdHandler.handleCommand(new CambiaRuoloUtente(this,d,cliccato,r));
//        }
//    }

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


    //MAGARI NON CHIAMO QUESTO MA FACCIO CREATOoRGANOGESTIONE() NEL ORGANOGEST CHE
    //CHIAMA MEDIATOR CREATOORGANO() CHE AGGIUNGO E CHE A SUA VOLTA FA QUESTO
    public void aggiungiUnita(OrganigrammaElement unita) {
        unitaDisegnate.add(unita);
        repaint();
    }
}