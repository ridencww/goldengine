package com.creativewidgetworks.goldparser.engine.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.Group;
import com.creativewidgetworks.goldparser.engine.Symbol;
import com.creativewidgetworks.goldparser.engine.Token;
import com.creativewidgetworks.goldparser.engine.enums.SymbolType;

public class GroupTest {

    @Test 
    public void testGettersSetters() {
        Group group = new Group();
        assertNull("name", group.getName());
        assertNull("start", group.getStart());
        assertNull("end", group.getEnd());
        assertNull("container", group.getContainer());
        
        Token container = new Token();
        Token token1 = new Token(new Symbol("/*", SymbolType.GROUP_START, 1), "/*");
        Token token2 = new Token(new Symbol("*/", SymbolType.GROUP_END, 2), "*/");
        
        group.setName("comment_block");
        group.setStart(token1);
        group.setEnd(token2);
        group.setContainer(container);
        
        assertEquals("name", "comment_block", group.getName());
        assertEquals("start", token1, group.getStart());
        assertEquals("end", token2, group.getEnd());
        assertEquals("container", container, group.getContainer());        
    }
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testIsEndingToken() {
        Group group = new Group();
        Token token1 = new Token(new Symbol("GROUP_END", SymbolType.GROUP_END, 1), "*/");
        Token token2 = new Token(new Symbol("COMMENT_LINE", SymbolType.COMMENT_LINE, 2), "//");

        assertFalse("null token", group.isEndingToken(null));
        assertFalse("end is null", group.isEndingToken(token1));
        group.setEnd(token1);
        assertFalse("no match", group.isEndingToken(token2));
        assertTrue("match", group.isEndingToken(token1));
    }

}
