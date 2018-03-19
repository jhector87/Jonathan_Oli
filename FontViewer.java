package ex1v3;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import test.FontBind;

import java.beans.EventHandler;
import java.util.Collection;
import java.util.EventListener;
import java.util.concurrent.Callable;

@SuppressWarnings("InfiniteRecursion")
public class FontViewer extends Application {
//	private String[] typeFace = {"Serif", "Sans-Serif", "Monospaced"};
	private String[] typeStyle = {"Regular", "Italic", "Bold"};
	private ObservableList<String> fonts = FXCollections.observableList(Font.getFamilies());
	private Label title = new Label("Big Java");
	private ListView<String> fontsInstalled = new ListView<>(fonts);

	private Slider fontSizeSlider = new Slider(10, 70, 35);
	private Spinner<Integer> spinner = new Spinner<>(10, 70, 35);

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
//		BorderPane pane = new BorderPane();
		VBox titleLabel = new VBox(title);
		title.setFont(Font.font(getCurrentFont(), getFontSize()));
		title.setPadding(new Insets(15));
		titleLabel.setFillWidth(true);
		titleLabel.setAlignment(Pos.CENTER);

		VBox cover = new VBox(new ImageView(getClass().getResource("../BigJava.jpg").toExternalForm()));
		cover.setAlignment(Pos.CENTER_RIGHT);

		HBox box = new HBox(titleLabel, cover);
		box.setPadding(new Insets(10));

		/*  ================
		 *      Slider
		 *  ================
		 */
		VBox sliderBox = new VBox(getSlider());
		sliderBox.setPadding(new Insets(0));
		sliderBox.setAlignment(Pos.CENTER_LEFT);
		sliderBox.setMaxWidth(Double.MAX_VALUE);


		/*  =====================================
		 *    RadioButton SMALL, MEDIUM, LARGE
		 *  =====================================
		 */
		ToggleGroup type = new ToggleGroup();
		RadioButton small = new RadioButton("small");
		small.setToggleGroup(type);
		small.setPadding(new Insets(10));
		RadioButton medium = new RadioButton("medium");
		medium.setToggleGroup(type);
		medium.setPadding(new Insets(10));
		RadioButton large = new RadioButton("large");
		large.setToggleGroup(type);
		large.setPadding(new Insets(10));
		HBox boxSize = new HBox(small, medium, large);


		type.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
			if (type.getSelectedToggle() != null) {
				if (small.isArmed()) setFontSize(20);
				if (medium.isArmed()) setFontSize(40);
				if (large.isArmed()) setFontSize(60);

				// TODO: MAKE IT DESELECT AUTOMATICALLY
				if (fontSizeSlider.isValueChanging()) type.getSelectedToggle().setSelected(false);
			}
	});

		/*  ================
		 *     Combo Box
		 *  ================
		 */
//		title.fontProperty().bind(Bindings.createObjectBinding(() ->
//						Font.font(familyChoice.getValue(),
//								  fontSizeSlider.getValue()),
//					fontSizeSlider.valueProperty(),
//								  familyChoice.valueProperty()));
		title.fontProperty().bind(Bindings.createObjectBinding(() ->
		Font.font(getCurrentFont(), getFontSize())));

		ComboBox<String> familyChoice = new ComboBox<>(fonts);

		VBox styleBox = new VBox(fontSizeViewer(), familyChoice, checkBox());
		styleBox.setAlignment(Pos.CENTER);
//		Scene scene = new Scene(pane);
		// TODO: The menu doesn't work if this bit of code is activated!!!

		GridPane stylePane = new GridPane();
		stylePane.setAlignment(Pos.CENTER);
		stylePane.add(boxSize, 1, 2);
		stylePane.add(styleBox, 1, 1);
		stylePane.add(sliderBox, 0, 1);

		VBox root = new VBox(menuBar(),box,stylePane);
		Scene scene = new Scene(root);

/*
		pane.setTop(menuBar());
		pane.setCenter(box);
		pane.setBottom(stylePane);*/

		/*primaryStage.heightProperty().bind(scene.heightProperty());
		primaryStage.widthProperty().bind(scene.widthProperty());*/
		primaryStage.setTitle("Font Viewer");
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
	}

	/*  ===========================================
	 *      It displays the size of the font used
	 *  ===========================================
	 */
	private HBox fontSizeViewer() {
		Label fontSizeLabel = new Label();
		fontSizeLabel.textProperty().bind(Bindings.concat("Font value: ", fontSizeSlider.valueProperty().asString("%3.0f")));

		HBox sizeBox = new HBox(fontSizeLabel);
		sizeBox.setAlignment(Pos.CENTER);
		sizeBox.setPadding(new Insets(10, 0, 10, 0));
		return sizeBox;
	}

	private HBox toggleGroup() throws Exception {
		ToggleGroup type = new ToggleGroup();
		RadioButton small = new RadioButton("small");
		small.setToggleGroup(type);
		small.setPadding(new Insets(10));
		RadioButton medium = new RadioButton("medium");
		medium.setToggleGroup(type);
		medium.setPadding(new Insets(10));
		RadioButton large = new RadioButton("large");
		large.setToggleGroup(type);
		large.setPadding(new Insets(10));
		HBox box = new HBox(small, medium, large);
//		box.setPadding(new Insets(10));
//
//		try {
//			type.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
//				title.styleProperty().unbind();
//				if (type.getSelectedToggle() != null) {
//					title.setStyle("-fx-font-weight: " + type.getSelectedToggle().getUserData().toString() + ";");
//					System.out.println(type.getSelectedToggle().getUserData().toString());
//				}
//			});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		toggleGroup().setAlignment(Pos.CENTER);

		return box;
	}

	private HBox checkBox() {
		CheckBox italic = new CheckBox("italic");
		italic.setPadding(new Insets(20, 10, 0, 10));
		CheckBox bold = new CheckBox("bold");
		bold.setPadding(new Insets(20, 10, 0, 10));

		if (italic.isSelected())
			title.setFont(Font.font(getCurrentFont(), FontPosture.ITALIC, fontSizeSlider.getValue()));
//		if (bold.isSelected())
//			title.setFont(Font.font(String.valueOf(title.getFont()), FontWeight.BOLD, fontSizeSlider.getValue()));
		if (bold.isSelected()) title.setStyle("-fx-font-style: bold");

		HBox box = new HBox(italic, bold);
		box.setAlignment(Pos.CENTER);
		return box;
	}

	/*  ==========
	 *     MENU
	 *  ==========
	 */
	private HBox menuBar() {
		MenuBar menuBar = new MenuBar();
		Menu file = new Menu("_File");
		Menu face = new Menu("Face");
		Menu style = new Menu("Style");
		Menu exitOption = new Menu("_Quit");
		exitOption.setOnAction(e -> Platform.exit());

		// Displays the fonts in the menu
//		for(String font : Font.getFamilies()) {
//			face.getItems().addAll(new MenuItem(font));
//		}
//
//
		for (int i = 0; i < fontsInstalled.getItems().size(); i++) {
			face.getItems().addAll(new MenuItem(fontsInstalled.getItems().get(i)));
		}

//		face.setOnAction(event -> title.setFont(Font.font(fontsInstalled.getSelectionModel().getSelectedItem())));

		fontsInstalled.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			// change the label text value to the newly selected item.
			title.setText("You Selected " + newValue);
			title.setFont(Font.font(newValue, getFontSize()));
		});

//		MenuItem faceType1 = new MenuItem(typeFace[0]);
//		faceType1.setOnAction(e -> title.setFont(Font.font("Serif")));
//
//		MenuItem faceType2 = new MenuItem(typeFace[1]);
//		faceType2.setOnAction(e -> title.setFont(Font.font("SanSerif")));
//
//		MenuItem faceType3 = new MenuItem(typeFace[2]);
//		faceType3.setOnAction(e -> title.setFont(Font.font("Monospaced")));

//		fontsInstalled.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//			title.fontProperty().unbind();
//			title.setFont(Font.font(newValue));
//		});

//		face.getItems().addAll(faceType1, faceType2, faceType3);

		MenuItem regularStyle = new MenuItem(typeStyle[0]);
		regularStyle.setOnAction(e -> title.setFont(Font.font("Verdana", FontPosture.ITALIC, getFontSize())));
		MenuItem italicStyle = new MenuItem(typeStyle[1]);
//		italicStyle.setOnAction(e -> title.setFont(Font.font(typeFace[0], FontPosture.ITALIC, getFontSize())));
		MenuItem boldStyle = new MenuItem(typeStyle[2]);
//		boldStyle.setOnAction(e -> title.setFont(Font.font(typeFace[0], FontWeight.BOLD, getFontSize())));

		style.getItems().addAll(regularStyle, italicStyle, boldStyle);

		file.getItems().addAll(face, style, new SeparatorMenuItem(), exitOption);
		file.setAccelerator(KeyCombination.keyCombination("Shortcut+f"));
		menuBar.getMenus().add(file);
		menuBar.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		HBox fonts = new HBox(fontsInstalled);
		fonts.setMaxHeight(30);
		return new HBox(menuBar, spinner, fonts);
	}

	/*  ===========================================
	 *      Sets up the slider
	 *  ===========================================
	 */
	private Slider getSlider() {
		fontSizeSlider.setOrientation(Orientation.VERTICAL);
		fontSizeSlider.setShowTickLabels(true);
		fontSizeSlider.setShowTickMarks(true);
		fontSizeSlider.setMajorTickUnit(5);
		fontSizeSlider.setMinorTickCount(3);
		fontSizeSlider.setSnapToTicks(true);
		fontSizeSlider.setPadding(new Insets(10));
		return fontSizeSlider;

	}

	// TODO: The spinner doesn't return the value
	private int getFontSize() {
		spinner.setEditable(true); // Makes it possible to just change the value via an input
		IntegerProperty spinnerValue = new SimpleIntegerProperty(spinner.getValue());
		IntegerProperty sliderValue = new SimpleIntegerProperty((int) fontSizeSlider.getValue());

		sliderValue.addListener((observable, oldValue, newValue) -> sliderValue.bindBidirectional(spinnerValue));
		fontSizeSlider.valueProperty().set(spinner.getValue());
		return (int) fontSizeSlider.getValue();
	}

	private void setFontSize(int n) {
		fontSizeSlider.setValue(n);
	}

	private String getCurrentFont(){
		fontsInstalled.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			// change the label text value to the newly selected item.
			title.setFont(Font.font(newValue));
		});
		return fontsInstalled.getSelectionModel().getSelectedItem();
	}


}
