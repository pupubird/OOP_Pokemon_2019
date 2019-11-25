package Program.PokemonModel;

public class FairyTypePokemon extends PokemonBase{
    public FairyTypePokemon(String name) {
        super(name);
    }

    public String fairyTypeLaunchAttack(PokemonBase target){
        String returnString = "";
        returnString += launchAttack(target);

        if(this.flipCoinIsHead()){
            target.setPoisoned();
            return returnString+ "\n" + target.getName()+" get poisoned! Idle for "+target.getEffectLeftRound()+" round.";
        }

        target.setParalysed();
        return returnString+ "\n" + target.getName()+" get paralysed! Idle for "+target.getEffectLeftRound()+" round.";

    }

    @Override
    public String launchAttack(PokemonBase target) {
        return super.launchAttack(target);
    }
}
