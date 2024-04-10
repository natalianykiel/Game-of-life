package pl.gof;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class FileGameOfLifeBoardDao implements Dao<GameOfLifeBoard>, AutoCloseable {
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final String filename;

    public FileGameOfLifeBoardDao(String filename) throws IOException {
        this.filename = filename;
        outputStream = new ObjectOutputStream(new FileOutputStream(filename));
    }

    @Override
    public GameOfLifeBoard read() throws IOException, ClassNotFoundException {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            return (GameOfLifeBoard) inputStream.readObject();
        }
    }

    @Override
    public void write(GameOfLifeBoard obj) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            outputStream.writeObject(obj);
        }
    }

    @Override
    public void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }
}
