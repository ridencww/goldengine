package com.creativewidgetworks.goldparser.engine.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.CharacterSet;
import com.creativewidgetworks.goldparser.engine.FAEdge;
import com.creativewidgetworks.goldparser.engine.FAState;
import com.creativewidgetworks.goldparser.engine.Symbol;
import com.creativewidgetworks.goldparser.engine.enums.SymbolType;

public class FAStateTest {

    @Test
    public void testConstuctorDefault() {
        FAState state = new FAState();
        assertNotNull("edges", state.getEdges());
        assertEquals("edges size", 0, state.getEdges().size());
        assertNull("accept", state.getAccept());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testConstructorSymbol() {
        Symbol symbol = new Symbol("Symbol1", SymbolType.NOISE, 2);
        FAState state = new FAState(symbol);
        assertNotNull("edges", state.getEdges());
        assertEquals("edges size", 0, state.getEdges().size());
        assertEquals("accept", symbol, state.getAccept());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testAddEdges() {
        FAState state = new FAState();
        assertEquals("edges size", 0, state.getEdges().size());
        state.getEdges().add(new FAEdge(new CharacterSet(), 4));
        state.getEdges().add(new FAEdge(new CharacterSet(), 5));
        assertEquals("edges size", 2, state.getEdges().size());
    }
    
}
