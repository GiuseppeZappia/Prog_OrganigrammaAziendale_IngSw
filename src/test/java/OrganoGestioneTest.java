import composite.OrganoGestione;
import composite.UnitaOrganizzativa;
import composite.utilities.Dipendente;
import composite.utilities.Ruolo;
import exceptions.*;
import gui.PannelloDisegno;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class OrganoGestioneTest {

    private OrganoGestione organoGestione;

    @BeforeAll
    public static void setupAll() {
        System.out.println("-------TEST INIZIATI-------");
    }

    @BeforeEach
    public void aggiorna() {
        System.out.println("NUOVO ORGANO DI GESTIONE");
        organoGestione=new OrganoGestione("Organo test");
    }

    @Test
    @DisplayName("Dovrebbe lanciare eccezione SubjectSenzaListener")
    public void lanciaEccezioneNoListener()  {
        Assertions.assertThrows(SubjectSenzaListenerInAscoltoException.class, ()->{
            organoGestione.addChild(new UnitaOrganizzativa("FiglioProva"));});
    }

    @Test
    @DisplayName("Dovrebbe lanciare eccezione FiglioNonValido")
    public void lanciaEccezioneFiglioSbagliato()  {
        Assertions.assertThrows(FiglioUnitaNonValidoException.class, ()->{
            organoGestione.addChild(new OrganoGestione("FiglioSbagliato"));});
    }

    @DisplayName("Dovrebbe aggiungere figlio")
    @RepeatedTest(value = 5, name = "Ripeto Aggiunta Figlio Test {currentRepetition} di {totalRepetitions}")
    public void aggiungiFiglio() throws FiglioUnitaNonValidoException, SubjectSenzaListenerInAscoltoException {
        organoGestione.addListener(new PannelloDisegno());//aggiungo un lis
        organoGestione.addChild(new UnitaOrganizzativa("figlioTest"));
        assertFalse(organoGestione.getChild().isEmpty());
        assertEquals(1, organoGestione.getChild().size());
    }

    @Test
    @DisplayName("Dovrebbe rimuovere figlio")
    public void rimuoviFiglio() throws FiglioUnitaNonValidoException, SubjectSenzaListenerInAscoltoException,
            FiglioNonPresenteInQuestaUnitaException {
        UnitaOrganizzativa figlio=new UnitaOrganizzativa("figlioTest");
        organoGestione.addListener(new PannelloDisegno());//aggiungo un lis
        organoGestione.addChild(figlio);
        organoGestione.removeChild(figlio);
        assertTrue(organoGestione.getChild().isEmpty());
        assertEquals(0, organoGestione.getChild().size());
    }


    @Test
    @DisplayName("Dovrebbe aggiungere dipendente")
    public void aggiungiDipendente() throws DipendenteGiaEsistenteException {
        Dipendente dipendente= new Dipendente("Theo Bernard Francois","Hernandez",
                "Marsiglia","Milanello",19);
        organoGestione.addDipendente(dipendente);
        assertFalse(organoGestione.getDipendenti().isEmpty());
        assertEquals(1, organoGestione.getDipendenti().size());
    }

    @Test
    @DisplayName("Dovrebbe aggiungere ruolo")
    public void aggiungiRuolo() throws RuoloGiaPresenteNellUnitaException {
        Ruolo ruolo= new Ruolo("Terzino","Terzino sinistro offensivo",
                "Velocità, strapotenza fisica, tecnica",1899);
        organoGestione.addRuolo(ruolo);
        assertFalse(organoGestione.getRuoliDisponibili().isEmpty());
        assertEquals(1, organoGestione.getRuoliDisponibili().size());
    }

    @ParameterizedTest
    @DisplayName("Dovrebbe aggiungere ruolo da Lista")
    @MethodSource("getRuoli")
    public void aggiungiRuoloDaLista(Ruolo ruolo) throws RuoloGiaPresenteNellUnitaException {
        organoGestione.addRuolo(ruolo);
        assertFalse(organoGestione.getRuoliDisponibili().isEmpty());
        assertEquals(1, organoGestione.getRuoliDisponibili().size());
    }
    static Ruolo[] getRuoli() {
        // Crea gli oggetti Ruolo da testare
        Ruolo ruolo1= new Ruolo("Terzino","Terzino sinistro offensivo",
                "Velocità, strapotenza fisica, tecnica",1899);
        Ruolo ruolo2= new Ruolo("Esterno","Esterno sinistro offensivo",
                "Piede invertito,Velocità, strapotenza fisica, tecnica",1899);
        return new Ruolo[]{ruolo1, ruolo2};
    }
    @Test
    @DisplayName("Dovrebbe rimuovere ruolo")
    public void rimuoviRuolo() throws RuoloGiaPresenteNellUnitaException, RuoloNonPresenteNellUnitaException, SubjectSenzaListenerInAscoltoException {
        Ruolo ruolo= new Ruolo("Terzino","Terzino sinistro offensivo",
                "Velocità, strapotenza fisica, tecnica",1899);
        organoGestione.addRuolo(ruolo);
        organoGestione.removeRuolo(ruolo);
        assertTrue(organoGestione.getRuoliDisponibili().isEmpty());
        assertEquals(0, organoGestione.getRuoliDisponibili().size());
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
