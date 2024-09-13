package stringPoolAndIntern;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;

public class StringInterningDiagnostic {
    static final Scanner scanner = new Scanner(System.in);
    static String STRING_TABLE_STATISTICS = "jcmd %s VM.stringtable|grep -m 1 '%s'";
    static String STRING_TABLE_KEY_VALUE = "jcmd %s VM.stringtable -verbose|grep '%s'";
    static String[] statisticsCmd;
    static String[] printKeyValueCmd;
    static long pid;
    static String NO = Character.toString(0x274C);
    static String YES = Character.toString(0x2705);

    //It tells the shell to read and execute the command(s) from the string that follows, rather than from a script file or interactive input.
    public static final String OPTION = "-c";
    public static final String emoji = "-c";

    public static final String BASH = "C:/Program Files/Git/bin/bash.exe";

    static {
        try {
            Class.forName("common.JavaLogo");
            pid = getPid();
            System.out.println("pid : " + pid);
            STRING_TABLE_STATISTICS = String.format(STRING_TABLE_STATISTICS, pid, "Number of literals");
            statisticsCmd = new String[]{BASH, OPTION, STRING_TABLE_STATISTICS};
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Random rand = new Random();

    public static void main(String[] args) {
        externalSourcesLiteralWillNotAddedToPoolAutomatically();
    }

    public static void externalSourcesLiteralWillNotAddedToPoolAutomatically() {
        System.out.print("before entering a string ->> ");
        jcmdUtility(statisticsCmd);
        System.out.println("enter a string : ");
        String literal = scanner.nextLine();
        System.out.print("after entering a string ->> ");
        jcmdUtility(statisticsCmd);
        setCommand(literal);
        System.out.print("after setCommand ->> ");
        jcmdUtility(statisticsCmd);
        checkKeyValueInStringPool(literal);
        literal = "localString1";//     OR        literal = literal.intern();
        System.out.print("after creating a literal ->> ");
        jcmdUtility(statisticsCmd);
        checkKeyValueInStringPool(literal);
    }

    private static void checkKeyValueInStringPool(String literal) {
        boolean isExist = jcmdUtility(printKeyValueCmd);
        System.out.println("[" + literal + "]" + " in string pool ->>> " + (isExist ? YES : NO));
    }

    private static void setCommand(String literal) {
        STRING_TABLE_KEY_VALUE = String.format(STRING_TABLE_KEY_VALUE, pid, literal);
        printKeyValueCmd = new String[]{BASH, OPTION, STRING_TABLE_KEY_VALUE};
    }

    public static boolean jcmdUtility(String[] commands) {
        boolean isLiteralInsidePool = false;
        try {
            ProcessBuilder builder = new ProcessBuilder(commands);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                isLiteralInsidePool = true;
            }
            process.waitFor();

        } catch (Exception _) {
        }
        return isLiteralInsidePool;
    }

    private static long getPid() {
        return ProcessHandle.current().pid();
    }


}