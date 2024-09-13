package stringPoolAndIntern;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class StringInterningDiagnosticTest {
    public static final Random rand = new Random();

    @BeforeAll
    static void setUp() throws ClassNotFoundException {
        Class.forName("common.JavaLogo");
    }

    @Test
    public void newStringVsLiteral() {
        String s1 = new String("J for Java");
        String s2 = "J for Java";
        System.out.println(s1 == s2);
        System.out.println(s1.equals(s2));
        System.out.println(s1.intern() == s2);

    }
    /*
            console input is not practical in Java test classes because
            the testing environment is designed to be automated and non-interactive.
            Instead, inputs should be simulated programmatically.
            String test = "Hello World!";
            ByteArrayInputStream in = new ByteArrayInputStream(test.getBytes());
            System.setIn(in);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter text: ");
            String input = scanner.nextLine();
     */
    @Test
    public void dynamicStringLiteralWillNotAddedToPoolAutomatically() {
        System.out.println("pid : " + getPid());
        String newStr = new String("new String");
        System.out.println(newStr.hashCode());
        String literal = "localString";
        String randomStr = literal + rand.nextInt(1, 100);
        String internStr = randomStr.intern();
        System.out.println(internStr);
    }

    @Test
    public void externalSourceStringLiteralWillNotAddedToPoolAutomatically() {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line.length());
                String intern = line.intern();
                System.out.println(intern);
            }
        } catch (IOException _) {
        }
    }

    private static long getPid() {
        return ProcessHandle.current().pid();
    }


}