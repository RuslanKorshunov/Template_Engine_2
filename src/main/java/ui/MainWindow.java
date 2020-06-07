package ui;

import controller.CityController;
import controller.Controller;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainWindow extends Application {
    private static final String TITLE;
    private static final String WELCOME_LABEL;
    private static final String SECOND_WELCOME_LABEL;
    private static final String BACK_LABEL;
    private static final String NEXT_LABEL;
    private static final String SAFE_LABEL;
    private static final String SCS_LABEL;
    private static final String NO_ROWS_LABEL;
    private static final String CITY_LABEL;
    private static final String CHOOSE_FROM_FILE_LABEL;
    private static final String LOAD_LABEL;
    private static final String CHOOSE_MODE_LABEL;
    private static final String FIRST_MODE_LABEL;
    private static final String SECOND_MODE_LABEL;
    private static final String FONT;
    private static final String SEMICOLON;
    private static final String SPACE;
    private static final double WIDTH;
    private static final double HEIGHT;
    private static double WIDTH_SCREEN;
    private static double HEIGHT_SCREEN;

    static {
        TITLE = "Template Engine";
        WELCOME_LABEL = "Пожалуйста, введите страницы-источники для парсинга (вводите через \";\")";
        SECOND_WELCOME_LABEL = "Пожалуйста, введите необходимые поля";
        BACK_LABEL = "Назад";
        NEXT_LABEL = "Далее";
        SAFE_LABEL = "Сохранить";
        SCS_LABEL = "SCS";
        NO_ROWS_LABEL = "Нет данных для отображения";
        CITY_LABEL = "Город";
        CHOOSE_FROM_FILE_LABEL = "или выберете csv-файл для загрузки";
        LOAD_LABEL = "Загрузить";
        CHOOSE_MODE_LABEL = "Выберете режим:";
        FIRST_MODE_LABEL = "парсинг на основе имеющихся ключевых слов";
        SECOND_MODE_LABEL = "обычный парсинг";
        FONT = "Arial";
        SEMICOLON = ";";
        SPACE = "\\s";
        WIDTH = 600;
        HEIGHT = WIDTH;
        WIDTH_SCREEN = 0;
        HEIGHT_SCREEN = 0;
    }

    private Stage stage;
    private AnchorPane root;
    private Controller controller;
    private Map<String, String> cities;

    public static void main(String... args) {
        setScreenParameters();
        Application.launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        controller = new CityController();

        this.stage = stage;
        stage.setTitle(TITLE);
        stage.setResizable(false);

        root = new AnchorPane();
        createWelcomePage();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);

        stage.show();
    }

    private void createWelcomePage() {
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.centerOnScreen();

        Font font = createFont(14);

        Label welcomeLabel = new Label(WELCOME_LABEL);
        welcomeLabel.setWrapText(true);
        welcomeLabel.setFont(font);
        AnchorPane.setTopAnchor(welcomeLabel, 40.0);
        AnchorPane.setLeftAnchor(welcomeLabel, 120.0);
        AnchorPane.setRightAnchor(welcomeLabel, 120.0);

        Label chooseMoodLabel = new Label(CHOOSE_MODE_LABEL);
        chooseMoodLabel.setWrapText(true);
        chooseMoodLabel.setFont(font);
        AnchorPane.setTopAnchor(chooseMoodLabel, 405.0);
        AnchorPane.setLeftAnchor(chooseMoodLabel, 140.0);
        AnchorPane.setRightAnchor(chooseMoodLabel, 140.0);

        Label chooseFromFileLabel = new Label(CHOOSE_FROM_FILE_LABEL);
        chooseFromFileLabel.setFont(font);
        AnchorPane.setTopAnchor(chooseFromFileLabel, 365.0);
        AnchorPane.setLeftAnchor(chooseFromFileLabel, 120.0);

        TextArea wikiPageArea = new TextArea();
        wikiPageArea.setFont(font);
        AnchorPane.setTopAnchor(wikiPageArea, 90.0);
        AnchorPane.setLeftAnchor(wikiPageArea, 60.0);
        AnchorPane.setRightAnchor(wikiPageArea, 60.0);
        AnchorPane.setBottomAnchor(wikiPageArea, 220.0);

        Button nextButton = new Button(NEXT_LABEL);
        nextButton.setFont(font);
        AnchorPane.setBottomAnchor(nextButton, 40.0);
        AnchorPane.setLeftAnchor(nextButton, 250.0);
        AnchorPane.setRightAnchor(nextButton, 250.0);

        Button loadButton = new Button(LOAD_LABEL);
        loadButton.setFont(font);
        AnchorPane.setTopAnchor(loadButton, 360.0);
        AnchorPane.setLeftAnchor(loadButton, 370.0);

        ToggleGroup modeGroup = new ToggleGroup();

        RadioButton firstMode = new RadioButton(FIRST_MODE_LABEL);
        firstMode.setFont(font);
        firstMode.setToggleGroup(modeGroup);
        AnchorPane.setTopAnchor(firstMode, 425.0);
        AnchorPane.setLeftAnchor(firstMode, 140.0);


        RadioButton secondMode = new RadioButton(SECOND_MODE_LABEL);
        secondMode.setFont(font);
        secondMode.setToggleGroup(modeGroup);
        AnchorPane.setTopAnchor(secondMode, 450.0);
        AnchorPane.setLeftAnchor(secondMode, 140.0);

        root.getChildren().clear();
        root.getChildren().addAll(chooseFromFileLabel, welcomeLabel, chooseMoodLabel, wikiPageArea,
                nextButton, firstMode, secondMode, loadButton);

        nextButton.setOnAction(event -> {
            String text = wikiPageArea.getText().replaceAll(SPACE, "");
            if (!text.equals("")) {
                List<String> cities = List.of(text.split(SEMICOLON));
                if (firstMode.isSelected()) {
                    createTablePage(cities, 1);
                } else {
                    createTablePage(cities, 2);
                }
            }
        });
    }

    private void createTablePage(List<String> cities, int mode) {
        stage.setResizable(true);

        this.cities = controller.parseData(cities, mode);

        System.out.println(this.cities);

        Font font = createFont(14);

        double cityLabelTopHeight = HEIGHT_SCREEN * 0.01;
        double cityLabelLeftWidth = WIDTH_SCREEN * 0.01;
        Label cityLabel = new Label(CITY_LABEL);
        AnchorPane.setTopAnchor(cityLabel, cityLabelTopHeight);
        AnchorPane.setLeftAnchor(cityLabel, cityLabelLeftWidth);
        cityLabel.setFont(font);

        AnchorPane vBox = CityPageBuilder.build(null);
        AnchorPane.setTopAnchor(vBox, HEIGHT_SCREEN * 0.01);
        AnchorPane.setBottomAnchor(vBox, HEIGHT_SCREEN * 0.01);
        AnchorPane.setLeftAnchor(vBox, WIDTH_SCREEN * 0.3);
        AnchorPane.setRightAnchor(vBox, WIDTH_SCREEN * 0.01);

        ComboBox<String> cityBox = new ComboBox<>(FXCollections.
                observableList(new ArrayList<>(this.cities.keySet())));
        AnchorPane.setTopAnchor(cityBox, HEIGHT_SCREEN * 0.05);
        AnchorPane.setLeftAnchor(cityBox, cityLabelLeftWidth);
        AnchorPane.setRightAnchor(cityBox, WIDTH_SCREEN * 0.9);
        cityBox.setOnAction(event -> {
            Map<String, List<String>> result = controller.
                    getData(this.cities.get(cityBox.getValue()));
            AnchorPane v = CityPageBuilder.build(result);
            vBox.getChildren().remove(0, vBox.getChildren().size());
            vBox.getChildren().addAll(v.getChildren());
        });

        Button backButton = new Button(BACK_LABEL);
        AnchorPane.setBottomAnchor(backButton, HEIGHT_SCREEN * 0.01);
        AnchorPane.setLeftAnchor(backButton, WIDTH_SCREEN * 0.01);
        backButton.setFont(font);
        backButton.setOnAction(event ->
                createWelcomePage()
        );

        Button safeButton = new Button(SAFE_LABEL);
        AnchorPane.setBottomAnchor(safeButton, HEIGHT_SCREEN * 0.01);
        AnchorPane.setLeftAnchor(safeButton, WIDTH_SCREEN * 0.06);
        safeButton.setFont(font);

        Button scsButton = new Button(SCS_LABEL);
        AnchorPane.setBottomAnchor(scsButton, HEIGHT_SCREEN * 0.01);
        AnchorPane.setLeftAnchor(scsButton, WIDTH_SCREEN * 0.13);
        scsButton.setFont(font);
        scsButton.setOnAction(event ->
                controller.writeInScs());


        root.getChildren().clear();
        stage.setHeight(HEIGHT_SCREEN);
        stage.setWidth(WIDTH_SCREEN);
        stage.centerOnScreen();
        root.getChildren().addAll(cityLabel, cityBox,
                vBox, backButton, safeButton, scsButton);
    }

    private Font createFont(int size) {
        return new Font(FONT, size);
    }

    private static void setScreenParameters() {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        HEIGHT_SCREEN = bounds.getHeight();
        WIDTH_SCREEN = bounds.getWidth();
    }
}
