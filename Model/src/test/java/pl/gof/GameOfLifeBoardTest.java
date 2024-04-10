package pl.gof;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameOfLifeBoardTest {

    @Test
    void constructorTest() {
        GameOfLifeBoard board1 = new GameOfLifeBoard(4, 4);
        GameOfLifeBoard board2 = new GameOfLifeBoard(4, 4);
        assertNotEquals(board1.getBoard(), board2.getBoard());
    }

    @Test
    void dieLonelyTest() {
        int width = 3;
        int height = 3;
        GameOfLifeBoard board = new GameOfLifeBoard(width, height);
        board.resetAll();
        board.getBoard().get(1).get(1).updateState(true);
        assertTrue(board.getBoard().get(1).get(1).getCellValue());
        board.doSimulationStep(new PlainGameOfLifeSimulator());
        assertFalse(board.getBoard().get(1).get(1).getCellValue());
        board.resetAll();
        board.getBoard().get(1).get(1).updateState(true);
        board.getBoard().get(1).get(2).updateState(true);
        assertTrue(board.getBoard().get(1).get(1).getCellValue());
        board.doSimulationStep(new PlainGameOfLifeSimulator());
        assertFalse(board.getBoard().get(1).get(1).getCellValue());
    }

    @Test
    void surviveTest() {
        int width = 3;
        int height = 3;
        GameOfLifeBoard board = new GameOfLifeBoard(width, height);
        board.resetAll();
        board.getBoard().get(1).get(1).updateState(true);
        board.getBoard().get(1).get(2).updateState(true);
        board.getBoard().get(1).get(0).updateState(true);
        assertTrue(board.getBoard().get(1).get(1).getCellValue());
        board.doSimulationStep(new PlainGameOfLifeSimulator());
        assertTrue(board.getBoard().get(1).get(1).getCellValue());
        board.resetAll();
        board.getBoard().get(1).get(1).updateState(true);
        board.getBoard().get(1).get(2).updateState(true);
        board.getBoard().get(1).get(0).updateState(true);
        board.getBoard().get(0).get(1).updateState(true);
        assertTrue(board.getBoard().get(1).get(1).getCellValue());
        board.doSimulationStep(new PlainGameOfLifeSimulator());
        assertTrue(board.getBoard().get(1).get(1).getCellValue());
    }

    @Test
    void dieOverpopulationTest() {
        int width = 3;
        int height = 3;
        GameOfLifeBoard board = new GameOfLifeBoard(width, height);
        board.resetAll();
        board.getBoard().get(1).get(1).updateState(true);
        board.getBoard().get(1).get(2).updateState(true);
        board.getBoard().get(1).get(0).updateState(true);
        board.getBoard().get(0).get(1).updateState(true);
        board.getBoard().get(2).get(1).updateState(true);
        assertTrue(board.getBoard().get(1).get(1).getCellValue());
        board.doSimulationStep(new PlainGameOfLifeSimulator());
        assertFalse(board.getBoard().get(1).get(1).getCellValue());
    }

    @Test
    void resurrectionTest() {
        int width = 3;
        int height = 3;
        GameOfLifeBoard board = new GameOfLifeBoard(width, height);
        board.resetAll();
        board.getBoard().get(1).get(2).updateState(true);
        board.getBoard().get(1).get(0).updateState(true);
        board.getBoard().get(0).get(1).updateState(true);
        assertFalse(board.getBoard().get(1).get(1).getCellValue());
        board.doSimulationStep(new PlainGameOfLifeSimulator());
        assertTrue(board.getBoard().get(1).get(1).getCellValue());
    }

    @Test
    void borderTest() {
        int width = 3;
        int height = 3;
        GameOfLifeBoard board = new GameOfLifeBoard(width, height);
        board.resetAll();
        board.getBoard().get(0).get(2).updateState(true);
        board.getBoard().get(0).get(0).updateState(true);
        board.getBoard().get(0).get(1).updateState(true);
        assertFalse(board.getBoard().get(2).get(2).getCellValue());
        board.doSimulationStep(new PlainGameOfLifeSimulator());
        assertTrue(board.getBoard().get(2).get(2).getCellValue());
    }

    @Test
    void copyBoardTest() {
        int width = 5;
        int height = 5;
        GameOfLifeBoard board = new GameOfLifeBoard(width, height);
        GameOfLifeBoard originalBoard = board.cloneBoard();

        // Dodatkowy test dla szczegółowego wyświetlenia błędu
        assertEquals(board, originalBoard);

        // Symulacja kroku i sprawdzenie, czy lista nie jest taka sama
        board.doSimulationStep(new PlainGameOfLifeSimulator());
        assertNotEquals(originalBoard, board);

        originalBoard.doSimulationStep(new PlainGameOfLifeSimulator());
        assertEquals(originalBoard, board);
    }

    @Test
    void setBoardTest() {
        int width = 3;
        int height = 3;
        GameOfLifeBoard board = new GameOfLifeBoard(width, height);

        // Tworzenie nowej listy i wypełnianie jej komórkami
        List<List<GameOfLifeCell>> newBoard = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            List<GameOfLifeCell> row = new ArrayList<>();
            for (int j = 0; j < height; j++) {
                row.add(new GameOfLifeCell());
            }
            newBoard.add(row);
        }

        // Ustawianie nowej listy w planszy
        board.setBoard(newBoard);

        // Porównywanie list
        assertEquals(newBoard, board.getBoard());
    }

    @Test
    public void testGetRow() {
        int width = 3;
        int height = 3;
        int countAlive = 0;
        int countDead = 0;
        GameOfLifeBoard board = new GameOfLifeBoard(width, height);
        board.resetAll();
        board.getBoard().get(0).get(2).updateState(true);
        board.getBoard().get(0).get(0).updateState(true);
        board.getBoard().get(0).get(1).updateState(true);
        for (int i = 0; i < width; i++) {
            if(board.getBoard().get(0).get(i).getCellValue()){
                countAlive++;
            }else {
                countDead++;
            }
        }
        GameOfLifeRow row = board.getRow(0);
        assertEquals(row.getAliveCount(),countAlive);
        assertEquals(row.getDeadCount(),countDead);
    }

    @Test
    public void testGetColumn(){
        int width = 3;
        int height = 3;
        int countAlive = 0;
        int countDead = 0;
        GameOfLifeBoard board = new GameOfLifeBoard(width, height);
        board.resetAll();
        board.getBoard().get(0).get(2).updateState(true);
        board.getBoard().get(0).get(0).updateState(true);
        board.getBoard().get(0).get(1).updateState(true);
        for (int j = 0; j < height; j++) {
            if(board.getBoard().get(j).get(0).getCellValue()){
                countAlive++;
            }else {
                countDead++;
            }
        }
        GameOfLifeColumn column = board.getGameOfLifeColumn(0);
        assertEquals(column.getAliveCount(),countAlive);
        assertEquals(column.getDeadCount(),countDead);
    }

    @Test
    public void testNeighbors() {
        int width = 5;
        int height = 5;
        GameOfLifeBoard board = new GameOfLifeBoard(width, height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                GameOfLifeCell cell = board.getBoard().get(i).get(j);
                List<GameOfLifeCell> neighbors = cell.getNeighbors();

                assertEquals(8, neighbors.size());

                for (GameOfLifeCell neighbor : neighbors) {
                    assertNotNull(neighbor);
                }
            }
        }
    }


    @Test
    public void testObserverPattern() {
        // Przygotowanie komórek i ich sąsiedztwa.
        GameOfLifeCell cell1 = new GameOfLifeCell();
        GameOfLifeCell cell2 = new GameOfLifeCell();
        GameOfLifeCell cell3 = new GameOfLifeCell();
        GameOfLifeCell cell4 = new GameOfLifeCell();

        // Zerowanie komórek
        cell1.updateState(false);
        cell2.updateState(false);
        cell3.updateState(false);
        cell4.updateState(false);

        // Przygotowanie wiersza i kolumny.
        List<GameOfLifeCell> rowCells = Arrays.asList(cell1, cell2);
        GameOfLifeRow row = new GameOfLifeRow(rowCells);

        List<GameOfLifeCell> columnCells = Arrays.asList(cell1, cell3);
        GameOfLifeColumn column = new GameOfLifeColumn(columnCells);

        // Inicjalny stan - wszystkie komórki martwe.
        assertEquals(0, row.getAliveCount());
        assertEquals(2, row.getDeadCount());
        assertEquals(0, column.getAliveCount());
        assertEquals(2, column.getDeadCount());

        // Zmiana stanu komórki 1 na żywy.
        cell1.updateState(true);

        // Po zmianie stanu komórki 1, wiersz i kolumna powinny być zaktualizowane.
        assertEquals(1, row.getAliveCount());
        assertEquals(1, row.getDeadCount());
        assertEquals(1, column.getAliveCount());
        assertEquals(1, column.getDeadCount());

        // Zmiana stanu komórki 2 na żywy.
        cell2.updateState(true);

        // Po zmianie stanu komórki 2, wiersz powinien być zaktualizowany.
        assertEquals(2, row.getAliveCount());
        assertEquals(0, row.getDeadCount());
        // Kolumna nie powinna być zaktualizowana, ponieważ to nie jest jej komórka.
        assertEquals(1, column.getAliveCount());
        assertEquals(1, column.getDeadCount());

        // Zmiana stanu komórki 2 na martwy.
        cell2.updateState(false);

        // Po zmianie stanu komórki 2, wiersz i kolumna powinny być zaktualizowane.
        assertEquals(1, row.getAliveCount());
        assertEquals(1, row.getDeadCount());
        assertEquals(1, column.getAliveCount());
        assertEquals(1, column.getDeadCount());

        // Zmiana stanu komórki 1 na żywy.
        cell1.updateState(false);

        // Po zmianie stanu komórki 1, wiersz i kolumna powinny być zaktualizowane.
        assertEquals(0, row.getAliveCount());
        assertEquals(2, row.getDeadCount());
        assertEquals(0, column.getAliveCount());
        assertEquals(2, column.getDeadCount());
    }

    @Test
    public void testRemoveObserver() {
        GameOfLifeCell cell = new GameOfLifeCell();
        GameOfLifeRow row = new GameOfLifeRow(Arrays.asList(cell));

        // Sprawdzamy początkową liczbę obserwatorów.
        assertEquals(1, cell.getObservers().size());

        // Usuwamy obserwatora (wiersz) z komórki.
        cell.removeObserver(row);

        // Sprawdzamy, czy obserwator został usunięty.
        assertEquals(0, cell.getObservers().size());
    }

    @Test
    public void cellEqualsTest(){
        int width = 5;
        int height = 5;
        GameOfLifeBoard board = new GameOfLifeBoard(width, height);
        assertEquals(board.getBoard().get(3).get(3),board.getBoard().get(3).get(3));
        assertEquals(board.getBoard().get(3).get(3).hashCode(),board.getBoard().get(3).get(3).hashCode());
        assertNotEquals(board.getBoard().get(3).get(3),board.getBoard().get(3).get(2));
        assertNotEquals(board.getBoard().get(3).get(3), null);

    }

    @Test
    public void boardEqualsTest(){
        int width = 5;
        int height = 5;
        GameOfLifeBoard board = new GameOfLifeBoard(width, height);
        GameOfLifeBoard newBoard = board.cloneBoard();
        GameOfLifeBoard newBoard2 = new GameOfLifeBoard(width, height);
        GameOfLifeBoard newBoard3 = new GameOfLifeBoard(3, 3);
        assertEquals(newBoard, board);
        assertEquals(board.hashCode(),newBoard.hashCode());
        assertNotEquals(board,newBoard2);
        assertNotEquals(board,newBoard3);

        assertEquals(board,board);
        assertNotEquals(board,null);
    }

    @Test
    public void columnEqualsTest(){
        // Przygotowanie komórek i ich sąsiedztwa.
        GameOfLifeCell cell1 = new GameOfLifeCell();
        GameOfLifeCell cell2 = new GameOfLifeCell();
        GameOfLifeCell cell3 = new GameOfLifeCell();
        GameOfLifeCell cell4 = new GameOfLifeCell();
        GameOfLifeCell cell5 = new GameOfLifeCell();

        // Zerowanie komórek
        cell1.updateState(false);
        cell2.updateState(false);
        cell3.updateState(false);
        cell4.updateState(false);
        cell5.updateState(false);

        List<GameOfLifeCell> columnCells1 = Arrays.asList(cell1, cell3);
        GameOfLifeColumn column1 = new GameOfLifeColumn(columnCells1);
        List<GameOfLifeCell> columnCells2 = Arrays.asList(cell2, cell4);
        GameOfLifeColumn column2 = new GameOfLifeColumn(columnCells2);
        List<GameOfLifeCell> columnCells3 = Arrays.asList(cell5);
        GameOfLifeColumn column3 = new GameOfLifeColumn(columnCells3);

        assertEquals(column1,column2);
        assertEquals(column1.hashCode(),column2.hashCode());

        cell1.updateState(true);

        assertNotEquals(column1,column2);
        assertNotEquals(column2,column3);

        assertEquals(column1,column1);
        assertNotEquals(column1,null);
    }

    @Test
    public void rowEqualsTest(){
        // Przygotowanie komórek i ich sąsiedztwa.
        GameOfLifeCell cell1 = new GameOfLifeCell();
        GameOfLifeCell cell2 = new GameOfLifeCell();
        GameOfLifeCell cell3 = new GameOfLifeCell();
        GameOfLifeCell cell4 = new GameOfLifeCell();
        GameOfLifeCell cell5 = new GameOfLifeCell();

        // Zerowanie komórek
        cell1.updateState(false);
        cell2.updateState(false);
        cell3.updateState(false);
        cell4.updateState(false);
        cell5.updateState(false);

        // Przygotowanie wiersza i kolumny.
        List<GameOfLifeCell> rowCells1 = Arrays.asList(cell1, cell2);
        GameOfLifeRow row1 = new GameOfLifeRow(rowCells1);
        List<GameOfLifeCell> rowCells2 = Arrays.asList(cell3, cell4);
        GameOfLifeRow row2 = new GameOfLifeRow(rowCells2);
        List<GameOfLifeCell> rowCells3 = Arrays.asList(cell5);
        GameOfLifeRow row3 = new GameOfLifeRow(rowCells3);

        assertEquals(row1,row2);
        assertEquals(row1.hashCode(),row2.hashCode());

        cell1.updateState(true);

        assertNotEquals(row1,row2);
        assertNotEquals(row2,row3);

        assertEquals(row1,row1);
        assertNotEquals(row1,null);
    }

    @Test
    public void boardToStringTest(){
        int width = 5;
        int height = 5;

        GameOfLifeBoard board = new GameOfLifeBoard(width, height);
        String expected = "Board -> Width: 5, Height: 5";
        String result = board.toString();

        assertEquals(result,expected);
    }

    @Test
    public void cellToStringTest(){
        int width = 5;
        int height = 5;

        GameOfLifeBoard board = new GameOfLifeBoard(width, height);
        String expected = "Cell -> Value: false";
        board.getBoard().get(3).get(3).updateState(false);
        String result = board.getBoard().get(3).get(3).toString();
        assertEquals(result,expected);

        expected = "Cell -> Value: true";
        board.getBoard().get(3).get(3).updateState(true);
        result = board.getBoard().get(3).get(3).toString();
        assertEquals(result,expected);
    }

    @Test
    public void columnToStringTest(){
        GameOfLifeCell cell1 = new GameOfLifeCell();
        GameOfLifeCell cell2 = new GameOfLifeCell();

        cell1.updateState(false);
        cell2.updateState(false);

        List<GameOfLifeCell> columnCells1 = Arrays.asList(cell1, cell2);
        GameOfLifeColumn column1 = new GameOfLifeColumn(columnCells1);

        String expected = "Column -> Alive: 0, Dead: 2";
        String result = column1.toString();
        assertEquals(result,expected);

        cell1.updateState(true);

        expected = "Column -> Alive: 1, Dead: 1";
        result = column1.toString();
        assertEquals(result,expected);
    }

    @Test
    public void rowToStringTest(){
        GameOfLifeCell cell1 = new GameOfLifeCell();
        GameOfLifeCell cell2 = new GameOfLifeCell();

        cell1.updateState(false);
        cell2.updateState(false);

        List<GameOfLifeCell> rowCells1 = Arrays.asList(cell1, cell2);
        GameOfLifeRow row1 = new GameOfLifeRow(rowCells1);

        String expected = "Row -> Alive: 0, Dead: 2";
        String result = row1.toString();
        assertEquals(result,expected);

        cell1.updateState(true);

        expected = "Row -> Alive: 1, Dead: 1";
        result = row1.toString();
        assertEquals(result,expected);
    }

    @Test
    public void testBoardClone() {
        GameOfLifeBoard originalBoard = new GameOfLifeBoard(5, 5);

        // Klonowanie planszy
        GameOfLifeBoard clonedBoard = originalBoard.cloneBoard();

        // Sprawdzenie, czy plansze są różne obiektami, ale mają taką samą zawartość
        assertNotSame(originalBoard, clonedBoard);
        assertEquals(clonedBoard, originalBoard);
    }

    @Test
    public void testClone() {
        GameOfLifeCell cell = new GameOfLifeCell();
        ArrayList<GameOfLifeCell> neighbors = new ArrayList<>();
        neighbors.add(new GameOfLifeCell());
        neighbors.add(new GameOfLifeCell());
        cell.setNeighbors(neighbors);

        GameOfLifeCell clonedCell = cell.clone();
        assertNotNull(clonedCell);
        assertNotSame(cell, clonedCell);

        // Sprawdzanie sklonowanej listy sąsiadów
        assertNotNull(clonedCell.getNeighbors());
        assertNotSame(cell.getNeighbors(), clonedCell.getNeighbors());
        assertEquals(cell.getNeighbors().size(), clonedCell.getNeighbors().size());
        for (int i = 0; i < cell.getNeighbors().size(); i++) {
            assertNotSame(cell.getNeighbors().get(i), clonedCell.getNeighbors().get(i));
        }
    }

    @Test
    void testGameOfLifeRowClone() {
        GameOfLifeCell cell1 = new GameOfLifeCell();
        GameOfLifeCell cell2 = new GameOfLifeCell();
        // Tworzenie wiersza
        List<GameOfLifeCell> rowCells1 = Arrays.asList(cell1, cell2);
        GameOfLifeRow originalRow = new GameOfLifeRow(rowCells1);
        // Klonowanie wiersza
        GameOfLifeRow clonedRow = (GameOfLifeRow) originalRow.clone();

        // Sprawdzenie, czy wiersz został poprawnie sklonowany
        assertNotSame(originalRow, clonedRow); // Upewnienie się, że to nie ten sam obiekt
        assertEquals(originalRow, clonedRow); // Porównanie list komórek
    }

    @Test
    void testGameOfLifeColumnClone() {
        GameOfLifeCell cell1 = new GameOfLifeCell();
        GameOfLifeCell cell2 = new GameOfLifeCell();
        // Tworzenie kolumny
        List<GameOfLifeCell> columnCells1 = Arrays.asList(cell1, cell2);
        GameOfLifeColumn originalColumn = new GameOfLifeColumn(columnCells1);
        // Klonowanie kolumny
        GameOfLifeColumn clonedColumn = (GameOfLifeColumn) originalColumn.clone();

        // Sprawdzenie, czy kolumna została poprawnie sklonowana
        assertNotSame(originalColumn, clonedColumn); // Upewnienie się, że to nie ten sam obiekt
        assertEquals(originalColumn, clonedColumn); // Porównanie list komórek
    }

    @Test
    public void testCellComparison() {
        GameOfLifeCell cell1 = new GameOfLifeCell();
        GameOfLifeCell cell2 = new GameOfLifeCell();

        cell1.updateState(true);
        cell2.updateState(true);

        // Testowanie metod porównujących
        assertEquals(0, cell1.compareTo(cell1)); // Ta sama komórka
        assertEquals(0, cell2.compareTo(cell2)); // Ta sama komórka

        // Sprawdzanie wartości domyślnych
        assertEquals(cell1.getCellValue(), cell2.getCellValue());

        // Zmiana wartości jednej komórki
        cell1.updateState(!cell1.getCellValue());

        // Testowanie porównania zmienionej komórki
        assertEquals(-1, cell1.compareTo(cell2)); // cell1 < cell2
        assertEquals(1, cell2.compareTo(cell1)); // cell2 > cell1
    }

    @Test
    public void compareTo_NullArgument_ThrowsNullPointerException() {
        GameOfLifeCell cell = new GameOfLifeCell();
        assertThrows(NullPointerException.class, () -> cell.compareTo(null));
    }
}