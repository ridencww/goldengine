package com.creativewidgetworks.goldparser.engine;

import java.util.ArrayList;

/**
 * ProductionList
 *
 * Manages a list of Production objects.
 *
 * Dependencies: 
 * @Production
 *
 * @author Devin Cook (http://www.DevinCook.com/GOLDParser)
 * @author Ralph Iden (http://www.creativewidgetworks.com), port to Java
 * @version 5.0.0 
 */
public class ProductionList extends ArrayList<Production> {

    /**
     * Constructor that establishes the size of the list and creates placeholder
     * objects so the list can be accessed in a "random" fashion when setting
     * items.
     * @param size
     */    
    public ProductionList(int size) {
        super(size);
        for (int i = 0; i < size; i++) {
            add(null);
        }
    }    
}
