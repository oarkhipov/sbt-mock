package ru.sbt.bpm.mock.spring.utils;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Created by sbt-bochev-as on 17.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */
@AllArgsConstructor
public class AjaxObject {
    private transient Gson gson = new Gson();
    @Getter private String info;
    @Getter private String error;
    @Getter private String data;

    public AjaxObject() {
        this.info = "";
        this.error = "";
        this.data = "";
    }

    public AjaxObject(String info, Exception e, String data) {
        this.info = info;
        setErrorFromException(e);
        this.data = data;
    }

    public void setInfo(String info) {
        this.info = fixNewLine(info);
    }

    public void setError(String error) {
        this.error = fixNewLine(error);
    }

    public void setData(String data) {
        this.data = StringEscapeUtils.escapeHtml(StringEscapeUtils.unescapeJava(fixNewLine(data)));
    }

    private String fixNewLine(String text) {
        return text.replace("\r", "\\r").replace("\n", "\\n");

    }
    public String toJSON() {
        return gson.toJson(this);
    }

    public void setErrorFromException(Exception exception) {
        final String exceptionStackTrace = ExceptionUtils.getExceptionStackTrace(exception);
        String[] splittedExceptions = exceptionStackTrace.split("\n");
        splittedExceptions = Arrays.copyOf(splittedExceptions, 10);
        final String joinedExceptions = StringUtils.join(splittedExceptions, "<br/>");
        setError(joinedExceptions);
    }

}
