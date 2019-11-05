package Program;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static Program.ControllerUtil.primaryScreenBounds;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        double width = ControllerUtil.getScreenWidth();
        double height = ControllerUtil.getScreenHeight();

        Parent root = FXMLLoader.load(getClass().getResource("resources/fxml/MenuPage.fxml"));
        Scene scene = new Scene(root, width, height);

        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);

        primaryStage.setFullScreen(true);
        primaryStage.setTitle("OOP Pokemon 2019");
        primaryStage.setScene(scene);
        primaryStage.show();

        // For switch scene purpose
        ControllerUtil.stage = primaryStage;

        // play music
        ControllerUtil.playMusic(getClass().getResource("resources/fxml/assets/theme.mp3"));
    }

    public static void main(String[] args) {
        launch(args);
    }


}
