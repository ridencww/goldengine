package com.creativewidgetworks.goldparser.simple3.rulehandlers;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;
import com.creativewidgetworks.goldparser.simple3.Simple3;

@ProcessRule(rule="<Value> ::= Id")

/**
 * Rule handler for the assign value of a Variable rule.
 *
 * @author Ralph Iden (http://www.creativewidgetworks.com)
 * @version 5.0 RC2 
 */
public class Id extends Reduction {
    private GOLDParser theParser;
    private String variableName;

    public Id(GOLDParser parser) {
        theParser = parser;
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() == 1) {
                variableName = reduction.get(0).asString();
            } else {
                parser.raiseParserException(Simple3.formatMessage("error.param_count", "1", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(Simple3.formatMessage("error.no_reduction"));
        }        
    }

    public String getVariableName() {
        return variableName;
    }
    
    @Override
    public Variable getValue() {
        Variable var = theParser.getProgramVariable(variableName);
        return var == null ? new Variable("") : var;
    }
    
    @Override
    public String toString() {
        return variableName + "=" + getValue();
    }

}
