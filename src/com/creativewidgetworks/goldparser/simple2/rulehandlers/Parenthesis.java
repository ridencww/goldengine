package com.creativewidgetworks.goldparser.simple2.rulehandlers;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;
import com.creativewidgetworks.goldparser.simple2.Simple2;

@ProcessRule(rule="<Value> ::= ( <Expression> )")

/**
 * Rule handler for the parenthesis rule.
 *
 * @author Ralph Iden (http://www.creativewidgetworks.com)
 * @version 5.0 RC2 
 */
public class Parenthesis extends Reduction {

    Reduction innerReduction;
    
    public Parenthesis(GOLDParser parser) {
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() == 3) {
                innerReduction = parser.getCurrentReduction().get(1).asReduction();
            } else {
                parser.raiseParserException(Simple2.formatMessage("error.param_count", "3", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(Simple2.formatMessage("error.no_reduction"));
        }           
    }

    @Override
    public Variable getValue() {
        return innerReduction.getValue();
    }
    
}
