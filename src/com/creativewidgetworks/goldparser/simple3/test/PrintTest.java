package com.creativewidgetworks.goldparser.simple3.test;

import java.math.BigDecimal;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.enums.SymbolType;
import com.creativewidgetworks.goldparser.parser.Variable;
import com.creativewidgetworks.goldparser.simple3.rulehandlers.Print;
import com.creativewidgetworks.goldparser.util.ConsoleDriver;
import com.creativewidgetworks.goldparser.util.ConsoleDriverForTests;

public class PrintTest extends GOLDParserTestCase {

    /*----------------------------------------------------------------------------*/
    /* Test object directly 
    /*----------------------------------------------------------------------------*/   

    @Test
    public void testPrint_no_reductions() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        try {
            new Print(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Invalid state, stack empty.", pe.getMessage());
        }
    }    

    /*----------------------------------------------------------------------------*/

    @Test
    public void testPrint_parameters() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        // Too few
        try {
            parser.makeReductionAndPush(
                makeToken("display", SymbolType.CONTENT, "display"));
            new Print(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 2 but got 1.", pe.getMessage());
        }
        try {
            parser.makeReductionAndPush(
                makeToken("display", SymbolType.CONTENT, "display"),
                makeToken("Expression", SymbolType.NON_TERMINAL, "i"),
                makeToken("read", SymbolType.CONTENT, "read"));
            new Print(parser);
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
            new Print(parser);
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
        new Print(parser);
    }    
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testPrint_display() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();  
        
        parser.makeReductionAndPush(
            makeToken("display", SymbolType.CONTENT, "display"),
            makeToken("Expression", SymbolType.NON_TERMINAL, makeStringLiteralForTest("Test")));
        
        ConsoleDriver savedDriver = Print.ioDriver;
        ConsoleDriverForTests testDriver = new ConsoleDriverForTests();
        
        try {
            Print.ioDriver = testDriver;
            new Print(parser).execute();
            assertEquals("wrong text", "Test\r\n", testDriver.getDataWritten());
        } finally {
            Print.ioDriver = savedDriver;
        }
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testPrint_display_and_read() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();  
        
        parser.makeReductionAndPush(
            makeToken("display", SymbolType.CONTENT, "display"),
            makeToken("Expression", SymbolType.NON_TERMINAL, makeStringLiteralForTest("Name: ")),
            makeToken("read", SymbolType.CONTENT, "read"),            
            makeToken("Id", SymbolType.CONTENT, "x")            
        );
        
        ConsoleDriver savedDriver = Print.ioDriver;
        ConsoleDriverForTests testDriver = new ConsoleDriverForTests();
        testDriver.addDataToRead("Ralph");
        
        try {
            Print.ioDriver = testDriver;
            new Print(parser).execute();
            assertEquals("wrong text", "Name: ", testDriver.getDataWritten());
            assertEquals("bad variable value", "Ralph", parser.getProgramVariable("x").asString());
        } finally {
            Print.ioDriver = savedDriver;
        }
    }
    
    /*----------------------------------------------------------------------------*/
    /* Test object indirectly using the engine 
    /*----------------------------------------------------------------------------*/

    @Test
    public void testPrint_parse_display() throws Exception {
        String[] actual = executeProgram("print \"Name: \"\r\n");
        validateLines(makeExpected("Name: "), actual);
    }
    
    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testPrint_parse_display_and_read() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        String sourceCode = 
            "print \"Name: \" read i\r\n" +
            "print 'Your name is ' + i\r\n";

        // Note that ConsoleDriverForTests does not echo the read data
        // and the test must take that into account.
        ConsoleDriverForTests testDriver = new ConsoleDriverForTests();
        testDriver.addDataToReadWithEOLN("Ralph");

        String[] actual = executeProgram(parser, sourceCode, testDriver);
        validateLines(makeExpected("Name: Your name is Ralph"), actual);
        
        Variable var = parser.getProgramVariable("i");
        assertNotNull("should have found variable \"i\"", var);
        assertTrue("wrong type",  var.asObject() instanceof String);
        assertEquals("wrong value", "Ralph", var.asString());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testPrint_parenthesis() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        parser.setProgramVariable("miles", new Variable(new BigDecimal("3")));
        
        String sourceCode = "" +
        		"miles = 3\r\n" +
        		"print 'Result = ' + (miles * 7)\r\n";
        
        ConsoleDriverForTests testDriver = new ConsoleDriverForTests();
        
        String[] actual = executeProgram(parser, sourceCode, testDriver);
        validateLines(makeExpected("Result = 21"), actual);
    }
    
}
