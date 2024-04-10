package pl.gof;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameOfLifeBoardDaoTest {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/GoL";
    private static final String USER = "user";
    private static final String PASSWORD = "123";

    private static Connection connection;
    private static Dao<GameOfLifeBoard> jdbcDao;

    @BeforeAll
    public static void setUp() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        connection.setAutoCommit(false); // Wyłączenie autocommita dla transakcji
        jdbcDao = GameOfLifeBoardDaoFactory.createJdbcGameOfLifeBoardDao(connection);
    }

    @AfterEach
    public void rollback() throws SQLException {
        connection.rollback(); // Wycofanie transakcji po każdym teście
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testReadWriteGameOfLifeBoard() throws IOException, ClassNotFoundException {
        try {
            // Tworzenie przykładowej planszy
            GameOfLifeBoard originalBoard = new GameOfLifeBoard(10, 10);
            // Ustawianie stanu planszy...

            // Zapisywanie planszy do bazy danych
            jdbcDao.write(originalBoard);

            // Odczytanie planszy z bazy danych
            GameOfLifeBoard retrievedBoard = jdbcDao.read();

            // Sprawdzanie, czy odczytana plansza nie jest nullem
            assertNotNull(retrievedBoard);

            // Porównanie odczytanej planszy z oryginalną planszą
            assertEquals(originalBoard, retrievedBoard);
        } finally {
            try {
                connection.commit(); // Potwierdzenie transakcji po teście
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
