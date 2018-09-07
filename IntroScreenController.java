import com.sun.xml.internal.bind.v2.model.core.NonElement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class IntroScreenController {

    @FXML
    private Button runButton;

    @FXML
    private CheckBox fullscreen;
    @FXML
    private TextField WIDTH,HEIGHT;

    @FXML
    private TextField Scale;

    @FXML
    private Label helpLabel;

    @FXML
    private Button openHelpButton;

    @FXML
    private Button closeHelpButton;

    @FXML
    private void initialize(){
        helpLabel.setText("KONTORLLER\nBytte mellom modi: SPACEBAR\nLukke avataren: ESCAPE\n\nFullskjerm-modus med annen\noppløsning enn native kan føre til\nmerkelig adferd.\n\nSkalering endrer på størrelsen\ntil avataren");
    }

    @FXML
    public  void handleRun(ActionEvent event){
        System.out.println("Run Handled");

        String args[] = {Boolean.toString(fullscreen.isSelected()),WIDTH.getText(),HEIGHT.getText(),Scale.getText()};

        ModernIntro.main(args);

    }

    @FXML
    public void handleOpenHelp(ActionEvent event){

    }
}
