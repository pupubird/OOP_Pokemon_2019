package Program;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
    public Button exitButton;

    @FXML
    public ImageView startButtonImage;
    @FXML
    public ImageView settingButtonImage;
    @FXML
    public ImageView exitButtonImage;

    @FXML
    public void navigateToSettingPage(){
        ControllerUtil.switchToScene(getClass().getResource("resources/fxml/SettingPage.fxml"));
    }

    @FXML
    public void exitProgram(){
        System.exit(0);
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
        exitButton.setPrefWidth(width/widthRatio);

        startButton.setPrefHeight(height/heightRatio);
        settingButton.setPrefHeight(height/heightRatio);
        exitButton.setPrefHeight(height/heightRatio);

        startButtonImage.setFitHeight(startButton.getPrefHeight()*2);
        settingButtonImage.setFitHeight(settingButton.getPrefHeight()*2);
        exitButtonImage.setFitHeight(exitButton.getPrefHeight()*2);
    }

}
