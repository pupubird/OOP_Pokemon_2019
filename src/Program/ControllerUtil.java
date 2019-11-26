package Program;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;


class ControllerUtil {

    static Stage stage;
    static MediaPlayer audioplayer;
    static Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();


    /**
     * @param url the URL class of the fxml file, use getClass.getResources().
     */
    static void switchToScene(URL url) {

        Parent root = null;
        try {
            // setting the Parent class to the url passed.
            root = FXMLLoader.load(url);

        } catch ( IOException e ) {
            e.printStackTrace();
        }
        // use current stage class, and switch the root scene.
        stage.getScene().setRoot(root);

    }


    static double getScreenWidth() {
        return primaryScreenBounds.getWidth();
    }


    static double getScreenHeight() {
        return primaryScreenBounds.getHeight();
    }


    /**
     * @param location the URL class of the resources file, use getClass.getResources().
     */
    static void playBackgroundMusic(URL location) {
        // play music
        try {
            audioplayer.stop();
        } catch ( NullPointerException e ) {
            System.out.println("No media is playing");
        }

        Media media = new Media(location.toString());

        audioplayer = new MediaPlayer(media);
        audioplayer.setAutoPlay(true);
        audioplayer.setVolume(0.5);
        audioplayer.setCycleCount(MediaPlayer.INDEFINITE);

    }

    /**
     * @param location the URL class of the resources file, use getClass.getResources().
     */
    static void playEffect(URL location) {

        new Thread(() -> {
            Media effect = new Media(location.toString());
            MediaPlayer effectPlayer = new MediaPlayer(effect);
            effectPlayer.setAutoPlay(true);
            // 1
            effectPlayer.setVolume(1);
            effectPlayer.setCycleCount(1);
        }).start();

    }


}
