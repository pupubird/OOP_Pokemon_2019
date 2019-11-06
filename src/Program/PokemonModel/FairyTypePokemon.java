package Program.PokemonModel;

public class FairyTypePokemon extends PokemonBase{
    public FairyTypePokemon(String name) {
        super(name);
    }

    @Override
    public String launchAttack(PokemonBase target) {
        super.launchAttack(target);

        if(this.flipCoinIsHead()){
            target.setPoisoned();
        }else{
            target.setParalysed();
        }

        return target.getName();
    }
}
