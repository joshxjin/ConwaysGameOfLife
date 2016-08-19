package Frontend;

import java.util.ArrayList;

import Backend.Cell;
import Backend.GameOfLife;
import Backend.Patterns;
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
	private int scale = 8;
	private ArrayList<Rectangle> rectList = new ArrayList<Rectangle>();
	private Pane pane = new Pane();
	private ComboBox<String> patternsCB;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		tl = new Timeline();
		tl.setCycleCount(Timeline.INDEFINITE);
		nextGeneration();
		HBox hb = new HBox();
		setupTopPane(hb);
		pane.setMinWidth(3000);
		pane.setMinHeight(3000);
		ScrollPane sp = new ScrollPane();
		sp.setHvalue(0.5);
		sp.setVvalue(0.5);
		sp.setPrefViewportWidth(primaryStage.getWidth());
		sp.setPrefViewportHeight(primaryStage.getHeight() - topPaneHeight);
		sp.setContent(pane);
		
		sp.setStyle("-fx-font-size: 10px");
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		sp.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		
		sp.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouse) {
				int mouseX, mouseY;
				//if (mouseX < pane.getWidth())
				// only register clicks inside the scroll bars
				if (mouse.getX() < sp.getWidth() - 12 && mouse.getY() < sp.getHeight() - 12) {
					if (pane.getWidth() > sp.getWidth()) {
						mouseX = (int)(((pane.getWidth() - sp.getWidth()) * sp.getHvalue() + Math.round(mouse.getX())) / scale);
						if (sp.getHvalue() >= 0.5)
							mouseX++;
					} else
						mouseX = (int)((mouse.getX() - 1) / scale);
					
					if (pane.getHeight() > sp.getHeight()) {
						mouseY = (int)(((pane.getHeight() - sp.getHeight()) * sp.getVvalue() + Math.round(mouse.getY())) / scale);
						if (sp.getVvalue() >= 0.5)
							mouseY++;
					} else
						mouseY = (int)((mouse.getY() - 1) / scale);
					
//					System.out.println("Mouse: " + mouse.getX() + "," + mouse.getY());
//					System.out.println("MouseLocal: " + mouseX + "," + mouseY);
//					System.out.println("Scrollbar: " + sp.getHvalue() + "," + sp.getVvalue() );
//					System.out.println("ScrollPane: " + sp.getWidth() + "," + sp.getHeight());
//					System.out.println("Pane: " + pane.getWidth() + "," + pane.getHeight());
//					System.out.println();
					
					for (Cell newCell : Patterns.getPattern(patternsCB.getValue(), mouseX, mouseY)) {
					
						if (game.getAliveCells().contains(newCell)) {
							//game.removeCell(mouseX, mouseY);
							game.getAliveCells().remove(newCell);
							drawCells();
						} else {
							//game.addCell(mouseX, mouseY);
							game.getAliveCells().add(newCell);
							//Rectangle rect = new Rectangle(mouseX * scale, mouseY * scale, scale, scale);
							Rectangle rect = new Rectangle(newCell.getX() * scale, newCell.getY() * scale, scale, scale);
							rect.setFill(Color.BLACK);
							rectList.add(rect);
							pane.getChildren().add(rect);
						}
					}
				}
			}
			
		});
		
		setupCenterPane();
		BorderPane bp = new BorderPane();
		bp.setTop(hb);
		bp.setCenter(sp);
		
		Scene scene = new Scene(bp);
		
		primaryStage.setScene(scene);
		primaryStage.setMinWidth(1000);
		primaryStage.setMinHeight(800);
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
		patternsCB = new ComboBox<String>();
		patternsCB.getItems().addAll(Patterns.getList());
		patternsCB.setValue(".");
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
