package com.creativewidgetworks.goldparser.parser.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.parser.SystemConsole;
import com.creativewidgetworks.goldparser.util.ConsoleDriver;

public class SystemConsoleTest extends TestCase {

    @Test
    public void testRead() throws Exception {
        InputStream saveIn = System.in;
        
        ByteArrayInputStream bais = new ByteArrayInputStream("Hello, world.\r".getBytes("utf-8"));
        
        // Intercept stdout and capture the data to verify it is being sent there
        String data = null;
        try {
            // Hook in our pre-loaded input stream
            System.setIn(bais);
            ConsoleDriver driver = new SystemConsole();
            data = driver.read();
        } finally {
            System.setIn(saveIn);
        }
        
        assertNotNull("should have data", data);
        assertEquals("wrong data", "Hello, world.", data);
    }
    
    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testWrite() throws Exception {
        PrintStream saveErr = System.err;
        PrintStream saveOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        // Intercept stdout and capture the data to verify it is being sent there
        PrintStream ps = null;
        try {
            // Capture program output
            ps = new PrintStream(baos);
            System.setOut(ps);
            
            ConsoleDriver driver = new SystemConsole();
            driver.write("The rain in Spain ");
            driver.write("falls mainly on the plain.");
        } finally {
            if (ps != null) {
                ps.close();
            }
            System.setOut(saveErr);
            System.setOut(saveOut);
        }
        
        String text = new String(baos.toString());
        assertEquals("wrong data", "The rain in Spain falls mainly on the plain.", text);
    }

}
