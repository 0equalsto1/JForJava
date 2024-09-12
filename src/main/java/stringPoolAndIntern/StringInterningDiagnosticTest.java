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
    public static String STRING_TABLE_STATISTICS = "jcmd \"%s\" VM.stringtable|grep -m 1 'Number of literals'";
    public static String STRING_TABLE_KEY_VALUE = "jcmd \"%s\" VM.stringtable -verbose|grep 'localString1'";
    public static String[] statisticsCmd;
    public static String[] printKeyValueCmd;

    @BeforeAll
    static void setUp() throws ClassNotFoundException {
        Class.forName("common.JavaLogo");
        String bash = "C:/Program Files/Git/bin/bash.exe";
        long pid = getPid();
        System.out.println("pid : " + pid);
        STRING_TABLE_STATISTICS = String.format(STRING_TABLE_STATISTICS, pid);
        STRING_TABLE_KEY_VALUE = String.format(STRING_TABLE_KEY_VALUE, pid);
        String option = "-c";//It tells the shell to read and execute the command(s) from the string that follows, rather than from a script file or interactive input.
        statisticsCmd = new String[]{bash, option, STRING_TABLE_STATISTICS};
        printKeyValueCmd = new String[]{bash, option, STRING_TABLE_KEY_VALUE};
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
    public void dynamicStringLiteralWillNotAddedToPoolAutomatically1() {
        jcmdUtility(statisticsCmd);
        jcmdUtility(printKeyValueCmd);
        String literal = "localString1";
        jcmdUtility(printKeyValueCmd);
        literal = "localString" + rand.nextInt(2, 3);
        jcmdUtility(printKeyValueCmd);
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