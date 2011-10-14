package com.creativewidgetworks.goldparser.engine.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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

        List<Integer> list = new ArrayList<Integer>();
        
        group.setName("comment_block");
        group.setStart(token1);
        group.setEnd(token2);
        group.setContainer(container);
        group.setIndex(999);
        group.setNesting(list);
       
        assertEquals("name", "comment_block", group.getName());
        assertEquals("start", token1, group.getStart());
        assertEquals("end", token2, group.getEnd());
        assertEquals("container", container, group.getContainer());        
        assertEquals("index", 999, group.getIndex());
        assertEquals("nesting", list, group.getNesting());
    }

}
