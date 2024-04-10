package pl.first.firstjava;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Marcin Kwapisz
 */
public class AppTest {

    public AppTest() {
    }

    @Test
    public void testEmptParams() {
        String[] params = {};
        try {
            App.main(params);
        } catch (ArrayIndexOutOfBoundsException oobe) {
            System.out.println("Expected exception" + oobe);
        }

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> App.
                main(params));
    }

    @Test
    public void testWithParams() {
        String[] params = {"Student"};
        App.main(params);

    }

}
