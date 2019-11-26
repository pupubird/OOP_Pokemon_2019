package Program.PokemonModel;

public class DefenseTypePokemon extends PokemonBase {

    private int resistancePoints;

    /**
     * @param name the name of the pokemon
     */
    public DefenseTypePokemon(String name) {
        super(name);
        this.resistancePoints = this.generateInt(5,10);
    }

    /**
     * @param receivedAttackPoint the received attack points from opponent pokemon
     * @return a log of all actions performed
     */
    String defenseTypeLaunchDefense(int receivedAttackPoint) {

        int resistance = flipCoinIsHead() ? this.getResistancePoints() :0;
        receivedAttackPoint -= receivedAttackPoint - resistance < 0 ? receivedAttackPoint : resistance;

        String returnString = "";
        returnString += super.defense(receivedAttackPoint);

        if ( resistance == 0 ) {
            return returnString + "\n" + "Flipped Coin: Tail, Defense Type Pokemon Effect Canceled";
        }


        return returnString + "\n" + "Flipped Coin: Head, Defense Type Pokemon Effect Triggered!";

    }



    @Override
    public void stageIncrease() {
        super.stageIncrease();
    }


    @Override
    public int getResistancePoints() {
        return resistancePoints;
    }


}
