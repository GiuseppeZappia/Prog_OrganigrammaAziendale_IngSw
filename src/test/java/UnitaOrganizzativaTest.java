import composite.OrganoGestione;
import composite.UnitaOrganizzativa;
import composite.utilities.Dipendente;
import composite.utilities.Ruolo;
import exceptions.*;
import gui.PannelloDisegno;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UnitaOrganizzativaTest {
    private UnitaOrganizzativa unita;

    @BeforeAll
    public static void setupAll() {
        System.out.println("-------TEST INIZIATI-------");
    }

    @BeforeEach
    public void aggiorna() {
        System.out.println("NUOVA UNITA'");
        unita=new UnitaOrganizzativa("Unita test");
    }

    @Test
    @DisplayName("Dovrebbe lanciare eccezione SubjectSenzaListener")
    public void lanciaEccezioneNoListener()  {
        Assertions.assertThrows(SubjectSenzaListenerInAscoltoException.class, ()->{
            unita.addChild(new UnitaOrganizzativa("FiglioProva"));});
    }

    @Test
    @DisplayName("Dovrebbe lanciare eccezione FiglioNonValido")
    public void lanciaEccezioneFiglioSbagliato()  {
        Assertions.assertThrows(FiglioUnitaNonValidoException.class, ()->{
            unita.addChild(new OrganoGestione("FiglioSbagliato"));});
    }

    @Test
    @DisplayName("Dovrebbe aggiungere figlio")
    public void aggiungiFiglio() throws FiglioUnitaNonValidoException, SubjectSenzaListenerInAscoltoException {
        unita.addListener(new PannelloDisegno());//aggiungo un lis
        unita.addChild(new UnitaOrganizzativa("figlioTest"));
        assertFalse(unita.getChild().isEmpty());
        assertEquals(1, unita.getChild().size());
    }

    @Test
    @DisplayName("Dovrebbe rimuovere figlio")
    public void rimuoviFiglio() throws FiglioUnitaNonValidoException, SubjectSenzaListenerInAscoltoException, FiglioNonPresenteInQuestaUnitaException {
        UnitaOrganizzativa figlio=new UnitaOrganizzativa("figlioTest");
        unita.addListener(new PannelloDisegno());//aggiungo un lis
        unita.addChild(figlio);
        unita.removeChild(figlio);
        assertTrue(unita.getChild().isEmpty());
        assertEquals(0, unita.getChild().size());
    }


    @Test
    @DisplayName("Dovrebbe aggiungere dipendente")
    public void aggiungiDipendente() throws DipendenteGiaEsistenteException {
        Dipendente dipendente= new Dipendente("Theo Bernard Francois","Hernandez","Marsiglia","Milanello",19);
        unita.addDipendente(dipendente);
        assertFalse(unita.getDipendenti().isEmpty());
        assertEquals(1, unita.getDipendenti().size());
    }

    @Test
    @DisplayName("Dovrebbe aggiungere ruolo")
    public void aggiungiRuolo() throws RuoloGiaPresenteNellUnitaException {
        Ruolo ruolo= new Ruolo("Terzino","Terzino sinistro offensivo","Velocità, strapotenza fisica, tecnica",1899);
        unita.addRuolo(ruolo);
        assertFalse(unita.getRuoliDisponibili().isEmpty());
        assertEquals(1, unita.getRuoliDisponibili().size());
    }

    @Test
    @DisplayName("Dovrebbe rimuovere ruolo")
    public void rimuoviRuolo() throws RuoloGiaPresenteNellUnitaException, RuoloNonPresenteNellUnitaException, SubjectSenzaListenerInAscoltoException {
        Ruolo ruolo= new Ruolo("Terzino","Terzino sinistro offensivo","Velocità, strapotenza fisica, tecnica",1899);
        unita.addRuolo(ruolo);
        unita.removeRuolo(ruolo);
        assertTrue(unita.getRuoliDisponibili().isEmpty());
        assertEquals(0, unita.getRuoliDisponibili().size());
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
