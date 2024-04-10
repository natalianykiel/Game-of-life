package pl.first.firstjava;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class GreeterTest {

    public GreeterTest() {
    }

    @Test
    public void testGreet() {
        System.out.println("greet");
        String name = "Student";
        Greeter instance = new Greeter();
        String expResult = "Hello Student";
        String result = instance.greet(name);
        assertEquals(expResult, result);
    }

}
