package Frontend;

import java.util.ArrayList;

import Backend.Cell;
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
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Application extends javafx.application.Application {
	private int topPaneHeight = 40;
	private GameOfLife game = new GameOfLife();
	private int defaultSpeed = 250;
	private Duration frameDuration = Duration.millis(defaultSpeed);
	private Timeline tl;
	private KeyFrame frame;
	private int scale = 10;
	private ArrayList<Rectangle> rectList = new ArrayList<Rectangle>();
	private Pane pane = new Pane();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		tl = new Timeline();
		tl.setCycleCount(Timeline.INDEFINITE);
		nextGeneration();
		HBox hb = new HBox();
		setupTopPane(hb);
		ScrollPane sp = new ScrollPane();
		sp.setPrefViewportWidth(primaryStage.getWidth());
		sp.setPrefViewportHeight(primaryStage.getHeight() - topPaneHeight);
		sp.setContent(pane);
		
		sp.setStyle("");
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		sp.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		
		sp.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouse) {
				int mouseX, mouseY;
				mouseX = (int)(mouse.getX() / scale);
				if (mouse.getX() < 0)
					mouseX--;
				mouseY = (int)(mouse.getY() / scale);
				if (mouse.getY() < 0)
					mouseY--;
				System.out.println(mouseX+","+mouseY);
				
				if (game.getAliveCells().contains(new Cell(mouseX, mouseY))) {
					game.removeCell(mouseX, mouseY);
					drawCells();
				} else {
					game.addCell(mouseX, mouseY);
					Rectangle rect = new Rectangle(mouseX * scale, mouseY * scale, scale, scale);
					rect.setFill(Color.BLACK);
					rectList.add(rect);
					pane.getChildren().add(rect);
				}
				
			}
			
		});
		
		setupCenterPane();
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
				tl.play();
			}
			
		});
		
		Button pause = new Button("Pause");
		pause.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				tl.pause();
			}
			
		});
		
		Button restart = new Button("Restart");
		restart.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				tl.stop();
				game.restart();
				pane.getChildren().removeAll(rectList);
				//pane.getChildren().clear();
			}
			
		});
		
		Button nextStep = new Button("Step");
		nextStep.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (tl.getStatus() != Status.RUNNING) {
					tl.stop(); 
					tl.setCycleCount(1);
					tl.play();
					tl.setCycleCount(Timeline.INDEFINITE);
				}
			}
			
		});
		// horizontal slider for animation speed control
		Slider speed = new Slider(0.1, 4, 1);
		speed.setPadding(new Insets(5, 0 ,0 , 0));
		speed.valueProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				frameDuration = Duration.millis(250 / (double)newValue);
				boolean playing = tl.getStatus() == Status.RUNNING;
				nextGeneration();
				if (playing)
					tl.play();
			}
			
		});
		// zoom in and out buttons
		Button zoomIn = new Button("+");
		zoomIn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (scale <= 20) {
					scale++;
					drawCells();
				}
			}
			
		});
		
		Button zoomOut = new Button("-");
		zoomOut.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (scale >= 1) {
					scale--;
					drawCells();
				}
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
		hb.getChildren().addAll(play, pause, nextStep, restart, speed, vertSeparator1, zoomIn, zoomOut, vertSeparator2, patternsCB);
	}
	
	public void setupCenterPane() {
		// center pane is where the visualisation of Conway's Game of Cell happens
		game.addCell(30, 20);
		game.addCell(30, 21);
		game.addCell(30, 22);
		
		for (Cell cell : game.getAliveCells()) {
			Rectangle rect = new Rectangle(cell.getX()*scale, cell.getY()*scale, scale, scale);
			rect.setFill(Color.BLACK);
			rectList.add(rect);
			pane.getChildren().add(rect);
		}
		
	}
	
	public void nextGeneration() {
		frame = new KeyFrame(frameDuration, new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				game.nextGeneration();
				drawCells();
			}
		});
		tl.stop();
		tl.getKeyFrames().setAll(frame);
	}
	
	public void drawCells() {
		pane.getChildren().removeAll(rectList);
		rectList.clear();
		for (Cell cell : game.getAliveCells()) {
			Rectangle rect = new Rectangle(cell.getX()*scale, cell.getY()*scale, scale, scale);
			rect.setFill(Color.BLACK);
			rectList.add(rect);
			pane.getChildren().add(rect);
		}
		
	}

	public static void main(String[] args) {
		launch(args);
	}

}
