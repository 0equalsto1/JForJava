package stringPoolAndIntern;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.regex.Pattern;

public class StringInterningTest {
    public static final String JCMD_PATH = "D:\\JDK\\jdk-21\\bin\\jcmd";
    public static final String VM_STRINGTABLE = "VM.stringtable";
    public static final String SPACE = " ";
    public static final Random rand = new Random();

    @Test
    public void hash() {
        System.out.println("8859_15".hashCode() & (127));
        System.out.println("ibm-866".hashCode() & (127));
    }

    @Test
    public void jcmd() {
        try {
            long pid = ProcessHandle.current().pid();
            System.out.println("pid : " + pid);
            String command = JCMD_PATH + SPACE + pid + SPACE + VM_STRINGTABLE;
//            ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            String grepPattern = "Number of literals";
            Pattern pattern = Pattern.compile(grepPattern);
            while ((line = reader.readLine()) != null) {
                if (pattern.matcher(line).find()) {
                    System.out.println(line.split("=")[0]);
                    break;
                }
            }
            process.waitFor();

        } catch (Exception _) {
        }
    }

    @Test
    public void dynamicStringLiteralWillNotAddedToPoolAutomatically1() {
        String localString = "localString1";
        System.out.println(localString);
        localString = "localString" + rand.nextInt(2, 3);
        System.out.println(localString);
        ProcessHandle processHandle = ProcessHandle.current();
        System.out.println(processHandle.pid());
        System.out.println("end...");

    }

    @Test
    public void dynamicStringLiteralWillNotAddedToPoolAutomatically2() {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                System.out.println(line.intern());
            }
        } catch (IOException _) {
        }
    }
}