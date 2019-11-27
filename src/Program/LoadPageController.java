package Program;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.net.URL;


/**
 * Load Page Controller
 */
public class LoadPageController {

    /**
     * The VBox class for load page pane window
     */
    @FXML
    public VBox LoadPagePane;
    /**
     * The ImageView class for pokemon logo in the middle of the window
     */
    public ImageView menuLogo;
    /**
     * Coming soon image
     */
    public ImageView comingSoonImage;
    /**
     * The image that's fixed beside the button
     */
    public ImageView returnButtonImage;
    /**
     * The return button
     */
    public Button returnButton;
    /**
     * The URL for clicking sound effect
     */
    private URL clickingEffect = getClass().getResource("resources/fxml/assets/mouseClick.mp3");


    /**
     * A function to navigate to menu
     */
    public void returnToMenu() {
        ControllerUtil.playEffect(clickingEffect);
        ControllerUtil.switchToScene(getClass().getResource("resources/fxml/MenuPage.fxml"));
    }


    /**
     * Initialize load page controller
     */
    public void initialize() {

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