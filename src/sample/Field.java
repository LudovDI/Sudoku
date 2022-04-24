package sample;

/**
 * Класс, который решает заданное пользователем поле Судоку
 */
public class Field {
    private final int[][] field; // поле Судоку

    /**
     * Конструктор класса Field
     */
    public Field() {
        field = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                field[i][j] = 0;
            }
        }
    }

    /**
     * Метод, который возвращает значение заданной ячейки Судоку
     */
    public int getCellField(int i, int j) {
        return field[i][j];
    }

    /**
     * Метод, который заполняет ячейку Судоку заданным числом
     */
    public void setCellField(int i, int j, int number) {
        field[i][j] = number;
    }

    /**
     * Метод, который проверяет, полная доска или нет
     */
    public boolean isFull() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (getCellField(i, j) == 0) return false;
            }
        }
        return true;
    }

    /**
     * Метод, который проверяет, пустая доска или нет
     */
    public boolean isEmpty() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (getCellField(i, j) != 0) return false;
            }
        }
        return true;
    }

    /**
     * Метод, который копирует поле Судоку
     */
    public Field copyField(Field field) {
        Field oldField = new Field();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                oldField.setCellField(i, j, field.getCellField(i, j));
            }
        }
        return oldField;
    }

    /**
     * Метод, который проверяет, можно ли вставить цифру в данную ячейку Судоку
     */
    public boolean canDoSelect(int selectNumber, int x, int y) {
        for (int i = 0; i < 9; i++) {
            if (field[x][i] == selectNumber || field[i][y] == selectNumber) {
                return false;
            }
        }
        for (int i = 3 * (x / 3); i < 3 * (x / 3) + 3; i++) {
            for (int j = 3 * (y / 3); j < 3 * (y / 3) + 3; j++) {
                if (field[i][j] == selectNumber) {
                    return false;
                }
            }
        }
        this.field[x][y] = selectNumber;
        return true;
    }

    /**
     * Метод, который удаляет цифру в ячейке Судоку
     */
    public void deleteCellField(int x, int y) {
        this.field[x][y] = 0;
    }

    /**
     * Метод, который ищет решение Судоку.
     * Применяется алгоритм поиска в глубину.
     */
    public boolean solveSudoku() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (field[i][j] == 0) {
                    for (int number = 1; number <= 9; number++) {
                        if (canDoSelect(number, i, j)) {
                            field[i][j] = number;
                            if (solveSudoku()) {
                                return true;
                            } else {
                                field[i][j] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
}