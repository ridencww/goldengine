package com.creativewidgetworks.goldparser.simple3.test;

import java.math.BigDecimal;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;
import com.creativewidgetworks.goldparser.parser.Variable;
import com.creativewidgetworks.goldparser.simple3.rulehandlers.Negation;

public class NegationTest extends GOLDParserTestCase {

    /*----------------------------------------------------------------------------*/
    /* Test object directly 
    /*----------------------------------------------------------------------------*/   

    @Test
    public void testNegation_no_reductions() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        try {
            new Negation(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Invalid state, stack empty.", pe.getMessage());
        }
    }    

    /*----------------------------------------------------------------------------*/

    @Test
    public void testNegation_parameters() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        // Too few
        try {
            parser.makeReductionAndPush();
            new Negation(parser).execute();
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 2 but got 0.", pe.getMessage());
        }

        parser.clear();
        
        // Too many
        try {
            parser.makeReductionAndPush(new Token("-"), new Token("1"), new Token("X"));
            new Negation(parser).execute();
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 2 but got 3.", pe.getMessage());
        }
        
        parser.clear();
        
        // Just right
        parser.makeReductionAndPush(new Token("-"), new Token(makeNumberLiteralForTest("1")));
        new Negation(parser).execute();
    }    

    /*----------------------------------------------------------------------------*/

    @Test
    public void testNegation_non_numeric() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        
        try {
            parser.makeReductionAndPush(new Token("-"), new Token(makeNumberLiteralForTest("bad_value")));
            Reduction reduction = new Negation(parser);
            reduction.execute();
            reduction.getValue();            
            fail("ParserException expected");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Token is not a number: bad_value", pe.getMessage());
        }

        parser.clear();
        
        Reduction reduction = null;
        try {
            parser.setProgramVariable("V1", new Variable("bad_value"));
            parser.makeReductionAndPush(new Token("-"), new Token(makeStringLiteralForTest("V1")));
            reduction = new Negation(parser);
            reduction.execute();
            reduction.getValue();
            fail("ParserException expected");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Negate expected numeric value to process.", pe.getMessage());
        }
        
        try {
            reduction.getValue();
            fail("ParserException expected");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Negate expected numeric value to process.", pe.getMessage());
        }
    }
    
    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testNegation() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        
        parser.makeReductionAndPush(new Token("-"), new Token(makeNumberLiteralForTest("1")));
        Reduction reduction = new Negation(parser);
        reduction.execute();
        assertEquals("wrong value", new BigDecimal("-1"), reduction.getValue().asNumber());
    }

    /*----------------------------------------------------------------------------*/
    /* Test object indirectly using the engine 
    /*----------------------------------------------------------------------------*/

    @Test 
    public void testNegation_parse() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        
        String sourceCode =
            "V1 = 1\r\n" +
            "print - V1\r\n" +
            "\r\n";

        String[] actual = executeProgram(parser, sourceCode);
        validateLines(makeExpected("-1"), actual);
    }
    
}
