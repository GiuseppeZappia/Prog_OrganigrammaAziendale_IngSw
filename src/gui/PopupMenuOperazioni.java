package gui;

import command.*;
import composite.OrganigrammaElement;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

public class PopupMenuOperazioni implements Serializable {
    private final JPopupMenu popupMenu;
    private OrganigrammaElement cliccato = null;
    private final CommandHandler cmdHandler;
    private final PannelloDisegno pd;

    public PopupMenuOperazioni(PannelloDisegno pd, CommandHandler cmdHandler) {
        this.pd = pd;
        this.cmdHandler = cmdHandler;
        popupMenu = new JPopupMenu();
        inizializza();
        addMouseListener();
    }

    private void inizializza() {
        JMenuItem menuItem1 = new JMenuItem("Aggiungi ruolo");
        JMenuItem menuItem2 = new JMenuItem("Rimuovi ruolo");
        JMenuItem menuItem3 = new JMenuItem("Aggiungi dipendente");
        JMenuItem menuItem4 = new JMenuItem("Rimuovi dipendente");
        JMenuItem menuItem5 = new JMenuItem("Cambia Ruolo Dipendente");
        JMenuItem menuItem6 = new JMenuItem("Lista dipendenti");
        JMenuItem menuItem7 = new JMenuItem("Lista ruoli");
        JMenuItem menuItem8 = new JMenuItem("Lista personale");
        menuItem1.addActionListener(e -> cmdHandler.handleCommand(new AggiungiRuoloCommand(pd, cliccato)));
        menuItem2.addActionListener(e -> cmdHandler.handleCommand(new RimuoviRuoloCommand(pd, cliccato)));
        menuItem3.addActionListener(e -> cmdHandler.handleCommand(new AggiungiDipendenteCommand(pd, cliccato)));
        menuItem4.addActionListener(e -> cmdHandler.handleCommand(new RimuoviDipendenteCommand(pd, cliccato)));
        menuItem5.addActionListener(e -> cmdHandler.handleCommand(new CambiaRuoloUtente(pd, cliccato)));
        menuItem6.addActionListener(e -> cmdHandler.handleCommand(new VisualizzaDipendentiUnitaCommand(pd, cliccato)));
        menuItem7.addActionListener(e -> cmdHandler.handleCommand(new VisualizzaRuoliUnitaCommand(cliccato, pd)));
        menuItem8.addActionListener(e -> cmdHandler.handleCommand(new VisualizzaPersonaleUnitaCommand(pd, cliccato)));
        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);
        popupMenu.add(menuItem3);
        popupMenu.add(menuItem4);
        popupMenu.add(menuItem5);
        popupMenu.add(menuItem6);
        popupMenu.add(menuItem7);
        popupMenu.add(menuItem8);
    }

    private void addMouseListener() {
        pd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                OrganigrammaElement elementoCliccato = pd.trovaPuntoCliccato(e.getX(), e.getY());
                if (e.getButton() == MouseEvent.BUTTON3 && elementoCliccato != null) {
                    mostraPopupMenu(e.getX(), e.getY());
                    cliccato = elementoCliccato;
                }
            }
        });
    }

    private void mostraPopupMenu(int x, int y) {
        popupMenu.show(pd, x, y);
    }


}
