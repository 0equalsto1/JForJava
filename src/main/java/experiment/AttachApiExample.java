package experiment;

import com.sun.tools.attach.VirtualMachine;

public class AttachApiExample {
    public static void main(String[] args) {
        try {
            // Get the current JVM process ID (or another target JVM PID)
//            String pid = ProcessHandle.current().pid() + "";

            // Attach to the JVM
            VirtualMachine vm = VirtualMachine.attach(String.valueOf(13096));

            // Use jcmd-style commands with the Attach API (e.g., trigger GC)
            vm.loadAgentLibrary("jdk.attach", "GC.run");

            // Detach from the JVM
            vm.detach();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
