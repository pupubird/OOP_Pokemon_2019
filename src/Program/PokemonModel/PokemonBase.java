package Program.PokemonModel;

import Program.GameplayPageController;
import java.util.ArrayList;


/**
 * The base class for all types of pokemon
 */
public class PokemonBase {

    /**
     * Pokemon details attribute
     */
    private int hp, energy, exp, stage;
    /**
     * The effect left round for poisoned/ paralysed.
     */
    private int effectLeftRound;
    /**
     * Pokemon details attribute
     */
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


    /**
     * Pokemon's exp + 1
     */
    public void expPlus() {
        this.exp += 1;
        if(this.exp == 20){
            stageIncrease();
        }
    }


    /**
     * Pokemon stage + 1
     */
    public void stageIncrease() {
        this.stage += 1;
        this.exp = 0;
        this.hp *= 2;
        this.energy *= 2;
    }


    /**
     * Set this pokemon's status to poisoned and increase effect left round.
     */
    void setPoisoned() {
        this.setStatus("poisoned");
        // actual + 1 to avoid turning to normal in the current round
        this.setEffectLeftRound(this.getEffectLeftRound()+2);
    }

    /**
     * Set this pokemon's status to paralysed and increase effect left round.
     */
    void setParalysed() {
        this.setStatus("paralysed");
        // actual + 1 to avoid turning to normal in the current round
        this.setEffectLeftRound(this.getEffectLeftRound()+3);
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


    /**
     * Random generate function
     * @return true = head, false = tail
     */
    boolean flipCoinIsHead() {
        return Math.floor(Math.random()*2)==1;
    }


    /**
     * Name getter
     * @return Pokemon name
     */
    public String getName() {
        return name;
    }


    /**
     * Name setter
     * @param name new name for the pokemon
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * HP getter
     * @return pokemon Hp
     */
    public int getHp() {
        return hp;
    }


    /**
     * HP setter
     * @param newHp new HP value
     */
    private void setHp(int newHp) {
        this.hp = newHp;
    }


    /**
     * Energy getter
     * @return Pokemon energy
     */
    public int getEnergy() {
        return energy;
    }


    /**
     * Energy setter
     * @param energy new energy value
     */
    public void setEnergy(int energy) {
        this.energy = energy;
    }


    /**
     * Experience getter
     * @return Pokemon experiences
     */
    public int getExp() {
        return exp;
    }


    /**
     * Experience setter
     * @param exp new exp
     */
    public void setExp(int exp) {
        this.exp = exp;
    }


    /**
     * Stage getter
     * @return Pokemon stage value
     */
    public int getStage() {
        return stage;
    }

    /**
     * Stage setter
     * @param stage new stage value
     */
    public void setStage(int stage) {
        this.stage = stage;
    }


    /**
     * EffectLeftRound getter
     * @return Effect left round value
     */
    public int getEffectLeftRound() {
        return effectLeftRound;
    }


    /**
     * EffectLeftRound setter
     * @param effectLeftRound new effectLeftRound
     */
    public void setEffectLeftRound(int effectLeftRound) {
        this.effectLeftRound = effectLeftRound;
    }


    /**
     * Pokemon color getter
     * @return Pokemon color
     */
    public String getColor() {
        return color;
    }


    /**
     * Pokemon color setter
     * @param color new color
     */
    public void setColor(String color) {
        this.color = color;
    }


    /**
     * Pokemon status getter
     * @return Pokemon's status
     */
    public String getStatus() {
        return status;
    }


    /**
     * Pokemon status setter
     * @param status new status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Attack point getter, this will be override for attack pokemon
     * @return Pokemon attack point, 0 for default
     */
    public int getAttackPoint(){
        return 0;
    }

    /**
     * Resistance point getter, this will be override for defense pokemon
     * @return Pokemon resistance point, 0 for default
     */
    public int getResistancePoints() {
        return 0;
    }
}
