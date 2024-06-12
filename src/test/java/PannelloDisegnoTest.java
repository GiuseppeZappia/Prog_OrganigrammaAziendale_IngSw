import composite.OrganoGestione;
import gui.PannelloDisegno;
import org.junit.jupiter.api.*;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

public class PannelloDisegnoTest {
    private PannelloDisegno pd;

    @BeforeAll
    public static void setupAll() {
        System.out.println("-------TEST INIZIATI-------");
    }

    @BeforeEach
    public void aggiorna() {
        System.out.println("NUOVO PANNELLO");
        pd =new PannelloDisegno();
    }

    @Test
    @DisplayName("DOVREBBE AGGIUNGERE ELEMENTO")
    public void aggiungiElemento(){
        pd.inseritoFiglio(new OrganoGestione("Organo prova"));
        assertFalse(pd.getUnitaDisegnate().isEmpty());
        assertEquals(1,pd.getUnitaDisegnate().size());
    }

    @DisplayName("DOVREBBE RIMUOVERE ELEMENTO")
    @RepeatedTest(value = 5,
            name = "Ripeto Rimozione Elemento Test {currentRepetition} di {totalRepetitions}")
    public void rimuoviElemento(){
        OrganoGestione elem=new OrganoGestione("Organo prova");
        pd.inseritoFiglio(elem);
        pd.rimossoFiglio(elem);
        assertTrue(pd.getUnitaDisegnate().isEmpty());
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
