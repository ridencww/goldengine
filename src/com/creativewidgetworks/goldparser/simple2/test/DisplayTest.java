package com.creativewidgetworks.goldparser.simple2.test;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.enums.SymbolType;
import com.creativewidgetworks.goldparser.parser.Variable;
import com.creativewidgetworks.goldparser.simple2.rulehandlers.Display;
import com.creativewidgetworks.goldparser.util.ConsoleDriver;
import com.creativewidgetworks.goldparser.util.ConsoleDriverForTests;
import com.creativewidgetworks.goldparser.util.GOLDParserTestCase;

public class DisplayTest extends GOLDParserTestCase {

    /*----------------------------------------------------------------------------*/
    /* Test object directly 
    /*----------------------------------------------------------------------------*/   

    @Test
    public void testDisplay_no_reductions() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        try {
            new Display(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Invalid state, stack empty.", pe.getMessage());
        }
    }    

    /*----------------------------------------------------------------------------*/

    @Test
    public void testDisplay_parameters() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        // Too few
        try {
            parser.makeReductionAndPush(
                makeToken("display", SymbolType.CONTENT, "display"));
            new Display(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 2 but got 1.", pe.getMessage());
        }
        try {
            parser.makeReductionAndPush(
                makeToken("display", SymbolType.CONTENT, "display"),
                makeToken("Expression", SymbolType.NON_TERMINAL, "i"),
                makeToken("read", SymbolType.CONTENT, "read"));
            new Display(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 4 but got 3.", pe.getMessage());
        }

        
        parser.clear();
        
        // Too many
        try {
            parser.makeReductionAndPush(
                makeToken("display", SymbolType.CONTENT, "display"),
                makeToken("Expression", SymbolType.NON_TERMINAL, "i"),
                makeToken("read", SymbolType.CONTENT, "read"),            
                makeToken("Id", SymbolType.CONTENT, "x"),
                makeToken("extra", SymbolType.CONTENT, "extra"));            
            new Display(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 4 but got 5.", pe.getMessage());
        }
        
        parser.clear();
        
        // Just right
        parser.makeReductionAndPush(
            makeToken("display", SymbolType.CONTENT, "display"),
            makeToken("Expression", SymbolType.NON_TERMINAL, "i"),
            makeToken("read", SymbolType.CONTENT, "read"),            
            makeToken("Id", SymbolType.CONTENT, "x"));
        new Display(parser);
    }    
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testDisplay_display() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();  
        
        parser.makeReductionAndPush(
            makeToken("display", SymbolType.CONTENT, "display"),
            makeToken("Expression", SymbolType.NON_TERMINAL, makeStringLiteralForTest("Test")));
        
        ConsoleDriver savedDriver = Display.ioDriver;
        ConsoleDriverForTests testDriver = new ConsoleDriverForTests();
        
        try {
            Display.ioDriver = testDriver;
            new Display(parser).execute();
            assertEquals("wrong text", "Test\r\n", testDriver.getDataWritten());
        } finally {
            Display.ioDriver = savedDriver;
        }
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testDisplay_display_and_read() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();  
        
        parser.makeReductionAndPush(
            makeToken("display", SymbolType.CONTENT, "display"),
            makeToken("Expression", SymbolType.NON_TERMINAL, makeStringLiteralForTest("Name: ")),
            makeToken("read", SymbolType.CONTENT, "read"),            
            makeToken("Id", SymbolType.CONTENT, "x")            
        );
        
        ConsoleDriver savedDriver = Display.ioDriver;
        ConsoleDriverForTests testDriver = new ConsoleDriverForTests();
        testDriver.addDataToRead("Ralph");
        
        try {
            Display.ioDriver = testDriver;
            new Display(parser).execute();
            assertEquals("wrong text", "Name: \r\n", testDriver.getDataWritten());
            assertEquals("bad variable value", "Ralph", parser.getProgramVariable("x").asString());
        } finally {
            Display.ioDriver = savedDriver;
        }
    }
    
    /*----------------------------------------------------------------------------*/
    /* Test object indirectly using the engine 
    /*----------------------------------------------------------------------------*/

    @Test
    public void testDisplay_parse_display() throws Exception {
        String[] actual = executeProgram("display \"Name: \"\r\n");
        validateLines(makeExpected("Name: "), actual);
    }
    
    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testDisplay_parse_display_and_read() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        String sourceCode = 
            "display \"Name: \" read i\r\n" +
            "display i\r\n";

        ConsoleDriverForTests testDriver = new ConsoleDriverForTests();
        testDriver.addDataToReadWithEOLN("Ralph");
        
        String[] actual = executeProgram(parser, sourceCode, testDriver);
        validateLines(makeExpected("Name: ", "Ralph"), actual);
        
        Variable var = parser.getProgramVariable("i");
        assertNotNull("should have found variable \"i\"", var);
        assertTrue("wrong type",  var.asObject() instanceof String);
        assertEquals("wrong value", "Ralph", var.asString());
    }

}
