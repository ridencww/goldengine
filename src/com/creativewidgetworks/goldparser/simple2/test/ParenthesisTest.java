package com.creativewidgetworks.goldparser.simple2.test;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Token;
import com.creativewidgetworks.goldparser.simple2.rulehandlers.Parenthesis;
import com.creativewidgetworks.goldparser.util.GOLDParserTestCase;

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
            "assign i = 4 / (3 + 1)\r\n" + 
            "display i\r\n";
        
        String[] actual = executeProgram(sourceCode);
        validateLines(makeExpected("1"), actual);
    }
    
}
