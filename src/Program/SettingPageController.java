package Program;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;


public class SettingPageController {
    private double volume = 0.5;

    @FXML
    public VBox SettingPagePane;
    public ImageView menuLogo;
    public Button volumeDownButton;
    public Button volumeUpButton;
    public Label volumeValueLabel;
    public ImageView returnButtonImage;
    public Button returnButton;

    private URL clickingEffect = getClass().getResource("resources/fxml/assets/mouseClick.mp3");

    public void volumeDown() {
        if (volume > 0.01) {

            volume -= 0.05;

            volumeValueLabel.setText(
                    translateVolume(volume)
            );
            ControllerUtil.audioplayer.setVolume(volume);

        }
    }

    public void volumeUp() {
        if (volume < 1) {

            volume += 0.05;

            volumeValueLabel.setText(
                    translateVolume(volume)
            );
            ControllerUtil.audioplayer.setVolume(volume);

        }
    }

    public String translateVolume(double vol) {
        // return volume in percentage format (45%)
        String volumePercentage = ((int) Math.round(vol * 100)) + "%";

        return volumePercentage;
    }

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
