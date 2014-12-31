package be.beeles_place.jambiLight.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

public class LogViewController implements Initializable {

    //Local variables.
    private Path logFile;

    //Components.
    @FXML
    private TextArea TA_Log;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Empty.
    }

    public void loadLog(Path logFile) {
        this.logFile = logFile;
        try {
            List<String> lines = Files.readAllLines(logFile);

            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                sb.append(line);
                sb.append("\n");
            }
            TA_Log.setText(sb.toString());

        } catch (IOException e) {
            TA_Log.setText("Cannot read log: " + e.getMessage());
        }
    }

    //Event handlers.
    @FXML
    void onReloadClicked(ActionEvent event) {
        loadLog(logFile);
    }
}