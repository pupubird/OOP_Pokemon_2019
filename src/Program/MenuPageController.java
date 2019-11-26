package Program;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


public class MenuPageController {

    // initialized variables with fx:id in fxml file
    @FXML
    public VBox MenuPagePane;
    public ImageView menuLogo;
    public Button startButton, settingButton, loadButton, exitButton;
    public ImageView startButtonImage, settingButtonImage, loadButtonImage, exitButtonImage;

    public void navigateToSettingPage(){
        ControllerUtil.playEffect(getClass().getResource("resources/fxml/assets/mouseClick.mp3"));
        ControllerUtil.switchToScene(getClass().getResource("resources/fxml/SettingPage.fxml"));
    }
    public void navigateToGamePage() {
        ControllerUtil.playEffect(getClass().getResource("resources/fxml/assets/mouseClick.mp3"));
        ControllerUtil.switchToScene(getClass().getResource("resources/fxml/GameplayPage.fxml"));
    }
    public void navigateToLoadPage() {
        ControllerUtil.playEffect(getClass().getResource("resources/fxml/assets/mouseClick.mp3"));
        ControllerUtil.switchToScene(getClass().getResource("resources/fxml/LoadPage.fxml"));
    }


    public void exitProgram() {
        System.exit(0);
    }


    private void vibrateEffect() {

        new AnimationTimer() {
            // vibrate pixel range
            private int vibratePixel = 10;
            // for counting timestamp
            private long lastSecond = -1;
            @Override
            public void handle(long now) {
                // for non-blocking javafx thread, create a new runnable.
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        // initialize
                        if (lastSecond == -1) {
                            lastSecond = now;
                        }

                        if (vibratePixel > 5) {
                            vibratePixel = -5;
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

        startButtonImage.setFitHeight(startButton.getPrefHeight() * 2);
        settingButtonImage.setFitHeight(settingButton.getPrefHeight() * 2);
        loadButtonImage.setFitHeight(loadButton.getPrefHeight() * 2);
        exitButtonImage.setFitHeight(exitButton.getPrefHeight() * 2);

        vibrateEffect();

    }


}
