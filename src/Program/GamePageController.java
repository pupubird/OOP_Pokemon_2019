package Program;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class GamePageController {
    @FXML
    public AnchorPane GamePagePane;

    @FXML
    public void returnToMenu(){
        ControllerUtil.playEffect(getClass().getResource("resources/fxml/assets/mouseClick.mp3"));
        ControllerUtil.playBackgroundMusic(getClass().getResource("resources/fxml/assets/theme.mp3"));
        ControllerUtil.switchToScene(getClass().getResource("resources/fxml/MenuPage.fxml"));
    }

    public void initialize(){
        double width = ControllerUtil.getScreenWidth();
        double height = ControllerUtil.getScreenHeight();

        GamePagePane.setMinWidth(width);
        GamePagePane.setMinHeight(height);
        GamePagePane.setMaxWidth(width);
        GamePagePane.setMaxHeight(height);

        GamePagePane.setStyle(
                "-fx-background-image:url("+getClass().getResource("resources/fxml/assets/background.jpg")+");" +
                        "-fx-background-repeat:no-repeat;" +
                        "-fx-background-size:cover;"
        );
        // play music
        ControllerUtil.playBackgroundMusic(getClass().getResource("resources/fxml/assets/battle.mp3"));
    }
}
