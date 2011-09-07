package com.creativewidgetworks.goldparser.simple3.test;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.enums.SymbolType;
import com.creativewidgetworks.goldparser.simple3.rulehandlers.LoopUntil;

public class LoopUntilTest extends GOLDParserTestCase {

    /*----------------------------------------------------------------------------*/
    /* Test object directly 
    /*----------------------------------------------------------------------------*/   

    @Test
    public void testLoopUntil_no_reductions() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        try {
            new LoopUntil(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Invalid state, stack empty.", pe.getMessage());
        }
    }    

    /*----------------------------------------------------------------------------*/

    @Test
    public void testLoopUntil_parameters() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();

        // Too few
        try {
            parser.makeReductionAndPush(
                makeToken("loop", SymbolType.CONTENT, "loop"),
                makeToken("Id", SymbolType.CONTENT, "x"),
                makeToken("until", SymbolType.CONTENT, "until")
            );
            new LoopUntil(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 4 but got 3.", pe.getMessage());
        }
        
        parser.clear();
        
        // Too many
        try {
            parser.makeReductionAndPush(
                makeToken("loop", SymbolType.CONTENT, "loop"),
                makeToken("Id", SymbolType.CONTENT, "x"),
                makeToken("until", SymbolType.CONTENT, "until"),
                makeToken("Id", SymbolType.CONTENT, "y"),
                makeToken("Id", SymbolType.CONTENT, "z")
                );            
            new LoopUntil(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 4 but got 5.", pe.getMessage());
        }
        
        parser.clear();
        
        // Just right
        parser.makeReductionAndPush(
            makeToken("loop", SymbolType.CONTENT, "loop"),
            makeToken("Id", SymbolType.CONTENT, "x"),
            makeToken("until", SymbolType.CONTENT, "until"),
            makeToken("Id", SymbolType.CONTENT, "y"));
        new LoopUntil(parser);
    }    
    
    /*----------------------------------------------------------------------------*/
    /* Test object indirectly using the engine 
    /*----------------------------------------------------------------------------*/

    @Test
    public void testLoopUntil() throws Exception {
        String src = 
            "i = 1\r\n" +
            "loop\r\n" +
            "  print i\r\n" +
            "  i = i + 1\r\n" +
            "until i == 6\r\n";

        String[] actual = executeProgram(src);
        validateLines(makeExpected("1", "2", "3", "4", "5"), actual);
    }
}
