package pl.gof;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.SimpleBooleanProperty;
import org.apache.commons.lang3.builder.HashCodeBuilder;


public class GameOfLifeBoard implements Serializable, Cloneable {
    private final int width;
    private final int height;

    private List<List<GameOfLifeCell>> board;

    public List<List<GameOfLifeCell>> getBoard() {
        return board;
    }


    public void setBoard(List<List<GameOfLifeCell>> board) {
        this.board = board;
        setNeighborsForAllCells();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public GameOfLifeBoard(int width, int height) {
        this.width = width;
        this.height = height;
        board = createFixedList(width, height);
        board = Collections.unmodifiableList(board);
        // Initialize and set references to neighbors for each cell
        setNeighborsForAllCells();
    }

    // Helper method to create a fixed-size List
    private List<List<GameOfLifeCell>> createFixedList(int width, int height) {
        List<List<GameOfLifeCell>> list = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            List<GameOfLifeCell> row = new ArrayList<>();
            for (int j = 0; j < height; j++) {
                row.add(new GameOfLifeCell());
            }
            list.add(row);
        }
        return list;
    }

    public void resetAll() {
        for (List<GameOfLifeCell> row : board) {
            for (GameOfLifeCell cell : row) {
                cell.updateState(false);
            }
        }
    }

    public void doSimulationStep(GameOfLifeSimulator simulator) {
        simulator.doStep(this);
    }

    public GameOfLifeRow getRow(int y) {
        List<GameOfLifeCell> row = board.get(y);
        return new GameOfLifeRow(row);
    }

    public GameOfLifeColumn getGameOfLifeColumn(int x) {
        List<GameOfLifeCell> column = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            column.add(board.get(i).get(x));
        }
        return new GameOfLifeColumn(column);
    }


    private void setNeighborsForAllCells() {
        int[][] neighborOffsets = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1}, {1, 0},  {1, 1}
        };

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                GameOfLifeCell cell = board.get(x).get(y);
                List<GameOfLifeCell> cellNeighbors = new ArrayList<>();

                for (int[] offset : neighborOffsets) {
                    int xoffset = offset[0];
                    int yoffset = offset[1];
                    int neighborX = (x + xoffset + width) % width;
                    int neighborY = (y + yoffset + height) % height;

                    cellNeighbors.add(board.get(neighborX).get(neighborY));
                }

                cell.setNeighbors(cellNeighbors);
            }
        }
    }

    @Override
    public String toString() {
        String result;
        result = "Board -> Width: " + getWidth() + ", Height: " + getHeight();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        GameOfLifeBoard otherBoard = (GameOfLifeBoard) obj;

        // Porównanie szerokości i wysokości plansz
        if (width != otherBoard.width || height != otherBoard.height) {
            return false;
        }

        // Sprawdzenie, czy listy komórek istnieją i mają odpowiednią ilość wierszy i kolumn
        if (this.board.size() != otherBoard.board.size()) {
            return false;
        }

        for (int i = 0; i < width; i++) {
            if (this.board.get(i).size() != otherBoard.board.get(i).size()) {
                return false;
            }
        }

        // Porównanie stanu każdej komórki w planszach
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (this.board.get(i).get(j).getCellValue()
                        != otherBoard.board.get(i).get(j).getCellValue()) {
                    return false;
                }
            }
        }

        return true;
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder(5,9).append(height).append(width).append(board).toHashCode();
    }

    @Override
    public Object clone() {
        try {
            GameOfLifeBoard clonedBoard = (GameOfLifeBoard) super.clone();
            clonedBoard.board = copyBoardState(); // Głęboka kopia planszy
            clonedBoard.setNeighborsForAllCells();
            return clonedBoard;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public GameOfLifeBoard cloneBoard() {
        return (GameOfLifeBoard) this.clone();
    }

    public List<List<GameOfLifeCell>> copyBoardState() {
        List<List<GameOfLifeCell>> copiedBoard = new ArrayList<>();

        // Kopiowanie stanu aktualnej planszy do copiedBoard
        for (int i = 0; i < width; i++) {
            List<GameOfLifeCell> row = new ArrayList<>();
            for (int j = 0; j < height; j++) {
                GameOfLifeCell originalCell = this.board.get(i).get(j);
                GameOfLifeCell clonedCell = new GameOfLifeCell();
                // Utwórz nową komórkę o wartości identycznej z oryginalną
                clonedCell.updateState(originalCell.getCellValue());
                row.add(clonedCell);
            }
            copiedBoard.add(row);
        }
        return copiedBoard;
    }
}