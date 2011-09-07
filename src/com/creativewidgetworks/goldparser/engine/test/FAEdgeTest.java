package com.creativewidgetworks.goldparser.engine.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.CharacterSet;
import com.creativewidgetworks.goldparser.engine.FAEdge;

public class FAEdgeTest {

    @Test
    public void testConstructor() {
        CharacterSet set = new CharacterSet();
        FAEdge edge = new FAEdge(set, 3);
        assertEquals("set", set, edge.getChars());
        assertEquals("target", 3, edge.getTarget());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testSetters() {
        CharacterSet set1 = new CharacterSet();
        CharacterSet set2 = new CharacterSet();
        FAEdge edge = new FAEdge(set1, 3);
        
        edge.setChars(set2);
        edge.setTarget(7);
        
        assertEquals("set", set2, edge.getChars());
        assertEquals("target", 7, edge.getTarget());        
    }

}
