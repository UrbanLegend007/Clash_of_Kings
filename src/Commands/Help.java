package Commands;

public class Help extends Command{

    public Help() {

    }

    @Override
    public String execute() {
        System.out.println("\n-> Travel = Muzes cestovat mezi kralovstvimi (kdyz cestujes, muzes se rozhodnout, jaka cast tve armady pujde s tebou)");
        System.out.println("          -> Talk = Ziskas tak informace, ktere se ti muzou hodit, nebo muzes zlepsit vztahy mezi kralovstvimi");
        System.out.println("          -> Trade = Muzes tak koupit ci prodat ruzne predmety, ci vytvorit primo obchodni dohody, pokud mas potrebny scroll, muzes dostat lepsi podminky");
        System.out.println("          -> Negotiation -> Peace terms = Muzes se domluvit na prijatelnych podminkach, pokud je mezi vámi nějeký konflikt");
        System.out.println("                         -> Aliance = Muzete se s nekterymi kralovstvimi vyhodne spojit, pokud mas potrebny scroll, muzes dostat lepsi podminky");
        System.out.println("                         -> Attack = Muzete vyhlasit nekteremu kralovstvi valku a prerusit tak veskere aliance a obchody");
        System.out.println("\n-> Get = Muzes ziskat ruzne predmety, pokud se tyto predmety nachazeji v tom kralovstvi, ve kterem se nachazis i ty, ale vyuzijes armadu a chvili trva");
        System.out.println("\n-> Maintain = Muzes vylepsit armadu");
        System.out.println("            -> Gear => Metal (vyberes kov, kterym chces vylepsit vybaveni, cim vzacnejsi, tim lepsi)");
        System.out.println("            -> Properties => Resources (vyberes zdroje, kterymi chces vylepsit vlastnosti, cim vyacnejsi, tim lepsi)");
        System.out.println("\n-> Control Army = Muzes ovladat armadu na bitevnim poli, nebo pouze na mape, aniz bys ty sam musel nekam chodit (obvykle v pripade valky)");
        System.out.println("                -> Attack = Zautocis na armadu na bitevnim poli, tvoje armada bude odkrytejsi, ale rychlejsi a silnejsi");
        System.out.println("                -> Defence = Budes branit uzemi a tvoje armada bude drzet pozice, nebude se prilis hybat, ale bude vice skryta");
        System.out.println("                -> Reinforcements = Zavolas posili bud svoje vlastni armady, ktera se nachazi jinde, nebo nejakeho jineho kralovstvi, se kterym mas alience");
        System.out.println("                -> Occupy = Zaberes jistou cast uzemi, hradu nebo vyhodne pozice");
        System.out.println("                -> Retreat = Ustoupis na jinou cast uzemi, pripadne opustis bojiste uplne v pripade nouze");
        System.out.println("                -> Use = Vyberes a pouzijes krystal, ktery ti muze pomoct v boji");
        return "\nToto jsou vsechny prikazy, ktere muzes provest";
    }

    @Override
    public boolean exit() {
        return false;
    }
}
