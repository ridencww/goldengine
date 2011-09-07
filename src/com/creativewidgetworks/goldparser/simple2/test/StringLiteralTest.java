package com.creativewidgetworks.goldparser.simple2.test;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.Token;
import com.creativewidgetworks.goldparser.parser.Variable;
import com.creativewidgetworks.goldparser.simple2.rulehandlers.StringLiteral;
import com.creativewidgetworks.goldparser.util.GOLDParserTestCase;

public class StringLiteralTest extends GOLDParserTestCase {
    
    /*----------------------------------------------------------------------------*/
    /* Test object directly 
    /*----------------------------------------------------------------------------*/    
    
    @Test
    public void testStringLiteral_no_reductions() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        StringLiteral r = new StringLiteral(parser);
        Variable value = r.getValue();
        assertNotNull("should have value", value);
        assertTrue("wrong type", value.asObject() instanceof String);
        assertEquals("wrong value", "", value.asString());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testStringLiteral() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        
        String TEST_VALUE = "Hello, world";
        parser.makeReductionAndPush(new Token("\"" + TEST_VALUE + "\""));
        
        StringLiteral r = new StringLiteral(parser);
        Variable value = r.getValue();
        assertNotNull("should have value", value);
        assertTrue("wrong type", value.asObject() instanceof String);
        assertEquals("wrong value", TEST_VALUE, value.asString());
        
        parser.clear();
        parser.makeReductionAndPush(new Token("'" + TEST_VALUE + "'"));
        
        r = new StringLiteral(parser);
        value = r.getValue();
        assertNotNull("should have value", value);
        assertTrue("wrong type", value.asObject() instanceof String);
        assertEquals("wrong value", TEST_VALUE, value.asString());
    }

    /*----------------------------------------------------------------------------*/
    /* Test object indirectly using the engine 
    /*----------------------------------------------------------------------------*/

    @Test
    public void testStringLiteral_matched_quotes() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        String sourceCode1 =
            "assign i = 'Hello, world'\r\n" +
            "display i\r\n" +
            "\r\n";

        String sourceCode2 =
            "assign i = \"Hello, test\"\r\n" +
            "display i\r\n" +
            "\r\n";


        String[] actual = executeProgram(parser, sourceCode1);
        validateLines(makeExpected("Hello, world"), actual);
        Variable var = parser.getProgramVariable("i");
        assertNotNull("should have found variable \"i\"", var);
        assertTrue("wrong type", var.asObject() instanceof String);
        assertEquals("wrong value", "Hello, world", var.asString());

        actual = executeProgram(parser, sourceCode2);
        validateLines(makeExpected("Hello, test"), actual);
        var = parser.getProgramVariable("i");
        assertNotNull("should have found variable \"i\"", var);
        assertTrue("wrong type", var.asObject() instanceof String);
        assertEquals("wrong value", "Hello, test", var.asString());
    }
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testStringLiteral_mismatched_quotes() throws Exception {
        String sourceCode1 =
            "assign i = 'Hello, world\"\r\n" +
            "display i\r\n" +
            "\r\n";

        String sourceCode2 =
            "assign i = \"Hello, test'\r\n" +
            "display i\r\n" +
            "\r\n";

        // basic reporting
        String[] actual = executeProgram(sourceCode1);
        validateLines(makeExpected("Lexical error at line 1, column 6. Read (Error)"), actual);
        actual = executeProgram(sourceCode2);
        validateLines(makeExpected("Lexical error at line 1, column 6. Read (Error)"), actual);
    }

}
