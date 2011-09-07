package com.creativewidgetworks.goldparser.engine.test.enums;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.enums.SymbolType;

public class SymbolTypeTest extends TestCase {

    @Test
    public void testAllValuesCovered() {
        // This test detects when new enumeration values are added that are
        // not reflected in the test indicating that this test 
        // suite should be updated.
        SymbolType.valueOf(SymbolType.COMMENT_LINE.toString()); // for eclEmma coverage
        assertEquals("wrong number of enumerated values... test should be examined and updated", 
            9, SymbolType.values().length);
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testLRActionType() {
        Object[][] expected = {
            {SymbolType.COMMENT_LINE, Integer.valueOf(6)},
            {SymbolType.CONTENT, Integer.valueOf(1)},
            {SymbolType.END, Integer.valueOf(3)},
            {SymbolType.ERROR, Integer.valueOf(7)},
            {SymbolType.GROUP_END, Integer.valueOf(5)},
            {SymbolType.GROUP_START, Integer.valueOf(4)},
            {SymbolType.NOISE, Integer.valueOf(2)},
            {SymbolType.NON_TERMINAL, Integer.valueOf(0)},
            {SymbolType.UNDEFINED, Integer.valueOf(-1)},
        };
        
        for (int i = 0; i < expected.length; i++) {
            int code = ((Integer)expected[i][1]).intValue();
            SymbolType type = SymbolType.getSymbolType(code);
            assertEquals("wrong enum value", expected[i][0], type);
            assertEquals("wrong code", code, type.getCode());
        }
        
        // Search for type that doesn't exist
        SymbolType type = SymbolType.getSymbolType(-2);
        assertEquals("should have returned UNDEFINED", SymbolType.UNDEFINED, type);
    }
    
}
