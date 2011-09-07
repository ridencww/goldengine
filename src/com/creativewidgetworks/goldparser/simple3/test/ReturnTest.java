package com.creativewidgetworks.goldparser.simple3.test;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Token;
import com.creativewidgetworks.goldparser.simple3.rulehandlers.Return;

public class ReturnTest extends GOLDParserTestCase {

    /*----------------------------------------------------------------------------*/
    /* Test object directly 
    /*----------------------------------------------------------------------------*/   

    @Test
    public void testReturn_no_reductions() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        try {
            new Return(parser);
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Invalid state, stack empty.", pe.getMessage());
        }
    }    

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testReturn_parameters() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
       
        // Too many
        try {
            parser.makeReductionAndPush(new Token("Return"), new Token("Ok"), new Token("Extra"));
            new Return(parser).execute();
            fail("Expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "Wrong number of parameters, expected 2 but got 3.", pe.getMessage());
        }
        
        parser.clear();
        
        // Just right
        parser.makeReductionAndPush(new Token("Return"));
        new Return(parser).execute();

        parser.clear();
        
        parser.makeReductionAndPush(new Token("Return"), new Token("1"));
        new Return(parser).execute();
    }    

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testReturn() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        Return reduction;
        
        // Return without a value
        parser.makeReductionAndPush(new Token("return"));
        reduction = new Return(parser);  
        reduction.execute();
        assertNull("should be empty return", reduction.getValue());
        
        parser.clear();
        
        // Return with a value 
        parser.makeReductionAndPush(new Token("return"), new Token(makeStringLiteralForTest("Hello")));
        reduction = new Return(parser);  
        reduction.execute();
        assertEquals("wrong value", "Hello", reduction.getValue().asString());
    }
    
}
