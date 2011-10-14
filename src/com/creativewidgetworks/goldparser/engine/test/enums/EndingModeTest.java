package com.creativewidgetworks.goldparser.engine.test.enums;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.enums.EndingMode;

public class EndingModeTest extends TestCase {

    @Test
    public void testAllValuesCovered() {
        // This test detects when new enumeration values are added that are
        // not reflected in the test indicating that this test 
        // suite should be updated.
        EndingMode.valueOf(EndingMode.UNDEFINED.toString()); // for eclEmma coverage
        assertEquals("wrong number of enumerated values... test should be examined and updated", 
            3, EndingMode.values().length);
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testEndingMode() {
        Object[][] expected = {
            {EndingMode.OPEN, Integer.valueOf(0)},
            {EndingMode.CLOSED, Integer.valueOf(1)},
            {EndingMode.UNDEFINED, Integer.valueOf(-1)},
        };

        for (int i = 0; i < expected.length; i++) {
            int code = ((Integer)expected[i][1]).intValue();
            EndingMode mode = EndingMode.getEndingMode(code);
            assertEquals("wrong enum value", expected[i][0], mode);
            assertEquals("wrong code", code, mode.getCode());
        }
        
        // Search for type that doesn't exist
        EndingMode mode = EndingMode.getEndingMode(-2);
        assertEquals("should have returned UNDEFINED", EndingMode.UNDEFINED, mode);
    }
    
}
