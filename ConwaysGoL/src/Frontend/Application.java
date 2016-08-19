package Frontend;

import Backend.GameOfLife;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Application extends javafx.application.Application {
	private GameOfLife game = new GameOfLife();
	private int defaultSpeed = 250;
	private Duration frameDuration = Duration.millis(defaultSpeed);
	private Timeline tl = new Timeline();
	private KeyFrame frame;
	private int scale = 10;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		tl.stop();
		HBox hb = new HBox();
		setupTopPane(hb);
		ScrollPane sp = new ScrollPane();
		setupCenterPane(sp);
		BorderPane bp = new BorderPane();
		bp.setTop(hb);
		bp.setCenter(sp);
		
		Scene scene = new Scene(bp);
		
		primaryStage.setScene(scene);
		primaryStage.setMinWidth(900);
		primaryStage.setMinHeight(700);
		primaryStage.centerOnScreen();
		primaryStage.show();
	}
	
	public void setupTopPane(HBox hb) {
		// top pane will be a HBox of user controls, buttons, combobox etc.
		// buttons to include, play, pause, restart
		Button play = new Button("Play");
		play.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (tl.getStatus() == Status.STOPPED) {
					tl.play();
					nextGeneration();
				} else if (tl.getStatus() == Status.PAUSED) {
					tl.play();
				}
			}
			
		});
		
		Button pause = new Button("Pause");
		pause.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (tl.getStatus() == Status.RUNNING)
					tl.pause();
			}
			
		});
		
		Button restart = new Button("Restart");
		restart.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				tl.stop();
				game.restart();
			}
			
		});
		// horizontal slider for animation speed control
		Slider speed = new Slider(0.1, 4, 1);
		speed.setPadding(new Insets(5, 0 ,0 , 0));
		speed.valueProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				frameDuration = Duration.millis(250 / (double)newValue);
			}
			
		});
		// zoom in and out buttons
		Button zoomIn = new Button("+");
		zoomIn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				scale++;
			}
			
		});
		
		Button zoomOut = new Button("-");
		zoomOut.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				scale--;
			}
			
		});
		// combobox for pattern selection
		ComboBox<String> patternsCB = new ComboBox<String>();
		patternsCB.getItems().add(".");
		patternsCB.setPrefWidth(130);
		Separator vertSeparator1 = new Separator(Orientation.VERTICAL);
		vertSeparator1.setPadding(new Insets(0, 2, 0, 2));
		Separator vertSeparator2 = new Separator(Orientation.VERTICAL);
		vertSeparator2.setPadding(new Insets(0, 0, 0, 4));
		hb.setPrefHeight(play.getHeight());
		hb.getChildren().addAll(play, pause, restart, speed, vertSeparator1, zoomIn, zoomOut, vertSeparator2, patternsCB);
	}
	
	public void setupCenterPane(ScrollPane sp) {
		// center pane is where the visualisation of Conway's Game of Cell happens
		
	}
	
	public void nextGeneration() {
		frame = new KeyFrame(frameDuration, new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				game.nextGeneration();
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

}
