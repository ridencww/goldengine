package com.creativewidgetworks.goldparser.engine;

/**
 * Position 
 *
 * Holds a line and column number pair.
 * 
 * Dependencies: None
 *
 * @author Devin Cook (http://www.DevinCook.com/GOLDParser)
 * @author Ralph Iden (http://www.creativewidgetworks.com), port to Java
 * @version 5.0 RC2
 */
public class Position {
    private int line;
    private int column;

    public Position(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public Position(Position position) {
        this.line = position.line;
        this.column = position.column;
    }

    public void incrementLine() {
        line++;
        column = 1;
    }
    
    public void incrementColumn() {
        column++;
    }
    
    public void set(Position newPosition) {
        if (newPosition != null) {
            this.line = newPosition.getLine();
            this.column = newPosition.getColumn();
        }
    }
  
    public int getColumn() {
        return column;
    }    
    
    public String getColumnAsString() {
        return String.valueOf(column);
    }
    
    public int getLine() {
        return line;
    }
    
    public String getLineAsString() {
        return String.valueOf(line);
    }
}