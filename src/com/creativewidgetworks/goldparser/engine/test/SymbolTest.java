package com.creativewidgetworks.goldparser.engine.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.Group;
import com.creativewidgetworks.goldparser.engine.Symbol;
import com.creativewidgetworks.goldparser.engine.enums.SymbolType;

public class SymbolTest extends TestCase {

    @Test
    public void testSymbolDefaultConstructor() {
        Symbol symbol = new Symbol();
        assertNull("name", symbol.getName());
        assertNull("symbolType", symbol.getType());
        assertNull("group", symbol.getGroup());
        assertEquals("tableIndex", 0, symbol.getTableIndex());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testSymbolConstructor() {
        Symbol symbol = new Symbol("ExampleSymbol", SymbolType.NOISE, 5);
        assertEquals("name", "ExampleSymbol", symbol.getName());
        assertEquals("symbolType", SymbolType.NOISE, symbol.getType());
        assertNull("group", symbol.getGroup());
        assertEquals("tableIndex", 5, symbol.getTableIndex());        
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testSetters() {
        Group group = new Group();
        
        Symbol symbol = new Symbol();
        symbol.setName("TestSymbol");
        symbol.setType(SymbolType.NON_TERMINAL);
        symbol.setTableIndex(9);
        symbol.setGroup(group);
        
        assertEquals("name", "TestSymbol", symbol.getName());
        assertEquals("symbolType", SymbolType.NON_TERMINAL, symbol.getType());
        assertEquals("group", group, symbol.getGroup());
        assertEquals("tableIndex", 9, symbol.getTableIndex());   
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testEquals() {
        Symbol symbol1 = new Symbol();
        Symbol symbol2 = new Symbol();
        symbol1.hashCode();
        assertTrue(!symbol1.equals(symbol2));
    }
    
    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testToString() {
        int index = 0;
        for (SymbolType symbolType : SymbolType.values()) {
            index++;
            
            String name = "Symbol" + index;
            
            String expected;
            if (symbolType.equals(SymbolType.CONTENT)) {
                expected = "'" + name + "'";
            } else if (symbolType.equals(SymbolType.NON_TERMINAL)) {
                expected = "<" + name + ">";
            } else {
                expected = "(" + name + ")";
            }
            
            Symbol symbol = new Symbol(name, symbolType, index);
            assertEquals("wrong text", expected, symbol.toString());
        }
    }
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testToStringEscapingName() {
        // Tests to see if name field of CONTENT type is filtered or not
        Symbol symbol = new Symbol("SymbolName", null, 1);
        assertEquals("wrong text", "<not initialized>", symbol.toString(true));
        assertEquals("wrong text", "<not initialized>", symbol.toString(false));

        symbol = new Symbol(null, SymbolType.CONTENT, 1);
        assertEquals("wrong text", "null", symbol.toString(true));
        assertEquals("wrong text", "null", symbol.toString(false));
        
        symbol = new Symbol("SymbolName", SymbolType.CONTENT, 1);
        assertEquals("wrong text", "'SymbolName'", symbol.toString(true));
        assertEquals("wrong text", "SymbolName", symbol.toString(false));

        symbol = new Symbol("Symbol.Name", SymbolType.CONTENT, 1);
        assertEquals("wrong text", "'Symbol.Name'", symbol.toString(true));
        assertEquals("wrong text", "Symbol.Name", symbol.toString(false));

        symbol = new Symbol("Symbol_Name", SymbolType.CONTENT, 1);
        assertEquals("wrong text", "'Symbol_Name'", symbol.toString(true));
        assertEquals("wrong text", "Symbol_Name", symbol.toString(false));

        symbol = new Symbol("'", SymbolType.CONTENT, 1);
        assertEquals("wrong text", "''", symbol.toString(true));
        assertEquals("wrong text", "''", symbol.toString(false));        

        symbol = new Symbol("Symbol*Name", SymbolType.CONTENT, 1);
        assertEquals("wrong text", "'Symbol*Name'", symbol.toString(true));
        assertEquals("wrong text", "'Symbol*Name'", symbol.toString(false));        
    }

}
