package com.creativewidgetworks.goldparser.simple3.test;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.enums.SymbolType;
import com.creativewidgetworks.goldparser.simple3.rulehandlers.WhileLoop;

public class WhileLoopTest extends GOLDParserTestCase {

    /*----------------------------------------------------------------------------*/
    /* Test object directly 
    /*----------------------------------------------------------------------------*/   

    @Test
    public void testWhileLoop_no_reductions() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        try {
            new WhileLoop(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Invalid state, stack empty.", pe.getMessage());
        }
    }    

    /*----------------------------------------------------------------------------*/

    @Test
    public void testWhileLoop_parameters() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        // Too few
        try {
            parser.makeReductionAndPush(
                makeToken("while", SymbolType.CONTENT, "while"),
                makeToken("Expression", SymbolType.NON_TERMINAL, ""),
                makeToken("do", SymbolType.CONTENT, "do")
            );
            new WhileLoop(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 5 but got 3.", pe.getMessage());
        }
        
        parser.clear();
        
        // Too many
        try {
            parser.makeReductionAndPush(
                makeToken("while", SymbolType.CONTENT, "while"),
                makeToken("Expression", SymbolType.NON_TERMINAL, "i"),
                makeToken("do", SymbolType.CONTENT, "do"),            
                makeToken("Id", SymbolType.CONTENT, "x"),
                makeToken("Id", SymbolType.CONTENT, "y"),
                makeToken("end", SymbolType.CONTENT, "end"));            
            new WhileLoop(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 5 but got 6.", pe.getMessage());
        }
        
        parser.clear();
        
        // Just right
        parser.makeReductionAndPush(
            makeToken("while", SymbolType.CONTENT, "while"),
            makeToken("Expression", SymbolType.NON_TERMINAL, "i"),
            makeToken("do", SymbolType.CONTENT, "do"),            
            makeToken("Id", SymbolType.CONTENT, "x"),
            makeToken("end", SymbolType.CONTENT, "end"));
        new WhileLoop(parser);
    }    
    
    /*----------------------------------------------------------------------------*/
    /* Test object indirectly using the engine 
    /*----------------------------------------------------------------------------*/

    @Test
    public void testWhileLoop() throws Exception {
        String src = 
            "i = 1\r\n" +
            "while i <= 5 do\r\n" +
            "  print i\r\n" +
            "  i = i + 1\r\n" +
            "end\r\n";

        String[] actual = executeProgram(src);
        validateLines(makeExpected("1", "2", "3", "4", "5"), actual);
    }
}
