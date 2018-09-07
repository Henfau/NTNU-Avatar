import javafx.scene.Scene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Settings");
        Parent root = FXMLLoader.load(getClass().getResource("IntroScreen.fxml"));
        Scene scene = new Scene(root,200,400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(){
        launch();
    }
}
