package com.creativewidgetworks.goldparser.engine.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.LRAction;
import com.creativewidgetworks.goldparser.engine.LRState;
import com.creativewidgetworks.goldparser.engine.Symbol;
import com.creativewidgetworks.goldparser.engine.enums.LRActionType;
import com.creativewidgetworks.goldparser.engine.enums.SymbolType;

public class LRStateTest {

    @Test 
    public void testConstructorDefault() {
        LRState state = new LRState();
        assertEquals("wrong size", 0, state.size());
    }
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testConstructorWithSizing() {
        LRState state = new LRState(5);
        assertEquals("wrong number of placeholders", 5, state.size());
        for (int i = 0; i < state.size(); i++) {
            assertNull(state.get(i));
        }
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testDefaultInitialState() {
        assertEquals("default initial state", 0, LRState.INITIAL_STATE);
    }
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testFind() {
        Symbol symbol1 = new Symbol("Symbol1", SymbolType.NON_TERMINAL, 1);
        Symbol symbol2 = new Symbol("Symbol2", SymbolType.NON_TERMINAL, 2);
        Symbol symbol3 = new Symbol("Symbol3", SymbolType.NON_TERMINAL, 3);
        Symbol symbol4 = new Symbol("Symbol4", SymbolType.NON_TERMINAL, 4);
        
        LRAction action1 = new LRAction(symbol1, LRActionType.ACCEPT, 11);
        LRAction action2 = new LRAction(symbol2, LRActionType.ACCEPT, 12);
        LRAction action3 = new LRAction(symbol3, LRActionType.ACCEPT, 13);
        
        LRState state = new LRState(3);
        state.set(0, action3);
        state.set(1, action2);
        state.set(2, action1);
        
        assertEquals("null symbol", LRState.LRACTION_UNDEFINED, state.find(null));
        assertEquals("not found", LRState.LRACTION_UNDEFINED, state.find(symbol4));
        assertEquals("action1", action1, state.find(symbol1));
        assertEquals("action2", action2, state.find(symbol2));
        assertEquals("action3", action3, state.find(symbol3));
    }
    
}
