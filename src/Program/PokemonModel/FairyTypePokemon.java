package Program.PokemonModel;

/**
 * fairy type pokemon which extends from pokemon base
 */
public class FairyTypePokemon extends PokemonBase {

    /**
     * @param name the name of the pokemon
     */
    public FairyTypePokemon(String name) {
        super(name);
    }

    /**
     * @param target the target pokemon class
     * @return a log of all actions performed
     */
    public String fairyTypeLaunchAttack(PokemonBase target) {

        String returnString = "";
        returnString += super.launchAttack(target);

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
