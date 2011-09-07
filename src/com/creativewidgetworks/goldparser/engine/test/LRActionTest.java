package com.creativewidgetworks.goldparser.engine.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.LRAction;
import com.creativewidgetworks.goldparser.engine.Symbol;
import com.creativewidgetworks.goldparser.engine.enums.LRActionType;
import com.creativewidgetworks.goldparser.engine.enums.SymbolType;

public class LRActionTest {

    @Test 
    public void testConstructorDefault() {
        Symbol symbol = new Symbol("Symbol1", SymbolType.NON_TERMINAL, 1);
        LRAction action = new LRAction(symbol, LRActionType.ACCEPT, 2);
        assertEquals("symbol", symbol, action.getSymbol());
        assertEquals("type", LRActionType.ACCEPT, action.getType());
        assertEquals("value", 2, action.getValue());
    }

}
