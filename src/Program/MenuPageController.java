package Program;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


public class MenuPageController {
    @FXML
    public VBox MenuPagePane;

    @FXML
    public ImageView menuLogo;

    @FXML
    public Button startButton;
    @FXML
    public Button settingButton;
    @FXML
    public Button loadButton;
    @FXML
    public Button exitButton;

    @FXML
    public ImageView startButtonImage;
    @FXML
    public ImageView settingButtonImage;
    @FXML
    public ImageView loadButtonImage;
    @FXML
    public ImageView exitButtonImage;

    private int vibratePixel = 10;
    private long lastSecond = -1;

    @FXML
    public void navigateToSettingPage(){
        ControllerUtil.playEffect(getClass().getResource("resources/fxml/assets/mouseClick.mp3"));
        ControllerUtil.switchToScene(getClass().getResource("resources/fxml/SettingPage.fxml"));
    }
    @FXML
    public void navigateToGamePage(){
        ControllerUtil.playEffect(getClass().getResource("resources/fxml/assets/mouseClick.mp3"));
        ControllerUtil.switchToScene(getClass().getResource("resources/fxml/GameplayPage.fxml"));
    }

    @FXML
    public void navigateToLoadPage(){
        ControllerUtil.playEffect(getClass().getResource("resources/fxml/assets/mouseClick.mp3"));
        ControllerUtil.switchToScene(getClass().getResource("resources/fxml/LoadPage.fxml"));
    }

    @FXML
    public void exitProgram() {
        System.exit(0);
    }

    private void vibrateEffect() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (vibratePixel > 5) {
                            vibratePixel = -5;
                        }
                        if (lastSecond == -1) {
                            lastSecond = now;
                        }
                        // update every 0.5s
                        if (now - lastSecond >= 500000000) {
                            startButtonImage.setTranslateY(startButtonImage.getTranslateY() + vibratePixel);
                            settingButtonImage.setTranslateY(settingButtonImage.getTranslateY() + vibratePixel);
                            loadButtonImage.setTranslateY(loadButtonImage.getTranslateY() + vibratePixel);
                            exitButtonImage.setTranslateY(exitButtonImage.getTranslateY() + vibratePixel);
                            vibratePixel += 10;
                            lastSecond = now;
                        }
                    }
                });
            }
        }.start();
    }

    @FXML
    public void initialize() {
        double widthRatio = 10;
        double heightRatio = 24;
        double imageWidthRatio = 3.25;

        double width = ControllerUtil.getScreenWidth();
        double height = ControllerUtil.getScreenHeight();

        MenuPagePane.setMinWidth(width);
        MenuPagePane.setMinHeight(height);
        MenuPagePane.setMaxWidth(width);
        MenuPagePane.setMaxHeight(height);

        MenuPagePane.setStyle(
                "-fx-background-image:url("+getClass().getResource("resources/fxml/assets/background.jpg")+");" +
                        "-fx-background-repeat:no-repeat;" +
                        "-fx-background-size:cover;"
        );

        menuLogo.setFitWidth(width/imageWidthRatio);

        startButton.setPrefWidth(width/widthRatio);
        settingButton.setPrefWidth(width/widthRatio);
        loadButton.setPrefWidth(width/widthRatio);
        exitButton.setPrefWidth(width/widthRatio);

        startButton.setPrefHeight(height/heightRatio);
        settingButton.setPrefHeight(height/heightRatio);
        loadButton.setPrefHeight(height/heightRatio);
        exitButton.setPrefHeight(height/heightRatio);

        startButtonImage.setFitHeight(startButton.getPrefHeight()*2);
        settingButtonImage.setFitHeight(settingButton.getPrefHeight()*2);
        loadButtonImage.setFitHeight(loadButton.getPrefHeight()*2);
        exitButtonImage.setFitHeight(exitButton.getPrefHeight()*2);

        vibrateEffect();
    }



}
