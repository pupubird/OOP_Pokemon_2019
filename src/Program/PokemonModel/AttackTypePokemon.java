package Program.PokemonModel;

import Program.GameplayPageController;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class AttackTypePokemon extends PokemonBase{
    private int attackAttribute;

    public AttackTypePokemon(String name) {
        super(name);
        this.attackAttribute = this.generateInt(2,5);
    }

    public String attackTypelaunchAttack(PokemonBase target, int attackPoint) {
        attackPoint = flipCoinIsHead()?attackPoint:1;
        int energyConsume = 1;
        super.setEnergy(super.getEnergy() - energyConsume);
        expPlus();
        String returnString = "";
        returnString += launchAttack(target,attackPoint);
        if(attackPoint != 1){
            return returnString + "\n" +"Flipped coin: Head, Attack Type Pokemon Effect Triggered!";
        }
        return returnString + "\n" + "Flipped coin: Tail, Attack Type Pokemon Effect Canceled!";

    }

    @Override
    public String launchAttack(PokemonBase target,int attackPoint){
        return super.launchAttack(target,attackPoint);
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
