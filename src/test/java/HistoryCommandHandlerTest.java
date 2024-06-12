import command.*;
import composite.OrganoGestione;
import gui.PannelloDisegno;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class HistoryCommandHandlerTest {
    private HistoryCommandHandler hcmd;

    @BeforeAll
    public static void setupAll() {
        System.out.println("-------TEST INIZIATI-------");
    }

    @BeforeEach
    public void aggiorna() {
        System.out.println("NUOVO COMMAND HANDLER");
        hcmd=new HistoryCommandHandler();
    }

    @Test
    @DisplayName("DOVREBBE AGGIUNGERE COMANDO CANCELLABILE")
    public void aggiuntaComandoCancellabile(){
        Command cmd= new DisegnaElementCommand(new PannelloDisegno(),new OrganoGestione("Organo prova"));
        hcmd.handleCommand(cmd);
        assertTrue(hcmd.getRedoList().isEmpty());
        assertFalse(hcmd.getHistory().isEmpty());
        assertEquals(1,hcmd.getHistory().size());
    }

    @Test
    @DisplayName("DOVREBBE AGGIUNGERE COMANDO NON CANCELLABILE DOPO UNO CANCELLABILE")
    public void aggiuntaComandoNonCancellabile() throws IOException {
        Command cmd= new DisegnaElementCommand(new PannelloDisegno(),new OrganoGestione("Organo prova"));
        hcmd.handleCommand(cmd);
        VisualizzaPersonaleTotaleCommand cmd2 = Mockito.mock(VisualizzaPersonaleTotaleCommand.class);
        when(cmd2.doIt()).thenReturn(false);//NON APRE LA FINESTRA NEL TEST,
        // SENNO APRIVA DIALOGO E LA DOVEVO CHIUDERE MANUALMENTE PER FARE ANDARE TEST, INVECE COSI RESTITUISCO FALSE QUANDO
        //CHIAMO QUESTO METODO
        hcmd.handleCommand(cmd2);
        assertTrue(hcmd.getHistory().isEmpty());
        assertTrue(hcmd.getHistory().isEmpty());
    }

    @AfterEach
    public void dopoIlSingolo(){
        System.out.println("SINGOLO TEST EFFETTUATO");
    }

    @AfterAll
    public static void fine(){
        System.out.println("---------TEST FINITI---------");
    }



}
