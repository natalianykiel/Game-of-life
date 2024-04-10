package pl.gof;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcGameOfLifeBoardDao implements Dao<GameOfLifeBoard> {
    private Connection connection;

    public JdbcGameOfLifeBoardDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public GameOfLifeBoard read() throws IOException, ClassNotFoundException {
        GameOfLifeBoard board = null;
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Komórki c INNER JOIN Plansze p ON c.plansza_id = p.id");

            // Przygotuj struktury do odczytania planszy
            List<List<GameOfLifeCell>> cells = new ArrayList<>();
            int width = 0;
            int height = 0;

            while (rs.next()) {
                // Odczytaj dane komórki
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                int value = rs.getInt("wartosc");

                // Aktualizuj szerokość i wysokość planszy
                width = Math.max(width, x + 1);
                height = Math.max(height, y + 1);

                // Dodaj nową listę, jeśli jeszcze nie istnieje dla danego wiersza
                while (cells.size() <= x) cells.add(new ArrayList<>());

                // Dodaj nowe komórki, jeśli jeszcze nie istnieją dla danego wiersza i kolumny
                while (cells.get(x).size() <= y) cells.get(x).add(new GameOfLifeCell());

                // Ustaw wartość dla konkretnej komórki
                cells.get(x).get(y).updateState(value == 1);
            }

            // Utwórz planszę na podstawie odczytanych danych
            board = new GameOfLifeBoard(width, height);
            board.setBoard(cells);

        } catch (SQLException e) {
            throw new IOException("Error reading board from database", e);
        }
        return board;
    }


    @Override
    public void write(GameOfLifeBoard obj) throws IOException {
        try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Plansze (nazwa) VALUES (?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, "Nazwa planszy");
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            int boardId;
            if (generatedKeys.next()) {
                boardId = generatedKeys.getInt(1);

                // Zapisz komórki planszy
                for (int x = 0; x < obj.getWidth(); x++) {
                    for (int y = 0; y < obj.getHeight(); y++) {
                        int value = 0;
                        if(obj.getBoard().get(x).get(y).getCellValue()){
                            value=1;
                        }

                        // Przygotuj zapytanie SQL do zapisania danych z obj do tabeli Komórki
                        try (PreparedStatement cellPstmt = connection.prepareStatement(
                                "INSERT INTO Komórki (x, y, wartosc, plansza_id) VALUES (?, ?, ?, ?)")) {
                            cellPstmt.setInt(1, x);
                            cellPstmt.setInt(2, y);
                            cellPstmt.setInt(3, value);
                            cellPstmt.setInt(4, boardId);
                            cellPstmt.executeUpdate();
                        }
                    }
                }
            } else {
                throw new SQLException("Creating board failed, no ID obtained.");
            }
        } catch (SQLException e) {
            throw new IOException("Error writing board to database", e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new IOException("Error closing connection", e);
        }
    }
}
