package com.creativewidgetworks.goldparser.engine.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.Position;

public class PositionTest {

    @Test
    public void testConstructor() {
        Position position = new Position(-1, 5);
        assertEquals("line", -1, position.getLine());
        assertEquals("lineAsString", "-1", position.getLineAsString());
        assertEquals("col", 5, position.getColumn());
        assertEquals("colAsString", "5", position.getColumnAsString());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testCopyConstructor() {
        Position anotherPosition = new Position(2, 30);

        Position position = new Position(anotherPosition);
        assertEquals("line", 2, position.getLine());
        assertEquals("lineAsString", "2", position.getLineAsString());
        assertEquals("col", 30, position.getColumn());
        assertEquals("colAsString", "30", position.getColumnAsString());
        
        // make sure it was a deep copy
        position.set(new Position(1, 1));
        assertEquals("line", 2, anotherPosition.getLine());
        assertEquals("lineAsString", "2", anotherPosition.getLineAsString());
        assertEquals("col", 30, anotherPosition.getColumn());
        assertEquals("colAsString", "30", anotherPosition.getColumnAsString());
    }
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testSet() {
        Position position = new Position(-1, 5);
        Position anotherPosition = new Position(2, 30);
        
        position.set(anotherPosition);
        
        assertEquals("line", 2, position.getLine());
        assertEquals("lineAsString", "2", position.getLineAsString());
        assertEquals("col", 30, position.getColumn());
        assertEquals("colAsString", "30", position.getColumnAsString());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testIncrementLine() {
        Position position = new Position(1, 15);
        for (int i = 0; i < 11; i++) {
            position.incrementLine(); // FYI: Row increments, column gets reset to 1
        }
        
        assertEquals("line", 12, position.getLine());
        assertEquals("lineAsString", "12", position.getLineAsString());
        assertEquals("col", 1, position.getColumn());
        assertEquals("colAsString", "1", position.getColumnAsString());        
    }
    
    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testIncrementColumn() {
        Position position = new Position(1, 1);
        for (int i = 0; i < 11; i++) {
            position.incrementColumn();
        }
        
        assertEquals("line", 1, position.getLine());
        assertEquals("lineAsString", "1", position.getLineAsString());
        assertEquals("col", 12, position.getColumn());
        assertEquals("colAsString", "12", position.getColumnAsString());        
    }

}
