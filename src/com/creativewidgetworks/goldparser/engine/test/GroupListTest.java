package com.creativewidgetworks.goldparser.engine.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.GroupList;

public class GroupListTest {

    @Test
    public void testConstructorDefault() {
        GroupList groups = new GroupList();
        assertEquals("wrong size", 0, groups.size());
    }
    
    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testConstructorWithSizing() {
        GroupList groups = new GroupList(5);
        assertEquals("wrong number of placeholders", 5, groups.size());
        for (int i = 0; i < groups.size(); i++) {
            assertNull(groups.get(i));
        }
    }

}
