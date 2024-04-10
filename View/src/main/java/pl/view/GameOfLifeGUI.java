package pl.view;


import exceptions.GameBoardLoadingException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import pl.gof.*;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import java.sql.Connection;

public class GameOfLifeGUI extends Application {

    private int boardSize = 10; // Default board size
    private double initialDensity = 0.3; // Default initial cell density
    private Timeline timeline;

    ResourceBundle texts;

    private static final Logger logger = Logger.getLogger(GameOfLifeGUI.class.getName());


    private final String resourceBundleBaseName = "MyResources";
    private Dao<GameOfLifeBoard> fileGameOfLifeBoardDao;

    private boolean isSimulationRunning = true;

    GameOfLifeBoard gameBoard;



    @Override
    public void start(Stage primaryStage) {
        try {
            LogManager.getLogManager().readConfiguration(
                    getClass().getResourceAsStream("/logging.properties")
            );

        } catch (IOException e) {
            e.printStackTrace();
        }

        texts = ResourceBundle.getBundle(resourceBundleBaseName);

        fileGameOfLifeBoardDao = createGameOfLifeBoardDao();

        Scene initialConfigScene = createInitialConfigScene(primaryStage);

        primaryStage.setScene(initialConfigScene);
        primaryStage.show();
    }

    private Scene createInitialConfigScene(Stage primaryStage) {
        VBox initialConfigLayout = new VBox(10);
        initialConfigLayout.setPadding(new Insets(10, 10, 10, 10));

        GridPane initialConfigGrid = new GridPane();
        initialConfigGrid.setAlignment(Pos.CENTER);
        initialConfigGrid.setHgap(10);
        initialConfigGrid.setVgap(10);

        Label sizeLabel = new Label(texts.getString("boardSize"));
        TextField sizeTextField = new TextField();
        sizeTextField.setText(String.valueOf(boardSize));
        initialConfigGrid.add(sizeLabel, 0, 0);
        initialConfigGrid.add(sizeTextField, 1, 0);

        Label densityLabel = new Label(texts.getString("initialDensity"));
        ComboBox<String> densityComboBox = new ComboBox<>();
        densityComboBox.getItems().addAll(texts.getString("small"), texts.getString("medium"), texts.getString("large"));
        densityComboBox.setValue(texts.getString("medium"));
        initialConfigGrid.add(densityLabel, 0, 1);
        initialConfigGrid.add(densityComboBox, 1, 1);

        Button startButton = new Button(texts.getString("startSimulation"));
        startButton.setOnAction(e -> {
            try {
                boardSize = Integer.parseInt(sizeTextField.getText());
                String selectedDensity = densityComboBox.getValue();
                if (selectedDensity.equals(texts.getString("small"))) {
                    initialDensity = 0.1;
                } else if (selectedDensity.equals(texts.getString("medium"))) {
                    initialDensity = 0.3;
                } else if (selectedDensity.equals(texts.getString("large"))) {
                    initialDensity = 0.5;
                }
                gameBoard = initializeGameOfLifeBoard(boardSize, initialDensity);
                startSimulation(primaryStage);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid board size.");
            }
        });
        initialConfigGrid.add(startButton, 0, 2);

        Button loadFromFileButton = new Button(texts.getString("loadFromFile"));
        loadFromFileButton.setOnAction(e -> {
            try {
                loadGameBoardFromDatabase(primaryStage);
            } catch (GameBoardLoadingException ex) {
                throw new RuntimeException(ex);
            }
        });
        initialConfigGrid.add(loadFromFileButton, 1, 3);

        Button changeLanguageButton = new Button(texts.getString("changeLanguage"));
        changeLanguageButton.setOnAction(e -> changeLanguage(primaryStage));
        initialConfigGrid.add(changeLanguageButton, 0, 4);

        initialConfigLayout.getChildren().add(initialConfigGrid);

        return new Scene(initialConfigLayout, 400, 400);
    }

    private void startSimulation(Stage primaryStage) {
        Scene simulationScene = createSimulationScene(primaryStage, gameBoard);
        primaryStage.setScene(simulationScene);

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            gameBoard.doSimulationStep(new PlainGameOfLifeSimulator());
            updateBoardInterface(gameBoard, (GridPane) ((VBox) simulationScene.getRoot()).getChildren().get(0));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private GameOfLifeBoard initializeGameOfLifeBoard(int size, double density) {
        GameOfLifeBoard board = new GameOfLifeBoard(size, size);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // Set cell as dead
                board.getBoard().get(i).get(j).updateState(random.nextDouble() < density); // Set cell as alive
            }
        }
        return board;
    }

    private void updateBoardInterface(GameOfLifeBoard gameBoard, GridPane boardGrid) {
        List<List<GameOfLifeCell>> board = gameBoard.getBoard();

        for (int i = 0; i < gameBoard.getHeight(); i++) {
            for (int j = 0; j < gameBoard.getWidth(); j++) {
                Rectangle cellRectangle = (Rectangle) boardGrid.getChildren().get(i * gameBoard.getWidth() + j);
                cellRectangle.setFill(board.get(i).get(j).getCellValue() ? Color.BLACK : Color.WHITE);
            }
        }
    }

    private Scene createSimulationScene(Stage primaryStage, GameOfLifeBoard gameBoard) {
        VBox simulationLayout = new VBox(10);
        simulationLayout.setPadding(new Insets(10, 10, 10, 10));

        GridPane boardGrid = new GridPane();

        // Bindowanie danych z gameBoard do interfejsu użytkownika
        for (int i = 0; i < gameBoard.getHeight(); i++) {
            for (int j = 0; j < gameBoard.getWidth(); j++) {
                Rectangle cellRectangle = new Rectangle(20, 20);

                // Wiązanie stanu komórki z gameBoard do interfejsu użytkownika
                BooleanProperty cellValueProperty = gameBoard.getBoard().get(i).get(j).cellValueProperty();
                cellRectangle.fillProperty().bind(Bindings.when(cellValueProperty).then(Color.BLACK).otherwise(Color.WHITE));
                cellRectangle.setOnMouseClicked(event -> {
                    if (!isSimulationRunning) {
                        // Zmiana stanu komórki w gameBoard po kliknięciu
                        cellValueProperty.set(!cellValueProperty.get());
                    }
                });

                boardGrid.add(cellRectangle, j, i);
            }
        }

        simulationLayout.getChildren().addAll(boardGrid);

        Button pauseResumeButton = new Button(texts.getString("pauseResume"));
        pauseResumeButton.setOnAction(e -> {
            if (isSimulationRunning) {
                timeline.pause();
                isSimulationRunning = false;
            } else {
                timeline.play();
                isSimulationRunning = true;
            }
        });
        simulationLayout.getChildren().add(pauseResumeButton);

        Button backButton = new Button(texts.getString("backToConfiguration"));
        backButton.setOnAction(e -> {
            primaryStage.setScene(createInitialConfigScene(primaryStage));
            timeline.stop();
        });
        simulationLayout.getChildren().add(backButton);

        Button saveToFileButton = new Button(texts.getString("saveToFile"));
        saveToFileButton.setOnAction(e -> saveGameBoardToDatabase(primaryStage, gameBoard));
        simulationLayout.getChildren().add(saveToFileButton);


        Button changeLanguageButton = new Button(texts.getString("changeLanguage"));
        changeLanguageButton.setOnAction(e -> changeLanguage(primaryStage));

        return new Scene(simulationLayout, 400, 400);
    }

    private void saveGameBoardToDatabase(Stage primaryStage, GameOfLifeBoard gameBoard) {
        try {
            fileGameOfLifeBoardDao.write(gameBoard);
            logger.info("Number of cells: " + gameBoard.getBoard().size());
            logger.info("Game board saved to database successfully.");
        } catch (IOException ex) {
            logger.severe("Error while saving the game board to database: " + ex.getMessage());
        }
    }

    private void loadGameBoardFromDatabase(Stage primaryStage) throws GameBoardLoadingException {
        try {
            GameOfLifeBoard loadedBoard = fileGameOfLifeBoardDao.read();

            if (loadedBoard != null) {
                gameBoard.setBoard(loadedBoard.copyBoardState());
                startSimulation(primaryStage);
                logger.info("Game board loaded from database successfully.");
            } else {
                throw new GameBoardLoadingException("error.loaded.board.null", texts);
            }
        } catch (IOException | ClassNotFoundException ex) {
            logger.severe("Exception caught while loading game board from database: " + ex.getMessage());
            throw new GameBoardLoadingException("error.loading.board", texts, ex);
        }
    }



    private Dao<GameOfLifeBoard> createGameOfLifeBoardDao() {
        Dao<GameOfLifeBoard> dao = null;
        try {
            // Tworzenie DAO obsługującego bazę danych
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/GoL", "user", "123");
            dao = GameOfLifeBoardDaoFactory.createJdbcGameOfLifeBoardDao(connection);
        } catch (SQLException e) {
            logger.severe("Error while creating DAO: " + e.getMessage());
        }
        return dao;
    }



    private void changeLanguage(Stage primaryStage) {
        if (texts.getLocale().getLanguage().equals("pl")) {
            texts = ResourceBundle.getBundle(resourceBundleBaseName, new Locale("en"));
        } else {
            texts = ResourceBundle.getBundle(resourceBundleBaseName, new Locale("pl"));
        }

        refreshUIWithNewLanguage(primaryStage);
    }

    private void refreshUIWithNewLanguage(Stage primaryStage) {
        Scene currentScene = primaryStage.getScene();

        if (currentScene != null) {
            VBox layout = (VBox) currentScene.getRoot();
            GridPane initialConfigGrid = (GridPane) layout.getChildren().get(0);

            Label sizeLabel = (Label) initialConfigGrid.getChildren().get(0);
            sizeLabel.setText(texts.getString("boardSize"));

            Label densityLabel = (Label) initialConfigGrid.getChildren().get(2);
            densityLabel.setText(texts.getString("initialDensity"));

            ComboBox<String> densityComboBox = (ComboBox<String>) initialConfigGrid.getChildren().get(3);
            densityComboBox.getItems().clear();
            densityComboBox.getItems().addAll(
                    texts.getString("small"),
                    texts.getString("medium"),
                    texts.getString("large")
            );
            densityComboBox.setValue(texts.getString("medium"));

            Button startButton = (Button) initialConfigGrid.getChildren().get(4);
            startButton.setText(texts.getString("startSimulation"));

            Button loadFromFileButton = (Button) initialConfigGrid.getChildren().get(5);
            loadFromFileButton.setText(texts.getString("loadFromFile"));

            primaryStage.setScene(currentScene);
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
