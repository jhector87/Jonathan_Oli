package ex1;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class FontViewer extends Application {

    private Label label;
    private Slider slider;
    private String[] typeFace = {"Serif", "SansSerif", "Monospaced"};
    private String[] typeStyle = {"Regular", "Italic", "Bold"};
    private Font font;
    private ObservableList<String> fonts = FXCollections.observableArrayList(typeFace[0], typeFace[1], typeFace[2]);
    private ComboBox<String> familyChoice;

    private Insets insets = new Insets(10);

    {
        slider = new Slider(10, 70, 35);
        label = new Label("Big Java");
        label.setFont(Font.font(slider.getValue()));
        label.setPadding(new Insets(10));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        AnchorPane anchorPane = new AnchorPane();
        ImageView image = new ImageView(getClass().getResource("../BigJava.jpg").toExternalForm());
        AnchorPane.setTopAnchor(image, 10.0);
        AnchorPane.setRightAnchor(image, 10.0);
        anchorPane.getChildren().add(image);

        HBox cover = new HBox(label, anchorPane);
        cover.setAlignment(Pos.CENTER);
        cover.setPadding(insets);

        VBox centerBottomPane = new VBox(fontSizeViewer(), comboBox(), checkBox());
        centerBottomPane.setAlignment(Pos.CENTER);
        centerBottomPane.setPadding(insets);

        BorderPane bottomPane = new BorderPane();
        bottomPane.setLeft(sliderBox());
        bottomPane.setCenter(centerBottomPane);
        bottomPane.setBottom(radioButtonSizeSetter());

        BorderPane mainPane = new BorderPane();
        mainPane.setTop(menu());
        mainPane.setCenter(cover);
        mainPane.setBottom(bottomPane);

        Scene scene = new Scene(mainPane);

        // Updates the ComoBox but does lazy Binding
        // Click in the Scene the update in the ComboBox after clicking in the Menu
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if (!label.getFont().getFamily().equalsIgnoreCase(familyChoice.getSelectionModel().getSelectedItem())) {
                familyChoice.getSelectionModel().select(label.getFont().getFamily());
            }

            if (label.fontProperty().equals("italic")) System.out.println(label.fontProperty().toString());
            System.out.println(label.getFont());
        });

        scene.addEventFilter(Event.ANY, event ->
                primaryStage.sizeToScene()
        );

        primaryStage.setTitle("Font Viewer");
        primaryStage.setScene(scene);
        primaryStage.hide();
        primaryStage.show();
    }

    /*  ================
     *      Slider
     *  ================
     */
    private VBox sliderBox() {
        slider.setOrientation(Orientation.VERTICAL);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(5);
        slider.setMinorTickCount(3);
        slider.setSnapToTicks(true);
        slider.setPadding(new Insets(10));
        VBox sliderBox = new VBox(slider);
        sliderBox.setPadding(new Insets(0));
        sliderBox.setAlignment(Pos.CENTER_LEFT);
        sliderBox.setMaxWidth(Double.MAX_VALUE);

        return sliderBox;
    }

    /*  ================
     *      Menu
     *  ================
     */
    private MenuBar menu() {
        MenuBar menuBar = new MenuBar();
        final Menu FILEMENU = new Menu("_File");
        final Menu FACEMENU = new Menu("Face");
        MenuItem serifMenu = new MenuItem(typeFace[0]);
        MenuItem sansSerifMenu = new MenuItem(typeFace[1]);
        MenuItem monospacedMenu = new MenuItem(typeFace[2]);
        FACEMENU.getItems().addAll(serifMenu, sansSerifMenu, monospacedMenu);

        FACEMENU.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                label.fontProperty().unbind(); // By unbinding it, I'm able to change the whole font
                serifMenu.setOnAction(e -> label.setFont(Font.font(typeFace[0], getFontSize())));
                sansSerifMenu.setOnAction(e -> label.setFont(Font.font(typeFace[1], getFontSize())));
                monospacedMenu.setOnAction(e -> label.setFont(Font.font(typeFace[2], getFontSize())));
            }
        });


        final Menu STYLEMENU = new Menu("_Style");
        CheckMenuItem regularMenu = new CheckMenuItem(typeStyle[0]);
        regularMenu.setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN));
        CheckMenuItem italicMenu = new CheckMenuItem(typeStyle[1]);
        italicMenu.setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN));
        CheckMenuItem boldMenu = new CheckMenuItem(typeStyle[2]);
        boldMenu.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN));
        STYLEMENU.getItems().addAll(regularMenu, italicMenu, boldMenu);

        STYLEMENU.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                label.fontProperty().unbind();

                regularMenu.setOnAction(e -> label.setFont(styleSetter("regular")));
                italicMenu.setOnAction(e -> label.setFont(styleSetter("italic")));
                boldMenu.setOnAction(e -> label.setFont(styleSetter("bold")));
            }
        });

        FILEMENU.getItems().addAll(FACEMENU, STYLEMENU);
        menuBar.getMenus().addAll(FILEMENU);

        return menuBar;
    }

    private Font styleSetter(String fontStyle) {

        switch (fontStyle.toLowerCase()) {
            case "regular":
                return Font.font(label.getFont().getFamily(), FontPosture.REGULAR, getFontSize());

            case "italic":
                return Font.font(label.getFont().getFamily(), FontPosture.ITALIC, getFontSize());

            case "bold":
                return Font.font(label.getFont().getFamily(), FontWeight.BOLD, getFontSize());

            case "italicBold":
                return Font.font(label.getFont().getFamily(), FontWeight.BOLD, FontPosture.ITALIC, getFontSize());
        }
        return null;
    }


    /*  ===========================================
     *      It displays the size of the font used
     *  ===========================================
     */
    private HBox fontSizeViewer() {
        Label fontSizeLabel = new Label();
        fontSizeLabel.textProperty().bind(Bindings.concat("Font size: ",
                slider.valueProperty().asString("%3.0f")));

        HBox sizeBox = new HBox(fontSizeLabel);
        sizeBox.setAlignment(Pos.CENTER);
        sizeBox.setPadding(new Insets(10, 0, 10, 0));
        return sizeBox;
    }

    /*  ================
     *     Combo Box
     *  ================
     */
    private ComboBox comboBox() {

// FIXME: Menu doesn't apply the functions if this code is running but the Slider works.
        familyChoice = new ComboBox<>(fonts);
        familyChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                label.setFont(Font.font(newValue, getFontSize()))
        );

        familyChoice.setPromptText("Please choose a font");
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


        italic.selectedProperty().addListener((observable, oldValue, newValue) ->
                italic.setOnAction(e -> label.setFont(styleSetter("italic"))));

        bold.selectedProperty().addListener((observable, oldValue, newValue) ->
                bold.setOnAction(e -> label.setFont(styleSetter("bold"))));

        HBox checkBox = new HBox(italic, bold);
        checkBox.setAlignment(Pos.CENTER);
        return checkBox;
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
                if (small.isArmed()) setFontSize(24);
                if (medium.isArmed()) setFontSize(36);
                if (large.isArmed()) setFontSize(48);
            }
        });

        HBox radioBtnBox = new HBox(small, medium, large);
        radioBtnBox.setAlignment(Pos.CENTER);
        return radioBtnBox;
    }

    private int getFontSize() {
        return (int) slider.getValue();
    }

    private void setFontSize(double fontSize) {
        slider.setValue(fontSize);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
