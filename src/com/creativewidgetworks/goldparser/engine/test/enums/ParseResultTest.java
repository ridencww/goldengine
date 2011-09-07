package com.creativewidgetworks.goldparser.engine.test.enums;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.enums.ParseResult;

public class ParseResultTest extends TestCase {

    @Test
    public void testAllValuesCovered() {
        // This test detects when new enumeration values are added that are
        // not reflected in the test indicating that this test 
        // suite should be updated.
        ParseResult.valueOf(ParseResult.ACCEPT.toString()); // for eclEmma coverage
        assertEquals("wrong number of enumerated values... test should be examined and updated", 
            7, ParseResult.values().length);
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testLRActionType() {
        Object[][] expected = {
            {ParseResult.ACCEPT, Integer.valueOf(1)},
            {ParseResult.INTERNAL_ERROR, Integer.valueOf(6)},
            {ParseResult.REDUCE_ELIMINATED, Integer.valueOf(4)},
            {ParseResult.REDUCE_NORMAL, Integer.valueOf(3)},
            {ParseResult.SHIFT, Integer.valueOf(2)},
            {ParseResult.SYNTAX_ERROR, Integer.valueOf(5)},
            {ParseResult.UNDEFINED, Integer.valueOf(-1)},
        };
        
        for (int i = 0; i < expected.length; i++) {
            int code = ((Integer)expected[i][1]).intValue();
            ParseResult type = ParseResult.getParseResult(code);
            assertEquals("wrong enum value", expected[i][0], type);
            assertEquals("wrong code", code, type.getCode());
        }
        
        // Search for type that doesn't exist
        ParseResult type = ParseResult.getParseResult(-2);
        assertEquals("should have returned UNDEFINED", ParseResult.UNDEFINED, type);
    }
    
}
