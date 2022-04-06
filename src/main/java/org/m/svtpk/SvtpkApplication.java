package org.m.svtpk;

import javafx.application.Application;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.m.svtpk.entity.EpisodeEntity;
import org.m.svtpk.services.EpisodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@SpringBootApplication
public class SvtpkApplication extends Application {
    EpisodeEntity currentEpisode = new EpisodeEntity();
    EpisodeService episodeService = new EpisodeService();

    @Override
    public void start(Stage stage) {
        stage.setTitle("SVTpk");
        Label l = new Label("SVTpk");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text title = new Text("SVTpk");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(title, 0, 0, 2, 1);
        Label addressFieldLabel = new Label("Ange adress med id");
        grid.add(addressFieldLabel, 0, 2);
        TextField addressTextField = new TextField();
        Button dlBtn = new Button("Kopiera");
        dlBtn.setDisable(true);
        HBox hboxDlBtn = new HBox(10);
        hboxDlBtn.setAlignment(Pos.BOTTOM_CENTER);
        hboxDlBtn.getChildren().add(dlBtn);
        grid.add(hboxDlBtn, 0, 10);
        Text infoText = new Text();
        grid.add(infoText, 0, 8);

        addressTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
            //try getting resource
            currentEpisode = episodeService.getEpisodeInfo(addressTextField.getText());

            if (!currentEpisode.getSvtId().equals("")) {
                infoText.setVisible(true);
                infoText.setFill(Color.DARKGREEN);
                infoText.setText(currentEpisode.toString());
                dlBtn.setDisable(false);
            } else if (addressTextField.getText().length() > 0) {
                currentEpisode = new EpisodeEntity();
                infoText.setVisible(true);
                infoText.setFill(Color.FIREBRICK);
                infoText.setText("Tyvärr, hittar inte det avsnittet.");
                dlBtn.setDisable(true);
            } else {
                infoText.setVisible(false);
                dlBtn.setDisable(true);
            }
        });
        grid.add(addressTextField, 0, 3);
        dlBtn.setOnAction(e -> {
            //ladda ner
            episodeService.copyEpisodeToDisk(currentEpisode);
        });
        Scene scene = new Scene(grid, 640, 480);
        stage.setScene(scene);


        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

}
