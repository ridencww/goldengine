package com.creativewidgetworks.goldparser.engine.test.enums;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.enums.ParseMessage;

public class ParseMessageTest extends TestCase {

    @Test
    public void testAllValuesCovered() {
        // This test detects when new enumeration values are added that are
        // not reflected in the test indicating that this test 
        // suite should be updated.
        ParseMessage.valueOf(ParseMessage.ACCEPT.toString()); // for eclEmma coverage
        assertEquals("wrong number of enumerated values... test should be examined and updated", 
            9, ParseMessage.values().length);
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testLRActionType() {
        Object[][] expected = {
            {ParseMessage.ACCEPT, Integer.valueOf(2)},
            {ParseMessage.GROUP_ERROR, Integer.valueOf(6)},
            {ParseMessage.INTERNAL_ERROR, Integer.valueOf(7)},
            {ParseMessage.LEXICAL_ERROR, Integer.valueOf(4)},
            {ParseMessage.NOT_LOADED_ERROR, Integer.valueOf(3)},
            {ParseMessage.REDUCTION, Integer.valueOf(1)},
            {ParseMessage.SYNTAX_ERROR, Integer.valueOf(5)},
            {ParseMessage.TOKEN_READ, Integer.valueOf(0)},
            {ParseMessage.UNDEFINED, Integer.valueOf(-1)},
        };
        
        for (int i = 0; i < expected.length; i++) {
            int code = ((Integer)expected[i][1]).intValue();
            ParseMessage type = ParseMessage.getParseMessage(code);
            assertEquals("wrong enum value", expected[i][0], type);
            assertEquals("wrong code", code, type.getCode());
        }
        
        // Search for type that doesn't exist
        ParseMessage type = ParseMessage.getParseMessage(-2);
        assertEquals("should have returned UNDEFINED", ParseMessage.UNDEFINED, type);
    }
    
}
