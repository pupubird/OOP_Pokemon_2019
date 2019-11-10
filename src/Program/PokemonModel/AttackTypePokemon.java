package Program.PokemonModel;

public class AttackTypePokemon extends PokemonBase{
    private int attackAttribute;

    public AttackTypePokemon(String name) {
        super(name);
        this.attackAttribute = this.generateInt(2,5);
    }

    public String attackTypelaunchAttack(PokemonBase target, int attackPoint) {
        String returnString = launchAttack(target);
        attackPoint = flipCoinIsHead()?attackPoint:1;
        int energyConsume = 1;
        // check if there is enough energy for critical damage (same type)
        if(super.getEnergy() - energyConsume > 2) {
            if (super.getColor().equals(target.getColor())) {
                attackPoint = attackPoint*2;
                energyConsume = 2;
            }
        }
        super.setEnergy(super.getEnergy() - energyConsume);

        expPlus();
        if(attackPoint != 1){
            return returnString + "\n" +"Flipped coin: Head, Attack Type Pokemon Effect Triggered!";
        }
        return returnString + "\n" + "Flipped coin: Tail, Attack Type Pokemon Effect Canceled!";
    }

    @Override
    public String launchAttack(PokemonBase target){
        return super.launchAttack(target);
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
