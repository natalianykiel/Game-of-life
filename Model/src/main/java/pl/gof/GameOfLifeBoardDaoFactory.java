package pl.gof;

import java.io.IOException;
import java.sql.Connection;

public class GameOfLifeBoardDaoFactory {
    public static Dao<GameOfLifeBoard> createFileGameOfLifeBoardDao(String filename)
            throws IOException {
        return new FileGameOfLifeBoardDao(filename);
    }

    public static Dao<GameOfLifeBoard> createJdbcGameOfLifeBoardDao(Connection connection) {
        return new JdbcGameOfLifeBoardDao(connection);
    }
}
