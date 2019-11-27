package Program.PokemonModel;

/**
 * Attack type pokemon which extends from pokemon base
 */
public class AttackTypePokemon extends PokemonBase {

    /**
     * The attack attribute for attack type pokemon
     */
    private int attackAttribute;


    /**
     * @param name the name of the pokemon
     */
    public AttackTypePokemon(String name) {
        super(name);
        this.attackAttribute = this.generateInt(20, 30);
    }


    /**
     * @param target the attacking target pokemon class
     * @return the log of the results attack process
     */
    public String attackTypelaunchAttack(PokemonBase target) {
        // based on business logic
        int attackPoint = flipCoinIsHead()?this.getAttackPoint():1;
        String returnString = "";

        returnString += super.launchAttack(target,attackPoint);

        if ( !returnString.contains("Not enough energy.") ) {

            if ( attackPoint != 1 ) {
                returnString += "\n" + "Flipped coin: Head, Attack Type Pokemon Effect Triggered!";
            } else {
                returnString += "\n" + "Flipped coin: Tail, Attack Type Pokemon Effect Canceled!";
            }

        }


        return returnString;
    }


    /**
     * Override pokemon base class stage increase: attack attribute *= 2
     */
    @Override
    public void stageIncrease() {
        this.attackAttribute *= 2;
        super.stageIncrease();
    }


    /**
     * Attack point getter
     * @return attack attribute of the attack type pokemon
     */
    @Override
    public int getAttackPoint() {
        return attackAttribute;
    }


}
