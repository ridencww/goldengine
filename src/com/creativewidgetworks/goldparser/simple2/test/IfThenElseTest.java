package com.creativewidgetworks.goldparser.simple2.test;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.enums.SymbolType;
import com.creativewidgetworks.goldparser.simple2.rulehandlers.IfThenElse;
import com.creativewidgetworks.goldparser.util.GOLDParserTestCase;

public class IfThenElseTest extends GOLDParserTestCase {

    /*----------------------------------------------------------------------------*/
    /* Test object directly 
    /*----------------------------------------------------------------------------*/   

    @Test
    public void testIfThenElse_no_reductions() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        try {
            new IfThenElse(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Invalid state, stack empty.", pe.getMessage());
        }
    }    

    /*----------------------------------------------------------------------------*/

    @Test
    public void testIfThenElse_parameters() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        // Too few
        try {
            parser.makeReductionAndPush(
                makeToken("if", SymbolType.CONTENT, "if"),
                makeToken("Expression", SymbolType.NON_TERMINAL, ""),
                makeToken("then", SymbolType.CONTENT, "then"),
                makeToken("Expression", SymbolType.NON_TERMINAL, "")
            );
            new IfThenElse(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 5 but got 4.", pe.getMessage());
        }
        parser.clear();
        try {
            parser.makeReductionAndPush(
                makeToken("if", SymbolType.CONTENT, "if"),
                makeToken("Expression", SymbolType.NON_TERMINAL, ""),
                makeToken("then", SymbolType.CONTENT, "then"),
                makeToken("Expression", SymbolType.NON_TERMINAL, ""),
                makeToken("else", SymbolType.CONTENT, "then"),
                makeToken("Expression", SymbolType.NON_TERMINAL, "")
            );
            new IfThenElse(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 7 but got 6.", pe.getMessage());
        }        
        
        parser.clear();
        
        // Too many
        try {
            parser.makeReductionAndPush(
                makeToken("if", SymbolType.CONTENT, "if"),
                makeToken("Expression", SymbolType.NON_TERMINAL, ""),
                makeToken("then", SymbolType.CONTENT, "then"),
                makeToken("Expression", SymbolType.NON_TERMINAL, ""),
                makeToken("else", SymbolType.CONTENT, "then"),
                makeToken("Expression", SymbolType.NON_TERMINAL, ""),
                makeToken("Expression", SymbolType.NON_TERMINAL, ""),
                makeToken("end", SymbolType.CONTENT, "end"));
            new IfThenElse(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 7 but got 8.", pe.getMessage());
        }
        
        parser.clear();
        
        // Just right
        parser.makeReductionAndPush(
            makeToken("if", SymbolType.CONTENT, "if"),
            makeToken("Expression", SymbolType.NON_TERMINAL, ""),
            makeToken("then", SymbolType.CONTENT, "then"),
            makeToken("Expression", SymbolType.NON_TERMINAL, ""),
            makeToken("end", SymbolType.CONTENT, "end"));
        new IfThenElse(parser);

        parser.clear();
        
        parser.makeReductionAndPush(
            makeToken("if", SymbolType.CONTENT, "if"),
            makeToken("Expression", SymbolType.NON_TERMINAL, ""),
            makeToken("then", SymbolType.CONTENT, "then"),
            makeToken("Expression", SymbolType.NON_TERMINAL, ""),
            makeToken("else", SymbolType.CONTENT, "then"),
            makeToken("Expression", SymbolType.NON_TERMINAL, ""),
            makeToken("end", SymbolType.CONTENT, "end"));        
        new IfThenElse(parser);
    }    
    
    /*----------------------------------------------------------------------------*/
    /* Test object indirectly using the engine 
    /*----------------------------------------------------------------------------*/

    @Test
    public void testIfThenElse_if_then() throws Exception {
        String src = 
            "display 'start'\r\n" +
            "if 1 == 1 then\r\n" +
            "  display \"one\"\r\n" +
            "end\r\n" +
            "if 2 == 1 then\r\n" +
            "  display 'true'\r\n" +
            "end\r\n" +
            "display 'end'\r\n";

        String[] actual = executeProgram(src);
        validateLines(makeExpected("start", "one", "end"), actual);
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testIfThenElse_if_then_else() throws Exception {
        String src = 
            "if 1 == 1 then\r\n" +
            "  display \"one\"\r\n" +
            "else" +
            "  display \"not one\"\r\n" +
            "end\r\n" +
            "if 2 == 1 then\r\n" +
            "  display 'true'\r\n" +
            "else" +
            "  display 'not true'\r\n" +
            "end\r\n";
        
        String[] actual = executeProgram(src);
        validateLines(makeExpected("one", "not true"), actual);
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testIfThenElse_if_then_else_chained() throws Exception {
        String src = 
            "assign x = 3\r\n" +
            "if x == 1 then\r\n" +
            "  display 'one'\r\n" +
            "else if x == 2 then\r\n" +
            "  display 'two'\r\n" +
            "else if x == 3 then\r\n" +
            "  display 'three'\r\n" +
            "end end end\r\n" +          
            "assign x = 2\r\n" +
            "if x == 1 then\r\n" +
            "  display 'one'\r\n" +
            "else if x == 2 then\r\n" +
            "  display 'two'\r\n" +
            "else if x == 3 then\r\n" +
            "  display 'three'\r\n" +
            "end end end\r\n" +
            "assign x = 1\r\n" +
            "if x == 1 then\r\n" +
            "  display 'one'\r\n" +
            "else if x == 2 then\r\n" +
            "  display 'two'\r\n" +
            "else if x == 3 then\r\n" +
            "  display 'three'\r\n" +
            "end end end\r\n";
        
        String[] actual = executeProgram(src);
        validateLines(makeExpected("three", "two", "one"), actual);
    }
}
