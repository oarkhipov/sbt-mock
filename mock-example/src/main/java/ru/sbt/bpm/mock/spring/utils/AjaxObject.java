package ru.sbt.bpm.mock.spring.utils;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Created by sbt-bochev-as on 17.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */
public class AjaxObject {
    private String info;
    private String error;
    private String data;

    public AjaxObject() {
        this.info = "";
        this.error = "";
        this.data = "";
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

    public String getInfo() {

        return info;
    }

    public String getError() {
        return error;
    }

    public String getData() {
        return data;
    }

    private String fixNewLine(String text) {
        return text.replace("\r", "\\r").replace("\n", "\\n");

    }
}
