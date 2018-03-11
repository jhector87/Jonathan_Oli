package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.css.FontFace;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.print.DocFlavor;
import javax.print.attribute.standard.DialogTypeSelection;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


public class Main extends Application {
	private Label label;
	private Slider fontSizeSlider = new Slider(10, 70, 35);
	private String[] faceType = {"Serif", "Sans-Serif", "Monospaced"};

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();

		label = new Label("Big Java");
//		label.setStyle("-fx-font-size: 25");
		label.setAlignment(Pos.CENTER);
		label.setPadding(new Insets(10));

		root.setTop(menuBar());
		root.setCenter(titleGrid());
		root.setBottom(options());

		ColumnConstraints cc = new ColumnConstraints();
		cc.setHgrow(Priority.ALWAYS);
		cc.setHalignment(HPos.CENTER);

		root.getCenter().autosize();

		primaryStage.setTitle("Font Viewer");
		primaryStage.setResizable(true);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
		primaryStage.getScene().setRoot(root);
		primaryStage.sizeToScene();
	}

	private void getScreenSize(BorderPane rootPane) {
		Scene scene = rootPane.getScene();

		if (scene == null) return;
		Window window = scene.getWindow();
		if (window == null) return;
		double rooPrefWidth = rootPane.prefWidth(-1);
		double decorationWidth = window.getWidth() - scene.getWidth();
		window.setWidth(rooPrefWidth + decorationWidth);
	}

	private MenuBar menuBar() {
		MenuBar menuBar = new MenuBar();
		Menu fontMenu = new Menu("_Font");
		fontMenu.setAccelerator(KeyCombination.keyCombination("Shortcut+F"));
		Menu face = new Menu("Face");
		Menu exit = new Menu("_Quit");
		exit.setOnAction(e -> Platform.exit());

		MenuItem faceType1 = new MenuItem(faceType[0]);
		faceType1.setOnAction(e -> label.setFont(Font.font("Verdana", FontWeight.BLACK, 18)));

		MenuItem faceType2 = new MenuItem(faceType[1]);
		faceType2.setOnAction(e -> label.setStyle("-fx-font-style: Sans-Serif"));
		MenuItem faceType3 = new MenuItem(faceType[2]);


		face.getItems().addAll(faceType1, faceType2, faceType3);

		fontMenu.getItems().addAll(face,new SeparatorMenuItem(), menuItem(), new SeparatorMenuItem(), exit);
		menuBar.getMenus().add(fontMenu);
		return menuBar;
	}

	private Menu menuItem() {
		Menu styleChoice = new Menu("Style");
		MenuItem regularMenu = new MenuItem("Regular");
		MenuItem boldMenu = new MenuItem("Bold");
		MenuItem italicMenu = new MenuItem("Italic");
		MenuItem extraLightMenu = new MenuItem("Extra-Light");

//		regularMenu.styleProperty().bindBidirectional(label.styleProperty());
		regularMenu.setOnAction(e -> label.setStyle("-fx-font-style: italic"));
//		regularMenu.setStyle("-fx-font-style: italic");

		boldMenu.setOnAction(e -> label.setText("Hello"));
		italicMenu.setOnAction(e -> label.setFont(Font.font(getFontFamily(), FontPosture.ITALIC, getFontSize())));
		extraLightMenu.setOnAction(e -> label.setFont(Font.font(getFontFamily(), FontWeight.EXTRA_LIGHT, getFontSize())));
		styleChoice.getItems().addAll(regularMenu, boldMenu, italicMenu, extraLightMenu);
		return styleChoice;
	}

	private GridPane titleGrid() {
		GridPane titleGrid = new GridPane();

		VBox imageHolder = new VBox(new ImageView(getClass().getResource("../BigJava.jpg").toExternalForm()));
		imageHolder.setAlignment(Pos.TOP_CENTER);
		imageHolder.setSpacing(10);
		titleGrid.add(label, 0, 1);
		titleGrid.add(imageHolder, 1, 1);

		return titleGrid;
	}

	private BorderPane options() {
		BorderPane pane = new BorderPane();
		VBox sliderBox = new VBox(fontSizeSlider);


		ComboBox<String> comboBox = new ComboBox<>();
		comboBox.getItems().addAll(faceType[0], faceType[1], faceType[2]);
		comboBox.setEditable(true);

		CheckBox italic = new CheckBox("Italic");
		CheckBox bold = new CheckBox("Bold");

		comboBox.setOnAction(e -> label.setStyle("-fx-font-family: " + comboBox.getValue()));
		RadioButton small = new RadioButton("small");
		RadioButton medium = new RadioButton("medium");
		RadioButton large = new RadioButton("large");

		ToggleGroup styleGroup = new ToggleGroup();
		small.setToggleGroup(styleGroup);
		medium.setToggleGroup(styleGroup);
		large.setToggleGroup(styleGroup);


		StringBinding styleBinding = Bindings.createStringBinding(this::getSampleText, fontSizeSlider.valueProperty());

		label.styleProperty().bind(styleBinding);

		HBox fontSizeViewer = new HBox(italic, bold);
		fontSizeViewer.setAlignment(Pos.CENTER);
		fontSizeViewer.setPadding(new Insets(15));
		HBox comboBoxViewer = new HBox(comboBox);
		comboBoxViewer.setAlignment(Pos.CENTER);
		comboBoxViewer.setPadding(new Insets(15));
		HBox styleViewer = new HBox(small, medium, large);
		styleViewer.setAlignment(Pos.CENTER);
		styleViewer.setPadding(new Insets(15));
		VBox fontContainer = new VBox(comboBoxViewer, fontSizeViewer);
		fontContainer.setPadding(new Insets(15));

		fontSizeSlider.setOrientation(Orientation.VERTICAL);
		fontSizeSlider.setShowTickLabels(true);
		fontSizeSlider.setShowTickMarks(true);
		fontSizeSlider.setMajorTickUnit(5);
		fontSizeSlider.setMinorTickCount(3);
		fontSizeSlider.setSnapToTicks(true);
		fontSizeSlider.setPadding(new Insets(10));

		Label fontSizeLabel = new Label();
		fontSizeLabel.textProperty().bind(Bindings.concat("Font value: ", fontSizeSlider.valueProperty().asString("%3.0f")));
		HBox sizeBox = new HBox(fontSizeLabel);
		sizeBox.setAlignment(Pos.CENTER);
		sizeBox.setPadding(new Insets(10, 0, 0, 0));

		fontContainer.setAlignment(Pos.CENTER);

		pane.setTop(sizeBox);
		pane.setLeft(sliderBox);
		pane.setCenter(fontContainer);
		pane.setBottom(styleViewer);
		return pane;
	}

	private String getSampleText() {
		int fontSize = (int) fontSizeSlider.getValue();

		return "-fx-font-size: " + fontSize;
	}

	private int getFontSize() {
		return (int) fontSizeSlider.getValue();
	}

	private String getFontFamily() {
		return "\"Helvetica\"";
	}

	private String getFontPosture() {
		return null;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
