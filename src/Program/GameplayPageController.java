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

public class GameplayPageController {
    @FXML
    public SplitPane GameplayPagePane;
    @FXML
    public VBox gameplayZonePane;
    @FXML
    public HBox player1groupPane;
    @FXML
    public HBox player2groupPane;

    @FXML
    public GridPane PokemonPropertiesPane;
    @FXML
    public HBox ButtonHBox;
    @FXML
    public HBox ButtonHBox2;

    @FXML
    public VBox ActionButtonPane;

    @FXML
    public Button AttackButton;
    @FXML
    public Button RechargeButton;
    @FXML
    public Button TrainButton;
    @FXML
    public Button SaveExitButton;

    @FXML
    public Label pokemonName;
    @FXML
    public Label type;
    @FXML
    public Label stage;
    @FXML
    public Label experience;
    @FXML
    public Label energy;
    @FXML
    public Label energyColor;
    @FXML
    public Label attackPoint;
    @FXML
    public Label resistancePoint;
    @FXML
    public Label status;

    @FXML
    public VBox player1card1;
    @FXML
    public VBox player1card2;
    @FXML
    public VBox player1card3;
    @FXML
    public VBox player1card4;
    @FXML
    public VBox player1card5;
    @FXML
    public VBox player1card6;

    @FXML
    public VBox player2card1;
    @FXML
    public VBox player2card2;
    @FXML
    public VBox player2card3;
    @FXML
    public VBox player2card4;
    @FXML
    public VBox player2card5;
    @FXML
    public VBox player2card6;

    @FXML
    public Label player1card1Hp;
    @FXML
    public Label player1card2Hp;
    @FXML
    public Label player1card3Hp;
    @FXML
    public Label player1card4Hp;
    @FXML
    public Label player1card5Hp;
    @FXML
    public Label player1card6Hp;

    @FXML
    public Label player2card1Hp;
    @FXML
    public Label player2card2Hp;
    @FXML
    public Label player2card3Hp;
    @FXML
    public Label player2card4Hp;
    @FXML
    public Label player2card5Hp;
    @FXML
    public Label player2card6Hp;

    @FXML
    public ImageView player2card1Image;
    @FXML
    public ImageView player2card2Image;
    @FXML
    public ImageView player2card3Image;
    @FXML
    public ImageView player2card4Image;
    @FXML
    public ImageView player2card5Image;
    @FXML
    public ImageView player2card6Image;

    @FXML
    public ImageView player1card1Image;
    @FXML
    public ImageView player1card2Image;
    @FXML
    public ImageView player1card3Image;
    @FXML
    public ImageView player1card4Image;
    @FXML
    public ImageView player1card5Image;
    @FXML
    public ImageView player1card6Image;

    private long lastSecond = -1;
    private VBox[][] playersCards;
    private PokemonBase[][] playersPokemons;
    private ImageView[][] playersCardImages;
    private Label[] pokemonDetailsPaneLabels;
    private Label[][] pokemonHpCardLabels;
    private Button[] buttons;
    private String currentButtonState;


    private void playerCardClicked(int[] cardIndex){
        switch (currentButtonState){
            case "normal":
                showPokemonDetailOnPane(cardIndex);
                break;
            case "attack":

                break;
            case "recharge":
                break;
            case "saveExit":
                break;
        }
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

    private void showPokemonDetailsOnCard(){

        // get every pokemon Hp and set to the respective card Label
        for(int i = 0; i < pokemonHpCardLabels.length; i++){
            for(int j = 0; j < pokemonHpCardLabels[i].length; j++){
                pokemonHpCardLabels[i][j].setText(
                        playersPokemons[i][j].getName()+"\n"+
                        "HP: "+Integer.toString(playersPokemons[i][j].getHp())
                );
            }
        }
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
                    energy.setText("To view a pokemon status, click on the pokemon!");
                    for(Button button: buttons){
                        button.setDisable(false);
                    }
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

    private void setPokemonCardImage(){
        // if no input is specify, generate it
        for(ImageView[] playersCard: playersCardImages){
            for(ImageView cardImage: playersCard){
                int imageIndex = playersPokemons[0][0].generateInt(1,12);
                cardImage.setImage(
                        new Image(getClass().getResource("resources/fxml/assets/pokemon"+imageIndex+".png").toString())
                );

            }
        }
    }
    private void setPokemonCardImage(String[][] playersCardImagesString){
        // input is specified = game is load from previous game
        for(int i = 0; i < playersCardImages.length; i++){
            for(int j = 0; j < playersCardImages[i].length;j++){
                playersCardImages[i][j].setImage(
                        new Image(getClass().getResource("resources/fxml/assets/pokemon"
                                +playersCardImagesString[i][j]+".png").toString())
                );

            }
        }

    }

    public void initialize(){

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

        playersCards = new VBox[][]{
                {
                    player1card1,player1card2,player1card3, player1card4,player1card5,player1card6
                },
                {
                    player2card1,player2card2,player2card3,player2card4,player2card5,player2card6
                }
        };

        playersCardImages = new ImageView[][]{
                {
                    player1card1Image,
                    player1card2Image,
                    player1card3Image,
                    player1card4Image,
                    player1card5Image,
                    player1card6Image,
                },{
                    player2card1Image,
                    player2card2Image,
                    player2card3Image,
                    player2card4Image,
                    player2card5Image,
                    player2card6Image,
                }
        };
        currentButtonState = "normal";

        for(VBox[] player: playersCards){
            for(VBox card: player){
                card.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
                    playerCardClicked(getCardIndex(card.getId()));
                });
            }
        }
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


        buttons = new Button[]{AttackButton,RechargeButton,TrainButton,SaveExitButton};

        double width = ControllerUtil.getScreenWidth();
        double height = ControllerUtil.getScreenHeight();

        double bottomPaneHeightRatio = 0.3;
        double PokemonPropertiesPaneWidthRatio = 0.7;

        double pokemonCardWidthRatio = 0.142;
        double pokemonCardHeightRatio = 0.3;

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

        ActionButtonPane.setStyle("-fx-background-color: #4eb9f7;");

        ButtonHBox.setMinHeight(height*bottomPaneHeightRatio*0.5);
        ButtonHBox.setSpacing(width*(1-PokemonPropertiesPaneWidthRatio)*0.125);
        ButtonHBox2.setMinHeight(height*bottomPaneHeightRatio*0.5);
        ButtonHBox2.setSpacing(width*(1-PokemonPropertiesPaneWidthRatio)*0.125);

        AttackButton.setMinWidth(width*(1-PokemonPropertiesPaneWidthRatio)*0.25);
        RechargeButton.setMinWidth(width*(1-PokemonPropertiesPaneWidthRatio)*0.25);
        TrainButton.setMinWidth(width*(1-PokemonPropertiesPaneWidthRatio)*0.25);
        SaveExitButton.setMinWidth(width*(1-PokemonPropertiesPaneWidthRatio)*0.25);

        player1groupPane.setMinHeight(height*(1-bottomPaneHeightRatio)*0.5);
        player2groupPane.setMinHeight(height*(1-bottomPaneHeightRatio)*0.5);

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

        // disable button, enable when all cards are revealed.
        for(Button button: buttons){
            button.setDisable(true);
        }

        // set for all pokemon card width and height
        for(VBox[] player: playersCards){
            for(VBox card: player){
                card.setMinWidth(width*pokemonCardWidthRatio);
                card.setMinHeight(height*pokemonCardHeightRatio);
                card.setVisible(false);
            }
        }
        clearText("Generating your pokemons!");
        setPokemonCardImage();

        // play music
        ControllerUtil.playBackgroundMusic(getClass().getResource("resources/fxml/assets/battle.mp3"));
        showPokemonDetailsOnCard();
        revealEffect();
    }
}
