package ru.sbt.bpm.mock.spring.utils;

import org.w3c.dom.ls.LSInput;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by sbt-vostrikov-mi on 06.04.2015.
 */
public class Input implements LSInput {

    private String publicId;

    private String systemId;

    private String baseURI;

    private InputStream byteStream;

    private boolean certifiedText;

    private Reader characterStream;

    private String encoding;

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getBaseURI() {
        return baseURI;
    }

    public InputStream getByteStream() {
//        return byteStream;
        synchronized (inputStream) {
            try {
                byte[] input = new byte[inputStream.available()];
                inputStream.read(input);
                String contents = new String(input);
                return new ByteArrayInputStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Exception " + e);
                return null;
            }
        }
    }

    public boolean getCertifiedText() {
        return certifiedText;
    }

    public Reader getCharacterStream() {
        return characterStream;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getStringData() {
        synchronized (inputStream) {
            try {
                byte[] input = new byte[inputStream.available()];
                inputStream.read(input);
                String contents = new String(input);
                return contents;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Exception " + e);
                return null;
            }
        }
    }

    public void setBaseURI(String baseURI) {
        this.baseURI = baseURI;
    }

    public void setByteStream(InputStream byteStream) {
        this.byteStream = byteStream;
    }

    public void setCertifiedText(boolean certifiedText) {
        this.certifiedText = certifiedText;
    }

    public void setCharacterStream(Reader characterStream) {
        this.characterStream = characterStream;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setStringData(String stringData) {
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public BufferedInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(BufferedInputStream inputStream) {
        this.inputStream = inputStream;
    }

    private BufferedInputStream inputStream;

    public Input(String publicId, String sysId, InputStream input) {
        this.publicId = publicId;
        this.systemId = sysId;
        this.encoding = "UTF-8";
//        this.byteStream = new BufferedInputStream(input);
//        this.byteStream = new BufferedInputStream(input); //TODO до конца не понял принцип работы - надо гуглить. но если расокментить поля кроме inputStream, то работаеть не будет.
        this.inputStream = new BufferedInputStream(input);
//        this.byteStream = new BufferedInputStream(input);
        certifiedText = false;
//        this.characterStream = new InputStreamReader(input);
    }

    public Input(String publicId, String sysId, InputStream input, String baseURI) {
        this(publicId, sysId, input);
        this.baseURI = baseURI;
    }

    public Input(String publicId, String sysId, InputStream input, String baseURI, String encoding) {
        this(publicId, sysId, input, baseURI);
        this.encoding = encoding;
    }

}
