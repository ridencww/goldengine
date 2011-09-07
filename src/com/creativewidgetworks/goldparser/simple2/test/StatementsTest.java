package com.creativewidgetworks.goldparser.simple2.test;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;
import com.creativewidgetworks.goldparser.parser.Variable;
import com.creativewidgetworks.goldparser.simple2.rulehandlers.Id;
import com.creativewidgetworks.goldparser.simple2.rulehandlers.Statements;
import com.creativewidgetworks.goldparser.util.GOLDParserTestCase;

public class StatementsTest extends GOLDParserTestCase {

    /*----------------------------------------------------------------------------*/
    /* Test object directly 
    /*----------------------------------------------------------------------------*/   

    @Test
    public void testStatements_no_reductions() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        try {
            new Statements(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Invalid state, stack empty.", pe.getMessage());
        }
    }    

    /*----------------------------------------------------------------------------*/

    @Test
    public void testStatements_params() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        
        // Too few
        try {
            parser.makeReductionAndPush();
            new Statements(parser).execute();
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 1..2 but got 0.", pe.getMessage());
        }
        
        parser.clear();
        
        // Too many
        try {
            parser.makeReductionAndPush(new Token("dummy1"), new Token("dummy2"), new Token("dummy3"));
            new Statements(parser).execute();
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 1..2 but got 3.", pe.getMessage());
        }
        
        parser.clear();
        
        // Just right
        parser.makeReductionAndPush(new Token(makeReduction()));
        new Statements(parser).execute();
        parser.clear();
        parser.makeReductionAndPush(new Token(makeReduction()), new Token(makeReduction()));
        new Statements(parser).execute();
    }    

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testId_parameters() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        // Too few
        try {
            parser.makeReductionAndPush();
            new Id(parser).execute();
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 1 but got 0.", pe.getMessage());
        }

        parser.clear();
        
        // Too many
        try {
            parser.makeReductionAndPush(new Token("X"), new Token("="));
            new Id(parser).execute();
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 1 but got 2.", pe.getMessage());
        }
        
        parser.clear();
        
        // Just right
        parser.makeReductionAndPush(new Token("X"));
        new Id(parser).execute();
    }    

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testId() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        
        parser.setProgramVariable("V1", new Variable("abc"));
        parser.makeReductionAndPush(new Token("V1"));

        Reduction reduction = new Id(parser);  
        reduction.execute();
        assertEquals("wrong value", "abc", reduction.getValue().asString());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testId_missing_variable() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        
        parser.makeReductionAndPush(new Token("V1"));
        
        Reduction reduction = new Id(parser);  
        reduction.execute();
        assertEquals("wrong value", "", reduction.getValue().asString());
    }

    /*----------------------------------------------------------------------------*/
    /* Test object indirectly using the engine 
    /*----------------------------------------------------------------------------*/

    @Test 
    public void testId_parse() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        parser.setProgramVariable("V1", new Variable("abc"));
        
        String sourceCode =
            "display V1\r\n" +
            "\r\n";

        String[] actual = executeProgram(parser, sourceCode);
        validateLines(makeExpected("abc"), actual);
    }
    
}
