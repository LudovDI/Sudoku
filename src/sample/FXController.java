package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class FXController {

    @FXML
    private GridPane table; // Поле Судоку, которое нужно заполнить цифрами

    private Label previousNumbers = new Label(); // Предыдущая цифра, у которой нужно убрать нижнее подчеркивание
    private boolean deleteNext = false; // Переменная для стирания цифр
    private Field field; // Поле Судоку, которое нужно передать в класс Field для его решения
    private Field oldField; // Поле Судоку, которое нужно сохранить в случае неудачи
    private int selectedNumber = 0; // Выбранная цифра, для заполнения ячеек Судоку
    Label[][] labels = new Label[9][9]; // Двумерный массив ячеек с цифрами Судоку для вывода на экран
    private static Stage rules; // Окно с правилами игры
    private static Stage info; // Окно с информацией о программе
    private boolean isLabelsPlaced = false; // Переменная для размещения labels в table

    /**
     * Конструктор класса FXController
     */
    public FXController() {
        oldField = new Field();
        field = new Field();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                labels[i][j] = new Label("");
                labels[i][j].setAlignment(Pos.CENTER);
                labels[i][j].setFont(Font.font(16));
                labels[i][j].setMaxSize(100.0, 100.0);
                labels[i][j].setOnMouseClicked(this::setNumber);
            }
        }
    }

    /**
     * Реализация кнопки "Новая игра"
     */
    public void newGame() {
        selectedNumber = 0;
        field = new Field();
        oldField = new Field();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                labels[i][j].setText("");
                labels[i][j].setStyle("-fx-text-fill: black;");
            }
        }
    }

    /**
     * Реализация кнопки "Выход"
     */
    public void exit() {
        Main.primary.close();
    }

    /**
     * Реализация кнопки "Выход" в окне "Правила игры"
     */
    public void ruleExit() {
        rules.close();
    }

    /**
     * Реализация кнопки "Выход" в окне "О программе"
     */
    public void programExit() {
        info.close();
    }

    /**
     * Реализация кнопки "Правила игры"
     */
    public void infoGame() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RulesOfGame.fxml"));
        Scene rulesScene = new Scene(root, 500, 500);
        Stage rulesWindow = new Stage();
        rulesWindow.setTitle("решатель Судоку");
        rulesWindow.setScene(rulesScene);
        rulesWindow.initOwner(Main.primary);
        rulesWindow.initModality(Modality.WINDOW_MODAL);
        rules = rulesWindow;
        rulesWindow.show();
    }

    /**
     * Реализация кнопки "О программе"
     */
    public void infoProgram() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AboutProgram.fxml"));
        Scene infoScene = new Scene(root, 450, 500);
        Stage infoWindow = new Stage();
        infoWindow.setTitle("Решатель Судоку");
        infoWindow.setScene(infoScene);
        infoWindow.initOwner(Main.primary);
        infoWindow.initModality(Modality.WINDOW_MODAL);
        info = infoWindow;
        infoWindow.show();

    }

    /**
     * Реализация кнопки "Х"
     */
    public void delete() {
        deleteNext = true;
    }

    /**
     * Реализация кнопки "Очистить все"
     */
    public void deleteAll() {
        oldField = new Field();
        field = new Field();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                labels[i][j].setText("");
                labels[i][j].setStyle("-fx-text-fill: black;");
            }
        }
    }

    /**
     * Реализация выбора цифры для заполнения поля Судоку
     */
    public void selectNumber(javafx.scene.input.MouseEvent mouseEvent) {
        deleteNext = false;
        if (!isLabelsPlaced) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    table.add(labels[i][j], i, j);
                }
            }
            isLabelsPlaced = true;
        }
        Label label = (Label) mouseEvent.getSource();
        previousNumbers.setUnderline(false);
        label.setUnderline(true);
        previousNumbers = label;
        selectedNumber = Integer.parseInt(label.getText());
    }

    /**
     * Реализация заполнения ячейки Судоку выбранной цифрой или удаление её содержимого
     */
    public void setNumber(javafx.scene.input.MouseEvent mouseEvent) {
        Label label = (Label) mouseEvent.getSource();
        int x = GridPane.getColumnIndex(label);
        int y = GridPane.getRowIndex(label);
        if (deleteNext) {
            label.setText("");
            field.deleteCellField(x, y);
        } else if (selectedNumber != 0) {
            if (field.canDoSelect(selectedNumber, x, y) || field.getCellField(x, y) != 0) {
                label.setText(String.valueOf(selectedNumber));
            } else {
                label.setText("X");
                Alert alert = new Alert(Alert.AlertType.NONE, "Ввод цифры " + selectedNumber +
                        " в эту ячейку противоречит правилам! Введите другую цифру или выберите другую ячейку.", ButtonType.OK);
                alert.setTitle("Ошибка");
                alert.showAndWait();
                label.setText("");
            }
        }
    }

    /**
     * Реализация кнопки "Следующий ход"
     */
    public void nextMove() {
        if (oldField.isEmpty()) oldField = oldField.copyField(field);
        if (oldField.isFull()) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Судоку решено.", ButtonType.OK);
            alert.setTitle("Внимание");
            alert.showAndWait();
        } else if (field.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Пустая доска.", ButtonType.OK);
            alert.setTitle("Ошибка");
            alert.showAndWait();
        } else if (field.solveSudoku()) {
            int i, j;
            do {
                i = (int) (Math.random() * 9);
                j = (int) (Math.random() * 9);
            } while (oldField.getCellField(i, j) != 0);
            labels[i][j].setText(String.valueOf(field.getCellField(i, j)));
            labels[i][j].setStyle("-fx-text-fill: blue;");
            oldField.setCellField(i, j, field.getCellField(i, j));
        } else {
            Alert alert = new Alert(Alert.AlertType.NONE, "Судоку не решается с такой растановкой цифр.", ButtonType.OK);
            alert.setTitle("Ошибка");
            alert.showAndWait();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    field.setCellField(i, j, oldField.getCellField(i, j));
                }
            }
        }
    }

    /**
     * Реализация кнопки "Решить сразу"
     */
    public void solveAll() {
        if (field.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Пустая доска.", ButtonType.OK);
            alert.setTitle("Ошибка");
            alert.showAndWait();
        } else {
            while (!oldField.isFull()) {
                nextMove();
            }
            Alert alert = new Alert(Alert.AlertType.NONE, "Судоку решено.", ButtonType.OK);
            alert.setTitle("Внимание");
            alert.showAndWait();
        }
    }
}
