package com.creativewidgetworks.goldparser.engine.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.Production;
import com.creativewidgetworks.goldparser.engine.Symbol;
import com.creativewidgetworks.goldparser.engine.SymbolList;
import com.creativewidgetworks.goldparser.engine.enums.SymbolType;

public class ProductionTest {

    @Test
    public void testConstructor() {
        Symbol symbol = new Symbol("Symbol1", SymbolType.CONTENT, 2);
        Production production = new Production(symbol, 7);
        assertEquals("head", symbol, production.getHead());
        assertEquals("handle", 0, production.getHandle().size());
        assertEquals("index", 7, production.getTableIndex());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testSetters() {
        Symbol symbol = new Symbol("Symbol1", SymbolType.CONTENT, 2);
        Production production = new Production(symbol, 7);
        
        // Setting to null creates a new, empty list
        production.setHandle(null);
        assertNotNull("handle", production.getHandle());
        
        Symbol newSymbol = new Symbol("Symbol2", SymbolType.CONTENT, 2);
        production.setHead(newSymbol);
        
        SymbolList newList = new SymbolList(3);
        production.setHandle(newList);
        
        production.setTableIndex(44);
        
        assertEquals("head", newSymbol, production.getHead());
        assertEquals("handle", newList, production.getHandle());
        assertEquals("handle size", 3, production.getHandle().size());
        assertEquals("index", 44, production.getTableIndex());        
    }
    
    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testContainsOneNonTerminal() {
        Production production = new Production(new Symbol(), 2);
        
        assertFalse("null list", production.containsOneNonTerminal());
        
        production.setHandle(new SymbolList());
        assertFalse("empty list", production.containsOneNonTerminal());
        
        production.getHandle().clear();
        production.getHandle().add(new Symbol("Symbol1", SymbolType.NOISE, 1));
        assertFalse("one terminal symbol", production.containsOneNonTerminal());
        
        production.getHandle().clear();
        production.getHandle().add(new Symbol("Symbol1", SymbolType.NON_TERMINAL, 1));
        assertTrue("one non-terminal", production.containsOneNonTerminal());
        
        production.getHandle().add(new Symbol("Symbol2", SymbolType.NON_TERMINAL, 2));
        assertFalse("two non-terminals", production.containsOneNonTerminal());
        
        production.getHandle().clear();
        production.getHandle().add(new Symbol("Symbol1", SymbolType.NON_TERMINAL, 1));
        assertTrue("one non-terminal", production.containsOneNonTerminal());
        production.getHandle().add(new Symbol("Symbol2", SymbolType.NOISE, 2));
        assertFalse("one terminal/one non-terminal", production.containsOneNonTerminal());        
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testToString() {
        Symbol symbol = new Symbol("Symbol1", SymbolType.NON_TERMINAL, 2);
        
        SymbolList list = new SymbolList();
        Symbol symbol2 = new Symbol("Symbol2", SymbolType.NON_TERMINAL, 3);
        Symbol symbol3 = new Symbol("Symbol3", SymbolType.CONTENT, 4);
        list.add(symbol2);
        list.add(symbol3);
        
        Production production = new Production(symbol, 7);
        production.setHandle(list);
        assertEquals("wrong text", "<Symbol1> ::= <Symbol2> 'Symbol3'", production.toString());    
    }

}
