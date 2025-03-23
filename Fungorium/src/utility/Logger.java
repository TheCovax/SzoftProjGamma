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
        log("-> " + methodName);
        indentLevel++;
    }

    public static void methodReturn(String methodName){
        indentLevel--;
        log("<- " + methodName);
    }

    public static void resetIndent(){
        indentLevel = 0;
    }
}
