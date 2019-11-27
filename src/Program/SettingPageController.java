package Program;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.net.URL;


/**
 * Setting Page Controller
 */
public class SettingPageController {

    /**
     * Value for background music
     */
    private double volume = 0.5;

    /**
     * The VBox class for setting page pane window
     */
    @FXML
    public VBox SettingPagePane;
    /**
     * The pokemon logo that's set to the middle of the window
     */
    public ImageView menuLogo;
    /**
     * The button of volume down
     */
    public Button volumeDownButton;
    /**
     * The button of volume down
     */
    public Button volumeUpButton;
    /**
     * The image beside the button
     */
    public ImageView returnButtonImage;
    /**
     * The label to show current volume
     */
    public Label volumeValueLabel;
    /**
     * The return button to navigate
     */
    public Button returnButton;

    /**
     * The URL of clicking sound effect
     */
    private URL clickingEffect = getClass().getResource("resources/fxml/assets/mouseClick.mp3");


    /**
     * To decrease the volume, call this function
     */
    public void volumeDown() {

        if ( volume > 0.01 ) {

            volume -= 0.05;
            volumeValueLabel.setText(translateVolume(volume));
            ControllerUtil.audioplayer.setVolume(volume);

        }

    }


    /**
     * To increase the volume, call this function
     */
    public void volumeUp() {

        if ( volume < 1 ) {
            volume += 0.05;
            volumeValueLabel.setText(translateVolume(volume));
            ControllerUtil.audioplayer.setVolume(volume);
        }

    }


    /**
     * @param vol the volume for the background music
     * @return the volume percentage
     */
    private String translateVolume(double vol) {

        // return volume in percentage format (45%)
        return ((int) Math.round(vol * 100)) + "%";

    }


    /**
     * Navigate to menu page
     */
    public void navigateToMenuPage() {

        ControllerUtil.playEffect(clickingEffect);
        ControllerUtil.switchToScene(getClass().getResource("resources/fxml/MenuPage.fxml"));

    }


    /**
     * To initialize setting page controller
     */
    public void initialize() {

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
