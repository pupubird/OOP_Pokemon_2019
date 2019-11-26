package Program.PokemonModel;

import Program.GameplayPageController;
import java.util.ArrayList;


/**
 * The base class for all types of pokemon
 */
public class PokemonBase {

    private int hp, energy, exp, stage, effectLeftRound;
    private String color, status, name;


    /**
     * @param name the name of the pokemon
     */
    PokemonBase(String name) {
        this.name = name;
        this.hp = generateInt(50, 80);
        this.energy = generateInt(20, 50);
        this.color = generateString(new String[]{"red", "blue", "yellow", "colorless"});
        this.status = "active";
        this.effectLeftRound = 0;
        this.stage = 0;
        this.exp = 0;
    }


    /**
     * @param target the target pokemon class
     * @return a log of all actions performed
     */
    public String launchAttack(PokemonBase target) {

        int energyConsume = 1;

        String returnString = "";

        if ( this.energy > 0 ) {


            int attackPoint = 1;
            // check if there is enough energy for critical damage (same type)
            if ( this.energy - energyConsume > 2 ) {

                if ( this.getClass().getName().equals(target.getClass().getName()) ) {
                    attackPoint = 2;
                    energyConsume = 2;
                }

            }
            this.energy -= energyConsume;
            // if target pokemon is not active, double attack point
            if(!target.getStatus().equals("active")){
                returnString += "\n" + "Target pokemon is in "+target.getStatus()+", Double attack!";
                attackPoint *= 2;

            }

            String classType = target.getClass().getName();

            if ( classType.contains("Defense") ) {
                DefenseTypePokemon defenseTypePokemon = (DefenseTypePokemon)target;
                returnString += "\n" + defenseTypePokemon.defenseTypeLaunchDefense(attackPoint);
            } else {
                returnString += "\n" + target.defense(attackPoint);
            }

            expPlus();

            if ( attackPoint == 2 ) {
                return returnString + "\n" +"Same Type, Double attack!";
            }


        } else {
            GameplayPageController.buttonEventQueue = new ArrayList<>();
            returnString += "Not enough energy.";
        }
        return returnString;

    }

    // self defined attack point, mainly used in attack type pokemon, but possible
    // to use for future item card attack
    /**
     * @param target target pokemon class
     * @param attackPoint possible damage to the target pokemon
     * @return a log of all actions performed
     */
    String launchAttack(PokemonBase target, int attackPoint){
        int energyConsume = 1;
        String returnString = "";

        if ( this.energy > 0 ) {


            // check if there is enough energy for critical damage (same type)
            if (this.energy - energyConsume >= 2) {
                if (this.getClass().getName().equals(target.getClass().getName())) {
                    returnString += "\n" + "Same Type, Double attack!";
                    attackPoint *= 2;
                    energyConsume = 2;
                }

            }
            this.energy -= energyConsume;

            if ( !target.getStatus().equals("active") ) {
                returnString += "\n" + "Target pokemon is in "+target.getStatus()+", Double attack!";
                attackPoint *= 2;
            }

            String classType = target.getClass().getName();

            if ( classType.contains("Defense") ) {
                DefenseTypePokemon defenseTypePokemon = (DefenseTypePokemon) target;
                returnString += "\n" + defenseTypePokemon.defenseTypeLaunchDefense(attackPoint);
            } else {
                returnString += "\n" + target.defense(attackPoint);
            }

            expPlus();


        } else {
            returnString += "Not enough energy.";
        }

        return returnString;

    }


    /**
     * @param receivedAttackPoint the attack point
     * @return a log of the process
     */
    String defense(int receivedAttackPoint) {
        this.setHp(this.getHp()- receivedAttackPoint);
        return this.getName()+" damages received "+receivedAttackPoint;
    }


    public void expPlus() {
        this.exp += 1;
        if(this.exp == 20){
            stageIncrease();

        }
    }


    public void stageIncrease() {
        this.stage += 1;
        this.exp = 0;
        this.hp *= 2;
        this.energy *= 2;
        // do effect here, call controllerUtil effect
    }


    void setPoisoned() {
        this.setStatus("poisoned");
        this.setEffectLeftRound(this.getEffectLeftRound()+1);
    }


    void setParalysed() {
        this.setStatus("paralysed");
        this.setEffectLeftRound(this.getEffectLeftRound()+2);
    }

    /**
     * @param from floor
     * @param to ceiling
     * @return A whole number from the range floor to ceiling
     */
    public int generateInt(int from, int to) {
        return (int)((Math.random()*(to-from+1))+from);
    }


    /**
     * @param generatorList A list of a string
     * @return one of the item in the list
     */
    public String generateString(String[] generatorList) {
        int random = (int)(Math.random()*generatorList.length);
        return generatorList[random];
    }


    boolean flipCoinIsHead() {
        return Math.floor(Math.random()*2)==1;
    }


    public String getName() {
        return name;
    }


    /**
     * @param name setter
     */
    public void setName(String name) {
        this.name = name;
    }


    public int getHp() {
        return hp;
    }


    /**
     * @param newHp setter
     */
    private void setHp(int newHp) {
        this.hp = newHp;
    }


    public int getEnergy() {
        return energy;
    }


    /**
     * @param energy setter
     */
    public void setEnergy(int energy) {
        this.energy = energy;
    }


    public int getExp() {
        return exp;
    }


    /**
     * @param exp setter
     */
    public void setExp(int exp) {
        this.exp = exp;
    }


    public int getStage() {
        return stage;
    }

    /**
     * @param stage setter
     */
    public void setStage(int stage) {
        this.stage = stage;
    }


    public int getEffectLeftRound() {
        return effectLeftRound;
    }


    /**
     * @param effectLeftRound setter
     */
    public void setEffectLeftRound(int effectLeftRound) {
        this.effectLeftRound = effectLeftRound;
    }


    public String getColor() {
        return color;
    }


    /**
     * @param color setter
     */
    public void setColor(String color) {
        this.color = color;
    }


    public String getStatus() {
        return status;
    }


    /**
     * @param status setter
     */
    public void setStatus(String status) {
        this.status = status;
    }

    // these will be override for attack and defense type pokemon
    public int getAttackPoint(){
        return 0;
    }


    public int getResistancePoints() {
        return 0;
    }
}
