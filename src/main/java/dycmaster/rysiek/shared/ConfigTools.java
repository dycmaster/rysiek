package dycmaster.rysiek.shared;


import com.google.common.base.Strings;

import java.util.Collection;

public class ConfigTools {
    private static final Collection<String> COMMENT_MARKS = Create.boxIntoSet("#");
    public static final String separator = ",";

    public static String getConfigLinePartToProcess(String line) {
        for (String commentMark : COMMENT_MARKS) {
            if (line.contains(commentMark)) {
                String res = line.substring(0, line.indexOf(commentMark));
                return res;
            }
        }
        return line;
    }

    public static boolean isConfigLineToBeProcessed(String line) {
        if (Strings.isNullOrEmpty(line)) {
            return false;
        }
        for (String comment : COMMENT_MARKS) {
            if (line.trim().startsWith(comment)) {
                return false;
            }
        }
        //line needs to contain at least two elements
        String[] elements = line.split(separator);
        if (elements.length < 3) {
            return false;
        }

        return true;
    }

    public static boolean isActionDispatcherConfigLineToBeProcessed(String line) {
        if (Strings.isNullOrEmpty(line)) {
            return false;
        }
        for (String comment : COMMENT_MARKS) {
            if (line.trim().startsWith(comment)) {
                return false;
            }
        }
        //line needs to contain at least two elements
        String[] elements = line.split(separator);
        if (elements.length < 3) {
            return false;
        }

        return true;
    }
    public static boolean isLayoutConfigLineToBeProcessed(String line) {
        if (Strings.isNullOrEmpty(line)) {
            return false;
        }
        for (String comment : COMMENT_MARKS) {
            if (line.trim().startsWith(comment)) {
                return false;
            }
        }
        //line needs to contain at least two elements
        String[] elements = line.split(separator);
        if (elements.length < 1) {
            return false;
        }

        return true;
    }
}
