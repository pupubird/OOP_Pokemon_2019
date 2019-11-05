package Program.Model;

/*
    Model.PokemonBase class is a class that uses Builder Design Pattern,
    Other Pokemon stats than <name> , <type>, unless specify,
    it will be auto-generated based on the type given.

    By using Builder Design Pattern, the code is more efficient and is more
    optimized, as the programmer only need to take care of three parameters,
    instead of almost ten.
*/
public class PokemonBase {
    private String name;
    private String type;
    private String energyColour;
    private int level = 1;
    private int hitPoint;
    private int energy;
    private int attackPoint;
    private int strengthPoint;
    private int resistancePoint;
    private String status = "normal";
    private int multiply = 2;
    private int energyDecreasePerMove = 1;

    public PokemonBase(String name, String type){
        this.name = name;
        this.type = type;
    }
    public String getName() {
        return name;
    }

    public PokemonBase setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public PokemonBase setType(String type) {
        this.type = type;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public PokemonBase setLevel(int level) {
        this.level = level;
        return this;
    }

    public int getHitPoint() {
        return hitPoint;
    }

    public PokemonBase setHitPoint(int hitPoint) {
        this.hitPoint = hitPoint;
        return this;
    }

    public int getEnergy() {
        return energy;
    }

    public PokemonBase setEnergy(int energy) {
        this.energy = energy;
        return this;
    }

    public String getEnergyColour() {
        return energyColour;
    }

    public PokemonBase setEnergyColour(String energyColour) {
        this.energyColour = energyColour;
        return this;
    }

    public int getAttackPoint() {
        return attackPoint;
    }

    public PokemonBase setAttackPoint(int attackPoint) {
        this.attackPoint = attackPoint;
        return this;
    }

    public int getStrengthPoint() {
        return strengthPoint;
    }

    public PokemonBase setStrengthPoint(int strengthPoint) {
        this.strengthPoint = strengthPoint;
        return this;
    }

    public int getResistancePoint() {
        return resistancePoint;
    }

    public PokemonBase setResistancePoint(int resistancePoint) {
        this.resistancePoint = resistancePoint;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public PokemonBase setStatus(String status) {
        this.status = status;
        return this;
    }

    public PokemonBase launchAttack(PokemonBase target){
        int totalDamage = calculateDamage(target);

        // decrease energy per attack
        this.setEnergy(this.getEnergy()-this.energyDecreasePerMove);

        // passing the calculated damage to the target pokemon
        target.defense(totalDamage);
        return this;
    }

    public void defense(int damageReceived){
        int actualDamage = damageReceived - this.resistancePoint;
        if(actualDamage>0){
            int currentHitPoint = this.hitPoint - actualDamage;
            this.setHitPoint(currentHitPoint);
        }
    }

    private int calculateDamage(PokemonBase target){
        // if both have the same type, multiply damage
        int weaknessExponent = isSameType(target.getType())?this.multiply:1;

        // if target status is not "normal", multiply damage
        int statusExponent = isNormal(target)?1:this.multiply;

        // ((ATK + STR) - RES) X WEAKNESS X STATUS
        int totalDamageOutput = (this.attackPoint+this.strengthPoint) * weaknessExponent * statusExponent;
        return totalDamageOutput;
    }

    private boolean isSameType(String target){
        return this.type.equals(target);
    }

    private boolean isNormal(PokemonBase target){
        return target.getStatus().equals("normal");
    }

}
