package com.creativewidgetworks.goldparser.engine.test.enums;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.enums.AdvanceMode;

public class AdvanceModeTest extends TestCase {

    @Test
    public void testAllValuesCovered() {
        // This test detects when new enumeration values are added that are
        // not reflected in the test indicating that this test 
        // suite should be updated.
        AdvanceMode.valueOf(AdvanceMode.UNDEFINED.toString()); // for eclEmma coverage
        assertEquals("wrong number of enumerated values... test should be examined and updated", 
            3, AdvanceMode.values().length);
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testAdvanceMode() {
        Object[][] expected = {
            {AdvanceMode.TOKEN, Integer.valueOf(0)},
            {AdvanceMode.CHARACTER, Integer.valueOf(1)},
            {AdvanceMode.UNDEFINED, Integer.valueOf(-1)},
        };

        for (int i = 0; i < expected.length; i++) {
            int code = ((Integer)expected[i][1]).intValue();
            AdvanceMode mode = AdvanceMode.getAdvanceMode(code);
            assertEquals("wrong enum value", expected[i][0], mode);
            assertEquals("wrong code", code, mode.getCode());
        }
        
        // Search for type that doesn't exist
        AdvanceMode mode = AdvanceMode.getAdvanceMode(-2);
        assertEquals("should have returned UNDEFINED", AdvanceMode.UNDEFINED, mode);
    }
    
}
