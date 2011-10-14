package com.creativewidgetworks.goldparser.simple3.rulehandlers;

import java.math.BigDecimal;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.simple3.Simple3;

@ProcessRule(rule="<Negate Exp> ::= - <Value>")

/**
 * Rule handler for the negation rule.
 *
 * @author Ralph Iden (http://www.creativewidgetworks.com)
 * @version 5.0 RC2 
 */
public class Negation extends Reduction {
    private GOLDParser theParser;
    private Reduction valueToNegate;

    public Negation(GOLDParser parser) {
        theParser = parser;
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() == 2) {
                valueToNegate = reduction.get(1).asReduction();
            } else {
                parser.raiseParserException(Simple3.formatMessage("error.param_count", "2", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(Simple3.formatMessage("error.no_reduction"));
        }            
    }

    @Override
    public Variable getValue() throws ParserException {
        BigDecimal bd = valueToNegate.getValue().asNumber();
        if (bd == null) {
            theParser.raiseParserException(Simple3.formatMessage("error.negation_number_expected"));
        }

        return new Variable(bd.negate());
    }

}
