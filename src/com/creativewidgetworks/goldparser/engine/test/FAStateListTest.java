package com.creativewidgetworks.goldparser.engine.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.FAStateList;
import com.creativewidgetworks.goldparser.engine.Symbol;
import com.creativewidgetworks.goldparser.engine.enums.SymbolType;

public class FAStateListTest extends TestCase {

    @Test
    public void testConstructorWithSizing() {
        FAStateList states = new FAStateList(5);
        assertEquals("wrong number of placeholders", 5, states.size());
        for (int i = 0; i < states.size(); i++) {
            assertNull(states.get(i));
        }
        assertNull("symbol", states.getErrorSymbol());
        assertEquals("initial state", 0, states.getInitialState());
    }
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testSetters() {
        Symbol symbol = new Symbol("ErrorSymbol", SymbolType.ERROR, 1);
        
        FAStateList states = new FAStateList(1);
        states.setInitialState(3);
        states.setErrorSymbol(symbol);
        
        assertEquals("initial state", 3, states.getInitialState());
        assertEquals("error symbol", symbol, states.getErrorSymbol());
    }

}
