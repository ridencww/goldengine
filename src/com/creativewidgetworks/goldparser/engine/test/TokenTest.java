package com.creativewidgetworks.goldparser.engine.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.Group;
import com.creativewidgetworks.goldparser.engine.LRState;
import com.creativewidgetworks.goldparser.engine.Position;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Symbol;
import com.creativewidgetworks.goldparser.engine.Token;
import com.creativewidgetworks.goldparser.engine.enums.SymbolType;

public class TokenTest {

    @Test
    public void testConstructorDefault() {
        Token token = new Token();
        assertNull("data", token.getData());
        assertNull("group", token.getGroup());
        assertNull("name", token.getName());
        assertNull("position", token.getPosition());
        assertEquals("wrong tableIndex", 0, token.getTableIndex());
        assertEquals("wrong state", LRState.INITIAL_STATE, token.getState());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testConstructor() {
        Symbol symbol = new Symbol("Symbol1", SymbolType.CONTENT, 1);
        Token token = new Token(symbol, "1234");
        assertNull("group", token.getGroup());
        assertNull("position", token.getPosition());
        assertEquals("name", "Symbol1", token.getName());
        assertEquals("data", "1234", token.getData());
        assertEquals("wrong tableIndex", 1, token.getTableIndex());
        assertEquals("wrong state", LRState.INITIAL_STATE, token.getState());        
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testConstructorForTests() {
        Token token = new Token(null);
        assertNull("group", token.getGroup());
        assertNull("position", token.getPosition());
        assertNull("data", token.getData());
        assertEquals("name", "", token.getName());
        assertEquals("wrong tableIndex", 0, token.getTableIndex());
        assertEquals("wrong state", LRState.INITIAL_STATE, token.getState());        
        
        token = new Token("1234");
        assertNull("group", token.getGroup());
        assertNull("position", token.getPosition());
        assertEquals("name", "1234", token.getName());
        assertEquals("data", "1234", token.getData());
        assertEquals("wrong tableIndex", 0, token.getTableIndex());
        assertEquals("wrong state", LRState.INITIAL_STATE, token.getState());        
    }
    
    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testAppendData() {
        // Appending null adds "null" to end for diagnostics
        Token token = new Token(new Symbol(), "1234");
        token.appendData(null);
        assertEquals("wrong text", "1234null", token.getData().toString());

        // Appending data to null data creates a String object
        token = new Token();
        token.appendData("1234");
        assertEquals("wrong text", "1234", token.getData().toString());
        
        // Appending data to a Reduction does nothing
        Reduction reduction = new Reduction();
        token = new Token();
        token.setData(reduction);
        token.appendData("ABC");
        assertEquals("should have left reduction untouched", reduction, token.getData());
        
        // Normal data appending
        token = new Token(new Symbol(), "1234");
        assertEquals("wrong text", "1234", token.getData().toString());
        token.appendData("5678");
        assertEquals("wrong text", "12345678", token.getData().toString());        
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testAsReduction() {
        Reduction reduction = new Reduction();

        // Should return the reduction
        Token token = new Token();
        token.setData(reduction);
        assertEquals("should have returned reduction", reduction, token.asReduction());
        
        // asReduction with a non-reduction object returns null
        token = new Token();
        token.setData(null);
        assertNull("should return null", token.asReduction());
        token.setData("Hello");
        assertNull("should return null", token.asReduction());
        token.setData(new Token());
        assertNull("should return null", token.asReduction());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testAsString() {
        Reduction reduction = new Reduction();

        // asString with a non-reduction object returns data.toString() representation
        Token token = new Token();
        token.setData(null);
        assertEquals("wrong text", "", token.asString());
        token.setData(new Token());
        assertEquals("wrong text", "<not initialized>", token.asString());
        token.setData(reduction);
        assertEquals("wrong text", "[]", token.asString());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testSetState() {
        Token token = new Token();
        assertEquals("wrong state", LRState.INITIAL_STATE, token.getState()); 
        token.setState(33);
        assertEquals("wrong state", 33, token.getState()); 
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testSetPosition() {
        Position newPosition = new Position(2,4);
        
        Token token = new Token(new Symbol(), null);
        assertNull(token.getPosition());
        token.setPosition(newPosition);
        assertEquals("wrong row", newPosition.getLine(), token.getPosition().getLine());
        assertEquals("wrong col", newPosition.getColumn(), token.getPosition().getColumn());
        
        // null clears position
        token.setPosition(null);
        assertNull(token.getPosition());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testSetSymbol() {
        Group group = new Group();
        Symbol symbol = new Symbol("NewSymbol", SymbolType.NOISE, 3);
        symbol.setGroup(group);

        Token token = new Token(new Symbol(), null);
        token.setSymbol(symbol);
        assertEquals("name", "NewSymbol", token.getName());
        assertEquals("group", group, token.getGroup());
        assertEquals("wrong tableIndex", 3, token.getTableIndex());
        assertEquals("wrong state", LRState.INITIAL_STATE, token.getState());   
    }    
    
}
