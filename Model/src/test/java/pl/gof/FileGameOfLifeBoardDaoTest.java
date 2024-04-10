package pl.gof;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class FileGameOfLifeBoardDaoTest {

    @TempDir
    static File tempDir;

    private static File testFile;

    @BeforeEach
    void setUp() {
        testFile = new File(tempDir, "test_board.txt");
    }


    @AfterEach
    void tearDown() {
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    void testFileGameOfLifeBoardDaoReadWrite() {
        GameOfLifeBoard testBoard = new GameOfLifeBoard(5, 5);

        try (Dao<GameOfLifeBoard> dao = GameOfLifeBoardDaoFactory.createFileGameOfLifeBoardDao(testFile.getAbsolutePath())) {

            // Zapis do pliku
            dao.write(testBoard);

            // Odczyt z pliku
            GameOfLifeBoard readBoard = dao.read();
            assertEquals(testBoard, readBoard);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            fail("Test failed with exception: " + e.getMessage());
        }
    }


    @Test
    void testFileGameOfLifeBoardDaoClose() {
        try {
            assertDoesNotThrow(() -> {
                try (FileGameOfLifeBoardDao dao = new FileGameOfLifeBoardDao(testFile.getAbsolutePath())) {
                    assertNotNull(dao);
                }
            });

            // Usunięcie pliku po zakończeniu testu
            assertTrue(testFile.delete());

        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }
}