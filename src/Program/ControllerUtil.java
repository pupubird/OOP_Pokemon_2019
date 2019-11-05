package Program;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

class ControllerUtil {
    static Stage stage;
    static MediaPlayer audioplayer;
    static void switchToScene(URL url){
        Parent root = null;
        try {
            root = FXMLLoader.load(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.getScene().setRoot(root);
    }
    static Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    static double getScreenWidth(){
        return primaryScreenBounds.getWidth();
    }
    static double getScreenHeight(){
        return primaryScreenBounds.getHeight();

    }
    static void playMusic(URL location){
        // play music
        Media media = new Media(location.toString());
        audioplayer = new MediaPlayer(media);
        audioplayer.setAutoPlay(true);
        audioplayer.setCycleCount(MediaPlayer.INDEFINITE);

    }
}
