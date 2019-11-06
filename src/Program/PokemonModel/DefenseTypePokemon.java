package Program.PokemonModel;

public class DefenseTypePokemon extends PokemonBase {
    private int defenseAttribute;

    public DefenseTypePokemon(String name) {
        super(name);
        this.defenseAttribute = this.generateInt(1,3);
    }

    @Override
    public void defense(int receivedAttackPoint) {
        int resistance = this.flipCoinIsHead()?this.defenseAttribute:0;
        receivedAttackPoint -= resistance;

        super.defense(receivedAttackPoint);
    }

    @Override
    public void stageIncrease() {
        super.stageIncrease();
    }

    public int getDefenseAttribute() {
        return defenseAttribute;
    }

    public void setDefenseAttribute(int defenseAttribute) {
        this.defenseAttribute = defenseAttribute;
    }
}
