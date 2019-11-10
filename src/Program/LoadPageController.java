package Program;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;

public class LoadPageController {
    @FXML
    public VBox LoadPagePane;
    @FXML
    public ImageView menuLogo;
    @FXML
    public ImageView returnButtonImage;
    @FXML
    public Button returnButton;

    private URL clickingEffect = getClass().getResource("resources/fxml/assets/mouseClick.mp3");

    @FXML
    public void returnToMenu(){
        ControllerUtil.playEffect(clickingEffect);
        ControllerUtil.playBackgroundMusic(getClass().getResource("resources/fxml/assets/theme.mp3"));
        ControllerUtil.switchToScene(getClass().getResource("resources/fxml/MenuPage.fxml"));
    }

    public void initialize(){
        double widthRatio = 10;
        double heightRatio = 24;
        double imageWidthRatio = 3.25;
        double width = ControllerUtil.getScreenWidth();
        double height = ControllerUtil.getScreenHeight();

        LoadPagePane.setMinWidth(width);
        LoadPagePane.setMinHeight(height);
        LoadPagePane.setMaxWidth(width);
        LoadPagePane.setMaxHeight(height);

        LoadPagePane.setStyle(
                "-fx-background-image:url("+getClass().getResource("resources/fxml/assets/background.jpg")+");" +
                        "-fx-background-repeat:no-repeat;" +
                        "-fx-background-size:cover;"
        );

        menuLogo.setFitWidth(width/imageWidthRatio);

        returnButton.setPrefWidth(width/widthRatio);
        returnButton.setPrefHeight(height/heightRatio);
        returnButtonImage.setFitHeight(returnButton.getPrefHeight()*2);

    }
}