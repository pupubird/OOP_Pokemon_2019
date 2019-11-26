package Program;

import Program.PokemonModel.AttackTypePokemon;
import Program.PokemonModel.DefenseTypePokemon;
import Program.PokemonModel.FairyTypePokemon;
import Program.PokemonModel.PokemonBase;
import javafx.animation.AnimationTimer;
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


/**
 * Game play page controller
 */
public class GameplayPageController {

    private boolean currentRoundIsComputer = false;
    public static ArrayList<VBox> buttonEventQueue = new ArrayList<>();
    private VBox[][] playersCards;
    private PokemonBase[][] playersPokemons;
    private ImageView[][] playersCardImages;
    private Label[] pokemonDetailsPaneLabels;
    private Label[][] pokemonHpCardLabels;
    private Button[] buttons;
    private String currentButtonState;

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
    /**
     * @param cardIndex, the clicked card's position in the array
     */
    private void buttonEventHandler(int[] cardIndex)  {

        String returnedLog = "Player 1: \n";
        // which action is clicked
        switch (currentButtonState){
            case "normal": // no button is clicked, this is use for checking pokemon stat.
                showPokemonDetailOnPane(cardIndex);
                break;

            case "attack":
                // verify player chosen his own pokemon, add to event queue for next event calls;
                int[][] pokemonIndexes = attackVerify(cardIndex);
                if(pokemonIndexes[0][0] != -1){
                    returnedLog += attack( pokemonIndexes[0] , pokemonIndexes[1] );
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
                saveExit();
                break;

        }
        // update pokemon details
        updatePokemonDetailsOnCard();
    }
    /**
     * @param cardIndex, the clicked card's position in the array
     * @return the clicked player's card index and the clicked target (opponent) card's index on the array.
     */
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

            buttonEventQueue = new ArrayList<>();
            return new int[][]{{-1},{-1}} ;

        } else if ( cardIndex[0] == 0 && buttonEventQueue.size() == 0) {
            buttonEventQueue.add(playersCards[cardIndex[0]][cardIndex[1]]);
        } else if ( cardIndex[0] == 1 && buttonEventQueue.size() == 1) {
            buttonEventQueue.add(playersCards[cardIndex[0]][cardIndex[1]]);
        }

        // remove duplicate
        if( buttonEventQueue.size() == 2 ) {

            if ( buttonEventQueue.get(0).getId().equals(buttonEventQueue.get(1).getId()) ) {
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
            if( buttonEventQueue.size() == 2 ) {

                if ( getCardIndex(buttonEventQueue.get(1).getId())[0] == 1 ) {

                    disableButton(true);
                    firstCardIndex = getCardIndex(buttonEventQueue.get(0).getId());
                    secondCardIndex = getCardIndex(buttonEventQueue.get(1).getId());

                    disableButton(false);
                    // event queue cycle done, back to status quo
                    buttonEventQueue = new ArrayList<>();

                }
            }
        }

        return new int[][]{
                firstCardIndex,
                secondCardIndex
        };

    }


    /**
     * @param indexPokemonFrom, the player card.
     * @param indexPokemonTo, the attack target card.
     * @return a log for all the actions performed.
     */
    private String attack(int[] indexPokemonFrom, int[] indexPokemonTo) {

        String pokemonReturnedLog="";

        PokemonBase attackingPokemon =  playersPokemons[indexPokemonFrom[0]][indexPokemonFrom[1]];
        PokemonBase receivingPokemon = playersPokemons[indexPokemonTo[0]][indexPokemonTo[1]];

        if (/*if it is idle*/ attackingPokemon.getEffectLeftRound() > 0  ) {

            buttonEventQueue = new ArrayList<>();
            // if computer get to here but it faces this error, call the function recursively again until it success.
            if (currentRoundIsComputer) {
                pokemonReturnedLog += computerTurn();
            } else{
                pokemonReturnedLog = "Pokemon is in idled for: " + attackingPokemon.getEffectLeftRound() + " round.";
            }
        }else {
            // check if the pokemon is dead
            if ( attackingPokemon.getHp() < 0 ) {

                if (currentRoundIsComputer) {
                    pokemonReturnedLog += computerTurn();
                }

            }

            else {

                String classType = attackingPokemon.getClass().getName();

                if ( classType.contains("Attack") ) {

                    AttackTypePokemon attackTypePokemon = (AttackTypePokemon) attackingPokemon;
                    pokemonReturnedLog = attackTypePokemon.attackTypelaunchAttack(receivingPokemon);

                } else if (classType.contains("Fairy")) {

                    FairyTypePokemon fairyTypePokemon = (FairyTypePokemon) attackingPokemon;
                    pokemonReturnedLog = fairyTypePokemon.fairyTypeLaunchAttack(receivingPokemon);

                } else {

                    // defense and other types of pokemons share the same launchAttack function.
                    pokemonReturnedLog = attackingPokemon.launchAttack(receivingPokemon);
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

                } else if ( currentRoundIsComputer ) {

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


    /**
     * @param indexPokemonFrom, the player card.
     * @param indexPokemonTo, the attack target card.
     */
    private void attackEffect(int[] indexPokemonFrom, int[] indexPokemonTo) {

        VBox attackingCard = playersCards[indexPokemonFrom[0]][indexPokemonFrom[1]];
        VBox receivingCard = playersCards[indexPokemonTo[0]][indexPokemonTo[1]];
        PokemonBase attackingPokemon = playersPokemons[indexPokemonFrom[0]][indexPokemonFrom[1]];

        double spacing = 10;

        new AnimationTimer() {

            private boolean doneAnimation = false;
            double currentX = 0, currentY = 0;
            // n = index, t = target, f = from
            // outputX = (nt - nf)*(space + width)
            // outputY = (height/2)+space
            double targetXIndex = indexPokemonTo[1] - indexPokemonFrom[1];
            double targetXSpaceWidth = spacing + attackingCard.getWidth();
            double outputX = targetXIndex * targetXSpaceWidth;

            double targetY = (receivingCard.getHeight()/2);
            double outputY = currentRoundIsComputer ? ( targetY + spacing ) : -(targetY + spacing);

            double pixelPerFrameX = outputX/10, pixelPerFrameY = outputY/10;


            private boolean oneRoundDone = false;
            private long lastSecond = -1;
            @Override
            public void handle(long now) {

                disableButton(true);
                long secondPassed = 1000000000;
                currentX += pixelPerFrameX;
                currentY -= pixelPerFrameY;

                attackingCard.setTranslateX(currentX);
                attackingCard.setTranslateY(currentY);

                // below are the keyframes for animations
                if(!doneAnimation) {
                    // go forward
                    if ( abs(currentX) > abs(outputX) || abs(currentY) > abs(outputY) ) {

                        pixelPerFrameX = -pixelPerFrameX;
                        pixelPerFrameY = -pixelPerFrameY;

                        oneRoundDone = true;

                        if (lastSecond < 0) {
                            lastSecond = now;
                        }

                        if (attackingPokemon.getClass().getName().contains("Fairy")) {
                            ControllerUtil.playEffect(getClass().getResource("resources/fxml/assets/fairyAttack.mp3"));

                        } else {
                            ControllerUtil.playEffect(getClass().getResource("resources/fxml/assets/attack.mp3"));
                        }


                        // go backward
                    }

                    else if ( oneRoundDone ) {


                        // blinking effect
                        if (now - lastSecond < secondPassed * 0.0375) {
                            receivingCard.setVisible(false);
                        }
                        if (now - lastSecond > secondPassed * 0.075 && now - lastSecond < secondPassed * 0.1125) {
                            receivingCard.setVisible(true);
                        }

                        if (now - lastSecond > secondPassed * 0.1125 && now - lastSecond < secondPassed * 0.15) {
                            receivingCard.setVisible(false);
                        }

                        if (now - lastSecond > secondPassed * 0.15 && now - lastSecond < secondPassed * 0.2) {
                            receivingCard.setVisible(true);
                        }


                        // returned to original position
                        if ( abs(currentX) <= 5 && abs(currentY) <= 5 ) {

                            currentY = 0;
                            pixelPerFrameX = 0;
                            pixelPerFrameY = 0;
                            oneRoundDone = false;
                            doneAnimation = true;
                            // show visible if not
                            receivingCard.setVisible(true);

                        }
                    }
                }


                if ( doneAnimation ) {

                    updateOnGameActionOccured();

                    if ( now - lastSecond > secondPassed * 1.5 && now - lastSecond < secondPassed * 2 ) {

                        if ( currentRoundIsComputer ) {
                            clearText("Computer is thinking...");
                        }

                    }

                    if ( now - lastSecond > secondPassed * 2 ) {

                        lastSecond = -1;
                        this.stop();

                        if ( currentRoundIsComputer ) {
                            computerTurn();
                        }

                        disableButton(false);

                    }
                }
            }
        }.start();
    }


    /**
     * @param indexPokemon, the index of desire pokemon to recharge in the array
     * @return a log for all the actions performed.
     */
    private String recharge(int[] indexPokemon) {

        String pokemonReturnedLog = "";
        PokemonBase selectedPokemon = playersPokemons[indexPokemon[0]][indexPokemon[1]];


        if(/*if it is idle*/ selectedPokemon.getEffectLeftRound() > 0 ) {

            buttonEventQueue = new ArrayList<>();

            if ( currentRoundIsComputer ) {

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pokemonReturnedLog += computerTurn();

            }

            else{
                pokemonReturnedLog = "Pokemon is in idled for: " + selectedPokemon.getEffectLeftRound() + " round.";
            }


        }

        else {

            if ( selectedPokemon.getHp() < 0 ) {

                if (currentRoundIsComputer) {
                    pokemonReturnedLog += computerTurn();
                }

            }

            else {

                String cardDrawn = selectedPokemon.generateString(new String[]{"red", "blue", "yellow"});
                boolean recharged = false;

                if ( !currentRoundIsComputer ) {
                    updateOnGameRoundDone();
                }

                currentRoundIsComputer = !currentRoundIsComputer;


                if ( selectedPokemon.getColor().equals("colorless")
                        || selectedPokemon.getColor().equals(cardDrawn) ) {

                    rechargeEffect(indexPokemon, true);
                    selectedPokemon.setEnergy(selectedPokemon.getEnergy() + 5);
                    recharged = true;

                }

                String showCard = String.format("Card Drawn : %s", cardDrawn);

                if ( recharged ) {
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


    /**
     * @param indexPokemon, the index of desire pokemon to recharge in the array
     * @param show, show the animation if recharge success.
     */
    private void rechargeEffect(int[] indexPokemon, boolean show) {

        new AnimationTimer() {
            private long lastSecond = -1;
            @Override
            public void handle(long now) {

                long secondPassed = 1000000000;

                if ( lastSecond < 0 ) {
                    lastSecond = now;
                    disableButton(true);
                }

                if( show ) {

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


                if ( now - lastSecond > secondPassed * 1.5 && now - lastSecond < secondPassed * 2 ) {

                    if (currentRoundIsComputer) {
                        clearText("Computer is thinking...");
                    }

                }


                if( now - lastSecond > secondPassed * 2 ) {

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


    /**
     * @param indexPokemon, the index of desire pokemon to recharge in the array
     * @return a log of all the actions performed
     */
    private String train(int[] indexPokemon) {

        String pokemonReturnedLog = "";
        PokemonBase selectedPokemon = playersPokemons[indexPokemon[0]][indexPokemon[1]];

        if (/*if it is idle*/ selectedPokemon.getEffectLeftRound() > 0 ) {

            buttonEventQueue = new ArrayList<>();

            if (currentRoundIsComputer) {
                pokemonReturnedLog += computerTurn();
            } else {
                pokemonReturnedLog += "Pokemon is in idled for: " + selectedPokemon.getEffectLeftRound() + " round.";
            }

        }

        else if ( selectedPokemon.getHp() < 0 ) {
            if (currentRoundIsComputer) {
                pokemonReturnedLog += computerTurn();
            }
        }

        else {

            if ( selectedPokemon.getEnergy() < 5 ) {

                pokemonReturnedLog += selectedPokemon.getName() + " does not have enough energy (5) to be trained !";
                if (currentRoundIsComputer) {
                    pokemonReturnedLog += computerTurn();
                }

            }

            else {

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


    /**
     * @param indexPokemon, the index of desire pokemon to recharge in the array
     */
    private void trainEffect(int[] indexPokemon) {

        VBox selectedCard = playersCards[indexPokemon[0]][indexPokemon[1]];

        new AnimationTimer() {

            private long lastSecond = -1;
            @Override
            public void handle(long now) {

                long secondPassed = 1000000000;

                    if ( lastSecond < 0 ) {
                        lastSecond = now;
                        disableButton(true);
                    }

                    if (now - lastSecond < secondPassed * 0.0375) {
                        selectedCard.setVisible(false);
                    }
                    if (now - lastSecond > secondPassed * 0.075 && now - lastSecond < secondPassed * 0.1125) {
                        selectedCard.setVisible(true);
                    }
                    if (now - lastSecond > secondPassed * 0.1125 && now - lastSecond < secondPassed * 0.15) {
                        selectedCard.setVisible(false);
                    }
                    if(now - lastSecond > secondPassed * 0.15 && now - lastSecond < secondPassed * 0.2) {
                        selectedCard.setVisible(true);
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

    private void saveExit(){
        ControllerUtil.switchToScene(getClass().getResource("resources/fxml/MenuPage.fxml"));
        ControllerUtil.playBackgroundMusic(getClass().getResource("resources/fxml/assets/theme.mp3"));
    }


    /**
     * @return a log of all the actions performed
     */
    private String computerTurn() {

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
        if ( action >= 0 && action <= 50 ) {
            returnedLog += attack(indexPokemonFrom, indexPokemonTo);
        }
        // recharge has 25% chance
        else if ( action>50 && action <=75 ) {
            returnedLog += recharge(indexPokemonFrom);
        }
        // train has 25% chance
        else {
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

            private long lastSecond = -1;
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


    /**
     * @param promptText, the string that will be shown on the details pane label
     */
    private void clearText(String promptText) {

        for( Label label: pokemonDetailsPaneLabels ) {
            label.setText("");
        }

        //if there is any promptText
        energy.setText(promptText);
    }


    /**
     * @param cardID, the fx id of the card
     * @return the index of the card's pokemon in the array
     */
    private int[] getCardIndex(String cardID) {

        int[] playerCard = new int[3];
        String[] playerCardIndex;

        // getting card index based on the id
        if ( cardID.contains("player1") ) {
            playerCardIndex = cardID.split("player1card");
        } else {
            playerCardIndex = cardID.split("player2card");
            playerCard[0] = 1;
        }

        playerCard[1]=Integer.parseInt(playerCardIndex[1]) - 1;

        return playerCard;
    }


    /**
     * @param cardIndex,the index of the card's pokemon in the array
     */
    private void showPokemonDetailOnPane(int[] cardIndex) {

        String className = playersPokemons[cardIndex[0]][cardIndex[1]].getClass().getName();
        PokemonBase selectedPokemon = playersPokemons[cardIndex[0]][cardIndex[1]];

        String classType;

        if ( className.contains("Attack") ) {
            classType = "Attack";
        } else if ( className.contains("Defense") ) {
            classType = "Defense";
        } else {
            classType = "Fairy";
        }

        String attackPoints = classType.equals("Attack") ?
                Integer.toString(selectedPokemon.getAttackPoint())
                :"-";

        String resistancePoints = classType.equals("Defense") ?
                Integer.toString(selectedPokemon.getResistancePoints())
                :"-";

        pokemonDetailsPaneLabels[0].setText("Name: " + selectedPokemon.getName());
        pokemonDetailsPaneLabels[1].setText("Type: " + classType);
        pokemonDetailsPaneLabels[2].setText("Stage: " + Integer.toString(selectedPokemon.getStage()));
        pokemonDetailsPaneLabels[3].setText("Experience: "+ Integer.toString(selectedPokemon.getExp()));
        pokemonDetailsPaneLabels[4].setText("Energy: " + Integer.toString(selectedPokemon.getEnergy()));
        pokemonDetailsPaneLabels[5].setText("Energy Color: " + selectedPokemon.getColor());
        pokemonDetailsPaneLabels[6].setText("Attack Point: " + attackPoints);
        pokemonDetailsPaneLabels[7].setText("Resistance Point: " + resistancePoints);
        pokemonDetailsPaneLabels[8].setText("Status: " + selectedPokemon.getStatus());

    }

    private void updateOnGameRoundDone() {

        for( PokemonBase[] player: playersPokemons ) {

            for ( PokemonBase pokemon : player ) {

                if( pokemon.getEffectLeftRound() > 0 ) {
                    pokemon.setEffectLeftRound(pokemon.getEffectLeftRound() - 1);
                }
                if( pokemon.getEffectLeftRound() == 0 ) {
                    pokemon.setStatus("active");
                }

            }
        }
    }


    private void updateOnGameActionOccured() {

        // to check if any player wins
        for ( int i = 0; i < playersPokemons.length; i++ ) {

            int count = 0;

            for ( int j = 0; j < playersPokemons[i].length; j++ ) {

                if( playersPokemons[i][j].getHp() <= 0 ) {
                    playersCards[i][j].setVisible(false);
                }
                if(playersPokemons[i][j].getHp() <= 0) {
                    count ++;
                }
                if( count >= 3 ) {
                    ControllerUtil.playBackgroundMusic(getClass().getResource("resources/fxml/assets/gameover.mp3"));
                    ControllerUtil.switchToScene(getClass().getResource("resources/fxml/GameOverPage.fxml"));
                    break;
                }
            }
        }
    }


    private void updatePokemonDetailsOnCard() {

        // get every pokemon Hp and set to the respective card Label
        for ( int i = 0; i < pokemonHpCardLabels.length; i++ ) {
            for ( int j = 0; j < pokemonHpCardLabels[i].length; j++ ) {
                pokemonHpCardLabels[i][j].setText(
                        "\n"
                        +playersPokemons[i][j].getName()
                        +"\n"+ "HP: "+Integer.toString(playersPokemons[i][j].getHp())
                );
            }
        }
    }


    /**
     * @param disable, true to disable all the buttons
     */
    private void disableButton(boolean disable) {
        for(Button button: buttons){
            button.setDisable(disable);
        }
    }


    private void initializePokemonCardImage() {
        // if no input is specify, generate it
        for( ImageView[] playersCard: playersCardImages ) {

            for( ImageView cardImage: playersCard ) {

                int imageIndex = playersPokemons[0][0].generateInt(1,12);
                Image image =  new Image(getClass().getResource("resources/fxml/assets/pokemon"+imageIndex+".png").toString());
                cardImage.setImage(image);

            }

        }
    }

    private void initializePlayersPokemons() {

        // generate pokemon if not load saved game
        playersPokemons = new PokemonBase[][]{{
                new FairyTypePokemon("Treecko"),
                new AttackTypePokemon("Kyogre"),
                new DefenseTypePokemon("Torchic"),
                new AttackTypePokemon("Rayquaza"),
                new DefenseTypePokemon("Mudkip"),
                new DefenseTypePokemon("Groudon"),
        },{
                new FairyTypePokemon("Salamence"),
                new AttackTypePokemon("Latios"),
                new DefenseTypePokemon("Gyarados"),
                new AttackTypePokemon("Alakazam"),
                new DefenseTypePokemon("Latias"),
                new DefenseTypePokemon("Machamp"),
        }};

    }


    private void initializePlayersCardVBox() {

        double width = ControllerUtil.getScreenWidth();
        double height = ControllerUtil.getScreenHeight();
        double pokemonCardWidthRatio = 0.142;
        double pokemonCardHeightRatio = 0.3;

        playersCards = new VBox[][] {
                {
                        player1card1,player1card2,player1card3, player1card4,player1card5,player1card6
                },
                {
                        player2card1,player2card2,player2card3,player2card4,player2card5,player2card6
                }
        };


        for ( int i = 0; i < playersPokemons.length; i++ ) {

            for ( int j = 0; j < playersPokemons[i].length; j++ ) {

                // show different color on border base on the pokemon color
                if ( !playersPokemons[i][j].getColor().equals("colorless") ) {

                    playersCards[i][j].setStyle("-fx-border-radius:10;-fx-border-color:"+playersPokemons[i][j].getColor()+";"
                            +"-fx-border-width:5;"
                    );

                } else{
                    playersCards[i][j].setStyle("-fx-border-radius:10;");
                }
            }
        }


        // set for all pokemon card width and height
        for ( VBox[] player: playersCards ) {

            for ( VBox card: player ) {
                card.setMinWidth(width*pokemonCardWidthRatio);
                card.setMinHeight(height*pokemonCardHeightRatio);
                card.setVisible(false);
            }

        }
    }


    private void initializePlayersCardVBoxMouseEvent() {

        double width = ControllerUtil.getScreenWidth();
        double height = ControllerUtil.getScreenHeight();
        double pokemonCardWidthRatio = 0.142;
        double pokemonCardHeightRatio = 0.3;

        for ( int i = 0; i < playersCards.length; i++ )
            for (int j = 0; j < playersCards[i].length; j++) {

                VBox card = playersCards[i][j];
                // on mouse hover enter -> hoverEffect
                int a = i;
                int b = j;

                card.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> pokemonHpCardLabels[a][b].setTextFill(Color.RED));

                // on mouse hover exit -> remove hoverEffect
                card.addEventHandler(MouseEvent.MOUSE_EXITED, event -> pokemonHpCardLabels[a][b].setTextFill(Color.BLACK));

                // on mouse clicked -> show stats
                card.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> buttonEventHandler(getCardIndex(card.getId())));

                card.setMinWidth(width * pokemonCardWidthRatio);
                card.setMinHeight(height * pokemonCardHeightRatio);
            }
    }


    private void initializePlayersCardVBoxImage() {

        playersCardImages = new ImageView[][] {
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


    private void initializeLabels() {

        pokemonDetailsPaneLabels = new Label[] {
                pokemonName,type,stage,
                experience,energy,energyColor,
                attackPoint,resistancePoint,status
        };

        pokemonHpCardLabels = new Label[][] {
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


    private void initializeButtons() {

        double width = ControllerUtil.getScreenWidth();
        double height = ControllerUtil.getScreenHeight();

        double bottomPaneHeightRatio = 0.3;
        double PokemonPropertiesPaneWidthRatio = 0.7;

        buttons = new Button[]{ AttackButton, RechargeButton, TrainButton, SaveExitButton };

        // disable button, enable when all cards are revealed.
        for ( Button button: buttons ) {
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


        AttackButton.addEventFilter( MouseEvent.MOUSE_CLICKED, event -> {

            currentButtonState = currentButtonState.equals("attack") ? "normal" : "attack";

            if ( currentButtonState.equals("normal") ) {
                clearText("Click any pokemon to see their stats!");
            } else {
                clearText("Attack: Please select one of your own pokemon"
                        + "\n" + "Click Attack Button again to cancel this movement"
                );
            }
        });


        RechargeButton.addEventFilter( MouseEvent.MOUSE_CLICKED, event -> {

            currentButtonState = currentButtonState.equals("recharge") ? "normal" : "recharge";

            if ( currentButtonState.equals("normal") ) {
                clearText("Click any pokemon to see their stats!");
            } else {
                clearText("Recharge: Please select one of your own pokemon"
                        + "\n" + "Click Recharge Button again to cancel this movement"
                );
            }
        });


        TrainButton.addEventFilter( MouseEvent.MOUSE_CLICKED, event -> {

            currentButtonState = currentButtonState.equals("train") ? "normal" : "train";

            if ( currentButtonState.equals("normal") ) {
                clearText("Click any pokemon to see their stats!");
            } else {
                clearText("Train: Please select one of your own pokemon"
                        + "\n" + "Click Train Button again to cancel this movement"
                );
            }
        });

    }


    private void initializePane() {

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


    public void initialize() {

        currentButtonState = "normal";

        this.initializePlayersPokemons();
        this.initializePlayersCardVBox();
        this.initializePlayersCardVBoxMouseEvent();
        this.initializePlayersCardVBoxImage();
        this.initializeLabels();
        this.initializeButtons();
        this.initializePane();
        this.initializePokemonCardImage();
        // play music
        ControllerUtil.playBackgroundMusic(getClass().getResource("resources/fxml/assets/battle.mp3"));
        updatePokemonDetailsOnCard();
        revealEffect();
        clearText("Click any pokemon to view their stats/ click any button to perform action!");

    }


}
