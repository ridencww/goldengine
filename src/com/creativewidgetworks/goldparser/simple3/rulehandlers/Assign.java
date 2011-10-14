package com.creativewidgetworks.goldparser.simple3.rulehandlers;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.simple3.Simple3;

@ProcessRule(rule={
    "<Statement> ::= Id '=' <Expression>"    
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
            if (reduction.size() == 3) {
                variableName = reduction.get(0).asString();
                variableValue = reduction.get(2).asReduction();
            } else {
                parser.raiseParserException(Simple3.formatMessage("error.param_count", "3", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(Simple3.formatMessage("error.no_reduction"));
        }
    }

    @Override
    public void execute() throws ParserException {
        theParser.setProgramVariable(variableName, variableValue.getValue());
    }

}
