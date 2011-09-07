package com.creativewidgetworks.goldparser.engine.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.Entry;
import com.creativewidgetworks.goldparser.engine.enums.EntryType;

public class EntryTest extends TestCase {

    @Test
    public void testConstructorDefault() {
        Entry entry = new Entry();
        assertEquals("type", EntryType.UNDEFINED, entry.getType());
        assertNull("value", entry.getValue());
    }
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testConstructorTypeValue() {
        Entry entry = new Entry(EntryType.STRING, "StringEntry");
        assertEquals("type", EntryType.STRING, entry.getType());
        assertEquals("value", "StringEntry", entry.getValue());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testSetters() {
        Entry entry = new Entry();
        entry.setType(EntryType.BYTE);
        entry.setValue(Byte.valueOf((byte)13));
        assertEquals("type", EntryType.BYTE, entry.getType());
        assertEquals("value", new Byte((byte)13), entry.getValue());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testToString() {
        Entry entry = new Entry(EntryType.STRING, "StringEntry");    
        assertEquals("wrong text", "STRING: StringEntry", entry.toString());
    }

}
