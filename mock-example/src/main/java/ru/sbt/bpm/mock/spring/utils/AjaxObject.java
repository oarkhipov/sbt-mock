package ru.sbt.bpm.mock.spring.utils;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

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
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        exception.printStackTrace(printWriter);
        setError(writer.toString());
    }
}
