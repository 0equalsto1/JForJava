package stringPoolAndIntern;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class StringInterningDiagnosticTest {
    public static final Random rand = new Random();
    @BeforeAll
    static void setUp() throws ClassNotFoundException {
        Class.forName("common.JavaLogo");
        String bash = "C:/Program Files/Git/bin/bash.exe";
        long pid = getPid();
        System.out.println("pid : " + pid);
    }

    @Test
    public void externalSourceStringLiteralWillNotAddedToPoolAutomatically() {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                System.out.println(line.intern());
            }
        } catch (IOException _) {
        }
    }

    @Test
    public void dynamicStringLiteralWillNotAddedToPoolAutomatically() {
        System.out.println("----------dynamicStringLiteralWillNotAddedToPoolAutomatically--------------");
        String literal = "localString1";
        literal = "localString" + rand.nextInt(2, 3);
    }

    @Test
    public void hash() {
        System.out.println("8859_15".hashCode() & (127));
        System.out.println("ibm-866".hashCode() & (127));
    }

    public void jcmdUtility(String[] commands) {
        try {
            ProcessBuilder builder = new ProcessBuilder(commands);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();

        } catch (Exception _) {
        }
    }

    private static long getPid() {
        return ProcessHandle.current().pid();
    }


}