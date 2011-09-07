package com.creativewidgetworks.goldparser.simple3.test;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.enums.SymbolType;
import com.creativewidgetworks.goldparser.simple3.rulehandlers.ForLoop;

public class ForLoopTest extends GOLDParserTestCase {

    /*----------------------------------------------------------------------------*/
    /* Test object directly 
    /*----------------------------------------------------------------------------*/   

    @Test
    public void testForLoop_no_reductions() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        try {
            new ForLoop(parser);
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
                makeToken("for", SymbolType.CONTENT, "for"),
                makeToken("(", SymbolType.CONTENT, "("),
                makeToken("Id", SymbolType.CONTENT, "x"),
                makeToken(")", SymbolType.CONTENT, ")")
            );
            new ForLoop(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 11 but got 4.", pe.getMessage());
        }
        
        parser.clear();
        
        // Too many
        try {
            parser.makeReductionAndPush(
                makeToken("for", SymbolType.CONTENT, "loop"),
                makeToken("(", SymbolType.CONTENT, "x"),
                makeToken("i", SymbolType.CONTENT, "i = 1"),
                makeToken(";", SymbolType.CONTENT, ";"),
                makeToken("c", SymbolType.CONTENT, "i < 5"),
                makeToken(";", SymbolType.CONTENT, ";"),
                makeToken("inc", SymbolType.CONTENT, "i = i + 1"),
                makeToken(")", SymbolType.CONTENT, "x"),
                makeToken("do", SymbolType.CONTENT, "do"),
                makeToken("code", SymbolType.CONTENT, "print"),
                makeToken("end", SymbolType.CONTENT, "end"),
                makeToken("extra", SymbolType.CONTENT, "extra")
                );            
            new ForLoop(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 11 but got 12.", pe.getMessage());
        }
        
        parser.clear();
        
        // Just right
        parser.makeReductionAndPush(
            makeToken("for", SymbolType.CONTENT, "loop"),
            makeToken("(", SymbolType.CONTENT, "x"),
            makeToken("i", SymbolType.CONTENT, "i = 1"),
            makeToken(";", SymbolType.CONTENT, ";"),
            makeToken("c", SymbolType.CONTENT, "i < 5"),
            makeToken(";", SymbolType.CONTENT, ";"),
            makeToken("inc", SymbolType.CONTENT, "i = i + 1"),
            makeToken(")", SymbolType.CONTENT, "x"),
            makeToken("do", SymbolType.CONTENT, "do"),
            makeToken("code", SymbolType.CONTENT, "print"),
            makeToken("end", SymbolType.CONTENT, "end")
            );
        new ForLoop(parser);
    }    
    
    /*----------------------------------------------------------------------------*/
    /* Test object indirectly using the engine 
    /*----------------------------------------------------------------------------*/

    @Test
    public void testForLoop() throws Exception {
        String src = 
            "\r\n" +
            "for (i = 1; i <= 5; i = i + 1) do\r\n" +
            "  print i\r\n" +
            "end\r\n";

        String[] actual = executeProgram(src);
        validateLines(makeExpected("1", "2", "3", "4", "5"), actual);
    }
}
