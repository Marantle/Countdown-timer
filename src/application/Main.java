package application;

import java.awt.GraphicsEnvironment;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
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
import javafx.util.converter.NumberStringConverter;

public class Main extends Application {
	private ObjectProperty<Color> bgc = new SimpleObjectProperty<>();
	private ObjectProperty<LocalTime> timeProp = new SimpleObjectProperty<>();
//	private TextField lblGreenVal = new TextField();
//	private TextField lblBlueVal = new TextField();
//	private TextField lblRedVal = new TextField();
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
	Timer timer = new Timer();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	private TextField taAddH;
	private TextField taAddM;
	private TextField taAddS;
	private String timeFontType = "BuiltTitlingRg-Regular";
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setOnCloseRequest(e -> {
			System.exit(0);
		});
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 900, 600);
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

			Button btnAddH = new Button("+H|F1");
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
			taAddH.setPrefWidth(35);
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

			Button btnAddM = new Button("+M|F2");
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
			taAddM.setPrefWidth(35);
			btnAddM.setOnAction(e -> {
				addMinutes(taAddM.getText());
			});

			Button btnAddS = new Button("+S|F3");
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
			taAddS.setPrefWidth(35);
			btnAddS.setOnAction(e -> {
				addSeconds(taAddS.getText());
			});

			Region reg = new Region();
			reg.setPrefWidth(10);
			Region reg2 = new Region();
			reg2.setPrefWidth(10);
			Region reg3 = new Region();
			reg3.setPrefWidth(10);

			HBox controls = new HBox(btnStart, btnStop, btnPause);
			HBox controls2 = new HBox(taAddH, btnAddH, reg, taAddM, btnAddM, reg2, taAddS, btnAddS);

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

			// Label lblRed = new Label("Red");
			// Label lblBlue = new Label("Blue");
			// Label lblGreen = new Label("Green");
			Label lblFont = new Label("Textsize:");
			// lblRed.setPrefWidth(50);
			// lblBlue.setPrefWidth(50);
			// lblGreen.setPrefWidth(50);
			lblFont.setPrefWidth(80);

			// Slider redSlider = new Slider();
			// redSlider.setPrefWidth(150);
			// redSlider.setMin(0);
			// redSlider.setMax(255);
			// redSlider.setValue(0);
			// redSlider.setShowTickLabels(true);
			// redSlider.setShowTickMarks(true);
			// redSlider.setMajorTickUnit(51);
			// redSlider.setMinorTickCount(15);
			// redSlider.setBlockIncrement(10);
			// redSlider.valueProperty().addListener((ChangeListener<Number>)
			// (observable, oldValue, newValue) -> redSlider
			// .setValue(Math.round(newValue.doubleValue())));
			// lblRedVal.textProperty().bindBidirectional(redSlider.valueProperty(),
			// new NumberStringConverter());
			// lblRedVal.setPrefWidth(35);
			// lblRedVal.textProperty().addListener((ChangeListener<String>)
			// (observable, oldValue, newValue) -> {
			// System.out.println(newValue);
			// if (newValue.equals("")) {
			// lblRedVal.setText("0");
			// return;
			// } else {
			// try {
			// int i = Integer.parseInt(newValue);
			// if (i < 0 || i > 255)
			// throw new NumberFormatException();
			// } catch (NumberFormatException e) {
			// lblRedVal.setText(oldValue);
			// return;
			// }
			// }
			// lblRedVal.setText(Integer.parseInt(newValue) + "");
			// fireColorChange();
			// });
			// red.getChildren().addAll(lblRed, redSlider, lblRedVal);
			//
			// Slider blueSlider = new Slider();
			// blueSlider.setPrefWidth(150);
			// blueSlider.setMin(0);
			// blueSlider.setMax(255);
			// blueSlider.setValue(0);
			// blueSlider.setShowTickLabels(true);
			// blueSlider.setShowTickMarks(true);
			// blueSlider.setMajorTickUnit(51);
			// blueSlider.setMinorTickCount(15);
			// blueSlider.setBlockIncrement(10);
			// blueSlider.valueProperty().addListener((ChangeListener<Number>)
			// (observable, oldValue,
			// newValue) ->
			// blueSlider.setValue(Math.round(newValue.doubleValue())));
			// lblBlueVal.textProperty().bindBidirectional(blueSlider.valueProperty(),
			// new NumberStringConverter());
			// lblBlueVal.setPrefWidth(35);
			// lblBlueVal.textProperty().addListener((ChangeListener<String>)
			// (observable, oldValue, newValue) -> {
			// System.out.println(newValue);
			// if (newValue.equals("")) {
			// lblBlueVal.setText("0");
			// return;
			// } else {
			// try {
			// int i = Integer.parseInt(newValue);
			// if (i < 0 || i > 255)
			// throw new NumberFormatException();
			// } catch (NumberFormatException e) {
			// lblBlueVal.setText(oldValue);
			// return;
			// }
			// }
			// lblBlueVal.setText(Integer.parseInt(newValue) + "");
			// fireColorChange();
			// });
			// blue.getChildren().addAll(lblBlue, blueSlider, lblBlueVal);
			//
			//// Slider greenSlider = new Slider();
			// greenSlider.setPrefWidth(150);
			// greenSlider.setMin(0);
			// greenSlider.setMax(255);
			// greenSlider.setValue(255);
			// greenSlider.setShowTickLabels(true);
			// greenSlider.setShowTickMarks(true);
			// greenSlider.setMajorTickUnit(51);
			// greenSlider.setMinorTickCount(15);
			// greenSlider.setBlockIncrement(10);
			// greenSlider.valueProperty().addListener((ChangeListener<Number>)
			// (observable, oldValue,
			// newValue) ->
			// greenSlider.setValue(Math.round(newValue.doubleValue())));
			// lblGreenVal.textProperty().bindBidirectional(greenSlider.valueProperty(),
			// new NumberStringConverter());
			// lblGreenVal.setPrefWidth(35);
			// lblGreenVal.textProperty().addListener((ChangeListener<String>)
			// (observable, oldValue, newValue) -> {
			// System.out.println(newValue);
			// if (newValue.equals("")) {
			// lblGreenVal.setText("0");
			// return;
			// } else {
			// try {
			// int i = Integer.parseInt(newValue);
			// if (i < 0 || i > 255)
			// throw new NumberFormatException();
			// } catch (NumberFormatException e) {
			// lblGreenVal.setText(oldValue);
			// return;
			// }
			// }
			// lblGreenVal.setText(Integer.parseInt(newValue) + "");
			// fireColorChange();
			// });
			// green.getChildren().addAll(lblGreen, greenSlider, lblGreenVal);

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
				System.out.println(newValue);
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
			timeRemaining.setTextAlignment(TextAlignment.LEFT);

			timeRemaining.setFont(new Font(timeFontType, 30));
			sp.getChildren().add(timeRemaining);
			ComboBox<String> fontCb = new ComboBox<>();
			fontCb.setMaxWidth(150);
			
			java.awt.Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();

			for (java.awt.Font f : fonts) {
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
			VBox timeBox = new VBox(hbHours, hbMinutes, hbSeconds, controls, controls2,
					bgColorBox, /* red, green, blue,*/ font, /* fontColor, */fontColorBox, fontBox);
			fontCb.setOnAction(e -> {
				fireFontTypeChange(fontCb.getSelectionModel().getSelectedItem());
			});

			timeBox.setSpacing(10);
			// timeBox.setMinWidth(200);

			final Menu menu = new Menu("Help");
			MenuItem menuitem = new MenuItem("About");

			menuitem.setOnAction(e -> {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("About");
				alert.setHeaderText("I made this!");
				alert.setContentText(
						"Whole thing made by Makotro in java using JavaFX libraries.\n If it doesnt work, harass Makeke.");

				alert.showAndWait();
			});
			menu.getItems().add(menuitem);
			MenuBar menuBar = new MenuBar();
			menuBar.getMenus().addAll(menu);

			timeRemaining.setTextFill(Color.WHITE);
			root.setCenter(sp);
			root.setRight(timeBox);
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
					default:
						break;
					}
				}
			});

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fireFontColorChange(Color color) {
		timeRemaining.setTextFill(color);
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

	private void handleReset() {
		if (timer != null)
			timer.cancel();
		Integer.parseInt(tfHours.getText());
		Integer.parseInt(tfMinutes.getText());
		Integer.parseInt(tfSeconds.getText());
		timeRemaining.setText("00:00:00");
	}

	private void handleStart() {
		if (timer != null)
			timer.cancel();
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

		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			public void run() {
				final LocalTime time = timeProp.get().minusSeconds(1);
				timeProp.set(time);
				System.out.println(time);
				Platform.runLater(new Runnable() {
					public void run() {
						timeRemaining.setText(time.format(formatter));
						if (time.format(formatter).equals("00:00:00"))
							timer.cancel();
					}
				});
				// oldT = newT.minusSeconds(0);
			}
		}, 0, 1000);
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
