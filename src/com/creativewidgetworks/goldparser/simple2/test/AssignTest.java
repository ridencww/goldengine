package com.creativewidgetworks.goldparser.simple2.test;

import java.math.BigDecimal;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Token;
import com.creativewidgetworks.goldparser.parser.Variable;
import com.creativewidgetworks.goldparser.simple2.rulehandlers.Assign;
import com.creativewidgetworks.goldparser.util.GOLDParserTestCase;

public class AssignTest extends GOLDParserTestCase {

    /*----------------------------------------------------------------------------*/
    /* Test object directly 
    /*----------------------------------------------------------------------------*/   

    @Test
    public void testAssign_no_reductions() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        try {
            new Assign(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Invalid state, stack empty.", pe.getMessage());
        }
    }    

    /*----------------------------------------------------------------------------*/

    @Test
    public void testAssign_parameters() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        // Too few
        try {
            parser.makeReductionAndPush(new Token("X"), new Token("="));
            new Assign(parser).execute();
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 4 but got 2.", pe.getMessage());
        }

        parser.clear();
        
        // Too many
        try {
            parser.makeReductionAndPush(new Token("Assign"), new Token("X"), new Token("="), new Token("1"), new Token("*"));
            new Assign(parser).execute();
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 4 but got 5.", pe.getMessage());
        }
        
        parser.clear();
        
        // Just right
        parser.makeReductionAndPush(
            new Token("Assign"), 
            new Token("X"), 
            new Token("="), 
            new Token(makeNumberLiteralForTest("1")));
        new Assign(parser).execute();
    }    
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testAssign_numeric_value() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        String TEST_KEY = "i";
        String TEST_VALUE = "123";

        Variable var = parser.getProgramVariable("i");
        assertNull("should not find variable '" + TEST_KEY + "'", var);
        
        parser.makeReductionAndPush(
            new Token("Assign"),
            new Token(TEST_KEY),
            new Token("="),
            new Token(makeNumberLiteralForTest(TEST_VALUE)));        

        new Assign(parser).execute();

        var = parser.getProgramVariable(TEST_KEY);
        assertNotNull("should find variable '" + TEST_KEY + "'", var);
        assertTrue("wrong type", var.asObject() instanceof BigDecimal);
        assertEquals("wrong value", new BigDecimal(TEST_VALUE), var.asNumber());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testAssign_string_value() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        String TEST_KEY = "i";
        String TEST_VALUE = "'The rain in Spain...'";

        Variable var = parser.getProgramVariable("i");
        assertNull("should not find variable '" + TEST_KEY + "'", var);
        
        parser.makeReductionAndPush(
            new Token("Assign"),
            new Token(TEST_KEY),
            new Token("="),
            new Token(makeStringLiteralForTest(TEST_VALUE)));        

        new Assign(parser).execute();

        var = parser.getProgramVariable(TEST_KEY);
        assertNotNull("should find variable " + TEST_KEY, var);
        assertTrue("wrong type", var.asObject() instanceof String);
        assertEquals("wrong value", TEST_VALUE, var.asString());
    }

    /*----------------------------------------------------------------------------*/
    /* Test object indirectly using the engine 
    /*----------------------------------------------------------------------------*/

    @Test
    public void testAssign_number() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        String sourceCode =
            "assign i = 5\r\n" +
            "display i\r\n" +
            "assign i = i + 4\r\n" +
            "display i\r\n" +
            "\r\n";

        String[] actual = executeProgram(parser, sourceCode);
        validateLines(makeExpected("5", "9"), actual);

        Variable var = parser.getProgramVariable("i");
        assertNotNull("should have found variable \"i\"", var);
        assertTrue("wrong type",  var.asObject() instanceof BigDecimal);
        assertEquals("wrong value", 9, var.asInt());
    }

    /*----------------------------------------------------------------------------*/

    @Test    
    public void testAssign_self() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        String sourceCode =
            "assign i = 7\r\n" +
            "display i\r\n" +
            "assign i = i\r\n" +
            "display i\r\n" +
            "\r\n";

        String[] actual = executeProgram(parser, sourceCode);
        validateLines(makeExpected("7", "7"), actual);

        Variable var = parser.getProgramVariable("i");
        assertNotNull("should have found variable \"i\"", var);
        assertTrue("wrong type",  var.asObject() instanceof BigDecimal);
        assertEquals("wrong value", 7, var.asInt());

        parser.setProgramVariable("i", new Variable(new BigDecimal("5")));
        
        sourceCode =
            "display i\r\n" +
            "assign i = i\r\n" +
            "display i\r\n" +
            "\r\n";

        actual = executeProgram(parser, sourceCode);
        validateLines(makeExpected("5", "5"), actual);
        
        var = parser.getProgramVariable("i");
        assertNotNull("should have found variable \"i\"", var);
        assertTrue("wrong type",  var.asObject() instanceof BigDecimal);
        assertEquals("wrong value", 5, var.asInt());
    }

}
