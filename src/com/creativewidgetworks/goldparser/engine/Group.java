package com.creativewidgetworks.goldparser.engine;

/**
 * Group  
 * Container for a group of symbols.
 *
 * Dependencies:
 * @see Symbol
 *
 * @author Devin Cook (http://www.DevinCook.com/GOLDParser)
 * @author Ralph Iden (http://www.creativewidgetworks.com), port to Java
 * @version 5.0 RC1 
 */
public class Group {
    private String name;
    private Symbol container;
    private Symbol start;
    private Symbol end;
    private boolean allowNesting;
    private boolean isTokenized;
    private boolean isOpenEnded;

    public String getName() {
        return name;
    }
    
    public Symbol getContainer() {
        return container;
    }
    
    public Symbol getStart() {
        return start;
    }
    
    public Symbol getEnd() {
        return end;
    }
    
    public boolean isAllowNesting() {
        return allowNesting;
    }
    
    public boolean isEndingToken(Token token) {
        return end == null ? false : end.getName().equals(token.getName());
    }
    
    public boolean isTokenized() {
        return isTokenized;
    }
    
    public boolean isOpenEnded() {
        return isOpenEnded;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setContainer(Symbol container) {
        this.container = container;
    }
    
    public void setStart(Symbol start) {
        this.start = start;
    }
    
    public void setEnd(Symbol end) {
        this.end = end;
    }
    
    public void setAllowNesting(boolean allowNesting) {
        this.allowNesting = allowNesting;
    }
    
    public void setTokenized(boolean isTokenized) {
        this.isTokenized = isTokenized;
    }
    
    public void setOpenEnded(boolean isOpenEnded) {
        this.isOpenEnded = isOpenEnded;
    }
}
