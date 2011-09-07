package com.creativewidgetworks.goldparser.engine.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.LRStateList;

public class LRStateListTest {

    @Test
    public void testConstructorWithSizing() {
        LRStateList states = new LRStateList(5);
        assertEquals("wrong number of placeholders", 5, states.size());
        for (int i = 0; i < states.size(); i++) {
            assertNull(states.get(i));
        }
        assertEquals("initial state", 0, states.getInitialState());
    }
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testSetters() {
        LRStateList states = new LRStateList(5);
        states.setInitialState(22);
        assertEquals("initial state", 22, states.getInitialState());
    }

}
