package com.creativewidgetworks.goldparser.simple2.rulehandlers;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.simple2.Simple2;


@ProcessRule(rule={
    "<Statement> ::= assign Id '=' <Expression>"    
 })

/**
 * Rule handler for the assignment rule.
 *
 * @author Ralph Iden (http://www.creativewidgetworks.com)
 * @version 5.0 RC2 
 */
public class Assign extends Reduction {
    private GOLDParser theParser;
    private String variableName;
    private Reduction variableValue;

    public Assign(GOLDParser parser) {
        theParser = parser;
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() == 4) {
                variableName = reduction.get(1).asString();
                variableValue = reduction.get(3).asReduction();
            } else {
                parser.raiseParserException(Simple2.formatMessage("error.param_count", "4", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(Simple2.formatMessage("error.no_reduction"));
        }
    }

    @Override
    public void execute() throws ParserException {
        theParser.setProgramVariable(variableName, variableValue.getValue());
    }

}
