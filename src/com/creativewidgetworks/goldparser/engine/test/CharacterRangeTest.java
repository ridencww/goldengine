package com.creativewidgetworks.goldparser.engine.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.CharacterRange;

public class CharacterRangeTest extends TestCase {

    @Test
    public void testCharacterRangeString() {
        CharacterRange chars = new CharacterRange("abcdefgABC");
        assertEquals("wrong set", "abcdefgABC", chars.getCharacters());
        assertEquals("start", 0, chars.getStart());
        assertEquals("end", 0, chars.getEnd());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testCharacterRangeIntInt() {
        CharacterRange chars = new CharacterRange(65, 68);
        assertEquals("string", null, chars.getCharacters());
        assertEquals("start", 65, chars.getStart());
        assertEquals("end", 68, chars.getEnd());
    }

}
