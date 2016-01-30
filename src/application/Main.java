package application;

import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;

public class Main extends Application {
	private ObjectProperty<Color> bgc = new SimpleObjectProperty<>();
	private ObjectProperty<LocalTime> timeProp = new SimpleObjectProperty<>();
	// private TextField lblGreenVal = new TextField();
	// private TextField lblBlueVal = new TextField();
	// private TextField lblRedVal = new TextField();
	private TextField lblFontVal = new TextField();
	private StackPane sp = new StackPane();
	private Button btnStart = new Button("Start!");
	private Button btnStop = new Button("Stop/reset");
	private Button btnPause = new Button("Pause!");
	private TextField tfMinutes;
	private TextField tfHours;
	private TextField tfSeconds;
	private Label timeRemaining;
	private IntegerProperty timeLeft = new SimpleIntegerProperty();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	private TextField taAddH;
	private TextField taAddM;
	private TextField taAddS;
	private String timeFontType = "BuiltTitlingRg-Regular";
	private Stage primaryStage;
	private Scene scene;
	private VBox rightControlsBox;
	private BorderPane root;
	protected double initialX;
	protected double initialY;
	private MenuBar menuBar;
	private BackgroundFill bgfT = new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY);;
	private Background transparentBg = new Background(bgfT);
	private BackgroundFill bgfW = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);;
	private Background whiteBg = new Background(bgfW);
	private BackgroundFill bgfR = new BackgroundFill(Color.INDIANRED, CornerRadii.EMPTY, Insets.EMPTY);;
	private Background redBg = new Background(bgfT);
	private Background oldBg;
	private MenuItem itemOnTop;
	private MenuItem itemBgVis;
	private Timeline timeline;
	private boolean playing;
	private Button btnPlayPause = new Button("Play/Pause");
	private Color labelSetColor;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setOnCloseRequest(e -> {
			System.exit(0);
		});
		try {
			root = new BorderPane();
			Scene scene = new Scene(root, 900, 600);
			this.scene = scene;
			tfHours = new TextField("13");
			Label lblHours = new Label("Hours: ");

			tfHours.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
				if (newValue.equals("")) {
					tfHours.setText("0");
					return;
				}
				try {
					int i = Integer.parseInt(newValue);
					if (i > 23 || i < 0)
						throw new NumberFormatException();
				} catch (NumberFormatException e) {
					tfHours.setText(oldValue);
					return;
				}
			});
			HBox hbHours = new HBox(lblHours, tfHours);
			tfMinutes = new TextField("39");
			tfMinutes.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
				if (newValue.equals("")) {
					tfMinutes.setText("0");
					return;
				}
				try {
					int i = Integer.parseInt(newValue);
					if (i > 59 || i < 0)
						throw new NumberFormatException();
				} catch (NumberFormatException e) {
					tfMinutes.setText(oldValue);
					return;
				}
			});

			Label lblMinutes = new Label("Minutes: ");
			HBox hbMinutes = new HBox(lblMinutes, tfMinutes);
			tfSeconds = new TextField("49");
			tfSeconds.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
				if (newValue.equals("")) {
					tfSeconds.setText("0");
					return;
				}
				try {
					int i = Integer.parseInt(newValue);
					if (i > 59 || i < 0)
						throw new NumberFormatException();
				} catch (NumberFormatException e) {
					tfSeconds.setText(oldValue);
					return;
				}
			});
			Label lblSeconds = new Label("Seconds");
			HBox hbSeconds = new HBox(lblSeconds, tfSeconds);

			lblHours.setPrefWidth(80);
			lblMinutes.setPrefWidth(80);
			lblSeconds.setPrefWidth(80);

			tfHours.setPrefWidth(100);
			tfMinutes.setPrefWidth(100);
			tfSeconds.setPrefWidth(100);

			Button btnAddH = new Button("Add to hours, shortcut: F1");
			taAddH = new TextField("1");
			taAddH.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					if (!taAddH.isFocused())
						return;
					switch (event.getCode()) {
					case F1:
						addHours(taAddH.getText());
						break;
					case F2:
						addMinutes(taAddM.getText());
						break;
					case F3:
						addSeconds(taAddS.getText());
						break;
					default:
						break;
					}
				}
			});
			taAddH.setPrefWidth(50);
			taAddH.textProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (newValue.equals("")) {
						taAddH.setText("");
						return;
					} else {
						try {
							int i = Integer.parseInt(newValue);
							if (i < 0 || i > 59)
								throw new NumberFormatException();
						} catch (NumberFormatException e) {
							taAddH.setText(oldValue);
							return;
						}
					}
					taAddH.setText(Integer.parseInt(newValue) + "");
				}
			});
			btnAddH.setOnAction(e -> {
				addHours(taAddH.getText());
			});

			Button btnAddM = new Button("Add to minutes, shortcut: F2");
			taAddM = new TextField("15");
			taAddM.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					if (!taAddM.isFocused())
						return;
					switch (event.getCode()) {
					case F1:
						addHours(taAddH.getText());
						break;
					case F2:
						addMinutes(taAddM.getText());
						break;
					case F3:
						addSeconds(taAddS.getText());
						break;
					default:
						break;
					}
				}
			});
			taAddM.setPrefWidth(50);
			btnAddM.setOnAction(e -> {
				addMinutes(taAddM.getText());
			});

			Button btnAddS = new Button("Add to seconds, shortcut F3");
			taAddS = new TextField("15");
			taAddS.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					if (!taAddS.isFocused())
						return;
					switch (event.getCode()) {
					case F1:
						addHours(taAddH.getText());
						break;
					case F2:
						addMinutes(taAddM.getText());
						break;
					case F3:
						addSeconds(taAddS.getText());
						break;
					default:
						break;
					}
				}
			});
			taAddS.setPrefWidth(50);
			btnAddS.setOnAction(e -> {
				addSeconds(taAddS.getText());
			});

			Region reg = new Region();
			reg.setPrefWidth(10);
			Region reg2 = new Region();
			reg2.setPrefWidth(10);
			Region reg3 = new Region();
			reg3.setPrefWidth(10);

			HBox controls = new HBox(btnStart, btnStop, btnPlayPause);
			HBox hourControl = new HBox(taAddH, new Label("->"), btnAddH);
			HBox minuteControl = new HBox(taAddM, new Label("->"), btnAddM);
			HBox secondControl = new HBox(taAddS, new Label("->"), btnAddS);
			
			VBox controls2 = new VBox(hourControl, minuteControl, secondControl);

			controls.setSpacing(10);

			// HBox red = new HBox();
			// HBox blue = new HBox();
			// HBox green = new HBox();
			HBox font = new HBox();

			ToggleGroup group = new ToggleGroup();
			RadioButton button1 = new RadioButton("BuiltTitlingRg-Regular");
			button1.setToggleGroup(group);
			button1.setSelected(true);
			RadioButton button2 = new RadioButton("Bebas");
			button2.setToggleGroup(group);
			new HBox(button1, button2);

			group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
				@Override
				public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
					RadioButton chk = (RadioButton) t1.getToggleGroup().getSelectedToggle(); // Cast
																								// object
																								// to
																								// radio
																								// button
					fireFontTypeChange(chk.getText());

				}
			});

			ToggleGroup group2 = new ToggleGroup();
			RadioButton button3 = new RadioButton("Black");
			button3.setToggleGroup(group2);
			button3.setSelected(true);
			RadioButton button4 = new RadioButton("White");
			button4.setToggleGroup(group2);
			new HBox(button3, button4);

			group2.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
				@Override
				public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
					t1.getToggleGroup().getSelectedToggle();

				}
			});

			Label lblFont = new Label("Textsize:");

			lblFont.setPrefWidth(80);

			ColorPicker bgPicker = new ColorPicker();
			bgPicker.setOnAction(e -> {
				fireColorChange(bgPicker.getValue());
			});
			bgPicker.setValue(Color.rgb(0, 255, 0, 1));

			Label bgColorLabel = new Label("Background: ");
			bgColorLabel.setPrefWidth(80);
			HBox bgColorBox = new HBox(bgColorLabel, bgPicker);
			Slider fontSlider = new Slider();
			fontSlider.setPrefWidth(150);
			fontSlider.setMin(10);
			fontSlider.setMax(300);
			fontSlider.setValue(30);
			fontSlider.setShowTickLabels(true);
			fontSlider.setShowTickMarks(true);
			fontSlider.setMajorTickUnit(50);
			fontSlider.setMinorTickCount(10);
			fontSlider.setBlockIncrement(10);
			fontSlider.valueProperty().addListener((ChangeListener<Number>) (observable, oldValue,
					newValue) -> fontSlider.setValue(Math.round(newValue.doubleValue())));
			lblFontVal.textProperty().bindBidirectional(fontSlider.valueProperty(), new NumberStringConverter());
			lblFontVal.setPrefWidth(35);
			lblFontVal.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
				if (newValue.equals("")) {
					lblFontVal.setText("10");
					return;
				} else {
					try {
						int i = Integer.parseInt(newValue);
						if (i < 10 || i > 300)
							throw new NumberFormatException();
					} catch (NumberFormatException e) {
						lblFontVal.setText(oldValue);
						return;
					}
				}
				lblFontVal.setText(Integer.parseInt(newValue) + "");
				fireFontChange(lblFontVal.getText());
			});

			font.getChildren().addAll(lblFont, fontSlider, lblFontVal);

			btnPause.setOnAction(e -> {
				handlePause();
			});
			btnPause.setVisible(false);
			btnStart.setOnAction(e -> {
				handleStart();
			});
			btnStop.setOnAction(e -> {
				handleReset();
			});

			btnPlayPause.setOnAction(e-> {
				handlePausePlay();	
			});
			
			// Rectangle bg = new Rectangle();
			Color bgColor = Color.rgb(0, 255, 0, 1);

			bgc.set(bgColor);

			// bg.fillProperty().bind(bgc);
			//
			// bg.heightProperty().bind(sp.heightProperty());
			// bg.widthProperty().bind(sp.widthProperty());
			// sp.getChildren().add(bg);
			BackgroundFill bgf = new BackgroundFill(bgColor, CornerRadii.EMPTY, Insets.EMPTY);

			sp.setBackground(new Background(bgf));
			timeRemaining = new Label("00:00:00");
			addDraggableNode(sp);
			addDraggableNode(timeRemaining);
			makeTimeLabelMenu(timeRemaining);
			timeRemaining.setTextAlignment(TextAlignment.LEFT);

			timeRemaining.setFont(new Font(timeFontType, 30));
			sp.getChildren().add(timeRemaining);
			ComboBox<String> fontCb = new ComboBox<>();
			fontCb.setMaxWidth(150);

			List<java.awt.Font> monoFonts1 = getAllFonts();

			for (java.awt.Font f : monoFonts1) {
				fontCb.getItems().add(f.getFontName());
			}
			fontCb.getSelectionModel().select("BuiltTitlingRg-Regular");

			// ComboBox<Color> colorCb = new ComboBox<>();
			ColorPicker cPicker = new ColorPicker();
			cPicker.setOnAction(e -> {
				fireFontColorChange(cPicker.getValue());
			});

			Label fontSizeLabel = new Label("Font:");
			fontSizeLabel.setPrefWidth(80);
			HBox fontBox = new HBox(fontSizeLabel, fontCb);
			Label fontColorLabel = new Label("Font Color:");
			fontColorLabel.setPrefWidth(80);
			HBox fontColorBox = new HBox(fontColorLabel, cPicker);
			rightControlsBox = new VBox(hbHours, hbMinutes, hbSeconds, controls, controls2, bgColorBox,
					/* red, green, blue, */ font, /* fontColor, */fontColorBox, fontBox);
			fontCb.setOnAction(e -> {
				fireFontTypeChange(fontCb.getSelectionModel().getSelectedItem());
			});

			rightControlsBox.setSpacing(10);
			// timeBox.setMinWidth(200);

			final Menu menu = new Menu("Help");
			MenuItem menuitem = new MenuItem("About");
			MenuItem menuitem2 = new MenuItem("Controls");
			MenuItem menuclose = new MenuItem("Exit");

			menuclose.setOnAction(e -> {
				System.exit(0);
			});
			menuitem.setOnAction(e -> {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("About");
				alert.setHeaderText("I made this!");
				alert.setContentText(
						"Whole thing made by Makotro in java using JavaFX libraries.\n If it doesnt work, harass Makeke.");

				alert.showAndWait();
			});
			
			menuitem2.setOnAction(e -> {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Help");
				alert.setHeaderText(null);
				alert.getDialogPane().setPrefSize(630, 350);
				StringBuilder sb = new StringBuilder();
				sb.append("You can drag the window with mouse button from the colored box or the time itself.");
				sb.append("\n");
				sb.append("\n");
				sb.append("Insert the starting time in the top three text boxes.");
				sb.append("\n");
				sb.append("Start button starts/restarts the timer from the time in those boxes.");
				sb.append("\n");
				sb.append("Stop button resets the timer to 0 and Play/Pause(shortcut Space bar) button pauses it until pressed again.");
				sb.append("\n");
				sb.append("\n");
				sb.append("Next are three text boxes with buttons, by pressing the button you will add the");
				sb.append("\n");
				sb.append(" the amount of time in the text box to the hour/minutes/seconds depending on the button.");
				sb.append("\n");
				sb.append("\n");
				sb.append("Rest of the controls are simpler and control the background color for chromakey effect,");
				sb.append("\n");
				sb.append(" the font of the timer, its color and its size, you may also mousewheel to change the size");
				sb.append("\n");
				sb.append("\n");
				sb.append("Right click the time itself to show additional controls that let you hide everything but the timer");
				sb.append("\n");
				sb.append(", you can still drag it with the mouse button and use the shortcuts: F1, F2, F3 and Space bar");
				sb.append("\n");
				sb.append("The other option is to toggle always on top, click that to do as it says and click it again to disable it");
				sb.append("\n");
				alert.setContentText(sb.toString());

				alert.showAndWait();
			});
			menu.getItems().addAll(menuitem, menuitem2, menuclose);
			menuBar = new MenuBar();
			menuBar.getMenus().addAll(menu);

			timeRemaining.setTextFill(Color.BLACK);
			root.setCenter(sp);
			root.setRight(rightControlsBox);
			root.setTop(menuBar);
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					switch (event.getCode()) {
					case F1:
						addHours(taAddH.getText());
						break;
					case F2:
						addMinutes(taAddM.getText());
						break;
					case F3:
						addSeconds(taAddS.getText());
						break;
					case SPACE:
						handlePausePlay();
						break;
					default:
						break;
					}
				}
			});
			scene.setOnScroll(e -> {
				if (e.getDeltaY() > 0) {
					double d = timeRemaining.getFont().getSize();
					fireFontChange((int)(d+1) + "");
					lblFontVal.setText((int)(d+5) + "");
				} else {
					double d = timeRemaining.getFont().getSize();
					fireFontChange((int)(d-1) + "");
					lblFontVal.setText((int)(d-5) + "");
				}
			});
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<java.awt.Font> getAllFonts() {
		java.awt.Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
		List<java.awt.Font> monoFonts1 = new ArrayList<>();
		FontRenderContext frc = new FontRenderContext(null, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT,
				RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
		for (java.awt.Font f : fonts) {
			Rectangle2D Boundsi = f.getStringBounds("i", frc);
			Rectangle2D Boundsm = f.getStringBounds("m", frc);
			Rectangle2D Bounds0 = f.getStringBounds("0", frc);
			Rectangle2D Bounds1 = f.getStringBounds("1", frc);
			Rectangle2D Bounds2 = f.getStringBounds("2", frc);
			Rectangle2D Bounds3 = f.getStringBounds("3", frc);
			Rectangle2D Bounds4 = f.getStringBounds("4", frc);
			Rectangle2D Bounds5 = f.getStringBounds("5", frc);
			Rectangle2D Bounds6 = f.getStringBounds("6", frc);
			Rectangle2D Bounds7 = f.getStringBounds("7", frc);
			Rectangle2D Bounds8 = f.getStringBounds("8", frc);
			Rectangle2D Bounds9 = f.getStringBounds("9", frc);
			if (Bounds0.getWidth() == Bounds1.getWidth())
				if (Bounds1.getWidth() == Bounds2.getWidth())
					if (Bounds2.getWidth() == Bounds3.getWidth())
						if (Bounds3.getWidth() == Bounds4.getWidth())
							if (Bounds4.getWidth() == Bounds5.getWidth())
								if (Bounds5.getWidth() == Bounds6.getWidth())
									if (Bounds6.getWidth() == Bounds7.getWidth())
										if (Bounds7.getWidth() == Bounds8.getWidth())
											if (Bounds8.getWidth() == Bounds9.getWidth())
												if (Bounds9.getWidth() == Boundsi.getWidth())
													if (Boundsi.getWidth() == Boundsm.getWidth())
														monoFonts1.add(f);

		}
		return monoFonts1;
	}

	private void makeTimeLabelMenu(Label timeRemaining2) {

		final ContextMenu contextMenu = new ContextMenu();
		contextMenu.setOnShowing(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent e) {
				System.out.println("showing");
			}
		});
		contextMenu.setOnShown(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent e) {
				System.out.println("shown");
			}
		});

		itemBgVis = new MenuItem("Click to hide background");
		itemBgVis.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				toggleBackGroundVisibility();
			}

		});
		itemOnTop = new MenuItem("Click to enable Always On Top");
		itemOnTop.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				toggleAlwaysOnTop();
			}

		});
		contextMenu.getItems().addAll(itemBgVis, itemOnTop);
		timeRemaining2.setContextMenu(contextMenu);
	}

	protected void toggleAlwaysOnTop() {
		this.primaryStage.setAlwaysOnTop(!this.primaryStage.isAlwaysOnTop());

		if (this.primaryStage.isAlwaysOnTop()) {
			itemOnTop.setText("Click to disable Always On Top");
		} else {
			itemOnTop.setText("Click to enable Always On Top");
		}

	}

	protected void toggleBackGroundVisibility() {
		System.out.println("jou transparent");
		if (scene.getFill().equals(Color.TRANSPARENT)) {
			System.out.println("going white");
			scene.setFill(Color.WHITE);
			root.setBackground(whiteBg);
			sp.setBackground(oldBg);
			itemBgVis.setText("Click to hide background");
		} else {
			System.out.println("going transparent");
			scene.setFill(Color.TRANSPARENT);

			root.setBackground(transparentBg);
			oldBg = sp.getBackground();
			sp.setBackground(transparentBg);
			itemBgVis.setText("Click to show background");
		}
		menuBar.setVisible(!menuBar.isVisible());
		rightControlsBox.setVisible(!rightControlsBox.isVisible());
	}

	private void fireFontColorChange(Color color) {
		timeRemaining.setTextFill(color);
	}

	private void addDraggableNode(final Node node) {

		node.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				if (me.getButton() != MouseButton.MIDDLE) {
					initialX = me.getSceneX();
					initialY = me.getSceneY();
				}
			}
		});

		node.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if (me.getButton() != MouseButton.MIDDLE) {
					node.getScene().getWindow().setX(me.getScreenX() - initialX);
					node.getScene().getWindow().setY(me.getScreenY() - initialY);
				}
			}
		});
	}

	private void fireFontTypeChange(String string) {
		timeFontType = string;
		timeRemaining.setFont(new Font(timeFontType, timeRemaining.getFont().getSize()));
	}

	private void fireFontChange(String string) {
		int i = Integer.parseInt(string);
		timeRemaining.setFont(new Font(timeFontType, i));

	}

	private void addSeconds(String text) {
		int seconds = Integer.parseInt(text);
		if (timeProp.get() == null)
			return;
		timeProp.set(timeProp.get().plusSeconds(seconds));
		timeRemaining.setText(timeProp.get().format(formatter));
	}

	private void addMinutes(String text) {
		int minutes = Integer.parseInt(text);
		if (timeProp.get() == null)
			return;
		timeProp.set(timeProp.get().plusMinutes(minutes));
		timeRemaining.setText(timeProp.get().format(formatter));
	}

	private void addHours(String text) {
		int hours = Integer.parseInt(text);
		if (timeProp.get() == null)
			return;
		timeProp.set(timeProp.get().plusHours(hours));
		timeRemaining.setText(timeProp.get().format(formatter));
	}

	private void handlePausePlay() {
		if (timeline == null)
			return;
		
		if (playing) {
			timeline.pause();
			timeRemaining.setBackground(redBg);
			labelSetColor = (Color) timeRemaining.getTextFill();
			fireFontColorChange(Color.RED);
			playing = false;
		} else {
			timeline.play();
			fireFontColorChange(labelSetColor);
			playing = true;
		}
	}
	private void handleReset() {
		if (timeline != null) {
			timeline.stop();
			timeline = null;
			playing = false;
		}
		Integer.parseInt(tfHours.getText());
		Integer.parseInt(tfMinutes.getText());
		Integer.parseInt(tfSeconds.getText());
		timeRemaining.setText("00:00:00");
	}

	private void handleStart() {
		if (timeline != null) {
			timeline.stop();
			timeline = null;
			playing = false;
		}
		int hours = Integer.parseInt(tfHours.getText());
		int minutes = Integer.parseInt(tfMinutes.getText());
		int seconds = Integer.parseInt(tfSeconds.getText());
		int i = 0;
		i += hours * 60 * 60;
		i += minutes * 60;
		i += seconds;
		timeLeft.set(i);
		final LocalTime t = LocalTime.of(hours, minutes, seconds);
		timeProp.set(t);

		timeRemaining.setText(t.format(formatter));
		timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> {
			final LocalTime time = timeProp.get().minusSeconds(1);
			timeProp.set(time);
//			Platform.runLater(new Runnable() {
//				public void run() {
					timeRemaining.setText(time.format(formatter));
					if (time.format(formatter).equals("00:00:00"))
						timeline.stop();
//				}
//			});
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		playing = true;
	}

	private void handlePause() {
		Integer.parseInt(tfHours.getText());
		Integer.parseInt(tfMinutes.getText());
		Integer.parseInt(tfSeconds.getText());
	}

	private void fireColorChange(Color color) {
		BackgroundFill bgf = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);

		sp.setBackground(new Background(bgf));

	}

	public static void main(String[] args) {
		launch(args);
	}
}
