package ex1v4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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


public class FontViewer extends Application {
    private String[] typeFace = {"Serif", "Sans-Serif", "Monospaced"};
    private String[] typeStyle = {"Regular", "Italic", "Bold"};
    private Label label = new Label("Big Java");
    //    private ListView<String> fontsInstalled = new ListView<>(FXCollections.observableList(Font.getFamilies()));
    private ListView<String> fontsInstalled = new ListView<>(FXCollections.observableArrayList(typeFace[0], typeFace[1], typeFace[2]));
    private ListView<String> fontsStyle = new ListView<>(FXCollections.observableArrayList("regular", "italic", "bold"));

    private Font fontBoldItalic = Font.font(label.getFont().getFamily(),
            FontWeight.BOLD, FontPosture.ITALIC, 35);
    private Font fontBold = Font.font(label.getFont().getFamily(),
            FontWeight.BOLD, FontPosture.REGULAR, 35);
    private Font fontItalic = Font.font(label.getFont().getFamily(),
            FontWeight.NORMAL, FontPosture.ITALIC, 35);
    private Font fontNormal = Font.font(label.getFont().getFamily(),
            FontWeight.NORMAL, FontPosture.REGULAR, 35);

    private Slider fontSizeSlider = new Slider(10, 70, 35);
    private Spinner<Integer> spinner = new Spinner<>(10, 70, 35);

    private int fontSize;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        label.setStyle(getFontSize());
        label.setPadding(new Insets(15));
        VBox titleLabel = new VBox(label);
        titleLabel.setAlignment(Pos.CENTER_LEFT);

        VBox cover = new VBox(new ImageView(getClass().getResource("../BigJava.jpg").toExternalForm()));
        cover.setAlignment(Pos.CENTER_RIGHT);

        HBox box = new HBox(label, cover);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER);

        box.setAlignment(Pos.CENTER);
        VBox styleBox = new VBox(fontSizeViewer(), comboBox(), checkBox());
        styleBox.setAlignment(Pos.CENTER);
        // TODO: The menu doesn't work if this bit of code is activated!!!

        BorderPane bottomHandler = new BorderPane();
        bottomHandler.setCenter(styleBox);
        bottomHandler.setBottom(radioButtonSizeSetter());
        bottomHandler.setLeft(sliderBox());

        VBox root = new VBox(menu(), box, bottomHandler);


        Scene scene = new Scene(root);

        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            System.out.println("i'M IN THE SCENE");
            if (menu().isPressed())
                comboBox().isDisable();
        });

        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
                System.out.println("I'm back")
        );

        primaryStage.setTitle("Font Viewer");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    private MouseEvent handler(MouseEvent e) {
        return e;
    }

    /*  ================
     *      Slider
     *  ================
     */
    private VBox sliderBox() {
        fontSizeSlider.setOrientation(Orientation.VERTICAL);
        fontSizeSlider.setShowTickLabels(true);
        fontSizeSlider.setShowTickMarks(true);
        fontSizeSlider.setMajorTickUnit(5);
        fontSizeSlider.setMinorTickCount(3);
        fontSizeSlider.setSnapToTicks(true);
        fontSizeSlider.setPadding(new Insets(10));
        VBox sliderBox = new VBox(fontSizeSlider);
        sliderBox.setPadding(new Insets(0));
        sliderBox.setAlignment(Pos.CENTER_LEFT);
        sliderBox.setMaxWidth(Double.MAX_VALUE);

        return sliderBox;
    }

    /*  =====================================
     *    RadioButton SMALL, MEDIUM, LARGE
     *  =====================================
     */
    private HBox radioButtonSizeSetter() {
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

        type.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (type.getSelectedToggle() != null) {
                if (small.isArmed()) setFontSize(20);
                if (medium.isArmed()) setFontSize(40);
                if (large.isArmed()) setFontSize(60);

            }
        });

        HBox radioBtnBox = new HBox(small, medium, large);
        radioBtnBox.setAlignment(Pos.CENTER);
        return radioBtnBox;
    }

    /*  ================
     *     Combo Box
     *  ================
     */
    private ComboBox comboBox() {

// FIXME: Menu doesn't apply the functions if this code is running but the Slider works.
        ComboBox<String> familyChoice = new ComboBox<>(FXCollections.observableArrayList(typeFace[0], typeFace[1], typeFace[2]));

        // FIXME: Works only but the Menu doesn't
        if (familyChoice.isVisible()) {
            label.fontProperty().bind(Bindings.createObjectBinding(() ->
                            Font.font(familyChoice.getValue(),
                                    fontSizeSlider.getValue()),
                    fontSizeSlider.valueProperty(),
                    familyChoice.valueProperty()));
        } else {
            label.textProperty().unbindBidirectional(familyChoice);
        }

        return familyChoice;
    }



    /*  ================
     *     CheckBox
     *  ================
     */
    private HBox checkBox() {

        CheckBox italic = new CheckBox("italic");
        italic.setPadding(new Insets(20, 10, 0, 10));
        CheckBox bold = new CheckBox("bold");
        bold.setPadding(new Insets(20, 10, 0, 10));

        EventHandler<ActionEvent> handler = e -> {
            if (bold.isSelected() && italic.isSelected()) {
                label.setFont(fontBoldItalic); // Both check boxes checked
            } else if (bold.isSelected()) {
                label.setFont(fontBold); // The Bold check box checked
            } else if (italic.isSelected()) {
                label.setFont(fontItalic); // The Italic check box checked
            } else {
                label.setFont(fontNormal); // Both check boxes unchecked
            }
        };


        italic.setOnAction(handler);
        bold.setOnAction(handler);

        HBox checkBox = new HBox(italic, bold);
        checkBox.setAlignment(Pos.CENTER);
        return checkBox;
    }


    /*  ==========
     *     MENU
     *  ==========
     */
    private HBox menu() {

        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("_File");
        Menu face = new Menu("Face");
        Menu style = new Menu("Style");
        Menu exitOption = new Menu("_Quit");
        exitOption.setOnAction(e -> Platform.exit());

        MenuItem faceType1 = new MenuItem(typeFace[0]);
        faceType1.setOnAction(e -> label.setFont(Font.font(faceType1.getText())));

        MenuItem faceType2 = new MenuItem(typeFace[1]);

        MenuItem faceType3 = new MenuItem(typeFace[2]);

        face.getItems().addAll(faceType1, faceType2, faceType3);

        MenuItem regularStyle = new MenuItem(typeStyle[0]);
        MenuItem italicStyle = new MenuItem(typeStyle[1]);
        MenuItem boldStyle = new MenuItem(typeStyle[2]);
//        regularStyle.setOnAction(e -> label.setStyle("-fx-font-style: " + regularStyle.getText().toLowerCase()));
//        italicStyle.setOnAction(e -> label.setStyle("-fx-font-style: " + italicStyle.getText().toLowerCase()));
//        boldStyle.setOnAction(e -> label.setStyle("-fx-font-weight: " + boldStyle.getText().toLowerCase()));


        style.getItems().addAll(regularStyle, italicStyle, boldStyle);

        file.getItems().addAll(face, style, new SeparatorMenuItem(), exitOption);
        file.setAccelerator(KeyCombination.keyCombination("Shortcut+f"));


        menuBar.getMenus().add(file);
        HBox menuBox = new HBox(menuBar);
        menuBox.setMaxWidth(Double.MAX_VALUE);
        return menuBox;
    }


    /*  ===========================================
     *      It displays the size of the font used
     *  ===========================================
     */
    private HBox fontSizeViewer() {
        Label fontSizeLabel = new Label();
        fontSizeLabel.textProperty().bind(Bindings.concat("Font value: ",
                fontSizeSlider.valueProperty().asString("%3.0f")));

        HBox sizeBox = new HBox(fontSizeLabel);
        sizeBox.setAlignment(Pos.CENTER);
        sizeBox.setPadding(new Insets(10, 0, 10, 0));
        return sizeBox;
    }

    private Label createLabel(String family) {
        label = new Label("Boo");
        label.setFont(Font.font(family));
        label.setStyle(getFontSize());
        label.setPadding(new Insets(10));
        System.out.println(family);
        return label;
    }

    // TODO: The spinner doesn't return the value
    private String getFontSize() {
        if (!fontSizeSlider.isValueChanging()) fontSize = (int) fontSizeSlider.getValue();
        label.setFont(Font.font(fontSize));
        return "-fx-font-size: " + fontSize + ";";
    }

    private void setFontSize(int n) {
        fontSizeSlider.setValue(n);
    }
}
