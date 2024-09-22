package stringPoolAndIntern;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StringInterningDiagnosticTest {
    public static final Random rand = new Random();
    static long pid = ProcessHandle.current().pid();

    @BeforeAll
    static void init() throws ClassNotFoundException {
        Class.forName("common.JforJava");
        System.out.println("pid : " + pid);
    }

    @Test
    public void newStringVsLiteral() {
        String s1 = new String("JforJava");
        String s2 = "JforJava";
        System.out.println(s1 == s2);
        System.out.println(s1.equals(s2));
        System.out.println(s1.intern() == s2);
    }

    @Test
    public void testFieldsLiterals() throws ReflectiveOperationException {
        Class<?> clazz = Class.forName("stringPoolAndIntern.StringInterningAtClassLoading");
        StringInterningAtClassLoading instance = (StringInterningAtClassLoading) clazz.getDeclaredConstructor().newInstance();
        System.out.println(instance.hashCode());


    }

    // jcmd pid VM.stringtable|grep -m 1 'Number of literals'
    // jcmd pid VM.stringtable -verbose|grep -i 'ClassLiteral'
    @Test
    public void stringLiteral() {
        String literal = "localString";
        System.out.println(literal);
    }

    @Test
    public void newString() {
        String newStr = new String("JforJava");
        String internStr = newStr.intern();
        System.out.println(internStr);
    }

    /*
        console input is not practical in Java test classes because the testing environment is designed to be
        automated and non-interactive.Instead, inputs should be simulated programmatically.
        String test = "Hello World!";
        ByteArrayInputStream in = new ByteArrayInputStream(test.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter text: ");
        String input = scanner.nextLine();
    */
     /*
        rand.nextInt(1, 10) + "randomStr" -> \x01randomStr created inside pool
        "randomStr" + rand.nextInt(1, 10)  -> randomStr\x01 created inside pool
        \x01 appears due to the concatenation . Next time
        the code is executed, the JVM will refer to randomStr\x01 for the
        concatenation result of "randomStr" + .
        */
    @Test
    public void runtimeStringWillNotAddedToPoolAutomatically() throws InterruptedException {
        String randomStr = String.valueOf(rand.nextInt(131313));
        String internStr = randomStr.intern();
        System.out.println(internStr);

        String hello = "Hello", lo = "lo";
        String hel = "Hel" + lo;  //this will also runtime string
        System.out.println(hello == hel);
        System.out.println(hello == hel.intern());

    }

    @Test
    public void externalSourceStringLiteralWillNotAddedToPoolAutomatically() {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                String intern = line.intern();
                System.out.println(intern);
            }
        } catch (IOException _) {
        }
    }

    @Test
    public void hash() {
        int hash1 = "8859_15".hashCode();
        System.out.println("hash of 8859_15 : " + hash1);
        System.out.println("binary of hash1 : " + Integer.toBinaryString(hash1));
        System.out.println("index of 8859_15 :" + (hash1 & (65535)));
        System.out.println("index of 8859_15 :" + (hash1 & (127)));
        int hash2 = "ibm-866".hashCode();
        System.out.println("hash of ibm-866 : " + hash2);
        System.out.println("binary of hash2 : " + Integer.toBinaryString(hash2));
        System.out.println("index of ibm-866 :" + (hash2 & (65535)));
        System.out.println("index of ibm-866 :" + (hash2 & (127)));
        System.out.println(Integer.toBinaryString(127));
        System.out.println(Integer.toBinaryString(65535));


    }
}