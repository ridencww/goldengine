package com.creativewidgetworks.goldparser.engine.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.CGT;
import com.creativewidgetworks.goldparser.engine.Entry;
import com.creativewidgetworks.goldparser.engine.enums.EntryType;

public class CGTTest extends TestCase {

    // "Gold Parser<0>"
    public static final byte[] HEADER = new byte[] {71,0,79,0,76,0,68,0,32,0,80,0,97,0,114,0,115,0,101,0,114,0,0,0};

    // TRUE, FALSE, TRUE, FALSE
    public static final byte[] SAMPLE_BOOLEANS = new byte[] {
        (byte)CGT.RECORD_CONTENT_MULTI, 4, 0,  // 4 entries follow
        (byte)EntryType.BOOLEAN.getCode(), 1,
        (byte)EntryType.BOOLEAN.getCode(), 0,
        (byte)EntryType.BOOLEAN.getCode(), 1,
        (byte)EntryType.BOOLEAN.getCode(), 0,
    };
    
    // 1, 2, 3
    public static final byte[] SAMPLE_BYTES = new byte[] {
        (byte)CGT.RECORD_CONTENT_MULTI, 3, 0,  // 3 entries follow
        (byte)EntryType.BYTE.getCode(), 1,
        (byte)EntryType.BYTE.getCode(), 2,
        (byte)EntryType.BYTE.getCode(), 3,
    };

    // 1, 256, 771
    public static final byte[] SAMPLE_UINT16S = new byte[] {
        (byte)CGT.RECORD_CONTENT_MULTI, 3, 0,  // 3 entries follow
        (byte)EntryType.UINT16.getCode(), 1, 0,
        (byte)EntryType.UINT16.getCode(), 0, 1,
        (byte)EntryType.UINT16.getCode(), 3, 3,
    };

    // "Abc", "DėF" (e dot)
    public static final byte[] SAMPLE_STRINGS = new byte[] {
        (byte)CGT.RECORD_CONTENT_MULTI, 2, 0,  // 2 entries follow
        (byte)EntryType.STRING.getCode(), 65, 0, 98, 0, 99, 0, 0, 0,
        (byte)EntryType.STRING.getCode(), 68, 0, 23, 1, 70, 0, 0, 0,
    };
    
    // Combination of exceptional types unlikely to be in the stream, but defined in enumeration
    public static final byte[] SAMPLE_UNLIKELY = new byte[] {
        (byte)CGT.RECORD_CONTENT_MULTI, 1, 0,  // 1 entry follows
        (byte)EntryType.EMPTY.getCode(),
        (byte)CGT.RECORD_CONTENT_MULTI, 1, 0,  // 1 entry follows
        (byte)EntryType.ERROR.getCode(),
        (byte)CGT.RECORD_CONTENT_MULTI, 1, 0,  // 1 entry follows
        (byte)-56, // unknown value mapped to UNDEFINED
    };
    
    /*----------------------------------------------------------------------------*/

    /**
     * Concatenate arrays together
     * @param first array to copy
     * @param other arrays to copy (optional)
     * @return byte[] array will all values added.
     */
    private byte[] concat(byte[] first, byte[]... others) {
        int offset = first.length;
        int totalLength = offset;
        for (byte[] array : others) {
          totalLength += array.length;
        }
        
        byte[] result = Arrays.copyOf(first, totalLength);
        for (byte[] array : others) {
          System.arraycopy(array, 0, result, offset, array.length);
          offset += array.length;
        }
        
        return result;
      }

    /*----------------------------------------------------------------------------*/

    private void validateExpectEMPTY(CGT cgt) {
        try {
            Entry entry = cgt.retrieveEntry();
            assertTrue("Expected EntryType.EMPTY", EntryType.EMPTY.equals(entry.getType()));
        } catch (IOException e) {
            // okay, expected
        }
    }
    
    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testAtEOF() throws Exception {
        CGT cgt = new CGT();
        assertTrue(cgt.atEOF());  // initially at EOF
        
        cgt.open(new ByteArrayInputStream(new byte[] {}));
        assertTrue("empty (no header, etc.) returns true", cgt.atEOF());
        
        cgt.open(new ByteArrayInputStream(HEADER));
        cgt.close();
        assertTrue("closed stream returns true", cgt.atEOF());

        cgt.open(new ByteArrayInputStream(HEADER));
        assertFalse("header, but nothing else read returns false", cgt.atEOF());
        cgt.getNextRecord();
        assertTrue("no more data returns true", cgt.atEOF());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testClose() throws Exception {
        CGT cgt = new CGT();
        assertTrue(cgt.atEOF());  
        
        // Should be able to close stream if it hasn't been opened
        cgt.close();
        assertTrue(cgt.atEOF());  

        // Should be able to close stream again after it has been closd
        cgt.close();
        assertTrue(cgt.atEOF());  
        
        // Open file and then close it without reading
        cgt.open(new ByteArrayInputStream(HEADER));
        assertFalse("should be ready to read", cgt.atEOF());
        cgt.close();
        assertTrue(cgt.atEOF());  
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testIsRecordComplete() throws Exception {
        CGT cgt = new CGT();
        cgt.open(new ByteArrayInputStream(concat(HEADER, SAMPLE_BYTES)));

        // Read next block
        cgt.getNextRecord();
        assertFalse("should have more to read", cgt.isRecordComplete());
        
        // Read all of the entries (3) and test
        cgt.retrieveByte();
        assertFalse("should have more to read", cgt.isRecordComplete());
        
        cgt.retrieveByte();
        assertFalse("should have more to read", cgt.isRecordComplete());
        
        cgt.retrieveByte();
        assertTrue("should have more to read", cgt.isRecordComplete());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testGetEntryCount() throws Exception {
        CGT cgt = new CGT();
        cgt.open(new ByteArrayInputStream(concat(HEADER, SAMPLE_BYTES)));
        assertEquals("shouldn't have entries to process", 0, cgt.getEntryCount());
        cgt.getNextRecord();
        assertEquals("wrong number of entries", 3, cgt.getEntryCount());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testGetEntriesRead() throws Exception {
        CGT cgt = new CGT();
        cgt.open(new ByteArrayInputStream(concat(HEADER, SAMPLE_BYTES)));
        assertEquals("shouldn't have entries that have been read", 0, cgt.getEntriesRead());
        cgt.getNextRecord();
        assertEquals("shouldn't have entries that have been read", 0, cgt.getEntriesRead());
        cgt.retrieveByte();
        assertEquals("wrong number of entries read", 1, cgt.getEntriesRead());
        cgt.retrieveByte();
        assertEquals("wrong number of entries read", 2, cgt.getEntriesRead());
        cgt.retrieveByte();
        assertEquals("wrong number of entries read", 3, cgt.getEntriesRead());
        
        // This will read past the number of entries.  Number read should remain the same
        try {
            cgt.retrieveByte();
            fail("didn't expect data");
        } catch (IOException e) {
            // ran out of data, this is expected
        }
        assertEquals("wrong number of entries read", 3, cgt.getEntriesRead());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testGetHeader() throws Exception {
        CGT cgt = new CGT();
        assertNull("didn't expect header", cgt.getHeader());
        cgt.open(new ByteArrayInputStream(concat(HEADER, SAMPLE_BYTES)));
        assertEquals("wrong value", "GOLD Parser", cgt.getHeader());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testGetNextRecord() throws Exception {
        CGT cgt = new CGT();
        
        // Empty file
        cgt.open(new ByteArrayInputStream(new byte[] {}));
        assertFalse(cgt.getNextRecord());
        
        // File with no sections
        cgt.open(new ByteArrayInputStream(HEADER));
        assertFalse(cgt.getNextRecord());
        
        // File with three sections
        cgt.open(new ByteArrayInputStream(concat(HEADER, SAMPLE_BOOLEANS, SAMPLE_BYTES, SAMPLE_STRINGS)));
        assertTrue("booleans", cgt.getNextRecord());
        assertTrue("bytes", cgt.getNextRecord());
        assertTrue("strings", cgt.getNextRecord());
        assertFalse("nothing more", cgt.getNextRecord());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testOpenFile() throws Exception {
        CGT cgt = new CGT();

        File tempFile = File.createTempFile("testfile", null);
        
        try {
            // Write test data to a temp file
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(concat(HEADER, SAMPLE_BYTES));
            fos.close();
            
            try {
                cgt.open((File)null);
                fail("Expected IOException");
            } catch (IOException e) {
                assertEquals("wrong message", "File null", e.getMessage());
            }
        
            // Expect successful open and reading of header
            cgt.open(tempFile);
            assertEquals("wrong value", "GOLD Parser", cgt.getHeader());
            
        } finally {
            cgt.close();
            tempFile.delete();
        }
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testOpenInputStream() throws Exception {
        CGT cgt = new CGT();
        
        try {
            cgt.open((InputStream)null);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("wrong message", "InputStream null", e.getMessage());
        }
        
        cgt.open(new ByteArrayInputStream(concat(HEADER, SAMPLE_BYTES)));
        assertEquals("wrong value", "GOLD Parser", cgt.getHeader());
    }
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testReadEntry() throws Exception {
        CGT cgt = new CGT();
        Entry entry;
        
        cgt.open(new ByteArrayInputStream(concat(HEADER, SAMPLE_BOOLEANS, SAMPLE_BYTES, SAMPLE_STRINGS, SAMPLE_UNLIKELY)));
        
        // Booleans
        assertTrue(cgt.getNextRecord());
        entry = cgt.retrieveEntry();
        assertEquals("wrong type", EntryType.BOOLEAN, entry.getType());
        assertEquals("wrong value", "true", entry.getValue().toString());

        // Bytes
        assertTrue(cgt.getNextRecord());
        entry = cgt.retrieveEntry();
        assertEquals("wrong type", EntryType.BYTE, entry.getType());
        assertEquals("wrong value", "1", entry.getValue().toString());

        // Strings
        assertTrue(cgt.getNextRecord());
        entry = cgt.retrieveEntry();
        assertEquals("wrong type", EntryType.STRING, entry.getType());
        assertEquals("wrong value", "Abc", entry.getValue().toString());

        // Empty
        assertTrue(cgt.getNextRecord());
        entry = cgt.retrieveEntry();
        assertEquals("wrong type", EntryType.EMPTY, entry.getType());
        assertEquals("wrong value", "", entry.getValue().toString());

        // Error
        assertTrue(cgt.getNextRecord());
        entry = cgt.retrieveEntry();
        assertEquals("wrong type", EntryType.ERROR, entry.getType());
        assertEquals("wrong value", "", entry.getValue().toString());

        // Undefined
        assertTrue(cgt.getNextRecord());
        entry = cgt.retrieveEntry();
        assertEquals("wrong type", EntryType.UNDEFINED, entry.getType());
        assertEquals("wrong value", "-56", entry.getValue().toString());
    }
    
    /*----------------------------------------------------------------------------*/
   
    @Test
    public void testRetrieveBoolean() throws Exception {
        CGT cgt = new CGT();
        
        // Empty file
        cgt.open(new ByteArrayInputStream(new byte[] {}));
        try {
            cgt.retrieveBoolean();
            fail("expected IOException");
        } catch (IOException e) {
            assertEquals("wrong msg", "Invalid entry type. Expected BOOLEAN, but got EMPTY", e.getMessage());
        }
        
        // Sample data
        cgt.open(new ByteArrayInputStream(concat(HEADER, SAMPLE_BOOLEANS)));
        assertTrue(cgt.getNextRecord());
        assertEquals(true, cgt.retrieveBoolean());
        assertEquals(false, cgt.retrieveBoolean());
        assertEquals(true, cgt.retrieveBoolean());
        assertEquals(false, cgt.retrieveBoolean());
        validateExpectEMPTY(cgt);
    }
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testRetrieveByte() throws Exception {
        CGT cgt = new CGT();

        // Empty file
        cgt.open(new ByteArrayInputStream(new byte[] {}));
        try {
            cgt.retrieveByte();
            fail("expected IOException");
        } catch (IOException e) {
            assertEquals("wrong msg", "Invalid entry type. Expected BYTE, but got EMPTY", e.getMessage());
        }
        
        // Sample data
        cgt.open(new ByteArrayInputStream(concat(HEADER, SAMPLE_BYTES)));
        assertTrue(cgt.getNextRecord());
        assertEquals(1, cgt.retrieveByte());
        assertEquals(2, cgt.retrieveByte());
        assertEquals(3, cgt.retrieveByte());
        validateExpectEMPTY(cgt);
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testRetrieveInteger() throws Exception {
        CGT cgt = new CGT();

        // Empty file
        cgt.open(new ByteArrayInputStream(new byte[] {}));
        try {
            cgt.retrieveInteger();
            fail("expected IOException");
        } catch (IOException e) {
            assertEquals("wrong msg", "Invalid entry type. Expected UINT16, but got EMPTY", e.getMessage());
        }
        
        // Sample data
        cgt.open(new ByteArrayInputStream(concat(HEADER, SAMPLE_UINT16S)));
        assertTrue(cgt.getNextRecord());
        assertEquals(1, cgt.retrieveInteger());
        assertEquals(256, cgt.retrieveInteger());
        assertEquals(771, cgt.retrieveInteger());
        validateExpectEMPTY(cgt);
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testRetrieveString() throws Exception {
        CGT cgt = new CGT();
        
        // Empty file
        cgt.open(new ByteArrayInputStream(new byte[] {}));
        try {
            cgt.retrieveString();
            fail("expected IOException");
        } catch (IOException e) {
            assertEquals("wrong msg", "Invalid entry type. Expected STRING, but got EMPTY", e.getMessage());
        }
        
        // Sample data
        cgt.open(new ByteArrayInputStream(concat(HEADER, SAMPLE_STRINGS)));
        assertTrue(cgt.getNextRecord());
        assertEquals("wrong value", "Abc", cgt.retrieveString());
        assertEquals("wrong value", "D\u0117F", cgt.retrieveString());
        validateExpectEMPTY(cgt);
    }

}
