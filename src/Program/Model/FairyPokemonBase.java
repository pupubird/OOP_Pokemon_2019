package Program.Model;

public class FairyPokemonBase extends PokemonBase {
    private String fairyPower;

    public String getFairyPower() {
        return fairyPower;
    }

    public FairyPokemonBase setFairyPower(String fairyPower) {
        this.fairyPower = fairyPower;
        return this;
    }

    public FairyPokemonBase(String name, String type,String fairyPower) {
        super(name, type);
        this.fairyPower = fairyPower;
    }

    @Override
    public PokemonBase launchAttack(PokemonBase target) {
        super.launchAttack(target);
        if(flipCoinIsHead()) {
            // head, fairy power triggered
            target.setStatus(fairyPower);
        }
        return this;
    }

    private boolean flipCoinIsHead(){
        return (int)(Math.random()*2)==1;
    }

}
