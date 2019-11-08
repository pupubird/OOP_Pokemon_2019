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
            return returnString+ "\n" + target.getName()+" get poisoned! Freezed for 2 round.";
        }

        target.setParalysed();
        return returnString+ "\n" + target.getName()+" get paralysed! Freezed for 1 round.";

    }

    @Override
    public String launchAttack(PokemonBase target) {
        return super.launchAttack(target);
    }
}
