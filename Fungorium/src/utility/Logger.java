package Fungorium.src.utility;

/**
 * Utility class for structured console logging with indentation,
 * simulating method call hierarchies in textual form.
 */
public class Logger {
    private static int indentLevel = 0;
    private static boolean enabled = true;

    public static void log(String message){
        if (enabled) {
            System.out.println("\t".repeat(indentLevel) + message);
        }
    }

    public static void methodCall(String methodName){
        if (enabled){
            log("-> " + methodName);
            indentLevel++;
        }
    }

    public static void methodReturn(String methodName){
        if (enabled){
            indentLevel--;
            log("<- " + methodName);
        }
    }



    public static void setEnabled(boolean state){
        enabled = state;
    }
    public static void resetIndent(){
        indentLevel = 0;
    }
}
