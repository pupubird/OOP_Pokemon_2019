package Program.PokemonModel;

public class FairyTypePokemon extends PokemonBase {

    public FairyTypePokemon(String name) {
        super(name);
    }

    public String fairyTypeLaunchAttack(PokemonBase target) {

        String returnString = "";
        returnString += super.launchAttack(target);;

        if ( !returnString.contains("Not enough energy.") ) {

            if ( this.flipCoinIsHead() ) {

                target.setPoisoned();
                return returnString + "\n" + target.getName() + " get poisoned! Idle for " + target.getEffectLeftRound() + " round.";

            }

            target.setParalysed();
            return returnString + "\n" + target.getName() + " get paralysed! Idle for " + target.getEffectLeftRound() + " round.";

        }

        return returnString;

    }

}
