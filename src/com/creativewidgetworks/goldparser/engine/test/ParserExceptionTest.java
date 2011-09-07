package com.creativewidgetworks.goldparser.engine.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.ParserException;

public class ParserExceptionTest {

    @Test
    public void testParserException() {
        ParserException pe = new ParserException();
        assertEquals("wrong msg", null, pe.getMessage());
        assertEquals("wrong cause", null, pe.getCause());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testParserExceptionThrowable() {
        NullPointerException npe = new NullPointerException("Whoops");
        ParserException pe = new ParserException(npe);
        assertEquals("wrong msg", "java.lang.NullPointerException: Whoops", pe.getMessage());
        assertEquals("wrong cause", npe, pe.getCause());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testParserExceptionString() {
        // string literal message
        ParserException pe = new ParserException("something bad happened");
        assertEquals("wrong msg", "something bad happened", pe.getMessage());
        assertEquals("wrong cause", null, pe.getCause());
        
        // message from resource
        pe = new ParserException("error.cgt_missing");
        assertEquals("wrong msg", "A grammar file must be specified.", pe.getMessage());
        assertEquals("wrong cause", null, pe.getCause());        
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testParserExceptionStringThrowable() {
        NullPointerException npe = new NullPointerException("Whoops");
        
        // string literal message
        ParserException pe = new ParserException("something bad happened", npe);
        assertEquals("wrong msg", "something bad happened", pe.getMessage());
        assertEquals("wrong cause", npe, pe.getCause());
        
        // message from resource
        pe = new ParserException("error.cgt_missing", npe);
        assertEquals("wrong msg", "A grammar file must be specified.", pe.getMessage());
        assertEquals("wrong cause", npe, pe.getCause());        
    }

}
