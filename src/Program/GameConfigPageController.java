package Program;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class GameConfigPageController {
    @FXML
    public AnchorPane GameConfigPagePane;

    @FXML
    public void returnToMenu(){
        ControllerUtil.playEffect(getClass().getResource("resources/fxml/assets/mouseClick.mp3"));
        ControllerUtil.playBackgroundMusic(getClass().getResource("resources/fxml/assets/theme.mp3"));
        ControllerUtil.switchToScene(getClass().getResource("resources/fxml/GameplayPage.fxml"));
    }

    public void initialize(){
        double width = ControllerUtil.getScreenWidth();
        double height = ControllerUtil.getScreenHeight();

        GameConfigPagePane.setMinWidth(width);
        GameConfigPagePane.setMinHeight(height);
        GameConfigPagePane.setMaxWidth(width);
        GameConfigPagePane.setMaxHeight(height);

        GameConfigPagePane.setStyle(
                "-fx-background-image:url("+getClass().getResource("resources/fxml/assets/background.jpg")+");" +
                        "-fx-background-repeat:no-repeat;" +
                        "-fx-background-size:cover;"
        );
        // play music
        ControllerUtil.playBackgroundMusic(getClass().getResource("resources/fxml/assets/config.mp3"));
    }
}
