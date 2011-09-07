package com.creativewidgetworks.goldparser.engine.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.Production;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Symbol;
import com.creativewidgetworks.goldparser.parser.Variable;

public class ReductionTest {

    @Test
    public void testConstructor() {
        Reduction reduction = new Reduction();
        assertNull("parent", reduction.getParent());
        assertNull("variable", reduction.getValue());
        assertEquals("should be empty", 0, reduction.size());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testConstructorWithSizing() {
        Reduction reduction = new Reduction(5);
        assertEquals("wrong number of placeholders", 5, reduction.size());
        for (int i = 0; i < reduction.size(); i++) {
            assertNull(reduction.get(i));
        }
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testExecute() {
        // Default execute() does nothing, but it still should be able to be called without error
        Reduction reduction = new Reduction();
        reduction.execute();  
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testSetParent() {
        Production production = new Production(new Symbol(), 5);
        Reduction reduction = new Reduction();
        reduction.setParent(production);
        assertEquals("wrong value", production, reduction.getParent());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testSetValue() {
        Variable variable = new Variable("Hello");
        Reduction reduction = new Reduction();
        assertNull(reduction.getValue());
        reduction.setValue(variable);
        assertEquals("wrong value", variable, reduction.getValue());
    }

}
