package com.creativewidgetworks.goldparser.engine.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.CharacterSetList;

public class CharacterSetListTest extends TestCase {

    @Test
    public void testListConstructorWithSizing() {
        CharacterSetList sets = new CharacterSetList(5);
        assertEquals("wrong number of placeholders", 5, sets.size());
        for (int i = 0; i < sets.size(); i++) {
            assertNull(sets.get(i));
        }
    }
   
}
