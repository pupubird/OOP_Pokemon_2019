package Program;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;


public class SettingPageController {
    @FXML
    public VBox SettingPagePane;
    @FXML
    public ImageView menuLogo;
    @FXML
    public ImageView returnButtonImage;
    @FXML
    public Button returnButton;

    private URL clickingEffect = getClass().getResource("resources/fxml/assets/mouseClick.mp3");

    @FXML
    public void navigateToMenuPage(){
        ControllerUtil.playEffect(clickingEffect);
        new MenuPageController();
        ControllerUtil.switchToScene(getClass().getResource("resources/fxml/MenuPage.fxml"));
    }

    public void initialize(){
        double widthRatio = 10;
        double heightRatio = 24;
        double imageWidthRatio = 3.25;

        double width = ControllerUtil.getScreenWidth();
        double height = ControllerUtil.getScreenHeight();

        SettingPagePane.setMinWidth(width);
        SettingPagePane.setMinHeight(height);
        SettingPagePane.setMaxWidth(width);
        SettingPagePane.setMaxHeight(height);

        SettingPagePane.setStyle(
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
