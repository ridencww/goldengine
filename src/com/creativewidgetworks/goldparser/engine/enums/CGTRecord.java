package com.creativewidgetworks.goldparser.engine.enums;

/**
 * CGTRecord 
 *
 * CGT record type enumeration
 *
 * Dependencies: None
 *
 * @author Devin Cook (http://www.DevinCook.com/GOLDParser)
 * @author Ralph Iden (http://www.creativewidgetworks.com), port to Java
 * @version 5.0 RC1 
 */
public enum CGTRecord {
    ATTRIBUTES    (65),  // A
    PARAMETER     (80),  // P
    COUNTS        (84),  // T Table counts
    CHARSET       (67),  // C
    
    INITIALSTATES (73),  // I
    SYMBOL        (83),  // S
    RULE          (82),  // R Rule/related productions
    DFASTATE      (68),  // D
    LRSTATE       (76),  // L
    CHARRANGES    (99),  // c
    GROUP         (71),  // G
    
    PROPERTY      (112), // p (version 5 key/value property)
    COUNTS5       (116), // t
    
    UNDEFINED     (-1);
    
    private final int enumCode;

    CGTRecord(int code) {
        this.enumCode = code;
    }

    public int getCode() {
        return enumCode;
    }

    public static CGTRecord getCGTRecord(int code) {
        for (int i = 0; i < values().length; i++) {
            if (values()[i].enumCode == code) {
                return values()[i];
            }
        }
        return UNDEFINED;
    }

}

