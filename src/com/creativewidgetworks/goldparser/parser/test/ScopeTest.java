package com.creativewidgetworks.goldparser.parser.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.parser.Scope;

public class ScopeTest extends TestCase {

    @Test
    public void testDefaultConstructor() throws Exception {
        Scope scope = new Scope();
        assertEquals("wrong name", Scope.GLOBAL_SCOPE, scope.getName());
        assertNull("shouldn't have parent", scope.getParent());
        assertEquals("wrong size", 0, scope.getVariables().size());
    }
    
    /*----------------------------------------------------------------------------*/
    
    public void testConstructorWithScope() throws Exception {
        Scope scope = new Scope("test");
        assertEquals("wrong name", "test", scope.getName());
        assertNull("shouldn't have parent", scope.getParent());
        assertEquals("wrong size", 0, scope.getVariables().size());
    }

    /*----------------------------------------------------------------------------*/
    
    public void testConstructorWithScopeAndParent() throws Exception {
        Scope parent = new Scope("theParent");

        Scope scope = new Scope("child", parent);
        assertEquals("wrong name", "child", scope.getName());

        Scope p = scope.getParent();
        assertNotNull("should have parent", p);
        assertEquals("wrong parent", "theParent", p.getName());

        assertEquals("wrong size", 0, scope.getVariables().size());
    }

}
