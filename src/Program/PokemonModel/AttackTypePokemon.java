package Program.PokemonModel;

public class AttackTypePokemon extends PokemonBase{
    private int attackAttribute;

    public AttackTypePokemon(String name) {
        super(name);
        this.attackAttribute = this.generateInt(2,5);
    }

    @Override
    public String launchAttack(PokemonBase target, int attackPoint) {
        return super.launchAttack(target, attackPoint);
    }

    @Override
    public void stageIncrease() {
        this.attackAttribute *= 2;
        super.stageIncrease();
    }

    @Override
    public int getAttackPoint() {
        return attackAttribute;
    }

}
