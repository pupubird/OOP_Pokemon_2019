package Program.PokemonModel;

public class DefenseTypePokemon extends PokemonBase {
    private int resistancePoints;

    public DefenseTypePokemon(String name) {
        super(name);
        this.resistancePoints = this.generateInt(1,3);
    }

    @Override
    public void defense(int receivedAttackPoint) {
        int resistance = this.flipCoinIsHead()?this.resistancePoints :0;
        receivedAttackPoint -= resistance;

        super.defense(receivedAttackPoint);
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
