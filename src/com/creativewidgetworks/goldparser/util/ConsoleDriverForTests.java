package com.creativewidgetworks.goldparser.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ConsoleDriverForTests implements ConsoleDriver {

    private int index;
    private List<String> dataToInput = new ArrayList<String>();

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    /*----------------------------------------------------------------------------*/
    // These are the methods that implement the ConsoleDriver interface
    /*----------------------------------------------------------------------------*/
    
    public String read() {
        String data = null;
        if (index < dataToInput.size()) {
            data = dataToInput.get(index++);
        }
        return data;
    }

    public void write(String data) {
        try {
            baos.write(data.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            // ignore
        } catch (IOException e) {
            // ignore
        }
    }

    /*----------------------------------------------------------------------------*/

    public void addDataToRead(String data) {
        dataToInput.add(data);
    }

    public void addDataToReadWithEOLN(String data) {
        dataToInput.add(data + "\r\n");
    }
    
    public void clearDataToRead() {
        index = 0;
        dataToInput.clear();
    }
    
    public String getDataWritten() {
        String data = baos.toString();
        baos.reset();
        return data;
    }
    
    /*----------------------------------------------------------------------------*/

    public InputStream getInputStream() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String line : dataToInput) {
            sb.append(line);
        }
        return new ByteArrayInputStream(sb.toString().getBytes("utf-8"));
    }
    
    public PrintStream getPrintStream() {
        return new PrintStream(baos);
    }
}
