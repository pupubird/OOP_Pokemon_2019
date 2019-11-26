package Program.PokemonModel;

public class DefenseTypePokemon extends PokemonBase {

    private int resistancePoints;

    public DefenseTypePokemon(String name) {
        super(name);
        this.resistancePoints = this.generateInt(5,10);
    }

    String defenseTypeLaunchDefense(int receivedAttackPoint, int resistance) {

        resistance = flipCoinIsHead() ? resistance :0;
        receivedAttackPoint -= receivedAttackPoint - resistance < 0 ? receivedAttackPoint : resistance;

        String returnString = "";
        returnString += defense(receivedAttackPoint);

        if ( resistance == 0 ) {
            return returnString + "\n" + "Flipped Coin: Tail, Defense Type Pokemon Effect Canceled";
        }


        return returnString + "\n" + "Flipped Coin: Head, Defense Type Pokemon Effect Triggered!";

    }


    @Override
    public String defense(int receivedAttackPoint) {
        return super.defense(receivedAttackPoint);
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
