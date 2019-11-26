package Program.PokemonModel;

import Program.GameplayPageController;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class AttackTypePokemon extends PokemonBase {

    private int attackAttribute;


    public AttackTypePokemon(String name) {
        super(name);
        this.attackAttribute = this.generateInt(2,5);
    }


    public String attackTypelaunchAttack(PokemonBase target, int attackPoint) {
        attackPoint = flipCoinIsHead()?attackPoint:1;
        String returnString = "";
        returnString += super.launchAttack(target,attackPoint);
        if(!returnString.contains("Not enough energy.")) {
            if (attackPoint != 1) {
                returnString += "\n" + "Flipped coin: Head, Attack Type Pokemon Effect Triggered!";
            } else {
                returnString += "\n" + "Flipped coin: Tail, Attack Type Pokemon Effect Canceled!";
            }
        }
        return returnString;
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
