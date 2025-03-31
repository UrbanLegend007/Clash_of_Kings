package Commands;

public class Help extends Command{

    public Help() {

    }

    @Override
    public String execute() {
        return "\n-> Travel = Muzes cestovat mezi kralovstvimi (kdyz cestujes, muzes se rozhodnout, jaka cast tve armady pujde s tebou)." +
                "\n          -> Talk = Ziskas tak informace, ktere se ti muzou hodit, nebo muzes zlepsit vztahy mezi kralovstvimi." +
                "\n          -> Trade = Muzes tak koupit ci prodat ruzne predmety, ci vytvorit primo obchodni dohody, pokud mas potrebny scroll, muzes dostat lepsi podminky, nebo kdyz mas s nimi alience." +
                "\n          -> Trade => Get (vyberes predmet, ktery chces dostat, pokud ti to kralovstvi povoli, nebo uzemi ziskas nasilim, tak ho dostanes, jinak ne)." +
                "\n          -> Negotiation -> Peace terms = Muzes se domluvit na prijatelnych podminkach, pokud je mezi vámi nějeký konflikt." +
                "\n                         -> Aliance = Muzete se s nekterymi kralovstvimi vyhodne spojit, pokud mas potrebny scroll, muzes dostat lepsi podminky." +
                "\n                         -> Attack = Muzete vyhlasit nekteremu kralovstvi valku a prerusit tak veskere aliance a obchody." +
                "\n\n-> Maintain = Muzes vylepsit armadu." +
                "\n            -> Gear => Metal (vyberes kov, kterym chces vylepsit vybaveni, cim vzacnejsi, tim lepsi)." +
                "\n            -> Properties => Resources (vyberes zdroje, kterymi chces vylepsit vlastnosti, cim vzacnejsi, tim lepsi)." +
                "\n\n-> Control Army = Muzes ovladat armadu na bitevnim poli, nebo pouze na mape, aniz bys ty sam musel nekam chodit (obvykle v pripade valky)." +
                "\n                -> Attack = Zautocis na fortress nepritele." +
                "\n                -> Use = Vyberes a pouzijes krystal, ktery ubere silu danemu fortress." +
                "\n\nToto jsou vsechny prikazy, ktere muzes provest.";
    }

    @Override
    public boolean exit() {
        return false;
    }
}
