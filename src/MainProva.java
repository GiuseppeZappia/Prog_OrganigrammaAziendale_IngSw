import Composite.OrganigrammaElement;
import Composite.OrganoGestione;
import Composite.SottoUnitaOrganizzativa;
import Composite.UnitaOrganizzativa;
import Exceptions.*;
import Mediator.ChangeManagerMediator;
import Mediator.ConcreteChangheManagerMediator;
import Utilities.Dipendente;
import Utilities.Ruolo;

public class MainProva {

    public static void main(String[] args) throws DipendenteGiaEsistenteException, RuoloGiaPresenteNellUnitaException, DipendenteNonPresenteNellUnitaException, RuoloNonPresenteNellUnitaException, FiglioUnitaNonValidoException, SubjectSenzaListenerInAscoltoException, FiglioNonPresenteInQuestaUnitaException {
        ChangeManagerMediator mediator = new ConcreteChangheManagerMediator();
        OrganoGestione consiglioDiAmministrazione=new OrganoGestione("CONSIGLIO DI AMMINISTRAZIONE",mediator);
        OrganigrammaElement comitTecnScient=new UnitaOrganizzativa("COMITATO TECNICO SCIENTIFICO",mediator);
        OrganigrammaElement acquisiti=new UnitaOrganizzativa("ACQUISITI",mediator);
        OrganigrammaElement produzione=new UnitaOrganizzativa("PRODUZIONE",mediator);
        OrganigrammaElement areaVendite=new UnitaOrganizzativa("AREA VENDITE",mediator);
        OrganigrammaElement ricercaEsviluppo=new SottoUnitaOrganizzativa("RICERCA E SVILUPPO",mediator);
        OrganigrammaElement marketing=new SottoUnitaOrganizzativa("MARKETING",mediator);
        OrganigrammaElement custoreCare=new SottoUnitaOrganizzativa("CUSTOMERE CARE",mediator);

        consiglioDiAmministrazione.addChild(comitTecnScient);
        consiglioDiAmministrazione.addChild(acquisiti);
        consiglioDiAmministrazione.addChild(produzione);
        consiglioDiAmministrazione.addChild(areaVendite);
        comitTecnScient.addChild(ricercaEsviluppo);
        areaVendite.addChild(marketing);
        areaVendite.addChild(custoreCare);

        System.out.println("**************PRIMA****************");

        System.out.println("FIGLI DI CONSIGLIO AMMINISTRAZIONE: "+consiglioDiAmministrazione.stampaFigli());
        System.out.println("FIGLI DI COMITATO TECNICO SCIENT: "+comitTecnScient.stampaFigli());
        System.out.println("FIGLI DI ACQUISTI: "+acquisiti.stampaFigli());
        System.out.println("FIGLI DI PRODUZIONE: "+produzione.stampaFigli());
        System.out.println("FIGLI DI AREAVENDITE: "+areaVendite.stampaFigli());

        System.out.println("***************RIMOZIONE SOLO DI AREA VENDITE E CTS***************");
        consiglioDiAmministrazione.removeChild(areaVendite);
        consiglioDiAmministrazione.removeChild(comitTecnScient);
        System.out.println("FIGLI DI CONSIGLIO AMMINISTRAZIONE: "+consiglioDiAmministrazione.stampaFigli());
        System.out.println("FIGLI DI COMITATO TECNICO SCIENT: "+comitTecnScient.stampaFigli());
        System.out.println("FIGLI DI ACQUISTI: "+acquisiti.stampaFigli());
        System.out.println("FIGLI DI PRODUZIONE: "+produzione.stampaFigli());
        System.out.println("FIGLI DI AREAVENDITE: "+areaVendite.stampaFigli());


        consiglioDiAmministrazione.rimuoviTutto();
        System.out.println("**************DOPO****************");
        System.out.println("FIGLI DI CONSIGLIO AMMINISTRAZIONE: "+consiglioDiAmministrazione.stampaFigli());
        System.out.println("FIGLI DI COMITATO TECNICO SCIENT: "+comitTecnScient.stampaFigli());
        System.out.println("FIGLI DI ACQUISTI: "+acquisiti.stampaFigli());
        System.out.println("FIGLI DI PRODUZIONE: "+produzione.stampaFigli());
        System.out.println("FIGLI DI AREAVENDITE: "+areaVendite.stampaFigli());


    }
}
