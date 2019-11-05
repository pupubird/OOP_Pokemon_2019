package Program;

import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class SettingPageController {
    @FXML
    public Button button1;
    @FXML
    public void button1Handler(){
        button1.setText("I am clicked!");
    }
    @FXML
    public void button2Handler(){
        ControllerUtil.switchToScene(getClass().getResource("resources/fxml/LandingPage.fxml"));
    }
}
