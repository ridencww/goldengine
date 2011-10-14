package com.creativewidgetworks.goldparser.simple3.test;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Token;
import com.creativewidgetworks.goldparser.simple3.rulehandlers.Parenthesis;

public class ParenthesisTest extends GOLDParserTestCase {

    /*----------------------------------------------------------------------------*/
    /* Test object directly 
    /*----------------------------------------------------------------------------*/   

    @Test
    public void testParenthesis_no_reductions() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        try {
            new Parenthesis(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Invalid state, stack empty.", pe.getMessage());
        }
    }    

    /*----------------------------------------------------------------------------*/

    @Test
    public void testParenthesis_parameters() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        // Too few
        try {
            parser.makeReductionAndPush();
            new Parenthesis(parser).execute();
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 3 but got 0.", pe.getMessage());
        }

        parser.clear();
        
        // Too many
        try {
            parser.makeReductionAndPush(new Token("("), new Token("1"), new Token(")"), new Token());
            new Parenthesis(parser).execute();
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 3 but got 4.", pe.getMessage());
        }
        
        parser.clear();
        
        // Just right
        parser.makeReductionAndPush(new Token("("), new Token(makeNumberLiteralForTest("1")), new Token(")"));
        new Parenthesis(parser).execute();
    }    

    /*----------------------------------------------------------------------------*/
    /* Test object indirectly using the engine 
    /*----------------------------------------------------------------------------*/

    @Test 
    public void testParenthesis() throws Exception {
        String sourceCode = 
            "i = 4 / (3 + 1)\r\n" + 
            "print i\r\n";
        
        String[] actual = executeProgram(sourceCode);
        validateLines(makeExpected("1"), actual);
    }
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testParenthesis_logical_in_parens_with_variables() throws Exception {
        String sourceCode = 
            "s1 = 'alpha'\r\n" +
            "s2 = 'beta'\r\n" +
            "print s1 < s2\r\n" + 
            "print (s1 < s2)\r\n";
        
        String[] actual = executeProgram(sourceCode);
        validateLines(makeExpected("true", "true"), actual);        
    }
}
