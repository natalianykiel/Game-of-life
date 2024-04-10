package pl.gof;

import java.io.IOException;

public interface Dao<T> extends AutoCloseable {
    T read() throws IOException, ClassNotFoundException;

    void write(T obj) throws IOException;

    void close() throws IOException;
}
