package com.creativewidgetworks.goldparser.simple3.test;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.ParserException;

/**
 * Tests cover Argument, Parameters, Function, and FunctionCall classes
 * @author riden
 *
 */
public class FunctionCallTest extends GOLDParserTestCase {

    @Test
    public void testCallToUndefinedFunction() throws Exception {
        String sourceCode =
            "print NoSuchFunction()\r\n" +
            "\r\n";

        String[] actual = executeProgram(sourceCode);
        validateLines(makeExpected(""), actual);
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testFunction_no_parms() throws Exception {
        String sourceCode =
            "function MyFunction() begin\r\n" +
            "  return 3.14159\r\n" +
            "end\r\n" +
            "print MyFunction()\r\n" +
            "\r\n";
        
        String[] actual = executeProgram(sourceCode);
        validateLines(makeExpected("3.14159"), actual);
    }
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testFunction_single_parm() throws Exception {
        String sourceCode =
            "function MyFunction(a) begin\r\n" +
            "  return a * 4\r\n" +
            "end\r\n" +
            "print MyFunction(3)\r\n" +
            "\r\n";

        String[] actual = executeProgram(sourceCode);
        validateLines(makeExpected("12"), actual);
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testFunction_single_parm_with_expression() throws Exception {
        String sourceCode =
            "function MyFunction(a) begin\r\n" +
            "  return a * 7 * 4\r\n" +
            "end\r\n" +
            "print MyFunction(3 + 5 - (3 + 2))\r\n" +
            "\r\n";

        String[] actual = executeProgram(sourceCode);
        validateLines(makeExpected("84"), actual);
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testFunction_single_parm_with_function_expression() throws Exception {
        String sourceCode =
            "function MyFunction(a) begin\r\n" +
            "  return a * 7 * 4\r\n" +
            "end\r\n" +
            "print MyFunction(MyFunction(1))\r\n" +
            "\r\n";

        String[] actual = executeProgram(sourceCode);
        validateLines(makeExpected("784"), actual);
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testFunction_multi_parms() throws Exception {
        String sourceCode =
            "function MyFunction(a, b) begin\r\n" +
            "  return a * b * 4\r\n" +
            "end\r\n" +
            "print MyFunction(3, 7)\r\n" +
            "\r\n";

        String[] actual = executeProgram(sourceCode);
        validateLines(makeExpected("84"), actual);
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testFunction_multi_parms_external_vars_available() throws Exception {
        String sourceCode =
            "b = 22\r\n" +
            "function MyFunction(a) begin\r\n" +
            "  return a * b * 4\r\n" +
            "end\r\n" +
            "print MyFunction(3)\r\n" +
            "\r\n";

        String[] actual = executeProgram(sourceCode);
        validateLines(makeExpected("264"), actual);
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testFunction_multi_parms_external_vars_with_same_name_untouched() throws Exception {
        String sourceCode =
            "a = 99\r\n" +
            "b = 22\r\n" +
            "function MyFunction(a, b) begin\r\n" +
            "  return a * b * 4\r\n" +
            "end\r\n" +
            "print MyFunction(3, 7)\r\n" +
            "print b\r\n" +
            "print a\r\n" +
            "\r\n";

        String[] actual = executeProgram(sourceCode);
        validateLines(makeExpected("84", "22", "99"), actual);
    }
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testFunction_multi_parms_with_expression() throws Exception {
        String sourceCode =
            "function MyFunction(a, b) begin\r\n" +
            "  return a * b * 4\r\n" +
            "end\r\n" +
            "print MyFunction(3 + 5 - (3 + 2), 7 * 2 / 2)\r\n" +
            "\r\n";

        String[] actual = executeProgram(sourceCode);
        validateLines(makeExpected("84"), actual);
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testFunction_multi_parms_with_function_expression() throws Exception {
        String sourceCode =
            "function MyFunction(a, b) begin\r\n" +
            "  return a * b * 4\r\n" +
            "end\r\n" +
            "print MyFunction(MyFunction(3, 5), 2)\r\n" +
            "\r\n";

        String[] actual = executeProgram(sourceCode);
        validateLines(makeExpected("480"), actual);

        sourceCode =
            "function MyFunction(a, b) begin\r\n" +
            "  return a * b * 4\r\n" +
            "end\r\n" +
            "print MyFunction(2, MyFunction(3, 5))\r\n" +
            "\r\n";

        actual = executeProgram(sourceCode);
        validateLines(makeExpected("480"), actual);
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testFunction_parms_too_few_parameters() throws Exception {
        String sourceCode =
            "function MyFunction(a, b) begin\r\n" +
            "  return a * b * 4\r\n" +
            "end\r\n" +
            "print MyFunction(3)\r\n" +
            "\r\n";

        try {
            setAllowParseExceptions(true);
            executeProgram(sourceCode);
            fail("Expected a ParserException");
        } catch (ParserException e) {
            assertEquals("wrong message", "Wrong number of arguments passed. Needed 2, but got 1.", e.getMessage());
        }

        sourceCode =
            "function MyFunction(a, b) begin\r\n" +
            "  return a * b * 4\r\n" +
            "end\r\n" +
            "print MyFunction()\r\n" +
            "\r\n";

        try {
            setAllowParseExceptions(true);
            executeProgram(sourceCode);
            fail("Expected a ParserException");
        } catch (ParserException e) {
            assertEquals("wrong message", "Wrong number of arguments passed. Needed 2, but got 0.", e.getMessage());
        }
    }
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testFunction_parms_too_many_parameters() throws Exception {
        String sourceCode =
            "function MyFunction(a, b) begin\r\n" +
            "  return a * b * 4\r\n" +
            "end\r\n" +
            "print MyFunction(3, 5, 7, 9)\r\n" +
            "\r\n";

        try {
            setAllowParseExceptions(true);
            executeProgram(sourceCode);
            fail("Expected a ParserException");
        } catch (ParserException e) {
            assertEquals("wrong message", "Wrong number of arguments passed. Needed 2, but got 4.", e.getMessage());
        }
    }

}
