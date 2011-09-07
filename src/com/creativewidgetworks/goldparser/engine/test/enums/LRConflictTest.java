package com.creativewidgetworks.goldparser.engine.test.enums;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.enums.LRConflict;

public class LRConflictTest extends TestCase {

    @Test
    public void testAllValuesCovered() {
        // This test detects when new enumeration values are added that are
        // not reflected in the test indicating that this test 
        // suite should be updated.
        LRConflict.valueOf(LRConflict.ACCEPT_REDUCE.toString()); // for eclEmma coverage
        assertEquals("wrong number of enumerated values... test should be examined and updated", 
            6, LRConflict.values().length);
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testLRActionType() {
        Object[][] expected = {
            {LRConflict.ACCEPT_REDUCE, Integer.valueOf(4)},
            {LRConflict.NONE, Integer.valueOf(5)},
            {LRConflict.REDUCE_REDUCE, Integer.valueOf(3)},
            {LRConflict.SHIFT_REDUCE, Integer.valueOf(2)},
            {LRConflict.SHIFT_SHIFT, Integer.valueOf(1)},
            {LRConflict.UNDEFINED, Integer.valueOf(-1)},
        };
        
        for (int i = 0; i < expected.length; i++) {
            int code = ((Integer)expected[i][1]).intValue();
            LRConflict type = LRConflict.getLRConflict(code);
            assertEquals("wrong enum value", expected[i][0], type);
            assertEquals("wrong code", code, type.getCode());
        }
        
        // Search for type that doesn't exist
        LRConflict type = LRConflict.getLRConflict(-2);
        assertEquals("should have returned UNDEFINED", LRConflict.UNDEFINED, type);
    }
    
}
