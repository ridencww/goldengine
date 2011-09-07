package com.creativewidgetworks.goldparser.engine.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.ProductionList;

public class ProductionListTest {

    @Test
    public void testConstructorWithSizing() {
        ProductionList productions = new ProductionList(5);
        assertEquals("wrong number of placeholders", 5, productions.size());
        for (int i = 0; i < productions.size(); i++) {
            assertNull(productions.get(i));
        }
    }

}
