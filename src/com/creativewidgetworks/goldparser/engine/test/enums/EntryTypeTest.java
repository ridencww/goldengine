package com.creativewidgetworks.goldparser.engine.test.enums;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.enums.EntryType;

public class EntryTypeTest extends TestCase {

    @Test
    public void testAllValuesCovered() {
        // This test detects when new enumeration values are added that are
        // not reflected in the test indicating that this test 
        // suite should be updated.
        EntryType.valueOf(EntryType.BOOLEAN.toString()); // for eclEmma coverage
        assertEquals("wrong number of enumerated values... test should be examined and updated", 
            7, EntryType.values().length);
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testGetEntryType() {
        Object[][] expected = {
            {EntryType.BOOLEAN, Integer.valueOf(66)},
            {EntryType.BYTE, Integer.valueOf(98)},
            {EntryType.EMPTY, Integer.valueOf(69)},
            {EntryType.ERROR, Integer.valueOf(0)},
            {EntryType.STRING, Integer.valueOf(83)},
            {EntryType.UINT16, Integer.valueOf(73)},
            {EntryType.UNDEFINED, Integer.valueOf(-1)},
        };
        
        for (int i = 0; i < expected.length; i++) {
            int code = ((Integer)expected[i][1]).intValue();
            EntryType type = EntryType.getEntryType(code);
            assertEquals("wrong enum value", expected[i][0], type);
            assertEquals("wrong code", code, type.getCode());
        }
        
        // Search for type that doesn't exist
        EntryType type = EntryType.getEntryType(-2);
        assertEquals("should have returned UNDEFINED", EntryType.UNDEFINED, type);
    }
    
}
