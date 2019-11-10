package Program.PokemonModel;

public class DefenseTypePokemon extends PokemonBase {
    private int resistancePoints;

    public DefenseTypePokemon(String name) {
        super(name);
        this.resistancePoints = this.generateInt(1,3);
    }

    public String defenseTypeLaunchDefense(int receivedAttackPoint, int resistance){
        receivedAttackPoint -= resistance;
        String returnString = "";
        defense(receivedAttackPoint);
        if (resistance == 0){
            return returnString+"\n"+ "Flipped Coin: Tail, Defense Type Pokemon Effect Canceled";
        }
        return returnString+"\n"+ "Flipped Coin: Head, Defense Type Pokemon Effect Triggered!";

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
