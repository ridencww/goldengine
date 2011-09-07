package com.creativewidgetworks.goldparser.simple3.test;

import java.math.BigDecimal;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Token;
import com.creativewidgetworks.goldparser.parser.Variable;
import com.creativewidgetworks.goldparser.simple3.rulehandlers.NumberLiteral;

public class NumberLiteralTest extends GOLDParserTestCase {
    
    /*----------------------------------------------------------------------------*/
    /* Test object directly 
    /*----------------------------------------------------------------------------*/    
    
    @Test
    public void testNumberLiteral_no_reductions() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        try {
            new NumberLiteral(parser);
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Token is not a number: <null>", pe.getMessage());
        }
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testNumberLiteral() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        Variable value;
        
        parser.makeReductionAndPush(new Token("1"));
        value = new NumberLiteral(parser).getValue();
        assertNotNull("should have value", value);
        assertTrue("wrong type", value.asObject() instanceof BigDecimal);
        assertEquals("wrong value", 1, value.asInt());
        assertEquals("wrong value", BigDecimal.ONE, value.asNumber());
        
        parser.clear();
        
        parser.makeReductionAndPush(new Token("1.234"));
        value = new NumberLiteral(parser).getValue();
        assertNotNull("should have value", value);
        assertTrue("wrong type", value.asObject() instanceof BigDecimal);
        assertEquals("wrong value", 1, value.asInt());
        assertEquals("wrong value", new BigDecimal("1.234"), value.asNumber());

        parser.clear();
        
        parser.makeReductionAndPush(new Token("-1.234"));
        value = new NumberLiteral(parser).getValue();
        assertNotNull("should have value", value);
        assertTrue("wrong type", value.asObject() instanceof BigDecimal);
        assertEquals("wrong value", -1, value.asInt());
        assertEquals("wrong value", new BigDecimal("-1.234"), value.asNumber());
        
        parser.clear();
        
        parser.makeReductionAndPush(new Token("NAN"));
        try {
            value = new NumberLiteral(parser).getValue();
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Token is not a number: NAN", pe.getMessage());
        }
    }

    /*----------------------------------------------------------------------------*/
    /* Test object indirectly using the engine 
    /*----------------------------------------------------------------------------*/

    @Test
    public void testNumberLiteral_valid() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        String sourceCode1 =
            "i = 1.234\r\n" +
            "print i\r\n" +
            "\r\n";

        String sourceCode2 =
            "i = -1.234\r\n" +
            "print i\r\n" +
            "\r\n";

        String sourceCode3 =
            "i = 1..2\r\n" +
            "print i\r\n" +
            "\r\n";

        String[] actual = executeProgram(parser, sourceCode1);
        validateLines(makeExpected("1.234"), actual);
        Variable var = parser.getProgramVariable("i");
        assertNotNull("should have found variable \"i\"", var);
        assertTrue("wrong type", var.asObject() instanceof BigDecimal);
        assertEquals("wrong value", new BigDecimal("1.234"), var.asNumber());

        actual = executeProgram(parser, sourceCode2);
        validateLines(makeExpected("-1.234"), actual);
        var = parser.getProgramVariable("i");
        assertNotNull("should have found variable \"i\"", var);
        assertTrue("wrong type", var.asObject() instanceof BigDecimal);
        assertEquals("wrong value", new BigDecimal("-1.234"), var.asNumber());

        // Not a number
        actual = executeProgram(parser, sourceCode3);
        validateLines(makeExpected("Lexical error at line 1, column 1. Read (Error)"), actual);
    }

}
