package com.creativewidgetworks.goldparser.engine.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.Symbol;
import com.creativewidgetworks.goldparser.engine.SymbolList;
import com.creativewidgetworks.goldparser.engine.enums.SymbolType;

public class SymbolListTest {

    @Test
    public void testListConstructor() {
        SymbolList symbols = new SymbolList();
        assertEquals("should be empty", 0, symbols.size());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testListConstructorWithSizing() {
        SymbolList symbols = new SymbolList(5);
        assertEquals("wrong number of placeholders", 5, symbols.size());
        for (int i = 0; i < symbols.size(); i++) {
            assertNull(symbols.get(i));
        }
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testFindByName() {
        SymbolList symbols = new SymbolList();
        symbols.add(new Symbol("Symbol1", SymbolType.CONTENT, 1));
        symbols.add(new Symbol("Symbol2", SymbolType.CONTENT, 2));
        symbols.add(new Symbol("Symbol3", SymbolType.CONTENT, 3));
        
        Symbol symbol;
        
        symbol = symbols.findByName(null);
        assertNull(symbol);

        symbol = symbols.findByName("");
        assertNull(symbol);

        symbol = symbols.findByName("Symbol0");
        assertNull(symbol);
        
        for (int i = 1; i <= 3; i++) {
            symbol = symbols.findByName("Symbol" + i);
            assertNotNull("should have found Symbol" + i, symbol);
            assertEquals("wrong symbol", "'Symbol" + i +"'", symbol.toString());
        }
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testToString() {
        SymbolList symbols = new SymbolList();
        symbols.add(new Symbol("Symbol1", SymbolType.CONTENT, 1));
        symbols.add(new Symbol("Symbol2", SymbolType.CONTENT, 2));
        symbols.add(new Symbol("Symbol3", SymbolType.CONTENT, 3));
        assertEquals("wrong text", "'Symbol1' 'Symbol2' 'Symbol3'", symbols.toString());
    }

}
