package ru.sbt.bpm.mock.utils;

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
        this.info = info;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setData(String data) {
        this.data = data;
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
}
