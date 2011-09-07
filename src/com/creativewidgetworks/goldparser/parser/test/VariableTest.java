package com.creativewidgetworks.goldparser.parser.test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.parser.Variable;

public class VariableTest extends TestCase {
    
    @Test
    public void testAsObject() throws Exception {
        // Null returns null
        assertNull("expected null value", new Variable(null).asObject());

        // Return expected type and value
        Object obj = new String("Hello");
        Variable var = new Variable(obj);
        assertNotNull("expected value", var.asObject());
        assertEquals("wrong value", obj.toString(), var.asObject().toString());
        
        // Uninitialized variable
        var = new Variable(null);
        assertNull("expected value", var.asObject());
        assertNull("expected value", var.asString());
        assertEquals("wrong value", "", var.toString());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testAsBoolean() throws Exception {
        // Null returns null
        assertNull("expected null value", new Variable(null).asBoolean());

        // Return expected type and value
        Variable var = new Variable(Boolean.TRUE);
        assertNotNull("expected value", var.asBoolean());
        assertEquals("wrong value", Boolean.TRUE, var.asBoolean()); 
        
        // String coerced into type
        var = new Variable("1");
        assertNotNull("expected value", var.asBoolean());
        assertEquals("wrong value", Boolean.TRUE, var.asBoolean()); 
        var = new Variable("true");
        assertNotNull("expected value", var.asBoolean());
        assertEquals("wrong value", Boolean.TRUE, var.asBoolean()); 
        var = new Variable("0");
        assertNotNull("expected value", var.asBoolean());
        assertEquals("wrong value", Boolean.FALSE, var.asBoolean()); 
        
        // Target type returns non-null value, all other types return null
        var = new Variable(Boolean.TRUE);
        assertNotNull("asBoolean", var.asBoolean());
        assertNull("asNumber", var.asNumber());
        assertNull("asString", var.asString()); 
        assertNull("asTimestamp", var.asTimestamp());
        
        // Target object not compatible with native type returns default value for type
        assertEquals("asBool", true, var.asBool());
        assertEquals("asDouble", Double.valueOf(Double.NaN), Double.valueOf(var.asDouble()));
        assertEquals("asInt", Integer.MIN_VALUE, var.asInt());
    }
    
    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testAsBool() throws Exception {
        // Null returns false
        assertEquals("asBool", false, new Variable(null).asBool());

        // Return expected type and value
        Variable var = new Variable(Boolean.TRUE);
        assertEquals("wrong value", true, var.asBool()); 
        
        // String coerced into type
        var = new Variable("1");
        assertEquals("wrong value", true, var.asBool()); 
        var = new Variable("true");
        assertEquals("wrong value", true, var.asBool()); 
        
        // Target type returns non-null value, all other types return null
        assertNotNull("asBoolean", var.asBoolean());
        assertNull("asNumber", var.asNumber());
        assertNotNull("asString", var.asString()); // coerced
        assertNull("asTimestamp", var.asTimestamp());
        
        // Target object not compatible with native type returns default value for type
        assertEquals("asBool", true, var.asBool());
        assertEquals("asDouble", Double.valueOf(Double.NaN), Double.valueOf(var.asDouble()));
        assertEquals("asInt", Integer.MIN_VALUE, var.asInt());
    }
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testAsDouble() throws Exception {
        // Null returns NAN
        assertEquals("NAN expected", Double.valueOf(Double.NaN), Double.valueOf(new Variable(null).asDouble()));

        // Return expected type and value
        Variable var = new Variable(new BigDecimal("1.23"));
        assertEquals("wrong value", Double.valueOf(1.23), Double.valueOf(var.asDouble())); 
        
        // String coerced into type
        var = new Variable("1.23");
        assertEquals("wrong value", Double.valueOf(1.23), Double.valueOf(var.asDouble())); 
        
        // Target type returns non-null value, all other types return null
        assertNull("asBoolean", var.asBoolean());
        assertNotNull("asNumber", var.asNumber());
        assertNotNull("asString", var.asString()); // coerced
        assertNull("asTimestamp", var.asTimestamp());
        
        // Target object not compatible with native type returns default value for type
        assertEquals("asBool", false, var.asBool());
        assertEquals("asDouble", Double.valueOf(1.23), Double.valueOf(var.asDouble()));
        assertEquals("asInt", 1, var.asInt());
    }
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testAsInt() throws Exception {
        // Null returns Integer.MIN_VALUE
        assertEquals("Integer.MIN_VALUE expected", Integer.MIN_VALUE, new Variable(null).asInt());

        // Return expected type and value
        Variable var = new Variable(new BigDecimal("3"));
        assertEquals("wrong value", 3, var.asInt()); 
        
        // String coerced into type
        var = new Variable("3");
        assertEquals("wrong value", 3, var.asInt()); 
        
        // Target type returns non-null value, all other types return null
        assertNull("asBoolean", var.asBoolean());
        assertNotNull("asNumber", var.asNumber());
        assertNotNull("asString", var.asString()); // coerced
        assertNull("asTimestamp", var.asTimestamp());
        
        // Target object not compatible with native type returns default value for type
        assertEquals("asBool", false, var.asBool());
        assertEquals("asDouble", Double.valueOf(3.00), Double.valueOf(var.asDouble()));
        assertEquals("asInt", 3, var.asInt());
    }
    
    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testAsNumber() throws Exception {
        // Null returns null
        assertNull("expected null", new Variable(null).asNumber());

        // Return expected type and value
        Variable var = new Variable(new BigDecimal("3.14159"));
        assertEquals("wrong value", new BigDecimal("3.14159"), var.asNumber()); 
        
        // String coerced into type
        var = new Variable("3.14159");
        assertEquals("wrong value", new BigDecimal("3.14159"), var.asNumber()); 
        
        // Target type returns non-null value, all other types return null
        assertNull("asBoolean", var.asBoolean());
        assertNotNull("asNumber", var.asNumber());
        assertNotNull("asString", var.asString()); // coerced
        assertNull("asTimestamp", var.asTimestamp());
        
        // Target object not compatible with native type returns default value for type
        assertEquals("asBool", false, var.asBool());
        assertEquals("asDouble", Double.valueOf(3.14159), Double.valueOf(var.asDouble()));
        assertEquals("asInt", 3, var.asInt());
    }  
    
    /*----------------------------------------------------------------------------*/
        
    @Test
    public void testAsString() throws Exception {
        // Null returns null
        assertNull("expected null", new Variable(null).asNumber());

        // Return expected type and value
        Variable var = new Variable("Hello, world");
        assertEquals("wrong value", "Hello, world", var.asString()); 
        
        // Target type returns non-null value, all other types return null
        assertNull("asBoolean", var.asBoolean());
        assertNull("asNumber", var.asNumber());
        assertNotNull("asString", var.asString());
        assertNull("asTimestamp", var.asTimestamp());
        
        // Target object not compatible with native type returns default value for type
        assertEquals("asBool", false, var.asBool());
        assertEquals("asDouble", Double.valueOf(Double.NaN), Double.valueOf(var.asDouble()));
        assertEquals("asInt", Integer.MIN_VALUE, var.asInt());
    }      

    /*----------------------------------------------------------------------------*/

    @Test
    public void testAsTimestamp() throws Exception {
        // Null returns null
        assertNull("expected null", new Variable(null).asTimestamp());

        // Return expected type and value
        Timestamp ts = new Timestamp(new Date().getTime());
        Variable var = new Variable(ts);
        assertEquals("wrong value", ts, var.asTimestamp()); 
        
        // String coerced into type
        var = new Variable(ts.toString());
        assertEquals("wrong value", ts, var.asTimestamp()); 
        
        // Target type returns non-null value, all other types return null
        assertNull("asBoolean", var.asBoolean());
        assertNull("asNumber", var.asNumber());
        assertNotNull("asString", var.asString()); // coerced
        assertNotNull("asTimestamp", var.asTimestamp());
        
        // Target object not compatible with native type returns default value for type
        assertEquals("asBool", false, var.asBool());
        assertEquals("asDouble", Double.valueOf(Double.NaN), Double.valueOf(var.asDouble()));
        assertEquals("asInt", Integer.MIN_VALUE, var.asInt());
    }        

    /*----------------------------------------------------------------------------*/

    @Test
    public void testToString() {
        Variable variable = new Variable(new BigDecimal("123.0"));
        assertEquals("wrong value", "123.0", variable.toString());
    }
    
}
