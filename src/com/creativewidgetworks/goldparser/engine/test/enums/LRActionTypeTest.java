package com.creativewidgetworks.goldparser.engine.test.enums;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.enums.LRActionType;

public class LRActionTypeTest extends TestCase {

    @Test
    public void testAllValuesCovered() {
        // This test detects when new enumeration values are added that are
        // not reflected in the test indicating that this test 
        // suite should be updated.
        LRActionType.valueOf(LRActionType.ACCEPT.toString()); // for eclEmma coverage
        assertEquals("wrong number of enumerated values... test should be examined and updated", 
            6, LRActionType.values().length);
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testLRActionType() {
        Object[][] expected = {
            {LRActionType.ACCEPT, Integer.valueOf(4)},
            {LRActionType.ERROR, Integer.valueOf(5)},
            {LRActionType.GOTO, Integer.valueOf(3)},
            {LRActionType.REDUCE, Integer.valueOf(2)},
            {LRActionType.SHIFT, Integer.valueOf(1)},
            {LRActionType.UNDEFINED, Integer.valueOf(-1)},
        };
        
        for (int i = 0; i < expected.length; i++) {
            int code = ((Integer)expected[i][1]).intValue();
            LRActionType type = LRActionType.getLRActionType(code);
            assertEquals("wrong enum value", expected[i][0], type);
            assertEquals("wrong code", code, type.getCode());
        }
        
        // Search for type that doesn't exist
        LRActionType type = LRActionType.getLRActionType(-2);
        assertEquals("should have returned UNDEFINED", LRActionType.UNDEFINED, type);
    }
    
}
