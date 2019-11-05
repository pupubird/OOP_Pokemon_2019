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
    public void initialize() throws FileNotFoundException {
        int widthRatio = 10;
        int heightRatio = 24;
        int imageWidthRatio = 5;
        int imageHeightRatio = 1;

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
        menuLogo.setFitHeight(height/imageHeightRatio);

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
