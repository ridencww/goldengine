package com.creativewidgetworks.goldparser.engine.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.CharacterRange;
import com.creativewidgetworks.goldparser.engine.CharacterSet;

public class CharacterSetTest extends TestCase {

    @Test
    public void testCharacterSet() {
        CharacterSet set = new CharacterSet();
        assertEquals("wrong size", 0, set.size());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testConstructorWithSizing() {
        CharacterSet set = new CharacterSet(5);
        assertEquals("wrong number of placeholders", 5, set.size());
        for (int i = 0; i < set.size(); i++) {
            assertNull(set.get(i));
        }
    }  

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testContainsChar() {
        // version 1 format (Strings)
        CharacterSet set = new CharacterSet();
        set.add(new CharacterRange("ABC"));
        set.add(new CharacterRange("XyZ"));
        assertTrue("A", set.contains("A".codePointAt(0)));
        assertTrue("B", set.contains("B".codePointAt(0)));
        assertTrue("C", set.contains("C".codePointAt(0)));
        assertTrue("X", set.contains("X".codePointAt(0)));
        assertTrue("y", set.contains("y".codePointAt(0)));
        assertTrue("Z", set.contains("Z".codePointAt(0)));
        assertFalse("a", set.contains("a".codePointAt(0)));
        assertFalse("Y", set.contains("Y".codePointAt(0)));
        assertFalse("P", set.contains("P".codePointAt(0)));
        
        // version 5 format (ranges)
        set = new CharacterSet();
        set.add(new CharacterRange(65, 67));   // A..C
        set.add(new CharacterRange(88, 88));   // X
        set.add(new CharacterRange(121, 121)); // y
        set.add(new CharacterRange(90, 90));   // Z
        assertTrue("A", set.contains("A".codePointAt(0)));
        assertTrue("B", set.contains("B".codePointAt(0)));
        assertTrue("C", set.contains("C".codePointAt(0)));
        assertTrue("X", set.contains("X".codePointAt(0)));
        assertTrue("y", set.contains("y".codePointAt(0)));
        assertTrue("Z", set.contains("Z".codePointAt(0)));
        assertFalse("a", set.contains("a".codePointAt(0)));
        assertFalse("Y", set.contains("Y".codePointAt(0)));
        assertFalse("P", set.contains("P".codePointAt(0)));        
    }

}
