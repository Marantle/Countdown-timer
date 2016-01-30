package stopwatch;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Stopwatch extends Application
{
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
   @Override
   public void start(Stage stage)
   {
	   BorderPane bp = new BorderPane();
	   
	   Label lbl = new Label("00:00:00");
	   
	   
	   Scene scene = new Scene(bp);
	   
	   stage.setScene(scene);
			   
	   Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> {
		   String time = lbl.getText();
		   LocalTime parsedTime = LocalTime.parse(time, formatter).plusSeconds(1);
		   lbl.setText(parsedTime.format(formatter));
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		bp.setCenter(lbl);
		   stage.show();
   }
	public static void main(String[] args) {
		launch(args);
	}
}