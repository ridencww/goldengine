package com.creativewidgetworks.goldparser.engine;

import com.creativewidgetworks.goldparser.engine.enums.SymbolType;

/**
 * Token  
 * 
 * While the Symbol represents a class of terminals and nonterminals, the
 * Token represents an individual piece of information.
 * 
 * Dependencies: 
 * @see Position
 * @see Symbol
 *
 * @author Devin Cook (http://www.DevinCook.com/GOLDParser)
 * @author Ralph Iden (http://www.creativewidgetworks.com), port to Java
 * @version 5.0 RC2 
 */
public class Token extends Symbol {
    private int state;
    private Object data;
    private Position position;

    public Token() {
        super();
        this.state = LRState.INITIAL_STATE;
    }

    /**
     * This constructor is used for the unit tests for the Rules processor
     * @param data 
     */
    public Token(Object data) {
        this(new Symbol(data == null ? "" : data.toString(), SymbolType.CONTENT, 0), data);
    }
    
    public Token(Symbol symbol, Object data) {
        this();
        this.data = data;
        this.name = symbol.name;
        this.type = symbol.type;
        this.tableIndex = symbol.tableIndex;
    }

    public void appendData(String moreData) {
        if (this.data == null || this.data instanceof String) {
            if (this.data == null) {
                this.data = "";
            }
            this.data = this.data.toString() + moreData;
        }
    }
    
    public Reduction asReduction() {
        return data instanceof Reduction ? (Reduction)data : null;
    }
    
    public String asString() {
        return data == null ? "" : data.toString();    
    }
    
    public int getState() {
        return state;
    }

    public Object getData() {
        return data;
    }

    public Position getPosition() {
        return position;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
    
    public void setSymbol(Symbol symbol) {
        setGroup(symbol.getGroup());
        setName(symbol.getName());
        setType(symbol.getType());
        setTableIndex(symbol.getTableIndex());  
    }
    
}