package Program;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static Program.ControllerUtil.primaryScreenBounds;

public class Main extends Application {


    /**
     * @param primaryStage Stage class that will be used as window
     * @throws Exception for all exceptions
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        // the width and height is determined by user screen size
        double width = ControllerUtil.getScreenWidth();
        double height = ControllerUtil.getScreenHeight();

        // Load menu page fxml as landing page
        Parent root = FXMLLoader.load(getClass().getResource("resources/fxml/MenuPage.fxml"));
        Scene scene = new Scene(root, width, height);

        // setting the stage size
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);

        // enter full screen and remove full screen exit hint
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("OOP Pokemon 2019");
        primaryStage.setScene(scene);
        primaryStage.show();

        // initialize
        ControllerUtil.stage = primaryStage;

        // play music
        ControllerUtil.playBackgroundMusic(getClass().getResource("resources/fxml/assets/theme.mp3"));

    }

    /**
     * @param args environment arguments
     */
    public static void main(String[] args) {
        launch(args);
    }


}
