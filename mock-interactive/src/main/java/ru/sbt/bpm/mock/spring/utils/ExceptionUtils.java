package ru.sbt.bpm.mock.spring.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by sbt-vostrikov-mi on 18.02.2016.
 */
public class ExceptionUtils {
    public static String getExceptionStackTrace(Exception exception) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        exception.printStackTrace(printWriter);
        return writer.toString();
    }
}
