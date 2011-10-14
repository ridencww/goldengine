package com.creativewidgetworks.goldparser.engine.test.enums;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.enums.CGTRecord;

import junit.framework.TestCase;

public class CGTRecordTest extends TestCase {

    @Test
    public void testAllValuesCovered() {
        // This test detects when new enumeration values are added that are
        // not reflected in the test indicating that this test 
        // suite should be updated.
        CGTRecord.valueOf(CGTRecord.CHARRANGES.toString()); // for eclEmma coverage
        assertEquals("wrong number of enumerated values... test should be examined and updated", 
            14, CGTRecord.values().length);
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testGetCGTRecord() {
        Object[][] expected = {
            {CGTRecord.CHARRANGES, Integer.valueOf(99)},
            {CGTRecord.CHARSET, Integer.valueOf(67)},
            {CGTRecord.COUNTS, Integer.valueOf(84)},
            {CGTRecord.COUNTS5, Integer.valueOf(116)},
            {CGTRecord.DFASTATE, Integer.valueOf(68)},
            {CGTRecord.GROUP, Integer.valueOf(103)},
            {CGTRecord.GROUPNESTING, Integer.valueOf(110)},
            {CGTRecord.INITIALSTATES, Integer.valueOf(73)},
            {CGTRecord.LRSTATE, Integer.valueOf(76)},
            {CGTRecord.PARAMETER, Integer.valueOf(80)},
            {CGTRecord.PROPERTY, Integer.valueOf(112)},
            {CGTRecord.RULE, Integer.valueOf(82)},
            {CGTRecord.SYMBOL, Integer.valueOf(83)},
            {CGTRecord.UNDEFINED, Integer.valueOf(-1)},
        };

        for (int i = 0; i < expected.length; i++) {
            int code = ((Integer)expected[i][1]).intValue();
            CGTRecord record = CGTRecord.getCGTRecord(code);
            assertEquals("wrong enum value", expected[i][0], record);
            assertEquals("wrong code", code, record.getCode());
        }
        
        // Search for type that doesn't exist
        CGTRecord record = CGTRecord.getCGTRecord(-2);
        assertEquals("should have returned UNDEFINED", CGTRecord.UNDEFINED, record);
    }
    
}
