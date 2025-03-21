package utility;

/**
 * Utility class for structured console logging with indentation,
 * simulating method call hierarchies in textual form.
 */
public class Logger {
    private static int indentLevel = 0;

    public static void log(String message){
        System.out.println("\t".repeat(indentLevel) + message);
    }

    public static void methodCall(String methodName){
        log("-> " + methodName + " called");
        indentLevel++;
    }

    public static void methodReturn(String methodName){
        log("-> " + methodName + " returned");
    }

    public static void resetIndent(){
        indentLevel = 0;
    }
}
