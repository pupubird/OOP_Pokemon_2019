package Program;

import Program.PokemonModel.AttackTypePokemon;
import Program.PokemonModel.DefenseTypePokemon;
import Program.PokemonModel.FairyTypePokemon;
import Program.PokemonModel.PokemonBase;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.*;

import static java.lang.Math.abs;

public class GameplayPageController {
    public static boolean continueSaveGame = false;

    private boolean oneRoundDone = false;
    private boolean currentRoundIsComputer = false;
    private long lastSecond = -1;
    private VBox[][] playersCards;
    private PokemonBase[][] playersPokemons;
    private ImageView[][] playersCardImages;
    private Label[] pokemonDetailsPaneLabels;
    private Label[][] pokemonHpCardLabels;
    private Button[] buttons;
    private String currentButtonState;

    public static ArrayList<VBox> buttonEventQueue = new ArrayList<>();

    @FXML
    public SplitPane GameplayPagePane;
    public VBox gameplayZonePane, ActionButtonPane;
    public HBox player1groupPane, player2groupPane;
    public GridPane PokemonPropertiesPane;
    public HBox ButtonHBox, ButtonHBox2;
    public Button AttackButton, RechargeButton, TrainButton, SaveExitButton;
    public Label pokemonName, type, stage, experience, energy, energyColor, attackPoint, resistancePoint, status;
    public VBox player1card1, player1card2, player1card3, player1card4, player1card5, player1card6;
    public VBox player2card1, player2card2, player2card3, player2card4, player2card5, player2card6;
    public Label player1card1Hp, player1card2Hp, player1card3Hp, player1card4Hp, player1card5Hp, player1card6Hp;
    public Label player2card1Hp, player2card2Hp, player2card3Hp, player2card4Hp, player2card5Hp, player2card6Hp;
    public ImageView player1card1Image, player1card2Image, player1card3Image, player1card4Image, player1card5Image, player1card6Image;
    public ImageView player2card1Image, player2card2Image, player2card3Image, player2card4Image, player2card5Image, player2card6Image;

    /*
    when user click something, it will be added into the event queue
    so that the history of clicked event will be recorded for animation to know
    what did the user clicked before the current click.
     */
    private void buttonEventHandler(int[] cardIndex) throws InterruptedException {
        String returnedLog = "Player 1: \n";
        // which action is clicked
        switch (currentButtonState){
            case "normal": // no button is clicked, this is use for checking pokemon stat.
                showPokemonDetailOnPane(cardIndex);
                break;
            case "attack":
                // verify player chosen his own pokemon, add to event queue for next event calls;
                int[][] pokemonsIndexes = attackVerify(cardIndex);
                if(pokemonsIndexes[0][0] != -1){
                    returnedLog += attack( pokemonsIndexes[0] , pokemonsIndexes[1] );
                    currentButtonState = "normal";
                    clearText(returnedLog);
                }
                break;
            case "recharge":
                returnedLog += recharge(cardIndex);
                currentButtonState = "normal";
                clearText(returnedLog);
                break;
            case "train":
                returnedLog += train(cardIndex);
                currentButtonState = "normal";
                clearText(returnedLog);
                break;
            case "saveExit":
                // prompt to confirm, if yes next page
                break;
        }
        // update pokemon details
        updatePokemonDetailsOnCard();
    }

    private int[][] attackVerify(int[] cardIndex){

        /*
        this is to check if user click opponent pokemon (in the first click),
        user should choose his own pokemon first to perform attack.

        if the size of the queue is 2, means user had clicked something twice
        if the first pokemon index passed (cardIndex) is 0 (player1), it means
        the first event queue is invalid, hence prompt again.
         */
        if(buttonEventQueue.size() == 2 && cardIndex[0]==0
                || (cardIndex[0]==1 && buttonEventQueue.size() == 0)) {

            clearText("Please re-choose the pokemon!");

            buttonEventQueue = new ArrayList<VBox>();
            return new int[][]{{-1},{-1}};

        }else if(cardIndex[0]==0 && buttonEventQueue.size() == 0) {
            buttonEventQueue.add(playersCards[cardIndex[0]][cardIndex[1]]);
        }else if(cardIndex[0]==1 && buttonEventQueue.size() == 1) {
            buttonEventQueue.add(playersCards[cardIndex[0]][cardIndex[1]]);
        }

        // remove duplicate
        if(buttonEventQueue.size() ==2){
            if(buttonEventQueue.get(0).getId().equals(buttonEventQueue.get(1).getId())){
                buttonEventQueue.remove(0);
            }
        }
        // the first item added into is player own pokemon
        int[] secondCardIndex = new int[]{-1};
        int[] firstCardIndex = new int[]{-1};

        // get the first event in the event queue
        if (getCardIndex(buttonEventQueue.get(0).getId())[0] == 0) {
            clearText("You chose " + playersPokemons[cardIndex[0]][cardIndex[1]].getName());
            // await for the next button event queue (user click the opponent pokemon as attacking target)
            if(buttonEventQueue.size() == 2){
                if (getCardIndex(buttonEventQueue.get(1).getId())[0] == 1) {

                    disableButton(true);

                    firstCardIndex = getCardIndex(buttonEventQueue.get(0).getId());
                    secondCardIndex = getCardIndex(buttonEventQueue.get(1).getId());

                    disableButton(false);
                    // event queue cycle done, back to status quo
                    buttonEventQueue = new ArrayList<VBox>();
                }
            }
        }

        return new int[][]{
                firstCardIndex,
                secondCardIndex
        };
    }
    private String attack(int[] indexPokemonFrom, int[] indexPokemonTo){


        String pokemonReturnedLog="";
        if(/*if it is idle*/ (playersPokemons[indexPokemonFrom[0]][indexPokemonFrom[1]].getEffectLeftRound()>0)){
            buttonEventQueue = new ArrayList<VBox>();
            // if computer get to here but it faces this error, call the function recursively again until it success.
            if (currentRoundIsComputer) {
                pokemonReturnedLog += computerTurn();
            }else{
                pokemonReturnedLog = "Pokemon is in idled for: "+playersPokemons[indexPokemonFrom[0]][indexPokemonFrom[1]].getEffectLeftRound()+" round.";
            }
        }else {
            // check if the pokemon is dead
            if (playersPokemons[indexPokemonFrom[0]][indexPokemonTo[1]].getHp() <0){
                if (currentRoundIsComputer) {
                    pokemonReturnedLog += computerTurn();
                }
            }else {
                String classType = playersPokemons[indexPokemonFrom[0]][indexPokemonFrom[1]].getClass().getName();
                if (classType.contains("Attack")) {
                    AttackTypePokemon attackTypePokemon = (AttackTypePokemon) playersPokemons[indexPokemonFrom[0]][indexPokemonFrom[1]];
                    pokemonReturnedLog = attackTypePokemon.attackTypelaunchAttack(playersPokemons[indexPokemonTo[0]][indexPokemonTo[1]],
                            attackTypePokemon.getAttackPoint()
                    );

                } else if (classType.contains("Fairy")) {

                    FairyTypePokemon fairyTypePokemon = (FairyTypePokemon) playersPokemons[indexPokemonFrom[0]][indexPokemonFrom[1]];
                    pokemonReturnedLog = fairyTypePokemon.fairyTypeLaunchAttack(playersPokemons[indexPokemonTo[0]][indexPokemonTo[1]]);

                } else {
                    // defense and other types of pokemons share the same launchAttack function.
                    pokemonReturnedLog = playersPokemons[indexPokemonFrom[0]][indexPokemonFrom[1]].launchAttack(
                            playersPokemons[indexPokemonTo[0]][indexPokemonTo[1]]
                    );
                }

                // execute the effect and call computer turn.
                if (!pokemonReturnedLog.contains("Not enough energy.")) {
                    if (currentRoundIsComputer) {
                        // from computer perspective, when computer finish, it means
                        // one round is done.
                        updateOnGameRoundDone();
                    }
                    currentRoundIsComputer = !currentRoundIsComputer;
                    attackEffect(indexPokemonFrom, indexPokemonTo);
                } else if (currentRoundIsComputer) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // computer fall into this not enough energy error
                    // call itself recursively again.
                    pokemonReturnedLog += computerTurn();
                }


                updatePokemonDetailsOnCard();
            }

        }
        return pokemonReturnedLog;
    }
    private void attackEffect(int[] indexPokemonFrom, int[] indexPokemonTo){
        double spacing = 10;

        new AnimationTimer() {
            private boolean doneAnimation = false;

            double currentX = 0, currentY = 0;
            // n = index, t = target, f = from
            // (nt - nf)(space + width)
            // (height/2)+space
            double targetXIndex = indexPokemonTo[1] - indexPokemonFrom[1];
            double targetXSpaceWidth = spacing + playersCards[indexPokemonFrom[0]][indexPokemonFrom[1]].getWidth();
            double outputX = targetXIndex*targetXSpaceWidth;

            double targetY = (playersCards[indexPokemonFrom[0]][indexPokemonTo[1]].getHeight()/2);
            double outputY = currentRoundIsComputer? (targetY+spacing) :- (targetY+spacing);

            double pixelPerFrameX = outputX/10, pixelPerFrameY = outputY/10;

            @Override
            public void handle(long now) {
                disableButton(true);
                long secondPassed = 1000000000;
                currentX += pixelPerFrameX;
                currentY -= pixelPerFrameY;

                playersCards[indexPokemonFrom[0]][indexPokemonFrom[1]].setTranslateX(currentX);
                playersCards[indexPokemonFrom[0]][indexPokemonFrom[1]].setTranslateY(currentY);

                // below are the keyframes for animations
                if(!doneAnimation) {
                    // go forward
                    if (abs(currentX) > abs(outputX) || abs(currentY) > abs(outputY)) {
                        pixelPerFrameX = -pixelPerFrameX;
                        pixelPerFrameY = -pixelPerFrameY;
                        oneRoundDone = true;
                        if (lastSecond < 0) {
                            lastSecond = now;
                        }
                        if (playersPokemons[indexPokemonFrom[0]][indexPokemonFrom[1]].getClass().getName().contains("Fairy")) {
                            ControllerUtil.playEffect(getClass().getResource("resources/fxml/assets/fairyAttack.mp3"));

                        } else {
                            ControllerUtil.playEffect(getClass().getResource("resources/fxml/assets/attack.mp3"));
                        }
                        // go backward
                    } else if (oneRoundDone) {
                        // blinking effect
                        if (now - lastSecond < secondPassed * 0.0375) {
                            playersCards[indexPokemonTo[0]][indexPokemonTo[1]].setVisible(false);
                        }
                        if (now - lastSecond > secondPassed * 0.075 && now - lastSecond < secondPassed * 0.1125) {
                            playersCards[indexPokemonTo[0]][indexPokemonTo[1]].setVisible(true);
                        }

                        if (now - lastSecond > secondPassed * 0.1125 && now - lastSecond < secondPassed * 0.15) {
                            playersCards[indexPokemonTo[0]][indexPokemonTo[1]].setVisible(false);
                        }

                        if (now - lastSecond > secondPassed * 0.15 && now - lastSecond < secondPassed * 0.2) {
                            playersCards[indexPokemonTo[0]][indexPokemonTo[1]].setVisible(true);
                        }

                        // returned to original position
                        if (abs(currentX) <= 5 && abs(currentY) <= 5) {

                            currentY = 0;
                            pixelPerFrameX = 0;
                            pixelPerFrameY = 0;
                            oneRoundDone = false;
                            doneAnimation = true;
                            // show visible if not
                            playersCards[indexPokemonTo[0]][indexPokemonTo[1]].setVisible(true);

                        }
                    }
                }
                if (doneAnimation) {
                    updateOnGameActionOccured();
                    if (now - lastSecond > secondPassed * 1.5 && now - lastSecond < secondPassed * 2) {
                        if (currentRoundIsComputer) {
                            clearText("Computer is thinking...");
                        }
                    }
                    if (now - lastSecond > secondPassed * 2) {
                        lastSecond = -1;
                        this.stop();
                        if (currentRoundIsComputer) {
                            computerTurn();
                        }
                        disableButton(false);
                    }
                }

            }
        }.start();
    }

    private String recharge(int[] indexPokemon){

        String pokemonReturnedLog = "";
        if(/*if it is idle*/ (playersPokemons[indexPokemon[0]][indexPokemon[1]].getEffectLeftRound()>0)){
            buttonEventQueue = new ArrayList<VBox>();
            if (currentRoundIsComputer) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pokemonReturnedLog += computerTurn();
            }else{
                pokemonReturnedLog = "Pokemon is in idled for: "+playersPokemons[indexPokemon[0]][indexPokemon[1]].getEffectLeftRound()+" round.";
            }
        }else {
            PokemonBase selectedPokemon = playersPokemons[indexPokemon[0]][indexPokemon[1]];
            if (playersPokemons[indexPokemon[0]][indexPokemon[1]].getHp() <0){
                if (currentRoundIsComputer) {
                    pokemonReturnedLog += computerTurn();
                }
            }else {
                String cardDrawn = selectedPokemon.generateString(new String[]{"red", "blue", "yellow"});
                boolean recharged = false;

                if (!currentRoundIsComputer) {
                    updateOnGameRoundDone();
                }
                currentRoundIsComputer = !currentRoundIsComputer;
                if (selectedPokemon.getColor().equals("colorless")
                        || selectedPokemon.getColor().equals(cardDrawn)) {
                    rechargeEffect(indexPokemon, true);
                    selectedPokemon.setEnergy(selectedPokemon.getEnergy() + 5);
                    recharged = true;
                }

                String showCard = String.format("Card Drawn : %s", cardDrawn);

                if (recharged) {
                    ControllerUtil.playEffect(getClass().getResource("resources/fxml/assets/recharge.mp3"));
                    pokemonReturnedLog += String.format("%s\n%s has successfully recharged ! (%s)"
                            , showCard, selectedPokemon.getName(), selectedPokemon.getColor());
                } else {
                    rechargeEffect(indexPokemon, false);
                    pokemonReturnedLog += String.format("%s\n%s has failed to recharged ! (%s)"
                            , showCard, selectedPokemon.getName(), selectedPokemon.getColor());
                }
            }

        }
        return pokemonReturnedLog;
    }
    private void rechargeEffect(int[] indexPokemon, boolean show){
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                long secondPassed = 1000000000;
                if(lastSecond < 0){
                    lastSecond = now;
                    disableButton(true);
                }
                if(show) {
                    if (now - lastSecond < secondPassed * 0.0375) {
                        playersCards[indexPokemon[0]][indexPokemon[1]].setVisible(false);
                    }
                    if (now - lastSecond > secondPassed * 0.075 && now - lastSecond < secondPassed * 0.1125) {
                        playersCards[indexPokemon[0]][indexPokemon[1]].setVisible(true);
                    }

                    if (now - lastSecond > secondPassed * 0.1125 && now - lastSecond < secondPassed * 0.15) {
                        playersCards[indexPokemon[0]][indexPokemon[1]].setVisible(false);
                    }

                    if (now - lastSecond > secondPassed * 0.15 && now - lastSecond < secondPassed * 0.2) {
                        playersCards[indexPokemon[0]][indexPokemon[1]].setVisible(true);
                    }
                }
                if (now - lastSecond > secondPassed * 1.5 && now - lastSecond < secondPassed * 2) {
                    if (currentRoundIsComputer) {
                        clearText("Computer is thinking...");
                    }
                }

                if(now - lastSecond > secondPassed * 2) {
                    lastSecond = -1;
                    updateOnGameActionOccured();
                    this.stop();
                    if(currentRoundIsComputer){
                        computerTurn();
                    }
                    disableButton(false);
                }
            }
        }.start();
    }

    private String train(int[] indexPokemon){

        String pokemonReturnedLog = "";
        if(/*if it is idle*/ (playersPokemons[indexPokemon[0]][indexPokemon[1]].getEffectLeftRound()>0)){
            buttonEventQueue = new ArrayList<VBox>();
            if (currentRoundIsComputer) {
                pokemonReturnedLog += computerTurn();
            }else{
                pokemonReturnedLog += "Pokemon is in idled for: "+playersPokemons[indexPokemon[0]][indexPokemon[1]].getEffectLeftRound()+" round.";
            }
        }else if (playersPokemons[indexPokemon[0]][indexPokemon[1]].getHp() <0){
            if (currentRoundIsComputer) {
                pokemonReturnedLog += computerTurn();
            }
        } else {

            //need round system to validate
            PokemonBase selectedPokemon = playersPokemons[indexPokemon[0]][indexPokemon[1]];

            if (selectedPokemon.getEnergy() < 5) {
                pokemonReturnedLog += selectedPokemon.getName() + " does not have enough energy (5) to be trained !";
                if (currentRoundIsComputer) {
                    pokemonReturnedLog += computerTurn();
                }
            } else {
                selectedPokemon.expPlus();
                selectedPokemon.setEnergy(selectedPokemon.getEnergy() - 5);
                if(currentRoundIsComputer){
                    updateOnGameRoundDone();
                }
                currentRoundIsComputer = !currentRoundIsComputer;
                trainEffect(indexPokemon);
                ControllerUtil.playEffect(getClass().getResource("resources/fxml/assets/train.mp3"));
                pokemonReturnedLog += selectedPokemon.getName() + " has increased its experience by 1 !";

            }
        }

        return  pokemonReturnedLog;

    }
    private void trainEffect(int[] indexPokemon){
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                long secondPassed = 1000000000;
                    if(lastSecond < 0){
                        lastSecond = now;
                        disableButton(true);
                    }
                    if (now - lastSecond < secondPassed * 0.0375) {
                        playersCards[indexPokemon[0]][indexPokemon[1]].setVisible(false);
                    }
                    if (now - lastSecond > secondPassed * 0.075 && now - lastSecond < secondPassed * 0.1125) {
                        playersCards[indexPokemon[0]][indexPokemon[1]].setVisible(true);
                    }

                    if (now - lastSecond > secondPassed * 0.1125 && now - lastSecond < secondPassed * 0.15) {
                        playersCards[indexPokemon[0]][indexPokemon[1]].setVisible(false);
                    }

                    if(now - lastSecond > secondPassed * 0.15 && now - lastSecond < secondPassed * 0.2){
                        playersCards[indexPokemon[0]][indexPokemon[1]].setVisible(true);
                    }

                    if (now - lastSecond > secondPassed * 1.5 && now - lastSecond < secondPassed * 2) {
                        if (currentRoundIsComputer) {
                            clearText("Computer is thinking...");
                        }
                    }
                    if(now - lastSecond > secondPassed * 2) {
                        lastSecond = -1;
                        this.stop();
                        updateOnGameActionOccured();
                        if(currentRoundIsComputer){
                            computerTurn();
                        }
                        disableButton(false);
                    }
                }
        }.start();
    }

    private void saveExit(){ }

    private String computerTurn()  {
        disableButton(true);
        int action = (int) Math.floor(Math.random() * 100);
        int cardAmount = 6;
        int[] indexPokemonFrom;
        int[] indexPokemonTo;
        do {
            indexPokemonFrom = new int[]{
                    1,
                    (int) Math.floor(Math.random() * cardAmount)
            };
            indexPokemonTo = new int[]{
                    0,
                    (int) Math.floor(Math.random() * cardAmount)
            };
            // if the index generated are not alive pokemon, regenerate again.
        } while (playersPokemons[indexPokemonFrom[0]][indexPokemonFrom[1]].getHp() <= 0
                || playersPokemons[indexPokemonTo[0]][indexPokemonTo[1]].getHp() <= 0
        );
        String returnedLog = "";
        // attack has 50% chance
        if(action >=0 && action <=50){
            returnedLog += attack(indexPokemonFrom, indexPokemonTo);

            // recharge has 25% chance
        }else if(action>50 && action <=75){
            returnedLog += recharge(indexPokemonFrom);
            // train has 25% chance
        }else{
            returnedLog += train(indexPokemonFrom);
        }
        clearText("Computer: \n"+returnedLog);
        // update pokemon details
        updatePokemonDetailsOnCard();
        disableButton(false);
        return returnedLog;
    }

    private void revealEffect() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                long secondPassed = 1000000000;
                if(lastSecond<0){
                    lastSecond = now;
                }
                if(now - lastSecond > secondPassed*0.6){
                    player1card1.setVisible(true);
                    player2card6.setVisible(true);
                }
                if(now - lastSecond > secondPassed*1.2){
                    player1card2.setVisible(true);
                    player2card5.setVisible(true);
                }
                if(now - lastSecond > secondPassed*1.8){
                    player1card3.setVisible(true);
                    player2card4.setVisible(true);
                }
                if(now - lastSecond > secondPassed*2.4){
                    player1card4.setVisible(true);
                    player2card3.setVisible(true);
                }
                if(now - lastSecond > secondPassed*3){
                    player1card5.setVisible(true);
                    player2card2.setVisible(true);
                }
                if(now - lastSecond > secondPassed*3.6){
                    player1card6.setVisible(true);
                    player2card1.setVisible(true);

                    //clean up for animations
                    disableButton(false);
                    lastSecond = -1;

                    this.stop();
                }
            }
        }.start();
    }

    private void clearText(String promptText){
        for(Label label: pokemonDetailsPaneLabels){
            label.setText("");
        }
        //if there is any promptText
        energy.setText(promptText);
    }

    private int[] getCardIndex(String cardID){
        int[] playerCard = new int[3];
        String[] playerCardIndex;
        // getting card index based on the id
        if(cardID.contains("player1")){
            playerCardIndex = cardID.split("player1card");
        }else {
            playerCardIndex = cardID.split("player2card");
            playerCard[0]=1;
        }
        playerCard[1]=Integer.parseInt(playerCardIndex[1])-1;
        return playerCard;
    }
    private void showPokemonDetailOnPane(int[] cardIndex){
        String className = playersPokemons[cardIndex[0]][cardIndex[1]].getClass().getName();

        String classType;
        if(className.contains("Attack")){
            classType = "Attack";
        }else if(className.contains("Defense")){
            classType = "Defense";
        }else {
            classType = "Fairy";
        }

        String attackPoints = classType.equals("Attack")?
                Integer.toString(playersPokemons[cardIndex[0]][cardIndex[1]].getAttackPoint())
                :"-";
        String resistancePoints = classType.equals("Defense")?
                Integer.toString(playersPokemons[cardIndex[0]][cardIndex[1]].getResistancePoints())
                :"-";
        pokemonDetailsPaneLabels[0].setText("Name: "+playersPokemons[cardIndex[0]][cardIndex[1]].getName());
        pokemonDetailsPaneLabels[1].setText("Type: "+classType);
        pokemonDetailsPaneLabels[2].setText("Stage: "+Integer.toString(playersPokemons[cardIndex[0]][cardIndex[1]].getStage()));
        pokemonDetailsPaneLabels[3].setText("Experience: "+Integer.toString(playersPokemons[cardIndex[0]][cardIndex[1]].getExp()));
        pokemonDetailsPaneLabels[4].setText("Energy: "+Integer.toString(playersPokemons[cardIndex[0]][cardIndex[1]].getEnergy()));
        pokemonDetailsPaneLabels[5].setText("Energy Color: "+playersPokemons[cardIndex[0]][cardIndex[1]].getColor());
        pokemonDetailsPaneLabels[6].setText("Attack Point: "+attackPoints);
        pokemonDetailsPaneLabels[7].setText("Resistance Point: "+resistancePoints);
        pokemonDetailsPaneLabels[8].setText("Status: "+playersPokemons[cardIndex[0]][cardIndex[1]].getStatus());

    }
    private void updateOnGameRoundDone(){
        for(PokemonBase[] player: playersPokemons){
            for(PokemonBase pokemon : player ){
                if(pokemon.getEffectLeftRound()>0) {
                    pokemon.setEffectLeftRound(pokemon.getEffectLeftRound() - 1);
                }
                if(pokemon.getEffectLeftRound() == 0){
                    pokemon.setStatus("active");
                }
            }
        }
    }
    private void updateOnGameActionOccured(){

        // to check if any player wins
        for (int i = 0; i < playersPokemons.length; i++){
            int count = 0;
            for(int j = 0; j < playersPokemons[i].length; j++){
                if(playersPokemons[i][j].getHp()<=0){
                    playersCards[i][j].setVisible(false);
                }
                if(playersPokemons[i][j].getHp() <= 0){
                    count ++;
                }
                if(count >= 3){
                    ControllerUtil.playBackgroundMusic(getClass().getResource("resources/fxml/assets/gameover.mp3"));
                    ControllerUtil.switchToScene(getClass().getResource("resources/fxml/GameOverPage.fxml"));
                    break;
                }
            }
        }
    }
    private void updatePokemonDetailsOnCard(){

        // get every pokemon Hp and set to the respective card Label
        for(int i = 0; i < pokemonHpCardLabels.length; i++){
            for(int j = 0; j < pokemonHpCardLabels[i].length; j++){
                pokemonHpCardLabels[i][j].setText(
                        "\n"
                        +playersPokemons[i][j].getName()
                        +"\n"+ "HP: "+Integer.toString(playersPokemons[i][j].getHp())
                );
            }
        }
    }
    private void disableButton(boolean disable){
        for(Button button: buttons){
            button.setDisable(disable);
        }
    }

    private void initializePokemonCardImage(){
        // if no input is specify, generate it
        for(ImageView[] playersCard: playersCardImages){
            for(ImageView cardImage: playersCard){
                int imageIndex = playersPokemons[0][0].generateInt(1,12);
                Image image =  new Image(getClass().getResource("resources/fxml/assets/pokemon"+imageIndex+".png").toString());
                cardImage.setImage(image);
            }
        }
    }
    private void initializePokemonCardImage(String[][] playersCardImagesString){
        // input is specified = game is load from previous game
        for(int i = 0; i < playersCardImages.length; i++) {
            for (int j = 0; j < playersCardImages[i].length; j++) {
                playersCardImages[i][j].setImage(
                        new Image(getClass().getResource("resources/fxml/assets/pokemon"
                                + playersCardImagesString[i][j] + ".png").toString())
                );

            }
        }
    }
    private void initializePlayersPokemons(){
        // generate pokemon if not load saved game
        playersPokemons = new PokemonBase[][]{{
                new FairyTypePokemon("gugubird"),
                new AttackTypePokemon("gugubird1"),
                new DefenseTypePokemon("gugubird2"),
                new AttackTypePokemon("gugubird3"),
                new DefenseTypePokemon("gugubird4"),
                new DefenseTypePokemon("gugubird5"),
        },{
                new FairyTypePokemon("gugubird"),
                new AttackTypePokemon("gugubird1"),
                new DefenseTypePokemon("gugubird2"),
                new AttackTypePokemon("gugubird3"),
                new DefenseTypePokemon("gugubird4"),
                new DefenseTypePokemon("gugubird5"),
        }};
    }
    private void initializePlayersCardVBox(){

        double width = ControllerUtil.getScreenWidth();
        double height = ControllerUtil.getScreenHeight();
        double pokemonCardWidthRatio = 0.142;
        double pokemonCardHeightRatio = 0.3;

        playersCards = new VBox[][]{
                {
                        player1card1,player1card2,player1card3, player1card4,player1card5,player1card6
                },
                {
                        player2card1,player2card2,player2card3,player2card4,player2card5,player2card6
                }
        };
        for(int i = 0; i < playersPokemons.length; i++){
            for(int j = 0; j < playersPokemons[i].length; j++){
                // show different color on border base on the pokemon color
                if(!playersPokemons[i][j].getColor().equals("colorless")){
                    playersCards[i][j].setStyle("-fx-border-radius:10;-fx-border-color:"+playersPokemons[i][j].getColor()+";"
                            +"-fx-border-width:5;"
                    );
                }else{
                    playersCards[i][j].setStyle("-fx-border-radius:10;");
                }
            }
        }
        // set for all pokemon card width and height
        for(VBox[] player: playersCards){
            for(VBox card: player){
                card.setMinWidth(width*pokemonCardWidthRatio);
                card.setMinHeight(height*pokemonCardHeightRatio);
                card.setVisible(false);
            }
        }
    }
    private void initializePlayersCardVBoxMouseEvent(){
        double width = ControllerUtil.getScreenWidth();
        double height = ControllerUtil.getScreenHeight();
        double pokemonCardWidthRatio = 0.142;
        double pokemonCardHeightRatio = 0.3;
        for(int i = 0; i< playersCards.length;i++){
            for(int j = 0; j <playersCards[i].length;j++){
                VBox card = playersCards[i][j];
                // on mouse hover enter -> hoverEffect
                int a = i;
                int b = j;
                card.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        pokemonHpCardLabels[a][b].setTextFill(Color.RED);
                    }
                });
                // on mouse hover exit -> remove hoverEffect
                card.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        pokemonHpCardLabels[a][b].setTextFill(Color.BLACK);
                    }
                });
                // on mouse clicked -> show stats
                card.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
                    try {
                        buttonEventHandler(getCardIndex(card.getId()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                card.setMinWidth(width*pokemonCardWidthRatio);
                card.setMinHeight(height*pokemonCardHeightRatio);
            }
        }
    }
    private void initializePlayersCardVBoxImage(){
        playersCardImages = new ImageView[][]{
                {
                        player1card1Image,player1card2Image,
                        player1card3Image,player1card4Image,
                        player1card5Image,player1card6Image,
                },{
                player2card1Image,player2card2Image,
                player2card3Image,player2card4Image,
                player2card5Image,player2card6Image,
        }
        };
    }
    private void initializeLabels(){
        pokemonDetailsPaneLabels = new Label[]{
                pokemonName,type,stage,
                experience,energy,energyColor,
                attackPoint,resistancePoint,status
        };

        pokemonHpCardLabels = new Label[][]{
                {
                        player1card1Hp, player1card2Hp,
                        player1card3Hp,player1card4Hp,
                        player1card5Hp,player1card6Hp
                },{
                player2card1Hp, player2card2Hp,
                player2card3Hp,player2card4Hp,
                player2card5Hp,player2card6Hp
        }
        };
    }
    private void initializeButtons(){

        double width = ControllerUtil.getScreenWidth();
        double height = ControllerUtil.getScreenHeight();

        double bottomPaneHeightRatio = 0.3;
        double PokemonPropertiesPaneWidthRatio = 0.7;

        buttons = new Button[]{AttackButton,RechargeButton,TrainButton,SaveExitButton};
        // disable button, enable when all cards are revealed.
        for(Button button: buttons){
            button.setDisable(true);
        }

        ActionButtonPane.setStyle("-fx-background-color: #4eb9f7;");

        ButtonHBox.setMinHeight(height*bottomPaneHeightRatio*0.5);
        ButtonHBox.setSpacing(width*(1-PokemonPropertiesPaneWidthRatio)*0.125);
        ButtonHBox2.setMinHeight(height*bottomPaneHeightRatio*0.5);
        ButtonHBox2.setSpacing(width*(1-PokemonPropertiesPaneWidthRatio)*0.125);

        AttackButton.setMinWidth(width*(1-PokemonPropertiesPaneWidthRatio)*0.25);
        RechargeButton.setMinWidth(width*(1-PokemonPropertiesPaneWidthRatio)*0.25);
        TrainButton.setMinWidth(width*(1-PokemonPropertiesPaneWidthRatio)*0.25);
        SaveExitButton.setMinWidth(width*(1-PokemonPropertiesPaneWidthRatio)*0.25);

        AttackButton.addEventFilter(MouseEvent.MOUSE_CLICKED,event -> {
            currentButtonState = currentButtonState.equals("attack")?"normal":"attack";
            if(currentButtonState.equals("normal")){
                clearText("Click any pokemon to see their stats!");
            }else {
                clearText("Attack: Please select one of your own pokemon"
                        + "\n" + "Click Attack Button again to cancel this movement"
                );
            }
        });
        RechargeButton.addEventFilter(MouseEvent.MOUSE_CLICKED,event -> {
            currentButtonState = currentButtonState.equals("recharge")?"normal":"recharge";
            if(currentButtonState.equals("normal")){
                clearText("Click any pokemon to see their stats!");
            }else {
                clearText("Recharge: Please select one of your own pokemon"
                        + "\n" + "Click Recharge Button again to cancel this movement"
                );
            }
        });
        TrainButton.addEventFilter(MouseEvent.MOUSE_CLICKED,event -> {
            currentButtonState = currentButtonState.equals("train")?"normal":"train";
            if(currentButtonState.equals("normal")){
                clearText("Click any pokemon to see their stats!");
            }else {
                clearText("Train: Please select one of your own pokemon"
                        + "\n" + "Click Train Button again to cancel this movement"
                );
            }
        });
    }
    private void initializePane(){


        double width = ControllerUtil.getScreenWidth();
        double height = ControllerUtil.getScreenHeight();

        double bottomPaneHeightRatio = 0.3;
        double PokemonPropertiesPaneWidthRatio = 0.7;


        GameplayPagePane.setMinWidth(width);
        GameplayPagePane.setMinHeight(height);
        GameplayPagePane.setMaxWidth(width);
        GameplayPagePane.setMaxHeight(height);
        GameplayPagePane.setStyle(
                "-fx-background-image:url("+getClass().getResource("resources/fxml/assets/background.jpg")+");" +
                        "-fx-background-repeat:no-repeat;" +
                        "-fx-background-size:100% 100%;"
        );

        PokemonPropertiesPane.setMinWidth(width*PokemonPropertiesPaneWidthRatio);
        PokemonPropertiesPane.setMinHeight(height*bottomPaneHeightRatio);
        PokemonPropertiesPane.setStyle(
                "-fx-background-color: #38b3fb;"
        );

        player1groupPane.setMinHeight(height*(1-bottomPaneHeightRatio)*0.5);
        player2groupPane.setMinHeight(height*(1-bottomPaneHeightRatio)*0.5);
    }
    public void initialize(){
        currentButtonState = "normal";

        this.initializePlayersPokemons();
        this.initializePlayersCardVBox();
        this.initializePlayersCardVBoxMouseEvent();
        this.initializePlayersCardVBoxImage();
        this.initializeLabels();
        this.initializeButtons();
        this.initializePane();

        if(continueSaveGame) {
            // pass the information of the pokemon inside
            initializePokemonCardImage();
        }else{
            // generate it instead
            initializePokemonCardImage();
        }
        // play music
        ControllerUtil.playBackgroundMusic(getClass().getResource("resources/fxml/assets/battle.mp3"));
        updatePokemonDetailsOnCard();
        revealEffect();
        clearText("Click any pokemon to view their stats/ click any button to perform action!");
    }
}
