package Program;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.net.URL;


/**
 * Game over page controller
 */
public class GameOverPageController {

    /**
     * The VBox class for game over page window
     */
    @FXML
    public VBox GameOverPagePane;
    /**
     * The ImageView class for the pokemon logo on the middle of the window
     */
    public ImageView menuLogo;
    /**
     * The ImageView class for image that's fixed behind the button
     */
    public ImageView returnButtonImage;
    /**
     * The button
     */
    public Button returnButton;

    /**
     * The URL for clicking sound effect
     */
    private URL clickingEffect = getClass().getResource("resources/fxml/assets/mouseClick.mp3");


    /**
     * A function to navigate to menu page
     */
    public void navigateToMenuPage() {

        ControllerUtil.playEffect(clickingEffect);
        ControllerUtil.playBackgroundMusic(getClass().getResource("resources/fxml/assets/theme.mp3"));
        ControllerUtil.switchToScene(getClass().getResource("resources/fxml/MenuPage.fxml"));

    }


    /**
     * Initialize game over page controller
     */
    public void initialize() {

        double widthRatio = 10;
        double heightRatio = 24;
        double imageWidthRatio = 3.25;

        double width = ControllerUtil.getScreenWidth();
        double height = ControllerUtil.getScreenHeight();

        GameOverPagePane.setMinWidth(width);
        GameOverPagePane.setMinHeight(height);
        GameOverPagePane.setMaxWidth(width);
        GameOverPagePane.setMaxHeight(height);

        GameOverPagePane.setStyle(
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
