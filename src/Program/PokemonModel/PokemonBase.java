package Program.PokemonModel;

import Program.GameplayPageController;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class PokemonBase {
    private int hp, energy, exp, stage, effectLeftRound;
    private String color, status, name;

    public PokemonBase(String name){
        this.name = name;
        this.hp = generateInt(50, 80);
        this.energy = generateInt(20,50);
        this.color = generateString(new String[]{"red","blue","yellow","colorless"});
        this.status = "active";
        this.effectLeftRound = 0;
        this.stage = 0;
        this.exp = 0;
    }


    public String launchAttack(PokemonBase target){
        int energyConsume = 1;
        String returnString = "";
        if(this.energy >0) {
            int attackPoint=1;
            // check if there is enough energy for critical damage (same type)
            if(this.energy - energyConsume > 2) {
                if (this.getClass().getName().equals(target.getClass().getName())) {
                    attackPoint = 2;
                    energyConsume = 2;
                }
            }
            this.energy -= energyConsume;
            if(!target.getStatus().equals("active")){
                returnString += "\n" + "Target pokemon is in "+target.getStatus()+", Double attack!";
                attackPoint *= 2;
            }
            String classType = target.getClass().getName();
            if(classType.contains("Defense")){
                DefenseTypePokemon defenseTypePokemon = (DefenseTypePokemon)target;
                returnString += "\n" + defenseTypePokemon.defenseTypeLaunchDefense(attackPoint,this.getResistancePoints());
            }else {
                returnString += "\n" + target.defense(attackPoint);
            }
            expPlus();

            if(attackPoint == 2){
                return returnString + "\n" +"Same Type, Double attack!";
            }

        }else {
            GameplayPageController.buttonEventQueue = new ArrayList<VBox>();
            returnString += "Not enough energy.";
        }
        return returnString;
    }

    public String launchAttack(PokemonBase target, int attackPoint){
        int energyConsume = 1;
        String returnString = "";

        if(this.energy >0) {
            // check if there is enough energy for critical damage (same type)
            if (this.energy - energyConsume > 2) {
                if (this.getClass().getName().equals(target.getClass().getName())) {
                    returnString += "\n" + "Same Type, Double attack!";
                    attackPoint *= 2;
                    energyConsume = 2;
                }
            }
            this.energy -= energyConsume;

            if(!target.getStatus().equals("active")){
                returnString += "\n" + "Target pokemon is in "+target.getStatus()+", Double attack!";
                attackPoint *= 2;
            }
            String classType = target.getClass().getName();
            if (classType.contains("Defense")) {
                DefenseTypePokemon defenseTypePokemon = (DefenseTypePokemon) target;
                returnString += "\n" + defenseTypePokemon.defenseTypeLaunchDefense(attackPoint, this.getResistancePoints());
            } else {
                returnString += "\n" + target.defense(attackPoint);
            }
            expPlus();
        }else {
            returnString += "Not enough energy.";
        }
        return returnString;
    }

    public String defense(int receivedAttackPoint){
        this.hp -= receivedAttackPoint;
        return this.getName()+" damages received "+receivedAttackPoint;
    }


    public void expPlus(){
        this.exp += 1;
        if(this.exp == 20){
            stageIncrease();
        }

    }
    public void stageIncrease(){
        this.stage += 1;
        this.exp = 0;
        this.hp *= 2;
        this.energy *= 2;

        // do effect here, call controllerUtil effect
    }
    public void setPoisoned(){
        this.setStatus("poisoned");
        this.setEffectLeftRound(this.getEffectLeftRound()+1);
    }
    public void setParalysed(){
        this.setStatus("paralysed");
        this.setEffectLeftRound(this.getEffectLeftRound()+2);
    }

    // for controller class to update every round
    public void updateStateEffectLeft(){
        if(this.effectLeftRound == 0){
            this.status = "active";
        }else{
            this.effectLeftRound -=1;
        }
    }

    public int generateInt(int from, int to){
        return (int)((Math.random()*(to-from+1))+from);
    }

    public String generateString(String[] generatorList){
        int random = (int)(Math.random()*generatorList.length);
        return generatorList[random];
    }

    public boolean flipCoinIsHead(){
        return Math.floor(Math.random()*2)==1;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getEffectLeftRound() {
        return effectLeftRound;
    }

    public void setEffectLeftRound(int effectLeftRound) {
        this.effectLeftRound = effectLeftRound;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAttackPoint(){
        return 0;
    }
    public int getResistancePoints(){
        return 0;
    }
}
