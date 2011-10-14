package com.creativewidgetworks.goldparser.simple3.rulehandlers;

import java.math.RoundingMode;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;
import com.creativewidgetworks.goldparser.simple3.Simple3;

@ProcessRule(rule={
    "<Expression> ::= <Expression> > <Add Exp>",
    "<Expression> ::= <Expression> < <Add Exp>",
    "<Expression> ::= <Expression> <= <Add Exp>",
    "<Expression> ::= <Expression> >= <Add Exp>",
    "<Expression> ::= <Expression> '==' <Add Exp>",
    "<Expression> ::= <Expression> <> <Add Exp>",
    "<Expression> ::= <Add Exp>",
    "<Add Exp> ::= <Add Exp> '+' <Mult Exp>",
    "<Add Exp> ::= <Add Exp> - <Mult Exp>",
    "<Add Exp> ::= <Add Exp> & <Mult Exp>",
    "<Add Exp> ::= <Mult Exp>",
    "<Mult Exp> ::= <Mult Exp> '*' <Negate Exp>",
    "<Mult Exp> ::= <Mult Exp> '/' <Negate Exp>",
    "<Mult Exp> ::= <Negate Exp>",
    "<Negate Exp> ::= <Value>"
})

/**
 * Rule handler for the expression rules.
 *
 * @author Ralph Iden (http://www.creativewidgetworks.com)
 * @version 5.0 RC2 
 */
public class Expression extends Reduction {
    private static final int PRECISION = 5; 
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final String validOperators = "== <> < <= > >= + - * / & ";
    
    private GOLDParser theParser;
    private String theOperator;
    private Reduction leftExpression;
    private Reduction rightExpression;

    public Expression(GOLDParser parser) {
        theParser = parser;
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() == 3) {
                leftExpression  = reduction.get(0).asReduction();
                theOperator     = reduction.get(1).asString();
                rightExpression = reduction.get(2).asReduction();  
                if (validOperators.indexOf(theOperator + " ") == -1) {
                    parser.raiseParserException(Simple3.formatMessage("error.invalid_operator", validOperators, theOperator));
                }
            } else {
                parser.raiseParserException(Simple3.formatMessage("error.param_count", "3", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(Simple3.formatMessage("error.no_reduction"));
        }        
    }

    @Override
    public Variable getValue() throws ParserException {
        Variable result = null;
        Variable lValue = leftExpression.getValue();
        Variable rValue = rightExpression.getValue();

        boolean b = false;
        if (theOperator.equals("==")) {
           if (bothValuesAreNumbers()) {
               b = lValue.asNumber().compareTo(rValue.asNumber()) == 0;
               result = new Variable(Boolean.valueOf(b));
           } else if (bothValuesAreBooleans()) {
               b = lValue.asBool() == rValue.asBool();
               result = new Variable(Boolean.valueOf(b));
           } else if (bothValuesAreTimestamps()) {
               b = lValue.asTimestamp().compareTo(rValue.asTimestamp()) == 0;
               result = new Variable(Boolean.valueOf(b));
           } else if (oneOrBothValuesAreStrings()) {
               b = lValue.toString().compareTo(rValue.toString()) == 0;
               result = new Variable(Boolean.valueOf(b));
           } else {
               theParser.raiseParserException(Simple3.formatMessage("error.type_mismatch"));
           }
        } else if (theOperator.equals("<>")) {
            if (bothValuesAreNumbers()) {
                b = lValue.asNumber().compareTo(rValue.asNumber()) != 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (bothValuesAreBooleans()) {
                b = lValue.asBool() != rValue.asBool();
                result = new Variable(Boolean.valueOf(b));
            } else if (bothValuesAreTimestamps()) {
                b = lValue.asTimestamp().compareTo(rValue.asTimestamp()) != 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (oneOrBothValuesAreStrings()) {
                b = lValue.toString().compareTo(rValue.toString()) != 0;
                result = new Variable(Boolean.valueOf(b));
            } else {
                theParser.raiseParserException(Simple3.formatMessage("error.type_mismatch"));
            }
        } else if (theOperator.equals("<")) {
            if (bothValuesAreNumbers()) {
                b = lValue.asNumber().compareTo(rValue.asNumber()) < 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (bothValuesAreTimestamps()) {
                b = lValue.asTimestamp().compareTo(rValue.asTimestamp()) < 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (oneOrBothValuesAreStrings()) {
                b = lValue.toString().compareTo(rValue.toString()) < 0;
                result = new Variable(Boolean.valueOf(b));
            } else {
                theParser.raiseParserException(Simple3.formatMessage("error.type_mismatch"));
            }
        } else if (theOperator.equals("<=")) {
            if (bothValuesAreNumbers()) {
                b = lValue.asNumber().compareTo(rValue.asNumber()) <= 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (bothValuesAreTimestamps()) {
                b = lValue.asTimestamp().compareTo(rValue.asTimestamp()) <= 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (oneOrBothValuesAreStrings()) {
                b = lValue.toString().compareTo(rValue.toString()) <= 0;
                result = new Variable(Boolean.valueOf(b));
            } else {
                theParser.raiseParserException(Simple3.formatMessage("error.type_mismatch"));
            }
        } else if (theOperator.equals(">")) {
            if (bothValuesAreNumbers()) {
                b = lValue.asNumber().compareTo(rValue.asNumber()) > 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (bothValuesAreTimestamps()) {
                b = lValue.asTimestamp().compareTo(rValue.asTimestamp()) > 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (oneOrBothValuesAreStrings()) {
                b = lValue.toString().compareTo(rValue.toString()) > 0;
                result = new Variable(Boolean.valueOf(b));
            } else {
                theParser.raiseParserException(Simple3.formatMessage("error.type_mismatch"));
            }
        } else if (theOperator.equals(">=")) {
            if (bothValuesAreNumbers()) {
                b = lValue.asNumber().compareTo(rValue.asNumber()) >= 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (bothValuesAreTimestamps()) {
                b = lValue.asTimestamp().compareTo(rValue.asTimestamp()) >= 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (oneOrBothValuesAreStrings()) {
                b = lValue.toString().compareTo(rValue.toString()) >= 0;
                result = new Variable(Boolean.valueOf(b));
            } else {
                theParser.raiseParserException(Simple3.formatMessage("error.type_mismatch"));
            }
        } else if (theOperator.equals("+")) {
            if (bothValuesAreNumbers()) {
                result = new Variable(lValue.asNumber().add(rValue.asNumber()));
            } else {
                // I prefer to overload + depending upon type 
                result = new Variable(lValue.toString() + rValue.toString()); 
            }
        } else if (theOperator.equals("-")) {
            if (bothValuesAreNumbers()) {
                result = new Variable(lValue.asNumber().subtract(rValue.asNumber()));
            } else {
                theParser.raiseParserException(Simple3.formatMessage("error.type_mismatch"));
            }
        } else if (theOperator.equals("&")) {
            // String concatenation
            if (oneOrBothValuesAreStrings()) {
                result = new Variable(lValue.toString() + rValue.toString());
            } else {
                theParser.raiseParserException(Simple3.formatMessage("error.type_mismatch"));
            }
        } else if (theOperator.equals("*")) {
            if (bothValuesAreNumbers()) {
                result = new Variable(lValue.asNumber().multiply(rValue.asNumber()));
            } else {
                theParser.raiseParserException(Simple3.formatMessage("error.type_mismatch"));
            }
        } else if (theOperator.equals("/")) {
            if (bothValuesAreNumbers()) {
                try {
                    // This path will strip off extraneous decimal places, but will fail 
                    // with numbers that repeat (e.g.,  1/3 = .33333333333)
                    result = new Variable(lValue.asNumber().divide(rValue.asNumber()));
                } catch (Exception e) {
                    // This path will handle the repeating numbers
                    result = new Variable(lValue.asNumber().divide(rValue.asNumber(),PRECISION, ROUNDING_MODE));
                }
            } else {
                theParser.raiseParserException(Simple3.formatMessage("error.type_mismatch"));
            }
        }

        return result;
    }

    /*----------------------------------------------------------------------------*/

    public boolean bothValuesAreBooleans() throws ParserException {
        return leftExpression.getValue().asBoolean() != null && rightExpression.getValue().asBoolean() != null;
    }

    public boolean bothValuesAreNumbers() throws ParserException {
        return leftExpression.getValue().asNumber() != null && rightExpression.getValue().asNumber() != null;
    }

    public boolean bothValuesAreTimestamps() throws ParserException {
        return leftExpression.getValue().asTimestamp() != null && rightExpression.getValue().asTimestamp() != null;
    }

    public boolean oneOrBothValuesAreStrings() throws ParserException {
        return leftExpression.getValue().asString() != null || rightExpression.getValue().asString() != null;
    }
}
